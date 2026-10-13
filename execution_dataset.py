"""
Module 4, Part 6: Model-Ready PyTorch Dataset, Padding, Masks & DataLoader.

Implements:
  1. ExecutionTraceDataset: PyTorch Dataset mapping raw Java encoder inputs and
     event-preserving runtime trace chunks.
  2. Memory-safe indexing: Pre-indexes line offsets and chunk counts to avoid
     materializing 113+ million tokens in RAM.
  3. Strict raw Java loading: Reads selected source code/{program_id}.java and never
     uses the SPR JSON dictionary in JSONL "source".
  4. Teacher-forcing sequence pairs: [BOS + chunk] -> [chunk + EOS].
  5. Collate function: Dynamic batch padding, padding masks (True = ignored),
     and loss masks (False = ignored).
  6. DataLoader factory with deterministic validation/test ordering.
"""

from bisect import bisect_right
from dataclasses import dataclass
import json
import os
from pathlib import Path
from typing import Any, Callable, Dict, List, Optional, Sequence, Tuple, Union

import torch
from torch.utils.data import DataLoader, Dataset

from chunk_trace import ChunkingResult, OversizedEventError, chunk_trace_tokens
from source_tokenizer import tokenize_java_code
from tokenize_and_analyze import tokenize_runtime
from vocabulary import Vocabulary


# ============================================================
# DATA CONTAINERS
# ============================================================

@dataclass
class DatasetItem:
    """A single unpadded training/evaluation example."""
    program_id: str
    chunk_index: int
    total_chunks: int
    source_tokens: List[str]
    source_ids: List[int]
    chunk_tokens: List[str]
    chunk_ids: List[int]
    decoder_input_ids: List[int]
    target_ids: List[int]


@dataclass
class BatchData:
    """A model-ready collated batch of padded tensors and metadata."""
    program_ids: List[str]
    chunk_indices: torch.Tensor          # (B,) torch.long
    total_chunks: torch.Tensor           # (B,) torch.long
    src_ids: torch.Tensor                # (B, S_max) torch.long
    src_padding_mask: torch.Tensor       # (B, S_max) torch.bool (True = padding/ignored)
    decoder_input_ids: torch.Tensor      # (B, T_max) torch.long
    target_ids: torch.Tensor             # (B, T_max) torch.long
    target_padding_mask: torch.Tensor    # (B, T_max) torch.bool (True = padding/ignored)
    loss_mask: torch.Tensor              # (B, T_max) torch.bool (True = active loss, False = padding)
    src_lengths: torch.Tensor            # (B,) torch.long
    target_lengths: torch.Tensor         # (B,) torch.long

    def to(self, device: Union[str, torch.device]) -> "BatchData":
        """Moves all tensor fields to the target device."""
        return BatchData(
            program_ids=self.program_ids,
            chunk_indices=self.chunk_indices.to(device),
            total_chunks=self.total_chunks.to(device),
            src_ids=self.src_ids.to(device),
            src_padding_mask=self.src_padding_mask.to(device),
            decoder_input_ids=self.decoder_input_ids.to(device),
            target_ids=self.target_ids.to(device),
            target_padding_mask=self.target_padding_mask.to(device),
            loss_mask=self.loss_mask.to(device),
            src_lengths=self.src_lengths.to(device),
            target_lengths=self.target_lengths.to(device),
        )

    def __getitem__(self, key: str) -> Any:
        """Enables dict-like subscripting for compatibility."""
        return getattr(self, key)


# ============================================================
# PYTORCH DATASET IMPLEMENTATION
# ============================================================

class ExecutionTraceDataset(Dataset):
    """
    Model-ready PyTorch Dataset for paired Java source code and execution trace chunks.

    Guarantees:
      1. Encoder input is loaded strictly from raw Java source files (selected source code/{pid}.java).
         The SPR dictionary in JSONL "source" is NEVER used.
      2. Runtime targets are tokenized via tokenize_runtime() and partitioned into atomic
         event-preserving chunks via chunk_trace_tokens().
      3. Chunks never cross program boundaries; all chunks from program P remain in P's split.
      4. Single oversized events exceeding max_chunk_size are preserved in metadata under
         oversized_policy='separate', or handled per configured policy ('keep', 'error').
      5. Full teacher-forcing sequence pairs are constructed:
         decoder_input_ids: [BOS] + chunk_ids
         target_ids:        chunk_ids + [EOS]
      6. Memory safety: Uses indexed offset caching with an LRU trace buffer to prevent
         holding hundreds of millions of tokens in unmanaged memory.
    """

    def __init__(
        self,
        jsonl_path: Union[str, Path],
        java_dir: Union[str, Path],
        source_vocab: Vocabulary,
        target_vocab: Vocabulary,
        max_chunk_size: int = 1024,
        oversized_policy: str = "separate",
        max_source_length: Optional[int] = None,
        cache_records: int = 16,
    ) -> None:
        self.jsonl_path = Path(jsonl_path).resolve()
        self.java_dir = Path(java_dir).resolve()
        self.source_vocab = source_vocab
        self.target_vocab = target_vocab
        self.max_chunk_size = max_chunk_size
        self.oversized_policy = oversized_policy
        self.max_source_length = max_source_length
        self.cache_records = max(1, cache_records)

        if not self.jsonl_path.is_file():
            raise FileNotFoundError(f"Dataset JSONL file not found: {self.jsonl_path}")
        if not self.java_dir.is_dir():
            raise FileNotFoundError(f"Java source directory not found: {self.java_dir}")

        # Metadata collected during indexing
        self.record_offsets: List[int] = []
        self.program_ids: List[str] = []
        self.chunks_per_record: List[int] = []
        self.cum_chunk_counts: List[int] = []
        self.total_chunks: int = 0
        self.total_oversized_events: int = 0
        self.oversized_metadata: List[Dict[str, Any]] = []

        # In-memory source cache: 449-562 programs takes <3 MB RAM
        self._source_cache: Dict[str, Tuple[List[str], List[int]]] = {}

        # Trace chunk LRU cache: maps record_idx -> list of chunk token lists
        self._trace_cache: Dict[int, List[List[str]]] = {}
        self._trace_cache_order: List[int] = []

        # Build index
        self._build_index()

    def _build_index(self) -> None:
        """
        Scans JSONL file to record byte offsets and calculate chunk counts.
        Only indexes offsets and chunk boundaries without keeping raw traces in memory.
        """
        cum = 0
        with open(self.jsonl_path, "r", encoding="utf-8") as f:
            while True:
                offset = f.tell()
                line = f.readline()
                if not line:
                    break
                line_str = line.strip()
                if not line_str:
                    continue

                try:
                    rec = json.loads(line_str)
                except json.JSONDecodeError as e:
                    raise ValueError(f"Malformed JSON at offset {offset} in {self.jsonl_path}: {e}") from e

                if "program_id" not in rec:
                    raise KeyError(f"Record at offset {offset} missing required 'program_id' field")

                pid = rec["program_id"]
                java_file = self.java_dir / f"{pid}.java"
                if not java_file.is_file():
                    raise FileNotFoundError(f"Missing required Java file for program_id '{pid}': {java_file}")

                rt = rec.get("runtime_trace", {})
                tokens = tokenize_runtime(rt)
                chunk_res = chunk_trace_tokens(
                    tokens,
                    max_chunk_size=self.max_chunk_size,
                    oversized_policy=self.oversized_policy,
                    strict=True,
                )

                num_chunks = chunk_res.chunk_count
                num_oversized = len(chunk_res.oversized_events)

                self.record_offsets.append(offset)
                self.program_ids.append(pid)
                self.chunks_per_record.append(num_chunks)
                cum += num_chunks
                self.cum_chunk_counts.append(cum)

                if num_oversized > 0:
                    self.total_oversized_events += num_oversized
                    for ov in chunk_res.oversized_events:
                        self.oversized_metadata.append({
                            "program_id": pid,
                            "event_token_length": len(ov),
                            "first_tokens": ov[:5],
                        })

        self.total_chunks = cum

    def _load_source(self, program_id: str) -> Tuple[List[str], List[int]]:
        """Loads and encodes raw Java source file with caching."""
        if program_id in self._source_cache:
            return self._source_cache[program_id]

        java_file = self.java_dir / f"{program_id}.java"
        if not java_file.is_file():
            raise FileNotFoundError(f"Java file for program '{program_id}' not found: {java_file}")

        with open(java_file, "r", encoding="utf-8", errors="ignore") as f:
            code = f.read()

        tokens = tokenize_java_code(code, preserve_comments=False)
        ids = self.source_vocab.encode(tokens, add_bos=False, add_eos=False)

        if self.max_source_length is not None and len(ids) > self.max_source_length:
            tokens = tokens[:self.max_source_length]
            ids = ids[:self.max_source_length]

        self._source_cache[program_id] = (tokens, ids)
        return tokens, ids

    def _get_record_chunks(self, record_idx: int) -> List[List[str]]:
        """Retrieves chunks for a record using an LRU cache."""
        if record_idx in self._trace_cache:
            # Move to end of LRU
            self._trace_cache_order.remove(record_idx)
            self._trace_cache_order.append(record_idx)
            return self._trace_cache[record_idx]

        offset = self.record_offsets[record_idx]
        with open(self.jsonl_path, "r", encoding="utf-8") as f:
            f.seek(offset)
            line = f.readline().strip()
            rec = json.loads(line)

        rt = rec.get("runtime_trace", {})
        tokens = tokenize_runtime(rt)
        chunk_res = chunk_trace_tokens(
            tokens,
            max_chunk_size=self.max_chunk_size,
            oversized_policy=self.oversized_policy,
            strict=True,
        )
        chunks = chunk_res.chunks

        # Cache management
        if len(self._trace_cache) >= self.cache_records:
            oldest = self._trace_cache_order.pop(0)
            self._trace_cache.pop(oldest, None)

        self._trace_cache[record_idx] = chunks
        self._trace_cache_order.append(record_idx)
        return chunks

    def __len__(self) -> int:
        return self.total_chunks

    def __getitem__(self, idx: int) -> DatasetItem:
        if idx < 0 or idx >= self.total_chunks:
            raise IndexError(f"Index {idx} out of range for dataset of {self.total_chunks} chunks")

        # Binary search to find record_idx
        # cum_chunk_counts stores cumulative chunk totals
        record_idx = bisect_right(self.cum_chunk_counts, idx)
        prev_cum = self.cum_chunk_counts[record_idx - 1] if record_idx > 0 else 0
        chunk_in_record = idx - prev_cum

        program_id = self.program_ids[record_idx]
        total_chunks = self.chunks_per_record[record_idx]

        # Load raw Java source
        src_tokens, src_ids = self._load_source(program_id)

        # Load target chunk
        chunks = self._get_record_chunks(record_idx)
        chunk_tokens = chunks[chunk_in_record] if chunk_in_record < len(chunks) else []
        chunk_ids = self.target_vocab.encode(chunk_tokens, add_bos=False, add_eos=False)

        # Teacher forcing sequence construction:
        # decoder_input_ids: [BOS] + chunk_ids
        # target_ids:        chunk_ids + [EOS]
        bos_id = self.target_vocab.bos_id
        eos_id = self.target_vocab.eos_id

        decoder_input_ids = [bos_id] + chunk_ids
        target_ids = chunk_ids + [eos_id]

        return DatasetItem(
            program_id=program_id,
            chunk_index=chunk_in_record,
            total_chunks=total_chunks,
            source_tokens=src_tokens,
            source_ids=src_ids,
            chunk_tokens=chunk_tokens,
            chunk_ids=chunk_ids,
            decoder_input_ids=decoder_input_ids,
            target_ids=target_ids,
        )


# ============================================================
# COLLATE FUNCTION & PADDING LOGIC
# ============================================================

def execution_trace_collate_fn(
    batch: Sequence[DatasetItem],
    src_pad_id: int = 0,
    tgt_pad_id: int = 0,
) -> BatchData:
    """
    Collates a list of DatasetItems into padded PyTorch tensors.

    Padding and Mask Semantics:
      - src_ids: Padded with src_pad_id (0) to max source length in the batch.
      - src_padding_mask: True for padding positions (ignored by attention),
                          False for real source tokens.
      - decoder_input_ids: Padded with tgt_pad_id (0) to max target length in the batch.
      - target_ids: Padded with tgt_pad_id (0) to max target length in the batch.
      - target_padding_mask: True for padding positions (ignored by attention),
                             False for real target tokens.
      - loss_mask: True for active token loss positions, False for padding positions.
                   (Equal to ~target_padding_mask).
      - All IDs have dtype torch.long; all masks have dtype torch.bool.
    """
    if not batch:
        raise ValueError("Cannot collate an empty batch")

    batch_size = len(batch)
    program_ids = [item.program_id for item in batch]
    chunk_indices = torch.tensor([item.chunk_index for item in batch], dtype=torch.long)
    total_chunks = torch.tensor([item.total_chunks for item in batch], dtype=torch.long)

    src_lengths = [len(item.source_ids) for item in batch]
    tgt_lengths = [len(item.decoder_input_ids) for item in batch]

    max_src_len = max(src_lengths) if src_lengths else 0
    max_tgt_len = max(tgt_lengths) if tgt_lengths else 0

    # Handle edge case where all sequences in batch are empty
    max_src_len = max(1, max_src_len)
    max_tgt_len = max(1, max_tgt_len)

    # Initialize padded tensors
    src_ids = torch.full((batch_size, max_src_len), src_pad_id, dtype=torch.long)
    src_padding_mask = torch.ones((batch_size, max_src_len), dtype=torch.bool)

    decoder_input_ids = torch.full((batch_size, max_tgt_len), tgt_pad_id, dtype=torch.long)
    target_ids = torch.full((batch_size, max_tgt_len), tgt_pad_id, dtype=torch.long)
    target_padding_mask = torch.ones((batch_size, max_tgt_len), dtype=torch.bool)

    for i, item in enumerate(batch):
        s_len = len(item.source_ids)
        if s_len > 0:
            src_ids[i, :s_len] = torch.tensor(item.source_ids, dtype=torch.long)
            src_padding_mask[i, :s_len] = False

        t_len = len(item.decoder_input_ids)
        if t_len > 0:
            decoder_input_ids[i, :t_len] = torch.tensor(item.decoder_input_ids, dtype=torch.long)
            target_ids[i, :t_len] = torch.tensor(item.target_ids, dtype=torch.long)
            target_padding_mask[i, :t_len] = False

    loss_mask = ~target_padding_mask
    src_lengths_tensor = torch.tensor(src_lengths, dtype=torch.long)
    target_lengths_tensor = torch.tensor(tgt_lengths, dtype=torch.long)

    return BatchData(
        program_ids=program_ids,
        chunk_indices=chunk_indices,
        total_chunks=total_chunks,
        src_ids=src_ids,
        src_padding_mask=src_padding_mask,
        decoder_input_ids=decoder_input_ids,
        target_ids=target_ids,
        target_padding_mask=target_padding_mask,
        loss_mask=loss_mask,
        src_lengths=src_lengths_tensor,
        target_lengths=target_lengths_tensor,
    )


# ============================================================
# DATALOADER FACTORY
# ============================================================

def create_dataloader(
    dataset: ExecutionTraceDataset,
    batch_size: int = 16,
    shuffle: bool = False,
    num_workers: int = 0,
    drop_last: bool = False,
    pin_memory: bool = False,
) -> DataLoader:
    """
    Creates a PyTorch DataLoader configured with the standard collate function.

    Parameters:
      dataset: An ExecutionTraceDataset instance.
      batch_size: Number of chunk examples per batch.
      shuffle: Whether to shuffle examples (True for train, False for val/test).
      num_workers: Number of background worker processes (default 0 for macOS safety).
      drop_last: If False, keeps the final partial batch.
      pin_memory: Enables page-locked memory for fast host-to-device transfers.

    Returns:
      Configured torch.utils.data.DataLoader yielding BatchData objects.
    """
    collate = lambda batch: execution_trace_collate_fn(
        batch,
        src_pad_id=dataset.source_vocab.pad_id,
        tgt_pad_id=dataset.target_vocab.pad_id,
    )

    return DataLoader(
        dataset=dataset,
        batch_size=batch_size,
        shuffle=shuffle,
        num_workers=num_workers,
        drop_last=drop_last,
        collate_fn=collate,
        pin_memory=pin_memory,
    )

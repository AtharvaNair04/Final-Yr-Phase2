"""
Disk-backed cache of pre-encoded dataset chunks.

ExecutionTraceDataset re-tokenizes a whole runtime trace whenever its small LRU cache misses,
which makes shuffled (random-access) loading very slow. This module stores the IDs that
ExecutionTraceDataset itself produces, once, and serves them back with the same DatasetItem
interface.

Source of truth: build_chunk_cache() iterates an existing ExecutionTraceDataset in index order and
copies its outputs; it never re-implements tokenization or chunking. A cached item reconstructs the
teacher-forcing pair with the same rule as execution_dataset.py:
    decoder_input_ids = [BOS] + chunk_ids
    target_ids        = chunk_ids + [EOS]

Fail-closed guarantees:
  - IDs outside [0, 65535] abort the build (CacheOverflowError); nothing is truncated.
  - An existing cache directory is never overwritten.
  - Loading recomputes fingerprints of the split file, Java sources, both vocabularies, the
    preprocessing code and the chunking parameters, plus SHA-256 of every array file, and raises
    CacheMismatchError on any difference. A stale cache is never reused silently.

Token-string fields (source_tokens, chunk_tokens) are returned empty: the collate function, the
training loop and the evaluation module use IDs only. Scripts that need token strings must keep
using ExecutionTraceDataset.
"""

from datetime import datetime, timezone
import hashlib
import json
import os
from pathlib import Path
from typing import Any, Dict, Iterable, List, Optional, Union

import numpy as np
from torch.utils.data import Dataset

from execution_dataset import DatasetItem, ExecutionTraceDataset
from vocabulary import Vocabulary

REPO_ROOT = Path(__file__).resolve().parent
FORMAT_VERSION = 1
UINT16_MAX = 65535
CODE_FILES = (
    "tokenize_and_analyze.py",
    "chunk_trace.py",
    "source_tokenizer.py",
    "vocabulary.py",
    "execution_dataset.py",
)
ARRAY_FILES = ("target_ids.npy", "chunk_offsets.npy", "chunk_meta.npy", "source_ids.npy", "source_offsets.npy")


class CacheError(Exception):
    """Base class for chunk-cache failures."""


class CacheMismatchError(CacheError):
    """The cache does not match the data, vocabularies, code or parameters it is being used with."""


class CacheOverflowError(CacheError):
    """A token ID does not fit the uint16 storage type."""


# ============================================================
# FINGERPRINTS
# ============================================================

def sha256_file(path: Union[str, Path]) -> str:
    """SHA-256 of the raw file bytes."""
    h = hashlib.sha256()
    with open(path, "rb") as f:
        while chunk := f.read(1 << 20):
            h.update(chunk)
    return h.hexdigest()


def sha256_lf_file(path: Union[str, Path]) -> str:
    """
    SHA-256 of the file with CRLF normalized to LF (streamed). Same convention as the vocabulary
    'train_fingerprint' once line endings are normalized, so Windows (CRLF) and macOS (LF) copies
    of identical content produce the same fingerprint.
    """
    h = hashlib.sha256()
    carry = b""
    with open(path, "rb") as f:
        while chunk := f.read(1 << 20):
            data = carry + chunk
            if data.endswith(b"\r"):
                data, carry = data[:-1], b"\r"
            else:
                carry = b""
            h.update(data.replace(b"\r\n", b"\n"))
    h.update(carry)
    return h.hexdigest()


def vocab_fingerprint(vocab: Vocabulary) -> str:
    """Fingerprint of the exact token-to-ID mapping (independent of file formatting)."""
    payload = json.dumps([vocab.vocab_type, vocab.id_to_token], ensure_ascii=False)
    return hashlib.sha256(payload.encode("utf-8")).hexdigest()


def java_fingerprint(java_dir: Union[str, Path], program_ids: Iterable[str]) -> str:
    """Fingerprint over (program_id, SHA-256 of its raw .java bytes) for the split's programs."""
    h = hashlib.sha256()
    for pid in sorted(set(program_ids)):
        h.update(f"{pid}\t{sha256_file(Path(java_dir) / f'{pid}.java')}\n".encode("utf-8"))
    return h.hexdigest()


def code_fingerprint(code_root: Union[str, Path] = REPO_ROOT) -> Dict[str, str]:
    """LF-normalized SHA-256 of each preprocessing source file the cached IDs depend on."""
    return {name: sha256_lf_file(Path(code_root) / name) for name in CODE_FILES}


def compute_fingerprints(
    jsonl_path: Union[str, Path],
    java_dir: Union[str, Path],
    program_ids: List[str],
    source_vocab: Vocabulary,
    target_vocab: Vocabulary,
    code_root: Union[str, Path] = REPO_ROOT,
) -> Dict[str, Any]:
    return {
        "split_jsonl_sha256_lf": sha256_lf_file(jsonl_path),
        "java_sources_sha256": java_fingerprint(java_dir, program_ids),
        "source_vocab_sha256": vocab_fingerprint(source_vocab),
        "target_vocab_sha256": vocab_fingerprint(target_vocab),
        "code_sha256_lf": code_fingerprint(code_root),
    }


def _to_uint16(ids: List[int], what: str) -> np.ndarray:
    arr = np.asarray(ids, dtype=np.int64)
    if arr.size and (arr.min() < 0 or arr.max() > UINT16_MAX):
        raise CacheOverflowError(
            f"{what}: ID range [{arr.min()}, {arr.max()}] does not fit uint16; refusing to truncate."
        )
    return arr.astype(np.uint16)


# ============================================================
# BUILD
# ============================================================

def build_chunk_cache(
    dataset: ExecutionTraceDataset,
    out_dir: Union[str, Path],
    code_root: Union[str, Path] = REPO_ROOT,
) -> Dict[str, Any]:
    """
    Copies every chunk of `dataset` (in index order) and every program's untruncated source IDs
    into `out_dir`. The dataset must be built with max_source_length=None so that sources are stored
    in full; truncation is applied at load time. Writes to a temporary sibling directory and renames
    it into place only after everything succeeds. Refuses to overwrite an existing directory.
    """
    if dataset.max_source_length is not None:
        raise CacheError("Build the cache from a dataset with max_source_length=None (sources are stored untruncated).")
    out_dir = Path(out_dir)
    if out_dir.exists():
        raise CacheError(f"Cache directory already exists: {out_dir}. Refusing to overwrite; delete it explicitly to rebuild.")
    tmp_dir = out_dir.with_name(f"{out_dir.name}.tmp-{os.getpid()}")
    if tmp_dir.exists():
        raise CacheError(f"Temporary directory already exists: {tmp_dir}")
    out_dir.parent.mkdir(parents=True, exist_ok=True)

    program_ids = list(dataset.program_ids)
    if len(set(program_ids)) != len(program_ids):
        raise CacheError("Duplicate program_id within split.")
    pid_to_record = {pid: r for r, pid in enumerate(program_ids)}

    # Sources (untruncated), via the dataset's own loader, for every program in record order.
    source_arrays, source_offsets = [], [0]
    for pid in program_ids:
        _, ids = dataset._load_source(pid)
        arr = _to_uint16(ids, f"source ids of {pid}")
        source_arrays.append(arr)
        source_offsets.append(source_offsets[-1] + arr.size)

    # Chunks, via the public __getitem__ in index order.
    target_arrays, chunk_offsets, meta = [], [0], []
    bos, eos = dataset.target_vocab.bos_id, dataset.target_vocab.eos_id
    for i in range(len(dataset)):
        item = dataset[i]
        r = pid_to_record[item.program_id]
        if item.decoder_input_ids != [bos] + item.chunk_ids or item.target_ids != item.chunk_ids + [eos]:
            raise CacheError(f"Index {i}: dataset teacher-forcing pair does not follow the documented rule.")
        stored_src = source_arrays[r].astype(np.int64).tolist()
        if item.source_ids != stored_src:
            raise CacheError(f"Index {i}: source ids differ from the program's loaded source.")
        arr = _to_uint16(item.chunk_ids, f"chunk {i} of {item.program_id}")
        target_arrays.append(arr)
        chunk_offsets.append(chunk_offsets[-1] + arr.size)
        meta.append((r, item.chunk_index, item.total_chunks))

    chunks_per_record = list(dataset.chunks_per_record)
    if sum(chunks_per_record) != len(meta):
        raise CacheError("Chunk count mismatch between dataset index and iterated items.")

    tmp_dir.mkdir()
    arrays = {
        "target_ids.npy": np.concatenate(target_arrays) if target_arrays else np.zeros(0, np.uint16),
        "chunk_offsets.npy": np.asarray(chunk_offsets, dtype=np.int64),
        "chunk_meta.npy": np.asarray(meta, dtype=np.int32).reshape(-1, 3),
        "source_ids.npy": np.concatenate(source_arrays) if source_arrays else np.zeros(0, np.uint16),
        "source_offsets.npy": np.asarray(source_offsets, dtype=np.int64),
    }
    for name, arr in arrays.items():
        np.save(tmp_dir / name, arr, allow_pickle=False)

    manifest = {
        "format_version": FORMAT_VERSION,
        "created_utc": datetime.now(timezone.utc).isoformat(timespec="seconds"),
        "params": {"max_chunk_size": dataset.max_chunk_size, "oversized_policy": dataset.oversized_policy},
        "counts": {
            "programs": len(program_ids),
            "chunks": len(meta),
            "target_tokens": int(arrays["target_ids.npy"].size),
            "source_tokens": int(arrays["source_ids.npy"].size),
            "oversized_events_dropped": int(dataset.total_oversized_events),
        },
        "program_ids": program_ids,
        "chunks_per_record": chunks_per_record,
        "fingerprints": compute_fingerprints(
            dataset.jsonl_path, dataset.java_dir, program_ids,
            dataset.source_vocab, dataset.target_vocab, code_root,
        ),
        "array_sha256": {name: sha256_file(tmp_dir / name) for name in ARRAY_FILES},
    }
    (tmp_dir / "manifest.json").write_text(json.dumps(manifest, indent=1), encoding="utf-8")
    os.replace(tmp_dir, out_dir)
    return manifest


# ============================================================
# LOAD
# ============================================================

class CachedExecutionTraceDataset(Dataset):
    """
    Drop-in replacement for ExecutionTraceDataset when only IDs are needed.

    Returns DatasetItem objects whose ID fields and metadata equal ExecutionTraceDataset's for the
    same index; source_tokens and chunk_tokens are empty lists. Exposes the attributes used by
    create_dataloader() and the existing scripts (source_vocab, target_vocab, program_ids,
    chunks_per_record, total_chunks, ...).
    """

    def __init__(
        self,
        cache_dir: Union[str, Path],
        jsonl_path: Union[str, Path],
        java_dir: Union[str, Path],
        source_vocab: Vocabulary,
        target_vocab: Vocabulary,
        max_chunk_size: int = 1024,
        oversized_policy: str = "separate",
        max_source_length: Optional[int] = None,
        code_root: Union[str, Path] = REPO_ROOT,
    ) -> None:
        self.cache_dir = Path(cache_dir)
        self.jsonl_path = Path(jsonl_path).resolve()
        self.java_dir = Path(java_dir).resolve()
        self.source_vocab = source_vocab
        self.target_vocab = target_vocab
        self.max_chunk_size = max_chunk_size
        self.oversized_policy = oversized_policy
        self.max_source_length = max_source_length

        manifest_path = self.cache_dir / "manifest.json"
        if not manifest_path.is_file():
            raise CacheError(f"No cache manifest at {manifest_path}")
        self.manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
        self._validate(code_root)

        arrays = {name: np.load(self.cache_dir / name, allow_pickle=False) for name in ARRAY_FILES}
        self._target_ids = arrays["target_ids.npy"]
        self._chunk_offsets = arrays["chunk_offsets.npy"]
        self._chunk_meta = arrays["chunk_meta.npy"]
        self._source_ids = arrays["source_ids.npy"]
        self._source_offsets = arrays["source_offsets.npy"]
        self._validate_arrays()

        self.program_ids: List[str] = list(self.manifest["program_ids"])
        self.chunks_per_record: List[int] = list(self.manifest["chunks_per_record"])
        self.cum_chunk_counts: List[int] = list(np.cumsum(self.chunks_per_record, dtype=np.int64).tolist())
        self.total_chunks: int = int(self.manifest["counts"]["chunks"])
        self.total_oversized_events: int = int(self.manifest["counts"]["oversized_events_dropped"])

    def _validate(self, code_root) -> None:
        m = self.manifest
        problems = []
        if m.get("format_version") != FORMAT_VERSION:
            problems.append(f"format_version {m.get('format_version')} != {FORMAT_VERSION}")
        expected_params = {"max_chunk_size": self.max_chunk_size, "oversized_policy": self.oversized_policy}
        if m.get("params") != expected_params:
            problems.append(f"params {m.get('params')} != {expected_params}")
        current = compute_fingerprints(
            self.jsonl_path, self.java_dir, m["program_ids"], self.source_vocab, self.target_vocab, code_root,
        )
        for key, value in current.items():
            if m["fingerprints"].get(key) != value:
                problems.append(f"fingerprint '{key}' differs")
        for name in ARRAY_FILES:
            path = self.cache_dir / name
            if not path.is_file():
                problems.append(f"missing array file {name}")
            elif sha256_file(path) != m["array_sha256"].get(name):
                problems.append(f"array file {name} checksum differs")
        if problems:
            raise CacheMismatchError(f"Cache {self.cache_dir} is stale or corrupt: " + "; ".join(problems))

    def _validate_arrays(self) -> None:
        m = self.manifest["counts"]
        n_chunks, n_programs = m["chunks"], m["programs"]
        checks = [
            self._target_ids.dtype == np.uint16 and self._source_ids.dtype == np.uint16,
            self._chunk_offsets.shape == (n_chunks + 1,) and self._source_offsets.shape == (n_programs + 1,),
            self._chunk_meta.shape == (n_chunks, 3),
            int(self._chunk_offsets[-1]) == self._target_ids.size == m["target_tokens"],
            int(self._source_offsets[-1]) == self._source_ids.size == m["source_tokens"],
            bool(np.all(np.diff(self._chunk_offsets) >= 0)) and bool(np.all(np.diff(self._source_offsets) >= 0)),
            sum(self.manifest["chunks_per_record"]) == n_chunks,
        ]
        if not all(checks):
            raise CacheMismatchError(f"Cache {self.cache_dir} arrays are inconsistent with its manifest.")

    def __len__(self) -> int:
        return self.total_chunks

    def _source(self, record_idx: int) -> List[int]:
        start, end = self._source_offsets[record_idx], self._source_offsets[record_idx + 1]
        ids = self._source_ids[start:end].astype(np.int64).tolist()
        if self.max_source_length is not None and len(ids) > self.max_source_length:
            ids = ids[:self.max_source_length]
        return ids

    def __getitem__(self, idx: int) -> DatasetItem:
        if idx < 0 or idx >= self.total_chunks:
            raise IndexError(f"Index {idx} out of range for dataset of {self.total_chunks} chunks")
        record_idx, chunk_index, total_chunks = (int(v) for v in self._chunk_meta[idx])
        start, end = self._chunk_offsets[idx], self._chunk_offsets[idx + 1]
        chunk_ids = self._target_ids[start:end].astype(np.int64).tolist()
        return DatasetItem(
            program_id=self.program_ids[record_idx],
            chunk_index=chunk_index,
            total_chunks=total_chunks,
            source_tokens=[],
            source_ids=self._source(record_idx),
            chunk_tokens=[],
            chunk_ids=chunk_ids,
            decoder_input_ids=[self.target_vocab.bos_id] + chunk_ids,
            target_ids=chunk_ids + [self.target_vocab.eos_id],
        )

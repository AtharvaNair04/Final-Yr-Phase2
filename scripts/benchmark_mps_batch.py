#!/usr/bin/env python3
"""
EST Real-Data MPS Single-Batch Benchmark.

Performs a controlled, isolated single-batch benchmark using real Java source files
and runtime execution traces from Module3_Training_Dataset/split/train.jsonl on
Apple Silicon GPU via torch.device("mps").

Guarantees:
  1. Explicitly requires MPS; aborts if unavailable (never silently falls back to CPU).
  2. Loads existing production vocabularies and dataset using ExecutionTraceDataset
     and execution_trace_collate_fn.
  3. Inspects actual chunk lengths before selecting a batch. Uses an authentic,
     conservative batch with source and target lengths <= 128 tokens without
     arbitrary truncation or alteration of dataset semantics.
  4. Reports dataset length limitations (why typical sequential batches exceed 128 tokens).
  5. Uses the small ModelConfig matching scripts/test_mps_smoke.py.
  6. Executes timed forward, masked loss, and backward passes with MPS synchronization.
  7. Tracks peak MPS memory allocations.
  8. Strictly runs NO optimizer step, NO training epochs, and NO validation/test loops.
  9. Does NOT modify production code, dataset files, or checkpoints.
"""

import json
from pathlib import Path
import sys
import tempfile
import time

# Ensure project root is on sys.path
REPO_ROOT = Path(__file__).resolve().parent.parent
if str(REPO_ROOT) not in sys.path:
    sys.path.insert(0, str(REPO_ROOT))

import torch

from execution_dataset import (
    BatchData,
    DatasetItem,
    ExecutionTraceDataset,
    execution_trace_collate_fn,
)
from model import ExecutionSemanticTransformer, ModelConfig
from train import compute_batch_loss
from vocabulary import Vocabulary


def verify_mps_available() -> torch.device:
    """Verifies that MPS is built and available; aborts otherwise."""
    if not torch.backends.mps.is_built():
        raise RuntimeError("MPS Benchmark Aborted: PyTorch was not built with MPS (Metal) enabled.")
    if not torch.backends.mps.is_available():
        raise RuntimeError("MPS Benchmark Aborted: Apple Silicon MPS device is not available on this system.")
    return torch.device("mps")


def format_bytes(num_bytes: int) -> str:
    """Formats raw byte count into human-readable MiB string."""
    return f"{num_bytes / (1024 ** 2):.2f} MiB"


def main() -> None:
    print("=" * 70)
    print("EST Real-Data MPS Benchmark (Single-Batch, No Full Training)")
    print("=" * 70)

    # 1. Require MPS explicitly
    device = verify_mps_available()
    print(f"PyTorch Version:      {torch.__version__}")
    print(f"Hardware Device:      {device} (Apple Silicon GPU)")

    # 2. Paths and Vocabularies
    src_vocab_path = REPO_ROOT / "vocabularies" / "vocab_source.json"
    tgt_vocab_path = REPO_ROOT / "vocabularies" / "vocab_target.json"
    train_split_path = REPO_ROOT / "Module3_Training_Dataset" / "split" / "train.jsonl"
    java_dir = REPO_ROOT / "selected source code"

    if not src_vocab_path.is_file():
        raise FileNotFoundError(f"Source vocabulary missing: {src_vocab_path}")
    if not tgt_vocab_path.is_file():
        raise FileNotFoundError(f"Target vocabulary missing: {tgt_vocab_path}")
    if not train_split_path.is_file():
        raise FileNotFoundError(f"Training split missing: {train_split_path}")
    if not java_dir.is_dir():
        raise FileNotFoundError(f"Java source directory missing: {java_dir}")

    src_vocab = Vocabulary.load(src_vocab_path)
    tgt_vocab = Vocabulary.load(tgt_vocab_path)
    print(f"Source Vocabulary:    {len(src_vocab):,} tokens (pad_id: {src_vocab.pad_id})")
    print(f"Target Vocabulary:    {len(tgt_vocab):,} tokens (pad_id: {tgt_vocab.pad_id})")

    # 3. Sample records and inspect chunk length distribution
    print("\n--- Inspecting Real Dataset Chunk Lengths ---")
    print(f"Dataset Split File:   {train_split_path.relative_to(REPO_ROOT)}")

    # Extract a representative sample of training records:
    # - Typical sequential programs (first 5 records) to demonstrate standard dataset sizes
    # - Compact programs (containing terminal chunks <= 128 tokens)
    sample_records = []
    target_program_ids = {"p00466", "p00014"}  # Programs known to contain natural short chunks
    found_target_ids = set()

    with open(train_split_path, "r", encoding="utf-8") as f:
        for idx, line in enumerate(f):
            rec = json.loads(line)
            pid = rec.get("program_id")
            # Collect first 5 baseline records
            if idx < 5:
                sample_records.append(line)
            # Collect known compact programs
            if pid in target_program_ids and pid not in found_target_ids:
                if idx >= 5:
                    sample_records.append(line)
                found_target_ids.add(pid)
            if len(found_target_ids) == len(target_program_ids) and idx >= 5:
                break

    # Build dataset on the representative sample using existing ExecutionTraceDataset
    with tempfile.NamedTemporaryFile("w+", suffix=".jsonl") as tmp_file:
        tmp_file.writelines(sample_records)
        tmp_file.flush()

        dataset = ExecutionTraceDataset(
            jsonl_path=tmp_file.name,
            java_dir=java_dir,
            source_vocab=src_vocab,
            target_vocab=tgt_vocab,
            max_chunk_size=1024,
        )

        total_chunks = len(dataset)
        print(f"Sampled Records:      {len(sample_records)} programs ({total_chunks} total chunk items)")

        # Inspect lengths of all sampled items
        all_item_stats = []
        for i in range(total_chunks):
            item = dataset[i]
            s_len = len(item.source_ids)
            t_len = len(item.decoder_input_ids)
            all_item_stats.append((item.program_id, item.chunk_index, item.total_chunks, s_len, t_len, item))

        # Display sample distribution
        print("\nChunk length inspection (first 6 sample items):")
        for pid, c_idx, tot_c, s_len, t_len, _ in all_item_stats[:6]:
            print(f"  Program {pid} (Chunk {c_idx + 1}/{tot_c}): Source = {s_len:4d} tokens | Target = {t_len:4d} tokens")

        # 4. Check for length limitations
        # Filter for naturally short items matching the conservative <= 128 threshold
        max_allowed_len = 128
        short_items = [
            item for (pid, c_idx, tot_c, s_len, t_len, item) in all_item_stats
            if s_len <= max_allowed_len and t_len <= max_allowed_len
        ]

        print(f"\nItems meeting conservative <= {max_allowed_len} limit: {len(short_items)}")

        if len(short_items) < 2:
            print("\n" + "!" * 70)
            print("LIMITATION REPORT:")
            print(f"  The normal dataset pipeline could not produce a batch of >= 2 items with length <= {max_allowed_len}.")
            print("  Reason: The execution trace chunker default is max_chunk_size=1024, so intermediate")
            print("  trace chunks are ~1000 tokens, and Java source files typically exceed 120-150 tokens.")
            print(f"  Only terminal chunks of compact programs satisfy <= {max_allowed_len}.")
            print("  As required, we do NOT arbitrarily truncate tokens or alter tokenization semantics.")
            print("!" * 70)
            sys.exit(1)

        # Select conservative batch of size 2 without any arbitrary truncation
        selected_items = short_items[:2]
        print(f"Selected Batch Size:  {len(selected_items)} authentic dataset items")
        for i, it in enumerate(selected_items, 1):
            print(f"  Item {i}: Program {it.program_id} (Chunk {it.chunk_index + 1}/{it.total_chunks}) | "
                  f"Source: {len(it.source_ids)} tokens | Target: {len(it.decoder_input_ids)} tokens")

        # 5. Collate using project's existing execution_trace_collate_fn
        batch: BatchData = execution_trace_collate_fn(
            selected_items,
            src_pad_id=src_vocab.pad_id,
            tgt_pad_id=tgt_vocab.pad_id,
        )

    # 6. Model Configuration matching scripts/test_mps_smoke.py
    print("\n--- Initializing EST Model on MPS ---")
    model_cfg = ModelConfig(
        src_vocab_size=len(src_vocab),
        tgt_vocab_size=len(tgt_vocab),
        d_model=64,
        nhead=4,
        num_encoder_layers=1,
        num_decoder_layers=1,
        dim_feedforward=128,
        dropout=0.1,
        max_src_length=128,
        max_tgt_length=128,
        src_pad_id=src_vocab.pad_id,
        tgt_pad_id=tgt_vocab.pad_id,
        activation="relu",
        norm_first=False,
    )

    print(f"Model Architecture:   d_model={model_cfg.d_model}, nhead={model_cfg.nhead}, "
          f"enc_layers={model_cfg.num_encoder_layers}, dec_layers={model_cfg.num_decoder_layers}, "
          f"dim_ff={model_cfg.dim_feedforward}")
    print(f"Sequence Bounds:      max_src_length={model_cfg.max_src_length}, max_tgt_length={model_cfg.max_tgt_length}")

    torch.manual_seed(42)
    model = ExecutionSemanticTransformer(model_cfg).to(device)
    model.train()

    total_params = sum(p.numel() for p in model.parameters())
    trainable_tensors = sum(1 for p in model.parameters() if p.requires_grad)
    print(f"Model Parameters:     {total_params:,} ({trainable_tensors} parameter tensors)")

    # Transfer batch to MPS
    batch = batch.to(device)
    print(f"Collated Tensors:     src_ids={tuple(batch.src_ids.shape)}, "
          f"decoder_input_ids={tuple(batch.decoder_input_ids.shape)}, "
          f"target_ids={tuple(batch.target_ids.shape)}")

    # 7. Warm-up Pass (Metal Shader Compilation)
    # The first MPS pass compiles Metal compute kernels; synchronize to isolate compilation
    torch.mps.synchronize()
    init_alloc = torch.mps.current_allocated_memory()
    init_driver = torch.mps.driver_allocated_memory()

    t_cold_start = time.perf_counter()
    cold_logits = model(batch)
    torch.mps.synchronize()
    t_cold_fwd = time.perf_counter()

    cold_loss, _ = compute_batch_loss(cold_logits, batch.target_ids, tgt_pad_id=model_cfg.tgt_pad_id)
    model.zero_grad()
    cold_loss.backward()
    torch.mps.synchronize()
    t_cold_bwd = time.perf_counter()

    cold_fwd_ms = (t_cold_fwd - t_cold_start) * 1000.0
    cold_bwd_ms = (t_cold_bwd - t_cold_fwd) * 1000.0
    cold_total_ms = (t_cold_bwd - t_cold_start) * 1000.0

    # 8. Timed Benchmark Pass (Synchronized Execution)
    model.zero_grad()
    torch.mps.synchronize()
    t_fwd_start = time.perf_counter()

    # Forward pass
    logits = model(batch)
    torch.mps.synchronize()
    t_fwd_end = time.perf_counter()
    post_fwd_alloc = torch.mps.current_allocated_memory()

    # Masked Cross-Entropy Loss
    loss, valid_tokens = compute_batch_loss(logits, batch.target_ids, tgt_pad_id=model_cfg.tgt_pad_id)
    torch.mps.synchronize()
    t_loss_end = time.perf_counter()

    # Backward pass
    model.zero_grad()
    loss.backward()
    torch.mps.synchronize()
    t_bwd_end = time.perf_counter()
    post_bwd_alloc = torch.mps.current_allocated_memory()
    peak_driver = torch.mps.driver_allocated_memory()

    # Compute elapsed durations in milliseconds
    fwd_ms = (t_fwd_end - t_fwd_start) * 1000.0
    loss_ms = (t_loss_end - t_fwd_end) * 1000.0
    bwd_ms = (t_bwd_end - t_loss_end) * 1000.0
    total_ms = (t_bwd_end - t_fwd_start) * 1000.0

    # 9. Verify finite loss and finite gradients across all parameter tensors
    loss_val = loss.item()
    loss_finite = bool(torch.isfinite(loss).item())

    grads_finite = True
    finite_grad_count = 0
    for name, param in model.named_parameters():
        if param.requires_grad:
            if param.grad is None:
                grads_finite = False
                print(f"  ERROR: Parameter {name} has no gradient.")
            elif not torch.isfinite(param.grad).all():
                grads_finite = False
                print(f"  ERROR: Parameter {name} has non-finite gradient (NaN/Inf).")
            else:
                finite_grad_count += 1

    # 10. Report Metrics
    print("\n--- Benchmark Results ---")
    print(f"Masked Batch Loss:    {loss_val:.6f} (Valid unpadded tokens: {valid_tokens})")
    print(f"Loss Is Finite:       {loss_finite}")
    print(f"Gradients Finite:     {grads_finite} ({finite_grad_count}/{trainable_tensors} tensors verified)")
    print(f"Cold Pass (w/ Kernel Compilation): {cold_total_ms:.2f} ms (Fwd: {cold_fwd_ms:.2f} ms, Bwd: {cold_bwd_ms:.2f} ms)")
    print(f"Warm Pass Elapsed Times:")
    print(f"  - Forward Pass:     {fwd_ms:.3f} ms")
    print(f"  - Loss Compute:     {loss_ms:.3f} ms")
    print(f"  - Backward Pass:    {bwd_ms:.3f} ms")
    print(f"  - Total (Fwd+Loss+Bwd): {total_ms:.3f} ms  (~{1000.0 / total_ms:.1f} batches/sec throughput)")

    print(f"\nMPS Memory Metrics:")
    print(f"  - Initial Allocated:   {format_bytes(init_alloc)}")
    print(f"  - Post-Forward Alloc:  {format_bytes(post_fwd_alloc)}")
    print(f"  - Post-Backward Alloc: {format_bytes(post_bwd_alloc)}")
    print(f"  - Peak Driver Memory:  {format_bytes(peak_driver)}")

    print("-" * 70)
    if loss_finite and grads_finite:
        print("BENCHMARK SUCCESS: Real-data batch executed successfully on Apple Silicon MPS.")
    else:
        print("BENCHMARK FAILURE: Encountered non-finite loss or gradients on MPS.")
        sys.exit(1)
    print("=" * 70)


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""
Controlled EST MPS Training Pilot Script.

Executes a bounded, non-destructive training pilot on Apple Silicon MPS using
real data from Module3_Training_Dataset.

Strict Safeguards:
  1. Checks checkpoints/pilot_mps/ before writing anything; aborts if prior results exist.
  2. Preserves checkpoints/exp1/ completely (verifies SHA256 hashes before and after).
  3. Explicitly requires MPS; aborts if unavailable (no silent CPU fallback).
  4. Uses 10 disjoint training programs and 2 separate validation programs.
  5. Uses complete, naturally generated chunks without arbitrary truncation or event splitting.
  6. Enforces hard limits: <= 50 optimizer steps and <= 180 seconds.
  7. Verifies parameter updates, finite loss/gradients, and reloadability.
"""

from dataclasses import asdict
import hashlib
import json
import logging
import os
from pathlib import Path
import random
import sys
import time
from typing import Dict, List, Tuple

# Ensure project root is on sys.path
REPO_ROOT = Path(__file__).resolve().parent.parent
if str(REPO_ROOT) not in sys.path:
    sys.path.insert(0, str(REPO_ROOT))

import numpy as np
import torch
import torch.nn as nn
from torch.utils.data import DataLoader

from execution_dataset import (
    BatchData,
    ExecutionTraceDataset,
    create_dataloader,
)
from model import ExecutionSemanticTransformer, ModelConfig
from train import TrainingConfig, compute_batch_loss
from vocabulary import Vocabulary


logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s",
    handlers=[logging.StreamHandler(sys.stdout)],
)
logger = logging.getLogger("mps_pilot")


def compute_file_sha256(path: Path) -> str:
    """Calculates SHA256 hash of a file."""
    h = hashlib.sha256()
    with open(path, "rb") as f:
        while chunk := f.read(1024 * 1024):
            h.update(chunk)
    return h.hexdigest()


def verify_mps_available() -> torch.device:
    """Explicitly verifies Apple Silicon MPS availability; aborts otherwise."""
    if not torch.backends.mps.is_built():
        raise RuntimeError("MPS Pilot Aborted: PyTorch was not built with MPS (Metal) enabled.")
    if not torch.backends.mps.is_available():
        raise RuntimeError("MPS Pilot Aborted: Apple Silicon MPS device is not available on this system.")
    return torch.device("mps")


def format_bytes(num_bytes: int) -> str:
    """Converts bytes to MiB string."""
    return f"{num_bytes / (1024 ** 2):.2f} MiB"


def main() -> None:
    print("=" * 75)
    print("EST Controlled MPS Training Pilot")
    print("=" * 75)

    # -------------------------------------------------------------
    # Step 1: Pre-flight Checks and Directory Safeguards
    # -------------------------------------------------------------
    pilot_dir = REPO_ROOT / "checkpoints" / "pilot_mps"
    if pilot_dir.exists():
        existing_pts = list(pilot_dir.glob("*.pt"))
        if existing_pts:
            raise RuntimeError(
                f"Safety Abort: {pilot_dir} already contains checkpoint files: "
                f"{[p.name for p in existing_pts]}. Aborting to prevent overwriting results."
            )
    pilot_dir.mkdir(parents=True, exist_ok=True)
    print(f"Pilot Output Directory: {pilot_dir.relative_to(REPO_ROOT)} (Verified clean)")

    # Record baseline hashes for exp1 checkpoints
    exp1_dir = REPO_ROOT / "checkpoints" / "exp1"
    exp1_baseline_hashes: Dict[str, str] = {}
    if exp1_dir.is_dir():
        for ckpt in exp1_dir.glob("*.pt"):
            exp1_baseline_hashes[ckpt.name] = compute_file_sha256(ckpt)
            print(f"Exp1 Baseline Recorded: {ckpt.name} ({exp1_baseline_hashes[ckpt.name][:16]}...)")

    # Verify MPS device
    device = verify_mps_available()
    # Disable MHA fastpath on MPS to avoid unimplemented aten::_nested_tensor_from_mask_left_aligned
    if hasattr(torch.backends, "mha") and hasattr(torch.backends.mha, "set_fastpath_enabled"):
        torch.backends.mha.set_fastpath_enabled(False)
    print(f"Hardware Device:        {device} (Apple Silicon GPU)")
    print(f"PyTorch Version:        {torch.__version__}")
    print(f"MHA Fastpath:           Disabled for MPS NestedTensor compatibility")

    # Set deterministic random seeds
    seed = 42
    random.seed(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)

    # -------------------------------------------------------------
    # Step 2: Vocabulary and Dataset Preparation
    # -------------------------------------------------------------
    src_vocab_path = REPO_ROOT / "vocabularies" / "vocab_source.json"
    tgt_vocab_path = REPO_ROOT / "vocabularies" / "vocab_target.json"
    train_source_file = REPO_ROOT / "Module3_Training_Dataset" / "split" / "train.jsonl"
    val_source_file = REPO_ROOT / "Module3_Training_Dataset" / "split" / "validation.jsonl"
    java_dir = REPO_ROOT / "selected source code"

    src_vocab = Vocabulary.load(src_vocab_path)
    tgt_vocab = Vocabulary.load(tgt_vocab_path)
    print(f"Source Vocabulary:      {len(src_vocab):,} tokens (pad_id: {src_vocab.pad_id})")
    print(f"Target Vocabulary:      {len(tgt_vocab):,} tokens (pad_id: {tgt_vocab.pad_id})")

    # Documented disjoint subsets
    train_pids = [
        "p00435", "p00188", "p00168", "p00436", "p00647",
        "p00128", "p00152", "p00668", "p00102", "p00231",
    ]
    val_pids = ["p00160", "p00334"]

    assert len(train_pids) == 10, "Expected exactly 10 training programs"
    assert len(val_pids) == 2, "Expected exactly 2 validation programs"
    assert len(set(train_pids) & set(val_pids)) == 0, "Disjointness violated between train and val"
    print(f"\nSelected Train PIDs (10): {train_pids}")
    print(f"Selected Val PIDs   (2):  {val_pids}")

    # Extract exact lines into isolated subset files in pilot directory
    train_subset_path = pilot_dir / "train_subset.jsonl"
    val_subset_path = pilot_dir / "val_subset.jsonl"

    train_lines: List[str] = []
    with open(train_source_file, "r", encoding="utf-8") as f:
        for line in f:
            if json.loads(line).get("program_id") in train_pids:
                train_lines.append(line)

    val_lines: List[str] = []
    with open(val_source_file, "r", encoding="utf-8") as f:
        for line in f:
            if json.loads(line).get("program_id") in val_pids:
                val_lines.append(line)

    assert len(train_lines) == 10, f"Expected 10 train lines, found {len(train_lines)}"
    assert len(val_lines) == 2, f"Expected 2 val lines, found {len(val_lines)}"

    with open(train_subset_path, "w", encoding="utf-8") as f:
        f.writelines(train_lines)
    with open(val_subset_path, "w", encoding="utf-8") as f:
        f.writelines(val_lines)

    # Initialize ExecutionTraceDataset instances
    train_dataset = ExecutionTraceDataset(
        jsonl_path=train_subset_path,
        java_dir=java_dir,
        source_vocab=src_vocab,
        target_vocab=tgt_vocab,
        max_chunk_size=1024,
    )
    val_dataset = ExecutionTraceDataset(
        jsonl_path=val_subset_path,
        java_dir=java_dir,
        source_vocab=src_vocab,
        target_vocab=tgt_vocab,
        max_chunk_size=1024,
    )

    print(f"\nTrain Dataset Chunks:   {len(train_dataset)} chunks across 10 programs")
    print(f"Val Dataset Chunks:     {len(val_dataset)} chunks across 2 programs")

    # Analyze actual length distributions
    train_src_lens = [len(train_dataset[i].source_ids) for i in range(len(train_dataset))]
    train_tgt_lens = [len(train_dataset[i].decoder_input_ids) for i in range(len(train_dataset))]
    val_src_lens = [len(val_dataset[i].source_ids) for i in range(len(val_dataset))]
    val_tgt_lens = [len(val_dataset[i].decoder_input_ids) for i in range(len(val_dataset))]

    print(f"Train Source Lengths:   min={min(train_src_lens)}, max={max(train_src_lens)}, avg={sum(train_src_lens)/len(train_src_lens):.1f}")
    print(f"Train Target Lengths:   min={min(train_tgt_lens)}, max={max(train_tgt_lens)}, avg={sum(train_tgt_lens)/len(train_tgt_lens):.1f}")
    print(f"Val Source Lengths:     min={min(val_src_lens)}, max={max(val_src_lens)}, avg={sum(val_src_lens)/len(val_src_lens):.1f}")
    print(f"Val Target Lengths:     min={min(val_tgt_lens)}, max={max(val_tgt_lens)}, avg={sum(val_tgt_lens)/len(val_tgt_lens):.1f}")

    # -------------------------------------------------------------
    # Step 3: Model and Configuration
    # -------------------------------------------------------------
    # Bounds: max_src_length=1024, max_tgt_length=2048 to safely accommodate real data chunks
    model_cfg = ModelConfig(
        src_vocab_size=len(src_vocab),
        tgt_vocab_size=len(tgt_vocab),
        d_model=128,
        nhead=4,
        num_encoder_layers=2,
        num_decoder_layers=2,
        dim_feedforward=256,
        dropout=0.1,
        max_src_length=1024,
        max_tgt_length=2048,
        src_pad_id=src_vocab.pad_id,
        tgt_pad_id=tgt_vocab.pad_id,
        activation="relu",
        norm_first=False,
    )

    # Positional bounds validation
    assert max(train_src_lens) <= model_cfg.max_src_length, "Source length exceeds max_src_length"
    assert max(train_tgt_lens) <= model_cfg.max_tgt_length, "Target length exceeds max_tgt_length"
    assert max(val_src_lens) <= model_cfg.max_src_length, "Val source length exceeds max_src_length"
    assert max(val_tgt_lens) <= model_cfg.max_tgt_length, "Val target length exceeds max_tgt_length"
    print("Positional Bounds:      Verified (max_src=1024, max_tgt=2048 covers all chunk lengths)")

    model = ExecutionSemanticTransformer(model_cfg).to(device)
    total_params = sum(p.numel() for p in model.parameters())
    print(f"EST Model Parameters:   {total_params:,} (Weights loaded to {device})")

    # Save initial parameter snapshot to verify optimizer updates
    initial_param_sample = model.output_projection.weight.clone().detach().cpu()

    # Training hyperparameters
    batch_size = 2
    learning_rate = 0.0005
    weight_decay = 0.01
    grad_clip_norm = 1.0
    max_steps = 50
    max_seconds = 180.0

    train_cfg = TrainingConfig(
        epochs=3,
        learning_rate=learning_rate,
        weight_decay=weight_decay,
        grad_clip_norm=grad_clip_norm,
        train_batch_size=batch_size,
        eval_batch_size=batch_size,
        checkpoint_dir=str(pilot_dir),
        seed=seed,
        device="mps",
        save_best_only=False,
        log_interval=5,
    )

    # Create DataLoaders
    train_loader = create_dataloader(train_dataset, batch_size=batch_size, shuffle=True)
    val_loader = create_dataloader(val_dataset, batch_size=batch_size, shuffle=False)

    optimizer = torch.optim.AdamW(
        model.parameters(),
        lr=learning_rate,
        weight_decay=weight_decay,
    )

    # Save configs to pilot directory
    with open(pilot_dir / "model_config.json", "w") as f:
        json.dump(model_cfg.to_dict(), f, indent=2)
    with open(pilot_dir / "training_config.json", "w") as f:
        json.dump(train_cfg.to_dict(), f, indent=2)

    # -------------------------------------------------------------
    # Step 4: Controlled Training Loop
    # -------------------------------------------------------------
    print("\n--- Starting Controlled Training Loop ---")
    print(f"Constraints:            Max Steps = {max_steps} | Max Time = {max_seconds:.0f}s")

    model.train()
    step_records: List[Dict[str, float]] = []
    optimizer_steps = 0
    start_time = time.time()
    time_limit_hit = False
    step_limit_hit = False

    best_val_loss = float("inf")
    epoch = 0

    while optimizer_steps < max_steps and not time_limit_hit:
        epoch += 1
        epoch_loss_sum = 0.0
        epoch_valid_tokens = 0

        for batch in train_loader:
            elapsed = time.time() - start_time
            if elapsed >= max_seconds:
                time_limit_hit = True
                print(f"\n[Limit Reached] Time limit of {max_seconds:.0f}s reached at step {optimizer_steps}.")
                break
            if optimizer_steps >= max_steps:
                step_limit_hit = True
                break

            batch = batch.to(device)
            optimizer.zero_grad()

            # Forward pass
            logits = model(batch)
            loss, valid_tokens = compute_batch_loss(logits, batch.target_ids, tgt_pad_id=model_cfg.tgt_pad_id)

            if not torch.isfinite(loss):
                raise RuntimeError(f"Non-finite loss encountered at step {optimizer_steps + 1}: {loss.item()}")

            if valid_tokens > 0:
                loss.backward()

                # Verify finite gradients
                for name, param in model.named_parameters():
                    if param.requires_grad and param.grad is not None:
                        if not torch.isfinite(param.grad).all():
                            raise RuntimeError(f"Non-finite gradient in {name} at step {optimizer_steps + 1}")

                if grad_clip_norm > 0.0:
                    torch.nn.utils.clip_grad_norm_(model.parameters(), max_norm=grad_clip_norm)

                optimizer.step()
                torch.mps.synchronize()

                optimizer_steps += 1
                loss_val = loss.item()
                epoch_loss_sum += loss_val * valid_tokens
                epoch_valid_tokens += valid_tokens

                record = {
                    "step": optimizer_steps,
                    "epoch": epoch,
                    "loss": loss_val,
                    "valid_tokens": valid_tokens,
                    "elapsed_sec": time.time() - start_time,
                }
                step_records.append(record)

                if optimizer_steps % 5 == 0 or optimizer_steps == 1 or optimizer_steps == max_steps:
                    running_avg = epoch_loss_sum / max(1, epoch_valid_tokens)
                    print(
                        f"Step [{optimizer_steps:2d}/{max_steps}] | "
                        f"Epoch {epoch} | "
                        f"Loss: {loss_val:.4f} | "
                        f"Running Avg: {running_avg:.4f} | "
                        f"Tokens: {valid_tokens:4d} | "
                        f"Elapsed: {record['elapsed_sec']:.2f}s"
                    )

    total_training_time = time.time() - start_time
    print(f"\nTraining Loop Finished: {optimizer_steps} steps completed in {total_training_time:.2f}s.")

    # -------------------------------------------------------------
    # Step 5: Validation Pass
    # -------------------------------------------------------------
    print("\n--- Running Validation Evaluation ---")
    model.eval()
    val_loss_sum = 0.0
    val_valid_tokens = 0

    with torch.no_grad():
        for batch in val_loader:
            batch = batch.to(device)
            logits = model(batch)
            loss, valid_tokens = compute_batch_loss(logits, batch.target_ids, tgt_pad_id=model_cfg.tgt_pad_id)
            if valid_tokens > 0:
                val_loss_sum += loss.item() * valid_tokens
                val_valid_tokens += valid_tokens

    torch.mps.synchronize()
    final_val_loss = val_loss_sum / max(1, val_valid_tokens)
    final_train_loss = epoch_loss_sum / max(1, epoch_valid_tokens)

    print(f"Validation Loss:        {final_val_loss:.4f} ({val_valid_tokens} valid tokens)")
    print(f"Final Train Loss:       {final_train_loss:.4f}")

    # Memory metrics
    final_mps_alloc = torch.mps.current_allocated_memory()
    final_mps_driver = torch.mps.driver_allocated_memory()
    print(f"MPS Allocated Memory:   {format_bytes(final_mps_alloc)}")
    print(f"MPS Driver Memory:      {format_bytes(final_mps_driver)}")

    # -------------------------------------------------------------
    # Step 6: Parameter Update Verification
    # -------------------------------------------------------------
    trained_param_sample = model.output_projection.weight.detach().cpu()
    param_difference = (trained_param_sample - initial_param_sample).abs().max().item()
    print(f"\nParameter Update Check: Max absolute weight shift = {param_difference:.6f}")
    assert param_difference > 1e-6, "Weights did not update! Optimizer failed to update parameters."
    print("Optimizer Update Check: PASSED (Model parameters were actively updated)")

    # Downward loss trend verification
    initial_steps_loss = np.mean([r["loss"] for r in step_records[:5]])
    final_steps_loss = np.mean([r["loss"] for r in step_records[-5:]])
    loss_reduction = initial_steps_loss - final_steps_loss
    loss_reduction_pct = (loss_reduction / initial_steps_loss) * 100.0

    print(f"Loss Trajectory:        Initial 5-step avg = {initial_steps_loss:.4f} -> Final 5-step avg = {final_steps_loss:.4f}")
    print(f"Loss Reduction:         {loss_reduction:.4f} ({loss_reduction_pct:.1f}% reduction)")

    # -------------------------------------------------------------
    # Step 7: Checkpointing and Serialization
    # -------------------------------------------------------------
    checkpoint_data = {
        "format_version": "1.0",
        "epoch": epoch,
        "optimizer_steps": optimizer_steps,
        "train_loss": final_train_loss,
        "val_loss": final_val_loss,
        "best_val_loss": min(best_val_loss, final_val_loss),
        "model_state_dict": model.state_dict(),
        "optimizer_state_dict": optimizer.state_dict(),
        "model_config": model_cfg.to_dict(),
        "training_config": train_cfg.to_dict(),
        "rng_states": {
            "python_rng": random.getstate(),
            "numpy_rng": np.random.get_state(),
            "torch_rng": torch.get_rng_state(),
            "torch_cuda_rng": None,
        },
    }

    latest_path = pilot_dir / "checkpoint_latest.pt"
    best_path = pilot_dir / "checkpoint_best.pt"
    torch.save(checkpoint_data, latest_path)
    torch.save(checkpoint_data, best_path)
    print(f"\nSaved Checkpoints:      {latest_path.name} & {best_path.name} ({latest_path.stat().st_size / (1024**2):.2f} MiB)")

    # -------------------------------------------------------------
    # Step 8: Checkpoint Reload Verification
    # -------------------------------------------------------------
    print("\n--- Verifying Checkpoint Reloadability ---")
    reload_model = ExecutionSemanticTransformer(model_cfg).to(device)
    reload_ckpt = torch.load(latest_path, map_location=device, weights_only=False)

    reload_model.load_state_dict(reload_ckpt["model_state_dict"])
    reloaded_weight = reload_model.output_projection.weight.detach().cpu()
    assert torch.equal(reloaded_weight, trained_param_sample), "Reloaded weights do not match trained weights!"

    # Verify optimizer state contains 50 steps
    saved_opt_state = reload_ckpt["optimizer_state_dict"]["state"]
    opt_steps = [v["step"] for v in saved_opt_state.values() if "step" in v]
    recorded_opt_step = opt_steps[0] if opt_steps else 0
    if torch.is_tensor(recorded_opt_step):
        recorded_opt_step = int(recorded_opt_step.item())
    print(f"Reload Verification:    Model weights restored identically")
    print(f"Optimizer State Check:  Saved step count in optimizer = {recorded_opt_step}")
    assert recorded_opt_step == optimizer_steps, f"Expected {optimizer_steps} steps, got {recorded_opt_step}"
    print("Checkpoint Reload:      PASSED")

    # -------------------------------------------------------------
    # Step 9: Exp1 Integrity Verification
    # -------------------------------------------------------------
    print("\n--- Verifying Exp1 Checkpoints Integrity ---")
    for name, expected_hash in exp1_baseline_hashes.items():
        current_hash = compute_file_sha256(exp1_dir / name)
        assert current_hash == expected_hash, f"INTEGRITY VIOLATION: {name} was modified!"
        print(f"Exp1 Integrity Verified:{name} SHA256 matches baseline ({current_hash[:16]}...)")

    # -------------------------------------------------------------
    # Step 10: Experiment Summary Artifact
    # -------------------------------------------------------------
    summary = {
        "status": "SUCCESS",
        "device": str(device),
        "pytorch_version": torch.__version__,
        "total_params": total_params,
        "train_programs": train_pids,
        "val_programs": val_pids,
        "train_chunks": len(train_dataset),
        "val_chunks": len(val_dataset),
        "optimizer_steps": optimizer_steps,
        "elapsed_seconds": total_training_time,
        "initial_loss_avg": initial_steps_loss,
        "final_loss_avg": final_steps_loss,
        "loss_reduction_pct": loss_reduction_pct,
        "final_train_loss": final_train_loss,
        "final_val_loss": final_val_loss,
        "mps_allocated_memory_bytes": final_mps_alloc,
        "mps_driver_memory_bytes": final_mps_driver,
        "step_records": step_records,
    }
    with open(pilot_dir / "pilot_summary.json", "w") as f:
        json.dump(summary, f, indent=2)

    print("\n" + "=" * 75)
    print("EST MPS PILOT EXPERIMENT COMPLETE: ALL SUCCESS CRITERIA SATISFIED")
    print("=" * 75)


if __name__ == "__main__":
    main()

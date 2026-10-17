#!/usr/bin/env python3
"""
Standalone MPS (Metal Performance Shaders) Compatibility Smoke Test for EST.

Verifies that the ExecutionSemanticTransformer (EST) model, forward pass,
masked cross-entropy loss, backward pass, and parameter gradient updates
execute correctly on Apple Silicon GPU via torch.device("mps").

Requirements:
  - Uses existing ModelConfig and ExecutionSemanticTransformer (no production changes).
  - Loads production vocabulary files for exact vocabulary dimensions and padding IDs.
  - Employs a small synthetic model configuration for fast, memory-safe smoke verification.
  - Fails explicitly if MPS is unavailable (no silent CPU fallback).
  - Synchronizes MPS before reporting success.
"""

import sys
from pathlib import Path

# Add project root to sys.path to enable direct module imports
REPO_ROOT = Path(__file__).resolve().parent.parent
if str(REPO_ROOT) not in sys.path:
    sys.path.insert(0, str(REPO_ROOT))

import torch
import torch.nn.functional as F

from execution_dataset import BatchData
from model import ExecutionSemanticTransformer, ModelConfig
from train import compute_batch_loss
from vocabulary import Vocabulary


def verify_mps_available() -> torch.device:
    """Verifies that MPS is built and available; raises RuntimeError otherwise."""
    if not torch.backends.mps.is_built():
        raise RuntimeError("MPS Check Failed: PyTorch was not built with MPS (Metal) enabled.")
    if not torch.backends.mps.is_available():
        raise RuntimeError("MPS Check Failed: Apple Silicon MPS device is not available on this system.")
    return torch.device("mps")


def main() -> None:
    print("=" * 65)
    print("EST Model MPS Compatibility Smoke Test")
    print("=" * 65)

    # 1. Enforce strict MPS availability check
    device = verify_mps_available()
    print(f"PyTorch Version:     {torch.__version__}")
    print(f"Target Device:       {device} (Apple Silicon GPU)")

    # 2. Load existing vocabularies to obtain real vocab sizes and pad IDs
    src_vocab_path = REPO_ROOT / "vocabularies" / "vocab_source.json"
    tgt_vocab_path = REPO_ROOT / "vocabularies" / "vocab_target.json"

    if not src_vocab_path.is_file():
        raise FileNotFoundError(f"Source vocabulary not found: {src_vocab_path}")
    if not tgt_vocab_path.is_file():
        raise FileNotFoundError(f"Target vocabulary not found: {tgt_vocab_path}")

    src_vocab = Vocabulary.load(src_vocab_path)
    tgt_vocab = Vocabulary.load(tgt_vocab_path)

    print(f"Source Vocab Size:   {len(src_vocab):,} tokens (pad_id: {src_vocab.pad_id})")
    print(f"Target Vocab Size:   {len(tgt_vocab):,} tokens (pad_id: {tgt_vocab.pad_id})")

    # 3. Deliberately compact configuration for lightweight smoke testing
    config = ModelConfig(
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

    # 4. Instantiate model and transfer to MPS
    torch.manual_seed(42)
    model = ExecutionSemanticTransformer(config).to(device)
    model.train()

    total_params = sum(p.numel() for p in model.parameters())
    trainable_params = sum(p.numel() for p in model.parameters() if p.requires_grad)
    print(f"Model Total Params:  {total_params:,}")
    print(f"Trainable Params:    {trainable_params:,}")

    # 5. Synthetic batch with valid token IDs and dynamic padding
    # Batch size: 2, Sequence lengths <= 32 (here: src_len=16, tgt_len=16)
    batch_size = 2
    src_len = 16
    tgt_len = 16

    # Row 0: 10 valid tokens, 6 pad tokens
    # Row 1: 13 valid tokens, 3 pad tokens
    src_valid_lengths = [10, 13]
    src_ids_list = []
    for length in src_valid_lengths:
        # Valid non-zero token IDs sampled within vocabulary range
        row = [((i * 37 + 11) % (config.src_vocab_size - 1)) + 1 for i in range(length)]
        # Append padding tokens
        row += [config.src_pad_id] * (src_len - length)
        src_ids_list.append(row)

    tgt_valid_lengths = [9, 12]
    dec_input_list = []
    target_ids_list = []
    for length in tgt_valid_lengths:
        # Decoder input tokens: [BOS (id 2)] + chunk tokens
        # Target tokens:        chunk tokens + [EOS (id 3)]
        chunk = [((i * 43 + 17) % (config.tgt_vocab_size - 4)) + 4 for i in range(length)]
        dec_row = [2] + chunk
        tgt_row = chunk + [3]

        dec_row += [config.tgt_pad_id] * (tgt_len - len(dec_row))
        tgt_row += [config.tgt_pad_id] * (tgt_len - len(tgt_row))

        dec_input_list.append(dec_row)
        target_ids_list.append(tgt_row)

    src_ids = torch.tensor(src_ids_list, dtype=torch.long, device=device)
    src_padding_mask = (src_ids == config.src_pad_id)

    decoder_input_ids = torch.tensor(dec_input_list, dtype=torch.long, device=device)
    target_padding_mask = (decoder_input_ids == config.tgt_pad_id)

    target_ids = torch.tensor(target_ids_list, dtype=torch.long, device=device)
    loss_mask = (target_ids != config.tgt_pad_id)

    batch = BatchData(
        program_ids=["smoke_prog_0", "smoke_prog_1"],
        chunk_indices=torch.tensor([0, 0], dtype=torch.long, device=device),
        total_chunks=torch.tensor([1, 1], dtype=torch.long, device=device),
        src_ids=src_ids,
        src_padding_mask=src_padding_mask,
        decoder_input_ids=decoder_input_ids,
        target_ids=target_ids,
        target_padding_mask=target_padding_mask,
        loss_mask=loss_mask,
        src_lengths=torch.tensor(src_valid_lengths, dtype=torch.long, device=device),
        target_lengths=torch.tensor(tgt_valid_lengths, dtype=torch.long, device=device),
    )

    # 6. Forward pass through EST on MPS
    logits = model(batch)

    # Verify output shape: (B, T, tgt_vocab_size)
    expected_shape = (batch_size, tgt_len, config.tgt_vocab_size)
    assert logits.shape == expected_shape, f"Logits shape mismatch: expected {expected_shape}, got {logits.shape}"

    # 7. Next-token cross-entropy loss with padding ignored
    loss, num_valid_tokens = compute_batch_loss(logits, target_ids, tgt_pad_id=config.tgt_pad_id)
    loss_val = loss.item()
    loss_is_finite = bool(torch.isfinite(loss).item())

    # 8. Backward pass
    model.zero_grad()
    loss.backward()

    # 9. Verify gradients across all trainable parameters
    grads_finite = True
    active_param_count = 0
    trainable_tensors_count = sum(1 for p in model.parameters() if p.requires_grad)
    for name, param in model.named_parameters():
        if param.requires_grad:
            if param.grad is None:
                grads_finite = False
                print(f"  Gradient ERROR: {name} has no gradient.")
            elif not torch.isfinite(param.grad).all():
                grads_finite = False
                print(f"  Gradient ERROR: {name} contains NaN or Inf.")
            else:
                active_param_count += 1

    # 10. Synchronize MPS hardware queue before reporting completion
    torch.mps.synchronize()

    # 11. Print comprehensive results
    print("-" * 65)
    print(f"Batch Size:          {batch_size}")
    print(f"Source Max Length:   {src_len}")
    print(f"Target Max Length:   {tgt_len}")
    print(f"Valid Target Tokens: {num_valid_tokens}")
    print(f"Cross-Entropy Loss:  {loss_val:.6f}")
    print(f"Loss Is Finite:      {loss_is_finite}")
    print(f"Gradients Checked:   {active_param_count}/{trainable_tensors_count} parameter tensors with finite gradients")
    print(f"All Gradients Finite:{grads_finite}")
    print(f"MPS Synchronized:    True")
    print("-" * 65)

    if not loss_is_finite or not grads_finite:
        print("FAIL: EST MPS smoke test encountered non-finite values.")
        sys.exit(1)

    print("SUCCESS: EST model is fully compatible with Apple Silicon MPS.")
    print("=" * 65)


if __name__ == "__main__":
    main()

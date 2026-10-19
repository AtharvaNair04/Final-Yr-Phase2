"""
EST Generation Failure Diagnostic Suite.

Executes 6 deterministic tests on checkpoints/multi_example_overfit_v1/checkpoint_best.pt:
  Test 1: Alignment and Token Shift Verification
  Test 2: Causal Mask Verification
  Test 3: Source Conditioning & Cross-Attention Sensitivity
  Test 4: Teacher-Forced vs. Greedy Divergence & Top-5 Candidate Inspection
  Test 5: EOS Token Logits and Probability Analysis
  Test 6: Vocabulary and Trace Round-Trip Serialization
"""

import json
import math
import sys
from collections import Counter
from pathlib import Path

import torch
import torch.nn.functional as F

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from execution_dataset import ExecutionTraceDataset, execution_trace_collate_fn
from model import ExecutionSemanticTransformer, ModelConfig
from vocabulary import Vocabulary

CHECKPOINT = ROOT / "checkpoints/multi_example_overfit_v1/checkpoint_best.pt"
TRAIN_JSONL = ROOT / "Module3_Training_Dataset/split/train.jsonl"
JAVA_DIR = ROOT / "selected source code"
SOURCE_VOCAB = ROOT / "vocabularies/vocab_source.json"
TARGET_VOCAB = ROOT / "vocabularies/vocab_target.json"

EXPECTED_PROGRAMS = [
    "p00014", "p00017", "p00029", "p00044",
    "p00063", "p00070", "p00084", "p00085",
]


def run_diagnostics():
    print("=" * 80)
    print("EST GENERATION FAILURE DIAGNOSTIC SUITE")
    print("=" * 80)

    device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)
    print(f"Device: {device}")

    src_vocab = Vocabulary.load(SOURCE_VOCAB)
    tgt_vocab = Vocabulary.load(TARGET_VOCAB)

    ckpt = torch.load(CHECKPOINT, map_location="cpu", weights_only=False)
    config = ModelConfig.from_dict(ckpt["model_config"])
    model = ExecutionSemanticTransformer(config)
    model.load_state_dict(ckpt["model_state_dict"], strict=True)
    model.to(device)
    model.eval()

    dataset = ExecutionTraceDataset(
        jsonl_path=TRAIN_JSONL,
        java_dir=JAVA_DIR,
        source_vocab=src_vocab,
        target_vocab=tgt_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    # Reconstruct the 8 training items
    groups = {}
    for index in range(len(dataset)):
        item = dataset[index]
        target_len = len(item.target_ids)
        if 1 < target_len <= 128 and 0 < len(item.source_ids) <= config.max_src_length:
            groups.setdefault(item.program_id, []).append((target_len, index))

    selected = []
    for pid in sorted(groups):
        groups[pid].sort()
        selected.append((pid, groups[pid][0][1]))
    selected = selected[:len(EXPECTED_PROGRAMS)]

    items = [dataset[idx] for _, idx in selected]
    first_item = items[0]  # p00014

    # -------------------------------------------------------------------------
    # TEST 1: Alignment and Token Shift Verification
    # -------------------------------------------------------------------------
    print("\n" + "=" * 80)
    print("TEST 1: ALIGNMENT & TOKEN SHIFT VERIFICATION (p00014)")
    print("=" * 80)
    bos_id = tgt_vocab.bos_id
    eos_id = tgt_vocab.eos_id
    pad_id = config.tgt_pad_id

    dec_in = first_item.decoder_input_ids
    tgt_ids = first_item.target_ids
    chk_ids = first_item.chunk_ids

    print(f"Chunk IDs length:        {len(chk_ids)}")
    print(f"Decoder Input length:    {len(dec_in)}")
    print(f"Target IDs length:       {len(tgt_ids)}")
    print(f"Decoder Input starts with BOS (id={bos_id}): {dec_in[0] == bos_id}")
    print(f"Target IDs ends with EOS (id={eos_id}):      {tgt_ids[-1] == eos_id}")

    # Check shift: dec_in[i+1] == tgt_ids[i] == chk_ids[i]
    shift_correct = True
    for i in range(len(chk_ids)):
        if dec_in[i + 1] != chk_ids[i] or tgt_ids[i] != chk_ids[i]:
            shift_correct = False
            break
    print(f"Shift property dec_in[i+1] == tgt_ids[i] == chk_ids[i]: {shift_correct}")

    print("\nFirst 15 Positions Comparison Table:")
    print(f"{'Pos':>3} | {'Decoder Input ID':>16} | {'Decoder Token':>16} | {'Target ID':>10} | {'Target Token':>16}")
    print("-" * 75)
    for pos in range(min(15, len(dec_in))):
        din_tok = tgt_vocab.decode([dec_in[pos]])[0]
        tgt_tok = tgt_vocab.decode([tgt_ids[pos]])[0]
        print(f"{pos:3d} | {dec_in[pos]:16d} | {din_tok:>16} | {tgt_ids[pos]:10d} | {tgt_tok:>16}")

    # -------------------------------------------------------------------------
    # TEST 2: Causal Mask Verification
    # -------------------------------------------------------------------------
    print("\n" + "=" * 80)
    print("TEST 2: CAUSAL MASK & FUTURE ISOLATION VERIFICATION")
    print("=" * 80)
    # Forward pass with real decoder inputs
    src_tensor = torch.tensor([first_item.source_ids], dtype=torch.long, device=device)
    src_mask = src_tensor.eq(config.src_pad_id)
    dec_tensor1 = torch.tensor([dec_in[:20]], dtype=torch.long, device=device)

    # Perturb future token at position 10 (change to arbitrary token ID 99)
    dec_tensor2 = dec_tensor1.clone()
    dec_tensor2[0, 10] = (dec_tensor2[0, 10] + 5) % (config.tgt_vocab_size - 4) + 4

    with torch.inference_mode():
        logits1 = model(src_tensor, dec_tensor1, src_padding_mask=src_mask)
        logits2 = model(src_tensor, dec_tensor2, src_padding_mask=src_mask)

    # Check logits at positions 0..9 (prior to perturbation)
    diff_prior = (logits1[0, :10] - logits2[0, :10]).abs().max().item()
    # Check logits at position 10..19 (at and after perturbation)
    diff_after = (logits1[0, 10:] - logits2[0, 10:]).abs().max().item()

    print(f"Max absolute logit difference at positions 0..9 (prior to pos 10): {diff_prior:.8f}")
    print(f"Max absolute logit difference at positions 10..19 (future tokens):  {diff_after:.8f}")
    print(f"Causal isolation strictly verified (diff_prior == 0.0): {diff_prior == 0.0}")

    # -------------------------------------------------------------------------
    # TEST 3: Source Conditioning & Cross-Attention Sensitivity
    # -------------------------------------------------------------------------
    print("\n" + "=" * 80)
    print("TEST 3: SOURCE CONDITIONING & CROSS-ATTENTION SENSITIVITY")
    print("=" * 80)
    # Item 0 (p00014) vs Item 1 (p00017)
    second_item = items[1]
    src_other = torch.tensor([second_item.source_ids], dtype=torch.long, device=device)
    src_other_mask = src_other.eq(config.src_pad_id)

    # Zeroed embedding / dummy source
    src_dummy = torch.full_like(src_tensor, fill_value=config.src_pad_id)
    src_dummy_mask = torch.ones_like(src_mask)  # all masked

    with torch.inference_mode():
        # Correct source
        mem_correct = model.encode(src_tensor, src_padding_mask=src_mask)
        h_correct = model.decode(dec_tensor1, memory=mem_correct, src_padding_mask=src_mask)
        logits_correct = model.output_projection(h_correct)

        # Other program source
        mem_other = model.encode(src_other, src_padding_mask=src_other_mask)
        h_other = model.decode(dec_tensor1, memory=mem_other, src_padding_mask=src_other_mask)
        logits_other = model.output_projection(h_other)

        # Zero source memory
        mem_zeros = torch.zeros_like(mem_correct)
        h_zeros = model.decode(dec_tensor1, memory=mem_zeros, src_padding_mask=None)
        logits_zeros = model.output_projection(h_zeros)

    diff_source = (logits_correct - logits_other).abs().max().item()
    mean_diff_source = (logits_correct - logits_other).abs().mean().item()
    diff_zeros = (logits_correct - logits_zeros).abs().max().item()

    print(f"Max logit diff (correct source vs other program source): {diff_source:.6f}")
    print(f"Mean logit diff (correct source vs other program source): {mean_diff_source:.6f}")
    print(f"Max logit diff (correct source vs zero memory):          {diff_zeros:.6f}")
    print(f"Does decoder output depend on source code? {'YES' if diff_source > 1e-3 else 'NO (COLLAPSED TO UNCONDITIONAL LM)'}")

    # -------------------------------------------------------------------------
    # TEST 4: Teacher-Forced vs. Greedy Divergence & Top-5 Candidates
    # -------------------------------------------------------------------------
    print("\n" + "=" * 80)
    print("TEST 4: STEP-BY-STEP GREEDY VS TEACHER-FORCED DIVERGENCE (p00014)")
    print("=" * 80)

    # Teacher forced forward pass on the full sequence
    full_dec = torch.tensor([dec_in], dtype=torch.long, device=device)
    with torch.inference_mode():
        full_logits = model(src_tensor, full_dec, src_padding_mask=src_mask)
        tf_preds = full_logits.argmax(dim=-1)[0].tolist()

    greedy_tokens = [bos_id]
    divergence_point = None

    print(f"{'Step':>4} | {'Ref ID':>6} | {'Ref Token':>16} | {'TF Pred ID':>10} | {'TF Token':>14} | {'Greedy Pred':>14} | Top-5 Greedy Candidates (Token: Prob)")
    print("-" * 120)

    for step in range(min(20, len(chk_ids))):
        ref_id = tgt_ids[step]
        ref_tok = tgt_vocab.decode([ref_id])[0]
        tf_pred_id = tf_preds[step]
        tf_pred_tok = tgt_vocab.decode([tf_pred_id])[0]

        # Greedy step
        curr_dec = torch.tensor([greedy_tokens], dtype=torch.long, device=device)
        with torch.inference_mode():
            mem = model.encode(src_tensor, src_padding_mask=src_mask)
            h = model.decode(curr_dec, memory=mem, src_padding_mask=src_mask)
            step_logits = model.output_projection(h[:, -1, :])[0]
            step_probs = F.softmax(step_logits, dim=-1)

        top5 = torch.topk(step_probs, k=5)
        top5_str = ", ".join(
            f"{tgt_vocab.decode([idx.item()])[0]!r}: {prob.item():.3f}"
            for idx, prob in zip(top5.indices, top5.values)
        )

        greedy_pred_id = top5.indices[0].item()
        greedy_pred_tok = tgt_vocab.decode([greedy_pred_id])[0]
        greedy_tokens.append(greedy_pred_id)

        if divergence_point is None and greedy_pred_id != ref_id:
            divergence_point = step

        mark = " *" if greedy_pred_id != ref_id else "  "
        print(f"{step:4d}{mark}| {ref_id:6d} | {ref_tok:>16} | {tf_pred_id:10d} | {tf_pred_tok:>14} | {greedy_pred_tok:>14} | {top5_str}")

    print(f"\nEarliest greedy divergence from reference: Step {divergence_point}")

    # -------------------------------------------------------------------------
    # TEST 5: EOS Token Logits and Probability Analysis
    # -------------------------------------------------------------------------
    print("\n" + "=" * 80)
    print("TEST 5: EOS TOKEN LOGITS & PROBABILITY ANALYSIS")
    print("=" * 80)
    print(f"Target Vocab EOS Token ID: {eos_id}")

    # Check EOS rank and logit across teacher-forced positions
    eos_tf_ranks = []
    eos_tf_probs = []
    last_pos = len(chk_ids)  # where EOS is target
    for pos in range(len(chk_ids) + 1):
        logits_pos = full_logits[0, pos]
        probs_pos = F.softmax(logits_pos, dim=-1)
        eos_prob = probs_pos[eos_id].item()
        # Rank: how many tokens had higher logit?
        rank = (logits_pos > logits_pos[eos_id]).sum().item() + 1
        eos_tf_ranks.append(rank)
        eos_tf_probs.append(eos_prob)

    print(f"At final position {last_pos} (where ground truth is <EOS>):")
    print(f"  Target at this position:   {tgt_vocab.decode([tgt_ids[last_pos]])[0]!r} (ID {tgt_ids[last_pos]})")
    print(f"  EOS Probability:           {eos_tf_probs[last_pos]:.6f}")
    print(f"  EOS Rank:                  {eos_tf_ranks[last_pos]} / {config.tgt_vocab_size}")
    top_at_eos = torch.topk(full_logits[0, last_pos], k=5)
    print("  Top 5 predicted at final position:")
    for idx, score in zip(top_at_eos.indices.tolist(), top_at_eos.values.tolist()):
        print(f"    ID={idx:5d} ({tgt_vocab.decode([idx])[0]!r}): logit={score:.4f}")

    print("\nEOS probability across the first 10 positions:")
    for pos in range(min(10, len(eos_tf_probs))):
        print(f"  Pos {pos:2d} (ref={tgt_vocab.decode([tgt_ids[pos]])[0]!r}): EOS prob = {eos_tf_probs[pos]:.6e}, rank = {eos_tf_ranks[pos]}")

    # -------------------------------------------------------------------------
    # TEST 6: Vocabulary and Trace Round-Trip Serialization
    # -------------------------------------------------------------------------
    print("\n" + "=" * 80)
    print("TEST 6: VOCABULARY AND TRACE ROUND-TRIP SERIALIZATION")
    print("=" * 80)
    ref_tokens = first_item.chunk_tokens
    encoded_ids = tgt_vocab.encode(ref_tokens, add_bos=False, add_eos=False)
    decoded_tokens = tgt_vocab.decode(encoded_ids, skip_special=False)
    round_trip_identical = (ref_tokens == decoded_tokens)
    unk_count = sum(1 for tok_id in encoded_ids if tok_id == tgt_vocab.unk_id)

    print(f"Total reference chunk tokens: {len(ref_tokens)}")
    print(f"Round-trip tokens identical:  {round_trip_identical}")
    print(f"Unknown (<UNK>) tokens count: {unk_count}")
    if not round_trip_identical:
        for idx, (orig, recon) in enumerate(zip(ref_tokens, decoded_tokens)):
            if orig != recon:
                print(f"  Mismatch at {idx}: original {orig!r} vs decoded {recon!r}")
                break

    print("\nStructural tokens presence in vocabulary:")
    for sym in ["<PAD>", "<UNK>", "<BOS>", "<EOS>", "<EVENT_START>", "<EVENT_END>", "<ATTR_START>", "<ATTR_END>", "TYPE", "STEP"]:
        tok_id = tgt_vocab.token_to_id.get(sym)
        print(f"  {sym:15s} -> ID {tok_id}")

    print("\n" + "=" * 80)
    print("DIAGNOSTICS COMPLETE")
    print("=" * 80)


if __name__ == "__main__":
    run_diagnostics()

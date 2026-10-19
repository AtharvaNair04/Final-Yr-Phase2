#!/usr/bin/env python3
"""
Controlled Multi-Example Overfitting Convergence Experiment.

Trains the existing ExecutionSemanticTransformer (EST) on the 8 training examples
to measure whether extended optimization enables true memorization and improves
ordinary greedy autoregressive generation.

Evaluates periodically at checkpoints:
  - Training loss and perplexity
  - Batch teacher-forced accuracy
  - Per-example teacher-forced token accuracy
  - Ordinary greedy generation token accuracy
  - Exact sequence matches
  - EOS termination rate
  - Generated sequence lengths
  - Structural validity (via validate_trace)

Outputs:
  Checkpoints saved to: checkpoints/multi_example_overfit_v2/
  Metrics and summary saved to: results/inference/overfit_experiment_v2/
"""

from collections import Counter
import json
from pathlib import Path
import random
import sys
import time
from typing import Any, Dict, List, Tuple

import torch
import torch.nn.functional as F

# Ensure repository root is on sys.path
ROOT = Path(__file__).resolve().parents[1]
if str(ROOT) not in sys.path:
    sys.path.insert(0, str(ROOT))

from execution_dataset import ExecutionTraceDataset, execution_trace_collate_fn
from model import ExecutionSemanticTransformer, ModelConfig
from scripts.run_inference_demo import validate_trace
from train import compute_batch_loss
from vocabulary import Vocabulary

# Inputs
INITIAL_CHECKPOINT = ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"
TRAIN_JSONL = ROOT / "Module3_Training_Dataset/split/train.jsonl"
JAVA_DIR = ROOT / "selected source code"
SOURCE_VOCAB_PATH = ROOT / "vocabularies/vocab_source.json"
TARGET_VOCAB_PATH = ROOT / "vocabularies/vocab_target.json"

# Outputs (Strictly new directories)
CKPT_DIR = ROOT / "checkpoints/multi_example_overfit_v2"
RESULTS_DIR = ROOT / "results/inference/overfit_experiment_v2"

# Hyperparameters
SEED = 42
MAX_STEPS = 1000
EVAL_INTERVAL = 200
CHECKPOINT_INTERVAL = 200
MAX_SECONDS = 360  # 6-minute safeguard
LEARNING_RATE = 0.0003
WEIGHT_DECAY = 0.01
GRAD_CLIP_NORM = 1.0
MAX_NEW_TOKENS = 160  # Max length for greedy generation evaluation

EXPECTED_TRAIN_PROGRAMS = [
    "p00014", "p00017", "p00029", "p00044",
    "p00063", "p00070", "p00084", "p00085",
]


def set_seed(seed: int) -> None:
    random.seed(seed)
    torch.manual_seed(seed)


def compute_token_accuracy(pred: List[str], ref: List[str]) -> float:
    """Computes positional token accuracy over min(len(pred), len(ref)), divided by len(ref)."""
    if not ref:
        return 1.0 if not pred else 0.0
    comparable = min(len(pred), len(ref))
    correct = sum(pred[i] == ref[i] for i in range(comparable))
    return float(correct / len(ref))


def evaluate_greedy_generation(
    model: ExecutionSemanticTransformer,
    item: Any,
    config: ModelConfig,
    target_vocab: Vocabulary,
    device: torch.device,
    max_len: int = MAX_NEW_TOKENS,
) -> Dict[str, Any]:
    """Generates tokens using ordinary greedy decoding (argmax) and evaluates metrics."""
    src_ids = torch.tensor([item.source_ids], dtype=torch.long, device=device)
    src_mask = src_ids.eq(config.src_pad_id)
    bos_id = target_vocab.bos_id
    eos_id = target_vocab.eos_id
    pad_id = config.tgt_pad_id

    reference_tokens = list(item.chunk_tokens)
    ref_ids = list(item.chunk_ids)

    generated = [bos_id]
    eos_reached = False

    with torch.inference_mode():
        memory = model.encode(src_ids, src_padding_mask=src_mask)

        for _ in range(max_len):
            dec_tensor = torch.tensor([generated], dtype=torch.long, device=device)
            tgt_mask = dec_tensor.eq(pad_id)

            hidden = model.decode(
                decoder_input_ids=dec_tensor,
                memory=memory,
                target_padding_mask=tgt_mask,
                src_padding_mask=src_mask,
            )
            logits = model.output_projection(hidden[:, -1, :])[0]
            next_id = int(logits.argmax(dim=-1).item())

            if next_id == eos_id:
                eos_reached = True
                break
            generated.append(next_id)

        # Teacher-forced evaluation for this item
        dec_in = [bos_id] + ref_ids
        dec_in = dec_in[:len(ref_ids)]
        dec_tensor = torch.tensor([dec_in], dtype=torch.long, device=device)
        tf_hidden = model.decode(
            decoder_input_ids=dec_tensor,
            memory=memory,
            target_padding_mask=dec_tensor.eq(pad_id),
            src_padding_mask=src_mask,
        )
        tf_logits = model.output_projection(tf_hidden)
        tf_preds = tf_logits.argmax(dim=-1)[0].tolist()

    pred_tokens = target_vocab.decode(generated[1:], skip_special=False)
    tf_count = min(len(ref_ids), len(tf_preds))
    tf_acc = sum(tf_preds[i] == ref_ids[i] for i in range(tf_count)) / max(len(ref_ids), 1)

    struct_res = validate_trace(pred_tokens)
    tok_acc = compute_token_accuracy(pred_tokens, reference_tokens)
    exact_match = (pred_tokens == reference_tokens)

    return {
        "program_id": item.program_id,
        "chunk_index": item.chunk_index,
        "ref_length": len(reference_tokens),
        "generated_length": len(pred_tokens),
        "teacher_forced_accuracy": float(tf_acc),
        "greedy_token_accuracy": float(tok_acc),
        "exact_match": exact_match,
        "eos_reached": eos_reached,
        "structurally_valid": struct_res["valid"],
        "structure_errors": struct_res.get("errors", []),
        "unique_tokens": len(set(pred_tokens)),
        "preview": pred_tokens[:30],
    }


def full_evaluation(
    model: ExecutionSemanticTransformer,
    batch: Any,
    items: List[Any],
    config: ModelConfig,
    target_vocab: Vocabulary,
    device: torch.device,
    step: int,
) -> Dict[str, Any]:
    """Runs batch teacher-forcing and per-example greedy evaluation."""
    model.eval()

    # 1. Batch Teacher-Forcing Loss & Accuracy
    with torch.inference_mode():
        logits = model(batch)
        loss, valid_tokens = compute_batch_loss(
            logits, batch.target_ids, tgt_pad_id=target_vocab.pad_id
        )
        preds = logits.argmax(dim=-1)
        mask = batch.target_ids.ne(target_vocab.pad_id)
        batch_tf_acc = (preds.eq(batch.target_ids) & mask).sum().item() / max(valid_tokens, 1)

    loss_val = float(loss.item())

    # 2. Per-Item Greedy Evaluations
    item_results = []
    for item in items:
        res = evaluate_greedy_generation(model, item, config, target_vocab, device)
        item_results.append(res)

    mean_greedy_acc = sum(r["greedy_token_accuracy"] for r in item_results) / len(item_results)
    mean_tf_acc = sum(r["teacher_forced_accuracy"] for r in item_results) / len(item_results)
    exact_matches = sum(1 for r in item_results if r["exact_match"])
    eos_count = sum(1 for r in item_results if r["eos_reached"])
    valid_struct_count = sum(1 for r in item_results if r["structurally_valid"])
    mean_gen_len = sum(r["generated_length"] for r in item_results) / len(item_results)

    return {
        "step": step,
        "loss": loss_val,
        "perplexity": float(torch.exp(torch.tensor(loss_val)).item()),
        "batch_teacher_forced_accuracy": float(batch_tf_acc),
        "mean_teacher_forced_accuracy": float(mean_tf_acc),
        "mean_greedy_accuracy": float(mean_greedy_acc),
        "exact_matches": exact_matches,
        "eos_terminations": eos_count,
        "structurally_valid_count": valid_struct_count,
        "mean_generated_length": float(mean_gen_len),
        "examples": item_results,
    }


def main():
    print("=" * 80)
    print("EST MULTI-EXAMPLE OVERFITTING CONVERGENCE EXPERIMENT")
    print("=" * 80)

    # 1. Path Safety Verification
    if CKPT_DIR.exists():
        raise RuntimeError(f"Safety abort: Checkpoint directory already exists: {CKPT_DIR}")
    if RESULTS_DIR.exists():
        raise RuntimeError(f"Safety abort: Results directory already exists: {RESULTS_DIR}")

    if not INITIAL_CHECKPOINT.is_file():
        raise FileNotFoundError(f"Initial checkpoint missing: {INITIAL_CHECKPOINT}")
    if not TRAIN_JSONL.is_file():
        raise FileNotFoundError(f"Training split missing: {TRAIN_JSONL}")

    set_seed(SEED)

    device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)

    print(f"Device:             {device}")
    print(f"Initial Checkpoint: {INITIAL_CHECKPOINT.relative_to(ROOT)}")
    print(f"Target CKPT Dir:    {CKPT_DIR.relative_to(ROOT)}")
    print(f"Target Results Dir: {RESULTS_DIR.relative_to(ROOT)}")
    print(f"Step Budget:        {MAX_STEPS} steps (timeout: {MAX_SECONDS}s)")
    print(f"Learning Rate:      {LEARNING_RATE} (AdamW)")

    # 2. Load Vocabularies & Model
    src_vocab = Vocabulary.load(SOURCE_VOCAB_PATH)
    tgt_vocab = Vocabulary.load(TARGET_VOCAB_PATH)

    ckpt_data = torch.load(INITIAL_CHECKPOINT, map_location="cpu", weights_only=False)
    config = ModelConfig.from_dict(ckpt_data["model_config"])

    model = ExecutionSemanticTransformer(config)
    model.load_state_dict(ckpt_data["model_state_dict"], strict=True)
    model.to(device)

    # 3. Load & Match 8 Training Examples
    dataset = ExecutionTraceDataset(
        jsonl_path=TRAIN_JSONL,
        java_dir=JAVA_DIR,
        source_vocab=src_vocab,
        target_vocab=tgt_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    groups: Dict[str, List[Tuple[int, int]]] = {}
    for index in range(len(dataset)):
        item = dataset[index]
        target_len = len(item.target_ids)
        if 1 < target_len <= 128 and 0 < len(item.source_ids) <= config.max_src_length:
            groups.setdefault(item.program_id, []).append((target_len, index))

    selected = []
    for pid in sorted(groups):
        groups[pid].sort()
        selected.append((pid, groups[pid][0][1]))

    selected = selected[:len(EXPECTED_TRAIN_PROGRAMS)]
    actual_pids = [pid for pid, _ in selected]

    if actual_pids != EXPECTED_TRAIN_PROGRAMS:
        raise RuntimeError(
            f"Program mismatch! Expected: {EXPECTED_TRAIN_PROGRAMS}, Got: {actual_pids}"
        )

    items = [dataset[idx] for _, idx in selected]
    print(f"\nVerified 8 Training Examples: {actual_pids}")

    # Build Collated Training Batch
    batch = execution_trace_collate_fn(
        items,
        src_pad_id=src_vocab.pad_id,
        tgt_pad_id=tgt_vocab.pad_id,
    ).to(device)

    valid_tokens = int(batch.target_ids.ne(tgt_vocab.pad_id).sum().item())
    print(f"Batch Shape: Source={tuple(batch.src_ids.shape)}, Target={tuple(batch.target_ids.shape)}")
    print(f"Total Valid Target Tokens in Batch: {valid_tokens}")

    # 4. Create Directories
    CKPT_DIR.mkdir(parents=True)
    RESULTS_DIR.mkdir(parents=True)

    # 5. Evaluate Step 0 (Initial Baseline)
    print("\n--- Evaluating Step 0 (Initial Baseline) ---")
    initial_eval = full_evaluation(model, batch, items, config, tgt_vocab, device, step=0)
    print(
        f"Step   0 | Loss: {initial_eval['loss']:.4f} (PPL: {initial_eval['perplexity']:.2f}) | "
        f"TF Acc: {initial_eval['batch_teacher_forced_accuracy']:.2%} | "
        f"Greedy Acc: {initial_eval['mean_greedy_accuracy']:.2%} | "
        f"Exact: {initial_eval['exact_matches']}/8 | "
        f"EOS: {initial_eval['eos_terminations']}/8 | "
        f"Valid Struct: {initial_eval['structurally_valid_count']}/8"
    )

    # Save initial checkpoint
    torch.save(
        {
            "step": 0,
            "model_state_dict": model.state_dict(),
            "model_config": config.to_dict(),
            "metrics": initial_eval,
            "initial_checkpoint": str(INITIAL_CHECKPOINT.relative_to(ROOT)),
        },
        CKPT_DIR / "checkpoint_initial.pt",
    )

    # 6. Training Loop
    optimizer = torch.optim.AdamW(
        model.parameters(),
        lr=LEARNING_RATE,
        weight_decay=WEIGHT_DECAY,
    )

    history = [initial_eval]
    best_loss = initial_eval["loss"]
    best_step = 0
    start_time = time.time()

    print("\n--- Starting Convergence Training Loop ---")
    for step in range(1, MAX_STEPS + 1):
        elapsed = time.time() - start_time
        if elapsed >= MAX_SECONDS:
            print(f"\nSafety timeout of {MAX_SECONDS}s reached at step {step - 1}.")
            break

        model.train()
        optimizer.zero_grad(set_to_none=True)

        logits = model(batch)
        loss, n_valid = compute_batch_loss(logits, batch.target_ids, tgt_pad_id=tgt_vocab.pad_id)

        if not torch.isfinite(loss):
            raise FloatingPointError(f"Encountered non-finite loss at step {step}: {loss.item()}")

        loss.backward()

        if GRAD_CLIP_NORM > 0.0:
            torch.nn.utils.clip_grad_norm_(model.parameters(), max_norm=GRAD_CLIP_NORM)

        optimizer.step()

        if device.type == "mps":
            torch.mps.synchronize()

        current_loss = float(loss.item())

        # Check for new best loss
        if current_loss < best_loss:
            best_loss = current_loss
            best_step = step
            torch.save(
                {
                    "step": step,
                    "model_state_dict": model.state_dict(),
                    "model_config": config.to_dict(),
                    "loss": best_loss,
                    "optimizer_state_dict": optimizer.state_dict(),
                },
                CKPT_DIR / "checkpoint_best.pt",
            )

        # Periodic Full Evaluation
        if step % EVAL_INTERVAL == 0 or step == MAX_STEPS:
            eval_metrics = full_evaluation(model, batch, items, config, tgt_vocab, device, step=step)
            history.append(eval_metrics)
            print(
                f"Step {step:4d} | Loss: {eval_metrics['loss']:.4f} (PPL: {eval_metrics['perplexity']:.2f}) | "
                f"TF Acc: {eval_metrics['batch_teacher_forced_accuracy']:.2%} | "
                f"Greedy Acc: {eval_metrics['mean_greedy_accuracy']:.2%} | "
                f"Exact: {eval_metrics['exact_matches']}/8 | "
                f"EOS: {eval_metrics['eos_terminations']}/8 | "
                f"Valid: {eval_metrics['structurally_valid_count']}/8 | "
                f"MeanLen: {eval_metrics['mean_generated_length']:.1f}"
            )

        # Periodic Checkpoint Saving
        if step % CHECKPOINT_INTERVAL == 0:
            torch.save(
                {
                    "step": step,
                    "model_state_dict": model.state_dict(),
                    "model_config": config.to_dict(),
                    "loss": current_loss,
                },
                CKPT_DIR / f"checkpoint_step_{step}.pt",
            )

    # 7. Final Checkpoint & Evaluation
    final_step = step if "step" in locals() else MAX_STEPS
    final_eval = full_evaluation(model, batch, items, config, tgt_vocab, device, step=final_step)

    torch.save(
        {
            "step": final_step,
            "model_state_dict": model.state_dict(),
            "model_config": config.to_dict(),
            "loss": float(final_eval["loss"]),
            "metrics": final_eval,
            "best_step": best_step,
            "best_loss": best_loss,
        },
        CKPT_DIR / "checkpoint_final.pt",
    )

    total_time = time.time() - start_time
    print(f"\nTraining completed: {final_step} steps in {total_time:.2f}s (Best Step: {best_step}, Best Loss: {best_loss:.4f})")

    # 8. Output Serialization
    # Machine-readable metrics.json
    metrics_output = {
        "experiment": "multi_example_overfit_v2_convergence",
        "device": str(device),
        "initial_checkpoint": str(INITIAL_CHECKPOINT.relative_to(ROOT)),
        "programs": actual_pids,
        "max_steps": MAX_STEPS,
        "steps_completed": final_step,
        "total_elapsed_seconds": total_time,
        "learning_rate": LEARNING_RATE,
        "weight_decay": WEIGHT_DECAY,
        "grad_clip_norm": GRAD_CLIP_NORM,
        "initial_metrics": initial_eval,
        "best_step": best_step,
        "best_loss": best_loss,
        "final_metrics": final_eval,
        "trajectory": [
            {k: v for k, v in h.items() if k != "examples"} for h in history
        ],
        "final_per_example_details": final_eval["examples"],
    }

    with open(RESULTS_DIR / "metrics.json", "w", encoding="utf-8") as f:
        json.dump(metrics_output, f, indent=2, ensure_ascii=False)

    # Concise Summary Markdown Report
    summary_md = generate_summary_report(metrics_output, history, final_eval)
    with open(RESULTS_DIR / "summary.md", "w", encoding="utf-8") as f:
        f.write(summary_md)

    print("\n" + "=" * 80)
    print("EXPERIMENT COMPLETED SUCCESSFULLY")
    print(f"Checkpoints saved to: {CKPT_DIR.relative_to(ROOT)}")
    print(f"Results saved to:     {RESULTS_DIR.relative_to(ROOT)}")
    print(f"  - {RESULTS_DIR / 'metrics.json'}")
    print(f"  - {RESULTS_DIR / 'summary.md'}")
    print("=" * 80)


def generate_summary_report(
    meta: Dict[str, Any],
    history: List[Dict[str, Any]],
    final_eval: Dict[str, Any],
) -> str:
    lines = []
    lines.append("# EST Multi-Example Overfitting Convergence Experiment: Summary Report")
    lines.append("")
    lines.append(f"**Experiment:** `multi_example_overfit_v2_convergence`  ")
    lines.append(f"**Device:** `{meta['device']}`  ")
    lines.append(f"**Initial Checkpoint:** `{meta['initial_checkpoint']}`  ")
    lines.append(f"**Evaluated Programs (8):** `{meta['programs']}`  ")
    lines.append(f"**Optimization:** {meta['steps_completed']} steps, AdamW (LR={meta['learning_rate']}, Weight Decay={meta['weight_decay']})  ")
    lines.append(f"**Runtime:** {meta['total_elapsed_seconds']:.2f}s  ")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## 1. Training & Evaluation Trajectory")
    lines.append("")
    lines.append("| Step | Loss | Perplexity | Batch TF Acc | Mean TF Acc | Greedy Acc | Exact Match | EOS Count | Valid Struct | Mean Gen Len |")
    lines.append("| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |")

    for h in history:
        lines.append(
            f"| {h['step']:4d} | {h['loss']:.4f} | {h['perplexity']:.2f} | "
            f"{h['batch_teacher_forced_accuracy']:.2%} | {h['mean_teacher_forced_accuracy']:.2%} | "
            f"{h['mean_greedy_accuracy']:.2%} | {h['exact_matches']}/8 | "
            f"{h['eos_terminations']}/8 | {h['structurally_valid_count']}/8 | "
            f"{h['mean_generated_length']:.1f} |"
        )

    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## 2. Final Per-Example Breakdown (Step {})".format(final_eval["step"]))
    lines.append("")
    lines.append("| Program ID | Ref Len | Gen Len | TF Acc | Greedy Acc | Exact Match | EOS Reached | Valid Struct | Unique Toks |")
    lines.append("| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |")

    for ex in final_eval["examples"]:
        lines.append(
            f"| `{ex['program_id']}` | {ex['ref_length']} | {ex['generated_length']} | "
            f"{ex['teacher_forced_accuracy']:.2%} | {ex['greedy_token_accuracy']:.2%} | "
            f"{ex['exact_match']} | {ex['eos_reached']} | "
            f"{ex['structurally_valid']} | {ex['unique_tokens']} |"
        )

    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## 3. Analysis & Conclusions")
    lines.append("")
    lines.append("### A. Did the model learn to memorize the training traces?")
    init_loss = history[0]["loss"]
    final_loss = final_eval["loss"]
    lines.append(f"- Initial Loss: **{init_loss:.4f}** $\\rightarrow$ Final Loss: **{final_loss:.4f}** (Best: **{meta['best_loss']:.4f}** at step {meta['best_step']}).")
    lines.append(f"- Teacher-Forced Accuracy: **{history[0]['mean_teacher_forced_accuracy']:.2%}** $\\rightarrow$ **{final_eval['mean_teacher_forced_accuracy']:.2%}**.")
    lines.append("")
    lines.append("### B. Did ordinary greedy generation improve as loss decreased?")
    lines.append(f"- Greedy Token Accuracy: **{history[0]['mean_greedy_accuracy']:.2%}** $\\rightarrow$ **{final_eval['mean_greedy_accuracy']:.2%}**.")
    lines.append(f"- Exact Matches: **{history[0]['exact_matches']}/8** $\\rightarrow$ **{final_eval['exact_matches']}/8**.")
    lines.append(f"- EOS Terminations: **{history[0]['eos_terminations']}/8** $\\rightarrow$ **{final_eval['eos_terminations']}/8**.")
    lines.append(f"- Structurally Valid Traces: **{history[0]['structurally_valid_count']}/8** $\\rightarrow$ **{final_eval['structurally_valid_count']}/8**.")
    lines.append("")

    return "\n".join(lines)


if __name__ == "__main__":
    main()

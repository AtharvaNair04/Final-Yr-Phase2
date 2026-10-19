import json
import random
import time
from pathlib import Path

import torch

from execution_dataset import ExecutionTraceDataset, execution_trace_collate_fn
from model import ExecutionSemanticTransformer, ModelConfig
from train import compute_batch_loss
from vocabulary import Vocabulary


ROOT = Path(__file__).resolve().parents[1]
CHECKPOINT = ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"
TRAIN_JSONL = ROOT / "Module3_Training_Dataset/split/train.jsonl"
JAVA_DIR = ROOT / "selected source code"
OUT = ROOT / "checkpoints/multi_example_overfit_v1"

SEED = 42
EXAMPLES = 8
MAX_TARGET_LENGTH = 128
MAX_STEPS = 300
MAX_SECONDS = 180
LEARNING_RATE = 0.0001

random.seed(SEED)
torch.manual_seed(SEED)


def main():
    if OUT.exists():
        raise RuntimeError(f"Output already exists; refusing to overwrite: {OUT}")

    for path in (CHECKPOINT, TRAIN_JSONL):
        if not path.is_file():
            raise FileNotFoundError(path)

    device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)
    print("Device:", device)

    src_vocab = Vocabulary.load(ROOT / "vocabularies/vocab_source.json")
    tgt_vocab = Vocabulary.load(ROOT / "vocabularies/vocab_target.json")

    checkpoint = torch.load(CHECKPOINT, map_location="cpu", weights_only=False)
    config = ModelConfig.from_dict(checkpoint["model_config"])

    if config.src_vocab_size != len(src_vocab) or config.tgt_vocab_size != len(tgt_vocab):
        raise ValueError("Checkpoint and vocabulary sizes do not match.")

    model = ExecutionSemanticTransformer(config).to(device)
    model.load_state_dict(checkpoint["model_state_dict"], strict=True)

    dataset = ExecutionTraceDataset(
        TRAIN_JSONL,
        JAVA_DIR,
        src_vocab,
        tgt_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    # Select short target chunks from distinct programs.
    groups = {}
    for index in range(len(dataset)):
        item = dataset[index]
        target_len = len(item.target_ids)
        if 1 < target_len <= MAX_TARGET_LENGTH and 0 < len(item.source_ids) <= config.max_src_length:
            groups.setdefault(item.program_id, []).append((target_len, index))

    candidates = []
    for program_id in sorted(groups):
        groups[program_id].sort()
        candidates.append((program_id, groups[program_id][0][1]))

    if len(candidates) < EXAMPLES:
        raise RuntimeError(f"Only {len(candidates)} suitable programs found.")

    selected = [dataset[index] for _, index in candidates[:EXAMPLES]]
    program_ids = [item.program_id for item in selected]

    batch = execution_trace_collate_fn(
        selected,
        src_pad_id=src_vocab.pad_id,
        tgt_pad_id=tgt_vocab.pad_id,
    ).to(device)

    print("Selected programs:", program_ids)
    print("Number of examples:", len(selected))
    print("Source batch shape:", tuple(batch.src_ids.shape))
    print("Target batch shape:", tuple(batch.target_ids.shape))
    print("Valid target tokens:", int(batch.target_ids.ne(tgt_vocab.pad_id).sum().item()))

    def evaluate():
        model.eval()
        with torch.inference_mode():
            logits = model(batch)
            loss, valid = compute_batch_loss(
                logits, batch.target_ids, tgt_pad_id=tgt_vocab.pad_id
            )
            predictions = logits.argmax(dim=-1)
            mask = batch.target_ids.ne(tgt_vocab.pad_id)
            accuracy = (predictions.eq(batch.target_ids) & mask).sum().item() / max(valid, 1)
            return {
                "loss": float(loss.item()),
                "token_accuracy": float(accuracy),
                "valid_tokens": int(valid),
                "unique_predicted_ids": int(predictions[mask].unique().numel()),
            }

    initial_metrics = evaluate()
    print("Initial metrics:", initial_metrics)

    optimizer = torch.optim.AdamW(model.parameters(), lr=LEARNING_RATE, weight_decay=0.01)
    records = []
    start = time.time()
    best_loss = initial_metrics["loss"]
    best_step = 0

    OUT.mkdir(parents=True)
    torch.save(
        {
            "model_state_dict": model.state_dict(),
            "model_config": config.to_dict(),
            "step": 0,
            "source_checkpoint": str(CHECKPOINT),
            "metrics": initial_metrics,
        },
        OUT / "checkpoint_initial.pt",
    )

    for step in range(1, MAX_STEPS + 1):
        if time.time() - start >= MAX_SECONDS:
            print("Time limit reached; stopping safely.")
            break

        model.train()
        optimizer.zero_grad(set_to_none=True)
        logits = model(batch)
        loss, valid = compute_batch_loss(
            logits, batch.target_ids, tgt_pad_id=tgt_vocab.pad_id
        )

        if valid == 0 or not torch.isfinite(loss).item():
            raise RuntimeError(f"Invalid loss at step {step}")

        loss.backward()
        grad_norm = torch.nn.utils.clip_grad_norm_(model.parameters(), 1.0)
        if not torch.isfinite(grad_norm).item():
            raise RuntimeError(f"Non-finite gradient norm at step {step}")

        optimizer.step()
        if device.type == "mps":
            torch.mps.synchronize()

        if step == 1 or step % 25 == 0:
            metrics = evaluate()
            record = {"step": step, **metrics}
            records.append(record)
            print(
                f"Step {step:3d} | loss={metrics['loss']:.4f} | "
                f"token accuracy={metrics['token_accuracy']:.2%} | "
                f"unique predictions={metrics['unique_predicted_ids']}"
            )

            if metrics["loss"] < best_loss:
                best_loss = metrics["loss"]
                best_step = step
                torch.save(
                    {
                        "model_state_dict": model.state_dict(),
                        "model_config": config.to_dict(),
                        "step": step,
                        "source_checkpoint": str(CHECKPOINT),
                        "metrics": metrics,
                    },
                    OUT / "checkpoint_best.pt",
                )

    final_metrics = evaluate()
    summary = {
        "experiment": "multi_example_overfit_v1",
        "device": str(device),
        "source_checkpoint": str(CHECKPOINT.relative_to(ROOT)),
        "program_ids": program_ids,
        "examples": len(selected),
        "max_target_length": MAX_TARGET_LENGTH,
        "steps_completed": step if "step" in locals() else 0,
        "elapsed_seconds": time.time() - start,
        "initial_metrics": initial_metrics,
        "final_metrics": final_metrics,
        "best_loss": best_loss,
        "best_step": best_step,
        "records": records,
        "interpretation_note": (
            "Training-set memorization diagnostic only. High training accuracy "
            "does not establish generalization or valid autoregressive generation."
        ),
    }

    torch.save(
        {
            "model_state_dict": model.state_dict(),
            "model_config": config.to_dict(),
            "step": summary["steps_completed"],
            "source_checkpoint": str(CHECKPOINT),
            "metrics": final_metrics,
        },
        OUT / "checkpoint_final.pt",
    )
    (OUT / "summary.json").write_text(json.dumps(summary, indent=2), encoding="utf-8")

    print("\nExperiment complete.")
    print("Final metrics:", final_metrics)
    print("Best loss:", best_loss, "at step", best_step)
    print("Saved to:", OUT)
    print("Original checkpoints were not modified.")


if __name__ == "__main__":
    main()

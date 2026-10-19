
import json
import random
import time
from pathlib import Path

import torch

from execution_dataset import (
    ExecutionTraceDataset,
    execution_trace_collate_fn,
)
from model import ExecutionSemanticTransformer, ModelConfig
from train import compute_batch_loss
from vocabulary import Vocabulary


ROOT = Path(__file__).resolve().parents[1]

INITIAL_CHECKPOINT = (
    ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"
)
TRAIN_JSONL = ROOT / "Module3_Training_Dataset/split/train.jsonl"
VAL_JSONL = ROOT / "Module3_Training_Dataset/split/validation.jsonl"
JAVA_DIR = ROOT / "selected source code"

OUT = ROOT / "checkpoints/validation_monitored_v1"

SEED = 42
MAX_TARGET_LENGTH = 512
MAX_TRAIN_EXAMPLES = 24
MAX_VAL_EXAMPLES = 8
BATCH_SIZE = 2

MAX_STEPS = 200
MAX_SECONDS = 240
EVALUATE_EVERY = 25
LEARNING_RATE = 0.0001
WEIGHT_DECAY = 0.01


def select_short_examples(dataset, limit, max_target_length, config):
    """Select short chunks from distinct programs where possible."""
    groups = {}

    for index in range(len(dataset)):
        item = dataset[index]
        target_len = len(item.target_ids)

        if (
            1 < target_len <= max_target_length
            and 0 < len(item.source_ids) <= config.max_src_length
        ):
            groups.setdefault(item.program_id, []).append(
                (target_len, index)
            )

    for program_id in groups:
        groups[program_id].sort()

    selected_indices = []
    depth = 0
    program_ids = sorted(groups)

    while len(selected_indices) < limit:
        added = False

        for program_id in program_ids:
            if depth < len(groups[program_id]):
                selected_indices.append(groups[program_id][depth][1])
                added = True

                if len(selected_indices) >= limit:
                    break

        if not added:
            break

        depth += 1

    return [dataset[index] for index in selected_indices]


def make_batches(items, src_vocab, tgt_vocab, device):
    batches = []

    for start in range(0, len(items), BATCH_SIZE):
        batch = execution_trace_collate_fn(
            items[start:start + BATCH_SIZE],
            src_pad_id=src_vocab.pad_id,
            tgt_pad_id=tgt_vocab.pad_id,
        )
        batches.append(batch.to(device))

    return batches


def evaluate(model, batches, tgt_pad_id):
    model.eval()
    weighted_loss = 0.0
    valid_tokens = 0
    correct_tokens = 0

    with torch.inference_mode():
        for batch in batches:
            logits = model(batch)
            loss, count = compute_batch_loss(
                logits,
                batch.target_ids,
                tgt_pad_id=tgt_pad_id,
            )

            predictions = logits.argmax(dim=-1)
            correct_tokens += (
                predictions.eq(batch.target_ids)
                & batch.target_ids.ne(tgt_pad_id)
            ).sum().item()

            weighted_loss += loss.item() * count
            valid_tokens += count

    return {
        "loss": weighted_loss / max(valid_tokens, 1),
        "token_accuracy": correct_tokens / max(valid_tokens, 1),
        "valid_tokens": valid_tokens,
    }


def main():
    random.seed(SEED)
    torch.manual_seed(SEED)

    if OUT.exists():
        raise RuntimeError(
            f"Output directory already exists: {OUT}\n"
            "Refusing to overwrite an existing experiment."
        )

    for path in (INITIAL_CHECKPOINT, TRAIN_JSONL, VAL_JSONL):
        if not path.is_file():
            raise FileNotFoundError(path)

    device = torch.device(
        "mps" if torch.backends.mps.is_available() else "cpu"
    )

    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)

    print("Device:", device)

    src_vocab = Vocabulary.load(
        ROOT / "vocabularies/vocab_source.json"
    )
    tgt_vocab = Vocabulary.load(
        ROOT / "vocabularies/vocab_target.json"
    )

    checkpoint = torch.load(
        INITIAL_CHECKPOINT,
        map_location="cpu",
        weights_only=False,
    )

    config = ModelConfig.from_dict(checkpoint["model_config"])

    if (
        config.src_vocab_size != len(src_vocab)
        or config.tgt_vocab_size != len(tgt_vocab)
    ):
        raise ValueError("Checkpoint/vocabulary size mismatch.")

    model = ExecutionSemanticTransformer(config).to(device)
    model.load_state_dict(
        checkpoint["model_state_dict"],
        strict=True,
    )

    train_dataset = ExecutionTraceDataset(
        TRAIN_JSONL,
        JAVA_DIR,
        src_vocab,
        tgt_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    val_dataset = ExecutionTraceDataset(
        VAL_JSONL,
        JAVA_DIR,
        src_vocab,
        tgt_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    train_items = select_short_examples(
        train_dataset,
        MAX_TRAIN_EXAMPLES,
        MAX_TARGET_LENGTH,
        config,
    )
    val_items = select_short_examples(
        val_dataset,
        MAX_VAL_EXAMPLES,
        MAX_TARGET_LENGTH,
        config,
    )

    if len(train_items) < 4 or not val_items:
        raise RuntimeError(
            f"Insufficient examples: train={len(train_items)}, "
            f"validation={len(val_items)}"
        )

    train_programs = sorted(
        {item.program_id for item in train_items}
    )
    val_programs = sorted(
        {item.program_id for item in val_items}
    )

    if set(train_programs) & set(val_programs):
        raise RuntimeError(
            "Training and validation programs overlap."
        )

    train_batches = make_batches(
        train_items, src_vocab, tgt_vocab, device
    )
    val_batches = make_batches(
        val_items, src_vocab, tgt_vocab, device
    )

    optimizer = torch.optim.AdamW(
        model.parameters(),
        lr=LEARNING_RATE,
        weight_decay=WEIGHT_DECAY,
    )

    print("Initial checkpoint:", INITIAL_CHECKPOINT.name)
    print("Training examples:", len(train_items))
    print("Training programs:", train_programs)
    print("Validation examples:", len(val_items))
    print("Validation programs:", val_programs)

    print("\nEvaluating initial model...")
    initial_train = evaluate(
        model, train_batches, tgt_vocab.pad_id
    )
    initial_val = evaluate(
        model, val_batches, tgt_vocab.pad_id
    )

    print("Initial train:", initial_train)
    print("Initial validation:", initial_val)

    best_val_loss = initial_val["loss"]
    best_step = 0
    records = []
    start_time = time.time()
    steps_completed = 0

    OUT.mkdir(parents=True)

    # Save the starting state so the experiment remains reproducible.
    torch.save(
        {
            "model_state_dict": model.state_dict(),
            "model_config": config.to_dict(),
            "step": 0,
            "validation": initial_val,
            "source_checkpoint": str(INITIAL_CHECKPOINT),
        },
        OUT / "checkpoint_initial.pt",
    )

    for step in range(1, MAX_STEPS + 1):
        if time.time() - start_time >= MAX_SECONDS:
            print("Time limit reached; stopping safely.")
            break

        model.train()
        batch = train_batches[(step - 1) % len(train_batches)]

        optimizer.zero_grad(set_to_none=True)

        logits = model(batch)
        loss, valid = compute_batch_loss(
            logits,
            batch.target_ids,
            tgt_pad_id=tgt_vocab.pad_id,
        )

        if valid == 0 or not torch.isfinite(loss).item():
            raise RuntimeError(f"Invalid loss at step {step}")

        loss.backward()
        grad_norm = torch.nn.utils.clip_grad_norm_(
            model.parameters(), 1.0
        )

        if not torch.isfinite(grad_norm).item():
            raise RuntimeError(
                f"Non-finite gradient norm at step {step}"
            )

        optimizer.step()

        if device.type == "mps":
            torch.mps.synchronize()

        steps_completed = step
        records.append({
            "step": step,
            "batch_loss": loss.item(),
        })

        should_evaluate = (
            step % EVALUATE_EVERY == 0
            or step == MAX_STEPS
        )

        if should_evaluate:
            train_metrics = evaluate(
                model, train_batches, tgt_vocab.pad_id
            )
            val_metrics = evaluate(
                model, val_batches, tgt_vocab.pad_id
            )

            record = {
                "step": step,
                "train": train_metrics,
                "validation": val_metrics,
            }
            records.append(record)

            print(
                f"Step {step:3d} | "
                f"batch loss={loss.item():.4f} | "
                f"train loss={train_metrics['loss']:.4f} | "
                f"val loss={val_metrics['loss']:.4f} | "
                f"val accuracy="
                f"{val_metrics['token_accuracy']:.2%}"
            )

            if val_metrics["loss"] < best_val_loss:
                best_val_loss = val_metrics["loss"]
                best_step = step

                torch.save(
                    {
                        "model_state_dict": model.state_dict(),
                        "model_config": config.to_dict(),
                        "step": step,
                        "validation": val_metrics,
                        "source_checkpoint": str(
                            INITIAL_CHECKPOINT
                        ),
                    },
                    OUT / "checkpoint_best.pt",
                )

                print("  Saved new best validation checkpoint.")

    final_train = evaluate(
        model, train_batches, tgt_vocab.pad_id
    )
    final_val = evaluate(
        model, val_batches, tgt_vocab.pad_id
    )

    summary = {
        "experiment": "validation_monitored_v1",
        "device": str(device),
        "initial_checkpoint": str(INITIAL_CHECKPOINT),
        "steps_completed": steps_completed,
        "elapsed_seconds": time.time() - start_time,
        "training_examples": len(train_items),
        "validation_examples": len(val_items),
        "training_programs": train_programs,
        "validation_programs": val_programs,
        "initial_train": initial_train,
        "initial_validation": initial_val,
        "final_train": final_train,
        "final_validation": final_val,
        "best_validation_loss": best_val_loss,
        "best_step": best_step,
        "records": records,
        "note": (
            "Small pilot only. Validation uses eight selected chunks; "
            "results do not establish full-test generalization."
        ),
    }

    (OUT / "summary.json").write_text(
        json.dumps(summary, indent=2),
        encoding="utf-8",
    )

    torch.save(
        {
            "model_state_dict": model.state_dict(),
            "model_config": config.to_dict(),
            "step": steps_completed,
            "validation": final_val,
            "source_checkpoint": str(INITIAL_CHECKPOINT),
        },
        OUT / "checkpoint_final.pt",
    )

    print("\nExperiment complete.")
    print("Steps:", steps_completed)
    print("Best step:", best_step)
    print("Best validation loss:", best_val_loss)
    print("Final train metrics:", final_train)
    print("Final validation metrics:", final_val)
    print("Results saved to:", OUT)


if __name__ == "__main__":
    main()

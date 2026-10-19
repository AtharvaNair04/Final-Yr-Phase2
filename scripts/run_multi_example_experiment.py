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
CHECKPOINT = ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"
TRAIN_JSONL = ROOT / "Module3_Training_Dataset/split/train.jsonl"
VAL_JSONL = ROOT / "Module3_Training_Dataset/split/validation.jsonl"
JAVA_DIR = ROOT / "selected source code"
OUT = ROOT / "checkpoints/multi_example_pilot_v2"

SEED = 42
MAX_TARGET_LENGTH = 512
BATCH_SIZE = 2
MAX_STEPS = 100
MAX_SECONDS = 180

random.seed(SEED)
torch.manual_seed(SEED)

if OUT.exists():
    raise RuntimeError(
        f"{OUT} already exists. Refusing to overwrite an experiment."
    )
if not CHECKPOINT.is_file():
    raise FileNotFoundError(CHECKPOINT)

device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
if device.type == "mps":
    torch.backends.mha.set_fastpath_enabled(False)

OUT.mkdir(parents=True)
src_vocab = Vocabulary.load(ROOT / "vocabularies/vocab_source.json")
tgt_vocab = Vocabulary.load(ROOT / "vocabularies/vocab_target.json")

checkpoint = torch.load(CHECKPOINT, map_location="cpu", weights_only=False)
config = ModelConfig.from_dict(checkpoint["model_config"])

if config.src_vocab_size != len(src_vocab) or config.tgt_vocab_size != len(tgt_vocab):
    raise ValueError("Checkpoint and vocabulary sizes do not match.")

model = ExecutionSemanticTransformer(config).to(device)
model.load_state_dict(checkpoint["model_state_dict"], strict=True)

train_ds = ExecutionTraceDataset(
    TRAIN_JSONL, JAVA_DIR, src_vocab, tgt_vocab,
    max_chunk_size=1024, max_source_length=config.max_src_length,
)
val_ds = ExecutionTraceDataset(
    VAL_JSONL, JAVA_DIR, src_vocab, tgt_vocab,
    max_chunk_size=1024, max_source_length=config.max_src_length,
)

def select_short_examples(dataset, max_items, max_target_length):
    """Choose short, complete chunks while spreading selection across programs."""
    groups = {}
    for index in range(len(dataset)):
        item = dataset[index]
        target_len = len(item.target_ids)
        if (
            1 < target_len <= max_target_length
            and 0 < len(item.source_ids) <= config.max_src_length
        ):
            groups.setdefault(item.program_id, []).append((target_len, index))

    selected = []
    # Round-robin across program IDs so one program cannot dominate selection.
    for pid in sorted(groups):
        groups[pid].sort()
    depth = 0
    while len(selected) < max_items:
        added = False
        for pid in sorted(groups):
            if depth < len(groups[pid]):
                selected.append(groups[pid][depth][1])
                added = True
                if len(selected) >= max_items:
                    break
        if not added:
            break
        depth += 1

    return [dataset[i] for i in selected]

train_items = select_short_examples(train_ds, 24, MAX_TARGET_LENGTH)
val_items = select_short_examples(val_ds, 8, MAX_TARGET_LENGTH)

if len(train_items) < 4:
    raise RuntimeError(
        f"Only {len(train_items)} suitable training chunks found. "
        "No training was performed."
    )
if not val_items:
    raise RuntimeError(
        "No suitable validation chunks found. No training was performed."
    )

train_programs = sorted({item.program_id for item in train_items})
val_programs = sorted({item.program_id for item in val_items})
if set(train_programs) & set(val_programs):
    raise RuntimeError("Training/validation program overlap detected.")

def make_batch(items):
    return execution_trace_collate_fn(
        items, src_pad_id=src_vocab.pad_id, tgt_pad_id=tgt_vocab.pad_id
    ).to(device)

train_batches = [
    make_batch(train_items[i:i + BATCH_SIZE])
    for i in range(0, len(train_items), BATCH_SIZE)
]
val_batches = [
    make_batch(val_items[i:i + BATCH_SIZE])
    for i in range(0, len(val_items), BATCH_SIZE)
]

def evaluate(batches):
    model.eval()
    loss_sum = 0.0
    token_count = 0
    correct = 0
    with torch.inference_mode():
        for batch in batches:
            logits = model(batch)
            loss, valid = compute_batch_loss(
                logits, batch.target_ids, tgt_pad_id=tgt_vocab.pad_id
            )
            loss_sum += loss.item() * valid
            token_count += valid
            predictions = logits.argmax(dim=-1)
            correct += (
                predictions.eq(batch.target_ids)
                & batch.target_ids.ne(tgt_vocab.pad_id)
            ).sum().item()
    return {
        "loss": loss_sum / max(token_count, 1),
        "token_accuracy": correct / max(token_count, 1),
        "valid_tokens": token_count,
    }

print("Device:", device)
print("Starting checkpoint epoch:", checkpoint.get("epoch"))
print("Training examples:", len(train_items))
print("Training programs:", train_programs)
print("Validation examples:", len(val_items))
print("Validation programs:", val_programs)

print("\nMeasuring starting metrics...")
initial_train = evaluate(train_batches)
initial_val = evaluate(val_batches)
print("Initial training:", initial_train)
print("Initial validation:", initial_val)

optimizer = torch.optim.AdamW(
    model.parameters(), lr=0.0001, weight_decay=0.01
)
records = []
start = time.time()
model.train()

for step in range(1, MAX_STEPS + 1):
    if time.time() - start >= MAX_SECONDS:
        print("Time limit reached; stopping safely.")
        break

    batch = train_batches[(step - 1) % len(train_batches)]
    model.train()
    optimizer.zero_grad(set_to_none=True)

    logits = model(batch)
    loss, valid = compute_batch_loss(
        logits, batch.target_ids, tgt_pad_id=tgt_vocab.pad_id
    )
    if valid == 0 or not torch.isfinite(loss).item():
        raise RuntimeError(f"Invalid loss at step {step}")

    loss.backward()
    torch.nn.utils.clip_grad_norm_(model.parameters(), 1.0)

    if not all(
        p.grad is None or torch.isfinite(p.grad).all().item()
        for p in model.parameters()
    ):
        raise RuntimeError(f"Non-finite gradient at step {step}")

    optimizer.step()
    if device.type == "mps":
        torch.mps.synchronize()

    record = {"step": step, "loss": loss.item()}
    records.append(record)

    if step == 1 or step % 10 == 0:
        print(f"Step {step:3d} | batch loss {loss.item():.4f}")

print("\nMeasuring final metrics...")
final_train = evaluate(train_batches)
final_val = evaluate(val_batches)

summary = {
    "device": str(device),
    "steps_completed": len(records),
    "elapsed_seconds": time.time() - start,
    "starting_checkpoint": str(CHECKPOINT.relative_to(ROOT)),
    "train_programs": train_programs,
    "validation_programs": val_programs,
    "training_examples": len(train_items),
    "validation_examples": len(val_items),
    "initial_train": initial_train,
    "final_train": final_train,
    "initial_validation": initial_val,
    "final_validation": final_val,
    "step_records": records,
    "interpretation_note": (
        "Small diagnostic experiment only; not evidence of generalization "
        "to the full held-out test set."
    ),
}

torch.save(
    {
        "model_state_dict": model.state_dict(),
        "model_config": config.to_dict(),
        "optimizer_state_dict": optimizer.state_dict(),
        "training_steps": len(records),
        "initial_checkpoint": str(CHECKPOINT),
        "summary": summary,
    },
    OUT / "checkpoint.pt",
)
(OUT / "summary.json").write_text(json.dumps(summary, indent=2))

print("\nFinal training:", final_train)
print("Final validation:", final_val)
print("Saved experiment:", OUT)
print("Existing pilot checkpoint was not modified.")

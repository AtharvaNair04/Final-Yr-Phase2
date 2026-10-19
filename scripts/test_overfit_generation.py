import json
import sys
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from execution_dataset import ExecutionTraceDataset
from vocabulary import Vocabulary
from scripts.compare_checkpoint_inference import evaluate

CHECKPOINT = ROOT / "checkpoints/multi_example_overfit_v1/checkpoint_best.pt"
TRAIN_JSONL = ROOT / "Module3_Training_Dataset/split/train.jsonl"
JAVA_DIR = ROOT / "selected source code"
SOURCE_VOCAB = ROOT / "vocabularies/vocab_source.json"
TARGET_VOCAB = ROOT / "vocabularies/vocab_target.json"
OUTPUT = ROOT / "results/inference/overfit_training_generation.json"

EXPECTED_PROGRAMS = [
    "p00014", "p00017", "p00029", "p00044",
    "p00063", "p00070", "p00084", "p00085",
]
MAX_NEW_TOKENS = 256


def main():
    if not CHECKPOINT.is_file():
        raise FileNotFoundError(CHECKPOINT)

    device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)

    source_vocab = Vocabulary.load(SOURCE_VOCAB)
    target_vocab = Vocabulary.load(TARGET_VOCAB)

    dataset = ExecutionTraceDataset(
        jsonl_path=TRAIN_JSONL,
        java_dir=JAVA_DIR,
        source_vocab=source_vocab,
        target_vocab=target_vocab,
        max_chunk_size=1024,
        max_source_length=1024,
    )

    # Reproduce the training experiment's selection: shortest eligible
    # chunk per program, with programs sorted alphabetically.
    groups = {}
    for index in range(len(dataset)):
        item = dataset[index]
        target_len = len(item.target_ids)
        if 1 < target_len <= 128 and 0 < len(item.source_ids) <= 1024:
            groups.setdefault(item.program_id, []).append((target_len, index))

    selected = []
    for pid in sorted(groups):
        groups[pid].sort()
        selected.append((pid, groups[pid][0][1]))

    selected = selected[:len(EXPECTED_PROGRAMS)]
    actual_programs = [pid for pid, _ in selected]

    if actual_programs != EXPECTED_PROGRAMS:
        raise RuntimeError(
            "Selected programs differ from the overfit experiment.\n"
            f"Expected: {EXPECTED_PROGRAMS}\n"
            f"Actual:   {actual_programs}"
        )

    print("Device:", device)
    print("Checkpoint:", CHECKPOINT.relative_to(ROOT))
    print("Evaluating training examples:", actual_programs)

    results = []
    for pid, index in selected:
        item = dataset[index]
        result = evaluate(
            CHECKPOINT, item, source_vocab, target_vocab, device
        )
        result["program_id"] = pid
        result["dataset_index"] = index
        results.append(result)

        print(
            f"\n{pid} | reference={result['reference_tokens']} tokens"
            f" | generated={result['generated_tokens']}"
            f" | teacher-forced={result['teacher_forced_token_accuracy']:.2%}"
            f" | greedy={result['greedy_token_accuracy']:.2%}"
            f" | unique={result['unique_generated_tokens']}"
            f" | EOS={result['eos_reached']}"
            f" | valid={result['structure']['valid']}"
        )
        print("Preview:", result["generated_preview"][:30])

    summary = {
        "checkpoint": str(CHECKPOINT.relative_to(ROOT)),
        "evaluated_programs": actual_programs,
        "example_count": len(results),
        "mean_teacher_forced_accuracy": sum(
            r["teacher_forced_token_accuracy"] for r in results
        ) / max(len(results), 1),
        "mean_greedy_accuracy": sum(
            r["greedy_token_accuracy"] for r in results
        ) / max(len(results), 1),
        "eos_count": sum(r["eos_reached"] for r in results),
        "structurally_valid_count": sum(
            r["structure"]["valid"] for r in results
        ),
        "results": results,
        "note": (
            "Training-example diagnostic only. Greedy accuracy is measured "
            "against reference traces; results do not establish generalization."
        ),
    }

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT.write_text(
        json.dumps(summary, indent=2, ensure_ascii=False),
        encoding="utf-8",
    )

    print("\nSummary")
    print("Mean teacher-forced accuracy:", f"{summary['mean_teacher_forced_accuracy']:.2%}")
    print("Mean greedy accuracy:", f"{summary['mean_greedy_accuracy']:.2%}")
    print("EOS reached:", summary["eos_count"], "/", len(results))
    print("Structurally valid:", summary["structurally_valid_count"], "/", len(results))
    print("Saved to:", OUTPUT)


if __name__ == "__main__":
    main()

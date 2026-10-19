import json
import sys
from pathlib import Path

import torch
import torch.nn.functional as F

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from model import ModelConfig, ExecutionSemanticTransformer
from vocabulary import Vocabulary
from execution_dataset import ExecutionTraceDataset

CHECKPOINT = ROOT / "checkpoints/multi_example_overfit_v2/checkpoint_final.pt"
TEST_JSONL = ROOT / "Module3_Training_Dataset/split/test.jsonl"
JAVA_DIR = ROOT / "selected source code"
SRC_VOCAB_PATH = ROOT / "vocabularies/vocab_source.json"
TGT_VOCAB_PATH = ROOT / "vocabularies/vocab_target.json"
OUTPUT_PATH = ROOT / "results/inference/source_conditioning_v2.json"

MAX_EXAMPLES = 8
MAX_REFERENCE_LENGTH = 160


def main():
    for path in (
        CHECKPOINT, TEST_JSONL, JAVA_DIR,
        SRC_VOCAB_PATH, TGT_VOCAB_PATH,
    ):
        if not path.exists():
            raise FileNotFoundError(f"Required path not found: {path}")

    if OUTPUT_PATH.exists():
        raise FileExistsError(
            f"Refusing to overwrite existing results: {OUTPUT_PATH}"
        )

    device = torch.device(
        "mps" if torch.backends.mps.is_available() else "cpu"
    )
    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)

    print("Device:", device)

    src_vocab = Vocabulary.load(SRC_VOCAB_PATH)
    tgt_vocab = Vocabulary.load(TGT_VOCAB_PATH)

    checkpoint = torch.load(
        CHECKPOINT, map_location="cpu", weights_only=False
    )
    config = ModelConfig.from_dict(checkpoint["model_config"])

    if config.src_vocab_size != len(src_vocab):
        raise ValueError("Source vocabulary size mismatch.")
    if config.tgt_vocab_size != len(tgt_vocab):
        raise ValueError("Target vocabulary size mismatch.")

    model = ExecutionSemanticTransformer(config)
    model.load_state_dict(checkpoint["model_state_dict"], strict=True)
    model.to(device)
    model.eval()

    dataset = ExecutionTraceDataset(
        jsonl_path=TEST_JSONL,
        java_dir=JAVA_DIR,
        source_vocab=src_vocab,
        target_vocab=tgt_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    examples = []
    seen = set()

    for i in range(len(dataset)):
        item = dataset[i]
        if item.program_id in seen:
            continue
        if not item.source_ids:
            continue
        if not (1 <= len(item.chunk_ids) <= MAX_REFERENCE_LENGTH):
            continue

        examples.append(item)
        seen.add(item.program_id)

        if len(examples) == MAX_EXAMPLES:
            break

    if len(examples) < 2:
        raise RuntimeError("Not enough eligible test examples.")

    print("Selected programs:", [x.program_id for x in examples])
    print("Each target will be evaluated with all other selected sources.")

    # Encode each source only once.
    encoded_sources = []
    with torch.inference_mode():
        for item in examples:
            src = torch.tensor(
                [item.source_ids], dtype=torch.long, device=device
            )
            mask = src.eq(config.src_pad_id)
            memory = model.encode(src, src_padding_mask=mask)
            encoded_sources.append((memory, mask))

    rows = []
    all_pair_wins = 0
    all_pair_count = 0
    all_prediction_changes = []

    with torch.inference_mode():
        for i, item in enumerate(examples):
            decoder_ids = torch.tensor(
                [item.decoder_input_ids],
                dtype=torch.long,
                device=device,
            )
            targets = torch.tensor(
                item.target_ids, dtype=torch.long, device=device
            )
            target_mask = decoder_ids.eq(config.tgt_pad_id)

            memory_correct, mask_correct = encoded_sources[i]

            def evaluate(memory, src_mask):
                hidden = model.decode(
                    decoder_input_ids=decoder_ids,
                    memory=memory,
                    target_padding_mask=target_mask,
                    src_padding_mask=src_mask,
                )
                logits = model.output_projection(hidden)[0]
                loss = F.cross_entropy(
                    logits, targets, reduction="mean"
                ).item()
                predictions = logits.argmax(dim=-1)
                accuracy = (
                    predictions.eq(targets).float().mean().item()
                )
                return loss, accuracy, predictions

            correct_loss, correct_acc, correct_pred = evaluate(
                memory_correct, mask_correct
            )

            wrong_results = []

            for j, other in enumerate(examples):
                if i == j:
                    continue

                memory_wrong, mask_wrong = encoded_sources[j]
                wrong_loss, wrong_acc, wrong_pred = evaluate(
                    memory_wrong, mask_wrong
                )

                changed = (
                    wrong_pred.ne(correct_pred).float().mean().item()
                )
                won = correct_loss < wrong_loss

                all_pair_wins += int(won)
                all_pair_count += 1
                all_prediction_changes.append(changed)

                wrong_results.append({
                    "wrong_source_program": other.program_id,
                    "loss": wrong_loss,
                    "accuracy": wrong_acc,
                    "prediction_change_rate": changed,
                    "correct_source_has_lower_loss": won,
                })

            mean_wrong_loss = sum(
                r["loss"] for r in wrong_results
            ) / len(wrong_results)

            row = {
                "target_program": item.program_id,
                "reference_length_including_eos": len(item.target_ids),
                "correct_source_loss": correct_loss,
                "correct_source_accuracy": correct_acc,
                "mean_wrong_source_loss": mean_wrong_loss,
                "correct_source_beats_all_wrong_sources": all(
                    correct_loss < r["loss"] for r in wrong_results
                ),
                "correct_source_wins": sum(
                    r["correct_source_has_lower_loss"]
                    for r in wrong_results
                ),
                "number_of_wrong_sources": len(wrong_results),
                "wrong_sources": wrong_results,
            }
            rows.append(row)

            print(
                f"{item.program_id}: correct loss={correct_loss:.4f}, "
                f"mean wrong-source loss={mean_wrong_loss:.4f}, "
                f"wins={row['correct_source_wins']}/"
                f"{len(wrong_results)}"
            )

    mean_correct_loss = sum(
        r["correct_source_loss"] for r in rows
    ) / len(rows)

    mean_wrong_loss = sum(
        r["mean_wrong_source_loss"] for r in rows
    ) / len(rows)

    summary = {
        "checkpoint": str(CHECKPOINT.relative_to(ROOT)),
        "device": str(device),
        "experiment": "multi-source teacher-forced conditioning diagnostic",
        "num_programs": len(rows),
        "wrong_source_comparisons": all_pair_count,
        "mean_correct_source_loss": mean_correct_loss,
        "mean_wrong_source_loss": mean_wrong_loss,
        "mean_loss_increase_with_wrong_source":
            mean_wrong_loss - mean_correct_loss,
        "correct_source_wins_across_all_pairs": all_pair_wins,
        "correct_source_win_rate_across_pairs":
            all_pair_wins / all_pair_count if all_pair_count else None,
        "mean_prediction_change_rate":
            sum(all_prediction_changes) / len(all_prediction_changes),
        "per_program": rows,
        "caveat": (
            "Small diagnostic sample. Alternative programs may share "
            "trace patterns. Results do not establish generalization."
        ),
    }

    OUTPUT_PATH.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT_PATH.write_text(
        json.dumps(summary, indent=2), encoding="utf-8"
    )

    print("\n===== MULTI-SOURCE SUMMARY =====")
    print("Programs:", len(rows))
    print("Wrong-source comparisons:", all_pair_count)
    print(f"Correct-source mean loss: {mean_correct_loss:.4f}")
    print(f"Wrong-source mean loss:   {mean_wrong_loss:.4f}")
    print(
        "Mean loss increase with wrong source: "
        f"{mean_wrong_loss - mean_correct_loss:+.4f}"
    )
    print(
        "Correct source wins: "
        f"{all_pair_wins}/{all_pair_count} comparisons "
        f"({all_pair_wins / all_pair_count:.1%})"
    )
    print(
        "Mean prediction change rate: "
        f"{sum(all_prediction_changes) / len(all_prediction_changes):.2%}"
    )
    print("Saved:", OUTPUT_PATH)


if __name__ == "__main__":
    main()

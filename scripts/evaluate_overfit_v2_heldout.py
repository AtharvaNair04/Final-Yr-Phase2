import json
import sys
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from model import ModelConfig, ExecutionSemanticTransformer
from vocabulary import Vocabulary
from execution_dataset import ExecutionTraceDataset
from scripts.run_inference_demo import validate_trace

CHECKPOINT = ROOT / "checkpoints/multi_example_overfit_v2/checkpoint_final.pt"
TEST_JSONL = ROOT / "Module3_Training_Dataset/split/test.jsonl"
TRAIN_JSONL = ROOT / "Module3_Training_Dataset/split/train.jsonl"
JAVA_DIR = ROOT / "selected source code"
SRC_VOCAB_PATH = ROOT / "vocabularies/vocab_source.json"
TGT_VOCAB_PATH = ROOT / "vocabularies/vocab_target.json"
OUTPUT_DIR = ROOT / "results/inference/overfit_v2_heldout"

TRAINED_PROGRAMS = {
    "p00014", "p00017", "p00029", "p00044",
    "p00063", "p00070", "p00084", "p00085",
}
MAX_NEW_TOKENS = 160
NUM_PROGRAMS = 8


def load_program_ids(path):
    ids = set()
    with path.open(encoding="utf-8") as f:
        for line in f:
            if line.strip():
                ids.add(json.loads(line)["program_id"])
    return ids


def main():
    if not CHECKPOINT.is_file():
        raise FileNotFoundError(CHECKPOINT)
    if OUTPUT_DIR.exists():
        raise RuntimeError(f"Output already exists; refusing to overwrite: {OUTPUT_DIR}")

    device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)
    print("Device:", device)

    src_vocab = Vocabulary.load(SRC_VOCAB_PATH)
    tgt_vocab = Vocabulary.load(TGT_VOCAB_PATH)
    checkpoint = torch.load(CHECKPOINT, map_location="cpu", weights_only=False)
    config = ModelConfig.from_dict(checkpoint["model_config"])

    if config.src_vocab_size != len(src_vocab) or config.tgt_vocab_size != len(tgt_vocab):
        raise ValueError("Checkpoint and vocabulary sizes do not match.")

    model = ExecutionSemanticTransformer(config)
    model.load_state_dict(checkpoint["model_state_dict"], strict=True)
    model.to(device)
    model.eval()

    train_ids = load_program_ids(TRAIN_JSONL)
    test_ids = load_program_ids(TEST_JSONL)
    overlap = train_ids & test_ids
    if overlap:
        raise RuntimeError(f"Training/test program overlap detected: {sorted(overlap)[:10]}")

    dataset = ExecutionTraceDataset(
        jsonl_path=TEST_JSONL,
        java_dir=JAVA_DIR,
        source_vocab=src_vocab,
        target_vocab=tgt_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    # Select one short chunk per distinct held-out program.
    selected = []
    seen = set()
    for idx in range(len(dataset)):
        item = dataset[idx]
        if item.program_id in seen or item.program_id in TRAINED_PROGRAMS:
            continue
        if not item.source_ids or not (1 <= len(item.chunk_ids) <= MAX_NEW_TOKENS):
            continue
        selected.append(item)
        seen.add(item.program_id)
        if len(selected) == NUM_PROGRAMS:
            break

    if len(selected) < NUM_PROGRAMS:
        raise RuntimeError(f"Found only {len(selected)} eligible distinct test programs.")

    print("Selected held-out programs:", [x.program_id for x in selected])
    print("Checkpoint:", CHECKPOINT.relative_to(ROOT))
    print("Training-example overlap:", set(x.program_id for x in selected) & TRAINED_PROGRAMS)

    results = []
    for item in selected:
        src = torch.tensor([item.source_ids], dtype=torch.long, device=device)
        src_mask = src.eq(config.src_pad_id)
        ref_ids = list(item.chunk_ids)
        ref_tokens = list(item.chunk_tokens)
        bos_id, eos_id, pad_id = tgt_vocab.bos_id, tgt_vocab.eos_id, tgt_vocab.pad_id

        with torch.inference_mode():
            memory = model.encode(src, src_padding_mask=src_mask)

            # Teacher-forced: correct preceding reference tokens are provided.
            dec_ids = [bos_id] + ref_ids[:-1]
            dec = torch.tensor([dec_ids], dtype=torch.long, device=device)
            hidden = model.decode(
                decoder_input_ids=dec,
                memory=memory,
                target_padding_mask=dec.eq(pad_id),
                src_padding_mask=src_mask,
            )
            tf_pred = model.output_projection(hidden).argmax(dim=-1)[0].tolist()
            tf_accuracy = sum(a == b for a, b in zip(tf_pred, ref_ids)) / len(ref_ids)

            # Ordinary greedy decoding; no repetition penalty or other decoding tricks.
            generated = [bos_id]
            eos_reached = False
            for _ in range(min(MAX_NEW_TOKENS, config.max_tgt_length - 1)):
                dec = torch.tensor([generated], dtype=torch.long, device=device)
                hidden = model.decode(
                    decoder_input_ids=dec,
                    memory=memory,
                    target_padding_mask=dec.eq(pad_id),
                    src_padding_mask=src_mask,
                )
                logits = model.output_projection(hidden[:, -1, :])
                next_id = int(logits.argmax(dim=-1).item())
                if next_id == eos_id:
                    eos_reached = True
                    break
                generated.append(next_id)

        pred_tokens = tgt_vocab.decode(generated[1:], skip_special=False)
        compared = min(len(pred_tokens), len(ref_tokens))
        greedy_accuracy = sum(
            pred_tokens[i] == ref_tokens[i] for i in range(compared)
        ) / max(len(ref_tokens), 1)
        structure = validate_trace(pred_tokens)

        result = {
            "program_id": item.program_id,
            "chunk_index": item.chunk_index,
            "reference_length": len(ref_tokens),
            "generated_length": len(pred_tokens),
            "teacher_forced_accuracy": tf_accuracy,
            "greedy_token_accuracy": greedy_accuracy,
            "exact_match": pred_tokens == ref_tokens,
            "eos_reached": eos_reached,
            "structurally_valid": structure["valid"],
            "structure_errors": structure["errors"],
            "reference_preview": ref_tokens[:30],
            "generated_preview": pred_tokens[:30],
        }
        results.append(result)
        print(
            f"{item.program_id}: TF={tf_accuracy:.2%}, "
            f"greedy={greedy_accuracy:.2%}, exact={result['exact_match']}, "
            f"EOS={eos_reached}, valid={structure['valid']}"
        )

    summary = {
        "checkpoint": str(CHECKPOINT.relative_to(ROOT)),
        "device": str(device),
        "evaluation_type": "held-out test programs; one chunk per program",
        "decoding": "ordinary greedy; no repetition penalty",
        "num_programs": len(results),
        "mean_teacher_forced_accuracy": sum(r["teacher_forced_accuracy"] for r in results) / len(results),
        "mean_greedy_token_accuracy": sum(r["greedy_token_accuracy"] for r in results) / len(results),
        "exact_matches": sum(r["exact_match"] for r in results),
        "eos_terminations": sum(r["eos_reached"] for r in results),
        "structurally_valid": sum(r["structurally_valid"] for r in results),
        "examples": results,
    }

    OUTPUT_DIR.mkdir(parents=True)
    (OUTPUT_DIR / "metrics.json").write_text(
        json.dumps(summary, indent=2, ensure_ascii=False), encoding="utf-8"
    )
    print("\nSummary")
    for key in (
        "mean_teacher_forced_accuracy", "mean_greedy_token_accuracy",
        "exact_matches", "eos_terminations", "structurally_valid"
    ):
        print(f"{key}: {summary[key]}")
    print("Saved:", OUTPUT_DIR / "metrics.json")


if __name__ == "__main__":
    main()

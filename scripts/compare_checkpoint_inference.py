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

OLD_CHECKPOINT = ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"
MULTI_CHECKPOINT = ROOT / "checkpoints/multi_example_pilot_v2/checkpoint.pt"
NEW_CHECKPOINT = ROOT / "checkpoints/validation_monitored_v1/checkpoint_best.pt"
OVERFIT_CHECKPOINT = ROOT / "checkpoints/multi_example_overfit_v1/checkpoint_best.pt"
SOURCE_VOCAB = ROOT / "vocabularies/vocab_source.json"
TARGET_VOCAB = ROOT / "vocabularies/vocab_target.json"
TEST_JSONL = ROOT / "Module3_Training_Dataset/split/test.jsonl"
JAVA_DIR = ROOT / "selected source code"
OUTPUT = ROOT / "results/inference/checkpoint_comparison_v3.json"

MAX_NEW_TOKENS = 256
TEST_INDEX = 33


def get_id(vocab, name, fallback):
    value = getattr(vocab, name, None)
    return fallback if value is None else int(value)


def evaluate(checkpoint_path, item, source_vocab, target_vocab, device):
    checkpoint = torch.load(
        checkpoint_path, map_location="cpu", weights_only=False
    )
    config = ModelConfig.from_dict(checkpoint["model_config"])

    if config.src_vocab_size != len(source_vocab):
        raise ValueError(f"Source vocabulary mismatch: {checkpoint_path}")
    if config.tgt_vocab_size != len(target_vocab):
        raise ValueError(f"Target vocabulary mismatch: {checkpoint_path}")

    model = ExecutionSemanticTransformer(config)
    model.load_state_dict(checkpoint["model_state_dict"], strict=True)
    model.to(device)
    model.eval()

    src_ids = torch.tensor(
        [item.source_ids], dtype=torch.long, device=device
    )
    src_mask = src_ids.eq(config.src_pad_id)
    bos_id = get_id(target_vocab, "bos_id", 2)
    eos_id = get_id(target_vocab, "eos_id", 3)
    pad_id = config.tgt_pad_id

    generated = [bos_id]
    eos_reached = False

    with torch.inference_mode():
        memory = model.encode(src_ids, src_padding_mask=src_mask)

        for _ in range(min(MAX_NEW_TOKENS, config.max_tgt_length - 1)):
            decoder_ids = torch.tensor(
                [generated], dtype=torch.long, device=device
            )
            tgt_mask = decoder_ids.eq(pad_id)
            hidden = model.decode(
                decoder_input_ids=decoder_ids,
                memory=memory,
                target_padding_mask=tgt_mask,
                src_padding_mask=src_mask,
            )
            logits = model.output_projection(hidden[:, -1, :])
            next_id = int(logits.argmax(dim=-1).item())

            if next_id == eos_id:
                eos_reached = True
                break
            generated.append(next_id)

        # Teacher-forced accuracy: predict each reference token while
        # supplying the correct preceding reference tokens.
        ref_ids = list(item.chunk_ids)
        decoder_input = [bos_id] + ref_ids
        decoder_input = decoder_input[:len(ref_ids)]
        decoder_tensor = torch.tensor(
            [decoder_input], dtype=torch.long, device=device
        )
        decoder_pad_mask = decoder_tensor.eq(pad_id)
        hidden = model.decode(
            decoder_input_ids=decoder_tensor,
            memory=memory,
            target_padding_mask=decoder_pad_mask,
            src_padding_mask=src_mask,
        )
        teacher_logits = model.output_projection(hidden)
        teacher_predictions = teacher_logits.argmax(dim=-1)[0].tolist()

    predicted_tokens = target_vocab.decode(
        generated[1:], skip_special=False
    )
    reference_tokens = list(item.chunk_tokens)
    teacher_count = min(len(reference_tokens), len(teacher_predictions))
    teacher_accuracy = (
        sum(
            teacher_predictions[i] == ref_ids[i]
            for i in range(teacher_count)
        ) / max(len(ref_ids), 1)
    )

    result = {
        "checkpoint": str(checkpoint_path.relative_to(ROOT)),
        "training_steps": checkpoint.get("training_steps"),
        "reference_tokens": len(reference_tokens),
        "generated_tokens": len(predicted_tokens),
        "eos_reached": eos_reached,
        "exact_match": predicted_tokens == reference_tokens,
        "greedy_token_accuracy": (
            sum(
                predicted_tokens[i] == reference_tokens[i]
                for i in range(min(len(predicted_tokens), len(reference_tokens)))
            ) / max(len(reference_tokens), 1)
        ),
        "teacher_forced_token_accuracy": teacher_accuracy,
        "unique_generated_tokens": len(set(predicted_tokens)),
        "top_generated_tokens": sorted(
            {t: predicted_tokens.count(t) for t in set(predicted_tokens)}.items(),
            key=lambda x: x[1],
            reverse=True,
        )[:8],
        "structure": validate_trace(predicted_tokens),
        "generated_preview": predicted_tokens[:60],
    }

    del model
    if device.type == "mps":
        torch.mps.synchronize()
    return result


def main():
    CHECKPOINTS = [
        OLD_CHECKPOINT,
        MULTI_CHECKPOINT,
        NEW_CHECKPOINT,
        OVERFIT_CHECKPOINT,
    ]
    for path in CHECKPOINTS:
        if not path.is_file():
            raise FileNotFoundError(f"Checkpoint is missing: {path}")

        device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
        if device.type == "mps":
            torch.backends.mha.set_fastpath_enabled(False)
        print("Device:", device)

    source_vocab = Vocabulary.load(SOURCE_VOCAB)
    target_vocab = Vocabulary.load(TARGET_VOCAB)
    dataset = ExecutionTraceDataset(
        jsonl_path=TEST_JSONL,
        java_dir=JAVA_DIR,
        source_vocab=source_vocab,
        target_vocab=target_vocab,
        max_chunk_size=1024,
        max_source_length=1024,
    )

    item = dataset[TEST_INDEX]
    print(
        f"Same test example: index={TEST_INDEX}, program={item.program_id}, "
        f"chunk={item.chunk_index + 1}/{item.total_chunks}, "
        f"reference tokens={len(item.chunk_tokens)}"
    )

    results = []
    for path in CHECKPOINTS:
        print(f"\nEvaluating {path.relative_to(ROOT)} ...")
        result = evaluate(path, item, source_vocab, target_vocab, device)
        results.append(result)
        print("Greedy token accuracy:", f"{result['greedy_token_accuracy']:.2%}")
        print("Teacher-forced token accuracy:",
              f"{result['teacher_forced_token_accuracy']:.2%}")
        print("Generated tokens:", result["generated_tokens"])
        print("EOS reached:", result["eos_reached"])
        print("Unique generated tokens:", result["unique_generated_tokens"])
        print("Structural validation:", result["structure"]["valid"])
        print("Top generated tokens:", result["top_generated_tokens"])
        print("Generated preview:", result["generated_preview"])

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT.write_text(
        json.dumps(
            {
                "test_index": TEST_INDEX,
                "program_id": item.program_id,
                "chunk_index": item.chunk_index,
                "reference_tokens": item.chunk_tokens,
                "results": results,
            },
            indent=2,
            ensure_ascii=False,
        ),
        encoding="utf-8",
    )
    print("\nComparison saved to:", OUTPUT)


if __name__ == "__main__":
    main()

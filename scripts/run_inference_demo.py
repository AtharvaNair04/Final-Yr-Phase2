import json
import sys
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from model import ModelConfig, ExecutionSemanticTransformer
from vocabulary import Vocabulary
from execution_dataset import ExecutionTraceDataset


CHECKPOINT = ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"
SOURCE_VOCAB_PATH = ROOT / "vocabularies/vocab_source.json"
TARGET_VOCAB_PATH = ROOT / "vocabularies/vocab_target.json"
TEST_JSONL = ROOT / "Module3_Training_Dataset/split/test.jsonl"
JAVA_DIR = ROOT / "selected source code"
OUTPUT_PATH = ROOT / "results/inference/pilot_test_prediction.json"

MAX_NEW_TOKENS = 256


def get_token_id(vocab, name, fallback):
    value = getattr(vocab, name, None)
    return fallback if value is None else int(value)


def validate_trace(tokens):
    """Basic structural checks; not a complete SRER parser."""
    event_start = "<EVENT_START>"
    event_end = "<EVENT_END>"
    attr_start = "<ATTR_START>"
    attr_end = "<ATTR_END>"
    escape = "\\"

    events = 0
    errors = []
    inside_event = False
    inside_attr = False
    i = 0

    while i < len(tokens):
        token = tokens[i]

        # Escaped reserved tokens are literal data, not delimiters.
        if token == escape:
            if i + 1 >= len(tokens):
                errors.append("Trailing escape token")
                break
            i += 2
            continue

        if token == event_start:
            if inside_event:
                errors.append("Nested EVENT_START before EVENT_END")
            if inside_attr:
                errors.append("Event started while inside an attribute")
            inside_event = True
            events += 1

        elif token == event_end:
            if not inside_event:
                errors.append("EVENT_END outside an event")
            if inside_attr:
                errors.append("EVENT_END encountered before ATTR_END")
            inside_event = False

        elif token == attr_start:
            if not inside_event:
                errors.append("ATTR_START outside an event")
            if inside_attr:
                errors.append("Nested ATTR_START")
            inside_attr = True

        elif token == attr_end:
            if not inside_attr:
                errors.append("ATTR_END without ATTR_START")
            inside_attr = False

        i += 1

    if inside_event:
        errors.append("Unclosed event")
    if inside_attr:
        errors.append("Unclosed attribute")
    if events == 0:
        errors.append("No events found")

    return {"valid": len(errors) == 0, "event_count": events, "errors": errors}


def main():
    if not CHECKPOINT.is_file():
        raise FileNotFoundError(f"Checkpoint not found: {CHECKPOINT}")

    device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")
    print(f"Device: {device}")

    # Avoid the known MPS Transformer attention fastpath issue.
    if device.type == "mps":
        torch.backends.mha.set_fastpath_enabled(False)

    source_vocab = Vocabulary.load(SOURCE_VOCAB_PATH)
    target_vocab = Vocabulary.load(TARGET_VOCAB_PATH)

    # Load on CPU first; move the instantiated model to the chosen device.
    checkpoint = torch.load(
        CHECKPOINT, map_location="cpu", weights_only=False
    )
    config = ModelConfig.from_dict(checkpoint["model_config"])

    if config.src_vocab_size != len(source_vocab):
        raise ValueError(
            f"Source vocabulary mismatch: checkpoint={config.src_vocab_size}, "
            f"file={len(source_vocab)}"
        )
    if config.tgt_vocab_size != len(target_vocab):
        raise ValueError(
            f"Target vocabulary mismatch: checkpoint={config.tgt_vocab_size}, "
            f"file={len(target_vocab)}"
        )

    model = ExecutionSemanticTransformer(config)
    model.load_state_dict(checkpoint["model_state_dict"], strict=True)
    model.to(device)
    model.eval()

    print(f"Checkpoint epoch: {checkpoint.get('epoch', 'unknown')}")
    print(f"Checkpoint validation loss: {checkpoint.get('val_loss', 'unknown')}")
    print(f"Model source limit: {config.max_src_length}")
    print(f"Model target limit: {config.max_tgt_length}")

    dataset = ExecutionTraceDataset(
        jsonl_path=TEST_JSONL,
        java_dir=JAVA_DIR,
        source_vocab=source_vocab,
        target_vocab=target_vocab,
        max_chunk_size=1024,
        max_source_length=config.max_src_length,
    )

    if len(dataset) == 0:
        raise RuntimeError("Test dataset contains no chunks.")

    # Pick a short reference chunk to make the first inference run manageable.
    # The selection rule depends only on reference length, not model predictions.
    candidate = None
    for idx in range(min(len(dataset), 100)):
        item = dataset[idx]
        if 0 < len(item.chunk_ids) <= MAX_NEW_TOKENS:
            candidate = (idx, item)
            break

    if candidate is None:
        raise RuntimeError(
            "No chunk of at most MAX_NEW_TOKENS tokens found among the first "
            "100 test chunks. Increase the search range or adjust the cap."
        )

    dataset_index, item = candidate
    print(f"Test dataset index: {dataset_index}")
    print(f"Program ID: {item.program_id}")
    print(f"Chunk: {item.chunk_index + 1}/{item.total_chunks}")
    print(f"Source tokens after truncation: {len(item.source_ids)}")
    print(f"Reference chunk tokens: {len(item.chunk_ids)}")

    if not item.source_ids:
        raise RuntimeError("Selected source has no tokens.")

    src_ids = torch.tensor(
        [item.source_ids], dtype=torch.long, device=device
    )
    src_padding_mask = src_ids.eq(config.src_pad_id)

    bos_id = get_token_id(target_vocab, "bos_id", 2)
    eos_id = get_token_id(target_vocab, "eos_id", 3)
    pad_id = config.tgt_pad_id

    generated_ids = [bos_id]
    finished = False

    with torch.inference_mode():
        memory = model.encode(
            src_ids, src_padding_mask=src_padding_mask
        )

        for _ in range(
            min(MAX_NEW_TOKENS, config.max_tgt_length - 1)
        ):
            decoder_ids = torch.tensor(
                [generated_ids], dtype=torch.long, device=device
            )
            target_padding_mask = decoder_ids.eq(pad_id)

            hidden = model.decode(
                decoder_input_ids=decoder_ids,
                memory=memory,
                target_padding_mask=target_padding_mask,
                src_padding_mask=src_padding_mask,
            )
            logits = model.output_projection(hidden[:, -1, :])
            next_id = int(torch.argmax(logits, dim=-1).item())

            if next_id == eos_id:
                finished = True
                break

            generated_ids.append(next_id)

    predicted_ids = generated_ids[1:]  # Exclude BOS.
    predicted_tokens = target_vocab.decode(
        predicted_ids, skip_special=False
    )
    reference_tokens = list(item.chunk_tokens)

    # Basic token-level metrics, including any early termination or truncation.
    comparable = min(len(predicted_tokens), len(reference_tokens))
    correct_positions = sum(
        predicted_tokens[i] == reference_tokens[i]
        for i in range(comparable)
    )
    positional_accuracy = (
        correct_positions / max(len(reference_tokens), 1)
    )
    exact_match = predicted_tokens == reference_tokens

    # EOS is not included in predicted_tokens; record completion separately.
    structural = validate_trace(predicted_tokens)

    result = {
        "checkpoint": str(CHECKPOINT.relative_to(ROOT)),
        "checkpoint_epoch": checkpoint.get("epoch"),
        "checkpoint_validation_loss": checkpoint.get("val_loss"),
        "device": str(device),
        "program_id": item.program_id,
        "dataset_index": dataset_index,
        "chunk_index_zero_based": item.chunk_index,
        "total_chunks_for_program": item.total_chunks,
        "source_token_count_after_truncation": len(item.source_ids),
        "reference_token_count": len(reference_tokens),
        "generated_token_count": len(predicted_tokens),
        "generation_cap": MAX_NEW_TOKENS,
        "eos_reached": finished,
        "exact_chunk_match": exact_match,
        "positional_token_accuracy": positional_accuracy,
        "structural_validation": structural,
        "reference_tokens": reference_tokens,
        "generated_tokens": predicted_tokens,
    }

    OUTPUT_PATH.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT_PATH.write_text(
        json.dumps(result, indent=2, ensure_ascii=False),
        encoding="utf-8",
    )

    print("\n===== INFERENCE RESULTS =====")
    print(f"EOS reached: {finished}")
    print(f"Generated tokens: {len(predicted_tokens)}")
    print(f"Exact chunk match: {exact_match}")
    print(f"Positional token accuracy: {positional_accuracy:.4f}")
    print(f"Structural validation: {structural}")
    print(f"Saved results to: {OUTPUT_PATH}")

    print("\n===== GENERATED TRACE (first 80 tokens) =====")
    print(" ".join(predicted_tokens[:80]))

    print("\n===== REFERENCE TRACE (first 80 tokens) =====")
    print(" ".join(reference_tokens[:80]))


if __name__ == "__main__":
    main()

"""
Script to build, serialize, validate, and evaluate Source and Target Vocabularies (Module 4, Part 5).

Guarantees:
  1. Fits vocabularies strictly on the training split (train.jsonl and paired Java files).
  2. Serializes deterministic artifacts to vocabularies/vocab_source.json and vocabularies/vocab_target.json.
  3. Reloads both artifacts and validates exact bidirectional mappings.
  4. Runs post-fit OOV diagnostics on validation and test splits without altering vocabularies.
"""

import json
import os
from pathlib import Path
import sys
import time

from source_tokenizer import tokenize_java_code
from tokenize_and_analyze import tokenize_runtime
from vocabulary import (
    DEFAULT_TARGET_MAX_SIZE,
    Vocabulary,
    build_source_vocabulary,
    build_target_vocabulary,
    calculate_oov_diagnostics,
)

BASE_DIR = Path(__file__).parent
SPLIT_DIR = BASE_DIR / "Module3_Training_Dataset" / "split"
JAVA_DIR = BASE_DIR / "selected source code"
OUTPUT_DIR = BASE_DIR / "vocabularies"

TRAIN_PATH = SPLIT_DIR / "train.jsonl"
VAL_PATH = SPLIT_DIR / "validation.jsonl"
TEST_PATH = SPLIT_DIR / "test.jsonl"


def evaluate_split_source_oov(vocab: Vocabulary, split_path: Path) -> dict:
    """Extracts raw Java tokens for program_ids in split_path and computes OOV stats."""
    sequences = []
    with open(split_path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            rec = json.loads(line)
            pid = rec["program_id"]
            java_path = JAVA_DIR / f"{pid}.java"
            with open(java_path, "r", encoding="utf-8", errors="ignore") as jf:
                code = jf.read()
            tokens = tokenize_java_code(code, preserve_comments=False)
            sequences.append(tokens)

    return calculate_oov_diagnostics(vocab, sequences)


def evaluate_split_target_oov(vocab: Vocabulary, split_path: Path) -> dict:
    """Tokenizes runtime traces in split_path and computes OOV stats."""
    sequences = []
    with open(split_path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            rec = json.loads(line)
            rt = rec.get("runtime_trace", {})
            tokens = tokenize_runtime(rt)
            sequences.append(tokens)

    return calculate_oov_diagnostics(vocab, sequences)


def main():
    print("=" * 75)
    print("MODULE 4 - PART 5: VOCABULARY CONSTRUCTION & ARTIFACT GENERATION")
    print("=" * 75)

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    source_vocab_path = OUTPUT_DIR / "vocab_source.json"
    target_vocab_path = OUTPUT_DIR / "vocab_target.json"

    # --------------------------------------------------------
    # 1. Build Source Vocabulary
    # --------------------------------------------------------
    print(f"\n[1/4] Building Source Vocabulary from {TRAIN_PATH.name}...")
    t0 = time.time()
    source_vocab, source_stats = build_source_vocabulary(
        train_jsonl_path=TRAIN_PATH,
        java_dir=JAVA_DIR,
        max_size=None,
        min_freq=1,
    )
    t_source = time.time() - t0
    print(f"  Source fitting completed in {t_source:.2f}s")
    print(f"  Training records processed    : {source_stats['records_processed']}")
    print(f"  Total source token instances  : {source_stats['total_token_occurrences']:,}")
    print(f"  Unique source tokens observed : {source_stats['unique_tokens_observed']:,}")
    print(f"  Final Source Vocabulary size  : {source_stats['vocabulary_size']:,} (4 special + {source_stats['vocabulary_size'] - 4} tokens)")

    # --------------------------------------------------------
    # 2. Build Target Vocabulary
    # --------------------------------------------------------
    print(f"\n[2/4] Building Target Vocabulary from {TRAIN_PATH.name} (Cap: {DEFAULT_TARGET_MAX_SIZE:,})...")
    t0 = time.time()
    target_vocab, target_stats = build_target_vocabulary(
        train_jsonl_path=TRAIN_PATH,
        max_size=DEFAULT_TARGET_MAX_SIZE,
        min_freq=1,
    )
    t_target = time.time() - t0
    print(f"  Target fitting completed in {t_target:.2f}s")
    print(f"  Training records processed    : {target_stats['records_processed']}")
    print(f"  Total target token instances  : {target_stats['total_token_occurrences']:,}")
    print(f"  Unique target tokens observed : {target_stats['unique_tokens_observed']:,}")
    print(f"  Reserved tokens count         : {target_stats['reserved_tokens_count']} (4 special + 8 structural)")
    print(f"  Eligible tokens admitted      : {target_stats['eligible_tokens_added']:,}")
    print(f"  Tokens excluded by cap        : {target_stats['excluded_by_cap']:,}")
    print(f"  Final Target Vocabulary size  : {target_stats['vocabulary_size']:,}")

    # --------------------------------------------------------
    # 3. Serialize and Reload Validation
    # --------------------------------------------------------
    print("\n[3/4] Serializing and validating reload integrity...")
    source_vocab.save(source_vocab_path)
    print(f"  Saved source vocabulary to: {source_vocab_path} ({source_vocab_path.stat().st_size:,} bytes)")

    target_vocab.save(target_vocab_path)
    print(f"  Saved target vocabulary to: {target_vocab_path} ({target_vocab_path.stat().st_size:,} bytes)")

    # Reload check
    src_reloaded = Vocabulary.load(source_vocab_path)
    tgt_reloaded = Vocabulary.load(target_vocab_path)

    assert len(src_reloaded) == len(source_vocab), "Source vocabulary length mismatch on reload"
    assert len(tgt_reloaded) == len(target_vocab), "Target vocabulary length mismatch on reload"
    assert src_reloaded.token_to_id == source_vocab.token_to_id, "Source token_to_id mismatch on reload"
    assert tgt_reloaded.token_to_id == target_vocab.token_to_id, "Target token_to_id mismatch on reload"
    print("  ✓ Reload validation PASSED: Both vocabularies match in-memory mappings exactly.")

    # --------------------------------------------------------
    # 4. Post-Fit OOV Diagnostics (Validation & Test Splits)
    # --------------------------------------------------------
    print("\n[4/4] Computing post-fit OOV diagnostics (Read-Only Evaluation)...")

    # Source OOV
    val_src_oov = evaluate_split_source_oov(source_vocab, VAL_PATH)
    test_src_oov = evaluate_split_source_oov(source_vocab, TEST_PATH)

    print("\n--- SOURCE VOCABULARY OOV DIAGNOSTICS ---")
    print(f"Validation Split ({VAL_PATH.name}, 56 records):")
    print(f"  Total tokens: {val_src_oov['total_tokens']:,}")
    print(f"  Known tokens: {val_src_oov['known_tokens']:,}")
    print(f"  Unknown tokens: {val_src_oov['unknown_tokens']:,}")
    print(f"  OOV token rate: {val_src_oov['oov_rate']:.4%}")
    print(f"  Distinct unseen tokens: {val_src_oov['distinct_unseen_tokens']}")
    print(f"  Sequences with OOV: {val_src_oov['sequences_with_oov']} / {val_src_oov['total_sequences']} ({val_src_oov['sequence_oov_rate']:.2%})")

    print(f"\nTest Split ({TEST_PATH.name}, 57 records):")
    print(f"  Total tokens: {test_src_oov['total_tokens']:,}")
    print(f"  Known tokens: {test_src_oov['known_tokens']:,}")
    print(f"  Unknown tokens: {test_src_oov['unknown_tokens']:,}")
    print(f"  OOV token rate: {test_src_oov['oov_rate']:.4%}")
    print(f"  Distinct unseen tokens: {test_src_oov['distinct_unseen_tokens']}")
    print(f"  Sequences with OOV: {test_src_oov['sequences_with_oov']} / {test_src_oov['total_sequences']} ({test_src_oov['sequence_oov_rate']:.2%})")

    # Target OOV
    val_tgt_oov = evaluate_split_target_oov(target_vocab, VAL_PATH)
    test_tgt_oov = evaluate_split_target_oov(target_vocab, TEST_PATH)

    print("\n--- TARGET VOCABULARY OOV DIAGNOSTICS (Cap: 50,000) ---")
    print(f"Validation Split ({VAL_PATH.name}, 56 records):")
    print(f"  Total tokens: {val_tgt_oov['total_tokens']:,}")
    print(f"  Known tokens: {val_tgt_oov['known_tokens']:,}")
    print(f"  Unknown tokens: {val_tgt_oov['unknown_tokens']:,}")
    print(f"  OOV token rate: {val_tgt_oov['oov_rate']:.4%}")
    print(f"  Distinct unseen tokens: {val_tgt_oov['distinct_unseen_tokens']:,}")
    print(f"  Sequences with OOV: {val_tgt_oov['sequences_with_oov']} / {val_tgt_oov['total_sequences']} ({val_tgt_oov['sequence_oov_rate']:.2%})")

    print(f"\nTest Split ({TEST_PATH.name}, 57 records):")
    print(f"  Total tokens: {test_tgt_oov['total_tokens']:,}")
    print(f"  Known tokens: {test_tgt_oov['known_tokens']:,}")
    print(f"  Unknown tokens: {test_tgt_oov['unknown_tokens']:,}")
    print(f"  OOV token rate: {test_tgt_oov['oov_rate']:.4%}")
    print(f"  Distinct unseen tokens: {test_tgt_oov['distinct_unseen_tokens']:,}")
    print(f"  Sequences with OOV: {test_tgt_oov['sequences_with_oov']} / {test_tgt_oov['total_sequences']} ({test_tgt_oov['sequence_oov_rate']:.2%})")

    print("\n" + "=" * 75)
    print("VOCABULARY BUILD AND EVALUATION COMPLETE")
    print("=" * 75)


if __name__ == "__main__":
    main()

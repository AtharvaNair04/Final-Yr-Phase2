"""
Unit and regression tests for Vocabulary Construction and Special Token Design (Module 4, Part 5).
"""

from collections import Counter
import json
import os
from pathlib import Path
import tempfile
import unittest

from source_tokenizer import tokenize_java_code
from tokenize_and_analyze import tokenize_runtime
import vocabulary
from vocabulary import (
    ATTR_END_ID,
    ATTR_END_TOKEN,
    ATTR_START_ID,
    ATTR_START_TOKEN,
    BOS_ID,
    BOS_TOKEN,
    DEFAULT_TARGET_MAX_SIZE,
    EMPTY_VAL_ID,
    EMPTY_VAL_TOKEN,
    EOS_ID,
    EOS_TOKEN,
    ESCAPE_ID,
    ESCAPE_TOKEN,
    EVENT_END_ID,
    EVENT_END_TOKEN,
    EVENT_START_ID,
    EVENT_START_TOKEN,
    PAD_ID,
    PAD_TOKEN,
    SHARED_SPECIAL_TOKENS,
    STEP_ID,
    STEP_TOKEN,
    TARGET_STRUCTURAL_TOKENS,
    TYPE_ID,
    TYPE_TOKEN,
    UNK_ID,
    UNK_TOKEN,
    Vocabulary,
    VocabularyCorruptedError,
    build_source_vocabulary,
    build_target_vocabulary,
    calculate_oov_diagnostics,
)


class TestVocabulary(unittest.TestCase):

    def setUp(self):
        # Create a tiny synthetic vocabulary for fast, isolated tests
        self.sample_tokens = ["public", "class", "Main", "int", "x", ";"]
        token_to_id = {
            PAD_TOKEN: PAD_ID,
            UNK_TOKEN: UNK_ID,
            BOS_TOKEN: BOS_ID,
            EOS_TOKEN: EOS_ID,
        }
        id_to_token = [PAD_TOKEN, UNK_TOKEN, BOS_TOKEN, EOS_TOKEN]
        for t in self.sample_tokens:
            token_to_id[t] = len(id_to_token)
            id_to_token.append(t)

        self.source_vocab = Vocabulary(
            token_to_id=token_to_id,
            id_to_token=id_to_token,
            vocab_type="source",
            metadata={"description": "synthetic source test"},
        )

        # Synthetic target vocabulary with structural tokens
        tgt_t2i = {
            PAD_TOKEN: PAD_ID,
            UNK_TOKEN: UNK_ID,
            BOS_TOKEN: BOS_ID,
            EOS_TOKEN: EOS_ID,
            EVENT_START_TOKEN: EVENT_START_ID,
            EVENT_END_TOKEN: EVENT_END_ID,
            ATTR_START_TOKEN: ATTR_START_ID,
            ATTR_END_TOKEN: ATTR_END_ID,
            EMPTY_VAL_TOKEN: EMPTY_VAL_ID,
            TYPE_TOKEN: TYPE_ID,
            STEP_TOKEN: STEP_ID,
            ESCAPE_TOKEN: ESCAPE_ID,
        }
        tgt_i2t = list(SHARED_SPECIAL_TOKENS) + list(TARGET_STRUCTURAL_TOKENS)
        extra_tgt = ["MC", "TS", "CALLER", "TARGET", "main", "1"]
        for t in extra_tgt:
            tgt_t2i[t] = len(tgt_i2t)
            tgt_i2t.append(t)

        self.target_vocab = Vocabulary(
            token_to_id=tgt_t2i,
            id_to_token=tgt_i2t,
            vocab_type="target",
            metadata={"description": "synthetic target test"},
        )

    # 1. Exact shared special-token IDs
    def test_1_shared_special_token_ids(self):
        """Verifies exact shared special-token IDs 0..3 and properties."""
        for v in (self.source_vocab, self.target_vocab):
            self.assertEqual(v.pad_id, 0)
            self.assertEqual(v.unk_id, 1)
            self.assertEqual(v.bos_id, 2)
            self.assertEqual(v.eos_id, 3)
            self.assertEqual(v.get_token(0), "<PAD>")
            self.assertEqual(v.get_token(1), "<UNK>")
            self.assertEqual(v.get_token(2), "<BOS>")
            self.assertEqual(v.get_token(3), "<EOS>")
            self.assertEqual(v.get_id("<PAD>"), 0)
            self.assertEqual(v.get_id("<UNK>"), 1)
            self.assertEqual(v.get_id("<BOS>"), 2)
            self.assertEqual(v.get_id("<EOS>"), 3)

    # 2. Exact target structural-token IDs
    def test_2_target_structural_token_ids(self):
        """Verifies exact target structural and escape token IDs 4..11."""
        tv = self.target_vocab
        expected = [
            (EVENT_START_ID, "<EVENT_START>", tv.event_start_id),
            (EVENT_END_ID, "<EVENT_END>", tv.event_end_id),
            (ATTR_START_ID, "<ATTR_START>", tv.attr_start_id),
            (ATTR_END_ID, "<ATTR_END>", tv.attr_end_id),
            (EMPTY_VAL_ID, "<EMPTY_VAL>", tv.empty_val_id),
            (TYPE_ID, "TYPE", tv.type_id),
            (STEP_ID, "STEP", tv.step_id),
            (ESCAPE_ID, "\\", tv.escape_id),
        ]
        for expected_id, token_str, prop_val in expected:
            self.assertEqual(expected_id, prop_val)
            self.assertEqual(tv.get_id(token_str), expected_id)
            self.assertEqual(tv.get_token(expected_id), token_str)

    # 3. Separate source and target vocabularies
    def test_3_separate_source_and_target_vocabularies(self):
        """Source vocabulary does not inherit target structural markers."""
        for st in TARGET_STRUCTURAL_TOKENS:
            self.assertNotIn(st, self.source_vocab)
            self.assertEqual(self.source_vocab.get_id(st), self.source_vocab.unk_id)

    # 4. Encoding of known tokens
    def test_4_encoding_known_tokens(self):
        """Encodes known tokens to correct IDs, supporting optional BOS/EOS."""
        tokens = ["public", "class", "Main"]
        expected_ids = [4, 5, 6]
        self.assertEqual(self.source_vocab.encode(tokens), expected_ids)

        # With BOS and EOS
        with_boundaries = self.source_vocab.encode(tokens, add_bos=True, add_eos=True)
        self.assertEqual(with_boundaries, [BOS_ID] + expected_ids + [EOS_ID])

    # 5. Unknown-token fallback
    def test_5_unknown_token_fallback(self):
        """Unseen tokens map to UNK_ID (1)."""
        tokens = ["public", "UNKNOWN_VAR", "class", "ANOTHER_UNKNOWN"]
        encoded = self.source_vocab.encode(tokens)
        self.assertEqual(encoded, [4, UNK_ID, 5, UNK_ID])

    # 6. Decoding valid IDs
    def test_6_decoding_valid_ids(self):
        """Decodes integer IDs back to token strings."""
        ids = [4, 5, 6, 7, 8, 9]
        expected = ["public", "class", "Main", "int", "x", ";"]
        self.assertEqual(self.source_vocab.decode(ids), expected)

        # With skip_special=True
        ids_with_special = [BOS_ID, 4, PAD_ID, 5, EOS_ID]
        self.assertEqual(self.source_vocab.decode(ids_with_special, skip_special=True), ["public", "class"])

    # 7. Token-list round-trip for in-vocabulary sequences
    def test_7_token_list_round_trip(self):
        """Verifies decode(encode(tokens)) == tokens for any in-vocabulary sequence."""
        tokens = ["public", "class", "Main", "int", "x", ";"]
        encoded = self.source_vocab.encode(tokens)
        decoded = self.source_vocab.decode(encoded)
        self.assertEqual(decoded, tokens)

    # 8. Invalid ID handling
    def test_8_invalid_id_handling(self):
        """Decoding invalid or out-of-range IDs raises ValueError."""
        v = self.source_vocab
        size = len(v)

        with self.assertRaises(ValueError):
            v.get_token(-1)
        with self.assertRaises(ValueError):
            v.get_token(size)
        with self.assertRaises(ValueError):
            v.get_token(999999)
        with self.assertRaises(ValueError):
            v.decode([4, size, 5])
        with self.assertRaises(ValueError):
            v.decode([4, -1, 5])

    # 9. Runtime structural-token preservation
    def test_9_runtime_structural_token_preservation(self):
        """Target vocabulary encodes and decodes full runtime events cleanly."""
        tokens = [
            "<EVENT_START>", "TYPE", "MC", "STEP", "1",
            "<ATTR_START>", "CALLER", "main", "<ATTR_END>",
            "<EVENT_END>"
        ]
        encoded = self.target_vocab.encode(tokens)
        decoded = self.target_vocab.decode(encoded)
        self.assertEqual(decoded, tokens)
        self.assertEqual(encoded[0], EVENT_START_ID)
        self.assertEqual(encoded[1], TYPE_ID)
        self.assertEqual(encoded[3], STEP_ID)
        self.assertEqual(encoded[5], ATTR_START_ID)
        self.assertEqual(encoded[8], ATTR_END_ID)
        self.assertEqual(encoded[9], EVENT_END_ID)

    # 10. Backslash escaping compatibility
    def test_10_backslash_escaping_compatibility(self):
        """Literal reserved tokens preceded by backslash encode as two consecutive IDs."""
        # e.g. attribute value containing literal <EVENT_START>
        tokens = [
            "<ATTR_START>", "NOTE", "\\", "<EVENT_START>", "<ATTR_END>"
        ]
        # In target_vocab, "NOTE" is not present, maps to UNK (1)
        encoded = self.target_vocab.encode(tokens)
        # Check that \\ has ID 11 and <EVENT_START> has ID 4
        self.assertEqual(encoded[2], ESCAPE_ID)
        self.assertEqual(encoded[3], EVENT_START_ID)

        decoded = self.target_vocab.decode(encoded)
        self.assertEqual(decoded, ["<ATTR_START>", "<UNK>", "\\", "<EVENT_START>", "<ATTR_END>"])

    # 11. Deterministic frequency ordering and alphabetical tie-breaking
    def test_11_deterministic_frequency_ordering_and_tie_breaking(self):
        """Verifies tokens are ranked by frequency descending, then alphabetical ascending."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            train_jsonl = tmp_path / "train.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            # Create test programs:
            # p1: "zebra apple banana apple"
            # p2: "apple banana cat dog"
            # Counts: apple: 3, banana: 2, cat: 1, dog: 1, zebra: 1
            # Frequencies:
            # - apple (3) -> ID 4
            # - banana (2) -> ID 5
            # - cat (1), dog (1), zebra (1) have tie (count 1) -> tie-break alphabetically:
            #   cat -> ID 6, dog -> ID 7, zebra -> ID 8
            with open(train_jsonl, "w", encoding="utf-8") as f:
                f.write(json.dumps({"program_id": "p1", "source": {}, "runtime_trace": {}}) + "\n")
                f.write(json.dumps({"program_id": "p2", "source": {}, "runtime_trace": {}}) + "\n")

            with open(java_dir / "p1.java", "w", encoding="utf-8") as f:
                f.write("zebra apple banana apple;")
            with open(java_dir / "p2.java", "w", encoding="utf-8") as f:
                f.write("apple banana cat dog;")

            vocab, stats = build_source_vocabulary(train_jsonl, java_dir)

            self.assertEqual(vocab.get_token(0), "<PAD>")
            self.assertEqual(vocab.get_token(1), "<UNK>")
            self.assertEqual(vocab.get_token(2), "<BOS>")
            self.assertEqual(vocab.get_token(3), "<EOS>")
            self.assertEqual(vocab.get_token(4), "apple")
            self.assertEqual(vocab.get_token(5), ";")  # count 2
            self.assertEqual(vocab.get_token(6), "banana")  # count 2
            # count 1 tokens sorted alphabetically: cat, dog, zebra
            self.assertEqual(vocab.get_token(7), "cat")
            self.assertEqual(vocab.get_token(8), "dog")
            self.assertEqual(vocab.get_token(9), "zebra")

    # 12. Target vocabulary cap, including reserved entries
    def test_12_target_vocabulary_cap(self):
        """Verifies capacity cap strictly limits total vocabulary size to max_size."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            train_jsonl = tmp_path / "train.jsonl"

            # 12 reserved tokens + 20 distinct attributes
            events = []
            for i in range(20):
                events.append({
                    "step": i + 1,
                    "type": "MC",
                    "attributes": {f"K_{i}": f"V_{i}"}
                })

            with open(train_jsonl, "w", encoding="utf-8") as f:
                f.write(json.dumps({"program_id": "p1", "runtime_trace": {"events": events}}) + "\n")

            # Set max_size to 16: exactly 12 reserved + 4 most frequent candidate tokens
            vocab, stats = build_target_vocabulary(train_jsonl, max_size=16)
            self.assertEqual(len(vocab), 16)
            self.assertEqual(stats["vocabulary_size"], 16)
            self.assertEqual(stats["reserved_tokens_count"], 12)
            self.assertEqual(stats["eligible_tokens_added"], 4)
            self.assertTrue(stats["excluded_by_cap"] > 0)

            # All 12 reserved tokens must be present at IDs 0..11
            for exp_id, exp_tok in enumerate(SHARED_SPECIAL_TOKENS + TARGET_STRUCTURAL_TOKENS):
                self.assertEqual(vocab.get_token(exp_id), exp_tok)

    # 13. Source vocabulary completeness for training-observed tokens
    def test_13_source_vocabulary_completeness(self):
        """Every token in the training set is included when min_freq=1 and no cap."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            train_jsonl = tmp_path / "train.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            code = "public class Example { int x = 42; }"
            with open(train_jsonl, "w", encoding="utf-8") as f:
                f.write(json.dumps({"program_id": "p1"}) + "\n")
            with open(java_dir / "p1.java", "w", encoding="utf-8") as f:
                f.write(code)

            expected_tokens = tokenize_java_code(code)
            vocab, stats = build_source_vocabulary(train_jsonl, java_dir)

            for tok in expected_tokens:
                self.assertIn(tok, vocab)
                self.assertNotEqual(vocab.get_id(tok), UNK_ID)

    # 14. Save/reload equivalence
    def test_14_save_and_reload_equivalence(self):
        """Saved vocabulary loads back identically and round-trips bitwise."""
        with tempfile.TemporaryDirectory() as tmpdir:
            save_path = Path(tmpdir) / "vocab_target.json"
            self.target_vocab.save(save_path)

            reloaded = Vocabulary.load(save_path)
            self.assertEqual(len(reloaded), len(self.target_vocab))
            self.assertEqual(reloaded.vocab_type, self.target_vocab.vocab_type)
            self.assertEqual(reloaded.id_to_token, self.target_vocab.id_to_token)
            self.assertEqual(reloaded.token_to_id, self.target_vocab.token_to_id)
            self.assertEqual(reloaded.metadata, self.target_vocab.metadata)

            # Re-saving reloaded vocabulary produces bitwise identical file
            save_path2 = Path(tmpdir) / "vocab_target_2.json"
            reloaded.save(save_path2)
            self.assertEqual(save_path.read_bytes(), save_path2.read_bytes())

    # 15. Invalid or corrupted serialized vocabulary rejection
    def test_15_corrupted_vocabulary_rejection(self):
        """Rejects corrupted dictionaries (wrong size, duplicate token, missing specials)."""
        valid_dict = self.source_vocab.to_dict()

        # 1. Declared size mismatch
        bad_size = dict(valid_dict)
        bad_size["vocab_size"] = 9999
        with self.assertRaises(VocabularyCorruptedError):
            Vocabulary.from_dict(bad_size)

        # 2. Duplicate token in list
        bad_dupe = dict(valid_dict)
        bad_dupe["id_to_token"] = list(valid_dict["id_to_token"])
        bad_dupe["id_to_token"][4] = "<PAD>"
        with self.assertRaises(VocabularyCorruptedError):
            Vocabulary.from_dict(bad_dupe)

        # 3. Missing required field
        bad_field = dict(valid_dict)
        del bad_field["vocab_type"]
        with self.assertRaises(VocabularyCorruptedError):
            Vocabulary.from_dict(bad_field)

        # 4. Corrupted special token at ID 0
        bad_special = dict(valid_dict)
        bad_special["id_to_token"] = ["WRONG"] + valid_dict["id_to_token"][1:]
        with self.assertRaises(VocabularyCorruptedError):
            Vocabulary.from_dict(bad_special)

    # 16. Deterministic artifact contents across repeated builds
    def test_16_deterministic_artifact_contents(self):
        """Repeated builds on identical input produce bitwise identical JSON files."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            train_jsonl = tmp_path / "train.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            code = "public class Example { void run() { int k = 10; } }"
            with open(train_jsonl, "w", encoding="utf-8") as f:
                f.write(json.dumps({"program_id": "p1"}) + "\n")
            with open(java_dir / "p1.java", "w", encoding="utf-8") as f:
                f.write(code)

            vocab1, _ = build_source_vocabulary(train_jsonl, java_dir)
            vocab2, _ = build_source_vocabulary(train_jsonl, java_dir)

            p1 = tmp_path / "v1.json"
            p2 = tmp_path / "v2.json"
            vocab1.save(p1)
            vocab2.save(p2)

            self.assertEqual(p1.read_bytes(), p2.read_bytes())

    # 17. Training-only fitting
    def test_17_training_only_fitting_no_leakage(self):
        """Proves tokens present exclusively in validation/test are NOT in vocabulary."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            train_jsonl = tmp_path / "train.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            # Train program has "foo"
            with open(train_jsonl, "w", encoding="utf-8") as f:
                f.write(json.dumps({"program_id": "p_train"}) + "\n")
            with open(java_dir / "p_train.java", "w", encoding="utf-8") as f:
                f.write("int foo = 1;")

            # Val program exists in java_dir but NOT in train.jsonl
            with open(java_dir / "p_val.java", "w", encoding="utf-8") as f:
                f.write("int secret_validation_token = 99;")

            vocab, _ = build_source_vocabulary(train_jsonl, java_dir)
            self.assertIn("foo", vocab)
            self.assertNotIn("secret_validation_token", vocab)

    # 18. Correct OOV-rate calculations
    def test_18_oov_rate_calculations(self):
        """Calculates token-level and sequence-level OOV rates correctly."""
        vocab = self.source_vocab  # contains sample_tokens: public, class, Main, int, x, ;

        seq1 = ["public", "class"]  # 0 OOV
        seq2 = ["public", "UNKNOWN_1", "Main"]  # 1 OOV
        seq3 = ["UNKNOWN_2", "UNKNOWN_3"]  # 2 OOV

        sequences = [seq1, seq2, seq3]
        diag = calculate_oov_diagnostics(vocab, sequences)

        self.assertEqual(diag["total_tokens"], 7)
        self.assertEqual(diag["known_tokens"], 4)
        self.assertEqual(diag["unknown_tokens"], 3)
        self.assertEqual(diag["distinct_unseen_tokens"], 3)
        self.assertEqual(diag["total_sequences"], 3)
        self.assertEqual(diag["sequences_with_oov"], 2)
        self.assertAlmostEqual(diag["oov_rate"], 3 / 7, places=5)
        self.assertAlmostEqual(diag["sequence_oov_rate"], 2 / 3, places=5)

    # 19. Empty token sequences and minimal inputs
    def test_19_empty_token_sequences_and_minimal_inputs(self):
        """Handles empty lists cleanly during encode, decode, and OOV calculation."""
        self.assertEqual(self.source_vocab.encode([]), [])
        self.assertEqual(self.source_vocab.encode([], add_bos=True, add_eos=True), [BOS_ID, EOS_ID])
        self.assertEqual(self.source_vocab.decode([]), [])

        diag = calculate_oov_diagnostics(self.source_vocab, [])
        self.assertEqual(diag["total_tokens"], 0)
        self.assertEqual(diag["oov_rate"], 0.0)

    # 20. Backwards compatibility of special token names (<SOS> alias)
    def test_20_sos_alias_and_membership(self):
        """Verifies __contains__, __len__, and backwards-compatible attributes."""
        self.assertTrue("<PAD>" in self.source_vocab)
        self.assertTrue("public" in self.source_vocab)
        self.assertFalse("NON_EXISTENT_TOKEN_12345" in self.source_vocab)
        self.assertEqual(len(self.source_vocab), 4 + len(self.sample_tokens))


if __name__ == "__main__":
    unittest.main()

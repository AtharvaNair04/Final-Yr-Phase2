"""
Unit and regression tests for ExecutionTraceDataset, Padding, Masks & DataLoader (Module 4, Part 6).
"""

import json
import os
from pathlib import Path
import tempfile
import unittest

import torch
from torch.utils.data import DataLoader

from execution_dataset import (
    BatchData,
    DatasetItem,
    ExecutionTraceDataset,
    create_dataloader,
    execution_trace_collate_fn,
)
from vocabulary import (
    BOS_ID,
    EOS_ID,
    PAD_ID,
    SHARED_SPECIAL_TOKENS,
    TARGET_STRUCTURAL_TOKENS,
    UNK_ID,
    Vocabulary,
)


class TestExecutionDataset(unittest.TestCase):

    def setUp(self):
        # Create small synthetic vocabularies for isolated testing
        src_tokens = ["public", "class", "Main", "int", "x", "=", "1", ";", "{", "}"]
        src_t2i = {t: idx for idx, t in enumerate(SHARED_SPECIAL_TOKENS + src_tokens)}
        src_i2t = list(SHARED_SPECIAL_TOKENS) + src_tokens
        self.src_vocab = Vocabulary(token_to_id=src_t2i, id_to_token=src_i2t, vocab_type="source")

        tgt_tokens = ["MC", "TS", "CALLER", "main", "TARGET", "Foo", "1"]
        tgt_all = list(SHARED_SPECIAL_TOKENS) + list(TARGET_STRUCTURAL_TOKENS) + tgt_tokens
        tgt_t2i = {t: idx for idx, t in enumerate(tgt_all)}
        tgt_i2t = list(tgt_all)
        self.tgt_vocab = Vocabulary(token_to_id=tgt_t2i, id_to_token=tgt_i2t, vocab_type="target")

    # 1. Loading a valid JSONL record
    def test_1_loading_valid_record(self):
        """Loads a valid record and creates an indexed dataset."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main { int x = 1; }", encoding="utf-8")
            events = [{"type": "TS", "step": 1, "attributes": {"CALLER": "main"}}]
            record = {"program_id": "p1", "source": {"dummy": "spr"}, "runtime_trace": {"events": events}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(
                jsonl_path=jsonl_file,
                java_dir=java_dir,
                source_vocab=self.src_vocab,
                target_vocab=self.tgt_vocab,
                max_chunk_size=1024,
            )

            self.assertEqual(len(dataset), 1)
            item = dataset[0]
            self.assertEqual(item.program_id, "p1")
            self.assertEqual(item.chunk_index, 0)
            self.assertEqual(item.total_chunks, 1)

    # 2. Raw Java file resolution using program_id
    def test_2_raw_java_file_resolution(self):
        """Resolves raw Java file using program_id from java_dir."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            code = "public class Main { int x = 1; }"
            (java_dir / "prog_xyz.java").write_text(code, encoding="utf-8")
            record = {
                "program_id": "prog_xyz",
                "source": {},
                "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}
            }
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            item = dataset[0]
            self.assertIn("class", item.source_tokens)
            self.assertIn("Main", item.source_tokens)

    # 3. Proving SPR dictionary in JSONL "source" is NOT used
    def test_3_spr_dictionary_not_used_as_source(self):
        """Proves that raw Java tokens are loaded, ignoring any conflicting SPR dict."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            # Real Java code contains "int x = 1;"
            (java_dir / "p1.java").write_text("public class Main { int x = 1; }", encoding="utf-8")

            # SPR dict contains completely different tokens "SPR_EXCLUSIVE_TOKEN"
            spr_dict = {"classes": [{"name": "SPR_EXCLUSIVE_TOKEN"}]}
            record = {
                "program_id": "p1",
                "source": spr_dict,
                "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}
            }
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            item = dataset[0]

            self.assertNotIn("SPR_EXCLUSIVE_TOKEN", item.source_tokens)
            self.assertIn("x", item.source_tokens)
            self.assertIn("Main", item.source_tokens)

    # 4. Missing Java file error handling
    def test_4_missing_java_file_error(self):
        """Raises FileNotFoundError if Java source file for program_id does not exist."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            # p1.java is NOT created
            record = {"program_id": "p1", "source": {}, "runtime_trace": {}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            with self.assertRaises(FileNotFoundError):
                ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)

    # 5. Invalid or missing JSONL fields
    def test_5_invalid_or_missing_jsonl_fields(self):
        """Raises KeyError on missing program_id or ValueError on malformed JSON."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            # Missing program_id
            jsonl_file.write_text(json.dumps({"no_id": True}) + "\n", encoding="utf-8")
            with self.assertRaises(KeyError):
                ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)

            # Corrupted JSON
            jsonl_file.write_text("{ corrupt json\n", encoding="utf-8")
            with self.assertRaises(ValueError):
                ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)

    # 6. Correct source tokenization and vocabulary encoding
    def test_6_source_tokenization_and_encoding(self):
        """Verifies source IDs match source vocabulary mappings."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            record = {"program_id": "p1", "source": {}, "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            item = dataset[0]

            expected_tokens = ["public", "class", "Main"]
            expected_ids = [self.src_vocab.get_id(t) for t in expected_tokens]
            self.assertEqual(item.source_tokens, expected_tokens)
            self.assertEqual(item.source_ids, expected_ids)

    # 7. Runtime tokenization and vocabulary encoding
    def test_7_runtime_tokenization_and_encoding(self):
        """Verifies runtime trace tokenization and vocabulary encoding."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            events = [{"type": "TS", "step": 1, "attributes": {"CALLER": "main"}}]
            record = {"program_id": "p1", "source": {}, "runtime_trace": {"events": events}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            item = dataset[0]

            self.assertIn("<EVENT_START>", item.chunk_tokens)
            self.assertIn("TYPE", item.chunk_tokens)
            self.assertIn("TS", item.chunk_tokens)
            self.assertIn("<ATTR_START>", item.chunk_tokens)
            self.assertIn("<EVENT_END>", item.chunk_tokens)
            self.assertEqual(item.chunk_ids, self.tgt_vocab.encode(item.chunk_tokens))

    # 8. Chunking preserves event boundaries
    def test_8_chunking_preserves_event_boundaries(self):
        """Verifies complete events are preserved across chunk boundaries."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            # 6 events: each event has ~10 tokens
            events = [{"type": "MC", "step": i, "attributes": {"CALLER": "main"}} for i in range(1, 7)]
            record = {"program_id": "p1", "source": {}, "runtime_trace": {"events": events}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            # Force small chunk size: 25 tokens -> each chunk holds ~2 events
            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab, max_chunk_size=25)
            self.assertTrue(len(dataset) > 1)

            for idx in range(len(dataset)):
                item = dataset[idx]
                tokens = item.chunk_tokens
                # Every chunk must start with <EVENT_START> and end with <EVENT_END>
                self.assertEqual(tokens[0], "<EVENT_START>")
                self.assertEqual(tokens[-1], "<EVENT_END>")
                # Equal number of starts and ends
                self.assertEqual(tokens.count("<EVENT_START>"), tokens.count("<EVENT_END>"))

    # 9. Every normal chunk retains program ID and chunk index
    def test_9_chunk_metadata_retention(self):
        """Metadata fields program_id, chunk_index, and total_chunks are strictly preserved."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            events = [{"type": "MC", "step": i, "attributes": {"CALLER": "main"}} for i in range(1, 6)]
            record = {"program_id": "p1", "source": {}, "runtime_trace": {"events": events}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab, max_chunk_size=25)
            total = len(dataset)

            for i in range(total):
                item = dataset[i]
                self.assertEqual(item.program_id, "p1")
                self.assertEqual(item.chunk_index, i)
                self.assertEqual(item.total_chunks, total)

    # 10. Multiple chunks from a program remain in the same split
    def test_10_all_chunks_stay_in_program_split(self):
        """Chunks from different programs are indexed strictly per file without mixing."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "train.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            (java_dir / "p2.java").write_text("public class Main", encoding="utf-8")

            events1 = [{"type": "MC", "step": i, "attributes": {}} for i in range(1, 4)]
            events2 = [{"type": "TS", "step": i, "attributes": {}} for i in range(1, 4)]

            records = [
                {"program_id": "p1", "source": {}, "runtime_trace": {"events": events1}},
                {"program_id": "p2", "source": {}, "runtime_trace": {"events": events2}},
            ]
            with open(jsonl_file, "w", encoding="utf-8") as f:
                for r in records:
                    f.write(json.dumps(r) + "\n")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab, max_chunk_size=15)
            # p1 chunks must precede p2 chunks
            seen_p2 = False
            for item in dataset:
                if item.program_id == "p2":
                    seen_p2 = True
                if seen_p2:
                    self.assertEqual(item.program_id, "p2")

    # 11. Oversized events are not silently discarded
    def test_11_oversized_events_handling(self):
        """Oversized events are collected and tracked in metadata under 'separate' policy."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            # Create one small event and one massive event
            small_event = {"type": "TS", "step": 1, "attributes": {}}
            massive_event = {"type": "MC", "step": 2, "attributes": {f"K_{i}": f"V_{i}" for i in range(50)}}

            record = {"program_id": "p1", "source": {}, "runtime_trace": {"events": [small_event, massive_event]}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            # max_chunk_size = 20: small fits, massive does not
            dataset = ExecutionTraceDataset(
                jsonl_file, java_dir, self.src_vocab, self.tgt_vocab,
                max_chunk_size=20, oversized_policy="separate"
            )

            self.assertEqual(dataset.total_oversized_events, 1)
            self.assertEqual(len(dataset.oversized_metadata), 1)
            self.assertEqual(dataset.oversized_metadata[0]["program_id"], "p1")
            self.assertTrue(dataset.oversized_metadata[0]["event_token_length"] > 20)

    # 12. Empty chunk handling
    def test_12_empty_chunk_behavior(self):
        """If a program has an empty trace, it produces 0 chunks."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            record = {"program_id": "p1", "source": {}, "runtime_trace": {"events": []}}
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            self.assertEqual(len(dataset), 0)

    # 13. Exactly one BOS and one EOS for each valid target sequence
    def test_13_exactly_one_bos_and_one_eos(self):
        """Verifies exactly one BOS at beginning and one EOS at end of sequence pair."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            record = {
                "program_id": "p1",
                "source": {},
                "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}
            }
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            item = dataset[0]

            self.assertEqual(item.decoder_input_ids[0], BOS_ID)
            self.assertNotIn(BOS_ID, item.decoder_input_ids[1:])
            self.assertEqual(item.target_ids[-1], EOS_ID)
            self.assertNotIn(EOS_ID, item.target_ids[:-1])

    # 14. Correct teacher-forcing shift
    def test_14_teacher_forcing_shift_contract(self):
        """Verifies target_ids is exactly decoder_input_ids shifted left by one."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "p1.java").write_text("public class Main", encoding="utf-8")
            record = {
                "program_id": "p1",
                "source": {},
                "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {"CALLER": "main"}}]}
            }
            jsonl_file.write_text(json.dumps(record) + "\n", encoding="utf-8")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            item = dataset[0]

            dec_in = item.decoder_input_ids
            tgt_out = item.target_ids

            self.assertEqual(len(dec_in), len(tgt_out))
            self.assertEqual(len(dec_in), len(item.chunk_ids) + 1)
            # Shift check: tgt_out[i] == dec_in[i+1]
            for i in range(len(item.chunk_ids)):
                self.assertEqual(tgt_out[i], dec_in[i + 1])
                self.assertEqual(tgt_out[i], item.chunk_ids[i])

    # 15. Mixed-length batches pad correctly
    def test_15_mixed_length_batch_padding(self):
        """Collate function pads heterogeneous sequence lengths correctly."""
        item1 = DatasetItem(
            program_id="p1", chunk_index=0, total_chunks=1,
            source_tokens=["public"], source_ids=[4],
            chunk_tokens=["A"], chunk_ids=[10],
            decoder_input_ids=[BOS_ID, 10], target_ids=[10, EOS_ID]
        )
        item2 = DatasetItem(
            program_id="p2", chunk_index=0, total_chunks=1,
            source_tokens=["public", "class", "Main"], source_ids=[4, 5, 6],
            chunk_tokens=["A", "B", "C"], chunk_ids=[10, 11, 12],
            decoder_input_ids=[BOS_ID, 10, 11, 12], target_ids=[10, 11, 12, EOS_ID]
        )

        batch = execution_trace_collate_fn([item1, item2], src_pad_id=PAD_ID, tgt_pad_id=PAD_ID)

        # Batch size 2
        self.assertEqual(batch.src_ids.shape, (2, 3))
        self.assertEqual(batch.decoder_input_ids.shape, (2, 4))
        self.assertEqual(batch.target_ids.shape, (2, 4))

        # Check item1 padding: pos 1 and 2 in source are PAD_ID
        self.assertEqual(batch.src_ids[0, 0].item(), 4)
        self.assertEqual(batch.src_ids[0, 1].item(), PAD_ID)
        self.assertEqual(batch.src_ids[0, 2].item(), PAD_ID)

    # 16. Padding masks use documented semantics (True = padding/ignored)
    def test_16_padding_mask_semantics(self):
        """src_padding_mask and target_padding_mask have True for padding positions."""
        item1 = DatasetItem(
            program_id="p1", chunk_index=0, total_chunks=1,
            source_tokens=["public"], source_ids=[4],
            chunk_tokens=["A"], chunk_ids=[10],
            decoder_input_ids=[BOS_ID, 10], target_ids=[10, EOS_ID]
        )
        item2 = DatasetItem(
            program_id="p2", chunk_index=0, total_chunks=1,
            source_tokens=["public", "class"], source_ids=[4, 5],
            chunk_tokens=["A", "B"], chunk_ids=[10, 11],
            decoder_input_ids=[BOS_ID, 10, 11], target_ids=[10, 11, EOS_ID]
        )

        batch = execution_trace_collate_fn([item1, item2])

        # item 1 source: index 0 is valid (False), index 1 is pad (True)
        self.assertFalse(batch.src_padding_mask[0, 0].item())
        self.assertTrue(batch.src_padding_mask[0, 1].item())
        # item 2 source: both valid (False)
        self.assertFalse(batch.src_padding_mask[1, 0].item())
        self.assertFalse(batch.src_padding_mask[1, 1].item())

    # 17. Loss masks exclude padding (True = active loss, False = padding)
    def test_17_loss_mask_semantics(self):
        """loss_mask is True for active target positions and False for padding."""
        item1 = DatasetItem(
            program_id="p1", chunk_index=0, total_chunks=1,
            source_tokens=["public"], source_ids=[4],
            chunk_tokens=["A"], chunk_ids=[10],
            decoder_input_ids=[BOS_ID, 10], target_ids=[10, EOS_ID]
        )
        item2 = DatasetItem(
            program_id="p2", chunk_index=0, total_chunks=1,
            source_tokens=["public"], source_ids=[4],
            chunk_tokens=["A", "B"], chunk_ids=[10, 11],
            decoder_input_ids=[BOS_ID, 10, 11], target_ids=[10, 11, EOS_ID]
        )

        batch = execution_trace_collate_fn([item1, item2])

        # item 1 targets: indices 0, 1 are active loss (True), index 2 is pad (False)
        self.assertTrue(batch.loss_mask[0, 0].item())
        self.assertTrue(batch.loss_mask[0, 1].item())
        self.assertFalse(batch.loss_mask[0, 2].item())

        # item 2 targets: indices 0, 1, 2 are active loss (True)
        self.assertTrue(batch.loss_mask[1, 0].item())
        self.assertTrue(batch.loss_mask[1, 1].item())
        self.assertTrue(batch.loss_mask[1, 2].item())

    # 18. Tensor shapes and dtypes are correct
    def test_18_tensor_shapes_and_dtypes(self):
        """All ID tensors are torch.long, masks are torch.bool, lengths are torch.long."""
        item = DatasetItem(
            program_id="p1", chunk_index=0, total_chunks=1,
            source_tokens=["public"], source_ids=[4],
            chunk_tokens=["A"], chunk_ids=[10],
            decoder_input_ids=[BOS_ID, 10], target_ids=[10, EOS_ID]
        )
        batch = execution_trace_collate_fn([item])

        self.assertEqual(batch.src_ids.dtype, torch.long)
        self.assertEqual(batch.decoder_input_ids.dtype, torch.long)
        self.assertEqual(batch.target_ids.dtype, torch.long)
        self.assertEqual(batch.src_lengths.dtype, torch.long)
        self.assertEqual(batch.target_lengths.dtype, torch.long)
        self.assertEqual(batch.chunk_indices.dtype, torch.long)
        self.assertEqual(batch.total_chunks.dtype, torch.long)

        self.assertEqual(batch.src_padding_mask.dtype, torch.bool)
        self.assertEqual(batch.target_padding_mask.dtype, torch.bool)
        self.assertEqual(batch.loss_mask.dtype, torch.bool)

    # 19. Deterministic validation/test ordering
    def test_19_deterministic_validation_ordering(self):
        """DataLoader with shuffle=False yields identical order across passes."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "val.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            for i in range(5):
                pid = f"val_p{i}"
                (java_dir / f"{pid}.java").write_text("public class Main", encoding="utf-8")

            with open(jsonl_file, "w", encoding="utf-8") as f:
                for i in range(5):
                    rec = {"program_id": f"val_p{i}", "source": {}, "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}}
                    f.write(json.dumps(rec) + "\n")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            loader1 = create_dataloader(dataset, batch_size=2, shuffle=False)
            loader2 = create_dataloader(dataset, batch_size=2, shuffle=False)

            pids1 = [b.program_ids for b in loader1]
            pids2 = [b.program_ids for b in loader2]
            self.assertEqual(pids1, pids2)

    # 20. DataLoader preserves final partial batch by default
    def test_20_final_partial_batch_preserved(self):
        """DataLoader with drop_last=False preserves final partial batch."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            jsonl_file = tmp_path / "data.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            for i in range(3):
                pid = f"p{i}"
                (java_dir / f"{pid}.java").write_text("public class Main", encoding="utf-8")

            with open(jsonl_file, "w", encoding="utf-8") as f:
                for i in range(3):
                    rec = {"program_id": f"p{i}", "source": {}, "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}}
                    f.write(json.dumps(rec) + "\n")

            dataset = ExecutionTraceDataset(jsonl_file, java_dir, self.src_vocab, self.tgt_vocab)
            # 3 items, batch_size=2 -> 2 batches (size 2 and size 1)
            loader = create_dataloader(dataset, batch_size=2, shuffle=False, drop_last=False)
            batches = list(loader)

            self.assertEqual(len(batches), 2)
            self.assertEqual(len(batches[0].program_ids), 2)
            self.assertEqual(len(batches[1].program_ids), 1)

    # 21. No accidental cross-split leakage
    def test_21_no_cross_split_leakage(self):
        """Datasets instantiated from distinct split files remain strictly isolated."""
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp_path = Path(tmpdir)
            train_file = tmp_path / "train.jsonl"
            val_file = tmp_path / "val.jsonl"
            java_dir = tmp_path / "java_src"
            java_dir.mkdir()

            (java_dir / "tr1.java").write_text("public class Main", encoding="utf-8")
            (java_dir / "val1.java").write_text("public class Main", encoding="utf-8")

            train_file.write_text(json.dumps({"program_id": "tr1", "source": {}, "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}}) + "\n", encoding="utf-8")
            val_file.write_text(json.dumps({"program_id": "val1", "source": {}, "runtime_trace": {"events": [{"type": "TS", "step": 1, "attributes": {}}]}}) + "\n", encoding="utf-8")

            ds_train = ExecutionTraceDataset(train_file, java_dir, self.src_vocab, self.tgt_vocab)
            ds_val = ExecutionTraceDataset(val_file, java_dir, self.src_vocab, self.tgt_vocab)

            train_pids = {ds_train[i].program_id for i in range(len(ds_train))}
            val_pids = {ds_val[i].program_id for i in range(len(ds_val))}

            self.assertEqual(train_pids, {"tr1"})
            self.assertEqual(val_pids, {"val1"})
            self.assertEqual(train_pids.intersection(val_pids), set())


if __name__ == "__main__":
    unittest.main()

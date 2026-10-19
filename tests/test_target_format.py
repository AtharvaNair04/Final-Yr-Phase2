"""Tests for the esr_v2_nostep target format, its dataset/cache path and step-aware evaluation."""

import json
import shutil
import tempfile
import unittest
from pathlib import Path

from chunk_cache import CacheMismatchError, CachedExecutionTraceDataset, build_chunk_cache
from evaluation import event_metrics, teacher_forced_counts, aggregate_tf
from execution_dataset import ExecutionTraceDataset
from target_format import (
    TARGET_FORMAT_V1, TARGET_FORMAT_V2, FormattedExecutionTraceDataset, StepFreeExecutionTraceDataset,
    TargetFormatError, count_events, first_n_events, insert_steps, insert_steps_chunks, strip_step_markers_lenient,
    strip_steps, tokenize_runtime_nostep,
)
from tokenize_and_analyze import tokenize_runtime
from vocabulary import SHARED_SPECIAL_TOKENS, TARGET_STRUCTURAL_TOKENS, Vocabulary

ID_FIELDS = ("program_id", "chunk_index", "total_chunks", "source_ids", "chunk_ids", "decoder_input_ids", "target_ids")


def ev(step, etype, **attrs):
    return {"step": step, "type": etype, "attributes": attrs}


# A trace with an escaped STEP key, an escaped STEP value token, an escaped backslash and an empty value.
TRICKY = {"events": [
    ev(1, "TS", VALUE_1="main (1)"),
    ev(2, "VW", CONTEXT="Main#main:1", STEP="5"),             # Java variable named `step`
    ev(3, "VW", CONTEXT="Main#main:1", X="STEP TYPE"),        # value tokens equal to reserved tokens
    ev(4, "ME", RETURNER="Main#main:1", VALUE="\\", NOTE=""),
]}


class TestSerializationAndConversions(unittest.TestCase):
    def test_v2_has_no_step_marker_but_keeps_escaped_step(self):
        v2 = tokenize_runtime_nostep(TRICKY)
        self.assertEqual(v2[:4], ["<EVENT_START>", "TYPE", "TS", "<ATTR_START>"])
        unescaped = [i for i, t in enumerate(v2) if t == "STEP" and v2[i - 1] != "\\"]
        self.assertEqual(unescaped, [])
        self.assertEqual(sum(1 for i, t in enumerate(v2) if t == "STEP" and v2[i - 1] == "\\"), 2)

    def test_round_trip_exact(self):
        v1, v2 = tokenize_runtime(TRICKY), tokenize_runtime_nostep(TRICKY)
        self.assertEqual(insert_steps(v2, 1), v1)
        self.assertEqual(strip_steps(v1), v2)
        self.assertEqual(strip_step_markers_lenient(v1), v2)   # lenient == strict on valid streams
        self.assertEqual(count_events(v2), 4)

    def test_empty_trace_and_input_shapes(self):
        self.assertEqual(tokenize_runtime_nostep({"events": []}), [])
        self.assertEqual(insert_steps([], 1), [])
        self.assertEqual(strip_steps([]), [])
        single = ev(1, "MC", CALLER="SYSTEM")
        for shape in (single, [single], {"runtime_events": [single]}):
            self.assertEqual(tokenize_runtime_nostep(shape), strip_steps(tokenize_runtime(shape)))

    def test_strict_functions_reject_malformed_input(self):
        v1, v2 = tokenize_runtime(TRICKY), tokenize_runtime_nostep(TRICKY)
        with self.assertRaises(TargetFormatError):
            insert_steps(v1, 1)                                  # already has STEP
        with self.assertRaises(TargetFormatError):
            strip_steps(v2)                                      # has no STEP
        with self.assertRaises(TargetFormatError):
            insert_steps(v2[:-1], 1)                             # unclosed event
        with self.assertRaises(TargetFormatError):
            insert_steps(["stray"] + v2, 1)                      # token outside events
        with self.assertRaises(TargetFormatError):
            insert_steps(["<EVENT_START>", "<ATTR_START>", "K", "v", "<ATTR_END>", "<EVENT_END>"], 1)  # no TYPE
        with self.assertRaises(TargetFormatError):
            insert_steps(v2, 0)

    def test_lenient_strip_on_malformed_output(self):
        out = ["<EVENT_START>", "TYPE", "VW", "STEP", "<UNK>", "<ATTR_START>", "\\", "STEP", "7", "<ATTR_END>",
               "<ATTR_START>", "STEP"]                           # trailing marker without value
        self.assertEqual(strip_step_markers_lenient(out),
                         ["<EVENT_START>", "TYPE", "VW", "<ATTR_START>", "\\", "STEP", "7", "<ATTR_END>", "<ATTR_START>"])
        self.assertEqual(strip_step_markers_lenient([4, 9, 50, 10, 77, 11, 10, 60], step=10, escape=11),
                         [4, 9, 50, 11, 10, 60])

    def test_first_n_events(self):
        v2 = tokenize_runtime_nostep(TRICKY)
        two = first_n_events(v2, 2)
        self.assertEqual(count_events(two), 2)
        self.assertEqual(two, v2[:len(two)])
        self.assertEqual(first_n_events(v2, 99), v2)
        self.assertEqual(first_n_events(v2, 0), [])

    def test_chunk_boundary_reconstruction(self):
        trace = {"events": [ev(i, "VW", CONTEXT="Main#main:1", X=str(i)) for i in range(1, 30)]}
        from chunk_trace import chunk_trace_tokens
        chunks = chunk_trace_tokens(tokenize_runtime_nostep(trace), max_chunk_size=40).chunks
        self.assertGreater(len(chunks), 2)
        self.assertEqual(insert_steps_chunks(chunks), tokenize_runtime(trace))
        # Chunk k on its own is numbered from (events before it) + 1.
        before = count_events(chunks[0])
        self.assertEqual(insert_steps(chunks[1], before + 1)[3:5], ["STEP", str(before + 1)])


class DatasetFixture(unittest.TestCase):
    def setUp(self):
        self.tmp = Path(tempfile.mkdtemp())
        self.java = self.tmp / "java"
        self.java.mkdir()
        self.jsonl = self.tmp / "split.jsonl"
        records = [
            ("p1", [ev(i, "VW", CONTEXT="Main#main:1", X=str(i)) for i in range(1, 12)]),
            ("p2", TRICKY["events"]),
            ("p3", []),
        ]
        with open(self.jsonl, "w", encoding="utf-8", newline="\n") as f:
            for pid, events in records:
                (self.java / f"{pid}.java").write_text("public class Main { int x = 1 ; }", encoding="utf-8")
                f.write(json.dumps({"program_id": pid, "source": {}, "runtime_trace": {"events": events}}) + "\n")
        base = list(SHARED_SPECIAL_TOKENS)
        src = base + ["public", "class", "Main", "{", "}", "int", "x", "=", "1", ";"]
        tgt = base + list(TARGET_STRUCTURAL_TOKENS) + ["VW", "TS", "ME", "CONTEXT", "Main", "#", "main", ":", "1", "X"]
        self.sv = Vocabulary({t: i for i, t in enumerate(src)}, src, vocab_type="source")
        self.tv = Vocabulary({t: i for i, t in enumerate(tgt)}, tgt, vocab_type="target")

    def tearDown(self):
        shutil.rmtree(self.tmp, ignore_errors=True)

    def ds(self, cls=ExecutionTraceDataset, **kw):
        return cls(self.jsonl, self.java, self.sv, self.tv, max_chunk_size=24, **kw)


class TestDatasets(DatasetFixture):
    def test_v1_subclass_reproduces_original_exactly(self):
        orig, sub = self.ds(), self.ds(FormattedExecutionTraceDataset, target_format=TARGET_FORMAT_V1)
        self.assertEqual(len(orig), len(sub))
        self.assertEqual(orig.chunks_per_record, sub.chunks_per_record)
        for i in range(len(orig)):
            for f in ID_FIELDS + ("chunk_tokens", "source_tokens"):
                self.assertEqual(getattr(orig[i], f), getattr(sub[i], f), f"{i} {f}")

    def test_v2_dataset_reconstructs_v1_per_program(self):
        v2 = self.ds(StepFreeExecutionTraceDataset)
        self.assertEqual(v2.target_format, TARGET_FORMAT_V2)
        by_pid = {}
        for i in range(len(v2)):
            it = v2[i]
            by_pid.setdefault(it.program_id, []).append(it.chunk_tokens)
        self.assertGreater(len(by_pid["p1"]), 1)                 # multi-chunk program
        for pid, events in (("p1", None), ("p2", TRICKY["events"])):
            rec = [json.loads(l) for l in self.jsonl.read_text(encoding="utf-8").splitlines() if json.loads(l)["program_id"] == pid][0]
            self.assertEqual(insert_steps_chunks(by_pid[pid]), tokenize_runtime(rec["runtime_trace"]))

    def test_v2_refuses_dropped_oversized_events(self):
        big = [ev(1, "TS", A=" ".join("abcdefghijklmnopqrstuvwxyz"))]
        with open(self.jsonl, "a", encoding="utf-8", newline="\n") as f:
            f.write(json.dumps({"program_id": "p1", "source": {}, "runtime_trace": {"events": big}}) + "\n")
        self.assertEqual(self.ds().total_oversized_events, 1)    # v1 dataset drops it silently
        with self.assertRaises(TargetFormatError):
            self.ds(StepFreeExecutionTraceDataset)

    def test_cache_formats_are_separate(self):
        v1_dir, v2_dir = self.tmp / "c1", self.tmp / "c2"
        build_chunk_cache(self.ds(), v1_dir)
        build_chunk_cache(self.ds(StepFreeExecutionTraceDataset), v2_dir)
        args = (self.jsonl, self.java, self.sv, self.tv)
        c2 = CachedExecutionTraceDataset(v2_dir, *args, max_chunk_size=24, target_format=TARGET_FORMAT_V2)
        src = self.ds(StepFreeExecutionTraceDataset)
        for i in range(len(src)):
            for f in ID_FIELDS:
                self.assertEqual(getattr(src[i], f), getattr(c2[i], f))
        with self.assertRaises(CacheMismatchError):
            CachedExecutionTraceDataset(v2_dir, *args, max_chunk_size=24)                      # read as v1
        with self.assertRaises(CacheMismatchError):
            CachedExecutionTraceDataset(v1_dir, *args, max_chunk_size=24, target_format=TARGET_FORMAT_V2)
        CachedExecutionTraceDataset(v1_dir, *args, max_chunk_size=24)                          # v1 still fine


class TestStepAwareEvaluation(unittest.TestCase):
    def test_step_match_not_credited_for_step_free_references(self):
        v2 = tokenize_runtime_nostep(TRICKY)
        m = event_metrics(v2, v2)
        self.assertIsNone(m["step_match_rate"])
        self.assertEqual(m["event_exact_rate"], 1.0)
        self.assertEqual(m["attr_value_match_rate"], 1.0)

    def test_step_match_unchanged_for_v1_references(self):
        v1 = tokenize_runtime(TRICKY)
        self.assertEqual(event_metrics(v1, v1)["step_match_rate"], 1.0)
        shifted = insert_steps(tokenize_runtime_nostep(TRICKY), 5)
        self.assertEqual(event_metrics(v1, shifted)["step_match_rate"], 0.0)
        # A step-free prediction against a v1 reference scores 0 on steps, not "not applicable".
        self.assertEqual(event_metrics(v1, tokenize_runtime_nostep(TRICKY))["step_match_rate"], 0.0)

    def test_teacher_forced_step_accuracy_absent_for_v2_targets(self):
        # STEP=10, ESC=11: an escaped STEP key is not a step-value position.
        target = [4, 9, 50, 6, 11, 10, 77, 7, 5, 3]
        c = teacher_forced_counts(target, target, [0.0] * len(target), eos_id=3, unk_id=1,
                                  structural_ids=set(range(4, 12)), step_id=10, escape_id=11)
        self.assertIsNone(aggregate_tf(c)["step_value_acc_excl_unk"])


if __name__ == "__main__":
    unittest.main()

"""Tests for Design A helpers: first-chunk subset, split integrity, source swapping, retrieval, statistics."""

import json
import shutil
import tempfile
import unittest
from pathlib import Path

from chunk_cache import CachedExecutionTraceDataset, build_chunk_cache
from execution_dataset import create_dataloader
from first_chunk import (
    FirstChunkDataset, assert_disjoint, binomial_two_sided_p, bootstrap_mean_ci, first_chunk_indices, jaccard,
    mismatch_assignment, retrieve_nearest, sign_test, wilson_interval,
)
from target_format import TARGET_FORMAT_V2, StepFreeExecutionTraceDataset
from vocabulary import SHARED_SPECIAL_TOKENS, TARGET_STRUCTURAL_TOKENS, Vocabulary


class TestFirstChunkSubset(unittest.TestCase):
    def setUp(self):
        self.tmp = Path(tempfile.mkdtemp())
        java = self.tmp / "java"
        java.mkdir()
        self.jsonl = self.tmp / "split.jsonl"
        n_events = {"p1": 12, "p2": 1, "p3": 0, "p4": 6}       # p3 has no chunks
        with open(self.jsonl, "w", encoding="utf-8", newline="\n") as f:
            for pid, n in n_events.items():
                (java / f"{pid}.java").write_text(f"class Main {{ int {pid} ; }}", encoding="utf-8")
                events = [{"step": i, "type": "VW", "attributes": {"X": str(i)}} for i in range(1, n + 1)]
                f.write(json.dumps({"program_id": pid, "source": {}, "runtime_trace": {"events": events}}) + "\n")
        base = list(SHARED_SPECIAL_TOKENS)
        src = base + ["class", "Main", "{", "}", "int", ";"]
        tgt = base + list(TARGET_STRUCTURAL_TOKENS) + ["VW", "X", "1", "2", "3"]
        self.sv = Vocabulary({t: i for i, t in enumerate(src)}, src, vocab_type="source")
        self.tv = Vocabulary({t: i for i, t in enumerate(tgt)}, tgt, vocab_type="target")
        self.java = java
        orig = StepFreeExecutionTraceDataset(self.jsonl, java, self.sv, self.tv, max_chunk_size=24)
        build_chunk_cache(orig, self.tmp / "cache")
        self.orig = orig
        self.cached = CachedExecutionTraceDataset(self.tmp / "cache", self.jsonl, java, self.sv, self.tv,
                                                  max_chunk_size=24, target_format=TARGET_FORMAT_V2)

    def tearDown(self):
        shutil.rmtree(self.tmp, ignore_errors=True)

    def test_selects_exactly_chunk_zero_per_program(self):
        self.assertGreater(len(self.cached), 4)                   # several multi-chunk programs
        idx = first_chunk_indices(self.cached)
        self.assertEqual(len(idx), 3)                             # p3 has no chunks
        sub = FirstChunkDataset(self.cached)
        self.assertEqual(sub.program_ids, ["p1", "p2", "p4"])
        for k in range(len(sub)):
            a, b = sub[k], self.orig[idx[k]]
            self.assertEqual(a.chunk_index, 0)
            for f in ("program_id", "source_ids", "chunk_ids", "decoder_input_ids", "target_ids", "total_chunks"):
                self.assertEqual(getattr(a, f), getattr(b, f))

    def test_dataloader_over_subset(self):
        loader = create_dataloader(FirstChunkDataset(self.cached), batch_size=2, shuffle=True)
        self.assertEqual(sum(b.src_ids.size(0) for b in loader), 3)

    def test_split_integrity(self):
        assert_disjoint(["a", "b"], ["c"])
        with self.assertRaises(ValueError):
            assert_disjoint(["a", "b"], ["b"])


class TestSourceSwapAndRetrieval(unittest.TestCase):
    def test_mismatch_is_deterministic_derangement(self):
        pids = ["p3", "p1", "p2", "p9"]
        a = mismatch_assignment(pids)
        self.assertEqual(a, mismatch_assignment(list(reversed(pids))))
        self.assertTrue(all(a[p] != p for p in pids))
        self.assertEqual(sorted(a.values()), sorted(pids))       # bijection
        self.assertEqual(a, {"p1": "p2", "p2": "p3", "p3": "p9", "p9": "p1"})
        with self.assertRaises(ValueError):
            mismatch_assignment(["only"])

    def test_retrieval_picks_highest_jaccard_with_stable_ties(self):
        self.assertAlmostEqual(jaccard([1, 2, 3], [2, 3, 4]), 0.5)
        self.assertEqual(jaccard([], []), 0.0)
        train = {"t2": [1, 2, 9], "t1": [1, 2, 3, 4], "t3": [7, 8]}
        self.assertEqual(retrieve_nearest([1, 2, 3], train), ("t1", 0.75))
        tie = {"b": [1, 2], "a": [1, 2]}
        self.assertEqual(retrieve_nearest([1, 2], tie)[0], "a")


class TestStatistics(unittest.TestCase):
    def test_binomial(self):
        self.assertAlmostEqual(binomial_two_sided_p(9, 10), 22 / 1024)      # P(X<=1)+P(X>=9)
        self.assertAlmostEqual(binomial_two_sided_p(5, 10), 1.0)
        self.assertEqual(binomial_two_sided_p(0, 0), 1.0)

    def test_wilson(self):
        lo, hi = wilson_interval(50, 100)
        self.assertAlmostEqual(lo, 0.4038, places=3)
        self.assertAlmostEqual(hi, 0.5962, places=3)
        self.assertEqual(wilson_interval(0, 0), (None, None))

    def test_sign_test_and_bootstrap(self):
        s = sign_test([0.1, 0.2, -0.1, 0.0, 0.3])
        self.assertEqual((s["wins"], s["losses"], s["ties"]), (3, 1, 1))
        self.assertAlmostEqual(s["p_value"], binomial_two_sided_p(3, 4))
        mean, lo, hi = bootstrap_mean_ci([1.0, 1.0, 1.0])
        self.assertEqual((mean, lo, hi), (1.0, 1.0, 1.0))
        mean, lo, hi = bootstrap_mean_ci([0.0, 1.0] * 20)
        self.assertAlmostEqual(mean, 0.5)
        self.assertLess(lo, 0.5)
        self.assertGreater(hi, 0.5)


if __name__ == "__main__":
    unittest.main()

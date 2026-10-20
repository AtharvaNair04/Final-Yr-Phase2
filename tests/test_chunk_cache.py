"""Synthetic equivalence, fingerprint-invalidation and overflow tests for chunk_cache.py."""

import json
import shutil
import tempfile
import unittest
from pathlib import Path

import torch

from chunk_cache import (
    CODE_FILES, REPO_ROOT, CacheError, CacheMismatchError, CacheOverflowError,
    CachedExecutionTraceDataset, _to_uint16, build_chunk_cache,
)
from execution_dataset import ExecutionTraceDataset, create_dataloader, execution_trace_collate_fn
from vocabulary import SHARED_SPECIAL_TOKENS, TARGET_STRUCTURAL_TOKENS, Vocabulary

ID_FIELDS = ("program_id", "chunk_index", "total_chunks", "source_ids", "chunk_ids", "decoder_input_ids", "target_ids")
BATCH_TENSORS = ("chunk_indices", "total_chunks", "src_ids", "src_padding_mask", "decoder_input_ids",
                 "target_ids", "target_padding_mask", "loss_mask", "src_lengths", "target_lengths")
MAX_CHUNK = 24


def make_vocab(tokens, vocab_type):
    base = list(SHARED_SPECIAL_TOKENS) + (list(TARGET_STRUCTURAL_TOKENS) if vocab_type == "target" else [])
    i2t = base + [t for t in tokens if t not in base]
    return Vocabulary({t: i for i, t in enumerate(i2t)}, i2t, vocab_type=vocab_type)


def event(step, etype, **attrs):
    return {"step": step, "type": etype, "attributes": attrs}


class CacheFixture(unittest.TestCase):
    def setUp(self):
        self.tmp = Path(tempfile.mkdtemp())
        self.java = self.tmp / "java"
        self.java.mkdir()
        self.jsonl = self.tmp / "split.jsonl"
        records = [
            # Multi-chunk program with a long source (exercises truncation).
            ("p1", "public class Main { int x = 1 ; int y = 2 ; int z = 3 ; }",
             [event(i, "VW", CONTEXT="Main#main:1", X=str(i)) for i in range(1, 9)]),
            # Program with an empty trace -> zero chunks.
            ("p2", "public class Main { }", []),
            # Escaped reserved tokens and an empty value.
            ("p3", "class Main { }", [event(1, "MC", STEP="TYPE <EVENT_END>", NOTE=""), event(2, "ME", VALUE="\\")]),
            # One oversized event (> MAX_CHUNK tokens) dropped under 'separate'.
            ("p4", "class Main { int x ; }",
             [event(1, "TS", A="a b c d e f g h i j k l m n o p q r s t"), event(2, "TE", B="z")]),
        ]
        with open(self.jsonl, "w", encoding="utf-8", newline="\n") as f:  # LF on every OS
            for pid, code, events in records:
                (self.java / f"{pid}.java").write_text(code, encoding="utf-8")
                f.write(json.dumps({"program_id": pid, "source": {}, "runtime_trace": {"events": events}}) + "\n")
        self.src_vocab = make_vocab(["public", "class", "Main", "{", "}", "int", "x", "=", "1", ";"], "source")
        self.tgt_vocab = make_vocab(["VW", "MC", "ME", "TS", "TE", "CONTEXT", "Main", "#", "main", ":", "1", "X"], "target")

    def tearDown(self):
        shutil.rmtree(self.tmp, ignore_errors=True)

    def original(self, max_source_length=None, vocab=None, max_chunk=MAX_CHUNK):
        return ExecutionTraceDataset(self.jsonl, self.java, self.src_vocab, vocab or self.tgt_vocab,
                                     max_chunk_size=max_chunk, max_source_length=max_source_length)

    def build(self, name="cache", **kw):
        out = self.tmp / name
        build_chunk_cache(self.original(), out, **kw)
        return out

    def cached(self, cache_dir, max_source_length=None, max_chunk_size=MAX_CHUNK, code_root=REPO_ROOT):
        return CachedExecutionTraceDataset(cache_dir, self.jsonl, self.java, self.src_vocab, self.tgt_vocab,
                                           max_chunk_size=max_chunk_size, max_source_length=max_source_length,
                                           code_root=code_root)


class TestEquivalence(CacheFixture):
    def test_fixture_exercises_edge_cases(self):
        ds = self.original()
        self.assertEqual(ds.chunks_per_record[1], 0)            # empty trace
        self.assertGreater(ds.chunks_per_record[0], 1)          # multi-chunk
        self.assertEqual(ds.total_oversized_events, 1)          # oversized event

    def test_every_item_identical_with_and_without_truncation(self):
        cache = self.build()
        for limit in (None, 5):
            orig, cach = self.original(limit), self.cached(cache, limit)
            self.assertEqual(len(orig), len(cach))
            self.assertEqual(orig.program_ids, cach.program_ids)
            self.assertEqual(orig.chunks_per_record, cach.chunks_per_record)
            self.assertEqual(orig.cum_chunk_counts, cach.cum_chunk_counts)
            self.assertEqual(orig.total_oversized_events, cach.total_oversized_events)
            for i in range(len(orig)):
                a, b = orig[i], cach[i]
                for f in ID_FIELDS:
                    self.assertEqual(getattr(a, f), getattr(b, f), f"index {i} field {f} limit {limit}")
                if limit is not None:
                    self.assertLessEqual(len(b.source_ids), limit)
                self.assertEqual(b.decoder_input_ids[1:], b.target_ids[:-1])  # one-token shift
            with self.assertRaises(IndexError):
                cach[len(cach)]
            with self.assertRaises(IndexError):
                cach[-1]

    def test_collated_batches_identical(self):
        cache = self.build()
        orig, cach = self.original(5), self.cached(cache, 5)
        n = len(orig)
        for start in range(0, n, 3):
            idx = range(start, min(n, start + 3))
            a = execution_trace_collate_fn([orig[i] for i in idx], 0, 0)
            b = execution_trace_collate_fn([cach[i] for i in idx], 0, 0)
            self.assertEqual(a.program_ids, b.program_ids)
            for f in BATCH_TENSORS:
                ta, tb = getattr(a, f), getattr(b, f)
                self.assertEqual(ta.dtype, tb.dtype, f)
                self.assertEqual(ta.shape, tb.shape, f)
                self.assertTrue(torch.equal(ta, tb), f)

    def test_dataloader_with_shuffle(self):
        cache = self.build()
        loader = create_dataloader(self.cached(cache), batch_size=2, shuffle=True)
        seen = sum(batch.src_ids.size(0) for batch in loader)
        self.assertEqual(seen, len(self.original()))


class TestFailClosed(CacheFixture):
    def assertStale(self, cache, **kw):
        with self.assertRaises(CacheMismatchError):
            self.cached(cache, **kw)

    def test_refuses_overwrite_and_truncated_build(self):
        cache = self.build()
        with self.assertRaises(CacheError):
            build_chunk_cache(self.original(), cache)
        with self.assertRaises(CacheError):
            build_chunk_cache(self.original(max_source_length=5), self.tmp / "other")

    def test_split_file_change(self):
        cache = self.build()
        lines = self.jsonl.read_text(encoding="utf-8").splitlines()
        self.jsonl.write_text("\n".join(lines[:-1]) + "\n", encoding="utf-8")
        self.assertStale(cache)

    def test_crlf_only_change_is_not_a_mismatch(self):
        cache = self.build()
        self.jsonl.write_bytes(self.jsonl.read_bytes().replace(b"\n", b"\r\n"))
        self.assertEqual(len(self.cached(cache)), len(self.original()))

    def test_java_source_change(self):
        cache = self.build()
        (self.java / "p3.java").write_text("class Main { int x ; }", encoding="utf-8")
        self.assertStale(cache)

    def test_vocabulary_mapping_change_same_size(self):
        cache = self.build()
        i2t = list(self.tgt_vocab.id_to_token)
        i2t[-1], i2t[-2] = i2t[-2], i2t[-1]               # same size, different mapping
        swapped = Vocabulary({t: i for i, t in enumerate(i2t)}, i2t, vocab_type="target")
        with self.assertRaises(CacheMismatchError):
            CachedExecutionTraceDataset(cache, self.jsonl, self.java, self.src_vocab, swapped, max_chunk_size=MAX_CHUNK)

    def test_parameter_change(self):
        cache = self.build()
        self.assertStale(cache, max_chunk_size=32)

    def test_code_change(self):
        code_root = self.tmp / "code"
        code_root.mkdir()
        for name in CODE_FILES:
            shutil.copy(REPO_ROOT / name, code_root / name)
        cache = self.build(code_root=code_root)
        self.cached(cache, code_root=code_root)               # unchanged code loads
        with open(code_root / "chunk_trace.py", "a", encoding="utf-8") as f:
            f.write("\n# edit\n")
        self.assertStale(cache, code_root=code_root)

    def test_corrupted_array(self):
        cache = self.build()
        path = cache / "target_ids.npy"
        data = bytearray(path.read_bytes())
        data[-1] ^= 0xFF
        path.write_bytes(bytes(data))
        self.assertStale(cache)


class TestOverflow(CacheFixture):
    def test_to_uint16_bounds(self):
        self.assertEqual(_to_uint16([0, 65535], "x").tolist(), [0, 65535])
        with self.assertRaises(CacheOverflowError):
            _to_uint16([65536], "x")
        with self.assertRaises(CacheOverflowError):
            _to_uint16([-1], "x")

    def test_build_refuses_ids_above_uint16(self):
        filler = [f"tok{i}" for i in range(65600)]
        big = make_vocab(filler + ["VW", "CONTEXT", "Main", "X"], "target")   # these ids are > 65535
        with self.assertRaises(CacheOverflowError):
            build_chunk_cache(self.original(vocab=big), self.tmp / "big")
        self.assertFalse((self.tmp / "big").exists())


if __name__ == "__main__":
    unittest.main()

"""
Unit and regression tests for Event-Preserving Chunking (Module 4, Part 3).
"""
import unittest
from tokenize_and_analyze import tokenize_runtime
from chunk_trace import (
    chunk_trace_tokens,
    chunk_runtime_trace,
    parse_events_from_tokens,
    ChunkingResult,
    MalformedEventStreamError,
    OversizedEventError
)


class TestChunkTrace(unittest.TestCase):

    def setUp(self):
        # Sample reusable events
        self.ev1 = {"type": "TS", "step": 1, "attributes": {"CALLER": "main"}}
        self.ev2 = {"type": "MC", "step": 2, "attributes": {"TARGET": "Foo#bar:1"}}
        self.ev3 = {"type": "VW", "step": 3, "attributes": {"CTX": "Main"}}
        self.ev4 = {"type": "ME", "step": 4, "attributes": {"RET": "void"}}

    def test_1_empty_trace_handling(self):
        """Empty trace produces 0 chunks and 0 events."""
        res = chunk_trace_tokens([])
        self.assertEqual(res.chunks, [])
        self.assertEqual(res.chunk_count, 0)
        self.assertEqual(res.total_events, 0)
        self.assertEqual(res.oversized_events, [])

        # Via dict
        res_dict = chunk_runtime_trace({"events": []})
        self.assertEqual(res_dict.chunks, [])
        self.assertEqual(res_dict.chunk_count, 0)

    def test_2_single_event_fits_exactly(self):
        """One event whose token length equals max_chunk_size fits exactly in 1 chunk."""
        tokens = tokenize_runtime({"events": [self.ev1]})
        ev_len = len(tokens)

        res = chunk_trace_tokens(tokens, max_chunk_size=ev_len)
        self.assertEqual(res.chunk_count, 1)
        self.assertEqual(len(res.chunks[0]), ev_len)
        self.assertEqual(res.chunks[0], tokens)

    def test_3_multiple_events_packed_into_one_chunk(self):
        """Multiple small events pack into a single chunk if sum of lengths <= max_chunk_size."""
        trace = {"events": [self.ev1, self.ev2, self.ev3]}
        tokens = tokenize_runtime(trace)
        total_len = len(tokens)

        res = chunk_trace_tokens(tokens, max_chunk_size=total_len + 10)
        self.assertEqual(res.chunk_count, 1)
        self.assertEqual(res.chunks[0], tokens)
        self.assertEqual(res.total_events, 3)

    def test_4_event_forces_new_chunk(self):
        """An event that does not fit in current chunk begins a new chunk without splitting."""
        t1 = tokenize_runtime({"events": [self.ev1]})
        t2 = tokenize_runtime({"events": [self.ev2]})
        tokens = tokenize_runtime({"events": [self.ev1, self.ev2]})

        len1 = len(t1)
        len2 = len(t2)
        # Limit fits ev1, but not ev1 + ev2
        max_chunk = len1 + len2 - 1

        res = chunk_trace_tokens(tokens, max_chunk_size=max_chunk)
        self.assertEqual(res.chunk_count, 2)
        self.assertEqual(res.chunks[0], t1)
        self.assertEqual(res.chunks[1], t2)
        self.assertEqual(res.total_events, 2)

    def test_5_event_exactly_at_boundary(self):
        """An event that reaches exactly max_chunk_size is packed, and the subsequent event starts chunk 2."""
        t1 = tokenize_runtime({"events": [self.ev1]})
        tokens = t1 + t1
        exact_limit = len(t1)

        res = chunk_trace_tokens(tokens, max_chunk_size=exact_limit)
        self.assertEqual(res.chunk_count, 2)
        self.assertEqual(res.chunks[0], t1)
        self.assertEqual(res.chunks[1], t1)
        self.assertEqual(res.total_events, 2)

    def test_6_single_event_larger_than_limit_separate_policy(self):
        """Oversized event under policy='separate' is moved to oversized_events without losing data."""
        tokens = tokenize_runtime({"events": [self.ev1, self.ev2, self.ev3]})
        t1 = tokenize_runtime({"events": [self.ev1]})
        t2 = tokenize_runtime({"events": [self.ev2]})
        t3 = tokenize_runtime({"events": [self.ev3]})

        # Make max_chunk_size smaller than ev2, but larger than ev1 and ev3
        len2 = len(t2)
        max_chunk = len2 - 1

        res = chunk_trace_tokens(tokens, max_chunk_size=max_chunk, oversized_policy="separate")
        # ev1 fits, ev2 is oversized, ev3 fits
        self.assertEqual(res.chunks, [t1, t3])
        self.assertEqual(res.oversized_events, [t2])
        self.assertEqual(res.total_events, 3)

    def test_6b_single_event_larger_than_limit_error_policy(self):
        """Oversized event under policy='error' raises OversizedEventError."""
        tokens = tokenize_runtime({"events": [self.ev1, self.ev2]})
        limit = len(tokens) - 5
        # Set limit smaller than ev1
        t1_len = len(tokenize_runtime({"events": [self.ev1]}))

        with self.assertRaises(OversizedEventError):
            chunk_trace_tokens(tokens, max_chunk_size=t1_len - 1, oversized_policy="error")

    def test_6c_single_event_larger_than_limit_keep_policy(self):
        """Oversized event under policy='keep' is emitted as its own standalone chunk."""
        t1 = tokenize_runtime({"events": [self.ev1]})
        t2 = tokenize_runtime({"events": [self.ev2]})
        tokens = t1 + t2
        len1 = len(t1)

        res = chunk_trace_tokens(tokens, max_chunk_size=len1 - 1, oversized_policy="keep")
        self.assertEqual(res.chunk_count, 2)
        self.assertEqual(res.chunks[0], t1)
        self.assertEqual(res.chunks[1], t2)

    def test_7_preservation_of_event_order(self):
        """Events across chunks appear in the exact order of the original trace."""
        events = [self.ev1, self.ev2, self.ev3, self.ev4]
        tokens = tokenize_runtime({"events": events})

        # Pack with small chunk size to produce multiple chunks
        res = chunk_trace_tokens(tokens, max_chunk_size=30)
        reconstructed_tokens = []
        for chk in res.chunks:
            reconstructed_tokens.extend(chk)

        self.assertEqual(reconstructed_tokens, tokens)

    def test_8_preservation_of_every_token_in_every_event(self):
        """No token within any event is dropped or modified."""
        trace = {
            "events": [
                {"type": "TS", "step": 1, "attributes": {"MSG": "Hello, world!"}},
                {"type": "MC", "step": 2, "attributes": {"TARGET": "Main#main:1"}},
                {"type": "VW", "step": 3, "attributes": {"PATH": r"C\folder\file"}}
            ]
        }
        tokens = tokenize_runtime(trace)
        res = chunk_trace_tokens(tokens, max_chunk_size=40)

        # Concatenate chunks
        flattened = [tok for chk in res.chunks for tok in chk]
        self.assertEqual(flattened, tokens)

    def test_9_escaped_literal_markers_inside_attributes(self):
        """Escaped <EVENT_START> and <EVENT_END> in attribute values are not treated as boundaries."""
        trace = {
            "events": [
                {"type": "TS", "step": 1, "attributes": {"NOTE": "<EVENT_START>"}},
                {"type": "MC", "step": 2, "attributes": {"NOTE": "<EVENT_END>"}}
            ]
        }
        tokens = tokenize_runtime(trace)
        # Parse events
        evs, malformed = parse_events_from_tokens(tokens)
        self.assertEqual(len(evs), 2)
        self.assertEqual(malformed, [])

        # Ensure escaped tokens remain intact inside their respective events
        self.assertIn("\\", evs[0])
        self.assertIn("<EVENT_START>", evs[0][1:-1])  # inside, escaped
        self.assertIn("\\", evs[1])
        self.assertIn("<EVENT_END>", evs[1][1:-1])

        # Chunking preserves them exactly
        res = chunk_trace_tokens(tokens, max_chunk_size=50)
        self.assertEqual(res.chunks[0], tokens)

    def test_10_malformed_incomplete_event_structures_strict(self):
        """Malformed token stream raises MalformedEventStreamError in strict mode."""
        # Unclosed event
        unclosed = ["<EVENT_START>", "TYPE", "TS", "STEP", "1"]
        with self.assertRaises(MalformedEventStreamError):
            chunk_trace_tokens(unclosed, strict=True)

        # Stray <EVENT_END>
        stray = ["TYPE", "TS", "<EVENT_END>"]
        with self.assertRaises(MalformedEventStreamError):
            chunk_trace_tokens(stray, strict=True)

        # Nested <EVENT_START>
        nested = ["<EVENT_START>", "TYPE", "TS", "<EVENT_START>", "<EVENT_END>"]
        with self.assertRaises(MalformedEventStreamError):
            chunk_trace_tokens(nested, strict=True)

    def test_10b_malformed_lenient_mode(self):
        """Malformed token stream in strict=False collects malformed tokens without crash."""
        tokens = ["ORPHAN_TOK", "<EVENT_START>", "TYPE", "TS", "<EVENT_END>", "TRAILING"]
        evs, malformed = parse_events_from_tokens(tokens, strict=False)
        self.assertEqual(len(evs), 1)
        self.assertIn("ORPHAN_TOK", malformed)
        self.assertIn("TRAILING", malformed)

    def test_11_deterministic_chunk_output(self):
        """Chunking is completely deterministic across repeated invocations."""
        trace = {"events": [self.ev1, self.ev2, self.ev3, self.ev4]}
        tokens = tokenize_runtime(trace)

        res1 = chunk_trace_tokens(tokens, max_chunk_size=35)
        res2 = chunk_trace_tokens(tokens, max_chunk_size=35)
        self.assertEqual(res1.chunks, res2.chunks)
        self.assertEqual(res1.chunk_count, res2.chunk_count)

    def test_12_conservation_of_all_valid_events(self):
        """All valid events appear exactly once across chunks, with 0 events duplicated or lost."""
        trace = {"events": [self.ev1, self.ev2, self.ev3, self.ev4]}
        tokens = tokenize_runtime(trace)
        evs_original, _ = parse_events_from_tokens(tokens)

        # Chunk with small limit
        res = chunk_trace_tokens(tokens, max_chunk_size=25)

        # Re-parse events from each chunk
        evs_reconstructed = []
        for chk in res.chunks:
            chk_evs, _ = parse_events_from_tokens(chk)
            evs_reconstructed.extend(chk_evs)

        self.assertEqual(len(evs_reconstructed), len(evs_original))
        for orig, recon in zip(evs_original, evs_reconstructed):
            self.assertEqual(orig, recon)

    def test_13_minimum_chunk_size_validation(self):
        """max_chunk_size < 6 raises ValueError (minimum event length is 6 tokens)."""
        with self.assertRaises(ValueError):
            chunk_trace_tokens(["<EVENT_START>"], max_chunk_size=5)

    def test_14_invalid_policy_validation(self):
        """Invalid oversized_policy raises ValueError."""
        with self.assertRaises(ValueError):
            chunk_trace_tokens(["<EVENT_START>"], oversized_policy="invalid_policy")


if __name__ == '__main__':
    unittest.main()

"""Tests for the Design A error-analysis helpers (TE detection, truncation, composition, reproduction checks)."""

import sys
import unittest
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))

import error_analysis_first_chunk as ea  # noqa: E402
from target_format import tokenize_runtime_nostep  # noqa: E402


def trace(*types, te_value=None):
    events = []
    for i, t in enumerate(types, start=1):
        attrs = {"CONTEXT": "Main#main:1"} if t != "TE" else {}
        if te_value is not None and t == "VW":
            attrs["X"] = te_value
        events.append({"step": i, "type": t, "attributes": attrs})
    return tokenize_runtime_nostep({"events": events})


class TestTE(unittest.TestCase):
    def test_te_final_in_reference(self):
        info = ea.te_info(trace("TS", "MC", "VW", "TE"))
        self.assertEqual(info, {"first_te_event_index": 3, "events": 4, "te_is_final_event": True})

    def test_no_te_and_te_not_final(self):
        self.assertIsNone(ea.te_info(trace("TS", "VW"))["first_te_event_index"])
        info = ea.te_info(trace("TS", "TE", "VW", "VW"))
        self.assertEqual((info["first_te_event_index"], info["te_is_final_event"]), (1, False))

    def test_te_text_inside_attribute_is_not_an_event(self):
        toks = trace("TS", "VW", "VW", te_value="TE")         # value token "TE" inside an attribute
        self.assertIn("TE", toks)
        self.assertIsNone(ea.te_info(toks)["first_te_event_index"])
        self.assertEqual(ea.truncate_after_first_te(toks), toks)

    def test_truncation(self):
        full = trace("TS", "MC", "TE", "VW", "VW")
        cut = ea.truncate_after_first_te(full)
        self.assertEqual(cut, trace("TS", "MC", "TE"))
        self.assertEqual(ea.truncate_after_first_te(trace("TS", "VW")), trace("TS", "VW"))
        malformed = full[:-3]                                  # unclosed last event; TE still intact earlier
        self.assertEqual(ea.truncate_after_first_te(malformed), trace("TS", "MC", "TE"))


class TestCompositionAndReproduction(unittest.TestCase):
    def test_type_counts(self):
        self.assertEqual(ea.type_counts(trace("TS", "VW", "VW", "TE")), Counter({"VW": 2, "TS": 1, "TE": 1}))

    def test_compare_metric(self):
        saved = {"eos_reached": True, "pred_length": 10, "token_edit_similarity": 0.5, "attr_value_match_rate": None}
        self.assertTrue(ea.compare_metric(saved, dict(saved), "token_edit_similarity"))
        self.assertTrue(ea.compare_metric(saved, dict(saved, token_edit_similarity=0.5 + 1e-12), "token_edit_similarity"))
        self.assertFalse(ea.compare_metric(saved, dict(saved, token_edit_similarity=0.5 + 1e-6), "token_edit_similarity"))
        self.assertFalse(ea.compare_metric(saved, dict(saved, pred_length=11), "pred_length"))
        self.assertFalse(ea.compare_metric(saved, dict(saved, eos_reached=False), "eos_reached"))
        self.assertTrue(ea.compare_metric(saved, dict(saved), "attr_value_match_rate"))
        self.assertFalse(ea.compare_metric(saved, dict(saved, attr_value_match_rate=0.1), "attr_value_match_rate"))


if __name__ == "__main__":
    unittest.main()

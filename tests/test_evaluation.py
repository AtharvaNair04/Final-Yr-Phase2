"""Unit tests for evaluation.py on small synthetic examples with hand-computed expected values."""

import unittest

import torch

from evaluation import (
    aggregate_tf,
    bracket_validity,
    check_compatibility,
    edit_similarity,
    event_metrics,
    generation_metrics,
    grammar_validity,
    greedy_generate,
    levenshtein,
    macro_average,
    parse_event,
    parse_trace,
    positional_accuracy,
    spearman,
    step_value_positions,
    teacher_forced_counts,
)
from model import ExecutionSemanticTransformer, ModelConfig
from tokenize_and_analyze import tokenize_runtime


def ev(t, step, *attrs):
    toks = ["<EVENT_START>", "TYPE", t, "STEP", str(step)]
    for k, *v in attrs:
        toks += ["<ATTR_START>", k, *v, "<ATTR_END>"]
    return toks + ["<EVENT_END>"]


class TestEditMetrics(unittest.TestCase):
    def test_levenshtein_known(self):
        self.assertEqual(levenshtein("kitten", "sitting"), 3)
        self.assertEqual(levenshtein([], [1, 2]), 2)
        self.assertEqual(levenshtein([1, 2, 3], [1, 2, 3]), 0)

    def test_edit_similarity(self):
        self.assertAlmostEqual(edit_similarity("kitten", "sitting"), 1 - 3 / 7)
        self.assertEqual(edit_similarity([], []), 1.0)
        self.assertEqual(edit_similarity([1], []), 0.0)

    def test_positional_accuracy_penalizes_length(self):
        self.assertEqual(positional_accuracy([1, 2, 3, 4], [1, 2]), 0.5)
        self.assertEqual(positional_accuracy([1, 2], [1, 2, 9, 9]), 0.5)
        self.assertEqual(positional_accuracy([], []), 1.0)


class TestParsing(unittest.TestCase):
    def test_parse_real_tokenizer_output(self):
        trace = {"events": [
            {"step": 1, "type": "MC", "attributes": {"CALLER": "SYSTEM", "TARGET": "Main#main:1"}},
            {"step": 2, "type": "VW", "attributes": {"NOTE": "", "STEP": "TYPE x"}},
        ]}
        events, outside = parse_trace(tokenize_runtime(trace))
        self.assertEqual(outside, 0)
        self.assertEqual([e.type for e in events], ["MC", "VW"])
        self.assertEqual([e.step for e in events], ["1", "2"])
        self.assertTrue(all(e.well_formed for e in events))
        self.assertEqual(events[0].attributes, [("CALLER", ("SYSTEM",)), ("TARGET", ("Main", "#", "main", ":", "1"))])
        # Escaped reserved key and value tokens are unescaped; empty value preserved.
        self.assertEqual(events[1].attributes, [("NOTE", ("<EMPTY_VAL>",)), ("STEP", ("TYPE", "x"))])

    def test_malformed_events_flagged(self):
        self.assertFalse(parse_event(["<EVENT_START>", "STEP", "1", "<EVENT_END>"]).well_formed)  # no TYPE
        self.assertFalse(parse_event(["<EVENT_START>", "TYPE", "MC", "<ATTR_START>", "K", "v", "<EVENT_END>"]).well_formed)
        self.assertFalse(parse_event(["<EVENT_START>", "TYPE", "MC", "<ATTR_START>", "K", "<ATTR_END>", "<EVENT_END>"]).well_formed)
        self.assertTrue(parse_event(["<EVENT_START>", "TYPE", "MC", "<EVENT_END>"]).well_formed)

    def test_validity_levels_differ(self):
        # Bracket-valid but grammar-invalid: no TYPE inside the event.
        toks = ["<EVENT_START>", "<ATTR_START>", "K", "v", "<ATTR_END>", "<EVENT_END>"]
        self.assertTrue(bracket_validity(toks))
        self.assertFalse(grammar_validity(toks))
        self.assertTrue(grammar_validity(ev("MC", 1, ("K", "v"))))
        self.assertFalse(bracket_validity(["<ATTR_START>", "<ATTR_START>"]))
        self.assertFalse(grammar_validity([]))
        self.assertFalse(grammar_validity(ev("MC", 1) + ["stray"]))  # token outside events


class TestEventMetrics(unittest.TestCase):
    def test_identical(self):
        ref = ev("MC", 1, ("A", "x")) + ev("VW", 2, ("B", "y"))
        m = event_metrics(ref, ref)
        self.assertEqual(m["type_positional_accuracy"], 1.0)
        self.assertEqual(m["event_exact_rate"], 1.0)
        self.assertEqual(m["attr_value_match_rate"], 1.0)
        self.assertEqual(m["type_bag_f1"], 1.0)

    def test_known_partial_case(self):
        ref = ev("MC", 1, ("A", "x")) + ev("VW", 2, ("B", "y")) + ev("ME", 3, ("C", "z"))
        pred = ev("MC", 5, ("A", "x")) + ev("FW", 6, ("B", "q"))
        m = event_metrics(ref, pred)
        self.assertEqual(m["ref_event_count"], 3)
        self.assertEqual(m["pred_event_count"], 2)
        self.assertAlmostEqual(m["type_positional_accuracy"], 1 / 3)
        self.assertAlmostEqual(m["type_sequence_edit_similarity"], 1 - 2 / 3)  # sub VW->FW, delete ME
        self.assertAlmostEqual(m["type_bag_precision"], 1 / 2)
        self.assertAlmostEqual(m["type_bag_recall"], 1 / 3)
        self.assertAlmostEqual(m["type_bag_f1"], 0.4)
        self.assertEqual(m["attr_key_match_rate"], 1.0)
        self.assertEqual(m["attr_value_match_rate"], 0.5)
        self.assertEqual(m["step_match_rate"], 0.0)
        self.assertEqual(m["event_exact_rate"], 0.0)
        self.assertAlmostEqual(m["event_exact_nostep_rate"], 1 / 3)

    def test_generation_metrics_bundle(self):
        m = generation_metrics([4, 5, 6], [4, 5], ev("MC", 1), ev("MC", 1), eos_reached=True)
        self.assertFalse(m["exact_match"])
        self.assertAlmostEqual(m["token_positional_accuracy"], 2 / 3)
        self.assertAlmostEqual(m["length_ratio"], 2 / 3)
        self.assertTrue(m["grammar_valid"])


class TestTeacherForced(unittest.TestCase):
    # ids: EOS=3, UNK=1, STEP=10, ESC=11, structural = {4..11}
    STRUCT = set(range(4, 12))

    def test_step_value_positions_respects_escape(self):
        self.assertEqual(step_value_positions([4, 9, 50, 10, 77, 11, 10, 60], 10, 11), [4])

    def test_counts_and_rates(self):
        target = [4, 10, 77, 1, 200, 3]   # EVENT_START STEP <77> UNK content EOS
        pred = [4, 10, 99, 1, 200, 3]     # wrong only on the step value
        nll = [0.1, 0.2, 2.0, 0.5, 0.4, 0.3]
        c = teacher_forced_counts(target, pred, nll, eos_id=3, unk_id=1, structural_ids=self.STRUCT, step_id=10, escape_id=11)
        r = aggregate_tf(c)
        self.assertAlmostEqual(r["token_acc_incl_eos"], 5 / 6)
        self.assertAlmostEqual(r["token_acc_excl_eos"], 4 / 5)
        self.assertAlmostEqual(r["token_acc_excl_eos_and_unk_targets"], 3 / 4)
        self.assertAlmostEqual(r["unk_target_rate"], 1 / 5)
        self.assertEqual(r["step_value_acc_excl_unk"], 0.0)
        self.assertAlmostEqual(r["content_token_acc_excl_unk"], 1.0)   # only token 200; the UNK target is excluded
        self.assertAlmostEqual(r["loss_incl_eos"], sum(nll) / 6)
        self.assertAlmostEqual(r["loss_excl_eos"], sum(nll[:-1]) / 5)

    def test_unk_step_target_not_counted_as_correct(self):
        target = [10, 1, 10, 50, 3]       # STEP <UNK> STEP <50> EOS
        pred = [10, 1, 10, 51, 3]         # UNK "predicted correctly", 50 wrong
        c = teacher_forced_counts(target, pred, [0.0] * 5, eos_id=3, unk_id=1, structural_ids=self.STRUCT, step_id=10, escape_id=11)
        r = aggregate_tf(c)
        self.assertEqual(r["step_value_acc_excl_unk"], 0.0)
        self.assertEqual(r["step_value_unk_count"], 1)

    def test_macro_average(self):
        m = macro_average([{"a": 1.0, "b": True}, {"a": 0.0, "b": False}, {"a": None, "b": True}])
        self.assertEqual(m["a"], 0.5)
        self.assertAlmostEqual(m["b_rate"], 2 / 3)


class TestModelHelpers(unittest.TestCase):
    def setUp(self):
        torch.manual_seed(0)
        self.cfg = ModelConfig(src_vocab_size=20, tgt_vocab_size=30, d_model=16, nhead=2,
                               num_encoder_layers=1, num_decoder_layers=1, dim_feedforward=32,
                               max_src_length=16, max_tgt_length=16)
        self.model = ExecutionSemanticTransformer(self.cfg)

    def test_greedy_respects_limit_and_matches_single(self):
        src = torch.tensor([[5, 6, 7, 0], [8, 9, 0, 0]])
        mask = src.eq(0)
        batched = greedy_generate(self.model, src, mask, bos_id=2, eos_id=3, max_new_tokens=6)
        self.assertEqual(len(batched), 2)
        for ids, eos in batched:
            self.assertLessEqual(len(ids), 6)
            self.assertNotIn(3, ids)
        # Row 0 decoded alone must give the same result as in the batch.
        single = greedy_generate(self.model, src[:1, :3], mask[:1, :3], bos_id=2, eos_id=3, max_new_tokens=6)
        self.assertEqual(single[0], batched[0])

    def test_compatibility(self):
        class V:
            def __init__(self, n): self.n = n; self.pad_id = 0
            def __len__(self): return self.n
        sd = self.model.state_dict()
        ok = check_compatibility(sd, self.cfg.to_dict(), V(20), V(30))
        self.assertTrue(ok["hard_checks_pass"])
        bad = check_compatibility(sd, self.cfg.to_dict(), V(21), V(30))
        self.assertFalse(bad["hard_checks_pass"])

    def test_spearman(self):
        self.assertAlmostEqual(spearman([1, 2, 3], [10, 20, 30]), 1.0)
        self.assertAlmostEqual(spearman([1, 2, 3], [3, 2, 1]), -1.0)
        self.assertIsNone(spearman([1, 1, 1], [1, 2, 3]))


if __name__ == "__main__":
    unittest.main()

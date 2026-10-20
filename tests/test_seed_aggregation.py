"""Tests for the Design A run-name/output parameters and the three-seed aggregation rules."""

import copy
import json
import sys
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))

import evaluate_first_chunk as efc  # noqa: E402
import train_first_chunk as tfc  # noqa: E402
from seed_aggregation import (  # noqa: E402
    CHECKS, aggregate, check_retrieval_consistent, mean_sd, seed_average, seed_averaged_tests,
)

THRESHOLDS = efc.ACCEPTANCE


class TestParameters(unittest.TestCase):
    def test_training_defaults_unchanged(self):
        args = tfc.build_parser().parse_args([])
        self.assertEqual((args.epochs, args.seed, args.run_name), (40, 42, "design_a_first_chunk_v1"))
        self.assertEqual(tfc.resolve_run_dir(args.run_name), tfc.RUN_DIR)
        self.assertEqual(tfc.RUN_DIR, ROOT / "checkpoints" / "design_a_first_chunk_v1")

    def test_training_custom_run_name(self):
        args = tfc.build_parser().parse_args(["--seed", "43", "--run-name", "design_a_first_chunk_seed43"])
        self.assertEqual(args.seed, 43)
        self.assertEqual(tfc.resolve_run_dir(args.run_name), ROOT / "checkpoints" / "design_a_first_chunk_seed43")
        for bad in ("../escape", "a/b", "a\\b", "..", "", "name with space"):
            with self.assertRaises(ValueError):
                tfc.resolve_run_dir(bad)

    def test_evaluation_defaults_unchanged(self):
        args = efc.build_parser().parse_args([])
        self.assertEqual(args.run_dir, tfc.RUN_DIR)
        self.assertEqual(args.out_dir, ROOT / "results" / "design_a_first_chunk_v1")
        args = efc.build_parser().parse_args(["--run-dir", "checkpoints/x", "--out-dir", "results/y"])
        self.assertEqual((args.run_dir, args.out_dir), (Path("checkpoints/x"), Path("results/y")))


def program(tf_ok, tf_mis, correct, mismatched, retrieval, common, src_from="pB", retr_from="t1"):
    sims = lambda v: {"token_edit_similarity": v, "type_sequence_edit_similarity": v}  # noqa: E731
    return {"tf_loss_correct": tf_ok, "tf_loss_mismatched": tf_mis, "correct": sims(correct),
            "mismatched": sims(mismatched), "retrieval": sims(retrieval), "common_window": sims(common),
            "retrieved_from": retr_from, "mismatched_source_from": src_from}


def metrics(seed, acceptance, distinct=56):
    summary = {block: {k: 0.5 for k in keys} for block, keys in
               {"model": ("token_edit_similarity", "type_sequence_edit_similarity", "type_bag_f1", "attr_value_match_rate",
                          "event_exact_rate", "grammar_valid_rate", "eos_reached_rate", "exact_match_rate", "length_ratio"),
                "mismatched_source": ("token_edit_similarity", "type_sequence_edit_similarity"),
                "common_window_model": ("token_edit_similarity", "type_sequence_edit_similarity")}.items()}
    gap = {"type_sequence_edit_similarity": {"mean_difference": 0.1}}
    return {"seed": seed, "experiment": f"run{seed}", "best_epoch": 19, "best_val_loss": 0.48, "acceptance": acceptance,
            "distinct_outputs": distinct, "source_conditioning_tf": {"win_rate": 0.75, "mean_loss_increase_with_mismatched_source": 0.02},
            "paired_tests": {"correct_vs_mismatched_source": gap, "model_vs_retrieval": gap},
            "capacity_diagnostic": {"best": {"token_acc_excl_eos": 0.93}, "final": {"token_acc_excl_eos": 0.96}},
            "summary": summary}


class TestAggregation(unittest.TestCase):
    def setUp(self):
        self.pp = [
            {"pA": program(0.4, 0.5, 0.6, 0.4, 0.5, 0.6), "pB": program(0.5, 0.6, 0.5, 0.5, 0.6, 0.5, src_from="pA")},
            {"pA": program(0.2, 0.5, 0.8, 0.4, 0.5, 0.8), "pB": program(0.7, 0.6, 0.3, 0.5, 0.6, 0.3, src_from="pA")},
            {"pA": program(0.3, 0.5, 0.7, 0.4, 0.5, 0.7), "pB": program(0.6, 0.6, 0.4, 0.5, 0.6, 0.4, src_from="pA")},
        ]

    def test_mean_sd(self):
        self.assertEqual(mean_sd([1.0, 2.0, 3.0]), (2.0, 1.0))
        self.assertEqual(mean_sd([5.0]), (5.0, None))
        self.assertEqual(mean_sd([]), (None, None))

    def test_seed_average_values(self):
        avg = seed_average(self.pp)
        self.assertAlmostEqual(avg["pA"]["correct"]["type_sequence_edit_similarity"], 0.7)
        self.assertAlmostEqual(avg["pB"]["tf_loss_correct"], 0.6)
        self.assertAlmostEqual(avg["pB"]["tf_loss_mismatched"], 0.6)        # tie -> not a win
        self.assertEqual(avg["pA"]["retrieval"]["token_edit_similarity"], 0.5)

    def test_inconsistent_inputs_rejected(self):
        bad = copy.deepcopy(self.pp)
        bad[2]["pA"]["retrieved_from"] = "t9"
        with self.assertRaises(ValueError):
            check_retrieval_consistent(bad)
        bad = copy.deepcopy(self.pp)
        del bad[1]["pB"]
        with self.assertRaises(ValueError):
            seed_average(bad)

    def test_seed_averaged_tests(self):
        avg = seed_average(self.pp)
        base = {"random_init_reference": {p: {"token_edit_similarity": 0.0, "type_sequence_edit_similarity": 0.0} for p in avg},
                "exp1_best": {p: {"token_edit_similarity": 0.9, "type_sequence_edit_similarity": 0.9} for p in avg}}
        t = seed_averaged_tests(avg, base, THRESHOLDS)
        self.assertEqual(t["4a_source_tf"]["wins"], 1)
        self.assertEqual(t["4b_source_gen"]["type_sequence_edit_similarity"]["wins"], 1)
        self.assertEqual(t["4b_source_gen"]["type_sequence_edit_similarity"]["losses"], 1)
        self.assertFalse(t["4b_source_gen"]["pass"])
        self.assertTrue(t["3_vs_random_init_reference"]["pass"])
        self.assertFalse(t["3_vs_exp1_best"]["pass"])

    def test_robust_rule(self):
        all_true = {c: True for c in CHECKS}
        two = [dict(all_true), dict(all_true), dict(all_true, **{"5_beats_retrieval": False})]
        two[0]["5_beats_retrieval"] = False                                   # retrieval passes in 1 of 3 seeds
        ms = [metrics(42 + i, two[i]) for i in range(3)]
        base = {name: {p: {"token_edit_similarity": 0.0, "type_sequence_edit_similarity": 0.0} for p in ("pA", "pB")}
                for name in ("random_init_reference", "exp1_best")}
        agg = aggregate(ms, self.pp, base, THRESHOLDS)
        self.assertEqual(agg["seeds"], [42, 43, 44])
        self.assertFalse(agg["robust"]["5_beats_retrieval"]["robust"])        # 1/3 seeds
        self.assertEqual(agg["robust"]["5_beats_retrieval"]["passes_in_seeds"], "1/3")
        self.assertFalse(agg["robust"]["4b_source_gen_sign_test"]["robust"])  # 3/3 seeds but seed-averaged fails
        self.assertTrue(agg["robust"]["3_beats_random_init_common_window"]["robust"])
        self.assertTrue(agg["robust"]["2_capacity_diagnostic_train_tf_acc_ge_0.95"]["robust"])  # per-seed only
        self.assertAlmostEqual(agg["across_seeds_mean_sd"]["best_val_loss"]["sd"], 0.0)

    @unittest.skipUnless((ROOT / "results" / "design_a_first_chunk_v1" / "metrics.json").is_file(), "seed-42 results absent")
    def test_real_seed42_duplicated_is_self_consistent(self):
        d = ROOT / "results" / "design_a_first_chunk_v1"
        m = json.loads((d / "metrics.json").read_text())
        pp = json.loads((d / "per_program.json").read_text())
        base = {}
        for name in ("random_init_reference", "exp1_best"):
            rows = json.loads((ROOT / "results" / "eval_validation_v2_nostep" / f"{name}.json").read_text())["gen_common_per_program"]
            base[name] = {r["program_id"]: r for r in rows}
        agg = aggregate([m, m, m], [pp, pp, pp], base, THRESHOLDS)
        # Averaging identical seeds must reproduce the single-seed tests exactly.
        self.assertEqual(agg["seed_averaged_tests"]["4a_source_tf"]["wins"], m["source_conditioning_tf"]["correct_source_lower_loss"])
        single = m["paired_tests"]["model_vs_retrieval"]["type_sequence_edit_similarity"]
        multi = agg["seed_averaged_tests"]["5_vs_retrieval"]["type_sequence_edit_similarity"]
        self.assertEqual((multi["wins"], multi["losses"]), (single["wins"], single["losses"]))
        for check in CHECKS:
            self.assertEqual(agg["robust"][check]["passes_in_seeds"], f"{3 * bool(m['acceptance'][check])}/3")


if __name__ == "__main__":
    unittest.main()

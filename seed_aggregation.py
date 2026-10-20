"""
Predeclared three-seed aggregation for Design A (no best-seed selection).

Inputs per seed: results/<run>/metrics.json and per_program.json written by scripts/evaluate_first_chunk.py.

Rules (fixed before seeds 43/44 were trained):
  - Every seed is reported; summary metrics are given as mean and sample standard deviation over seeds.
  - Primary analysis: each program's metrics are averaged over seeds, then the same paired tests as the
    single-seed protocol are run across the 56 programs.
  - A check is "robust" only if it passes the seed-averaged test AND passes in at least 2 of the seeds.
  - The retrieval baseline does not depend on the seed; it must be identical in every input (checked).
"""

import math
from typing import Any, Dict, List, Optional, Sequence, Tuple

from first_chunk import binomial_two_sided_p, bootstrap_mean_ci, sign_test, wilson_interval

SPACES = ("correct", "mismatched", "common_window")
SIM_KEYS = ("token_edit_similarity", "type_sequence_edit_similarity")
SUMMARY_KEYS = {
    "model": ("token_edit_similarity", "type_sequence_edit_similarity", "type_bag_f1", "attr_value_match_rate",
              "event_exact_rate", "grammar_valid_rate", "eos_reached_rate", "exact_match_rate", "length_ratio"),
    "mismatched_source": ("token_edit_similarity", "type_sequence_edit_similarity"),
    "common_window_model": ("token_edit_similarity", "type_sequence_edit_similarity"),
}
CHECKS = ("1_integrity", "2_capacity_diagnostic_train_tf_acc_ge_0.95", "3_beats_random_init_common_window",
          "3_beats_exp1_common_window", "4a_source_tf_wilson_lower_gt_0.5", "4b_source_gen_sign_test",
          "4c_distinct_outputs_ge_28", "5_beats_retrieval")


def mean_sd(values: Sequence[float]) -> Tuple[Optional[float], Optional[float]]:
    """Mean and sample standard deviation (None when fewer than two values)."""
    vals = [v for v in values if v is not None]
    if not vals:
        return None, None
    m = sum(vals) / len(vals)
    if len(vals) < 2:
        return m, None
    return m, math.sqrt(sum((v - m) ** 2 for v in vals) / (len(vals) - 1))


def check_same_programs(per_program_list: List[Dict[str, Any]]) -> List[str]:
    pids = sorted(per_program_list[0])
    for pp in per_program_list[1:]:
        if sorted(pp) != pids:
            raise ValueError("Seeds were evaluated on different program sets")
    return pids


def check_retrieval_consistent(per_program_list: List[Dict[str, Any]]) -> None:
    first = per_program_list[0]
    for pp in per_program_list[1:]:
        for pid, row in first.items():
            if (pp[pid]["retrieved_from"] != row["retrieved_from"] or pp[pid]["retrieval"] != row["retrieval"]
                    or pp[pid]["mismatched_source_from"] != row["mismatched_source_from"]):
                raise ValueError(f"Seed-independent quantities differ between seeds for {pid}")


def seed_average(per_program_list: List[Dict[str, Any]]) -> Dict[str, Dict[str, Any]]:
    """Per program: seed-averaged similarity metrics per space, TF losses; retrieval copied (seed-independent)."""
    pids = check_same_programs(per_program_list)
    check_retrieval_consistent(per_program_list)
    out: Dict[str, Dict[str, Any]] = {}
    n = len(per_program_list)
    for pid in pids:
        row: Dict[str, Any] = {space: {k: sum(pp[pid][space][k] for pp in per_program_list) / n for k in SIM_KEYS}
                               for space in SPACES}
        row["retrieval"] = {k: per_program_list[0][pid]["retrieval"][k] for k in SIM_KEYS}
        row["tf_loss_correct"] = sum(pp[pid]["tf_loss_correct"] for pp in per_program_list) / n
        row["tf_loss_mismatched"] = sum(pp[pid]["tf_loss_mismatched"] for pp in per_program_list) / n
        out[pid] = row
    return out


# Seed-averaging introduces floating-point noise ((x + x + x) / 3 != x), which would turn exact ties into
# tiny wins or losses in the sign test; differences below this tolerance are treated as ties.
TIE_TOLERANCE = 1e-12


def _snap(d: float) -> float:
    return 0.0 if abs(d) < TIE_TOLERANCE else d


def _paired(a: Dict[str, float], b: Dict[str, float]) -> Dict[str, Any]:
    diffs = [_snap(a[p] - b[p]) for p in sorted(a)]
    mean, lo, hi = bootstrap_mean_ci(diffs)
    return {"n": len(diffs), "mean_difference": mean, "bootstrap_95ci": [lo, hi], **sign_test(diffs)}


def seed_averaged_tests(avg: Dict[str, Dict[str, Any]], baselines: Dict[str, Dict[str, Dict[str, float]]],
                        thresholds: Dict[str, float]) -> Dict[str, Any]:
    """Single-seed tests repeated on seed-averaged per-program values. baselines[name][pid][metric]."""
    pids = sorted(avg)
    tests: Dict[str, Any] = {}
    wins = sum(_snap(avg[p]["tf_loss_mismatched"] - avg[p]["tf_loss_correct"]) > 0 for p in pids)
    lo, hi = wilson_interval(wins, len(pids))
    tests["4a_source_tf"] = {"wins": wins, "n": len(pids), "wilson_95ci": [lo, hi],
                             "binomial_p_two_sided": binomial_two_sided_p(wins, len(pids)),
                             "pass": lo is not None and lo > thresholds["source_tf_win_wilson_lower_gt"]}
    for name, (sa, sb) in {"4b_source_gen": ("correct", "mismatched"), "5_vs_retrieval": ("correct", "retrieval")}.items():
        res = {k: _paired({p: avg[p][sa][k] for p in pids}, {p: avg[p][sb][k] for p in pids}) for k in SIM_KEYS}
        main = res["type_sequence_edit_similarity"]
        limit = thresholds["source_gen_sign_test_p_lt" if name.startswith("4b") else "retrieval_sign_test_p_lt"]
        res["pass"] = main["wins"] > main["losses"] and main["p_value"] < limit
        tests[name] = res
    for bname, rows in baselines.items():
        res = {k: _paired({p: avg[p]["common_window"][k] for p in pids}, {p: rows[p][k] for p in pids}) for k in SIM_KEYS}
        res["pass"] = all(res[k]["mean_difference"] > 0 for k in SIM_KEYS)   # same rule as the single-seed check 3
        tests[f"3_vs_{bname}"] = res
    return tests


def aggregate(metrics_list: List[Dict[str, Any]], per_program_list: List[Dict[str, Any]],
              baselines: Dict[str, Dict[str, Dict[str, float]]], thresholds: Dict[str, float]) -> Dict[str, Any]:
    if len(metrics_list) != len(per_program_list) or len(metrics_list) < 2:
        raise ValueError("Need matching metrics and per-program files for at least two seeds")
    seeds = [m.get("seed") for m in metrics_list]
    per_seed = [{"seed": m.get("seed"), "experiment": m.get("experiment"), "best_epoch": m["best_epoch"],
                 "best_val_loss": m["best_val_loss"], "acceptance": m["acceptance"],
                 "distinct_outputs": m["distinct_outputs"],
                 "source_tf_win_rate": m["source_conditioning_tf"]["win_rate"],
                 "source_tf_loss_increase": m["source_conditioning_tf"]["mean_loss_increase_with_mismatched_source"],
                 "source_gen_gap_type_seq": m["paired_tests"]["correct_vs_mismatched_source"]["type_sequence_edit_similarity"]["mean_difference"],
                 "retrieval_gap_type_seq": m["paired_tests"]["model_vs_retrieval"]["type_sequence_edit_similarity"]["mean_difference"],
                 "capacity_best": m["capacity_diagnostic"]["best"]["token_acc_excl_eos"],
                 "capacity_final": m["capacity_diagnostic"]["final"]["token_acc_excl_eos"],
                 **{f"{block}.{k}": m["summary"][block].get(k) for block, keys in SUMMARY_KEYS.items() for k in keys}}
                for m in metrics_list]
    numeric = [k for k in per_seed[0] if k not in ("seed", "experiment", "acceptance")
               and isinstance(per_seed[0][k], (int, float))]
    across = {k: dict(zip(("mean", "sd"), mean_sd([row[k] for row in per_seed]))) for k in numeric}

    avg = seed_average(per_program_list)
    tests = seed_averaged_tests(avg, baselines, thresholds)
    mean_distinct = across["distinct_outputs"]["mean"]
    seed_avg_pass = {
        "1_integrity": all(m["acceptance"]["1_integrity"] for m in metrics_list),
        "2_capacity_diagnostic_train_tf_acc_ge_0.95": None,     # diagnostic; no seed-averaged test
        "3_beats_random_init_common_window": tests["3_vs_random_init_reference"]["pass"],
        "3_beats_exp1_common_window": tests["3_vs_exp1_best"]["pass"],
        "4a_source_tf_wilson_lower_gt_0.5": tests["4a_source_tf"]["pass"],
        "4b_source_gen_sign_test": tests["4b_source_gen"]["pass"],
        "4c_distinct_outputs_ge_28": mean_distinct >= thresholds["distinct_outputs_min"],
        "5_beats_retrieval": tests["5_vs_retrieval"]["pass"],
    }
    robust = {}
    for check in CHECKS:
        passes = sum(bool(m["acceptance"][check]) for m in metrics_list)
        sa = seed_avg_pass[check]
        robust[check] = {"passes_in_seeds": f"{passes}/{len(metrics_list)}", "seed_averaged_pass": sa,
                         "robust": (passes >= 2) and (sa is None or sa)}
    return {"seeds": seeds, "rule": "robust = seed-averaged test passes AND passes in >= 2 seeds (no best-seed selection)",
            "per_seed": per_seed, "across_seeds_mean_sd": across, "seed_averaged_tests": tests, "robust": robust}

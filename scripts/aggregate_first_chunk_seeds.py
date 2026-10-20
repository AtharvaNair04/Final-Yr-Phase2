#!/usr/bin/env python3
"""
Three-seed aggregation of Design A validation results (seed_aggregation.py rules; no best-seed selection).

  python scripts/aggregate_first_chunk_seeds.py
  python scripts/aggregate_first_chunk_seeds.py --results results/a results/b results/c --out-dir results/x

Defaults: seeds 42 / 43 / 44 = results/design_a_first_chunk_v1, _seed43, _seed44.
Baselines for check 3 come from results/eval_validation_v2_nostep (validation only).
Writes --out-dir (default results/design_a_first_chunk_3seed_aggregate; refuses to overwrite).
"""

import argparse
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / "scripts"))

from seed_aggregation import CHECKS, aggregate  # noqa: E402
from evaluate_first_chunk import ACCEPTANCE  # noqa: E402

DEFAULT_RESULTS = [ROOT / "results" / name for name in
                   ("design_a_first_chunk_v1", "design_a_first_chunk_seed43", "design_a_first_chunk_seed44")]
DEFAULT_OUT = ROOT / "results" / "design_a_first_chunk_3seed_aggregate"
BASELINE_DIR = ROOT / "results" / "eval_validation_v2_nostep"


def build_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument("--results", type=Path, nargs="+", default=DEFAULT_RESULTS)
    parser.add_argument("--out-dir", type=Path, default=DEFAULT_OUT)
    return parser


def load_baselines():
    out = {}
    for name in ("random_init_reference", "exp1_best"):
        rows = json.loads((BASELINE_DIR / f"{name}.json").read_text())["gen_common_per_program"]
        out[name] = {r["program_id"]: r for r in rows}
    return out


def fmt(v, pct=False):
    if v is None:
        return "n/a"
    return f"{100 * v:.1f}%" if pct else f"{v:.3f}"


def markdown(agg):
    lines = ["# Design A: three-seed aggregate (validation)", "", f"Rule: {agg['rule']}.", "",
             "## Per seed", "", "| Seed | best epoch | best val loss | type-seq sim | token edit sim | distinct | "
             "TF source win rate | source gen gap (type-seq) | model − retrieval (type-seq) | checks passed |",
             "|---|---|---|---|---|---|---|---|---|---|"]
    for row in agg["per_seed"]:
        passed = sum(bool(v) for v in row["acceptance"].values())
        lines.append(f"| {row['seed']} | {row['best_epoch']} | {row['best_val_loss']:.4f} | "
                     f"{fmt(row['model.type_sequence_edit_similarity'])} | {fmt(row['model.token_edit_similarity'])} | "
                     f"{row['distinct_outputs']} | {fmt(row['source_tf_win_rate'], True)} | "
                     f"{row['source_gen_gap_type_seq']:+.3f} | {row['retrieval_gap_type_seq']:+.3f} | {passed}/8 |")
    lines += ["", "## Across seeds (mean ± sd)", "", "| Metric | mean | sd |", "|---|---|---|"]
    for k, v in agg["across_seeds_mean_sd"].items():
        lines.append(f"| {k} | {fmt(v['mean'])} | {fmt(v['sd'])} |")
    lines += ["", "## Seed-averaged paired tests (56 programs)", ""]
    t = agg["seed_averaged_tests"]
    lines.append(f"- 4a teacher-forced source wins: {t['4a_source_tf']['wins']}/{t['4a_source_tf']['n']}, "
                 f"Wilson {fmt(t['4a_source_tf']['wilson_95ci'][0])}–{fmt(t['4a_source_tf']['wilson_95ci'][1])}, "
                 f"p = {t['4a_source_tf']['binomial_p_two_sided']:.2g}")
    for name in ("4b_source_gen", "5_vs_retrieval", "3_vs_exp1_best", "3_vs_random_init_reference"):
        m = t[name]["type_sequence_edit_similarity"]
        lines.append(f"- {name} (type-seq): mean diff {m['mean_difference']:+.3f} "
                     f"[{m['bootstrap_95ci'][0]:+.3f}, {m['bootstrap_95ci'][1]:+.3f}], "
                     f"sign test {m['wins']}/{m['losses']}/{m['ties']} p = {m['p_value']:.2g}")
    lines += ["", "## Robustness verdicts", "", "| Check | passes in seeds | seed-averaged | robust |", "|---|---|---|---|"]
    for check in CHECKS:
        r = agg["robust"][check]
        lines.append(f"| {check} | {r['passes_in_seeds']} | {r['seed_averaged_pass']} | {r['robust']} |")
    return "\n".join(lines) + "\n"


def main():
    args = build_parser().parse_args()
    out_dir = args.out_dir.resolve()
    if out_dir.exists():
        raise SystemExit(f"{out_dir} exists; refusing to overwrite.")
    metrics = [json.loads((d / "metrics.json").read_text()) for d in args.results]
    per_program = [json.loads((d / "per_program.json").read_text()) for d in args.results]
    agg = aggregate(metrics, per_program, load_baselines(), ACCEPTANCE)
    agg["inputs"] = [str(d.resolve().relative_to(ROOT)) for d in args.results]
    out_dir.mkdir(parents=True)
    (out_dir / "aggregate.json").write_text(json.dumps(agg, indent=1))
    (out_dir / "summary.md").write_text(markdown(agg), encoding="utf-8")
    print(markdown(agg))


if __name__ == "__main__":
    main()

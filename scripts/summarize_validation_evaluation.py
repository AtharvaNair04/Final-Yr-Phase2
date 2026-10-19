#!/usr/bin/env python3
"""Renders results/eval_validation_v1/summary.json as summary.md (tables only; no new measurements)."""

import json
from pathlib import Path

OUT_DIR = Path(__file__).resolve().parents[1] / "results" / "eval_validation_v1"


def fmt(v, pct=False):
    if v is None:
        return "n/a"
    if isinstance(v, float):
        return f"{100 * v:.1f}%" if pct else f"{v:.3f}"
    return str(v)


def main():
    s = json.loads((OUT_DIR / "summary.json").read_text())
    ck = s["checkpoints"]
    lines = [
        "# Diagnostic validation evaluation (protocol eval_validation_v1)",
        "",
        f"Validation split only: {s['validation_programs']} programs, {s['validation_chunks']} chunks. "
        f"Device {s['device']}, torch {s['torch']}. Greedy cap {s['max_new_tokens']} tokens. "
        f"Reference chunk reconstruction verified for {s['reference_reconstruction_ok']} programs.",
        "",
        "**These checkpoints have different training histories (see last table). This is not a fair comparison "
        "of training strategies.**",
        "",
        "## Teacher-forced",
        "",
        "| Checkpoint | TF-all loss (micro) | TF-all acc excl EOS (micro) | TF-all acc (macro/program) | "
        "content acc excl UNK (macro) | STEP-value acc excl UNK (macro) | TF-first loss | TF-first acc |",
        "|---|---|---|---|---|---|---|---|",
    ]
    for name, r in ck.items():
        if "skipped" in r:
            lines.append(f"| {name} | skipped: {r['skipped']} |||||||")
            continue
        a, m, f = r["tf_all"]["micro"], r["tf_all"]["macro_over_programs"], r["tf_first"]["micro"]
        lines.append(
            f"| {name} | {fmt(a['loss_incl_eos'])} | {fmt(a['token_acc_excl_eos'], True)} | "
            f"{fmt(m['token_acc_excl_eos'], True)} | {fmt(m['content_token_acc_excl_unk'], True)} | "
            f"{fmt(m['step_value_acc_excl_unk'], True)} | {fmt(f['loss_incl_eos'])} | {fmt(f['token_acc_excl_eos'], True)} |")
    lines += [
        "",
        "## Greedy generation, first chunk per program (macro over 56 programs)",
        "",
        "| Checkpoint | exact | EOS | bracket-valid | grammar-valid | token edit sim | type seq edit sim | "
        "type bag F1 | attr value match | event exact (no STEP) | full-program exact (1-chunk programs) |",
        "|---|---|---|---|---|---|---|---|---|---|---|",
    ]
    for name, r in ck.items():
        if "skipped" in r:
            continue
        g = r["gen_first"]
        lines.append(
            f"| {name} | {fmt(g['exact_match_rate'], True)} | {fmt(g['eos_reached_rate'], True)} | "
            f"{fmt(g['bracket_valid_rate'], True)} | {fmt(g['grammar_valid_rate'], True)} | "
            f"{fmt(g['token_edit_similarity'])} | {fmt(g['type_sequence_edit_similarity'])} | "
            f"{fmt(g['type_bag_f1'])} | {fmt(g.get('attr_value_match_rate'))} | "
            f"{fmt(g['event_exact_nostep_rate'], True)} | {g['full_program_exact_matches']}/{g['single_chunk_programs']} |")
    lines += ["", "## Training history, contamination and compatibility", "",
              "| Checkpoint | History | Validation contamination | max_src | truncated val programs | "
              "bias-vs-rank Spearman (heuristic) |", "|---|---|---|---|---|---|"]
    for name, r in ck.items():
        c = r.get("compatibility", {})
        lines.append(
            f"| {name} | {r.get('history', '')} | {r.get('validation_contamination', '')} | "
            f"{r.get('model_config', {}).get('max_src_length')} | {len(r.get('source_truncated_programs', []))} | "
            f"{fmt(c.get('heuristic_bias_vs_frequency_rank_spearman'))} |")
    lines += ["", "Not computed: BLEU (not implemented in this protocol); event-level metrics for chunks k>0 "
              "(generation of later chunks is undefined in the current design)."]
    (OUT_DIR / "summary.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
    print("\n".join(lines))


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""
Design A evaluation-only error analysis (validation split only; no training; test split never opened).

For each seed's checkpoint_best.pt (42, 43, 44):
  1. Regenerate greedy predictions with the committed evaluation functions (evaluate_first_chunk.generate /
     score, same batching, cap 1025) and check that every per-program raw metric reproduces the saved
     per_program.json "correct" entry. Tolerance: booleans and lengths must be identical; float metrics within
     REPRO_TOL. Any mismatch stops the run before anything else is computed.
  2. Save the full predicted token-id sequences.
  3. TE diagnostics: whether each output contains a valid unescaped TE event and where; whether TE is the
     final event of each reference.
  4. First-TE truncation (DIAGNOSTIC ONLY): outputs are cut after their first TE event and re-scored; raw and
     truncated scores are compared separately for the 8 short-trace programs and the other 48.
  5. Event-type composition (multiset counts per type) and attribute-key match for model and retrieval.

Primary results remain those in results/design_a_first_chunk_*; nothing here replaces them.
Writes results/design_a_error_analysis_v1/ (refuses to overwrite).
"""

import json
import sys
import time
from collections import Counter
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / "scripts"))

from evaluation import event_metrics, parse_trace  # noqa: E402
from first_chunk import bootstrap_mean_ci, sign_test  # noqa: E402
from vocabulary import Vocabulary  # noqa: E402
import evaluate_first_chunk as efc  # noqa: E402
import run_validation_evaluation as rve  # noqa: E402

OUT_DIR = ROOT / "results" / "design_a_error_analysis_v1"
SEEDS = {42: ("checkpoints/design_a_first_chunk_v1", "results/design_a_first_chunk_v1"),
         43: ("checkpoints/design_a_first_chunk_seed43", "results/design_a_first_chunk_seed43"),
         44: ("checkpoints/design_a_first_chunk_seed44", "results/design_a_first_chunk_seed44")}
REPRO_TOL = 1e-9
SHORT_THRESHOLD = 900          # reference first chunk shorter than this => trace ends inside chunk 0
TIME_LIMIT_SECONDS = 3600


# ============================================================
# HELPERS (pure; unit-tested)
# ============================================================

def te_info(tokens):
    """Index of the first well-formed TE event (None if absent), number of events, and whether that TE is last."""
    events, _ = parse_trace(tokens)
    idx = next((i for i, e in enumerate(events) if e.well_formed and e.type == "TE"), None)
    return {"first_te_event_index": idx, "events": len(events), "te_is_final_event": idx is not None and idx == len(events) - 1}


def truncate_after_first_te(tokens):
    """Tokens up to and including the <EVENT_END> of the first well-formed TE event; unchanged if there is none."""
    events, _ = parse_trace(tokens)
    idx = next((i for i, e in enumerate(events) if e.well_formed and e.type == "TE"), None)
    if idx is None:
        return list(tokens)
    seen, i = -1, 0
    while i < len(tokens):
        if tokens[i] == "\\":
            i += 2
            continue
        if tokens[i] == "<EVENT_START>":
            seen += 1
        if tokens[i] == "<EVENT_END>" and seen == idx:
            return list(tokens[:i + 1])
        i += 1
    return list(tokens)


def type_counts(tokens):
    events, _ = parse_trace(tokens)
    return Counter(e.type for e in events)


def compare_metric(saved, new, key):
    a, b = saved[key], new[key]
    if isinstance(a, bool) or isinstance(b, bool) or key in ("pred_length", "ref_length"):
        return a == b
    if a is None or b is None:
        return a is None and b is None
    return abs(a - b) <= REPRO_TOL


# ============================================================
# MAIN
# ============================================================

def main():
    if OUT_DIR.exists():
        raise SystemExit(f"{OUT_DIR} exists; refusing to overwrite.")
    t_start = time.time()
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    src_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_source.json")
    tgt_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_target.json")
    train_rows, _ = efc.as_items(efc.load_first_chunks("train", src_vocab, tgt_vocab))
    val_rows, val_src = efc.as_items(efc.load_first_chunks("validation", src_vocab, tgt_vocab))
    refs = {r["pid"]: r["tgt"][:-1] for r in val_rows}
    ref_tok = {p: tgt_vocab.decode(ids) for p, ids in refs.items()}
    train_targets = {r["pid"]: r["tgt"][:-1] for r in train_rows}
    short = sorted(p for p in refs if len(refs[p]) < SHORT_THRESHOLD)

    predictions, reproduction, per_seed_rows = {}, {}, {}
    for seed, (run_dir, res_dir) in SEEDS.items():
        saved = json.loads((ROOT / res_dir / "per_program.json").read_text())
        model, cfg, _ = rve.build_model(str(seed), {"path": f"{run_dir}/checkpoint_best.pt"}, src_vocab, tgt_vocab, device)
        gen = efc.generate(model, cfg, val_rows, val_src, tgt_vocab, device)
        del model
        if device.type == "cuda":
            torch.cuda.empty_cache()
        mismatches = []
        for p, (ids, eos) in gen.items():
            new = efc.score(refs[p], ids, eos, tgt_vocab)
            bad = [k for k in efc.GEN_KEYS if not compare_metric(saved[p]["correct"], new, k)]
            if tgt_vocab.decode(ids[:40]) != saved[p]["pred_head"]:
                bad.append("pred_head")
            if bad:
                mismatches.append({"program_id": p, "fields": bad})
        reproduction[seed] = {"programs": len(gen), "mismatching_programs": len(mismatches), "details": mismatches,
                              "tolerance": REPRO_TOL}
        print(f"seed {seed}: reproduction mismatches {len(mismatches)}/{len(gen)} ({time.time() - t_start:.0f}s)", flush=True)
        if mismatches:
            OUT_DIR.mkdir(parents=True)
            (OUT_DIR / "reproduction_failure.json").write_text(json.dumps(reproduction, indent=1))
            raise SystemExit(f"Raw reproduction failed for seed {seed}; stopping before any analysis.")
        predictions[seed] = {p: {"pred_ids": ids, "eos_reached": eos} for p, (ids, eos) in gen.items()}
        per_seed_rows[seed] = saved
        if time.time() - t_start > TIME_LIMIT_SECONDS:
            raise SystemExit("Execution limit exceeded; stopping.")

    # TE diagnostics and truncation
    ref_te = {p: te_info(ref_tok[p]) for p in refs}
    te, trunc = {}, {}
    for seed, preds in predictions.items():
        te[seed], trunc[seed] = {}, {}
        for p, row in preds.items():
            toks = tgt_vocab.decode(row["pred_ids"])
            te[seed][p] = te_info(toks)
            cut = truncate_after_first_te(toks)
            cut_ids = row["pred_ids"][:len(cut)]
            trunc[seed][p] = efc.score(refs[p], cut_ids, row["eos_reached"], tgt_vocab)

    def group_compare(group):
        out = {}
        for key in ("type_sequence_edit_similarity", "token_edit_similarity"):
            raw = [sum(per_seed_rows[s][p]["correct"][key] for s in SEEDS) / 3 for p in group]
            cut = [sum(trunc[s][p][key] for s in SEEDS) / 3 for p in group]
            diffs = [c - r if abs(c - r) >= 1e-12 else 0.0 for r, c in zip(raw, cut)]
            mean, lo, hi = bootstrap_mean_ci(diffs)
            out[key] = {"n": len(group), "raw_mean": sum(raw) / len(raw), "truncated_mean": sum(cut) / len(cut),
                        "mean_change": mean, "bootstrap_95ci": [lo, hi], **sign_test(diffs)}
        return out
    others = sorted(p for p in refs if p not in short)
    truncation = {"short_trace_programs": short, "short": group_compare(short), "other_48": group_compare(others)}

    # Event-type composition and attribute keys (model per seed, retrieval once)
    retrieved = {p: per_seed_rows[42][p]["retrieved_from"] for p in refs}
    retr_tok = {p: tgt_vocab.decode(train_targets[retrieved[p]]) for p in refs}
    ref_counts = sum((type_counts(ref_tok[p]) for p in refs), Counter())
    composition = {"reference": dict(ref_counts), "retrieval": dict(sum((type_counts(retr_tok[p]) for p in refs), Counter()))}
    for seed, preds in predictions.items():
        composition[f"model_seed{seed}"] = dict(sum((type_counts(tgt_vocab.decode(preds[p]["pred_ids"])) for p in refs), Counter()))

    def per_type_overlap(pred_tokens):
        """Per event type: multiset overlap with the reference summed over programs -> precision / recall."""
        hit, pred_n, ref_n = Counter(), Counter(), Counter()
        for p in refs:
            pc, rc = type_counts(pred_tokens[p]), type_counts(ref_tok[p])
            for t in set(pc) | set(rc):
                hit[t] += min(pc[t], rc[t]); pred_n[t] += pc[t]; ref_n[t] += rc[t]
        return {str(t): {"precision": hit[t] / pred_n[t] if pred_n[t] else None,
                         "recall": hit[t] / ref_n[t] if ref_n[t] else None,
                         "predicted": pred_n[t], "reference": ref_n[t]} for t in sorted(set(pred_n) | set(ref_n), key=str)}
    per_type = {"retrieval": per_type_overlap(retr_tok)}
    keys = {"retrieval": {p: event_metrics(ref_tok[p], retr_tok[p])["attr_key_match_rate"] for p in refs}}
    for seed, preds in predictions.items():
        toks = {p: tgt_vocab.decode(preds[p]["pred_ids"]) for p in refs}
        per_type[f"model_seed{seed}"] = per_type_overlap(toks)
        keys[f"model_seed{seed}"] = {p: event_metrics(ref_tok[p], toks[p])["attr_key_match_rate"] for p in refs}
    key_summary = {name: {"mean_attr_key_match_rate": (lambda v: sum(v) / len(v) if v else None)([x for x in vals.values() if x is not None]),
                          "programs_with_value": sum(x is not None for x in vals.values())} for name, vals in keys.items()}

    OUT_DIR.mkdir(parents=True)
    for seed, preds in predictions.items():
        (OUT_DIR / f"predictions_seed{seed}.json").write_text(json.dumps(preds))
    report = {
        "note": "Diagnostic only. Primary results remain in results/design_a_first_chunk_*; first-TE truncation does not replace them.",
        "reproduction": reproduction, "reference_te": ref_te, "model_te": te,
        "model_te_summary": {seed: {"outputs_with_te": sum(v["first_te_event_index"] is not None for v in te[seed].values()),
                                    "short_programs_with_te": sum(te[seed][p]["first_te_event_index"] is not None for p in short),
                                    "outputs_where_te_is_final_event": sum(v["te_is_final_event"] for v in te[seed].values())}
                             for seed in te},
        "reference_te_summary": {"short_programs": len(short),
                                 "short_with_te_final": sum(ref_te[p]["te_is_final_event"] for p in short),
                                 "all_with_te": sum(ref_te[p]["first_te_event_index"] is not None for p in refs)},
        "truncation_diagnostic": truncation, "event_type_composition": composition, "per_type_overlap": per_type,
        "attribute_keys": {"summary": key_summary, "per_program": keys},
        "runtime_seconds": time.time() - t_start,
    }
    (OUT_DIR / "analysis.json").write_text(json.dumps(report, indent=1))
    print(json.dumps({k: report[k] for k in ("model_te_summary", "reference_te_summary", "runtime_seconds")}, indent=1))
    print(json.dumps(truncation, indent=1))


if __name__ == "__main__":
    main()

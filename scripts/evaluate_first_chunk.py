#!/usr/bin/env python3
"""
Design A evaluation (validation split only; the test split is never opened).

For the trained model (checkpoint_best.pt = lowest validation loss) on the 56 validation first chunks:
  1. Teacher-forced loss/accuracy with the correct source and with a mismatched source (deterministic
     derangement: each program gets the next program's source in id order).
  2. Greedy generation (cap 1025 new tokens, no penalties) with correct and mismatched sources:
     exact match, token edit similarity, type-sequence edit similarity, grammar validity, EOS, distinct outputs.
  3. Token-overlap retrieval baseline: the first chunk of the training program whose source token-ID set has
     the highest Jaccard similarity.
  4. Common step-free window (first E events, E from results/eval_validation_v2_nostep) for paired comparison
     with the existing random-init and exp1 baselines.
  5. Capacity diagnostic: teacher-forced accuracy on the 449 training first chunks (best and final checkpoints).
  6. Complete-trace results only for validation programs whose whole trace is one chunk.

Acceptance thresholds are fixed in ACCEPTANCE below (set before results were seen).
Writes --out-dir (default results/design_a_first_chunk_v1/; refuses to overwrite) for the run in --run-dir
(default checkpoints/design_a_first_chunk_v1/).
"""

import argparse
import json
import sys
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / "scripts"))

from evaluation import generation_metrics, greedy_generate, macro_average  # noqa: E402
from first_chunk import (  # noqa: E402
    bootstrap_mean_ci, mismatch_assignment, retrieve_nearest, sign_test, wilson_interval, binomial_two_sided_p,
)
from target_format import first_n_events  # noqa: E402
from vocabulary import Vocabulary  # noqa: E402
import run_validation_evaluation as rve  # noqa: E402
from train_first_chunk import RUN_DIR, load_first_chunks  # noqa: E402

OUT_DIR = ROOT / "results" / "design_a_first_chunk_v1"
BASELINE_DIR = ROOT / "results" / "eval_validation_v2_nostep"
ACCEPTANCE = {
    "capacity_train_tf_acc_min": 0.95,          # diagnostic only
    "source_tf_win_wilson_lower_gt": 0.5,
    "source_gen_sign_test_p_lt": 0.05,
    "distinct_outputs_min": 28,                 # of 56
    "retrieval_sign_test_p_lt": 0.05,
}
GEN_KEYS = ("exact_match", "eos_reached", "grammar_valid", "bracket_valid", "token_edit_similarity",
            "token_positional_accuracy", "type_sequence_edit_similarity", "type_bag_f1", "attr_value_match_rate",
            "event_exact_rate", "length_ratio", "pred_length", "ref_length", "step_match_rate")


def as_items(ds):
    items = [ds[i] for i in range(len(ds))]
    rows = [{"pid": it.program_id, "chunk_index": it.chunk_index, "total_chunks": it.total_chunks,
             "dec": it.decoder_input_ids, "tgt": it.target_ids} for it in items]
    return rows, {it.program_id: it.source_ids for it in items}


def tf_per_program(model, cfg, rows, sources, tgt_vocab, device):
    per_chunk = rve.teacher_forced(model, cfg, rows, sources, tgt_vocab, device)
    agg, per_prog = rve.summarize_tf(per_chunk)
    return agg, per_prog


def generate(model, cfg, rows, sources, tgt_vocab, device):
    out = {}
    for b in range(0, len(rows), rve.GEN_BATCH):
        batch = rows[b:b + rve.GEN_BATCH]
        src, _ = rve.collate(batch, sources, cfg.max_src_length, cfg.src_pad_id, cfg.tgt_pad_id, device)
        for r, res in zip(batch, greedy_generate(model, src, src.eq(cfg.src_pad_id), tgt_vocab.bos_id,
                                                 tgt_vocab.eos_id, rve.MAX_NEW_TOKENS)):
            out[r["pid"]] = res
    return out


def score(ref_ids, pred_ids, eos, tgt_vocab):
    m = generation_metrics(ref_ids, pred_ids, tgt_vocab.decode(ref_ids), tgt_vocab.decode(pred_ids), eos)
    return {k: m[k] for k in GEN_KEYS}


def paired(a, b, key):
    diffs = [a[p][key] - b[p][key] for p in a if a[p][key] is not None and b[p][key] is not None]
    mean, lo, hi = bootstrap_mean_ci(diffs)
    return {"metric": key, "n": len(diffs), "mean_difference": mean, "bootstrap_95ci": [lo, hi], **sign_test(diffs)}


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser()
    parser.add_argument("--run-dir", type=Path, default=RUN_DIR, help="training run directory (checkpoints/...)")
    parser.add_argument("--out-dir", type=Path, default=OUT_DIR, help="new results directory")
    return parser


def main():
    args = build_parser().parse_args()
    run_dir, out_dir = args.run_dir.resolve(), args.out_dir.resolve()
    if out_dir.exists():
        raise SystemExit(f"{out_dir} exists; refusing to overwrite.")
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    src_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_source.json")
    tgt_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_target.json")
    train_rows, train_src = as_items(load_first_chunks("train", src_vocab, tgt_vocab))
    val_rows, val_src = as_items(load_first_chunks("validation", src_vocab, tgt_vocab))
    assign = mismatch_assignment(list(val_src))
    mis_src = {pid: val_src[assign[pid]] for pid in val_src}
    refs = {r["pid"]: r["tgt"][:-1] for r in val_rows}
    run_config = json.loads((run_dir / "run_config.json").read_text())
    history = json.loads((run_dir / "training_history.json").read_text())

    spec = lambda name: {"path": str((run_dir / name).relative_to(ROOT))}  # noqa: E731
    model, cfg, meta = rve.build_model("best", spec("checkpoint_best.pt"), src_vocab, tgt_vocab, device)

    # 1. Teacher-forced, correct vs mismatched source.
    tf_ok, tf_ok_prog = tf_per_program(model, cfg, val_rows, val_src, tgt_vocab, device)
    tf_mis, tf_mis_prog = tf_per_program(model, cfg, val_rows, mis_src, tgt_vocab, device)
    wins = sum(tf_ok_prog[p]["loss_incl_eos"] < tf_mis_prog[p]["loss_incl_eos"] for p in val_src)
    lo, hi = wilson_interval(wins, len(val_src))
    loss_diff = [tf_mis_prog[p]["loss_incl_eos"] - tf_ok_prog[p]["loss_incl_eos"] for p in val_src]
    m, blo, bhi = bootstrap_mean_ci(loss_diff)
    source_tf = {"programs": len(val_src), "correct_source_lower_loss": wins, "win_rate": wins / len(val_src),
                 "wilson_95ci": [lo, hi], "binomial_p_two_sided": binomial_two_sided_p(wins, len(val_src)),
                 "mean_loss_increase_with_mismatched_source": m, "bootstrap_95ci": [blo, bhi]}

    # 2. Generation, correct vs mismatched source.
    gen_ok_raw = generate(model, cfg, val_rows, val_src, tgt_vocab, device)
    gen_mis_raw = generate(model, cfg, val_rows, mis_src, tgt_vocab, device)
    gen_ok = {p: score(refs[p], ids, eos, tgt_vocab) for p, (ids, eos) in gen_ok_raw.items()}
    gen_mis = {p: score(refs[p], ids, eos, tgt_vocab) for p, (ids, eos) in gen_mis_raw.items()}
    distinct = len({tuple(ids) for ids, _ in gen_ok_raw.values()})
    identical_under_swap = sum(gen_ok_raw[p][0] == gen_mis_raw[p][0] for p in val_src)

    # 3. Retrieval baseline.
    retrieved = {p: retrieve_nearest(val_src[p], train_src) for p in val_src}
    train_targets = {r["pid"]: r["tgt"][:-1] for r in train_rows}
    retr = {p: score(refs[p], train_targets[nb], True, tgt_vocab) for p, (nb, _) in retrieved.items()}
    for p in retr:
        retr[p]["eos_reached"] = None   # not applicable to retrieval

    # 4. Common step-free window vs existing baselines.
    base = {name: {r["program_id"]: r for r in json.loads((BASELINE_DIR / f"{name}.json").read_text())["gen_common_per_program"]}
            for name in ("random_init_reference", "exp1_best")}
    window = {p: base["exp1_best"][p]["window_events"] for p in val_src}
    end, esc = tgt_vocab.event_end_id, tgt_vocab.escape_id
    common = {}
    for p, (ids, eos) in gen_ok_raw.items():
        ref_c = first_n_events(refs[p], window[p], event_end=end, escape=esc)
        pred_c = first_n_events(ids, window[p], event_end=end, escape=esc)
        common[p] = score(ref_c, pred_c, eos, tgt_vocab)
    common_vs = {name: {k: paired(common, base[name], k) for k in ("token_edit_similarity", "type_sequence_edit_similarity")}
                 for name in base}

    # 5. Capacity diagnostic on training first chunks.
    capacity = {"best": tf_per_program(model, cfg, train_rows, train_src, tgt_vocab, device)[0]["micro"]}
    del model
    final_model, fcfg, _ = rve.build_model("final", spec("checkpoint_latest.pt"), src_vocab, tgt_vocab, device)
    capacity["final"] = tf_per_program(final_model, fcfg, train_rows, train_src, tgt_vocab, device)[0]["micro"]
    del final_model

    # 6. Complete traces (single-chunk programs only).
    single = sorted(r["pid"] for r in val_rows if r["total_chunks"] == 1)
    complete = {p: {"exact_match": gen_ok[p]["exact_match"], "token_edit_similarity": gen_ok[p]["token_edit_similarity"],
                    "type_sequence_edit_similarity": gen_ok[p]["type_sequence_edit_similarity"],
                    "grammar_valid": gen_ok[p]["grammar_valid"], "eos_reached": gen_ok[p]["eos_reached"]} for p in single}

    summary = {
        "model": macro_average(list(gen_ok.values())), "mismatched_source": macro_average(list(gen_mis.values())),
        "retrieval": macro_average(list(retr.values())), "common_window_model": macro_average(list(common.values())),
        "common_window_baselines": {n: macro_average([{k: v for k, v in r.items() if isinstance(v, (int, float, bool)) or v is None}
                                                      for r in base[n].values()]) for n in base},
    }
    paired_tests = {
        "correct_vs_mismatched_source": {k: paired(gen_ok, gen_mis, k) for k in ("type_sequence_edit_similarity", "token_edit_similarity")},
        "model_vs_retrieval": {k: paired(gen_ok, retr, k) for k in ("type_sequence_edit_similarity", "token_edit_similarity")},
        "common_window_model_vs_baselines": common_vs,
    }
    best_cap = max(capacity["best"]["token_acc_excl_eos"], capacity["final"]["token_acc_excl_eos"])
    st, rt = paired_tests["correct_vs_mismatched_source"]["type_sequence_edit_similarity"], paired_tests["model_vs_retrieval"]["type_sequence_edit_similarity"]
    acceptance = {
        "1_integrity": run_config["pretraining_checks"]["pass"],
        "2_capacity_diagnostic_train_tf_acc_ge_0.95": best_cap >= ACCEPTANCE["capacity_train_tf_acc_min"],
        "3_beats_random_init_common_window": all(common_vs["random_init_reference"][k]["mean_difference"] > 0 for k in common_vs["random_init_reference"]),
        "3_beats_exp1_common_window": all(common_vs["exp1_best"][k]["mean_difference"] > 0 for k in common_vs["exp1_best"]),
        "4a_source_tf_wilson_lower_gt_0.5": lo > ACCEPTANCE["source_tf_win_wilson_lower_gt"],
        "4b_source_gen_sign_test": st["wins"] > st["losses"] and st["p_value"] < ACCEPTANCE["source_gen_sign_test_p_lt"],
        "4c_distinct_outputs_ge_28": distinct >= ACCEPTANCE["distinct_outputs_min"],
        "5_beats_retrieval": rt["wins"] > rt["losses"] and rt["p_value"] < ACCEPTANCE["retrieval_sign_test_p_lt"],
    }
    report = {
        "experiment": run_config["experiment"], "seed": run_config["training_config"]["seed"],
        "run_dir": str(run_dir.relative_to(ROOT)), "split": "validation", "checkpoint": "checkpoint_best.pt",
        "best_epoch": history["best_epoch"], "best_val_loss": history["best_val_loss"],
        "optimizer_steps": history["optimizer_steps"], "acceptance_thresholds": ACCEPTANCE, "acceptance": acceptance,
        "teacher_forced_correct_source": tf_ok, "teacher_forced_mismatched_source": tf_mis,
        "source_conditioning_tf": source_tf, "distinct_outputs": distinct,
        "generations_identical_under_source_swap": identical_under_swap,
        "summary": summary, "paired_tests": paired_tests, "capacity_diagnostic": capacity,
        "complete_traces_single_chunk_programs": complete,
        "compatibility": meta["compatibility"],
    }
    out_dir.mkdir(parents=True)
    (out_dir / "metrics.json").write_text(json.dumps(report, indent=1))
    per_program = {p: {"correct": gen_ok[p], "mismatched": gen_mis[p], "retrieval": retr[p],
                       "retrieved_from": retrieved[p][0], "retrieval_jaccard": retrieved[p][1],
                       "mismatched_source_from": assign[p], "tf_loss_correct": tf_ok_prog[p]["loss_incl_eos"],
                       "tf_loss_mismatched": tf_mis_prog[p]["loss_incl_eos"], "common_window": common[p],
                       "window_events": window[p], "pred_head": tgt_vocab.decode(gen_ok_raw[p][0][:40])}
                   for p in sorted(val_src)}
    (out_dir / "per_program.json").write_text(json.dumps(per_program, indent=1))
    print(json.dumps({"acceptance": acceptance, "source_conditioning_tf": source_tf, "distinct_outputs": distinct,
                      "paired_tests": paired_tests, "capacity": capacity}, indent=1))


if __name__ == "__main__":
    main()

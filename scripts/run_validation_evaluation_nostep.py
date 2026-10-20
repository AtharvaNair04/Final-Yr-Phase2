#!/usr/bin/env python3
"""
Step-free diagnostic evaluation of existing checkpoints (protocol eval_validation_v2_nostep).

Validation split only; the test split is never opened. Reuses the committed driver
(run_validation_evaluation.py) unchanged for model loading, batching, teacher forcing and greedy decoding.

Common step-free comparison space (GEN-common):
  For each validation program, E = number of events in its esr_v1_step chunk 0 (1024-token chunk).
  Reference  = step-free form of the v1 chunk 0, which equals the first E events of the esr_v2_nostep
               serialization (asserted for every program).
  Candidate  = greedy output in the checkpoint's native format (all existing checkpoints are
               esr_v1_step: no 'target_format' key), canonicalized with strip_step_markers_lenient
               (removes every unescaped STEP marker and the token after it) and cut after its E-th
               unescaped <EVENT_END>. A future esr_v2_nostep model would skip the strip.
  Decoding is identical to eval_validation_v1 (greedy, cap 1025 new tokens, no penalties), so these are
  the same generations re-scored, not new samples. STEP correctness is not part of this space:
  step_match_rate is None by construction.

TF-v2-diagnostic: teacher-forced statistics of each checkpoint on esr_v2_nostep targets. These models
were trained on esr_v1_step, so the numbers measure behaviour under a format shift and are NOT
comparable with any teacher-forced score in eval_validation_v1 or with a step-free model's TF scores.

Writes results/eval_validation_v2_nostep/ (per-checkpoint JSON, summary.json, summary.md).
"""

import json
import sys
import time
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / "scripts"))

from chunk_cache import CachedExecutionTraceDataset  # noqa: E402
from evaluation import generation_metrics, greedy_generate, macro_average  # noqa: E402
from target_format import TARGET_FORMAT_V1, TARGET_FORMAT_V2, first_n_events, strip_step_markers_lenient  # noqa: E402
from vocabulary import Vocabulary  # noqa: E402
import run_validation_evaluation as rve  # noqa: E402

OUT_DIR = ROOT / "results" / "eval_validation_v2_nostep"
V2_CACHE = ROOT / "data_cache" / "chunks_v2_nostep" / "validation"
COMPARISON_SPACE = ("step-free; first E events, E = events in the program's esr_v1_step chunk 0; "
                    "candidate STEP markers removed by strip_step_markers_lenient")
TF_NOTE = ("diagnostic only: checkpoint trained on esr_v1_step, scored on esr_v2_nostep targets; "
           "not comparable with eval_validation_v1 teacher-forced scores")
METADATA_KEYS = ("program_id", "total_chunks", "window_events", "native_pred_length", "pred_head",
                 "full_program_exact_match")


def count_events(ids, event_end_id, escape_id):
    n, i = 0, 0
    while i < len(ids):
        if ids[i] == escape_id:
            i += 2
            continue
        n += ids[i] == event_end_id
        i += 1
    return n


def load_v2_items(src_vocab, tgt_vocab):
    ds = CachedExecutionTraceDataset(V2_CACHE, ROOT / "Module3_Training_Dataset" / "split" / "validation.jsonl",
                                     ROOT / "selected source code", src_vocab, tgt_vocab,
                                     target_format=TARGET_FORMAT_V2)
    return [{"pid": it.program_id, "chunk_index": it.chunk_index, "total_chunks": it.total_chunks,
             "dec": it.decoder_input_ids, "tgt": it.target_ids} for it in (ds[i] for i in range(len(ds)))]


def main():
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    src_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_source.json")
    tgt_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_target.json")
    step_id, esc_id, end_id = tgt_vocab.step_id, tgt_vocab.escape_id, tgt_vocab.event_end_id

    _, v1_items, sources, recon_ok = rve.load_validation(src_vocab, tgt_vocab)
    v2_items = load_v2_items(src_vocab, tgt_vocab)
    v1_first = {it["pid"]: it for it in v1_items if it["chunk_index"] == 0}
    v2_first = {it["pid"]: it["tgt"][:-1] for it in v2_items if it["chunk_index"] == 0}

    window, ref_common, consistent = {}, {}, {}
    for pid, it in v1_first.items():
        v1_ref = it["tgt"][:-1]
        window[pid] = count_events(v1_ref, end_id, esc_id)
        ref_common[pid] = strip_step_markers_lenient(v1_ref, step=step_id, escape=esc_id)
        consistent[pid] = ref_common[pid] == first_n_events(v2_first[pid], window[pid], event_end=end_id, escape=esc_id)
    if not all(consistent.values()):
        raise RuntimeError(f"Common reference inconsistent for {[p for p, ok in consistent.items() if not ok]}")
    print(f"Validation: {len(v1_items)} v1 chunks, {len(v2_items)} v2 chunks, {len(v1_first)} programs; "
          f"common references consistent for all programs", flush=True)

    OUT_DIR.mkdir(parents=True, exist_ok=True)
    summary = {"protocol": "eval_validation_v2_nostep", "comparison_space": COMPARISON_SPACE,
               "teacher_forced_note": TF_NOTE, "device": str(device), "torch": torch.__version__,
               "max_new_tokens": rve.MAX_NEW_TOKENS, "validation_programs": len(v1_first),
               "v1_validation_chunks": len(v1_items), "v2_validation_chunks": len(v2_items),
               "reference_reconstruction_ok_v1": f"{sum(recon_ok.values())}/{len(recon_ok)}",
               "common_reference_consistent": f"{sum(consistent.values())}/{len(consistent)}", "checkpoints": {}}

    firsts = list(v1_first.values())
    for name, spec in rve.CHECKPOINTS.items():
        t0 = time.time()
        model, cfg, meta = rve.build_model(name, spec, src_vocab, tgt_vocab, device)
        native = TARGET_FORMAT_V1  # no existing checkpoint records a target_format
        per_chunk = rve.teacher_forced(model, cfg, v2_items, sources, tgt_vocab, device)
        tf_all, _ = rve.summarize_tf(per_chunk)
        tf_first, _ = rve.summarize_tf(per_chunk, lambda pid, ci: ci == 0)

        rows = []
        for b in range(0, len(firsts), rve.GEN_BATCH):
            batch = firsts[b:b + rve.GEN_BATCH]
            src, _ = rve.collate(batch, sources, cfg.max_src_length, cfg.src_pad_id, cfg.tgt_pad_id, device)
            outs = greedy_generate(model, src, src.eq(cfg.src_pad_id), tgt_vocab.bos_id, tgt_vocab.eos_id,
                                   rve.MAX_NEW_TOKENS)
            for it, (pred_ids, eos) in zip(batch, outs):
                pid = it["pid"]
                canon = strip_step_markers_lenient(pred_ids, step=step_id, escape=esc_id) if native == TARGET_FORMAT_V1 else pred_ids
                pred_c = first_n_events(canon, window[pid], event_end=end_id, escape=esc_id)
                ref_c = ref_common[pid]
                m = generation_metrics(ref_c, pred_c, tgt_vocab.decode(ref_c), tgt_vocab.decode(pred_c), eos)
                m.update({"program_id": pid, "total_chunks": it["total_chunks"], "window_events": window[pid],
                          "native_pred_length": len(pred_ids),
                          "full_program_exact_match": m["exact_match"] if it["total_chunks"] == 1 else None,
                          "pred_head": tgt_vocab.decode(pred_c[:40])})
                rows.append(m)
        gen = macro_average([{k: v for k, v in r.items() if k not in METADATA_KEYS} for r in rows])
        single = [r for r in rows if r["total_chunks"] == 1]
        gen["single_chunk_programs"] = len(single)
        gen["full_program_exact_matches"] = sum(r["exact_match"] for r in single)
        gen["distinct_outputs_first_40_tokens"] = len({tuple(r["pred_head"]) for r in rows})

        record = {"history": spec["history"], "validation_contamination": spec["validation_contamination"],
                  "path": spec["path"], "native_target_format": native, "compatibility": meta["compatibility"],
                  "model_config": meta["model_config"],
                  "source_truncated_programs": sorted(p for p, s in sources.items() if len(s) > cfg.max_src_length),
                  "gen_common": gen, "tf_v2_diagnostic": {"note": TF_NOTE, "tf_all": tf_all, "tf_first": tf_first},
                  "seconds": time.time() - t0}
        (OUT_DIR / f"{name}.json").write_text(json.dumps({**record, "gen_common_per_program": rows}, indent=1))
        summary["checkpoints"][name] = record
        print(f"{name}: GEN-common exact {gen['exact_match_rate']:.3f} edit {gen['token_edit_similarity']:.3f} "
              f"type-seq {gen['type_sequence_edit_similarity']:.3f} distinct {gen['distinct_outputs_first_40_tokens']} | "
              f"TF-v2 (diagnostic) loss {tf_all['micro']['loss_incl_eos']:.3f} ({record['seconds']:.0f}s)", flush=True)
        del model
        if device.type == "cuda":
            torch.cuda.empty_cache()

    (OUT_DIR / "summary.json").write_text(json.dumps(summary, indent=1))
    write_markdown(summary)
    print(f"Saved to {OUT_DIR}")


def fmt(v, pct=False):
    if v is None:
        return "n/a"
    if isinstance(v, float):
        return f"{100 * v:.1f}%" if pct else f"{v:.3f}"
    return str(v)


def write_markdown(s):
    lines = [
        "# Step-free diagnostic evaluation (protocol eval_validation_v2_nostep)", "",
        f"Validation only: {s['validation_programs']} programs. Comparison space: {s['comparison_space']}. "
        f"Common references consistent: {s['common_reference_consistent']}.", "",
        "**Diagnostic baseline. Checkpoints have different training histories and were all trained on "
        "esr_v1_step; this is not a fair comparison of training strategies. Step accuracy is not part of "
        "this space (n/a).**", "",
        "## Generation, first chunk, common step-free window (macro over programs)", "",
        "| Checkpoint | exact | EOS (native) | grammar-valid | token edit sim | type seq edit sim | type bag F1 | "
        "attr value match | event exact | step match | distinct outputs | full-program exact |",
        "|---|---|---|---|---|---|---|---|---|---|---|---|",
    ]
    for name, r in s["checkpoints"].items():
        g = r["gen_common"]
        lines.append(
            f"| {name} | {fmt(g['exact_match_rate'], True)} | {fmt(g['eos_reached_rate'], True)} | "
            f"{fmt(g['grammar_valid_rate'], True)} | {fmt(g['token_edit_similarity'])} | "
            f"{fmt(g['type_sequence_edit_similarity'])} | {fmt(g['type_bag_f1'])} | {fmt(g.get('attr_value_match_rate'))} | "
            f"{fmt(g['event_exact_rate'], True)} | {fmt(g.get('step_match_rate'))} | "
            f"{g['distinct_outputs_first_40_tokens']}/{s['validation_programs']} | "
            f"{g['full_program_exact_matches']}/{g['single_chunk_programs']} |")
    lines += ["", "## Teacher-forced on step-free targets (DIAGNOSTIC ONLY, not comparable)", "",
              f"_{s['teacher_forced_note']}_", "",
              "| Checkpoint | TF loss (micro) | TF acc excl EOS (micro) | content acc excl UNK (macro) | STEP-value acc |",
              "|---|---|---|---|---|"]
    for name, r in s["checkpoints"].items():
        a, m = r["tf_v2_diagnostic"]["tf_all"]["micro"], r["tf_v2_diagnostic"]["tf_all"]["macro_over_programs"]
        lines.append(f"| {name} | {fmt(a['loss_incl_eos'])} | {fmt(a['token_acc_excl_eos'], True)} | "
                     f"{fmt(m['content_token_acc_excl_unk'], True)} | {fmt(a['step_value_acc_excl_unk'])} |")
    (OUT_DIR / "summary.md").write_text("\n".join(lines) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""
Diagnostic validation evaluation of existing EST checkpoints (protocol eval_validation_v1).

Protocol (validation split only; the test split is never opened):
  TF-all    Teacher-forced pass over every validation chunk (max_chunk_size=1024, as in training).
            Reported chunk-micro (all tokens pooled) and program-macro (per-program rates, then
            unweighted mean over the 56 programs).
  TF-first  The same statistics restricted to chunk 0 of each program (56 chunks).
  GEN-first Greedy generation for chunk 0 of each program. Start [BOS], argmax each step, stop at
            EOS or after MAX_NEW_TOKENS=1025 tokens (largest chunk 1024 + EOS). Same cap for every
            example; no repetition penalty, sampling or constraints. Batch size 8.
            Only chunk 0 is generated: every chunk of a program has identical encoder input and an
            identical [BOS] decoder start, so generating chunk k>0 is undefined in this design.
            "Full-program exact match" is reported only for programs whose trace is one chunk.
  Source    Each checkpoint uses its own configured max_src_length; longer sources are truncated
            (as all earlier scripts did) and the affected programs are listed.

Writes to results/eval_validation_v1/ (one JSON per checkpoint + summary.json);
scripts/summarize_validation_evaluation.py renders summary.md from summary.json.
Checkpoints are loaded read-only (torch.load, map_location=cpu) and never saved.
"""

import argparse
import hashlib
import json
import subprocess
import sys
import time
from collections import defaultdict
from pathlib import Path

import torch
import torch.nn.functional as F

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from evaluation import (  # noqa: E402
    aggregate_tf, check_compatibility, generation_metrics, greedy_generate, macro_average,
    teacher_forced_counts,
)
from execution_dataset import ExecutionTraceDataset  # noqa: E402
from model import ExecutionSemanticTransformer, ModelConfig  # noqa: E402
from tokenize_and_analyze import tokenize_runtime  # noqa: E402
from vocabulary import Vocabulary  # noqa: E402

VAL_JSONL = ROOT / "Module3_Training_Dataset" / "split" / "validation.jsonl"
TRAIN_JSONL = ROOT / "Module3_Training_Dataset" / "split" / "train.jsonl"
JAVA_DIR = ROOT / "selected source code"
VOCAB_DIR = ROOT / "vocabularies"
OUT_DIR = ROOT / "results" / "eval_validation_v1"
MAX_NEW_TOKENS = 1025
TF_BATCH = 2  # batch 4 at T=1025 is slower per chunk on a 4 GB GPU (memory pressure)
GEN_BATCH = 8

# Training-history labels (from the audit). None of these is a fair comparison of strategies.
CHECKPOINTS = {
    "random_init_reference": {
        "path": None,
        "history": "Untrained model (seed 42, pilot-size config, max_src 4096). Floor reference.",
        "validation_contamination": "none",
    },
    "exp1_best": {
        "path": "checkpoints/exp1/checkpoint_best.pt",
        "history": "From scratch on CPU; 373 AdamW steps x batch 2 = ~746 chunks; programs and val set unknown.",
        "validation_contamination": "unknown (original validation set unrecoverable)",
    },
    "pilot_mps_best": {
        "path": "checkpoints/pilot_mps/checkpoint_best.pt",
        "history": "From scratch on MPS; 50 steps on 90 chunks of 10 train programs.",
        "validation_contamination": "selected on 5 chunks of validation programs p00160, p00334",
    },
    "multi_example_pilot_v2": {
        "path": "checkpoints/multi_example_pilot_v2/checkpoint.pt",
        "history": "pilot_mps + 100 steps on 24 short train chunks.",
        "validation_contamination": "monitored on 8 short chunks of 8 validation programs",
    },
    "validation_monitored_v1_best": {
        "path": "checkpoints/validation_monitored_v1/checkpoint_best.pt",
        "history": "pilot_mps + 200 steps on 24 short train chunks.",
        "validation_contamination": "best step selected on 8 short chunks of 8 validation programs",
    },
    "multi_example_overfit_v1_best": {
        "path": "checkpoints/multi_example_overfit_v1/checkpoint_best.pt",
        "history": "pilot_mps + 300 steps on 8 short train chunks (memorization probe).",
        "validation_contamination": "inherits pilot_mps selection",
    },
    "multi_example_overfit_v2_final": {
        "path": "checkpoints/multi_example_overfit_v2/checkpoint_final.pt",
        "history": "pilot_mps + 1000 steps on 8 short train chunks (memorization probe).",
        "validation_contamination": "inherits pilot_mps selection",
    },
}


def sha256_bytes(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def vocab_provenance(tgt_vocab) -> dict:
    """Vocabulary files vs git HEAD (CRLF-normalized) and the train-split fingerprint."""
    out = {}
    for name in ("vocab_source.json", "vocab_target.json"):
        local = (VOCAB_DIR / name).read_bytes().replace(b"\r\n", b"\n")
        try:
            head = subprocess.run(["git", "show", f"HEAD:vocabularies/{name}"], cwd=ROOT,
                                  capture_output=True, check=True).stdout
            out[name] = {"sha256_lf": sha256_bytes(local), "matches_git_head": sha256_bytes(head) == sha256_bytes(local)}
        except Exception as e:  # git unavailable
            out[name] = {"sha256_lf": sha256_bytes(local), "matches_git_head": None, "error": str(e)}
    train_lf = TRAIN_JSONL.read_bytes().replace(b"\r\n", b"\n")
    out["train_fingerprint_in_vocab"] = tgt_vocab.metadata.get("train_fingerprint")
    out["train_jsonl_sha256_lf"] = sha256_bytes(train_lf)
    out["train_fingerprint_matches"] = out["train_fingerprint_in_vocab"] == out["train_jsonl_sha256_lf"]
    return out


def load_validation(src_vocab, tgt_vocab):
    """Materializes all validation chunks as id lists (token strings kept only for chunk 0)."""
    ds = ExecutionTraceDataset(VAL_JSONL, JAVA_DIR, src_vocab, tgt_vocab, max_chunk_size=1024)
    items = []
    sources = {}
    for i in range(len(ds)):
        it = ds[i]
        sources.setdefault(it.program_id, it.source_ids)
        items.append({
            "pid": it.program_id, "chunk_index": it.chunk_index, "total_chunks": it.total_chunks,
            "dec": it.decoder_input_ids, "tgt": it.target_ids,
        })
    # Reconstruction semantics: concatenated reference chunks must equal the full serialized trace.
    chunks_by_pid = defaultdict(list)
    for it in items:
        chunks_by_pid[it["pid"]].append(it["tgt"][:-1])
    recon_ok = {}
    with open(VAL_JSONL, encoding="utf-8") as f:
        for line in f:
            rec = json.loads(line)
            full = tgt_vocab.encode(tokenize_runtime(rec["runtime_trace"]))
            recon_ok[rec["program_id"]] = [t for c in chunks_by_pid[rec["program_id"]] for t in c] == full
    return ds, items, sources, recon_ok


def build_model(name, spec, src_vocab, tgt_vocab, device):
    if spec["path"] is None:
        torch.manual_seed(42)
        cfg = ModelConfig(src_vocab_size=len(src_vocab), tgt_vocab_size=len(tgt_vocab), d_model=128, nhead=4,
                          num_encoder_layers=2, num_decoder_layers=2, dim_feedforward=256,
                          max_src_length=4096, max_tgt_length=2048)
        model = ExecutionSemanticTransformer(cfg)
        meta = {"checkpoint_keys": None}
    else:
        ckpt = torch.load(ROOT / spec["path"], map_location="cpu", weights_only=False)
        cfg = ModelConfig.from_dict(ckpt["model_config"])
        model = ExecutionSemanticTransformer(cfg)
        model.load_state_dict(ckpt["model_state_dict"], strict=True)
        meta = {"checkpoint_keys": sorted(ckpt.keys())}
    meta["compatibility"] = check_compatibility(model.state_dict(), cfg.to_dict(), src_vocab, tgt_vocab)
    meta["model_config"] = cfg.to_dict()
    return model.to(device).eval(), cfg, meta


def _bucket(n, cap):
    """Round up to a multiple of 128 (capped) so few distinct tensor shapes reach the GPU allocator.
    Extra positions are PAD and masked, so results are unchanged apart from float rounding."""
    return min(cap, -(-n // 128) * 128)


def collate(rows, sources, max_src, src_pad, tgt_pad, device, max_tgt=2048):
    srcs = [sources[r["pid"]][:max_src] for r in rows]
    S = _bucket(max(len(s) for s in srcs), max_src)
    T = _bucket(max(len(r["dec"]) for r in rows), max_tgt)
    src = torch.full((len(rows), S), src_pad, dtype=torch.long)
    dec = torch.full((len(rows), T), tgt_pad, dtype=torch.long)
    for i, (s, r) in enumerate(zip(srcs, rows)):
        src[i, :len(s)] = torch.tensor(s)
        dec[i, :len(r["dec"])] = torch.tensor(r["dec"])
    return src.to(device), dec.to(device)


def teacher_forced(model, cfg, items, sources, tgt_vocab, device):
    struct = set(range(4, 12))
    per_chunk = []
    order = sorted(range(len(items)), key=lambda i: len(items[i]["dec"]))
    with torch.no_grad():
        for b in range(0, len(order), TF_BATCH):
            if b % 2000 == 0:
                print(f"  teacher-forced {b}/{len(order)} chunks", flush=True)
            rows = [items[i] for i in order[b:b + TF_BATCH]]
            src, dec = collate(rows, sources, cfg.max_src_length, cfg.src_pad_id, cfg.tgt_pad_id, device)
            src_mask, dec_mask = src.eq(cfg.src_pad_id), dec.eq(cfg.tgt_pad_id)
            logits = model(src, dec, src_mask, dec_mask)
            # Per-position NLL = logsumexp - target logit (equal to cross-entropy, without the
            # transposed copy). PAD positions are computed but sliced away below (only [:n] is used).
            tgt = _targets(rows, dec, cfg)
            nll = torch.logsumexp(logits, dim=-1) - logits.gather(-1, tgt.unsqueeze(-1)).squeeze(-1)
            pred = logits.argmax(-1)
            del logits
            for i, r in enumerate(rows):
                n = len(r["tgt"])
                c = teacher_forced_counts(r["tgt"], pred[i, :n].tolist(), nll[i, :n].tolist(),
                                          tgt_vocab.eos_id, tgt_vocab.unk_id, struct,
                                          tgt_vocab.step_id, tgt_vocab.escape_id)
                per_chunk.append((r["pid"], r["chunk_index"], c))
    return per_chunk


def _targets(rows, dec, cfg):
    tgt = torch.full_like(dec, cfg.tgt_pad_id)
    for i, r in enumerate(rows):
        tgt[i, :len(r["tgt"])] = torch.tensor(r["tgt"], device=dec.device)
    return tgt


def summarize_tf(per_chunk, select=lambda pid, ci: True):
    pooled, by_pid = defaultdict(float), defaultdict(lambda: defaultdict(float))
    for pid, ci, c in per_chunk:
        if not select(pid, ci):
            continue
        for k, v in c.items():
            pooled[k] += v
            by_pid[pid][k] += v
    per_program = {pid: aggregate_tf(c) for pid, c in by_pid.items()}
    return {"micro": aggregate_tf(pooled), "macro_over_programs": macro_average(list(per_program.values())),
            "programs": len(per_program)}, per_program


def generate_first(model, cfg, items, sources, tgt_vocab, device):
    firsts = [it for it in items if it["chunk_index"] == 0]
    results = []
    for b in range(0, len(firsts), GEN_BATCH):
        rows = firsts[b:b + GEN_BATCH]
        src, _ = collate(rows, sources, cfg.max_src_length, cfg.src_pad_id, cfg.tgt_pad_id, device)
        outs = greedy_generate(model, src, src.eq(cfg.src_pad_id), tgt_vocab.bos_id, tgt_vocab.eos_id, MAX_NEW_TOKENS)
        for r, (pred_ids, eos) in zip(rows, outs):
            ref_ids = r["tgt"][:-1]
            m = generation_metrics(ref_ids, pred_ids, tgt_vocab.decode(ref_ids), tgt_vocab.decode(pred_ids), eos)
            m.update({"program_id": r["pid"], "total_chunks": r["total_chunks"],
                      "full_program_exact_match": m["exact_match"] if r["total_chunks"] == 1 else None,
                      "pred_head": tgt_vocab.decode(pred_ids[:40])})
            results.append(m)
    agg = macro_average([{k: v for k, v in m.items() if k not in ("pred_head",)} for m in results])
    single = [m for m in results if m["total_chunks"] == 1]
    agg["single_chunk_programs"] = len(single)
    agg["full_program_exact_matches"] = sum(m["exact_match"] for m in single)
    return agg, results


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--only", nargs="*", default=None, help="subset of checkpoint names")
    args = parser.parse_args()
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    src_vocab = Vocabulary.load(VOCAB_DIR / "vocab_source.json")
    tgt_vocab = Vocabulary.load(VOCAB_DIR / "vocab_target.json")
    provenance = vocab_provenance(tgt_vocab)
    print("Vocabulary provenance:", json.dumps(provenance, indent=1))

    t0 = time.time()
    _, items, sources, recon_ok = load_validation(src_vocab, tgt_vocab)
    print(f"Validation: {len(items)} chunks, {len(sources)} programs, loaded in {time.time() - t0:.0f}s; "
          f"reference reconstruction ok for {sum(recon_ok.values())}/{len(recon_ok)} programs")

    names = args.only or list(CHECKPOINTS)
    summary = {"protocol": "eval_validation_v1", "device": str(device), "torch": torch.__version__,
               "max_new_tokens": MAX_NEW_TOKENS, "vocab_provenance": provenance,
               "validation_chunks": len(items), "validation_programs": len(sources),
               "reference_reconstruction_ok": f"{sum(recon_ok.values())}/{len(recon_ok)}", "checkpoints": {}}

    for name in names:
        spec = CHECKPOINTS[name]
        t0 = time.time()
        model, cfg, meta = build_model(name, spec, src_vocab, tgt_vocab, device)
        if not meta["compatibility"]["hard_checks_pass"]:
            print(f"SKIP {name}: incompatible {meta['compatibility']}")
            summary["checkpoints"][name] = {"skipped": "incompatible", **meta}
            continue
        truncated = sorted(p for p, s in sources.items() if len(s) > cfg.max_src_length)
        per_chunk = teacher_forced(model, cfg, items, sources, tgt_vocab, device)
        tf_all, tf_all_prog = summarize_tf(per_chunk)
        tf_first, _ = summarize_tf(per_chunk, lambda pid, ci: ci == 0)
        gen_agg, gen_rows = generate_first(model, cfg, items, sources, tgt_vocab, device)
        record = {"history": spec["history"], "validation_contamination": spec["validation_contamination"],
                  "path": spec["path"], **meta, "source_truncated_programs": truncated,
                  "tf_all": tf_all, "tf_first": tf_first, "gen_first": gen_agg,
                  "seconds": time.time() - t0}
        (OUT_DIR / f"{name}.json").write_text(json.dumps(
            {**record, "tf_all_per_program": tf_all_prog, "gen_first_per_program": gen_rows}, indent=1))
        summary["checkpoints"][name] = record
        print(f"{name}: TF-all loss {tf_all['micro']['loss_incl_eos']:.3f} acc {tf_all['micro']['token_acc_excl_eos']:.3f} | "
              f"GEN exact {gen_agg.get('exact_match_rate')} EOS {gen_agg.get('eos_reached_rate')} "
              f"grammar {gen_agg.get('grammar_valid_rate')} ({record['seconds']:.0f}s)", flush=True)
        del model
        if device.type == "cuda":
            torch.cuda.empty_cache()

    (OUT_DIR / "summary.json").write_text(json.dumps(summary, indent=1))
    print(f"Saved to {OUT_DIR}")


if __name__ == "__main__":
    main()

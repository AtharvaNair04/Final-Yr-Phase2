#!/usr/bin/env python3
"""
Verifies the chunk cache against ExecutionTraceDataset on real data (read-only; trains nothing).

  1. Every train and validation chunk, in index order: all model-relevant IDs and metadata equal.
     Per-program chunk counts equal; program order equal; zero train/validation overlap.
  2. Collated batches (consecutive groups of 4) identical: values, dtypes, shapes, masks.
  3. Validation with max_source_length=1024 (the limit of the Mac checkpoints): identical truncation.
  4. Teacher-forced evaluation of exp1 on the validation cache, using the committed evaluation
     driver's functions, compared with results/eval_validation_v1/exp1_best.json.
  5. Throughput: random-access loading (cache vs original) and a shuffled DataLoader pass.

Writes results/chunk_cache_verification_v1/report.json.
"""

import json
import random
import sys
import time
from collections import Counter
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / "scripts"))

from chunk_cache import CachedExecutionTraceDataset  # noqa: E402
from execution_dataset import ExecutionTraceDataset, create_dataloader, execution_trace_collate_fn  # noqa: E402
from vocabulary import Vocabulary  # noqa: E402
import run_validation_evaluation as rve  # noqa: E402  (committed evaluation driver; functions reused unchanged)

SPLITS = {"train": "train.jsonl", "validation": "validation.jsonl"}
CACHE_ROOT = ROOT / "data_cache" / "chunks_v1"
JAVA_DIR = ROOT / "selected source code"
OUT_DIR = ROOT / "results" / "chunk_cache_verification_v1"
ID_FIELDS = ("program_id", "chunk_index", "total_chunks", "source_ids", "chunk_ids", "decoder_input_ids", "target_ids")
BATCH_TENSORS = ("chunk_indices", "total_chunks", "src_ids", "src_padding_mask", "decoder_input_ids",
                 "target_ids", "target_padding_mask", "loss_mask", "src_lengths", "target_lengths")
# exp1 tolerance: identical IDs, batching and kernels give bit-identical results in practice; the
# bound allows for non-deterministic CUDA reduction order (float32 logsumexp, ~1e-7 relative per
# token) accumulated in float64 over ~11M tokens.
LOSS_TOL = 1e-5
ACC_TOL = 1e-6


def datasets(split, src_vocab, tgt_vocab, max_source_length=None):
    jsonl = ROOT / "Module3_Training_Dataset" / "split" / SPLITS[split]
    orig = ExecutionTraceDataset(jsonl, JAVA_DIR, src_vocab, tgt_vocab, max_chunk_size=1024,
                                 max_source_length=max_source_length)
    cached = CachedExecutionTraceDataset(CACHE_ROOT / split, jsonl, JAVA_DIR, src_vocab, tgt_vocab,
                                         max_source_length=max_source_length)
    return orig, cached


def compare_all(orig, cached, check_batches=True):
    res = {"chunks_original": len(orig), "chunks_cache": len(cached),
           "program_order_equal": orig.program_ids == cached.program_ids,
           "chunks_per_record_equal": orig.chunks_per_record == cached.chunks_per_record,
           "item_mismatches": 0, "batch_mismatches": 0, "first_mismatch": None}
    counts_o, counts_c = Counter(), Counter()
    group_o, group_c = [], []
    for i in range(len(orig)):
        a, b = orig[i], cached[i]
        counts_o[a.program_id] += 1
        counts_c[b.program_id] += 1
        if any(getattr(a, f) != getattr(b, f) for f in ID_FIELDS):
            res["item_mismatches"] += 1
            res["first_mismatch"] = res["first_mismatch"] or i
        if check_batches:
            group_o.append(a)
            group_c.append(b)
            if len(group_o) == 4 or i == len(orig) - 1:
                x = execution_trace_collate_fn(group_o, 0, 0)
                y = execution_trace_collate_fn(group_c, 0, 0)
                same = x.program_ids == y.program_ids and all(
                    getattr(x, f).dtype == getattr(y, f).dtype and torch.equal(getattr(x, f), getattr(y, f))
                    for f in BATCH_TENSORS)
                res["batch_mismatches"] += 0 if same else 1
                group_o, group_c = [], []
    res["per_program_counts_equal"] = counts_o == counts_c and all(
        counts_o[p] == n for p, n in zip(orig.program_ids, orig.chunks_per_record) if n)
    res["pass"] = (res["chunks_original"] == res["chunks_cache"] and res["program_order_equal"]
                   and res["chunks_per_record_equal"] and res["item_mismatches"] == 0
                   and res["batch_mismatches"] == 0 and res["per_program_counts_equal"])
    return res


def exp1_teacher_forced(cached_val, src_vocab, tgt_vocab):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    items = [{"pid": it.program_id, "chunk_index": it.chunk_index, "total_chunks": it.total_chunks,
              "dec": it.decoder_input_ids, "tgt": it.target_ids} for it in (cached_val[i] for i in range(len(cached_val)))]
    sources = {}
    for it in items:
        if it["pid"] not in sources:
            r = cached_val.program_ids.index(it["pid"])
            sources[it["pid"]] = cached_val._source(r)
    model, cfg, _ = rve.build_model("exp1_best", rve.CHECKPOINTS["exp1_best"], src_vocab, tgt_vocab, device)
    per_chunk = rve.teacher_forced(model, cfg, items, sources, tgt_vocab, device)
    tf_all, _ = rve.summarize_tf(per_chunk)
    tf_first, _ = rve.summarize_tf(per_chunk, lambda pid, ci: ci == 0)
    ref = json.loads((ROOT / "results" / "eval_validation_v1" / "exp1_best.json").read_text())
    diffs = {}
    for block in ("tf_all", "tf_first"):
        new = {"tf_all": tf_all, "tf_first": tf_first}[block]
        for agg in ("micro", "macro_over_programs"):
            for key, old in ref[block][agg].items():
                val = new[agg].get(key)
                if isinstance(old, (int, float)) and isinstance(val, (int, float)):
                    diffs[f"{block}.{agg}.{key}"] = abs(val - old)
    worst_loss = max(v for k, v in diffs.items() if ".loss" in k)
    worst_other = max(v for k, v in diffs.items() if ".loss" not in k and not k.endswith("tokens_incl_eos"))
    tokens_equal = all(v == 0 for k, v in diffs.items() if k.endswith("tokens_incl_eos"))
    # Keys measured now but absent from the stored file (renamed UNK-aware metrics) are listed, not compared.
    return {"max_abs_diff_loss": worst_loss, "max_abs_diff_rates": worst_other, "token_counts_equal": tokens_equal,
            "compared_metrics": len(diffs), "loss_tolerance": LOSS_TOL, "rate_tolerance": ACC_TOL,
            "new_tf_all_micro": tf_all["micro"],
            "pass": worst_loss <= LOSS_TOL and worst_other <= ACC_TOL and tokens_equal}


def throughput(orig_train, cached_train, device):
    rng = random.Random(0)
    idx = [rng.randrange(len(cached_train)) for _ in range(1000)]
    t0 = time.perf_counter()
    for i in idx:
        cached_train[i]
    cache_s = (time.perf_counter() - t0) / len(idx)
    orig_idx = idx[:20]
    t0 = time.perf_counter()
    for i in orig_idx:
        orig_train[i]
    orig_s = (time.perf_counter() - t0) / len(orig_idx)

    torch.manual_seed(0)
    loader = create_dataloader(cached_train, batch_size=2, shuffle=True, num_workers=0)
    n_batches, t0 = 0, time.perf_counter()
    for batch in loader:
        batch = batch.to(device)
        n_batches += 1
        if n_batches == 5000:
            break
    if device.type == "cuda":
        torch.cuda.synchronize()
    loader_s = (time.perf_counter() - t0) / n_batches
    return {"random_access_cache_seconds_per_chunk": cache_s,
            "random_access_original_seconds_per_chunk": orig_s, "original_sample_size": len(orig_idx),
            "speedup": orig_s / cache_s,
            "shuffled_dataloader_batch2_seconds_per_batch_incl_host_to_device": loader_s,
            "dataloader_batches_measured": n_batches,
            "dataloader_projected_minutes_per_epoch": len(loader) * loader_s / 60}


def main():
    src_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_source.json")
    tgt_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_target.json")
    report = {}
    pairs = {}
    for split in ("validation", "train"):
        t0 = time.perf_counter()
        orig, cached = datasets(split, src_vocab, tgt_vocab)
        pairs[split] = (orig, cached)
        report[split] = compare_all(orig, cached)
        report[split]["seconds"] = time.perf_counter() - t0
        print(split, report[split], flush=True)

    o1024, c1024 = datasets("validation", src_vocab, tgt_vocab, max_source_length=1024)
    report["validation_truncated_1024"] = compare_all(o1024, c1024, check_batches=True)
    report["validation_truncated_1024"]["programs_truncated"] = sum(
        len(pairs["validation"][1]._source(r)) > 1024 for r in range(len(pairs["validation"][1].program_ids)))
    print("validation_truncated_1024", report["validation_truncated_1024"], flush=True)

    overlap = set(pairs["train"][1].program_ids) & set(pairs["validation"][1].program_ids)
    report["train_validation_overlap"] = len(overlap)

    report["exp1_teacher_forced"] = exp1_teacher_forced(pairs["validation"][1], src_vocab, tgt_vocab)
    print("exp1", {k: v for k, v in report["exp1_teacher_forced"].items() if k != "new_tf_all_micro"}, flush=True)

    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    report["throughput"] = throughput(pairs["train"][0], pairs["train"][1], device)
    print("throughput", report["throughput"], flush=True)

    report["all_pass"] = all([report["validation"]["pass"], report["train"]["pass"],
                              report["validation_truncated_1024"]["pass"], report["train_validation_overlap"] == 0,
                              report["exp1_teacher_forced"]["pass"]])
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    (OUT_DIR / "report.json").write_text(json.dumps(report, indent=1))
    print("ALL PASS" if report["all_pass"] else "FAILURES PRESENT", flush=True)


if __name__ == "__main__":
    main()

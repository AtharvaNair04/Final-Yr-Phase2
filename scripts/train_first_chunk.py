#!/usr/bin/env python3
"""
Design A: train a fresh EST on chunk 0 of every program (esr_v2_nostep target format).

  python scripts/train_first_chunk.py --check-only     # pre-training checks only
  python scripts/train_first_chunk.py                  # checks + training

Data: data_cache/chunks_v2_nostep (verified), chunk_index == 0 only: exactly 449 train / 56 validation
examples. The test split is never opened.
Model: the existing small configuration (d_model 128, 4 heads, 2+2 layers, FF 256, dropout 0.1),
max_src_length 4096 (no source truncation), max_tgt_length 2048. Trained from scratch.
Training: existing TrainingManager unchanged; AdamW lr 5e-4, weight decay 0.01, clip 1.0, batch 2,
fixed budget of --epochs epochs, seed 42; validation loss every epoch; checkpoint_best.pt = lowest
validation loss. Outputs in checkpoints/design_a_first_chunk_v1/ (refuses to reuse an existing directory).
"""

import argparse
import json
import logging
import platform
import sys
import time
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from chunk_cache import CachedExecutionTraceDataset  # noqa: E402
from execution_dataset import create_dataloader  # noqa: E402
from first_chunk import FirstChunkDataset, assert_disjoint  # noqa: E402
from model import ExecutionSemanticTransformer, ModelConfig  # noqa: E402
from target_format import TARGET_FORMAT_V2  # noqa: E402
from train import TrainingConfig, TrainingManager, set_seed  # noqa: E402
from vocabulary import Vocabulary  # noqa: E402

CACHE = ROOT / "data_cache" / "chunks_v2_nostep"
SPLIT_DIR = ROOT / "Module3_Training_Dataset" / "split"
JAVA_DIR = ROOT / "selected source code"
RUN_DIR = ROOT / "checkpoints" / "design_a_first_chunk_v1"
EXPECTED = {"train": 449, "validation": 56}


def load_first_chunks(split, src_vocab, tgt_vocab):
    base = CachedExecutionTraceDataset(CACHE / split, SPLIT_DIR / f"{split}.jsonl", JAVA_DIR, src_vocab, tgt_vocab,
                                       target_format=TARGET_FORMAT_V2)
    return FirstChunkDataset(base)


def pretraining_checks(train_ds, val_ds, model_cfg):
    checks = {}
    for name, ds in (("train", train_ds), ("validation", val_ds)):
        items = [ds[i] for i in range(len(ds))]
        src = [len(it.source_ids) for it in items]
        tgt = [len(it.target_ids) for it in items]
        checks[name] = {
            "examples": len(ds), "expected": EXPECTED[name],
            "all_chunk_index_zero": all(it.chunk_index == 0 for it in items),
            "source_len": {"min": min(src), "max": max(src), "mean": sum(src) / len(src)},
            "target_len_incl_eos": {"min": min(tgt), "max": max(tgt), "mean": sum(tgt) / len(tgt)},
            "target_tokens": sum(tgt),
            "teacher_forcing_alignment_ok": all(it.decoder_input_ids[1:] == it.target_ids[:-1] for it in items),
        }
    assert_disjoint(train_ds.program_ids, val_ds.program_ids)
    checks["train_validation_overlap"] = 0
    checks["fits_model_limits"] = (max(checks[s]["source_len"]["max"] for s in EXPECTED) <= model_cfg.max_src_length
                                   and max(checks[s]["target_len_incl_eos"]["max"] for s in EXPECTED) <= model_cfg.max_tgt_length)
    checks["pass"] = (all(checks[s]["examples"] == EXPECTED[s] and checks[s]["all_chunk_index_zero"]
                          and checks[s]["teacher_forcing_alignment_ok"] for s in EXPECTED) and checks["fits_model_limits"])
    return checks


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--epochs", type=int, default=40)
    parser.add_argument("--seed", type=int, default=42)
    parser.add_argument("--check-only", action="store_true")
    args = parser.parse_args()

    src_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_source.json")
    tgt_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_target.json")
    train_ds = load_first_chunks("train", src_vocab, tgt_vocab)
    val_ds = load_first_chunks("validation", src_vocab, tgt_vocab)

    model_cfg = ModelConfig(src_vocab_size=len(src_vocab), tgt_vocab_size=len(tgt_vocab), d_model=128, nhead=4,
                            num_encoder_layers=2, num_decoder_layers=2, dim_feedforward=256, dropout=0.1,
                            max_src_length=4096, max_tgt_length=2048,
                            src_pad_id=src_vocab.pad_id, tgt_pad_id=tgt_vocab.pad_id)
    train_cfg = TrainingConfig(epochs=args.epochs, learning_rate=5e-4, weight_decay=0.01, grad_clip_norm=1.0,
                               train_batch_size=2, eval_batch_size=2, checkpoint_dir=str(RUN_DIR),
                               seed=args.seed, device="cuda" if torch.cuda.is_available() else "cpu",
                               save_best_only=False, log_interval=75)

    checks = pretraining_checks(train_ds, val_ds, model_cfg)
    print(json.dumps(checks, indent=1))
    if not checks["pass"]:
        raise SystemExit("Pre-training checks failed; not training.")
    if args.check_only:
        return
    if RUN_DIR.exists():
        raise SystemExit(f"{RUN_DIR} already exists; refusing to overwrite an earlier run.")
    RUN_DIR.mkdir(parents=True)

    run_config = {
        "experiment": "design_a_first_chunk_v1", "target_format": TARGET_FORMAT_V2,
        "data": {"cache": str(CACHE.relative_to(ROOT)), "selection": "chunk_index == 0",
                 "train_examples": len(train_ds), "validation_examples": len(val_ds), "test_split": "not opened"},
        "model_config": model_cfg.to_dict(), "training_config": train_cfg.to_dict(),
        "steps_per_epoch": -(-len(train_ds) // train_cfg.train_batch_size),
        "total_optimizer_steps_budget": args.epochs * -(-len(train_ds) // train_cfg.train_batch_size),
        "model_selection": "lowest validation loss (checkpoint_best.pt)",
        "environment": {"python": platform.python_version(), "torch": torch.__version__,
                        "device": torch.cuda.get_device_name(0) if torch.cuda.is_available() else "cpu"},
        "pretraining_checks": checks,
    }
    (RUN_DIR / "run_config.json").write_text(json.dumps(run_config, indent=1))

    logging.basicConfig(level=logging.INFO, format="%(asctime)s %(message)s",
                        handlers=[logging.FileHandler(RUN_DIR / "train.log", encoding="utf-8"),
                                  logging.StreamHandler(sys.stdout)])
    set_seed(args.seed)  # seed before model construction so initial weights are reproducible
    model = ExecutionSemanticTransformer(model_cfg)
    manager = TrainingManager(model, train_cfg)  # re-seeds; shuffling below uses that state
    train_loader = create_dataloader(train_ds, batch_size=train_cfg.train_batch_size, shuffle=True)
    val_loader = create_dataloader(val_ds, batch_size=train_cfg.eval_batch_size, shuffle=False)

    t0 = time.time()
    history = manager.fit(train_loader, val_loader)
    elapsed = time.time() - t0
    best = min(history, key=lambda r: r["val_loss"])
    summary = {"history": history, "elapsed_seconds": elapsed, "best_epoch": best["epoch"],
               "best_val_loss": best["val_loss"], "final_train_loss": history[-1]["train_loss"],
               "final_val_loss": history[-1]["val_loss"],
               "optimizer_steps": int(manager.optimizer.state_dict()["state"][0]["step"])}
    (RUN_DIR / "training_history.json").write_text(json.dumps(summary, indent=1))
    print(f"Done in {elapsed / 60:.1f} min; best epoch {best['epoch']} val loss {best['val_loss']:.4f}")


if __name__ == "__main__":
    main()

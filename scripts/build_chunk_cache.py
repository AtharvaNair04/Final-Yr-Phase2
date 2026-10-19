#!/usr/bin/env python3
"""
Builds the disk-backed pre-encoded chunk cache (chunk_cache.py) for the requested splits.

  python scripts/build_chunk_cache.py --splits train validation
  python scripts/build_chunk_cache.py --splits train validation --target-format esr_v2_nostep

Output: data_cache/chunks_v1/<split>/ for esr_v1_step, data_cache/chunks_v2_nostep/<split>/ for
esr_v2_nostep (git-ignored). An existing directory is never overwritten.
The test split is refused unless --allow-test is given (it is reserved for final evaluation).
"""

import argparse
import sys
import time
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from chunk_cache import build_chunk_cache  # noqa: E402
from execution_dataset import ExecutionTraceDataset  # noqa: E402
from target_format import TARGET_FORMAT_V1, TARGET_FORMAT_V2, StepFreeExecutionTraceDataset  # noqa: E402
from vocabulary import Vocabulary  # noqa: E402

SPLIT_FILES = {"train": "train.jsonl", "validation": "validation.jsonl", "test": "test.jsonl"}
CACHE_ROOTS = {
    TARGET_FORMAT_V1: ROOT / "data_cache" / "chunks_v1",
    TARGET_FORMAT_V2: ROOT / "data_cache" / "chunks_v2_nostep",
}
DATASET_CLASSES = {TARGET_FORMAT_V1: ExecutionTraceDataset, TARGET_FORMAT_V2: StepFreeExecutionTraceDataset}


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--splits", nargs="+", choices=list(SPLIT_FILES), required=True)
    parser.add_argument("--allow-test", action="store_true", help="required to build the test split")
    parser.add_argument("--target-format", choices=list(CACHE_ROOTS), default=TARGET_FORMAT_V1)
    args = parser.parse_args()
    cache_root = CACHE_ROOTS[args.target_format]
    dataset_cls = DATASET_CLASSES[args.target_format]
    if "test" in args.splits and not args.allow_test:
        parser.error("the test split is reserved for final evaluation; pass --allow-test to build it")

    src_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_source.json")
    tgt_vocab = Vocabulary.load(ROOT / "vocabularies" / "vocab_target.json")
    for split in args.splits:
        t0 = time.perf_counter()
        dataset = dataset_cls(
            ROOT / "Module3_Training_Dataset" / "split" / SPLIT_FILES[split],
            ROOT / "selected source code", src_vocab, tgt_vocab,
            max_chunk_size=1024, oversized_policy="separate", max_source_length=None,
        )
        manifest = build_chunk_cache(dataset, cache_root / split)
        print(f"{split} [{args.target_format}]: {manifest['counts']} in {time.perf_counter() - t0:.0f}s "
              f"-> {cache_root / split}", flush=True)


if __name__ == "__main__":
    main()

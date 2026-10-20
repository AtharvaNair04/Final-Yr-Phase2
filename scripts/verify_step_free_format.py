#!/usr/bin/env python3
"""
Real-data round-trip verification of the esr_v2_nostep target format (read-only).

For every train and validation program:
  1. insert_steps(tokenize_runtime_nostep(trace), 1) == tokenize_runtime(trace)       (v2 -> v1)
  2. strip_steps(tokenize_runtime(trace)) == tokenize_runtime_nostep(trace)           (v1 -> v2)
  3. Chunk-wise: insert_steps_chunks(v2 chunks at max_chunk_size=1024) == tokenize_runtime(trace)
     (each chunk numbered from the number of events before it; 0 oversized events required)
  4. Common comparison window: strip_steps(v1 chunk 0) == first E events of the v2 serialization,
     where E = number of events in the v1 chunk 0 (the window used to compare formats).

Writes results/step_free_format_verification/report.json.
"""

import json
import sys
import time
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from chunk_trace import chunk_trace_tokens  # noqa: E402
from target_format import (  # noqa: E402
    count_events, first_n_events, insert_steps, insert_steps_chunks, strip_steps, tokenize_runtime_nostep,
)
from tokenize_and_analyze import tokenize_runtime  # noqa: E402

OUT_DIR = ROOT / "results" / "step_free_format_verification"


def main():
    report = {}
    for split in ("train", "validation"):
        t0 = time.perf_counter()
        s = {"programs": 0, "events": 0, "escaped_STEP_tokens": 0, "oversized_v2": 0,
             "fail_v2_to_v1": [], "fail_v1_to_v2": [], "fail_chunkwise": [], "fail_window": [],
             "multi_chunk_programs_v2": 0}
        with open(ROOT / "Module3_Training_Dataset" / "split" / f"{split}.jsonl", encoding="utf-8") as f:
            for line in f:
                rec = json.loads(line)
                pid, rt = rec["program_id"], rec["runtime_trace"]
                v1, v2 = tokenize_runtime(rt), tokenize_runtime_nostep(rt)
                s["programs"] += 1
                s["events"] += len(rt["events"])
                s["escaped_STEP_tokens"] += sum(1 for a, b in zip(v2, v2[1:]) if a == "\\" and b == "STEP")
                if insert_steps(v2, 1) != v1:
                    s["fail_v2_to_v1"].append(pid)
                if strip_steps(v1) != v2:
                    s["fail_v1_to_v2"].append(pid)
                c2 = chunk_trace_tokens(v2, max_chunk_size=1024, oversized_policy="separate")
                s["oversized_v2"] += len(c2.oversized_events)
                s["multi_chunk_programs_v2"] += c2.chunk_count > 1
                if c2.oversized_events or insert_steps_chunks(c2.chunks) != v1:
                    s["fail_chunkwise"].append(pid)
                c1 = chunk_trace_tokens(v1, max_chunk_size=1024, oversized_policy="separate")
                if c1.chunks:
                    window = count_events(c1.chunks[0])
                    if strip_steps(c1.chunks[0]) != first_n_events(v2, window):
                        s["fail_window"].append(pid)
        s["pass"] = not (s["fail_v2_to_v1"] or s["fail_v1_to_v2"] or s["fail_chunkwise"] or s["fail_window"]
                         or s["oversized_v2"])
        s["seconds"] = time.perf_counter() - t0
        report[split] = s
        print(split, s, flush=True)
    report["all_pass"] = all(report[s]["pass"] for s in ("train", "validation"))
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    (OUT_DIR / "report.json").write_text(json.dumps(report, indent=1))
    print("ALL PASS" if report["all_pass"] else "FAILURES PRESENT", flush=True)


if __name__ == "__main__":
    main()

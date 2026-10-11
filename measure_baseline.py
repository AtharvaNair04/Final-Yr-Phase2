"""
Baseline length measurement and candidate chunk size evaluation for Module 4, Part 3.
Processes records streaming through train, validation, and test splits without modifying any files.
"""
import json
import os
import sys
import statistics

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
SPLIT_DIR = os.path.join(BASE_DIR, "Module3_Training_Dataset", "split")
SPLITS = {
    "TRAIN": "train.jsonl",
    "VALIDATION": "validation.jsonl",
    "TEST": "test.jsonl"
}

from tokenize_and_analyze import tokenize_runtime, percentile

CANDIDATES = [512, 1024, 2048, 4096]


def get_event_lengths_from_tokens(tokens):
    """Returns list of token counts for each event in the token stream."""
    event_lens = []
    curr_len = 0
    in_event = False
    for tok in tokens:
        if tok == "<EVENT_START>":
            curr_len = 1
            in_event = True
        elif tok == "<EVENT_END>":
            if in_event:
                curr_len += 1
                event_lens.append(curr_len)
                in_event = False
                curr_len = 0
        else:
            if in_event:
                curr_len += 1
    return event_lens


def simulate_chunking_from_lens(ev_lens, max_chunk_size):
    """Simulates packing complete events into chunks of at most max_chunk_size."""
    chunks = []
    oversized = []
    curr_chunk_len = 0

    for ev_len in ev_lens:
        if ev_len > max_chunk_size:
            oversized.append(ev_len)
            continue

        if curr_chunk_len + ev_len <= max_chunk_size:
            curr_chunk_len += ev_len
        else:
            if curr_chunk_len > 0:
                chunks.append(curr_chunk_len)
            curr_chunk_len = ev_len

    if curr_chunk_len > 0:
        chunks.append(curr_chunk_len)

    return chunks, oversized


def analyze_split(split_name, filename):
    path = os.path.join(SPLIT_DIR, filename)
    records = 0
    empty_traces = 0
    trace_lengths = []
    event_counts = []
    
    event_len_min = float("inf")
    event_len_max = 0
    event_len_sum = 0
    total_events = 0
    
    ev_exceeding_candidates = {c: 0 for c in CANDIDATES}
    
    candidate_stats = {
        c: {
            "total_chunks": 0,
            "oversized": 0,
            "traces_with_oversized": 0,
            "chunk_lens": []
        }
        for c in CANDIDATES
    }

    print(f"\nProcessing {split_name} ({filename})...", flush=True)

    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            rec = json.loads(line)
            records += 1
            rt = rec.get("runtime_trace", {})

            tokens = tokenize_runtime(rt)
            t_len = len(tokens)
            trace_lengths.append(t_len)
            if t_len == 0:
                empty_traces += 1

            ev_lens = get_event_lengths_from_tokens(tokens)
            del tokens

            ev_count = len(ev_lens)
            event_counts.append(ev_count)
            total_events += ev_count

            for elen in ev_lens:
                if elen < event_len_min:
                    event_len_min = elen
                if elen > event_len_max:
                    event_len_max = elen
                event_len_sum += elen
                for c in CANDIDATES:
                    if elen > c:
                        ev_exceeding_candidates[c] += 1

            for c in CANDIDATES:
                chk_lens, ov_lens = simulate_chunking_from_lens(ev_lens, c)
                candidate_stats[c]["total_chunks"] += len(chk_lens)
                candidate_stats[c]["oversized"] += len(ov_lens)
                if ov_lens:
                    candidate_stats[c]["traces_with_oversized"] += 1
                candidate_stats[c]["chunk_lens"].extend(chk_lens)

            if records % 50 == 0:
                print(f"  Processed {records} records...", flush=True)

    print(f"Finished {split_name}: {records} records.", flush=True)

    return {
        "records": records,
        "empty_traces": empty_traces,
        "trace_lengths": trace_lengths,
        "event_counts": event_counts,
        "total_events": total_events,
        "event_len_min": event_len_min if total_events else 0,
        "event_len_max": event_len_max if total_events else 0,
        "event_len_mean": (event_len_sum / total_events) if total_events else 0,
        "ev_exceeding": ev_exceeding_candidates,
        "candidate_stats": candidate_stats
    }


def print_split_results(name, res):
    tl = res["trace_lengths"]
    ec = res["event_counts"]
    n = res["records"]
    print(f"\n{'='*75}")
    print(f"SPLIT RESULTS: {name} (Records: {n})")
    print(f"{'='*75}")
    print(f"Empty runtime traces : {res['empty_traces']}")
    print(f"Total runtime tokens : {sum(tl):,}")
    print(f"Total events parsed  : {res['total_events']:,}")

    print("\n--- Runtime Token Length Distribution (Whole Trace) ---")
    print(f"Minimum   : {min(tl):,}")
    print(f"Mean      : {statistics.mean(tl):,.2f}")
    print(f"Median    : {statistics.median(tl):,.2f}")
    print(f"Maximum   : {max(tl):,}")
    print(f"90th %ile : {percentile(tl, 0.90):,.2f}")
    print(f"95th %ile : {percentile(tl, 0.95):,.2f}")
    print(f"99th %ile : {percentile(tl, 0.99):,.2f}")

    print("\n--- Event Count Distribution (Events per Trace) ---")
    print(f"Minimum   : {min(ec):,}")
    print(f"Mean      : {statistics.mean(ec):,.2f}")
    print(f"Median    : {statistics.median(ec):,.2f}")
    print(f"Maximum   : {max(ec):,}")
    print(f"90th %ile : {percentile(ec, 0.90):,.2f}")
    print(f"95th %ile : {percentile(ec, 0.95):,.2f}")
    print(f"99th %ile : {percentile(ec, 0.99):,.2f}")

    print("\n--- Individual Event Token Length Distribution ---")
    print(f"Minimum   : {res['event_len_min']:,}")
    print(f"Mean      : {res['event_len_mean']:,.2f}")
    print(f"Maximum   : {res['event_len_max']:,}")

    print("\n--- Candidate Chunk Size Thresholds ---")
    print(f"{'Chunk Size':<12} | {'Traces > Limit':<18} | {'% Traces':<10} | {'Events > Limit':<16} | {'% Events':<10}")
    print("-" * 75)
    for c in CANDIDATES:
        exceeding_traces = sum(1 for x in tl if x > c)
        pct_traces = (exceeding_traces / n) * 100 if n else 0
        exceeding_events = res["ev_exceeding"][c]
        pct_events = (exceeding_events / res["total_events"]) * 100 if res["total_events"] else 0
        print(f"{c:<12} | {exceeding_traces:<18,} | {pct_traces:<9.2f}% | {exceeding_events:<16,} | {pct_events:<9.4f}%")


def main():
    splits_results = {}
    combined = {
        "records": 0,
        "empty_traces": 0,
        "trace_lengths": [],
        "event_counts": [],
        "total_events": 0,
        "event_len_min": float("inf"),
        "event_len_max": 0,
        "event_len_sum": 0,
        "ev_exceeding": {c: 0 for c in CANDIDATES},
        "candidate_stats": {
            c: {
                "total_chunks": 0,
                "oversized": 0,
                "traces_with_oversized": 0,
                "chunk_lens": []
            }
            for c in CANDIDATES
        }
    }

    for split_name, fname in SPLITS.items():
        res = analyze_split(split_name, fname)
        splits_results[split_name] = res
        print_split_results(split_name, res)

        combined["records"] += res["records"]
        combined["empty_traces"] += res["empty_traces"]
        combined["trace_lengths"].extend(res["trace_lengths"])
        combined["event_counts"].extend(res["event_counts"])
        combined["total_events"] += res["total_events"]
        if res["event_len_min"] < combined["event_len_min"]:
            combined["event_len_min"] = res["event_len_min"]
        if res["event_len_max"] > combined["event_len_max"]:
            combined["event_len_max"] = res["event_len_max"]
        combined["event_len_sum"] += res["event_len_mean"] * res["total_events"]
        for c in CANDIDATES:
            combined["ev_exceeding"][c] += res["ev_exceeding"][c]
            c_stat = combined["candidate_stats"][c]
            r_stat = res["candidate_stats"][c]
            c_stat["total_chunks"] += r_stat["total_chunks"]
            c_stat["oversized"] += r_stat["oversized"]
            c_stat["traces_with_oversized"] += r_stat["traces_with_oversized"]
            c_stat["chunk_lens"].extend(r_stat["chunk_lens"])

    combined["event_len_mean"] = combined["event_len_sum"] / combined["total_events"] if combined["total_events"] else 0
    print_split_results("COMPLETE DATASET (ALL SPLITS)", combined)

    print(f"\n{'='*75}")
    print("PHASE D: COMPLETE DATASET CHUNK EVALUATION")
    print(f"{'='*75}")
    total_events = combined["total_events"]
    total_traces = combined["records"]

    for c in CANDIDATES:
        c_stat = combined["candidate_stats"][c]
        total_chunks = c_stat["total_chunks"]
        oversized = c_stat["oversized"]
        traces_w_ov = c_stat["traces_with_oversized"]
        ch_lens = c_stat["chunk_lens"]

        pct_events_retained = ((total_events - oversized) / total_events) * 100 if total_events else 0
        pct_traces_retained = ((total_traces - traces_w_ov) / total_traces) * 100 if total_traces else 0

        print(f"\n--- Candidate Chunk Size: {c} Tokens ---")
        print(f"Total Chunks Produced          : {total_chunks:,}")
        print(f"Source Traces Represented      : {total_traces - combined['empty_traces']:,} / {total_traces:,}")
        print(f"Oversized Events (> {c})       : {oversized:,} ({oversized/total_events*100:.5f}% of events)")
        print(f"Traces with Oversized Events   : {traces_w_ov:,} ({traces_w_ov/total_traces*100:.2f}% of traces)")
        print(f"Events Retained in Chunks      : {pct_events_retained:.5f}%")
        print(f"Traces 100% Retained in Chunks : {pct_traces_retained:.2f}%")

        if ch_lens:
            print(f"Chunk Length Distribution:")
            print(f"  Min: {min(ch_lens):,} | Mean: {statistics.mean(ch_lens):,.2f} | Median: {statistics.median(ch_lens):,} | Max: {max(ch_lens):,}")
            print(f"  90th %ile: {percentile(ch_lens, 0.90):,.2f} | 95th %ile: {percentile(ch_lens, 0.95):,.2f} | 99th %ile: {percentile(ch_lens, 0.99):,.2f}")


if __name__ == "__main__":
    main()

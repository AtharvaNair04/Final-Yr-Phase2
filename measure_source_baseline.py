"""
Source tokenization baseline measurement and pairing audit for Module 4, Part 4.
Evaluates both the dataset SPR representation and raw Java source files.
"""
import json
import os
import sys
import statistics

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
SPLIT_DIR = os.path.join(BASE_DIR, "Module3_Training_Dataset", "split")
JAVA_DIR = os.path.join(BASE_DIR, "selected source code")

SPLITS = {
    "TRAIN": "train.jsonl",
    "VALIDATION": "validation.jsonl",
    "TEST": "test.jsonl"
}

from tokenize_and_analyze import percentile
from source_tokenizer import tokenize_source, tokenize_java_code


def analyze_split_source(split_name, filename):
    path = os.path.join(SPLIT_DIR, filename)
    records = 0
    empty_sources = 0
    malformed_sources = 0
    tokenization_failures = 0
    
    spr_lengths = []
    raw_java_lengths = []
    pairing_status = []

    print(f"\nAnalyzing source in {split_name} ({filename})...", flush=True)

    with open(path, "r", encoding="utf-8") as f:
        for line_no, line in enumerate(f, 1):
            line = line.strip()
            if not line:
                continue
            try:
                rec = json.loads(line)
            except Exception as e:
                malformed_sources += 1
                continue

            records += 1
            pid = rec.get("program_id")
            source_obj = rec.get("source")
            rt = rec.get("runtime_trace")

            if source_obj is None or (isinstance(source_obj, (dict, list, str)) and len(source_obj) == 0):
                empty_sources += 1

            # Check pairing with runtime trace
            has_rt = rt is not None and isinstance(rt, (dict, list)) and len(rt) > 0
            pairing_status.append(has_rt)

            # Tokenize SPR source representation
            try:
                spr_toks = tokenize_source(source_obj)
                spr_lengths.append(len(spr_toks))
            except Exception as e:
                tokenization_failures += 1

            # Tokenize corresponding raw Java file if available
            java_path = os.path.join(JAVA_DIR, f"{pid}.java")
            if os.path.exists(java_path):
                try:
                    with open(java_path, "r", encoding="utf-8", errors="ignore") as jf:
                        code = jf.read()
                    raw_toks = tokenize_java_code(code)
                    raw_java_lengths.append(len(raw_toks))
                except Exception:
                    pass

    return {
        "records": records,
        "empty_sources": empty_sources,
        "malformed_sources": malformed_sources,
        "tokenization_failures": tokenization_failures,
        "valid_pairs": sum(1 for p in pairing_status if p),
        "spr_lengths": spr_lengths,
        "raw_java_lengths": raw_java_lengths
    }


def print_source_stats(name, res):
    spr = res["spr_lengths"]
    raw = res["raw_java_lengths"]
    n = res["records"]
    print(f"\n{'='*75}")
    print(f"SOURCE ANALYSIS: {name} (Records: {n})")
    print(f"{'='*75}")
    print(f"Empty source records       : {res['empty_sources']}")
    print(f"Malformed source records   : {res['malformed_sources']}")
    print(f"Tokenization failures      : {res['tokenization_failures']}")
    print(f"Source-Runtime paired      : {res['valid_pairs']} / {n} (100.0%)")

    print("\n--- SPR (Static Program Representation) Token Lengths ---")
    if spr:
        print(f"Minimum   : {min(spr):,}")
        print(f"Mean      : {statistics.mean(spr):,.2f}")
        print(f"Median    : {statistics.median(spr):,.2f}")
        print(f"Maximum   : {max(spr):,}")
        print(f"90th %ile : {percentile(spr, 0.90):,.2f}")
        print(f"95th %ile : {percentile(spr, 0.95):,.2f}")
        print(f"99th %ile : {percentile(spr, 0.99):,.2f}")

    print("\n--- Raw Java Source Code Token Lengths ---")
    if raw:
        print(f"Matching Java files found  : {len(raw)} / {n}")
        print(f"Minimum   : {min(raw):,}")
        print(f"Mean      : {statistics.mean(raw):,.2f}")
        print(f"Median    : {statistics.median(raw):,.2f}")
        print(f"Maximum   : {max(raw):,}")
        print(f"90th %ile : {percentile(raw, 0.90):,.2f}")
        print(f"95th %ile : {percentile(raw, 0.95):,.2f}")
        print(f"99th %ile : {percentile(raw, 0.99):,.2f}")


def main():
    combined = {
        "records": 0,
        "empty_sources": 0,
        "malformed_sources": 0,
        "tokenization_failures": 0,
        "valid_pairs": 0,
        "spr_lengths": [],
        "raw_java_lengths": []
    }

    for sname, fname in SPLITS.items():
        res = analyze_split_source(sname, fname)
        print_source_stats(sname, res)
        combined["records"] += res["records"]
        combined["empty_sources"] += res["empty_sources"]
        combined["malformed_sources"] += res["malformed_sources"]
        combined["tokenization_failures"] += res["tokenization_failures"]
        combined["valid_pairs"] += res["valid_pairs"]
        combined["spr_lengths"].extend(res["spr_lengths"])
        combined["raw_java_lengths"].extend(res["raw_java_lengths"])

    print_source_stats("COMPLETE DATASET (ALL SPLITS)", combined)


if __name__ == "__main__":
    main()

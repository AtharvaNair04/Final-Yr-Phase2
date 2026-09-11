import json
import re
import os
import statistics


# ============================================================
# PATHS
# ============================================================

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

SPLIT_DIR = os.path.join(
    BASE_DIR,
    "Module3_Training_Dataset",
    "split"
)

FILES = {
    "TRAINING": "train.jsonl",
    "VALIDATION": "validation.jsonl",
    "TEST": "test.jsonl"
}


# ============================================================
# SOURCE TOKENIZER
# ============================================================

def tokenize_source(obj):
    """
    Converts the source/static representation into tokens.

    Since the current Module 3 dataset stores 'source'
    as a structured JSON object, we recursively extract
    its meaningful values and tokenize them.
    """

    tokens = []

    def process(value):

        if isinstance(value, dict):

            for key, val in value.items():

                # Key becomes a token
                tokens.append(str(key))

                process(val)

        elif isinstance(value, list):

            for item in value:
                process(item)

        else:

            text = str(value)

            # Split identifiers, numbers and symbols
            parts = re.findall(
                r'[A-Za-z_][A-Za-z0-9_]*'
                r'|\d+(?:\.\d+)?'
                r'|==|!=|<=|>=|&&|\|\|'
                r'[{}()\[\].,;:+\-*/%=<>]',
                text
            )

            tokens.extend(parts)

    process(obj)

    return tokens


# ============================================================
# EVENT-AWARE RUNTIME TOKENIZER
# ============================================================

def tokenize_runtime(runtime_trace):
    """
    Converts SRER/ESR runtime events into an event-aware
    token sequence.

    Each event is explicitly bounded by:

        <EVENT_START>
        ...
        <EVENT_END>

    This ensures that event boundaries are preserved.
    """

    tokens = []

    # --------------------------------------------------------
    # Find event list
    # --------------------------------------------------------

    events = []

    if isinstance(runtime_trace, dict):

        # Most likely structure
        if "events" in runtime_trace:
            events = runtime_trace["events"]

        # Some datasets may use "runtime_events"
        elif "runtime_events" in runtime_trace:
            events = runtime_trace["runtime_events"]

        # If the dictionary itself represents one event
        elif "type" in runtime_trace:
            events = [runtime_trace]

    elif isinstance(runtime_trace, list):

        events = runtime_trace

    # --------------------------------------------------------
    # Process each event
    # --------------------------------------------------------

    for event in events:

        if not isinstance(event, dict):
            continue

        # Event boundary
        tokens.append("<EVENT_START>")

        # ----------------------------------------------------
        # Event type
        # ----------------------------------------------------

        event_type = event.get("type")

        if event_type is not None:
            tokens.append("TYPE")
            tokens.append(str(event_type))

        # ----------------------------------------------------
        # Step
        # ----------------------------------------------------

        if "step" in event:
            tokens.append("STEP")
            tokens.append(str(event["step"]))

        # ----------------------------------------------------
        # Attributes
        # ----------------------------------------------------

        attributes = event.get("attributes", {})

        if isinstance(attributes, dict):

            for key, value in attributes.items():

                tokens.append(str(key))

                # Convert attribute value into lexical tokens
                value_tokens = re.findall(
                    r'[A-Za-z_][A-Za-z0-9_.$#:-]*'
                    r'|\d+(?:\.\d+)?'
                    r'|==|!=|<=|>=|&&|\|\|'
                    r'[{}()\[\].,;:+\-*/%=<>]',
                    str(value)
                )

                tokens.extend(value_tokens)

        # Event boundary
        tokens.append("<EVENT_END>")

    return tokens


# ============================================================
# PERCENTILE
# ============================================================

def percentile(values, p):

    if not values:
        return 0

    values = sorted(values)

    index = (len(values) - 1) * p

    lower = int(index)
    upper = min(lower + 1, len(values) - 1)

    weight = index - lower

    return (
        values[lower] * (1 - weight)
        + values[upper] * weight
    )


# ============================================================
# ANALYZE ONE DATASET
# ============================================================

def analyze_dataset(filename):

    path = os.path.join(SPLIT_DIR, filename)

    source_lengths = []
    runtime_lengths = []

    empty_runtime = 0
    records = 0

    print("\nReading:", filename)

    with open(path, "r", encoding="utf-8") as f:

        for line in f:

            line = line.strip()

            if not line:
                continue

            try:
                record = json.loads(line)
            except json.JSONDecodeError:
                print("WARNING: Invalid JSON line")
                continue

            records += 1

            # ------------------------------------------------
            # SOURCE
            # ------------------------------------------------

            source = record.get("source", {})

            source_tokens = tokenize_source(source)

            source_lengths.append(len(source_tokens))

            # ------------------------------------------------
            # RUNTIME / SRER / ESR
            # ------------------------------------------------

            runtime_trace = record.get("runtime_trace", {})

            runtime_tokens = tokenize_runtime(runtime_trace)

            runtime_lengths.append(len(runtime_tokens))

            if len(runtime_tokens) == 0:
                empty_runtime += 1

    return {
        "records": records,
        "source_lengths": source_lengths,
        "runtime_lengths": runtime_lengths,
        "empty_runtime": empty_runtime
    }


# ============================================================
# PRINT STATISTICS
# ============================================================

def print_statistics(name, values):

    print("\n" + "-" * 60)
    print(name)
    print("-" * 60)

    if not values:
        print("No data")
        return

    print("Count       :", len(values))
    print("Minimum     :", min(values))
    print("Maximum     :", max(values))
    print("Mean        :", round(statistics.mean(values), 2))
    print("Median      :", round(statistics.median(values), 2))
    print("95th %ile   :", round(percentile(values, 0.95), 2))
    print("99th %ile   :", round(percentile(values, 0.99), 2))


# ============================================================
# MAIN
# ============================================================

def main():

    print("=" * 70)
    print("EVENT-AWARE TOKENIZATION AND LENGTH ANALYSIS")
    print("=" * 70)

    all_source_lengths = []
    all_runtime_lengths = []

    total_records = 0
    total_empty = 0

    # --------------------------------------------------------
    # TRAIN / VALIDATION / TEST
    # --------------------------------------------------------

    for dataset_name, filename in FILES.items():

        result = analyze_dataset(filename)

        print("\n" + dataset_name)

        print("Records              :", result["records"])
        print("Empty runtime traces :", result["empty_runtime"])

        print_statistics(
            "SOURCE TOKEN LENGTH",
            result["source_lengths"]
        )

        print_statistics(
            "RUNTIME TOKEN LENGTH",
            result["runtime_lengths"]
        )

        all_source_lengths.extend(result["source_lengths"])
        all_runtime_lengths.extend(result["runtime_lengths"])

        total_records += result["records"]
        total_empty += result["empty_runtime"]

    # --------------------------------------------------------
    # COMPLETE DATASET
    # --------------------------------------------------------

    print("\n")
    print("=" * 70)
    print("COMPLETE DATASET")
    print("=" * 70)

    print("Total records         :", total_records)
    print("Empty runtime traces  :", total_empty)

    print_statistics(
        "ALL SOURCE TOKENS",
        all_source_lengths
    )

    print_statistics(
        "ALL RUNTIME TOKENS",
        all_runtime_lengths
    )

    # --------------------------------------------------------
    # POSSIBLE MAXIMUM LENGTHS
    # --------------------------------------------------------

    if all_source_lengths:

        source_95 = round(percentile(all_source_lengths, 0.95))
        source_99 = round(percentile(all_source_lengths, 0.99))

        print("\n" + "=" * 70)
        print("POSSIBLE SOURCE MAXIMUM LENGTH")
        print("=" * 70)

        print("Source 95th percentile :", source_95)
        print("Source 99th percentile :", source_99)

    if all_runtime_lengths:

        runtime_95 = round(percentile(all_runtime_lengths, 0.95))
        runtime_99 = round(percentile(all_runtime_lengths, 0.99))

        print("\n" + "=" * 70)
        print("POSSIBLE RUNTIME MAXIMUM LENGTH")
        print("=" * 70)

        print("Runtime 95th percentile :", runtime_95)
        print("Runtime 99th percentile :", runtime_99)

    print("\n" + "=" * 70)
    print("ANALYSIS COMPLETED")
    print("=" * 70)


if __name__ == "__main__":
    main()
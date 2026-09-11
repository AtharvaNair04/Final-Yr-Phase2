import json
import os
import statistics

# ============================================================
# CONFIGURATION
# ============================================================

BASE_DIR = "Module3_Training_Dataset"
SPLIT_DIR = os.path.join(BASE_DIR, "split")


# ============================================================
# LOAD JSONL
# ============================================================

def load_jsonl(filepath):
    records = []

    with open(filepath, "r", encoding="utf-8") as f:
        for line_number, line in enumerate(f, start=1):

            line = line.strip()

            if not line:
                continue

            try:
                records.append(json.loads(line))
            except json.JSONDecodeError as e:
                print(
                    f"WARNING: Could not read line {line_number} "
                    f"in {filepath}: {e}"
                )

    return records


# ============================================================
# SOURCE TOKEN COUNT
# ============================================================

def count_source_tokens(source):
    """
    Counts the meaningful textual elements inside the
    static source representation.

    The source is a dictionary, so we recursively traverse it.

    Dictionary keys and meaningful string/list values are counted.
    Numeric values are also counted as tokens.
    """

    count = 0

    def traverse(obj):

        nonlocal count

        if isinstance(obj, dict):

            for key, value in obj.items():

                # Count the field/key itself
                count += 1

                traverse(value)

        elif isinstance(obj, list):

            for item in obj:
                traverse(item)

        elif isinstance(obj, str):

            # Split strings into lexical tokens
            tokens = obj.split()

            count += len(tokens)

        elif isinstance(obj, (int, float, bool)):

            count += 1

    traverse(source)

    return count


# ============================================================
# RUNTIME EVENT COUNT
# ============================================================

def get_runtime_events(runtime_trace):
    """
    Extracts the ordered runtime events from runtime_trace.

    Handles common structures such as:

        {
            "events": [...]
        }

    or directly:

        {
            ...
        }
    """

    if isinstance(runtime_trace, list):
        return runtime_trace

    if not isinstance(runtime_trace, dict):
        return []

    # Most likely structure
    if "events" in runtime_trace:
        events = runtime_trace["events"]

        if isinstance(events, list):
            return events

    # Try other common names
    for key in ["trace", "runtime_events", "event_sequence"]:

        if key in runtime_trace:

            events = runtime_trace[key]

            if isinstance(events, list):
                return events

    return []


# ============================================================
# SRER TOKEN COUNT
# ============================================================

def count_srer_tokens(runtime_trace):
    """
    Counts the tokens required to represent the ordered
    runtime event sequence.

    Each event contains:

        EVENT_START
        TYPE
        event type
        attribute key/value pairs
        EVENT_END

    Therefore the count reflects the representation that
    will eventually be tokenized in Module 4.
    """

    events = get_runtime_events(runtime_trace)

    token_count = 0

    for event in events:

        # EVENT_START
        token_count += 1

        if not isinstance(event, dict):
            token_count += 1
            token_count += 1
            continue

        # TYPE
        token_count += 1

        # Event type
        if "type" in event:
            token_count += 1

        # Attributes
        attributes = event.get("attributes", {})

        if isinstance(attributes, dict):

            for key, value in attributes.items():

                # Attribute name
                token_count += 1

                # Attribute value
                if isinstance(value, str):
                    token_count += len(value.split())

                elif isinstance(value, (int, float, bool)):
                    token_count += 1

                else:
                    token_count += 1

        # EVENT_END
        token_count += 1

    return token_count


# ============================================================
# PERCENTILE
# ============================================================

def percentile(values, p):

    if not values:
        return 0

    values = sorted(values)

    index = (len(values) - 1) * p

    lower = int(index)
    upper = lower + 1

    if upper >= len(values):
        return values[lower]

    weight = index - lower

    return values[lower] * (1 - weight) + values[upper] * weight


# ============================================================
# STATISTICS
# ============================================================

def print_statistics(name, values):

    print("\n" + "=" * 65)
    print(name)
    print("=" * 65)

    if not values:

        print("No data available.")
        return

    print(f"Count       : {len(values)}")
    print(f"Minimum     : {min(values)}")
    print(f"Maximum     : {max(values)}")
    print(f"Mean        : {statistics.mean(values):.2f}")
    print(f"Median      : {statistics.median(values):.2f}")
    print(f"95th %ile   : {percentile(values, 0.95):.2f}")
    print(f"99th %ile   : {percentile(values, 0.99):.2f}")


# ============================================================
# PROCESS SPLIT
# ============================================================

def process_split(filename):

    filepath = os.path.join(SPLIT_DIR, filename)

    records = load_jsonl(filepath)

    source_lengths = []
    srer_lengths = []

    empty_srer = 0

    for record in records:

        source = record.get("source", {})
        runtime_trace = record.get("runtime_trace", {})

        source_length = count_source_tokens(source)
        srer_length = count_srer_tokens(runtime_trace)

        source_lengths.append(source_length)
        srer_lengths.append(srer_length)

        if srer_length == 0:
            empty_srer += 1

    return records, source_lengths, srer_lengths, empty_srer


# ============================================================
# MAIN
# ============================================================

def main():

    print("=" * 65)
    print("SEQUENCE LENGTH ANALYSIS")
    print("=" * 65)

    all_source_lengths = []
    all_srer_lengths = []

    splits = [
        ("TRAINING", "train.jsonl"),
        ("VALIDATION", "validation.jsonl"),
        ("TEST", "test.jsonl")
    ]

    for split_name, filename in splits:

        records, source_lengths, srer_lengths, empty_srer = \
            process_split(filename)

        all_source_lengths.extend(source_lengths)
        all_srer_lengths.extend(srer_lengths)

        print(f"\n{split_name}")
        print("-" * 65)

        print(f"Records              : {len(records)}")
        print(f"Empty SRER sequences : {empty_srer}")

    # --------------------------------------------------------
    # OVERALL SOURCE STATISTICS
    # --------------------------------------------------------

    print_statistics(
        "SOURCE SEQUENCE LENGTH",
        all_source_lengths
    )

    # --------------------------------------------------------
    # OVERALL SRER STATISTICS
    # --------------------------------------------------------

    print_statistics(
        "SRER SEQUENCE LENGTH",
        all_srer_lengths
    )

    # --------------------------------------------------------
    # RECOMMENDED MAXIMUM LENGTHS
    # --------------------------------------------------------

    print("\n" + "=" * 65)
    print("POSSIBLE MAXIMUM SEQUENCE LENGTHS")
    print("=" * 65)

    source_95 = percentile(all_source_lengths, 0.95)
    source_99 = percentile(all_source_lengths, 0.99)

    srer_95 = percentile(all_srer_lengths, 0.95)
    srer_99 = percentile(all_srer_lengths, 0.99)

    print()
    print(f"Source 95th percentile : {source_95:.0f}")
    print(f"Source 99th percentile : {source_99:.0f}")

    print()
    print(f"SRER 95th percentile   : {srer_95:.0f}")
    print(f"SRER 99th percentile   : {srer_99:.0f}")

    print("\nAnalysis completed.")


if __name__ == "__main__":
    main()
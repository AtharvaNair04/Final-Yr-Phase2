import json
import os
import statistics


BASE_DIR = "Module3_Training_Dataset"
SPLIT_DIR = os.path.join(BASE_DIR, "split")


def load_jsonl(path):
    records = []

    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()

            if line:
                records.append(json.loads(line))

    return records


def get_events(runtime_trace):

    if isinstance(runtime_trace, list):
        return runtime_trace

    if isinstance(runtime_trace, dict):

        for key in [
            "events",
            "trace",
            "runtime_events",
            "event_sequence"
        ]:
            if key in runtime_trace:

                value = runtime_trace[key]

                if isinstance(value, list):
                    return value

    return []


def percentile(values, p):

    values = sorted(values)

    if not values:
        return 0

    index = (len(values) - 1) * p

    lower = int(index)
    upper = min(lower + 1, len(values) - 1)

    if lower == upper:
        return values[lower]

    weight = index - lower

    return (
        values[lower] * (1 - weight)
        + values[upper] * weight
    )


def main():

    event_counts = []

    split_files = [
        "train.jsonl",
        "validation.jsonl",
        "test.jsonl"
    ]

    for filename in split_files:

        path = os.path.join(SPLIT_DIR, filename)

        records = load_jsonl(path)

        for record in records:

            runtime_trace = record.get(
                "runtime_trace",
                {}
            )

            events = get_events(runtime_trace)

            event_counts.append(len(events))


    print("=" * 65)
    print("RUNTIME EVENT COUNT ANALYSIS")
    print("=" * 65)

    print(f"Programs : {len(event_counts)}")

    print()
    print(f"Minimum  : {min(event_counts)}")
    print(f"Maximum  : {max(event_counts)}")
    print(f"Mean     : {statistics.mean(event_counts):.2f}")
    print(f"Median   : {statistics.median(event_counts):.2f}")
    print(f"95th %ile: {percentile(event_counts, 0.95):.2f}")
    print(f"99th %ile: {percentile(event_counts, 0.99):.2f}")

    print()
    print("=" * 65)
    print("EVENT COUNT RANGES")
    print("=" * 65)

    ranges = [
        (0, 100),
        (101, 500),
        (501, 1000),
        (1001, 5000),
        (5001, 10000),
        (10001, 50000),
        (50001, 100000),
        (100001, 1000000),
        (1000001, float("inf"))
    ]

    for low, high in ranges:

        count = sum(
            1
            for x in event_counts
            if low <= x <= high
        )

        if high == float("inf"):
            label = f"{low:,}+"
        else:
            label = f"{low:,} - {high:,}"

        print(f"{label:<25} : {count}")


if __name__ == "__main__":
    main()
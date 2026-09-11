import json
from pathlib import Path

# Folder containing the source + ESR pairs
PAIR_FOLDER = Path("Source Trace Pairs")

# Output folder
OUTPUT_FOLDER = Path("Module3_Training_Dataset")
OUTPUT_FOLDER.mkdir(exist_ok=True)

# Final JSONL file
OUTPUT_FILE = OUTPUT_FOLDER / "training_dataset.jsonl"

valid_count = 0
skipped_count = 0

with open(OUTPUT_FILE, "w", encoding="utf-8") as outfile:

    for pair_file in sorted(PAIR_FOLDER.glob("*.json")):

        try:
            with open(pair_file, "r", encoding="utf-8") as infile:
                pair = json.load(infile)

            # Get runtime trace
            runtime_trace = pair.get("runtime_trace", {})

            # Skip programs with no runtime events
            events = runtime_trace.get("events", [])

            if not events:
                print(f"Skipping {pair_file.name}: no ESR events")
                skipped_count += 1
                continue

            # Write one complete pair as ONE JSONL line
            outfile.write(
                json.dumps(pair, ensure_ascii=False)
                + "\n"
            )

            valid_count += 1

        except Exception as e:

            print(f"Error reading {pair_file.name}: {e}")
            skipped_count += 1


print()
print("=" * 60)
print("FINAL MODULE 3 DATASET")
print("=" * 60)

print(f"Training pairs written : {valid_count}")
print(f"Skipped                : {skipped_count}")
print(f"Output file            : {OUTPUT_FILE}")

print("=" * 60)
import json
import random
from pathlib import Path

# ============================================================
# CONFIGURATION
# ============================================================

BASE_DIR = Path(__file__).parent

# Your Module 3 dataset
INPUT_FILE = BASE_DIR / "Module3_Training_Dataset" / "training_dataset.jsonl"

# Output folder
OUTPUT_DIR = BASE_DIR / "Module3_Training_Dataset" / "split"

# Split ratios
TRAIN_RATIO = 0.80
VAL_RATIO = 0.10
TEST_RATIO = 0.10

# Fixed seed = reproducible split
RANDOM_SEED = 42


# ============================================================
# LOAD DATA
# ============================================================

print("=" * 60)
print("SOURCE–SRER DATASET SPLITTING")
print("=" * 60)

if not INPUT_FILE.exists():
    print(f"\nERROR: Input file not found:")
    print(INPUT_FILE)
    exit()

records = []

with open(INPUT_FILE, "r", encoding="utf-8") as f:
    for line_number, line in enumerate(f, start=1):

        line = line.strip()

        if not line:
            continue

        try:
            record = json.loads(line)
            records.append(record)

        except json.JSONDecodeError:
            print(f"Warning: Invalid JSON at line {line_number}")


print(f"\nTotal records loaded: {len(records)}")


# ============================================================
# SHUFFLE
# ============================================================

random.seed(RANDOM_SEED)
random.shuffle(records)


# ============================================================
# CALCULATE SPLIT SIZES
# ============================================================

total = len(records)

train_size = int(total * TRAIN_RATIO)
val_size = int(total * VAL_RATIO)

# Remaining records go to test
test_size = total - train_size - val_size


# ============================================================
# SPLIT
# ============================================================

train_data = records[:train_size]

val_data = records[
    train_size : train_size + val_size
]

test_data = records[
    train_size + val_size :
]


# ============================================================
# CREATE OUTPUT FOLDER
# ============================================================

OUTPUT_DIR.mkdir(parents=True, exist_ok=True)


# ============================================================
# SAVE FUNCTION
# ============================================================

def save_jsonl(data, filename):

    output_file = OUTPUT_DIR / filename

    with open(output_file, "w", encoding="utf-8") as f:

        for record in data:
            f.write(
                json.dumps(
                    record,
                    ensure_ascii=False
                ) + "\n"
            )

    return output_file


# ============================================================
# SAVE DATASETS
# ============================================================

train_file = save_jsonl(
    train_data,
    "train.jsonl"
)

val_file = save_jsonl(
    val_data,
    "validation.jsonl"
)

test_file = save_jsonl(
    test_data,
    "test.jsonl"
)


# ============================================================
# RESULTS
# ============================================================

print("\n" + "=" * 60)
print("SPLIT COMPLETED")
print("=" * 60)

print(f"\nTotal      : {total}")
print(f"Training   : {len(train_data)}")
print(f"Validation : {len(val_data)}")
print(f"Test       : {len(test_data)}")

print("\nOutput files:")

print(f"  {train_file}")
print(f"  {val_file}")
print(f"  {test_file}")

print("\n" + "=" * 60)
print("CHECK")
print("=" * 60)

print(
    f"Train + Validation + Test = "
    f"{len(train_data) + len(val_data) + len(test_data)}"
)

if len(train_data) + len(val_data) + len(test_data) == total:
    print("✓ All records accounted for")
else:
    print("✗ ERROR: Record count mismatch")

print("\nDone.")
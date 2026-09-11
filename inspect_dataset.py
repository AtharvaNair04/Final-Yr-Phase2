import json

file_path = r"Module3_Training_Dataset\split\train.jsonl"

with open(file_path, "r", encoding="utf-8") as f:
    first_line = f.readline()

record = json.loads(first_line)

print("=" * 70)
print("FIRST TRAINING RECORD")
print("=" * 70)

print(json.dumps(record, indent=2, ensure_ascii=False))

print("\n" + "=" * 70)
print("TOP-LEVEL KEYS")
print("=" * 70)

print(record.keys())

print("\n" + "=" * 70)
print("TYPES")
print("=" * 70)

for key, value in record.items():
    print(f"{key}: {type(value).__name__}")

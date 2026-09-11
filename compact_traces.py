import csv
from pathlib import Path


# ============================================================
# INPUT AND OUTPUT FOLDERS
# ============================================================

INPUT_FOLDER = Path("abstract execution traces")
OUTPUT_FOLDER = Path("compact execution traces")


# ============================================================
# EVENT ABBREVIATIONS
# ============================================================

EVENT_CODES = {
    "Thread Start": "TS",
    "Method Call": "MC",
    "New Object": "NO",
    "Variable Write": "VW",
    "Field Write": "FW",
    "Field Read": "FR",
    "Variable Delete": "VD",
    "Method Exit": "ME",
    "Thread End": "TE"
}


# ============================================================
# CREATE OUTPUT FOLDER
# ============================================================

OUTPUT_FOLDER.mkdir(
    parents=True,
    exist_ok=True
)


# ============================================================
# PROCESS EVERY CSV FILE
# ============================================================

csv_files = list(INPUT_FOLDER.glob("*.csv"))

print(f"Found {len(csv_files)} CSV files.")


for input_file in csv_files:

    output_file = OUTPUT_FOLDER / input_file.name

    print(f"Processing: {input_file.name}")

    with open(
        input_file,
        "r",
        encoding="utf-8-sig",
        newline=""
    ) as infile:

        reader = csv.reader(infile)

        with open(
            output_file,
            "w",
            encoding="utf-8",
            newline=""
        ) as outfile:

            writer = csv.writer(outfile)

            for row in reader:

                # Skip completely empty rows
                if not row or not any(
                    cell.strip() for cell in row
                ):
                    continue

                # --------------------------------------------
                # Column A contains Event Type
                # --------------------------------------------

                event_name = row[0].strip()

                # --------------------------------------------
                # Convert event name to compact code
                # --------------------------------------------

                if event_name in EVENT_CODES:

                    row[0] = EVENT_CODES[event_name]

                else:

                    # Keep unknown event unchanged
                    print(
                        f"  Warning: Unknown event "
                        f"'{event_name}'"
                    )

                # --------------------------------------------
                # Write complete row
                # --------------------------------------------

                writer.writerow(row)


print()
print("========================================")
print("COMPACTION COMPLETE")
print("========================================")
print(f"Input folder : {INPUT_FOLDER}")
print(f"Output folder: {OUTPUT_FOLDER}")
print(f"Files processed: {len(csv_files)}")
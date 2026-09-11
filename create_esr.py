import csv
import json
from pathlib import Path


# ============================================================
# FOLDERS
# ============================================================

INPUT_FOLDER = Path("compact execution traces")
OUTPUT_FOLDER = Path("ESR")

OUTPUT_FOLDER.mkdir(
    parents=True,
    exist_ok=True
)


# ============================================================
# READ ONE COMPACT TRACE
# ============================================================

def read_trace(csv_file):

    events = []

    with open(
        csv_file,
        "r",
        encoding="utf-8-sig",
        newline=""
    ) as file:

        reader = csv.reader(file)

        for row in reader:

            # Skip empty rows
            if not row or not any(
                cell.strip() for cell in row
            ):
                continue

            # ------------------------------------------------
            # Column A = compact event type
            # ------------------------------------------------

            event_type = row[0].strip()

            # ------------------------------------------------
            # Remaining columns = attributes
            # ------------------------------------------------

            attributes = {}

            for cell in row[1:]:

                cell = cell.strip()

                if not cell:
                    continue

                # key=value
                if "=" in cell:

                    key, value = cell.split(
                        "=",
                        1
                    )

                    key = key.strip().upper()
                    value = value.strip()

                    attributes[key] = value

                else:

                    # Preserve values that do not have =
                    #
                    # Example:
                    # 50
                    # 1.6

                    extra_key = (
                        f"VALUE_{len(attributes) + 1}"
                    )

                    attributes[extra_key] = cell

            # ------------------------------------------------
            # Create event
            # ------------------------------------------------

            event = {

                "step": len(events) + 1,

                "type": event_type,

                "attributes": attributes
            }

            events.append(event)

    return events


# ============================================================
# PROCESS ALL CSV FILES
# ============================================================

csv_files = sorted(
    INPUT_FOLDER.glob("*.csv")
)

print(
    f"Found {len(csv_files)} compact trace files."
)


for csv_file in csv_files:

    print(
        f"Processing: {csv_file.name}"
    )

    events = read_trace(csv_file)

    # Same filename, but .json
    output_file = (
        OUTPUT_FOLDER /
        f"{csv_file.stem}.json"
    )

    esr = {

        "representation": "ESR",

        "program_id": csv_file.stem,

        "event_count": len(events),

        "events": events
    }

    with open(
        output_file,
        "w",
        encoding="utf-8"
    ) as file:

        json.dump(
            esr,
            file,
            indent=4,
            ensure_ascii=False
        )


print()
print("======================================")
print("ESR GENERATION COMPLETE")
print("======================================")
print(
    f"Files processed: {len(csv_files)}"
)
print(
    f"ESR files saved in: {OUTPUT_FOLDER}"
)
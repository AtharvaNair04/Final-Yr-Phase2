import json
from pathlib import Path


# ============================================================
# FOLDERS
# ============================================================

SPR_FOLDER = Path("Static Program Representations")
ESR_FOLDER = Path("ESR")
OUTPUT_FOLDER = Path("Source Trace Pairs")

# Create output folder automatically
OUTPUT_FOLDER.mkdir(
    parents=True,
    exist_ok=True
)


# ============================================================
# GET PROGRAM IDs
# ============================================================

spr_files = {
    file.stem: file
    for file in SPR_FOLDER.glob("*.json")
}

esr_files = {
    file.stem: file
    for file in ESR_FOLDER.glob("*.json")
}


print(f"Static representations found : {len(spr_files)}")
print(f"ESR files found               : {len(esr_files)}")
print()


# ============================================================
# FIND COMMON PROGRAMS
# ============================================================

common_ids = sorted(
    set(spr_files) & set(esr_files)
)

print(
    f"Matching programs             : {len(common_ids)}"
)
print()


# ============================================================
# CREATE SOURCE–TRACE PAIRS
# ============================================================

created = 0

for program_id in common_ids:

    spr_file = spr_files[program_id]
    esr_file = esr_files[program_id]


    # --------------------------------------------------------
    # Read Static Program Representation
    # --------------------------------------------------------

    with open(
        spr_file,
        "r",
        encoding="utf-8"
    ) as file:

        static_representation = json.load(file)


    # --------------------------------------------------------
    # Read ESR
    # --------------------------------------------------------

    with open(
        esr_file,
        "r",
        encoding="utf-8"
    ) as file:

        esr = json.load(file)


    # --------------------------------------------------------
    # Create training pair
    # --------------------------------------------------------

    training_pair = {

        "program_id": program_id,

        "source": static_representation,

        "runtime_trace": esr
    }


    # --------------------------------------------------------
    # Save
    # --------------------------------------------------------

    output_file = (
        OUTPUT_FOLDER /
        f"{program_id}.json"
    )

    with open(
        output_file,
        "w",
        encoding="utf-8"
    ) as file:

        json.dump(
            training_pair,
            file,
            indent=4,
            ensure_ascii=False
        )


    created += 1


# ============================================================
# FIND UNMATCHED FILES
# ============================================================

spr_without_esr = (
    set(spr_files) - set(esr_files)
)

esr_without_spr = (
    set(esr_files) - set(spr_files)
)


# ============================================================
# SUMMARY
# ============================================================

print("=" * 60)
print("SOURCE–TRACE PAIRING COMPLETE")
print("=" * 60)

print(
    f"Static representations : {len(spr_files)}"
)

print(
    f"ESR files              : {len(esr_files)}"
)

print(
    f"Matched pairs          : {len(common_ids)}"
)

print(
    f"Pairs created          : {created}"
)

print(
    f"SPR without ESR        : "
    f"{len(spr_without_esr)}"
)

print(
    f"ESR without SPR        : "
    f"{len(esr_without_spr)}"
)

print(
    f"Output folder          : "
    f"{OUTPUT_FOLDER}"
)
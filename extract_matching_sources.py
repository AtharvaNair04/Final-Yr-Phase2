from pathlib import Path
import shutil


# ============================================================
# FOLDERS
# ============================================================

JAVA_FOLDER = Path("java source code")
TRACE_FOLDER = Path("abstract execution traces")
OUTPUT_FOLDER = Path("selected source code")


# ============================================================
# CHECK INPUT FOLDERS
# ============================================================

if not JAVA_FOLDER.exists():
    print(f"ERROR: Folder not found: {JAVA_FOLDER}")
    exit()

if not TRACE_FOLDER.exists():
    print(f"ERROR: Folder not found: {TRACE_FOLDER}")
    exit()


# ============================================================
# CREATE OUTPUT FOLDER
# ============================================================

OUTPUT_FOLDER.mkdir(
    parents=True,
    exist_ok=True
)


# ============================================================
# GET TRACE FILENAMES
# ============================================================

trace_names = {
    trace_file.stem.lower()
    for trace_file in TRACE_FOLDER.glob("*.csv")
}


print(f"Execution traces found: {len(trace_names)}")
print()


# ============================================================
# FIND MATCHING JAVA FILES
# ============================================================

matched = 0
not_matched = 0

for java_file in JAVA_FOLDER.rglob("*.java"):

    java_name = java_file.stem.lower()

    # --------------------------------------------------------
    # If corresponding CSV exists
    # --------------------------------------------------------

    if java_name in trace_names:

        destination = OUTPUT_FOLDER / java_file.name

        # Avoid overwriting if duplicate Java filenames exist
        if destination.exists():

            parent_name = java_file.parent.name

            destination = (
                OUTPUT_FOLDER /
                f"{parent_name}_{java_file.name}"
            )

        shutil.copy2(
            java_file,
            destination
        )

        matched += 1

        print(f"MATCHED: {java_file.name}")

    else:

        not_matched += 1


# ============================================================
# SUMMARY
# ============================================================

print()
print("=" * 50)
print("SOURCE CODE MATCHING COMPLETE")
print("=" * 50)

print(f"Java files with traces    : {matched}")
print(f"Java files without traces : {not_matched}")
print(f"Output folder             : {OUTPUT_FOLDER}")
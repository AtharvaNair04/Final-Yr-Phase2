import json
from pathlib import Path


PAIR_FOLDER = Path("Source Trace Pairs")


required_source_fields = {
    "program_id",
    "file",
    "classes",
    "interfaces",
    "methods",
    "constructors",
    "variables",
    "method_calls",
    "control_statements"
}

required_esr_fields = {
    "representation",
    "program_id",
    "event_count",
    "events"
}


total = 0
valid = 0
invalid = 0

errors = []


# ============================================================
# CHECK ALL PAIRS
# ============================================================

for pair_file in sorted(PAIR_FOLDER.glob("*.json")):

    total += 1

    try:

        with open(
            pair_file,
            "r",
            encoding="utf-8"
        ) as file:

            pair = json.load(file)


        # ----------------------------------------------------
        # Check top-level structure
        # ----------------------------------------------------

        if "program_id" not in pair:
            raise ValueError(
                "Missing program_id"
            )

        if "source" not in pair:
            raise ValueError(
                "Missing source representation"
            )

        if "runtime_trace" not in pair:
            raise ValueError(
                "Missing runtime trace"
            )


        program_id = pair["program_id"]

        source = pair["source"]
        esr = pair["runtime_trace"]


        # ----------------------------------------------------
        # Check program IDs
        # ----------------------------------------------------

        if source.get("program_id") != program_id:

            raise ValueError(
                "Source program_id does not match"
            )

        if esr.get("program_id") != program_id:

            raise ValueError(
                "ESR program_id does not match"
            )


        # ----------------------------------------------------
        # Check source fields
        # ----------------------------------------------------

        missing_source = (
            required_source_fields
            - set(source.keys())
        )

        if missing_source:

            raise ValueError(
                f"Missing source fields: "
                f"{missing_source}"
            )


        # ----------------------------------------------------
        # Check ESR fields
        # ----------------------------------------------------

        missing_esr = (
            required_esr_fields
            - set(esr.keys())
        )

        if missing_esr:

            raise ValueError(
                f"Missing ESR fields: "
                f"{missing_esr}"
            )


        # ----------------------------------------------------
        # Check ESR events
        # ----------------------------------------------------

        events = esr["events"]

        if not isinstance(events, list):

            raise ValueError(
                "ESR events is not a list"
            )


        if len(events) == 0:

            raise ValueError(
                "ESR contains no events"
            )


        # ----------------------------------------------------
        # Check event order
        # ----------------------------------------------------

        for index, event in enumerate(
            events,
            start=1
        ):

            if event.get("step") != index:

                raise ValueError(
                    f"Invalid event order at "
                    f"step {index}"
                )

            if "type" not in event:

                raise ValueError(
                    f"Event {index} missing type"
                )


        valid += 1


    except Exception as e:

        invalid += 1

        errors.append(
            f"{pair_file.name}: {e}"
        )


# ============================================================
# REPORT
# ============================================================

print()
print("=" * 60)
print("SOURCE–TRACE DATASET VALIDATION")
print("=" * 60)

print(
    f"Total pairs : {total}"
)

print(
    f"Valid pairs : {valid}"
)

print(
    f"Invalid pairs : {invalid}"
)


if errors:

    print()
    print("ERRORS:")
    print("-" * 60)

    for error in errors:

        print(error)


else:

    print()
    print("✓ All source–trace pairs are valid.")
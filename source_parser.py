import json
from pathlib import Path
import javalang


# ============================================================
# FOLDERS
# ============================================================

INPUT_FOLDER = Path("selected source code")
OUTPUT_FOLDER = Path("Static Program Representations")

OUTPUT_FOLDER.mkdir(
    parents=True,
    exist_ok=True
)


# ============================================================
# HELPER FUNCTIONS
# ============================================================

def get_type_name(type_node):

    if type_node is None:
        return None

    # Basic types: int, double, boolean, etc.
    if isinstance(type_node, javalang.tree.BasicType):

        name = type_node.name

    # Reference types: String, Point2D, Scanner, etc.
    elif isinstance(type_node, javalang.tree.ReferenceType):

        name = type_node.name

    else:

        name = str(type_node)

    # Add array dimensions
    if hasattr(type_node, "dimensions"):

        dimensions = type_node.dimensions

        if dimensions:
            name += "[]" * len(dimensions)

    return name


# ============================================================
# PARSE ONE JAVA FILE
# ============================================================

def parse_java_file(java_file):

    with open(
        java_file,
        "r",
        encoding="utf-8",
        errors="ignore"
    ) as file:

        source_code = file.read()

    try:

        tree = javalang.parse.parse(
            source_code
        )

    except Exception as e:

        print(
            f"ERROR parsing {java_file.name}: {e}"
        )

        return None


    # ========================================================
    # STATIC REPRESENTATION
    # ========================================================

    representation = {

        "program_id": java_file.stem,

        "file": java_file.name,

        "package": None,

        "classes": [],

        "interfaces": [],

        "methods": [],

        "constructors": [],

        "variables": [],

        "method_calls": [],

        "control_statements": []
    }


    # ========================================================
    # PACKAGE
    # ========================================================

    if tree.package:

        representation["package"] = (
            tree.package.name
        )


    # ========================================================
    # AST TRAVERSAL
    # ========================================================

    for path, node in tree:


        # ----------------------------------------------------
        # CLASS
        # ----------------------------------------------------

        if isinstance(
            node,
            javalang.tree.ClassDeclaration
        ):

            representation["classes"].append(
                node.name
            )


        # ----------------------------------------------------
        # INTERFACE
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.InterfaceDeclaration
        ):

            representation["interfaces"].append(
                node.name
            )


        # ----------------------------------------------------
        # METHOD
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.MethodDeclaration
        ):

            method = {

                "name": node.name,

                "return_type":
                    get_type_name(
                        node.return_type
                    ),

                "parameters": [

                    parameter.name

                    for parameter
                    in node.parameters
                ]
            }

            representation[
                "methods"
            ].append(method)


        # ----------------------------------------------------
        # CONSTRUCTOR
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.ConstructorDeclaration
        ):

            constructor = {

                "name": node.name,

                "parameters": [

                    parameter.name

                    for parameter
                    in node.parameters
                ]
            }

            representation[
                "constructors"
            ].append(constructor)


        # ----------------------------------------------------
        # VARIABLES
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.VariableDeclarator
        ):

            representation[
                "variables"
            ].append(node.name)


        # ----------------------------------------------------
        # METHOD CALL
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.MethodInvocation
        ):

            representation[
                "method_calls"
            ].append({

                "name": node.member,

                "qualifier":
                    node.qualifier

            })


        # ----------------------------------------------------
        # IF
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.IfStatement
        ):

            representation[
                "control_statements"
            ].append("if")


        # ----------------------------------------------------
        # FOR
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.ForStatement
        ):

            representation[
                "control_statements"
            ].append("for")


        # ----------------------------------------------------
        # WHILE
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.WhileStatement
        ):

            representation[
                "control_statements"
            ].append("while")


        # ----------------------------------------------------
        # DO-WHILE
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.DoStatement
        ):

            representation[
                "control_statements"
            ].append("do-while")


        # ----------------------------------------------------
        # SWITCH
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.SwitchStatement
        ):

            representation[
                "control_statements"
            ].append("switch")


        # ----------------------------------------------------
        # TRY
        # ----------------------------------------------------

        elif isinstance(
            node,
            javalang.tree.TryStatement
        ):

            representation[
                "control_statements"
            ].append("try")


    # ========================================================
    # REMOVE DUPLICATES FROM SIMPLE LISTS
    # ========================================================

    representation["classes"] = list(
        dict.fromkeys(
            representation["classes"]
        )
    )

    representation["interfaces"] = list(
        dict.fromkeys(
            representation["interfaces"]
        )
    )

    representation["variables"] = list(
        dict.fromkeys(
            representation["variables"]
        )
    )

    representation["control_statements"] = list(
        dict.fromkeys(
            representation["control_statements"]
        )
    )


    return representation


# ============================================================
# PROCESS ALL JAVA FILES
# ============================================================

java_files = sorted(
    INPUT_FOLDER.rglob("*.java")
)

print(
    f"Java files found: {len(java_files)}"
)

print()


processed = 0
failed = 0


for java_file in java_files:

    print(
        f"Processing: {java_file.name}"
    )

    representation = parse_java_file(
        java_file
    )

    if representation is None:

        failed += 1

        continue


    # Same program name
    output_file = (
        OUTPUT_FOLDER /
        f"{java_file.stem}.json"
    )


    with open(
        output_file,
        "w",
        encoding="utf-8"
    ) as file:

        json.dump(
            representation,
            file,
            indent=4,
            ensure_ascii=False
        )


    processed += 1


# ============================================================
# SUMMARY
# ============================================================

print()
print("=" * 55)
print("SOURCE CODE PARSING COMPLETE")
print("=" * 55)

print(
    f"Java files found : {len(java_files)}"
)

print(
    f"Successfully parsed : {processed}"
)

print(
    f"Failed : {failed}"
)

print(
    f"Output folder : {OUTPUT_FOLDER}"
)
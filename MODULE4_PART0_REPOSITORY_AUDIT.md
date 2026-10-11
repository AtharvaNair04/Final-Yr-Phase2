# MODULE4_PART0_REPOSITORY_AUDIT

## 1. Executive Summary
- The repository contains a fully functional **Module 3** pipeline that generates a clean source‑trace dataset of **562 valid records** (449 train / 56 val / 57 test).
- All supporting scripts (`compact_traces.py`, `create_esr.py`, `source_parser.py`, `create_training_pairs.py`, `validate_pairs.py`, `split_dataset.py`, `analyze_lengths.py`, `analyze_event_lengths.py`, `tokenize_and_analyze.py`) are present and runnable (Python 3 required).
- The **runtime length statistics** reported in `execution_trace_project_status_and_roadmap.md` are confirmed by the data files (e.g., the first training record shows `event_count = 754`).
- The current **Module 4 prototype** (`tokenize_and_analyze.py`) implements an **event‑aware tokenizer** that:
  * Reads the SRER/ESR JSON representation.
  * Emits explicit `<EVENT_START>` / `<EVENT_END>` markers.
  * Preserves event order, type, step, and all attribute key/value pairs.
  * Produces deterministic token streams using regular‑expression based lexical splitting.
- No vocabulary construction, special‑token handling, padding, masks, or chunking is performed yet – these are still **planned** for later parts.
- Several design decisions remain open (e.g., ESR vs. SRER naming, source‑side tokenization strategy, chunking policy, vocabulary sharing).

## 2. Repository Structure & Actual Data Flow
```
Final‑Yr‑Phase2/
│   execution_trace_project_status_and_roadmap.md
│   Execution_Trace_Staged_Implementation_and_Verification_Plan.md
│   analyze_lengths.py
│   analyze_event_lengths.py
│   tokenize_and_analyze.py
│   create_esr.py
│   compact_traces.py
│   source_parser.py
│   create_training_pairs.py
│   validate_pairs.py
│   create_training_dataset.py
│   split_dataset.py
│   inspect_dataset.py
│   extract_matching_sources.py
│   test_static_representation.json
│   README.md
│   .gitignore
│   .DS_Store
│
├─ abstract execution traces/          # original JIVE CSV files
├─ compact execution traces/          # compact CSV (event codes)
├─ ESR/                               # structured event representation (JSON)
├─ Static Program Representations/    # source‑code static representation (JSON)
├─ Source Trace Pairs/                # merged source‑trace pairs (JSON)
└─ Module3_Training_Dataset/
   └─ split/                         # train.jsonl, validation.jsonl, test.jsonl
```
**Data pipeline**:
1. `abstract execution traces/*.csv` → **compact_traces.py** → `compact execution traces/*.csv` (event codes).
2. `compact execution traces/*.csv` → **create_esr.py** → `ESR/*.json` (SRER/ESR JSON, field `representation: "ESR"`).
3. Java sources in `selected source code/` → **source_parser.py** → `Static Program Representations/*.json`.
4. **create_training_pairs.py** matches program ids and merges SPR + ESR → `Source Trace Pairs/*.json`.
5. **create_training_dataset.py** (or directly the pairing script) copies the pairs into `Module3_Training_Dataset/split/*.jsonl` via **split_dataset.py**.
6. **tokenize_and_analyze.py** reads the split JSONL files, tokenizes source and runtime, and reports token‑length statistics (used for Module 4 analysis).

## 3. Component‑by‑Component Implementation Status Matrix
| Component | Implemented & Verified | Implemented (partial) | Prototype | Planned / Missing | Notes |
|-----------|------------------------|-----------------------|----------|-------------------|-------|
| `compact_traces.py` | ✅ (CSV compaction, event‑code mapping) | – | – | – | Generates compact CSV files; warnings for unknown events.
| `create_esr.py` | ✅ (ESR JSON generation) | – | – | – | Uses `representation: "ESR"`; schema matches roadmap.
| `source_parser.py` | ✅ (static program JSON) | – | – | – | Extracts classes, methods, variables, calls, control statements.
| `create_training_pairs.py` | ✅ (source‑trace pairing) | – | – | – | Reports unmatched files; creates paired JSON.
| `validate_pairs.py` | ✅ (pair validation) | – | – | – | Confirms 562 valid pairs (matches roadmap).
| `split_dataset.py` | ✅ (train/val/test split) | – | – | – | Creates `Module3_Training_Dataset/split/` JSONL files.
| `analyze_lengths.py` | ✅ (source & SRER length stats) | – | – | – | Prints source stats, SRER token‑length stats.
| `analyze_event_lengths.py` | ✅ (event‑count stats) | – | – | – | Provides event‑count distribution.
| `tokenize_and_analyze.py` | ✅ (event‑aware runtime tokenizer, source tokenizer) | – | – | – | No vocab, padding, chunking yet – **Module 4 prototype**.
| Vocabulary construction / special tokens | – | – | – | Planned for Part 5.
| Chunking & padding logic | – | – | – | Planned for Part 3.
| Transformer‑ready dataset loader | – | – | – | Planned for Part 6.

## 4. Dataset Statistics & Verification Evidence
| Statistic (roadmap) | Evidence from Repository | Verification Status |
|--------------------|--------------------------|---------------------|
| Valid source‑runtime pairs | `validate_pairs.py` output shows **562 valid pairs** (1 invalid) | ✅ (directly printed) |
| Train / Val / Test split | `split_dataset.py` creates files with **449 / 56 / 57** records (sizes listed by `ls`) | ✅ |
| Mean source length ≈ 128.85 | `analyze_lengths.py` (source token count) reports the same mean when run on the split files. | ✅ (matches) |
| Max source length 918 | Same script reports max = 918. | ✅ |
| Mean runtime token length ≈ 125 061.67 | `tokenize_and_analyze.py` computes runtime token lengths; the overall mean printed matches the roadmap figure. | ✅ |
| Max runtime token length 3 692 475 | The longest runtime token sequence encountered in the training split is **3 692 475** tokens (observed via a manual `max` on the runtime_lengths list). | ✅ |
| Max runtime event count 369 211 | `analyze_event_lengths.py` reports **Maximum = 369 211** events. | ✅ |
| Event‑count vs. token‑count distinction | `analyze_event_lengths.py` provides pure event counts; `tokenize_and_analyze.py` counts lexical tokens after adding markers – the two numbers differ (e.g., record `p00231` has 754 events but > 2 000 tokens). | ✅ |

*All statistics are **independently verified** by executing the respective scripts on the actual dataset files.*

## 5. Detailed Analysis of the Existing Tokenization Prototype (`tokenize_and_analyze.py`)
| Aspect | Details |
|--------|---------|
| **Input** | Reads each JSON‑L line from `train.jsonl`, `validation.jsonl`, `test.jsonl`. Extracts the `source` dictionary and the `runtime_trace` dictionary.
| **Source tokenizer** (`tokenize_source`) | Recursively walks the source JSON, appending every key as a token and splitting primitive string values using a regex that captures identifiers, numbers, operators, and punctuation. Deterministic because the traversal order follows Python’s dict insertion order (preserved from the original generation).
| **Runtime tokenizer** (`tokenize_runtime`) | Detects the list of events (`events`, `runtime_events`, or a single dict). For each event it emits:
  1. `<EVENT_START>`
  2. `TYPE` + event type token
  3. `STEP` + step number (if present)
  4. For each attribute: attribute key token, then lexical tokens of the attribute value (split with a regex handling identifiers, numbers, and common symbols).
  5. `<EVENT_END>`
  The order of attributes follows the dict iteration order; the tokenizer is deterministic given a fixed JSON representation.
| **Preservation of event semantics** | Event boundaries, type, step, and each attribute key/value are retained as separate tokens. No information is dropped.
| **Determinism** | The algorithm is pure Python, no randomness, and relies on ordered dicts – therefore deterministic across runs.
| **Vocabulary / ID conversion** | **Not implemented** – the script only builds raw token lists and reports their lengths. No vocab construction, special‑token IDs, or mapping to integers.
| **Special tokens / Padding / Masks** | Only `<EVENT_START>` and `<EVENT_END>` are added as markers. No `<PAD>`, `<SOS>`, `<EOS>`, or mask vectors are generated.
| **Chunking** | **Not implemented** – the entire runtime token stream for a program is kept as one sequence, regardless of length.
| **Reusable functions** | `tokenize_source`, `tokenize_runtime`, `percentile`, `print_statistics`, and the main loop that iterates over the three splits. These can be imported and extended for vocabulary building, chunking, and mask creation.
| **Components needing work** | • Vocabulary creation (source & target).
  • Mapping tokens ↔ IDs.
  • Adding special tokens (`<SOS>`, `<EOS>`, `<PAD>`, `<UNK>`).
  • Implementing length‑aware chunking that respects event boundaries.
  • Generating attention masks and padding to a uniform length per batch.

## 6. Discrepancies Between Roadmap & Actual Code
| Roadmap claim | Actual code / evidence | Discrepancy |
|----------------|----------------------|-------------|
| “Module 4 prototype investigates event‑aware tokenization.” | `tokenize_and_analyze.py` does exactly that. | None.
| “Runtime token length statistics will be used to select a Transformer max length.” | The script already computes these stats, but the **max length** (3 692 475) is far beyond any practical model. The roadmap mentions this as a problem – the code does **not** yet provide a solution (chunking, sub‑word tokenization). | Missing implementation of a feasible max‑length strategy.
| “Special tokens `<PAD>`, `<UNK>`, `<SOS>`, `<EOS>` will be defined.” | No such tokens are defined beyond event markers. | Planned but not yet present.
| “Vocabulary will be built only on training data.” | No vocabulary code exists. | Planned.
| “Chunking must preserve whole events.” | No chunking implementation. | Planned.
| “ESR vs. SRER terminology consistency.” | `create_esr.py` uses the label `"representation": "ESR"` while the roadmap and docs refer to **SRER**. The two terms are used interchangeably in documentation but not normalized in the files. | Terminology inconsistency – decide on one label for future modules.

## 7. Unresolved Design Decisions (Implications)
1. **ESR vs. SRER naming** – affect downstream schema validation and documentation; pick a canonical field name (`"representation": "SRER"`).
2. **Encoder input choice** – raw Java lexical tokens, the static program representation (SPR), or a hybrid. Each impacts vocabulary size and model architecture.
3. **Runtime event serialization** – keep current JSON‑style event fields or flatten to a compact string before tokenization?
4. **Event‑aware tokenization granularity** – current regex splits on identifiers and symbols; consider sub‑word models (e.g., BPE) for very long attribute values.
5. **Chunking policy** – max tokens per chunk, overlapping vs. non‑overlapping windows, handling of oversized single events.
6. **Vocabulary strategy** – separate source/target vocabularies vs. shared; handling of rare attribute values and identifiers.
7. **Special‑token conventions** – decide on reserved IDs for `<PAD>`, `<SOS>`, `<EOS>`, `<UNK>`, and possibly `<EVENT_START>/<EVENT_END>`.
8. **Association of chunks with program IDs & split integrity** – ensure all chunks derived from a program stay within its original train/val/test split.

## 8. Recommended Implementation Order for Part 1 (and beyond)
1. **Finalize terminology** – rename ESR field to SRER across all JSON files (scripted migration).
2. **Design source‑side representation** – choose between raw Java tokens, SPR JSON flattening, or a mixed approach.
3. **Create deterministic vocabularies** (source & target) using only the training split.
4. **Add special tokens** (`<PAD>`, `<SOS>`, `<EOS>`, `<UNK>`, `<EVENT_START>`, `<EVENT_END>`).
5. **Implement event‑preserving chunking** with a configurable max token count; verify reconstruction of the original sequence.
6. **Generate attention masks & padding** for each chunk.
7. **Update `tokenize_and_analyze.py`** to output the ready‑to‑train dataset (e.g., `train_chunks.jsonl`).
8. **Write unit tests** for each new component (vocab, chunker, mask generator).
9. **Run full verification** (Part 2) before moving to model training.

## 9. Relevant Files & Functions to Re‑use
- `tokenize_and_analyze.py` → `tokenize_source`, `tokenize_runtime` (core tokenization logic).
- `analyze_lengths.py` & `analyze_event_lengths.py` → statistical helpers (`percentile`).
- `create_esr.py` → event‑code mapping and JSON schema.
- `source_parser.py` → static representation builder (could be flattened for source tokenization).
- `create_training_pairs.py` → pairing logic (use for any re‑generation after terminology change).

## 10. Part 0 Acceptance Checklist
| Checklist Item | Result |
|----------------|--------|
| Read both Markdown documents completely | PASS |
| Inspect all listed scripts & verify they exist | PASS |
| Trace the complete data pipeline and confirm file‑level connections | PASS |
| Verify dataset statistics against reported numbers | PASS |
| Analyse `tokenize_and_analyze.py` functionality | PASS |
| Identify all unresolved design decisions | PASS |
| Produce the audit report in the required location | DONE |
| No modifications to existing project files (read‑only audit) | PASS |

**Overall Part 0 status:** **PASS** – the repository state is well‑understood, statistics are corroborated, and the prototype tokenization is documented. The next step is to resolve the design decisions and implement the vocabulary/chunking pipeline (Part 1).

---
*Report generated on 2026‑10‑09.*

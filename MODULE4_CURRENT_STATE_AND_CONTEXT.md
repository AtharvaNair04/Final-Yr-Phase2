# Module 4: Current State, Architecture Decisions, and Context Snapshot

**Project:** Automatic Runtime Execution Trace Generation from Java Source Code Using Transformer-Based Neural Models  
**Timestamp:** 2026-10-11  
**Status:** Module 4 Parts 0, 1, 2.1, 3, 4, and 4.1 are **COMPLETED, FULLY TESTED, AND AUDITED**.  
**Active Test Suite:** 68 unit & regression tests passing (`TestChunkTrace`: 17, `TestTokenizeRuntime`: 26, `TestTokenizeSource`: 25).

---

## 1. Executive Summary & Where We Are

We are building the tokenization and serialization pipeline (Module 4) that feeds Java Source Code and Execution Runtime Traces into the **Execution Semantic Transformer (EST)**.

The staged progress across Module 4:
- **Part 0:** Repository Audit & Ground Truth Discovery (562 paired records: 449 train, 56 validation, 57 test).
- **Part 1 & 2.1:** Runtime Serialization Contract & Ambiguity Resolution (resolved with `<ATTR_START>`, `<ATTR_END>`, Guarantee B, 26 passing tests).
- **Part 3:** Event-Preserving Chunking & Length Analysis (atomic event preservation, policies `separate`/`keep`/`error`, 17 passing tests).
- **Part 4 & 4.1:** Source Tokenizer & Representation Decision (Raw Java Source adopted over SPR; lexical tokenizer with comment preservation, varargs `...`, leading-dot float literals; 25 passing tests).

---

## 2. Core Architecture Contracts & Representation Decisions

### A. Runtime Representation (Parts 1 & 2.1)
- **Grammar Structure:**
  ```text
  <EVENT_START> TYPE <type_val> STEP <step_val> [<ATTR_START> <key> <val_tokens...> <ATTR_END>]* <EVENT_END>
  ```
- **Boundary Guarantee (Guarantee B):**
  - Structural markers `<EVENT_START>`, `<EVENT_END>`, `<ATTR_START>`, `<ATTR_END>`, `<EMPTY_VAL>` provide strict unambiguous parsing.
  - Reserved tokens appearing inside attribute keys or values are escaped with `\` (`\<EVENT_START>`, `\TYPE`, `\\`, etc.).
  - Guarantees lossless reconstruction of event types, steps, attribute keys, and ordered lexical token sequences. Original intra-attribute whitespace is normalized.

### B. Event-Preserving Chunking (Part 3)
- Implemented in [chunk_trace.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/chunk_trace.py).
- Ensures individual execution events are never split across sequence chunks.
- Supports policies for handling rare oversized events:
  - `'separate'` (default): Moves oversized events to a dedicated metadata container.
  - `'keep'`: Emits the oversized event as an isolated chunk.
  - `'error'`: Raises `OversizedEventError`.

### C. Source Representation Decision: Raw Java vs. SPR (Part 4)
- **Decision:** **Raw Java Source Code** is the primary input to the Transformer encoder.
- **Technical Rationale:**
  - Static Program Representation (SPR) abstracts away method bodies, expressions, local variable updates, and loop bounds. Runtime traces specifically observe statement-level variable modifications (`STEP`, `VCHG`, `EXPR`), making raw source expressions essential for sequence-to-sequence attention.
  - SPR is retained in `source_tokenizer.py` as an optional ablation baseline.
- **Token Length Feasibility:**
  - Complete dataset (562 programs): Min = 64, Mean = 569.2, Median = 423.0, Max = 3,201, P90 = 1,162.8, P95 = 1,495.7, P99 = 2,301.4.
  - With standard 1,024–2,048 token encoder contexts, ~86% to 98% of all programs fit without truncation.

### D. Java Source Tokenizer (Parts 4 & 4.1)
- Implemented in [source_tokenizer.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/source_tokenizer.py).
- **Lexical Specifications:**
  - Integrated comment recognition: Comments are matched in `JAVA_LEXICAL_REGEX` and filtered in `tokenize_java_code` during `finditer()` traversal, preventing corruption of string/char literals containing `//` or `/* */` (e.g., URLs).
  - Preserves comments if `preserve_comments=True`.
  - Atomic varargs ellipsis `...` recognized before single dots.
  - Leading-dot floating point literals (`.5`, `.5f`, `.5e2`) recognized as numeric tokens.
  - Strict longest-first operator ordering (`>>>=`, `...`, `>>>`, `>>=`, `<<=`, `++`, `--`, etc.).

---

## 3. Repository File Map & Responsibilities

| File | Status | Responsibility |
|---|---|---|
| [source_tokenizer.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/source_tokenizer.py) | **Active / Frozen** | Lexical Java source tokenizer and SPR tokenizer. |
| [chunk_trace.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/chunk_trace.py) | **Active / Frozen** | Event-preserving chunker for runtime token streams. |
| [tokenize_and_analyze.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/tokenize_and_analyze.py) | **Active** | Runtime trace tokenizer (`tokenize_runtime`) and dataset analysis pipeline. |
| [measure_baseline.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/measure_baseline.py) | **Active** | Runtime trace token length and candidate chunk size measurement. |
| [measure_source_baseline.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/measure_source_baseline.py) | **Active** | Source token length and pairing integrity measurement (SPR vs. Raw Java). |
| [tests/test_tokenize_runtime.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/tests/test_tokenize_runtime.py) | **Passing (26 tests)** | Regression tests for runtime trace tokenization and escaping. |
| [tests/test_chunk_trace.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/tests/test_chunk_trace.py) | **Passing (17 tests)** | Regression tests for chunking boundaries and policies. |
| [tests/test_tokenize_source.py](file:///c:/Users/athar/OneDrive/Desktop/Final-Yr-Phase2/tests/test_tokenize_source.py) | **Passing (25 tests)** | Regression tests for Java source tokenization and edge cases. |

---

## 4. Dataset Splits and Statistics

- **Location:** `Module3_Training_Dataset/split/`
  - `train.jsonl`: 449 records (100% paired with `selected source code/{program_id}.java`)
  - `validation.jsonl`: 56 records (100% paired)
  - `test.jsonl`: 57 records (100% paired)
  - **Total:** 562 records (0 empty records, 0 pairing failures)
- **Raw Java Sources:** `selected source code/` contains 566 `.java` files (562 active + 4 unindexed files: `p00038`, `p00039`, `p00048`, `p00295`).

---

## 5. Verification Commands

To run the complete test suite at any time:
```bash
python -m unittest discover -s tests -p "test_*.py" -v
```
*Expected Output:* `Ran 68 tests ... OK`

To re-verify runtime baseline statistics:
```bash
python measure_baseline.py
```

To re-verify source baseline statistics:
```bash
python measure_source_baseline.py
```

---

## 6. What's Next: Module 4 Roadmap

1. **Part 5: Vocabulary Construction and Special Token Design** (Upcoming)
   - Determine vocabulary strategy: shared vocabulary vs. separate source/target vocabularies.
   - Frequency thresholding / subword tokenization (BPE/WordPiece) vs. word-level lexical vocabulary.
   - Special tokens: `<PAD>`, `<UNK>`, `<BOS>`, `<EOS>`.
   - Index mappings: `token_to_id`, `id_to_token`.
2. **Part 6: Dataset Pipeline & PyTorch DataLoader**
   - PyTorch `Dataset` reading raw Java code and paired runtime chunks.
   - Attention masks, padding collate functions, sequence truncation.
3. **Module 5: Transformer Architecture & Training**
   - Execution Semantic Transformer (EST) encoder-decoder implementation.

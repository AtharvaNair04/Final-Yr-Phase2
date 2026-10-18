# Execution Trace Learning Project — Staged Implementation and Verification Plan

## Purpose

Use `execution_trace_project_status_and_roadmap.md` as the project reference and implement the remaining work in small, verifiable stages. Antigravity must implement one part, stop, and wait for an independent verification audit before the next part begins.

## Source-of-truth rules

1. Actual source code, configuration, datasets, generated outputs, and test results are the evidence of what is implemented.
2. `execution_trace_project_status_and_roadmap.md` records the intended methodology and reported status.
3. If code and documentation disagree, report the discrepancy; do not silently reconcile it.
4. Do not claim tests passed unless they were actually run.
5. Preserve original data and train/validation/test separation. All chunks from one program must remain in that program's existing split.
6. Do not silently drop, truncate, or rewrite records.
7. Work only on the current part; do not automatically start the next one.

---

# Staged implementation plan

## Part 0 — Repository and prototype audit (read-only)

**Goal:** establish the real current state before coding.

Inspect the roadmap, `analyze_lengths.py`, `analyze_event_lengths.py`, `tokenize_and_analyze.py`, `create_esr.py`, `source_parser.py`, pairing/validation/dataset/splitting scripts, actual split files, generated outputs, and repository status.

**Deliver:** status matrix (implemented/partial/planned/absent/broken), actual data flow and schemas, independently checked statistics where practical, documentation/code discrepancies, and unresolved design decisions.

**Acceptance gate:** no code changes; current state and recommended next step are evidenced.

## Part 1 — Runtime representation contract and schema audit

Inspect real ESR JSON and compact traces across event types. Reconcile actual attributes with illustrative schemas. Choose ESR or SRER terminology consistently based on repository/report conventions; do not rename files blindly. Specify deterministic event serialization, event boundaries, step handling, attribute order, and missing/null values.

**Acceptance gate:** a documented representation contract backed by real examples and schema tests.

## Part 2 — Event-aware runtime tokenizer

Inspect and reuse `tokenize_and_analyze.py` where appropriate. Define event-boundary and field markers based on Part 1. Preserve event order, event type, and attribute associations. Handle punctuation, whitespace, separators, reserved-token collisions, and missing values deterministically. Add focused tests.

**Acceptance gate:** representative events serialize deterministically; event count/order/type/attributes are preserved; tests pass.

## Part 3 — Event-preserving chunking and length analysis

Distinguish event count, serialized-token count, subword-token count, and model-token count. Re-profile lengths after the final tokenizer. Choose a configurable chunk limit based on measured distribution and available compute. Split only between complete events; define behaviour for a single oversized event. Decide whether chunks are independent, overlapping, or context-carrying. Preserve program ID, chunk index, event range/step, and ordering metadata. Keep all chunks from a program in its existing split.

**Acceptance gate:** chunks reconstruct the original event sequence exactly under the documented policy; no event is split; chunk lengths and split integrity are checked; before/after statistics are reported.

## Part 4 — Source tokenizer and source representation decision

Determine whether the encoder consumes raw Java lexical tokens, the existing Static Program Representation (SPR), or a justified combination. Do not assume these are interchangeable. Implement the selected deterministic representation and test simple, nested, and edge-case Java programs.

**Acceptance gate:** the input choice is justified, deterministic, tested, and documented.

## Part 5 — Vocabulary, special tokens, and token IDs

Choose separate source/target vocabularies or a shared vocabulary and document why. Define `<PAD>`, `<UNK>`, `<SOS>`, `<EOS>`, and approved event markers. Fit vocabulary only on training data. Persist vocabularies/configuration, implement token↔ID conversion, test round trips, and report unknown-token rates.

**Acceptance gate:** deterministic vocabulary and unique reserved IDs; encode/decode tests pass; validation/test data do not influence vocabulary fitting.

## Part 6 — Padding, masks, and model-ready dataset

Define source/target sequence policies using measured lengths. Implement padding, attention masks, decoder target shifting for teacher forcing, and consistent `<SOS>`/`<EOS>` handling. Create reproducible artifacts or a dataset loader without altering original data. Test shapes, dtypes, masks, empty sequences, batching, and split integrity. Never allocate based on the longest unchunked trace.

**Acceptance gate:** batches load reproducibly, masks correctly identify padding, target shifting is tested, no split leakage occurs, and memory use is reasonable.

## Part 7 — Transformer baseline and ESS definition

Define model input/output shapes, padding masks, causal target masks, and cross-attention masks. Give Execution Semantic Space (ESS) a precise definition in documentation and code. Implement a small configurable encoder-decoder, embeddings/positional information, output projection, and focused shape/gradient/mask tests.

**Acceptance gate:** a tiny synthetic batch completes a forward pass; shapes and masks are correct; gradients flow. Do not claim successful learning yet.

## Part 8 — Training and checkpointing

Implement teacher forcing, cross-entropy loss with padding ignored, optimizer, backpropagation, validation, and checkpointing. Run a short smoke test first. Track train/validation loss and resource usage. Save configuration, seed, vocabulary references, and checkpoint metadata. Never tune on the test set.

**Acceptance gate:** short training completes, validation runs, checkpoint saves/reloads, and split use is clean.

## Part 9 — Inference and trace reconstruction

Implement autoregressive decoding with documented stopping and maximum-length policies. Handle special tokens, malformed generations, and chunk ordering/context. Convert generated tokens back into structured events. Test on validation examples before test evaluation.

**Acceptance gate:** source input produces a decoded event sequence; termination and parse failures are handled and documented.

## Part 10 — Evaluation, error analysis, and final verification

Implement and define BLEU, exact match, event-type accuracy, and event-order accuracy, including alignment rules. Report validation and test results separately. Account for chunking when comparing complete traces. Categorize missing/extra events, wrong types/attributes/order, and termination errors. Perform ablations only after a reproducible baseline. State limitations and failure cases.

**Acceptance gate:** metric definitions and tests exist; results are reproducible; test-set use is controlled; claims are supported by measured results.

## Execution order

`Part 0 → Part 1 → Part 2 → Part 3 → Part 4 → Part 5 → Part 6 → Part 7 → Part 8 → Part 9 → Part 10`

Never skip a failed acceptance gate. Correct the current part and re-run its verification before proceeding.

---

# Antigravity Prompt 1 — Implement one part

Replace `[PART NUMBER AND TITLE]` with the current part only.

> You are working on the existing Java execution-trace learning project.
>
> **Primary reference:** `execution_trace_project_status_and_roadmap.md`  
> **Staged plan:** `Execution_Trace_Staged_Implementation_and_Verification_Plan.md`  
> **Current task:** implement **[PART NUMBER AND TITLE] only**.
>
> 1. Read the roadmap and the selected part's goal, tasks, and acceptance gate.
> 2. Inspect relevant source files, schemas, datasets, outputs, and tests before editing.
> 3. Classify requirements as already implemented, partial, missing, or broken. Reuse correct existing work.
> 4. Report contradictions between code and documentation; do not silently alter methodology or schemas.
> 5. Implement only this part. Avoid unrelated refactoring and do not start later parts.
> 6. Add/update focused tests and run relevant commands. Report exact commands and observed results.
> 7. Preserve original data and program-level train/validation/test separation.
> 8. Do not silently discard, truncate, or rewrite records. Make transformations explicit and measurable.
> 9. If a consequential design decision is unresolved, explain options and trade-offs before an irreversible change.
>
> **Final report required:** scope; initial status; files inspected; files changed and reasons; design decisions; commands/tests and actual results; acceptance-gate checklist marked pass/fail/not tested; limitations/risks; exact next step.
>
> Stop after this part. Do not automatically implement the next part.

---

# Antigravity Prompt 2 — Independently verify one part

Run after Prompt 1. Use the same `[PART NUMBER AND TITLE]`. This is an audit, not an implementation task.

> You are independently auditing the existing Java execution-trace learning project.
>
> **Primary reference:** `execution_trace_project_status_and_roadmap.md`  
> **Staged plan:** `Execution_Trace_Staged_Implementation_and_Verification_Plan.md`  
> **Part to verify:** **[PART NUMBER AND TITLE]**.
>
> 1. Read the selected part's requirements and acceptance gate.
> 2. Inspect the actual changed source, configuration, outputs, and tests. Do not rely only on the implementer's summary.
> 3. Trace the relevant data flow and determine whether requirements are actually satisfied.
> 4. Run appropriate safe tests and validation commands. Record exact commands and outputs.
> 5. Check normal cases, edge cases, determinism, compatibility, and data integrity as relevant.
> 6. Check for regressions, silent data loss, schema changes, hard-coded assumptions, data leakage, and unsupported claims.
> 7. Where relevant, verify all chunks from a program remain in its original dataset split.
> 8. **Do not edit files, fix failures, or implement missing features.** Report defects and recommended corrections.
> 9. Label findings verified, failed, not tested, or cannot verify. A syntax check alone is not proof of functional correctness.
>
> **Final report required:** overall verdict (PASS / PASS WITH LIMITATIONS / FAIL / INSUFFICIENT EVIDENCE); acceptance-gate checklist; files/code paths inspected; commands and observed outputs; functional/edge-case findings; data-integrity/leakage checks where relevant; defects with locations; unverified claims; required corrections; whether it is safe to proceed and why.
>
> Do not modify the project. Stop after the audit and wait for review.

---

# Operating checklist

For each part:

1. [ ] Create a recoverable checkpoint/commit.
2. [ ] Run Prompt 1 for that part.
3. [ ] Read the report and inspect the diff.
4. [ ] Run Prompt 2 for the same part.
5. [ ] Review evidence against the acceptance gate.
6. [ ] If FAIL or INSUFFICIENT EVIDENCE, correct only the current part and repeat verification.
7. [ ] Proceed only after acceptance.
8. [ ] Update the status roadmap to reflect what was actually verified, not merely attempted.

## Project-specific caution

The roadmap reports runtime traces reaching millions of tokens. Recheck these measurements after the final tokenizer: event count, serialized token count, subword-token count, and model-token count are different quantities. Chunking must be designed around the finalized representation and must preserve complete events and program-level split integrity.

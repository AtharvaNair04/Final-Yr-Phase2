# EST Project — Architecture Gap Analysis and Revised Implementation Plan

**Project:** Automatic Runtime Execution Trace Generation from Java Source Code Using Transformer-Based Neural Models  
**Model:** Execution Semantic Transformer (EST)  
**Reference date:** 18 October 2026  
**Target deadline:** 31 October 2026  
**Purpose:** Working reference for revising the implementation, running controlled experiments, and documenting final results.

---

## 1. Executive summary

The project has a functioning data-processing pipeline and an implemented Transformer encoder–decoder, but the current implementation does not fully match the intended architecture and the generated traces are not yet reliable.

The clearest confirmed implementation mismatch is that the **source vocabulary is built from raw Java files, while the model receives tokens extracted from Static Program Representation (SPR) records**. This leads to substantial unknown-token (`<UNK>`) usage on actual model inputs.

A second major issue is generation quality: teacher-forced token accuracy is substantially higher than greedy-generation token accuracy in the small held-out experiment. The model can memorize selected training examples, but this has not translated into reliable held-out trace generation.

The recommended strategy is to make the smallest evidence-backed changes first, preserve existing artifacts, and evaluate every change before expanding scope.

### Current overall assessment

- The data pipeline, paired dataset, Transformer components, and training utilities exist.
- The model can fit selected training examples.
- Current generation behavior is not yet reliable enough to claim successful runtime-trace reconstruction.
- The source-vocabulary/model-input mismatch is confirmed and should be addressed first.
- The proposed corrections are not guaranteed to solve generation quality; held-out evaluation is required.

---

## 2. Intended architecture

The supplied architecture diagram defines four layers.

### 2.1 Data Layer

1. IBM Project CodeNet Java programs.
2. Java source files (`.java`).
3. JIVE execution environment.
4. Ground-truth execution trace containing events such as thread start, type load, method call, and method entry.
5. Paired dataset containing source code and execution trace.

### 2.2 Preprocessing Layer

1. AST-aware tokenizer:
   - Parse Java code into an Abstract Syntax Tree (AST).
   - Tokenize source code.
2. Trace linearization:
   - Convert JIVE traces into a structured event sequence.
3. Subword tokenization:
   - Byte-Pair Encoding (BPE).
   - Prepare sequences for Transformer input.
4. Produce tokenized source and tokenized trace sequences.

### 2.3 Model Layer

- Source code tokens feed a Transformer encoder.
- Shifted trace tokens feed a Transformer decoder.
- Encoder contains multi-head self-attention, Add & Norm, feed-forward network, and Add & Norm.
- Decoder contains masked multi-head self-attention, Add & Norm, encoder–decoder cross-attention, Add & Norm, feed-forward network, and Add & Norm.
- The model produces a predicted execution-trace event sequence.

### 2.4 Evaluation Layer

The diagram specifies:

- BLEU score.
- Exact-match accuracy.
- Event-type accuracy.
- Event-order accuracy.
- Performance analysis.
- Ablation studies.
- Error analysis.

**Important:** A component in the diagram is considered implemented only when its actual code and behavior have been verified. A diagram label alone is not evidence of implementation.

---

## 3. Current implementation status

The statuses below reflect the code and experiments inspected by 18 October 2026. Items not conclusively verified are explicitly marked as such.

| Architecture component | Current finding | Status |
|---|---|---|
| Java source files | 566 matching Java files were identified | Implemented |
| JIVE trace preprocessing | Trace compaction, ESR generation, pairing and validation exist | Implemented |
| Paired source–trace data | 562 valid pairs; train/validation/test split created | Implemented |
| AST-aware source tokenizer | A regex-based Java lexer and recursive SPR tokenizer are present; an AST parser was not established | Different from diagram |
| Trace linearization | Runtime events are serialized as ordered tokens with structural markers | Implemented |
| BPE/subword tokenization | No BPE implementation was established during inspection | Not established |
| Source vocabulary | Built from raw Java tokens, although the model consumes SPR tokens | Confirmed mismatch |
| Target vocabulary | Capped at 50,000 tokens | Implemented; OOV remains relevant |
| Transformer encoder–decoder | Encoder, causal decoder, cross-attention, and output projection exist | Implemented |
| Shifted decoder inputs | Decoder input is `[BOS] + target`; labels are `target + [EOS]` | Implemented |
| Autoregressive generation | Generation runs, but outputs are often repetitive or incorrect | Mechanically implemented; quality inadequate |
| BLEU, exact match, event-type/order metrics | Some diagnostic metrics exist; full production evaluation coverage was not confirmed | Verify/complete |
| Ablation and error analysis | Several diagnostic experiments exist; a systematic final evaluation remains | Partial |

---

## 4. Confirmed data and pipeline facts

### 4.1 Dataset and splits

- 566 matching Java files were identified.
- 563 source–trace pairs were formed; 562 were valid after validation.
- Split seed: `42`.
- Train: 449 programs.
- Validation: 56 programs.
- Test: 57 programs.
- Program IDs were checked for cross-split overlap; none was found.
- JSONL split records contain `program_id`, `source` (SPR structure), and `runtime_trace`.
- Dataset code tokenizes `record["source"]` with `tokenize_source()` and encodes it using the source vocabulary.
- The dataset also chunks runtime traces; a chunk receives the full source representation rather than the previous trace chunk.

### 4.2 Existing tokenization and vocabulary

- `source_tokenizer.py` implements regex-based lexical tokenization for raw Java and recursive extraction/tokenization for structured SPR objects.
- `build_source_vocabulary()` in `vocabulary.py` reads raw Java files associated with the training split.
- The existing source vocabulary has 3,155 entries.
- Special IDs are:
  - `<PAD>` = 0
  - `<UNK>` = 1
  - `<BOS>` = 2
  - `<EOS>` = 3
- The current source builder is invoked with `max_size=None` and `min_freq=1`; there is no configured source-vocabulary cap in this call.
- `ExecutionTraceDataset` encodes the SPR tokens from each record with this same source vocabulary.
- `ModelConfig.src_vocab_size` is configurable, but existing checkpoints depend on their original vocabulary size and token-to-ID mapping. A new vocabulary must not be substituted into an old checkpoint without explicit compatibility handling.

### 4.3 Existing target trace representation

- Runtime events are linearized into token sequences.
- Structural markers include `<EVENT_START>`, `<EVENT_END>`, `<ATTR_START>`, `<ATTR_END>`, and `<EMPTY_VAL>`.
- The serializer preserves event count/order/type/step, attribute keys and boundaries, and lexical value-token sequence; it does not preserve whitespace.
- Target vocabulary size is 50,000, with structural tokens reserved in addition to the four general special tokens.
- Runtime traces can be extremely long; the dataset uses chunking.
- Dataset splits contain many more chunks than programs, so chunk-level sampling and reporting must be interpreted carefully.

---

## 5. Source vocabulary mismatch: evidence and interpretation

### 5.1 The two different paths

**Vocabulary construction path:**

`Training Java files → tokenize_java_code() → source vocabulary`

**Actual model-input path:**

`SPR JSON in JSONL → tokenize_source(record["source"]) → source_vocab.encode() → source token IDs`

The vocabulary is fitted on raw Java lexical tokens, but the model input is created from SPR field names and values. These are different token distributions.

### 5.2 Measured OOV results

A raw-Java diagnostic using the same `tokenize_java_code()` function as the vocabulary builder reported:

- Total source tokens: 29,194.
- Unknown occurrences: 959.
- Raw-Java OOV rate: 3.2849%.
- Distinct unknown token strings: 214.

A separate diagnostic using the actual model-input path, `tokenize_source(record["source"])` on validation SPR records, reported:

- Validation programs containing unknowns: 56 / 56.
- SPR model-input tokens: 6,005.
- Unknown occurrences: 2,125.
- Model-input OOV rate: 35.3872%.
- Distinct unknown tokens: 241.

Examples of missing SPR structural tokens include:

- `program_id`
- `file`
- `package`
- `classes`
- `interfaces`
- `methods`
- `constructors`
- `variables`
- `method_calls`
- `control_statements`
- `qualifier`
- `parameters`
- `return_type`

`qualifier` alone appeared as an unknown 821 times in the reported validation SPR tokenization diagnostic. `parameters` appeared 185 times and `return_type` 172 times.

### 5.3 What is confirmed and what is not

**Confirmed:** the source vocabulary is constructed from raw Java tokens, while the model consumes SPR-derived tokens. The measured OOV rate on the actual SPR model input is 35.39% for the validation records.

**Not confirmed:** the exact proportion of generation failure caused by this mismatch. The model also has a substantial teacher-forced-versus-greedy-generation gap, and other causes may contribute.

**Next correction:** build a separate candidate source vocabulary from the training split's SPR representations using the same `tokenize_source()` function as the dataset. Preserve the current vocabulary and all existing checkpoints. Evaluate candidate coverage on train and validation without fitting on validation or test.

Do not rebuild or overwrite the existing vocabulary until the candidate has been independently validated.

---

## 6. Generation-quality findings

### 6.1 Small held-out diagnostic

A checkpoint trained on eight selected training examples was evaluated on eight different test programs. This is a small diagnostic sample, not a full-test benchmark.

| Metric | Result |
|---|---:|
| Mean teacher-forced token accuracy | 69.0152% |
| Mean greedy token accuracy | 11.9169% |
| Exact matches | 0 / 8 |
| EOS termination | 8 / 8 |
| Structurally valid outputs | 7 / 8 |

The generated outputs frequently had incorrect event types, step values, or repeated patterns. Some outputs were structurally valid while still being semantically incorrect.

### 6.2 Interpretation

Teacher forcing supplies the correct previous target tokens when evaluating the next-token prediction. Greedy generation instead feeds the model's own predictions back into the decoder. Early mistakes can accumulate and cause subsequent predictions to diverge.

The observed gap is evidence of a generation problem. It does not, by itself, prove a specific root cause such as exposure bias or an architectural defect.

### 6.3 Training-example overfit experiment

A 1,000-step experiment on eight selected examples showed:

- Step 0 mean teacher-forced accuracy around 9.54%.
- Step 200 mean teacher-forced accuracy around 83.75%.
- Step 400 onward teacher-forced accuracy reached 100%.
- At the final checkpoint, mean greedy token accuracy on the selected training examples was approximately 96.88%.
- Four of eight examples were exact matches in the reported final metrics.
- All eight outputs terminated with EOS and passed structural validation.

This demonstrates that the model can memorize these selected sequences. It does **not** demonstrate generalization to unseen programs.

### 6.4 Source-conditioning diagnostic

A separate experiment compared correct source inputs with alternative source inputs for eight selected test programs:

- 56 wrong-source comparisons were made.
- Correct-source mean loss: 2.4283.
- Wrong-source mean loss: 2.3597.
- Correct source achieved lower loss in 26 / 56 comparisons (46.4%).
- Mean prediction change rate when source was changed: 10.63%.

Interpretation: source changes do influence predictions, but the correct source did not consistently improve teacher-forced loss in this small diagnostic. This suggests source conditioning may be weak or ineffective, but it does not prove the encoder–decoder architecture is inherently defective.

---

## 7. What should not be changed prematurely

1. Do not discard or recreate the dataset without evidence that it is invalid.
2. Do not overwrite the current vocabularies or checkpoints.
3. Do not replace the Transformer architecture before testing the confirmed preprocessing mismatch.
4. Do not introduce AST parsing, BPE, and an architecture change simultaneously; doing so would make the effect of each change difficult to identify.
5. Do not treat structural validity as proof of trace correctness.
6. Do not treat a small pilot or eight-program diagnostic as a final performance benchmark.
7. Do not resume a checkpoint with a different vocabulary mapping.
8. Do not use Antigravity for this workflow.

---

## 8. Revised implementation plan

The plan prioritizes small, reversible changes with measurable acceptance criteria.

### Phase 1 — Candidate SPR source vocabulary

**Goal:** make vocabulary construction consistent with the actual model input.

Tasks:

1. Add a separate candidate-vocabulary construction path rather than silently changing the existing raw-Java builder.
2. Read only the training split's SPR records.
3. Tokenize each SPR with the same `tokenize_source()` function used by `ExecutionTraceDataset`.
4. Build a deterministic vocabulary, preserving the established special-token IDs.
5. Save the candidate as a separate artifact; do not overwrite the current vocabulary.
6. Measure OOV on training and validation SPR inputs. Do not use validation/test data to fit the vocabulary.
7. Inspect vocabulary size, token ordering, special IDs, and coverage.

Acceptance criteria:

- Candidate vocabulary construction is deterministic.
- The special-token IDs remain correct.
- Structural SPR field names used in the training data are represented.
- Validation OOV is substantially lower than the current 35.39% SPR-input OOV.
- Existing vocabulary and checkpoints remain unchanged.

### Phase 2 — Pipeline and compatibility checks

Tasks:

1. Run existing unit tests.
2. Construct a fresh model whose `src_vocab_size` matches the candidate vocabulary.
3. Run a small forward pass with a batch from the existing dataset pipeline.
4. Verify tensor shapes, masks, finite logits, finite loss, and finite gradients.
5. Verify source vocabulary IDs are consistent between dataset construction and model configuration.

Acceptance criteria:

- Existing tests pass, or any failures are understood and documented.
- The fresh model runs a training step without dimension or numerical errors.
- No old checkpoint is accidentally loaded with the new vocabulary.

### Phase 3 — Controlled training pilot

Tasks:

1. Train a new checkpoint from scratch using the candidate source vocabulary.
2. Keep the model architecture and other experimental variables fixed initially.
3. Record training and validation loss, token accuracy, and generation metrics.
4. Keep experiment outputs and checkpoints separate from existing experiments.
5. Use validation performance for checkpoint selection, not test performance.

Acceptance criteria:

- Training is reproducible from saved configuration and seeds.
- Validation metrics and generation samples are recorded.
- The experiment can be compared fairly with the existing baseline.

### Phase 4 — Held-out generation evaluation

Evaluate the candidate model on held-out programs and compare against the existing baseline.

Report:

- Teacher-forced token accuracy.
- Greedy-generation token accuracy.
- Exact match.
- EOS termination.
- Structural validity.
- Event-type accuracy.
- Event-order accuracy.
- BLEU, if retained as part of the evaluation design.
- Error examples showing correct and incorrect event types, steps, attributes, and values.

Use the same examples and evaluation rules for the baseline and candidate. Preserve the test split for final evaluation; use validation for iterative development.

Acceptance criteria:

- Results are computed over a clearly defined sample or the full test set.
- The report clearly distinguishes diagnostic subsets from final evaluation.
- Structural validity is not presented as semantic correctness.
- Improvements are supported by measured comparisons rather than assumed.

### Phase 5 — Decide whether to expand preprocessing

Only after the candidate-vocabulary experiment:

- Decide whether BPE/subword tokenization is necessary.
- Decide whether AST-aware source parsing is necessary for the intended research claim.
- Make one change at a time and evaluate its effect.
- Update the architecture diagram and report to reflect what was actually implemented.

Because the deadline is 31 October 2026, do not begin a major tokenizer or architecture rewrite unless the simpler correction fails and there is sufficient time for testing.

### Phase 6 — Final documentation

Document:

- Final architecture and any deviations from the original design.
- Dataset construction and split protocol.
- Vocabulary/tokenization method.
- Model configuration and training setup.
- Evaluation metrics and held-out results.
- Error analysis, limitations, and reproducibility instructions.

---

## 9. Recommended experiment tracking table

Keep this table updated for every controlled experiment.

| Experiment | Source representation/vocabulary | Model initialization | Training subset | Validation results | Held-out generation | Status |
|---|---|---|---|---|---|---|
| Existing pilot | Raw-Java vocabulary / SPR model input | Existing pilot checkpoint | Small pilot | Recorded in experiment outputs | Poor on selected held-out sample | Baseline |
| Candidate SPR vocabulary | To be built from training SPR only | Fresh model | To be decided | To be measured | To be measured | Planned |
| Follow-up experiment | Change only one factor | Fresh or explicitly compatible checkpoint | To be documented | To be measured | To be measured | Not started |

Record the exact vocabulary artifact, checkpoint path, configuration, random seed, sample selection, and metric calculation for each run.

---

## 10. Important implementation notes

- Source vocabulary construction should match the representation consumed by the model.
- The model's source embedding size must equal the candidate source vocabulary size.
- A checkpoint's embedding rows are tied to the vocabulary IDs used during training. Matching only the vocabulary size is not enough if token-to-ID mappings differ.
- Keep existing files and checkpoints intact until the candidate pipeline has passed checks.
- A valid event sequence may still have incorrect semantics or runtime values.
- Runtime traces depend on the executed input, execution path, and runtime environment. If these conditions are not represented in the model input, one source program may not determine a unique trace.
- The source-vocabulary correction is a justified next step, not a guarantee of high accuracy.
- The implementation should be judged by reproducible held-out behavior, not training loss alone.

---

## 11. Immediate next action

**Do not train yet.**

First implement and validate a separate candidate source vocabulary built from training SPR records. Then measure validation OOV, run tests, and perform a fresh-model smoke test. Only proceed to a new training pilot after those checks pass.

This is the smallest confirmed correction to try before making broader changes to the tokenizer or Transformer architecture.

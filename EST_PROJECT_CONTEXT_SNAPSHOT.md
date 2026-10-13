# Final-Year Project Snapshot --- EST Dataset Pipeline

**Snapshot date:** 14 October 2026\
**Project:** *Automatic Runtime Execution Trace Generation from Java
Source Code Using Transformer-Based Neural Models*\
**Model direction:** Execution Semantic Transformer (EST)\
**Purpose:** Handoff snapshot before a teammate begins Module 4, Part 7.
This records the current verified state; it does not claim the
Transformer is implemented or trained.

## 1. Current status

  -------------------------------------------------------------------------
  Area                      Status                  Notes
  ------------------------- ----------------------- -----------------------
  Module 3 --- data         Completed earlier       562 valid source/trace
  preparation and paired                            pairs; fixed
  dataset                                           train/validation/test
                                                    split

  Module 4 ---              Complete                Analysis
  sequence-length analysis                          scripts/findings exist
                                                    from earlier work

  Source tokenizer          Implemented and tested  Regex-based Java
                                                    lexical tokenizer;
                                                    limitations below

  Runtime trace             Implemented and tested  Event-aware
  tokenizer/serialization                           representation

  Chunking and baseline     Implemented and tested  Normal chunk limit
  measurement                                       1,024 target tokens

  Source/target             Implemented and tested  Existing JSON
  vocabularies                                      artifacts; target cap
                                                    50,000

  PyTorch execution         Implemented,            Accepted with a
  dataset/DataLoader        unit-tested, and        performance note
                            smoke-tested on all     
                            real splits             

  **Module 4, Part 7 ---    **Not implemented**     This is the next stage
  EST Transformer model**                           

  Training, model           Not established as      Do not claim these are
  selection, final test     complete                done
  evaluation, inference                             
  -------------------------------------------------------------------------

Most recent full suite: **109 tests passed** --- 17 chunking, 21
dataset, 26 runtime tokenizer, 25 source tokenizer, and 20 vocabulary
tests.

## 2. Repository and environment

-   Original repository root:
    `/Users/karthikkishor/Development/FinalYearProject`
-   Last reported branch: `main`
-   Python environment: `.venv/`, Python 3.11 and PyTorch
-   Raw Java sources: `selected source code/`
-   Split data: `Module3_Training_Dataset/split/`
-   Vocabulary artifacts: `vocabularies/`
-   Tests: `tests/`

Relevant files reported:

``` text
source_tokenizer.py
tokenize_and_analyze.py
chunk_trace.py
measure_baseline.py
vocabulary.py
build_vocabularies.py
execution_dataset.py
tests/test_source_tokenizer.py
tests/test_runtime_tokenizer.py
tests/test_chunk_trace.py
tests/test_vocabulary.py
tests/test_execution_dataset.py
vocabularies/vocab_source.json
vocabularies/vocab_target.json
```

Earlier pipeline scripts include `source_parser.py`,
`compact_traces.py`, `create_esr.py`, `extract_matching_sources.py`,
`create_training_pairs.py`, `validate_pairs.py`,
`create_training_dataset.py`, `split_dataset.py`, and
`analyze_lengths.py`. Confirm exact filenames in the current checkout
before relying on this list.

### Git state recorded before this snapshot

The last reported untracked paths were:

``` text
build_vocabularies.py
execution_dataset.py
tests/test_execution_dataset.py
tests/test_vocabulary.py
vocabularies/
vocabulary.py
```

`git diff --check` returned no output. Since the files were untracked,
`git diff --stat` did not include them. This is historical state;
recheck the current working tree:

``` bash
git status --short
git diff --check
git check-ignore .venv
```

Do not commit, push, reset, or discard anything without the repository
owner's explicit approval. Review the actual working tree before
committing.

## 3. Dataset and split facts

The JSONL `source` field is an **SPR dictionary**, not raw Java.
`execution_dataset.py` loads the matching raw source from
`selected source code/{program_id}.java`.

  Split          Records        Chunks
  ------------ --------- -------------
  Train              449       112,513
  Validation          56        10,976
  Test                57        10,503
  **Total**      **562**   **133,992**

Splits were reported as seed 42, 80%/10%/10% by record, with no
program-ID overlap in the earlier validation. Do not recreate or
reshuffle them during Part 7.

Other reported facts: - 566 Java files were matched to JIVE CSV traces
during preprocessing. - 562 valid pairs remained; one invalid pair was
`p00295`. - Four extra Java files relative to the split records:
`p00038.java`, `p00039.java`, `p00048.java`, `p00295.java`. - Train:
5,571,285 events and 113,735,609 serialized runtime tokens. -
Validation: 499,803 events and 11,054,858 serialized runtime tokens. -
Test: 491,727 events and 10,608,894 serialized runtime tokens. - Total:
6,562,815 events and 135,399,361 serialized runtime tokens.

Historical split hashes from an earlier audit (verify actual filenames
and hashes before using these as an integrity guarantee):

``` text
train.jsonl  88809a79dae488942c65f9da55a363eaed33618de93a1149b2aff32b3fd97178
val.jsonl    3041a8f9e661f50a535b2526e599e730e185f4630577a840ccaa8a91f64ef629
test.jsonl   8003a4aeef06201df0fb4783e1bce41811182a8f8ca05fb60b3ea62478249858
```

## 4. Tokenization and chunking

### Source tokenizer

`source_tokenizer.py` provides source lexical tokenization. Tests fixed
comment-removal corruption for strings containing `//` and `/*...*/`,
plus issues involving `...`, duplicate operators, and leading-dot
floating-point literals.

Known limitations: it is regex-based, not a complete Java parser; exotic
Unicode behavior may differ from the Java Language Specification;
unmatched characters may be skipped; and `>>` requires care around
nested generic types. Do not replace it casually in Part 7. If a change
is necessary, isolate it, add regression tests, and audit
vocabulary/encoding effects.

### Runtime trace serialization

General format:

``` text
<EVENT_START> TYPE <type_val> STEP <step_val>
[<ATTR_START> <key> <val_tokens...> <ATTR_END>]*
<EVENT_END>
```

Structural tokens include `<EVENT_START>`, `<EVENT_END>`,
`<ATTR_START>`, `<ATTR_END>`, `<EMPTY_VAL>`, `TYPE`, `STEP`, and `\`.
Reserved literal tokens/backslashes are escaped. Tests check event
structure and ordered lexical token sequences, normalizing
intra-attribute whitespace.

### Chunking

-   Normal chunk size: 1,024 target tokens.
-   Dataset indexing calculates chunk counts and maps a global index to
    a record/chunk.
-   The dataset uses the `separate` oversized-event policy with strict
    tokenization/chunking.
-   An audit compared `separate` and `keep` on all three real splits and
    found **zero oversized events** in those splits. Thus, no actual
    event was excluded by the current policy in that audit.
-   Changing chunking policy or maximum length affects examples and
    boundaries; do not change casually.

## 5. Vocabulary state

Files: `vocabularies/vocab_source.json` and
`vocabularies/vocab_target.json`.

-   Source vocabulary: **3,155** entries --- 3,151 observed training
    lexical tokens plus four shared special tokens.
-   Target vocabulary: **50,000** entries, including reserved tokens.
-   Shared IDs: `<PAD>=0`, `<UNK>=1`, `<BOS>=2`, `<EOS>=3`.
-   Target structural IDs: `<EVENT_START>=4`, `<EVENT_END>=5`,
    `<ATTR_START>=6`, `<ATTR_END>=7`, `<EMPTY_VAL>=8`, `TYPE=9`,
    `STEP=10`, and `\=11`.
-   The target cap excludes 372,179 distinct observed token types
    (88.16% of observed types).
-   Reported target occurrence-level OOV rates: validation 3.4969%; test
    6.4139%.
-   Reported sequences containing at least one OOV: validation 92.86%;
    test 91.23%.

`<UNK>` loses the identity of an out-of-vocabulary dynamic value; do not
claim exact reconstruction of unknown values. Keep vocabulary artifacts
and special-token IDs consistent. An earlier note mentioned `<SOS>` as
an alias, but it is **not implemented**. Use `<BOS>` / `bos_id`; do not
assume `<SOS>` exists.

## 6. `execution_dataset.py`: behavior and audit

The implementation: 1. Indexes JSONL byte offsets and chunk counts. 2.
Loads raw Java from `selected source code/{program_id}.java`, rather
than treating the JSONL SPR dictionary as source code. 3.
Tokenizes/encodes source and obtains the requested runtime-trace chunk.
4. Uses teacher forcing: decoder input is `[BOS] + chunk_ids`; target
labels are `chunk_ids + [EOS]`. 5. Dynamically pads batches. The collate
function creates boolean padding masks and a loss mask selecting valid
target positions (`~target_padding_mask`); IDs are long integers and
masks are booleans.

Implementation considerations: - Indexing tokenizes/chunks each trace;
the trace is re-tokenized/re-chunked on cache misses. An LRU trace cache
defaults to 16 records. - The source cache is an unbounded dictionary
keyed by program ID. This was not shown to cause a correctness issue in
the smoke test; revisit only if measured memory pressure occurs during
training. - `create_dataloader` uses a lambda collate function. Default
`num_workers=0` works on the current macOS setup. If multiprocessing
workers are enabled later, verify lambda pickling/spawn compatibility. -
Source truncation is possible if `max_source_length` is configured;
default is `None`. Avoid silent truncation unless deliberately
configured and documented. - The actual split audit found no oversized
events.

### Real-split smoke test

Indexing and first-batch retrieval succeeded for all splits. Batch
shapes/masks were checked and each split reported PASS.

  --------------------------------------------------------------------------
  Split               Records         Chunks  Indexing time   Python-tracked
                                                                 peak memory
  ------------ -------------- -------------- -------------- ----------------
  Train                   449        112,513       181.19 s        444.5 MiB

  Validation               56         10,976        17.45 s        395.9 MiB

  Test                     57         10,503        16.96 s        318.4 MiB
  --------------------------------------------------------------------------

These are measurements from one run, not performance guarantees. **Part
6 is accepted with a performance note.** Functional requirements passed
unit tests and real-data smoke testing. Do not keep expanding Part 6
checks without a concrete issue.

## 7. Next stage --- Module 4, Part 7: EST Transformer model

**Not implemented yet.** The teammate should implement only Part 7 using
the exact prompt/specification provided by the project owner.

Before coding, inspect the actual repository and at least: -
`execution_dataset.py` and `tests/test_execution_dataset.py` -
`vocabulary.py` and both vocabulary JSON files - `source_tokenizer.py` -
`chunk_trace.py` and runtime serialization/tokenization code - project
design notes and the Part 7 prompt

Constraints to preserve: - Input is raw Java loaded from
`selected source code/`, tokenized/encoded with the existing source
tokenizer/vocabulary. - Target is the chunked, event-aware serialized
runtime trace encoded with the existing target vocabulary. - Respect
`<PAD>`, `<UNK>`, `<BOS>`, `<EOS>` and target structural-token IDs. -
Preserve train/validation/test membership. Do not rebuild or reshuffle
the dataset. - Use teacher-forcing inputs/labels correctly; prevent
target leakage. - Apply source and target padding masks, and use a
causal decoder mask so target positions cannot attend to future target
tokens. - Keep model implementation, training loop, checkpointing,
evaluation, and inference as separate stages if the project plan does
so. Do not silently implement future stages as part of Part 7. - Add
focused tests using small synthetic batches for model forward pass,
shapes, and masks. Do not launch full-dataset training as part of a
model-only stage. - Do not regenerate vocabularies or change
tokenizer/data code unless Part 7 explicitly requires it and the change
is separately justified and tested.

After implementation, independently review the actual diff and tests. Do
not rely only on the coding agent's completion summary.

## 8. Handoff sequence

1.  Review the current Git working tree and commit this snapshot with
    the already-reviewed Part 5/6 code and tests.
2.  Share the repository and exact Part 7 prompt with the teammate.
3.  Have the teammate inspect the repository, then implement **Part 7
    only**.
4.  Run relevant tests and review the diff; do not begin the training
    stage until Part 7 has been independently accepted.
5.  Update this snapshot only after later stages have actually been
    implemented and verified.

## 9. Guardrails

-   Do not claim the Transformer is implemented, trained, or evaluated.
-   Do not alter split membership or regenerate data/vocabularies
    without an explicit, justified need.
-   The dataset and `.venv` are ignored by Git, so `git status` alone
    does not prove dataset integrity.
-   Do not commit secrets, `.env` files, model checkpoints, large
    datasets, or the virtual environment.
-   Preserve existing code/artifacts unless a verified issue requires a
    targeted change.

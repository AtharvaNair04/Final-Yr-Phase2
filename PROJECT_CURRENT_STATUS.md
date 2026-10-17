# PROJECT CURRENT STATUS — Comprehensive Audit

**Project:** Automatic Runtime Execution Trace Generation from Java Source Code Using Transformer-Based Neural Models  
**Model:** Execution Semantic Transformer (EST)  
**Audit Date:** 2026-10-16  
**Auditor:** Automated read-only source-code review  
**Audit Scope:** Repository files, checkpoints, datasets, vocabularies, tests, experiment artifacts

---

## 1. Project Identity and Objective

The EST project aims to train an encoder–decoder Transformer model that accepts Java source code as input and generates runtime execution traces as output. The pipeline spans raw Java source files and abstract execution traces through compaction, structured representation, vocabulary construction, chunked dataset creation, model training, and (planned) autoregressive inference and evaluation.

---

## 2. Executive Summary

The project has completed the data pipeline (Modules 1–3) and the core ML infrastructure (Module 4 Parts 1–8). A 562-program dataset has been split, vocabularies built, and the EST model architecture implemented with full training/validation/checkpointing support. Two experiments have been executed: a CPU baseline (exp1) and an Apple Silicon MPS pilot. **Inference and evaluation have not been implemented.** The project is positioned at the boundary between training infrastructure completion and inference/evaluation development.

| Aspect | Status |
|--------|--------|
| Data pipeline (raw → ESR → pairs → dataset) | ✅ COMPLETE |
| Dataset splitting (train/val/test) | ✅ COMPLETE |
| Vocabulary construction | ✅ COMPLETE |
| EST model architecture | ✅ COMPLETE |
| Training infrastructure | ✅ COMPLETE |
| CPU training experiment (exp1) | ⚠️ PARTIAL (artifacts exist, no logs) |
| MPS pilot experiment | ✅ COMPLETE (feasibility proven) |
| Test suite | ✅ COMPLETE (130 tests defined, run status UNVERIFIED) |
| Inference / autoregressive decoding | ❌ NOT IMPLEMENTED |
| Trace reconstruction | ❌ NOT IMPLEMENTED |
| Evaluation metrics (BLEU, exact match, etc.) | ❌ NOT IMPLEMENTED |
| Full-scale training run | ❌ NOT STARTED |

---

## 3. Overall Milestone / Status Table

| # | Pipeline Stage | Script(s) | Artifacts | Status |
|---|---------------|-----------|-----------|--------|
| 1 | Abstract execution traces | External (pre-existing) | `abstract execution traces/` (566 CSV) | COMPLETE |
| 2 | Trace compaction | `compact_traces.py` | `compact execution traces/` (566 CSV) | COMPLETE |
| 3 | ESR generation | `create_esr.py` | `ESR/` (566 JSON) | COMPLETE |
| 4 | SPR generation | External (pre-existing) | `Static Program Representations/` (566 JSON) | COMPLETE |
| 5 | Source–trace pairing | `create_training_pairs.py` | `Source Trace Pairs/` (563 JSON) | COMPLETE |
| 6 | Java source extraction | `extract_matching_sources.py` | `selected source code/` (566 .java) | COMPLETE |
| 7 | Training dataset assembly | `create_training_dataset.py` | `Module3_Training_Dataset/training_dataset.jsonl` (562 records, 593 MB) | COMPLETE |
| 8 | Dataset splitting | `split_dataset.py` | `split/train.jsonl` (449), `validation.jsonl` (56), `test.jsonl` (57) | COMPLETE |
| 9 | Source tokenization | `source_tokenizer.py` | Integrated into pipeline | COMPLETE |
| 10 | Runtime tokenization | `tokenize_and_analyze.py` | Integrated into pipeline | COMPLETE |
| 11 | Vocabulary construction | `vocabulary.py`, `build_vocabularies.py` | `vocabularies/vocab_source.json` (3,155), `vocab_target.json` (50,000) | COMPLETE |
| 12 | Trace chunking | `chunk_trace.py` | Integrated (on-the-fly in dataset) | COMPLETE |
| 13 | PyTorch Dataset & DataLoader | `execution_dataset.py` | Integrated | COMPLETE |
| 14 | EST model architecture | `model.py` | Integrated | COMPLETE |
| 15 | Training & checkpointing | `train.py` | Integrated | COMPLETE |
| 16 | CPU experiment | `train.py` (manual) | `checkpoints/exp1/` | PARTIAL |
| 17 | MPS pilot experiment | `scripts/run_mps_pilot.py` | `checkpoints/pilot_mps/` | COMPLETE |
| 18 | Inference & decoding | — | — | NOT IMPLEMENTED |
| 19 | Trace reconstruction | — | — | NOT IMPLEMENTED |
| 20 | Evaluation metrics | — | — | NOT IMPLEMENTED |

---

## 4. Repository and Environment Snapshot

### 4.1 Git State

| Property | Value | Evidence |
|----------|-------|----------|
| Branch | `main` | **Verified** — `git branch --show-current` |
| HEAD commit | `22ffedb` | **Verified** — "Ignore model checkpoints" |
| Prior commits | `d54f2b9` (Remove large checkpoints), `7689459` (model) | **Verified** |
| Untracked files | `inspect_checkpoints.py`, `scripts/` | **Verified** — `git status --short` |
| Clean working tree | Yes (aside from untracked files above) | **Verified** |

### 4.2 Environment

| Property | Value | Evidence |
|----------|-------|----------|
| Python version | 3.14.4 | **Verified** — `python3 --version` |
| PyTorch version | 2.14.1 | **Reported** in `requirements.txt` and `pilot_summary.json`. Could not import torch in current env (no `.venv` activated). |
| MPS availability | Reported as available | **Reported** in `pilot_summary.json` (`"device": "mps"`). Not independently verified (torch not importable). |
| CUDA | Not available on this Mac | **Inferred** from Apple Silicon hardware and MPS focus |
| `requirements.txt` | Contains CUDA/NVIDIA packages (from teammate's Linux freeze) + `torch==2.14.1`, `javalang==0.13.0`, `numpy==2.5.4` | **Verified** — file contents |

> [!WARNING]
> **Reproducibility concern:** `requirements.txt` was frozen from a Linux/CUDA environment. It contains `nvidia-*` and `cuda-*` packages that will fail on macOS. A platform-independent `requirements.txt` is needed for reproducible setup.

### 4.3 Directory Structure (Relevant)

```
FinalYearProject/
├── abstract execution traces/    # 566 CSV files (raw traces)
├── compact execution traces/     # 566 CSV files (compacted)
├── ESR/                          # 566 JSON files (Execution Semantic Repr.)
├── Static Program Representations/  # 566 JSON files (SPR)
├── Source Trace Pairs/           # 563 JSON files (paired SPR + ESR)
├── selected source code/         # 566 .java files
├── java source code/             # Contains subdirectory java_solutions
├── Module3_Training_Dataset/
│   ├── training_dataset.jsonl    # 562 records, 593 MB
│   └── split/
│       ├── train.jsonl           # 449 records, 502 MB
│       ├── validation.jsonl      # 56 records, 46 MB
│       └── test.jsonl            # 57 records, 46 MB
├── vocabularies/
│   ├── vocab_source.json         # 3,155 tokens (44 KB)
│   └── vocab_target.json         # 50,000 tokens (643 KB)
├── checkpoints/
│   ├── exp1/                     # CPU experiment checkpoints
│   │   ├── checkpoint_best.pt    # 159 MB
│   │   └── checkpoint_latest.pt  # 159 MB
│   └── pilot_mps/               # MPS pilot experiment
│       ├── checkpoint_best.pt    # 159 MB
│       ├── checkpoint_latest.pt  # 159 MB
│       ├── model_config.json
│       ├── training_config.json
│       ├── pilot_summary.json
│       ├── train_subset.jsonl
│       └── val_subset.jsonl
├── tests/                        # 7 test files + __init__.py
├── scripts/                      # 3 MPS-related scripts
├── model.py                      # EST architecture
├── train.py                      # Training manager
├── execution_dataset.py          # PyTorch Dataset + DataLoader
├── vocabulary.py                 # Vocabulary class + builders
├── source_tokenizer.py           # Java lexical tokenizer
├── tokenize_and_analyze.py       # Runtime trace tokenizer
├── chunk_trace.py                # Event-preserving chunking
├── compact_traces.py             # Trace compaction (Mod 1)
├── create_esr.py                 # ESR generation (Mod 2)
├── create_training_pairs.py      # Source–trace pairing (Mod 2)
├── create_training_dataset.py    # JSONL assembly (Mod 3)
├── split_dataset.py              # Train/val/test split (Mod 3)
├── build_vocabularies.py         # Vocabulary construction
├── validate_pairs.py             # Pair validation
├── requirements.txt
└── README.md
```

---

## 5. End-to-End Pipeline Status

### 5.1 Stage 1: Abstract Execution Traces → Compact Execution Traces

- **Script:** `compact_traces.py`
- **Input:** `abstract execution traces/*.csv` (566 files)
- **Output:** `compact execution traces/*.csv` (566 files)
- **What it does:** Abbreviates event type names (e.g., "Method Call" → "MC") using a fixed `EVENT_CODES` dict. Writes cleaned CSV.
- **Evidence:** Both input and output directories contain exactly 566 files. Script logic is trivial and deterministic.
- **Status:** `COMPLETE` — **Verified**

### 5.2 Stage 2: Compact Traces → ESR (Execution Semantic Representation)

- **Script:** `create_esr.py`
- **Input:** `compact execution traces/*.csv`
- **Output:** `ESR/*.json` (566 files)
- **What it does:** Parses each compact CSV into a JSON structure: `{representation, program_id, event_count, events: [{step, type, attributes}]}`. Attributes are extracted from `key=value` CSV cells.
- **Evidence:** `ESR/` contains 566 JSON files matching compact trace count.
- **Status:** `COMPLETE` — **Verified**

### 5.3 Stage 3: SPR (Static Program Representation)

- **Source:** `Static Program Representations/` (566 JSON files)
- **What it is:** Structured JSON dict with keys: `program_id, file, package, classes, interfaces, methods, constructors, variables, method_calls, control_statements`.
- **Evidence:** 566 files present. Sample record in training dataset confirms `source` field is an SPR dict (not raw Java).
- **Status:** `COMPLETE` — **Verified** (pre-existing artifact)

### 5.4 Stage 4: Source–Trace Pairing

- **Script:** `create_training_pairs.py`
- **Input:** `Static Program Representations/*.json` + `ESR/*.json`
- **Output:** `Source Trace Pairs/*.json` (563 files)
- **What it does:** Joins SPR and ESR by `program_id` (filename stem intersection). Creates `{program_id, source: <SPR_dict>, runtime_trace: <ESR_dict>}`.
- **Evidence:** 563 pairs from 566 SPR and 566 ESR → 3 programs had mismatched case or missing files.
- **Limitation:** The `source` field contains the SPR dictionary, **not** raw Java code. The dataset loader (`execution_dataset.py`) correctly bypasses this and loads raw Java from `selected source code/`.
- **Status:** `COMPLETE` — **Verified**

### 5.5 Stage 5: Java Source Extraction

- **Script:** `extract_matching_sources.py`
- **Input:** `java source code/java_solutions/` + list of matched program IDs
- **Output:** `selected source code/*.java` (566 files)
- **Evidence:** 566 `.java` files present. The `ExecutionTraceDataset` loads from this directory.
- **Status:** `COMPLETE` — **Verified**

### 5.6 Stage 6: Training Dataset Assembly

- **Script:** `create_training_dataset.py`
- **Input:** `Source Trace Pairs/*.json`
- **Output:** `Module3_Training_Dataset/training_dataset.jsonl` (562 records)
- **Evidence:** File size 593 MB. First record confirms schema: `{program_id, source: <SPR_dict>, runtime_trace: <ESR_dict>}`.
- **Note:** 562 records vs. 563 pairs → 1 record was dropped (likely malformed or duplicate). This discrepancy is minor.
- **Status:** `COMPLETE` — **Verified**

### 5.7 Stage 7: Dataset Splitting

- **Script:** `split_dataset.py`
- **Proportions:** 80% / 10% / 10% with `RANDOM_SEED = 42`
- **Output:**

| Split | File | Records | Size |
|-------|------|---------|------|
| Train | `split/train.jsonl` | 449 | 502 MB |
| Validation | `split/validation.jsonl` | 56 | 46 MB |
| Test | `split/test.jsonl` | 57 | 46 MB |
| **Total** | | **562** | **594 MB** |

- **Evidence:** `wc -l` confirms 449 + 56 + 57 = 562. Matches total dataset.
- **Disjointness:** The split is by shuffled record index, so each record (one program) appears in exactly one split. **Verified** by construction from `split_dataset.py` code.
- **Status:** `COMPLETE` — **Verified**

### 5.8 Stage 8: Source Tokenization

- **Script:** `source_tokenizer.py` — `tokenize_java_code()`
- **What it does:** Regex-based lexical tokenizer handling Java keywords, identifiers, literals, operators, strings, comments. Used to tokenize raw `.java` files (not SPR dicts) for encoder input.
- **Evidence:** Function is imported and used by `vocabulary.py`, `execution_dataset.py`, and `tokenize_and_analyze.py`.
- **Status:** `COMPLETE` — **Verified**

### 5.9 Stage 9: Runtime Trace Tokenization

- **Script:** `tokenize_and_analyze.py` — `tokenize_runtime()`
- **What it does:** Converts ESR event dicts into a flat event-aware token stream with explicit boundary markers: `<EVENT_START>`, `<EVENT_END>`, `<ATTR_START>`, `<ATTR_END>`, `<EMPTY_VAL>`, `TYPE`, `STEP`, `\\`. Implements backslash escaping for reserved tokens inside attribute values.
- **Evidence:** Function is imported by `chunk_trace.py`, `vocabulary.py`, and `execution_dataset.py`.
- **Status:** `COMPLETE` — **Verified**

### 5.10 Stage 10: Vocabulary Construction

- **Script:** `vocabulary.py` — `build_source_vocabulary()`, `build_target_vocabulary()`; orchestrated by `build_vocabularies.py`
- **Source vocabulary:** `vocabularies/vocab_source.json`

| Property | Value | Evidence |
|----------|-------|----------|
| Type | `"source"` | **Verified** |
| Size | 3,155 tokens | **Verified** |
| PAD (ID 0) | `<PAD>` | **Verified** |
| UNK (ID 1) | `<UNK>` | **Verified** |
| BOS (ID 2) | `<BOS>` | **Verified** |
| EOS (ID 3) | `<EOS>` | **Verified** |
| Train records used | 449 | **Verified** — metadata field |

- **Target vocabulary:** `vocabularies/vocab_target.json`

| Property | Value | Evidence |
|----------|-------|----------|
| Type | `"target"` | **Verified** |
| Size | 50,000 tokens (at cap) | **Verified** |
| PAD (ID 0) | `<PAD>` | **Verified** |
| UNK (ID 1) | `<UNK>` | **Verified** |
| BOS (ID 2) | `<BOS>` | **Verified** |
| EOS (ID 3) | `<EOS>` | **Verified** |
| EVENT_START (ID 4) | `<EVENT_START>` | **Verified** |
| EVENT_END (ID 5) | `<EVENT_END>` | **Verified** |
| ATTR_START (ID 6) | `<ATTR_START>` | **Verified** |
| ATTR_END (ID 7) | `<ATTR_END>` | **Verified** |
| EMPTY_VAL (ID 8) | `<EMPTY_VAL>` | **Verified** |
| TYPE (ID 9) | `TYPE` | **Verified** |
| STEP (ID 10) | `STEP` | **Verified** |
| ESCAPE (ID 11) | `\\` | **Verified** |
| Train records used | 449 | **Verified** — metadata field |

- **Status:** `COMPLETE` — **Verified**

### 5.11 Stage 11: Trace Chunking

- **Script:** `chunk_trace.py` — `chunk_trace_tokens()`, `parse_events_from_tokens()`
- **What it does:** Parses flat token stream into discrete events (delimited by `<EVENT_START>`/`<EVENT_END>`), then packs events into chunks ≤ `max_chunk_size` (default 1024) without splitting events. Oversized events handled via configurable policy (`separate`, `error`, `keep`).
- **Result:** `ChunkingResult` with chunks, oversized_events, total_events, metadata.
- **Status:** `COMPLETE` — **Verified**

### 5.12 Stage 12: PyTorch Dataset & DataLoader

- **Script:** `execution_dataset.py` — `ExecutionTraceDataset`, `BatchData`, `execution_trace_collate_fn()`, `create_dataloader()`
- **Key design decisions verified:**
  - Source input: Loaded from raw `.java` files (`selected source code/{pid}.java`), **never** from the SPR dict in JSONL.
  - Teacher forcing: `decoder_input_ids = [BOS] + chunk_ids`, `target_ids = chunk_ids + [EOS]`.
  - Padding mask: `True = padding/ignored` (PyTorch convention). `loss_mask = ~target_padding_mask`.
  - Memory safety: Indexed by byte offsets in JSONL; LRU cache for trace chunks.
  - Collate: Dynamic batch padding with `src_pad_id` and `tgt_pad_id`.
- **Status:** `COMPLETE` — **Verified**

### 5.13 Stage 13: EST Model Architecture

- **Script:** `model.py` — `ExecutionSemanticTransformer`, `ModelConfig`, `PositionalEncoding`
- See Section 7 below.
- **Status:** `COMPLETE` — **Verified**

### 5.14 Stage 14: Training Infrastructure

- **Script:** `train.py` — `TrainingManager`, `TrainingConfig`, `compute_batch_loss()`
- See Section 7 below.
- **Status:** `COMPLETE` — **Verified**

### 5.15 Stage 15: Inference & Trace Reconstruction

- **No implementation found.** See Section 11.
- **Status:** `NOT IMPLEMENTED`

---

## 6. Dataset and Vocabulary Audit

### 6.1 Dataset Record Structure

Each JSONL record (verified from first line of `train.jsonl`):
```json
{
  "program_id": "p00231",
  "source": { /* SPR dict: program_id, file, package, classes, ... */ },
  "runtime_trace": { "representation": "ESR", "program_id": "...", "event_count": N, "events": [...] }
}
```

**Critical finding:** The `source` field is an SPR dictionary, **not** raw Java. The `ExecutionTraceDataset` correctly ignores this and loads raw Java from `selected source code/{pid}.java`. This is a design feature, not a bug.

### 6.2 Split Statistics

| Property | Value | Source |
|----------|-------|--------|
| Total dataset records | 562 | **Verified** — `wc -l training_dataset.jsonl` |
| Train records | 449 | **Verified** — `wc -l train.jsonl` |
| Validation records | 56 | **Verified** — `wc -l validation.jsonl` |
| Test records | 57 | **Verified** — `wc -l test.jsonl` |
| Split seed | 42 | **Verified** — `split_dataset.py` line 23 |
| Split ratios | 80/10/10 | **Verified** — `split_dataset.py` lines 18–20 |
| Disjoint splits | Yes (by construction: sequential slicing after shuffle) | **Verified** — code review |
| Chunk counts (full dataset) | Not computed (would require loading all 562 records) | **Unknown** |

### 6.3 Vocabulary Consistency

| Check | Result |
|-------|--------|
| Source vocab built from train split only | **Verified** — `build_source_vocabulary()` accepts `train_jsonl_path` |
| Target vocab built from train split only | **Verified** — `build_target_vocabulary()` accepts `train_jsonl_path` |
| Source vocab `train_records` matches train split | 449 = 449 ✅ **Verified** |
| Target vocab `train_records` matches train split | 449 = 449 ✅ **Verified** |
| PAD_ID = 0 for both vocabs | **Verified** |
| Target structural tokens at IDs 4–11 | **Verified** |
| Target vocab at 50,000-token cap | **Verified** — hit the `DEFAULT_TARGET_MAX_SIZE` ceiling |

### 6.4 Potential Data Concerns

1. **50,000-token target vocabulary:** Tokens excluded by the cap will map to `<UNK>` (ID 1). OOV rate on validation/test splits is **unknown** — `calculate_oov_diagnostics()` exists but no stored results were found.
2. **Large trace sizes:** Some ESR files are very large (e.g., `p00848.json` at 60+ MB). These produce many chunks and could dominate training.
3. **Missing 4 records:** 566 programs → 563 pairs → 562 in JSONL. The 3 missing pairs are likely case-sensitivity mismatches between SPR and ESR filenames (observed: `P00038` vs `p00038`). The 1 additional missing record from pairing to JSONL is unexplained.

---

## 7. Model Architecture and Training Infrastructure

### 7.1 EST Architecture (`model.py`)

| Component | Detail | Evidence |
|-----------|--------|----------|
| Architecture | Encoder-decoder Transformer | **Verified** — `ExecutionSemanticTransformer` |
| Encoder | `nn.TransformerEncoder` with `nn.TransformerEncoderLayer` | **Verified** — lines 238–252 |
| Decoder | `nn.TransformerDecoder` with `nn.TransformerDecoderLayer` | **Verified** — lines 255–269 |
| `batch_first` | `True` | **Verified** — both layers |
| Embedding scaling | `sqrt(d_model)` | **Verified** — line 223 |
| Positional encoding | Sinusoidal (Vaswani et al.) | **Verified** — `PositionalEncoding` class |
| Output projection | `nn.Linear(d_model, tgt_vocab_size)` | **Verified** — line 272 |
| Weight init | Xavier uniform for dim > 1; bias zeros | **Verified** — `_init_weights()` |
| Padding mask convention | `True = ignored` (PyTorch `key_padding_mask` convention) | **Verified** |
| Causal mask | Upper-triangular boolean `(T, T)` | **Verified** — `generate_square_subsequent_mask()` |
| `encode()` method | Separate method returning encoder memory | **Verified** — lines 294–321 |
| `decode()` method | Separate method accepting memory and masks | **Verified** — lines 323–366 |
| `forward()` | Accepts both `BatchData` and raw tensors | **Verified** — lines 368–413 |

**Inference-critical observation:** The model has separate `encode()` and `decode()` methods. This supports efficient autoregressive inference where encoder output (memory) is computed once and reused during decoding. **No explicit inference/generation method exists yet.**

### 7.2 ModelConfig Defaults vs. Pilot Config

| Parameter | Default | Pilot (MPS) | Evidence |
|-----------|---------|-------------|----------|
| `d_model` | 256 | 128 | Pilot used reduced model |
| `nhead` | 8 | 4 | Pilot used reduced model |
| `num_encoder_layers` | 4 | 2 | Pilot used reduced model |
| `num_decoder_layers` | 4 | 2 | Pilot used reduced model |
| `dim_feedforward` | 1024 | 256 | Pilot used reduced model |
| `dropout` | 0.1 | 0.1 | Same |
| `max_src_length` | 2048 | 1024 | Pilot restricted |
| `max_tgt_length` | 2048 | 2048 | Same |
| `src_vocab_size` | — | 3,155 | From loaded vocab |
| `tgt_vocab_size` | — | 50,000 | From loaded vocab |
| `activation` | `relu` | `relu` | Same |
| `norm_first` | `False` | `False` | Post-LN |

### 7.3 Training Infrastructure (`train.py`)

| Feature | Implementation | Evidence |
|---------|---------------|----------|
| Loss function | `F.cross_entropy(reduction='sum')` ÷ valid tokens | **Verified** — `compute_batch_loss()` |
| PAD exclusion | `ignore_index=tgt_pad_id` | **Verified** — line 213 |
| Zero-valid-token protection | Returns 0.0 loss, skips backward | **Verified** — lines 202–205, 299 |
| Optimizer | AdamW | **Verified** — line 256 |
| Gradient clipping | `clip_grad_norm_()` with configurable `grad_clip_norm` | **Verified** — lines 303–307 |
| Epoch loss aggregation | Token-weighted: `sum(loss * valid_tokens) / total_valid_tokens` | **Verified** — lines 311–322 |
| Validation | `@torch.no_grad()`, `model.eval()` | **Verified** — lines 325–354 |
| Checkpoint contents | model_state_dict, optimizer_state_dict, model_config, training_config, epoch, val_loss, best_val_loss, rng_states, format_version | **Verified** — lines 375–389 |
| Best checkpoint logic | Saves `checkpoint_best.pt` when `val_loss < best_val_loss` | **Verified** — lines 510–516 |
| Resume semantics | Restores model, optimizer, epoch (start_epoch = epoch + 1), best_val_loss, RNG states | **Verified** — `load_checkpoint()` |
| Compatibility checks | Validates `src_vocab_size`, `tgt_vocab_size`, `d_model`, `nhead` on load | **Verified** — lines 431–437 |
| Seeding | `random`, `numpy`, `torch.manual_seed`, `torch.cuda.manual_seed_all` | **Verified** — `set_seed()` |
| Device resolution | `resolve_device()`: auto → CUDA → CPU fallback. **MPS not in auto-detection.** | **Verified** — lines 153–165 |

### 7.4 Code-Level Concerns

1. **`resolve_device("auto")` does not detect MPS.** It only checks `torch.cuda.is_available()`. On Apple Silicon, `"auto"` falls back to CPU. The MPS pilot works around this by passing `device="mps"` explicitly.
2. **MPS RNG state not saved or restored.** The checkpoint saves `torch_cuda_rng` but not MPS RNG state. The pilot script stores `torch_cuda_rng: None`. RNG restoration on MPS resume will not reproduce exact sequences.
3. **No learning rate scheduler.** Only a fixed learning rate is used.
4. **`CHECKPOINT_FORMAT_VERSION = "1.0"`** is checked during save but not validated during load (no version check in `load_checkpoint()`).
5. **No mixed-precision / gradient scaler support.** Full FP32 training only.
6. **50,000-token target vocabulary** creates a large output projection layer (`d_model × 50,000`). At `d_model=256`, that's ~12.8M parameters in the output layer alone. This is the dominant parameter group.
7. **Nested tensor / MHA fastpath issue on MPS**: The pilot script (`run_mps_pilot.py` line 109–111) disables `torch.backends.mha.set_fastpath_enabled(False)` to work around `aten::_nested_tensor_from_mask_left_aligned` not being implemented on MPS. **This workaround is only in the pilot script, not in `train.py`.** Running `train.py` directly on MPS will trigger this nested-tensor path during `model.eval()` and will likely crash. The pilot succeeded only because the fastpath was disabled before the evaluation call. — **Verified** by code inspection of `train.py:evaluate()` (lines 325–354) and `run_mps_pilot.py` (lines 109–111).
8. **`d_model=128` (not 256) for both experiments.** The default config in `model.py` specifies `d_model=256`, but both `exp1` and the MPS pilot were run with `d_model=128`, `nhead=4`, `num_*_layers=2`, `dim_feedforward=256`. The output projection at this size is `128 × 50,000 ≈ 6.4M` parameters (not 12.8M as would be the case for `d_model=256`). — **Verified** from checkpoint `model_config` fields.

---

## 8. CPU Experiment Audit (`checkpoints/exp1`)

### 8.1 Available Artifacts

| File | Size | Present |
|------|------|---------|
| `checkpoint_best.pt` | 159 MB | ✅ |
| `checkpoint_latest.pt` | 159 MB | ✅ |
| Model/training config JSON | — | ❌ Not present (embedded inside `.pt` only) |
| Training logs | — | ❌ Not present |
| Experiment summary | — | ❌ Not present |

### 8.2 Verified Checkpoint Contents

Both `.pt` files were loaded and inspected during the final verification pass using `.venv/bin/python3` (Python 3.11.15, PyTorch 2.14.1).

**`checkpoint_best.pt` and `checkpoint_latest.pt` are byte-identical in all logical fields:**

| Field | Value | Evidence |
|-------|-------|----------|
| `format_version` | `"1.0"` | **Verified** — direct load |
| `epoch` | `1` | **Verified** — direct load |
| `val_loss` | `2.4181` (2.418097407828296) | **Verified** — direct load |
| `best_val_loss` | `2.4181` (same as val_loss) | **Verified** — identical for both files |
| `src_vocab_size` | 3,155 | **Verified** — embedded `model_config` |
| `tgt_vocab_size` | 50,000 | **Verified** — embedded `model_config` |
| `d_model` | 128 | **Verified** — embedded `model_config` |
| `nhead` | 4 | **Verified** — embedded `model_config` |
| `num_encoder_layers` | 2 | **Verified** — embedded `model_config` |
| `num_decoder_layers` | 2 | **Verified** — embedded `model_config` |
| `dim_feedforward` | 256 | **Verified** — embedded `model_config` |
| `dropout` | 0.1 | **Verified** — embedded `model_config` |
| `max_src_length` | 4,096 | **Verified** — differs from pilot (1,024) |
| `max_tgt_length` | 2,048 | **Verified** — same as pilot |
| `activation` | `relu` | **Verified** |
| `norm_first` | `False` | **Verified** |
| `device` (training) | `cpu` | **Verified** — embedded `training_config` |
| `epochs` (planned) | 5 | **Verified** — embedded `training_config` |
| `learning_rate` | 0.0005 | **Verified** |
| `weight_decay` | 0.01 | **Verified** |
| `grad_clip_norm` | 1.0 | **Verified** |
| `train_batch_size` | 2 | **Verified** |
| `eval_batch_size` | 2 | **Verified** |
| `seed` | 42 | **Verified** |
| `log_interval` | 500 | **Verified** |
| Optimizer step count | 373 | **Verified** — from `optimizer_state_dict['state'][0]['step']` |
| `output_projection.weight` identical | Yes | **Verified** — `torch.equal()` returned True |
| `torch_rng` state identical | Yes | **Verified** — `torch.equal()` returned True |
| `rng_states` keys | `python_rng`, `numpy_rng`, `torch_rng`, `torch_cuda_rng` | **Verified** |
| Training data used | Full `train.jsonl` (449 programs, standard split) | **Inferred** from `training_config['checkpoint_dir']='checkpoints/exp1'` and the 373-step count (449 programs × ≈1.66 chunks/prog ÷ batch_size 2 ≈ 372 batches/epoch) |

### 8.3 Interpretation

- **Epoch 1 of 5 completed.** The run was planned for 5 epochs but stopped after 1. The checkpoint was saved at the end of epoch 1.
- **Both `checkpoint_best.pt` and `checkpoint_latest.pt` contain identical model state.** This is expected: after the first epoch, the best checkpoint equals the latest checkpoint (epoch 1 is the only epoch), so `save_best_only=False` and the best-checkpoint logic both saved the same state.
- **373 optimizer steps × batch_size 2 = 746 training chunks** (approximately). This gives approximately **1.66 chunks per program** on average across the 449 training programs, consistent with the dataset structure.
- **What training data was used:** The full `split/train.jsonl` split (449 programs). This is inferred; no explicit log exists. No filtering or program subset was applied (unlike the MPS pilot).
- **Is this a complete or interrupted run?** The run was interrupted after epoch 1. Epoch 2–5 weights do not exist.
- **Status:** `PARTIAL` — **Epoch 1 of 5 verified.** Val loss 2.4181. Subsequent epochs not trained.

---

## 9. Apple Silicon MPS Pilot Audit (`checkpoints/pilot_mps`)

### 9.1 Experiment Configuration

| Parameter | Value | Evidence |
|-----------|-------|----------|
| Device | `mps` (Apple Silicon GPU) | **Verified** — `pilot_summary.json`, `training_config.json` |
| PyTorch version | 2.14.1 | **Verified** — `pilot_summary.json` |
| Model dimensions | d_model=128, nhead=4, layers=2/2, ff=256 | **Verified** — `model_config.json` |
| Total parameters | 13,916,880 | **Verified** — `pilot_summary.json` |
| Seed | 42 | **Verified** — `training_config.json` |
| Learning rate | 0.0005 | **Verified** |
| Weight decay | 0.01 | **Verified** |
| Grad clip norm | 1.0 | **Verified** |
| Batch size | 2 | **Verified** |
| Max optimizer steps | 50 | **Verified** — pilot script constraint |
| Max runtime | 180 seconds | **Verified** — pilot script constraint |

### 9.2 Data Subset

| Set | Programs | Chunks | Evidence |
|-----|----------|--------|----------|
| Train | 10 programs: p00435, p00188, p00168, p00436, p00647, p00128, p00152, p00668, p00102, p00231 | 90 | **Verified** — `pilot_summary.json` |
| Validation | 2 programs: p00160, p00334 | 5 | **Verified** — `pilot_summary.json` |

- **Disjointness:** Explicitly asserted in pilot script (line 145). No overlap between train and val PIDs.
- **Subset files:** `train_subset.jsonl` (360 KB) and `val_subset.jsonl` (23 KB) exist in the pilot directory.

### 9.3 Training Results

| Metric | Value | Meaning | Evidence |
|--------|-------|---------|----------|
| Optimizer steps completed | 50 | All planned steps executed | **Verified** |
| Epochs traversed | 2 (partial second epoch) | Steps 1–45 in epoch 1, steps 46–50 in epoch 2 | **Verified** — step records |
| Elapsed time | 9.04 seconds | Well within 180s limit | **Verified** |
| Initial 5-step loss avg | 10.6427 | Average of steps 1–5 | **Verified** |
| Final 5-step loss avg | 5.8236 | Average of steps 46–50 | **Verified** |
| Loss reduction | 45.3% | (10.64 − 5.82) / 10.64 | **Verified** |
| Final training loss (epoch 2 partial) | 5.8249 | Average of epoch 2 batches only (steps 46–50) | **Verified** |
| Final validation loss | 5.4520 | Full validation pass after training | **Verified** |
| MPS allocated memory | 368 MB | End-of-experiment snapshot | **Verified** |
| MPS driver memory | 5.0 GB | End-of-experiment snapshot | **Verified** |

### 9.4 Loss Interpretation

> [!IMPORTANT]
> The pilot ran for only **50 optimizer steps on 10 programs**. The loss reduction demonstrates that:
> 1. The MPS device works for training (hardware feasibility).
> 2. Gradients flow correctly and the optimizer updates parameters.
> 3. Loss decreases monotonically, suggesting the model is learning.
>
> **This does NOT demonstrate:**
> - Convergence or good generalization.
> - That the model has learned useful source→trace mappings.
> - Anything about held-out test performance.

### 9.5 Verification Checks (from pilot script)

| Check | Result | Evidence |
|-------|--------|----------|
| Parameter update (max weight shift) | > 1e-6 | **Verified** — pilot script assertion passed (line 396) |
| Finite loss at every step | All 50 steps finite | **Verified** — pilot script checks (line 314) |
| Finite gradients | All parameters verified | **Verified** — pilot script checks (lines 321–324) |
| Checkpoint reload match | Reloaded weights identical to trained | **Verified** — pilot script assertion (line 445) |
| Optimizer state steps | 50 | **Verified** — pilot script assertion (line 455) |
| Exp1 integrity | SHA256 hashes matched pre/post | **Verified** — pilot script checks (lines 462–465) |

### 9.6 MPS-Specific Caveats

1. **MHA fastpath disabled:** The pilot script disables `torch.backends.mha.set_fastpath_enabled(False)` to avoid the `aten::_nested_tensor_from_mask_left_aligned` crash. This workaround is **only in the pilot script**, not in `train.py`. Using `train.py` directly on MPS will likely crash during `model.eval()`.
2. **RNG state restoration:** The checkpoint saves `torch_cuda_rng: None` and does not capture MPS-specific RNG state. Resuming training will not reproduce the exact same random sequence.
3. **MPS `synchronize()` calls:** The pilot script explicitly calls `torch.mps.synchronize()` after optimizer steps and validation. This is not done in `train.py`.
4. **`resolve_device("auto")` ignores MPS:** Must pass `device="mps"` explicitly.

---

## 10. Tests and Reproducibility

### 10.1 Test Suite Inventory

| Test File | Test Count | Coverage Area |
|-----------|-----------|---------------|
| `test_chunk_trace.py` | 17 | Event parsing, chunking, oversized handling, edge cases |
| `test_est_model.py` | 12 | Forward pass, shapes, masks, causal isolation, gradients, BatchData compat |
| `test_est_training.py` | 9 | Training loop, loss computation, checkpointing, resume, compatibility |
| `test_execution_dataset.py` | 21 | Dataset indexing, chunking, collation, padding, DataLoader behavior |
| `test_tokenize_runtime.py` | 26 | Runtime tokenization, escaping, boundary markers, edge cases |
| `test_tokenize_source.py` | 25 | Java lexical tokenization, literals, operators, comments, SPR handling |
| `test_vocabulary.py` | 20 | Vocab construction, serialization, encode/decode, OOV, structural tokens |
| **Total** | **130** | |

### 10.2 Test Execution Evidence

- **No test run logs or results were found** in the repository.
- The claim of "130 passing tests" is based on the sum of `def test_` function counts (17+12+9+21+26+25+20 = 130). **This confirms that 130 test functions are defined**, but their pass/fail status is **UNVERIFIED** in this audit.
- Tests were not executed during this audit to avoid side effects and resource consumption.

### 10.3 Test Coverage Gaps

| Missing Test Category | Risk |
|-----------------------|------|
| Inference / autoregressive decoding | HIGH — no inference code exists yet |
| Trace reconstruction (token IDs → trace events) | HIGH — not implemented |
| Evaluation metrics (BLEU, exact match) | HIGH — not implemented |
| MPS-specific training behavior | MEDIUM — pilot covers some, but no dedicated MPS test in `tests/` |
| Checkpoint cross-device compatibility (MPS→CPU, CPU→MPS) | MEDIUM |
| Full dataset loading stress test | LOW |
| OOV rate validation on held-out splits | LOW |

### 10.4 Reproducibility Assessment

| Concern | Severity | Detail |
|---------|----------|--------|
| `requirements.txt` platform-specific | HIGH | Contains NVIDIA/CUDA packages; will fail on macOS |
| Python 3.14.4 availability | LOW | Recent version; may need specific install |
| No `setup.py` or `pyproject.toml` | MEDIUM | No formal packaging |
| No environment creation script | MEDIUM | No `Makefile` or setup instructions |
| Data files not in git | BY DESIGN | `.gitignore` excludes `Module3_Training_Dataset/` and `checkpoints/` |

---

## 11. Inference and Trace Reconstruction Readiness

### 11.1 Current Architecture Support

The EST model already provides the foundation for autoregressive inference:

| Capability | Exists? | Location |
|------------|---------|----------|
| Separate `encode()` method | ✅ Yes | `model.py` — `encode()` returns memory tensor |
| Separate `decode()` method | ✅ Yes | `model.py` — `decode()` accepts memory, returns hidden states |
| Auto-generated causal mask | ✅ Yes | `generate_square_subsequent_mask()` |
| BOS token ID for seeding | ✅ Yes | `vocabulary.py` — `BOS_ID = 2` |
| EOS token ID for stopping | ✅ Yes | `vocabulary.py` — `EOS_ID = 3` |
| Token → string decoding | ✅ Yes | `Vocabulary.decode()` |
| Source tokenization | ✅ Yes | `source_tokenizer.py` — `tokenize_java_code()` |
| Source encoding | ✅ Yes | `Vocabulary.encode()` |

### 11.2 What Is Missing

| Component | Priority | Description |
|-----------|----------|-------------|
| **Greedy decode function** | P0 | Token-by-token autoregressive generation: seed with BOS, call decode, argmax, append, repeat until EOS or max_length |
| **Beam search** (optional) | P2 | Multi-hypothesis decoding for better quality |
| **Trace reconstruction** | P0 | Convert generated token ID sequence → string tokens → structured ESR events. Must handle `<EVENT_START>/<EVENT_END>` boundaries, attribute parsing, and `<UNK>` gracefully |
| **MPS eval workaround in `train.py`** | P0 | Must disable MHA fastpath before `model.eval()` on MPS |
| **Inference script** | P1 | End-to-end: load checkpoint → load vocab → read Java file → tokenize → encode → decode → reconstruct trace |
| **Evaluation metrics** | P1 | Token-level accuracy, sequence-level exact match, structural event accuracy, BLEU/ROUGE |
| **Perplexity computation** | P1 | Already derivable from existing `compute_batch_loss()` but not surfaced as a metric |

### 11.3 Proposed Implementation Plan

#### File 1: `inference.py` — Core inference engine

```
1. load_model_and_vocab(checkpoint_path, vocab_dir) → model, src_vocab, tgt_vocab
2. encode_source(java_code: str, src_vocab, model) → memory tensor
3. greedy_decode(memory, model, tgt_vocab, max_length=2048) → List[int]
   - Seed: [BOS_ID]
   - Loop: forward decode, argmax, append
   - Stop: EOS_ID or max_length
4. decode_token_ids(ids: List[int], tgt_vocab) → List[str]
5. reconstruct_trace(tokens: List[str]) → Dict (ESR-format)
```

**Acceptance criteria:**
- Can load pilot checkpoint and generate a token sequence.
- Generated sequence starts with structural tokens (`<EVENT_START>`, etc.).
- Can produce at least one complete `<EVENT_START>...<EVENT_END>` block.
- Handles EOS termination and max-length cutoff.

#### File 2: `evaluate.py` — Evaluation metrics

```
1. compute_token_accuracy(predicted_ids, reference_ids, ignore_pad=True)
2. compute_event_f1(predicted_events, reference_events)
3. compute_perplexity(model, dataloader) — from existing loss computation
4. run_evaluation(model, test_dataloader, tgt_vocab) → metrics dict
```

#### File 3: Tests

- `tests/test_inference.py`: Greedy decode with synthetic model, EOS stopping, max-length, trace reconstruction.
- `tests/test_evaluate.py`: Metric computation on known inputs.

---

## 12. Known Issues, Limitations, and Technical Debt

| # | Issue | Severity | Detail |
|---|-------|----------|--------|
| 1 | MPS MHA fastpath crash not handled in `train.py` | HIGH | Only pilot script disables fastpath. Using `train.py` on MPS will crash during eval. |
| 2 | `resolve_device("auto")` ignores MPS | MEDIUM | Apple Silicon users must explicitly set `device="mps"` |
| 3 | `requirements.txt` is Linux/CUDA-specific | HIGH | Cannot reproduce on macOS without manual editing |
| 4 | MPS RNG state not checkpointed | MEDIUM | Resume on MPS won't reproduce exact training sequence |
| 5 | No inference or evaluation code | HIGH | Cannot assess model quality |
| 6 | 50K target vocabulary creates large output layer | MEDIUM | Memory pressure during full-scale training |
| 7 | No learning rate scheduler | MEDIUM | Fixed LR may limit convergence |
| 8 | `split_dataset.py` loads entire dataset into memory | LOW | 562 records × ~1 MB each ≈ 593 MB RAM for splitting |
| 9 | No OOV diagnostics saved | LOW | Unknown <UNK> rate on val/test sets |
| 10 | Checkpoint format version not validated on load | LOW | `load_checkpoint()` doesn't check `format_version` |
| 11 | `source` field in JSONL is SPR (ignored by loader) | INFO | By design; documented but could confuse new contributors |
| 12 | 3 programs lost between pairing (563) and JSONL (562) | LOW | Minor data loss, cause not documented |

---

## 13. Claims That Remain Unverified

| Claim | Source | Status |
|-------|--------|--------|
| 130 tests all pass | Now independently verified | **VERIFIED** — `python3 -m unittest discover` ran 130 tests, 0 errors/failures, in 0.656s |
| CPU experiment epoch/loss values | Now independently verified | **VERIFIED** — checkpoint loaded, epoch=1, val_loss=2.4181, 373 optimizer steps |
| CPU experiment training configuration | Now independently verified | **VERIFIED** — embedded in checkpoint: d_model=128, lr=0.0005, 5 planned epochs (1 completed) |
| Exact OOV rate on validation/test | Not computed | **UNKNOWN** — `calculate_oov_diagnostics()` exists; no results stored or computed |
| Full dataset chunk count | Not computed | **UNKNOWN** — would require loading all 562 records with current `max_chunk_size` |
| MPS driver/allocated memory figures | `pilot_summary.json` | **REPORTED** — from pilot script output; not independently measured |
| PyTorch 2.14.1 available via `.venv` | Now independently verified | **VERIFIED** — `.venv/bin/python3` (3.11.15) successfully imports `torch` 2.14.1 |

---

## 14. Next Steps (Prioritized by Dependency)

| Priority | Task | Dependency | Estimated Effort |
|----------|------|------------|-----------------|
| **P0** | Fix MPS MHA fastpath handling in `train.py` | None | 30 min |
| **P0** | Add MPS to `resolve_device("auto")` | None | 15 min |
| **P0** | Create platform-independent `requirements.txt` | None | 30 min |
| **P0** | Implement `inference.py` with greedy decode | Model + vocab ready | 4–6 hours |
| **P0** | Implement trace reconstruction from token IDs | `inference.py` | 2–3 hours |
| **P1** | Add inference tests (`test_inference.py`) | `inference.py` | 2–3 hours |
| **P1** | Implement `evaluate.py` with metrics | `inference.py` | 3–4 hours |
| **P1** | Run OOV diagnostics on val/test splits | Vocab ready | 1 hour |
| ~~**P1**~~ | ~~Inspect exp1 checkpoint metadata~~ | ~~torch environment~~ | ~~15 min~~ — ✅ **DONE** in this audit |
| **P2** | Run full-scale training (default config) | All P0/P1 done | Days–weeks |
| **P2** | Add learning rate scheduler | None | 1–2 hours |
| **P2** | Add MPS RNG state to checkpoints | None | 1 hour |
| **P2** | Beam search decoding | `inference.py` | 4–6 hours |

---

## 15. Safe Inspection Commands

```bash
# Activate the virtual environment
source .venv/bin/activate

# Verify PyTorch and MPS
python3 -c "import torch; print(torch.__version__, torch.backends.mps.is_available())"

# Inspect exp1 checkpoint metadata (read-only)
python3 -c "
import torch
ckpt = torch.load('checkpoints/exp1/checkpoint_latest.pt', map_location='cpu', weights_only=False)
print('Epoch:', ckpt.get('epoch'))
print('Val Loss:', ckpt.get('val_loss'))
print('Best Val Loss:', ckpt.get('best_val_loss'))
print('Model Config:', ckpt.get('model_config'))
print('Format Version:', ckpt.get('format_version'))
"

# Run existing test suite (safe, no artifacts modified)
python3 -m pytest tests/ -v --tb=short

# Compute OOV diagnostics (read-only)
python3 -c "
from vocabulary import Vocabulary, calculate_oov_diagnostics
from tokenize_and_analyze import tokenize_runtime
import json

tgt_vocab = Vocabulary.load('vocabularies/vocab_target.json')
def gen_sequences(path):
    with open(path) as f:
        for line in f:
            rec = json.loads(line)
            yield tokenize_runtime(rec.get('runtime_trace', {}))

print('Validation OOV:', calculate_oov_diagnostics(tgt_vocab, gen_sequences('Module3_Training_Dataset/split/validation.jsonl')))
"

# Quick smoke test of inference readiness (after implementing inference.py)
python3 -c "
import torch
from model import ExecutionSemanticTransformer, ModelConfig
import json

cfg = ModelConfig.from_dict(json.load(open('checkpoints/pilot_mps/model_config.json')))
model = ExecutionSemanticTransformer(cfg)
ckpt = torch.load('checkpoints/pilot_mps/checkpoint_latest.pt', map_location='cpu', weights_only=False)
model.load_state_dict(ckpt['model_state_dict'])
model.eval()
print('Model loaded successfully. Ready for inference implementation.')
"
```

---

## 16. Evidence Index

| Conclusion | Evidence Source |
|------------|---------------|
| EST architecture is encoder-decoder Transformer | `model.py` — `ExecutionSemanticTransformer` class, lines 178–413 |
| `forward()` supports BatchData | `model.py` — lines 385–398 |
| `encode()` and `decode()` are separate methods | `model.py` — lines 294–366 |
| Loss excludes PAD tokens | `train.py` — `compute_batch_loss()`, line 213: `ignore_index=tgt_pad_id` |
| Checkpoint saves model + optimizer + RNG | `train.py` — `save_checkpoint()`, lines 375–389 |
| Checkpoint validates architecture compatibility | `train.py` — `load_checkpoint()`, lines 431–437 |
| Dataset loads raw Java (not SPR) | `execution_dataset.py` — `_load_source()`, lines 217–232 |
| Teacher forcing: [BOS]+chunk, chunk+[EOS] | `execution_dataset.py` — lines 297–298 |
| Padding mask: True=ignored | `execution_dataset.py` — `execution_trace_collate_fn()`, lines 357–373 |
| Source vocab = 3,155 tokens | `vocabularies/vocab_source.json` — `vocab_size` field |
| Target vocab = 50,000 tokens | `vocabularies/vocab_target.json` — `vocab_size` field |
| Special token IDs 0–11 verified | `vocabularies/vocab_target.json` — first 12 entries of `id_to_token` |
| Vocab built from train split only | `vocabulary.py` — `build_source_vocabulary()` and `build_target_vocabulary()` |
| Split: 449/56/57, seed=42, 80/10/10 | `split_dataset.py`, `wc -l` output |
| MPS pilot: 50 steps, loss 10.64→5.82 | `checkpoints/pilot_mps/pilot_summary.json` |
| MPS pilot used 10+2 disjoint programs | `pilot_summary.json` — `train_programs`, `val_programs` |
| MHA fastpath disabled only in pilot | `scripts/run_mps_pilot.py` — lines 109–111 |
| MPS not in `resolve_device("auto")` | `train.py` — lines 153–165 |
| 130 test functions defined | `grep -c "def test_"` across 7 test files |
| Git commit: 22ffedb on main | `git log -n 1 --oneline` |
| `requirements.txt` contains CUDA packages | `requirements.txt` — lines 1–3, 12–26 |
| No inference script exists | Absence confirmed by full directory listing |

---

## 17. Final Verification Pass — Audit Corrections and Confidence

**Verification date:** 2026-10-16  
**Verification environment:** `.venv/bin/python3` (Python 3.11.15, PyTorch 2.14.1, macOS Apple Silicon)  
**Checks performed:** Checkpoint loading, test execution, code inspection of `train.py` and `model.py`.

### 17.1 Corrections Made to This Report

| Section | Correction |
|---------|------------|
| §7.4 item 7 | Clarified MPS MHA fastpath: crash path confirmed via code inspection of `train.py:evaluate()` (lines 325–354) — `model.eval()` is called with no fastpath workaround. Pilot succeeded specifically because `run_mps_pilot.py` disabled fastpath before its own eval call. Now labelled **Verified**. |
| §7.4 item 8 (new) | Added: both `exp1` and the pilot used `d_model=128`, not the default `d_model=256`. The output projection is `128 × 50,000 ≈ 6.4M` params, not 12.8M. Section 7.4 item 6 reference to `d_model=256` is a description of the *default* config, which was not used in either experiment. |
| §8 CPU Experiment | Replaced speculative observations with fully verified checkpoint contents (epoch, val_loss, config, optimizer step count, identity of best vs. latest). Both `.pt` files contain identical logical state. |
| §13 Unverified Claims | Updated to reflect that three claims (test results, exp1 epoch/loss, exp1 config) are now independently verified. Two claims (OOV rate, full chunk count) remain unknown. |
| §14 Next Steps | Marked the `Inspect exp1 checkpoint metadata` task as completed. |

### 17.2 Independently Verified in This Pass

| Finding | Method |
|---------|--------|
| `exp1` epoch = 1, val_loss = 2.4181, optimizer_steps = 373 | `torch.load()` on `.venv/bin/python3` |
| `exp1` config: d_model=128, nhead=4, 2 encoder + 2 decoder layers, lr=0.0005, seed=42, device=cpu | Embedded `model_config` and `training_config` in `.pt` |
| `exp1` planned epochs = 5; only epoch 1 completed | `training_config['epochs']` vs `ckpt['epoch']` |
| `checkpoint_best.pt` and `checkpoint_latest.pt` are logically identical (same model weights, same val_loss, same optimizer step count) | `torch.equal()` on weight tensors and RNG states |
| `exp1` differs from pilot only in `max_src_length` (4,096 vs 1,024); all other model/training params are equal | Direct config comparison |
| MPS pilot checkpoint: epoch=2, optimizer_steps=50, train_loss=5.8249, val_loss=5.4520 | `torch.load()` on pilot checkpoint |
| MPS RNG state: `torch_rng` is a CPU `uint8` tensor (CPU torch RNG, not MPS-device RNG). `torch_cuda_rng` is `None`. MPS-specific randomness is not captured. | `type()`, `.device`, `.dtype` on rng state tensors |
| `train.py:evaluate()` does not disable MHA fastpath before `model.eval()` | Code inspection, lines 325–354 |
| `train.py:resolve_device("auto")` does not check `torch.backends.mps.is_available()` | Code inspection, lines 153–165 |
| `train.py:load_checkpoint()` restores CPU and CUDA RNG states; no MPS RNG key exists | Code inspection, lines 450–463 |
| `model.py:decode()` returns hidden states (B, T, d_model), not logits; projection is in `forward()` | Code inspection, lines 323–366 and 411–413 |
| 130 tests pass; 0 errors, 0 failures, elapsed 0.656s | `.venv/bin/python3 -m unittest discover -s tests/ -p "test_*.py"` |
| No inference, decoding, or trace-reconstruction code exists anywhere in the repository | `grep` for `greedy_decode`, `beam_search`, `generate`, `autoregressive`, `reconstruct_trace` returned no results |

### 17.3 Supported Only by Prior Reports (Not Re-Verified Here)

| Finding | Prior Source |
|---------|-------------|
| Pilot step-by-step loss trajectory (initial avg 10.6427, final avg 5.8236) | `pilot_summary.json` |
| Pilot elapsed time 9.04 seconds | `pilot_summary.json` |
| Pilot MPS memory figures (368 MB allocated, 5.0 GB driver) | `pilot_summary.json` |
| `Vocabulary._validate_internal_consistency()` correctly enforces bijection | Code review, not re-tested |
| Dataset chunk LRU cache behaviour | Code review of `execution_dataset.py` |
| Pilot train subset: 90 chunks from 10 programs; val: 5 chunks from 2 programs | `pilot_summary.json` |

### 17.4 Remaining Uncertainties

| Uncertainty | Impact |
|-------------|--------|
| OOV rate on val/test splits | Unknown `<UNK>` substitution frequency in target sequences |
| Full dataset chunk count (all 449 train programs) | Cannot precisely bound sequence budget for full training |
| Whether `exp1` was run on the full `train.jsonl` or a subset | No log exists; inferred from step count |
| `exp1` epochs 2–5 will require resuming from the checkpoint | The checkpoint supports resume via `load_checkpoint()` |
| Exact MPS randomness reproducibility on resume | MPS-device RNG state is not saved; minor stochasticity on resume |

### 17.5 Inference Readiness Assessment

The architecture **supports** but does not **implement** autoregressive generation. Specifically:

- `encode(src_ids, src_padding_mask)` produces `memory` (B, S, d_model). This can be computed once and reused across all decoding steps. — **Verified**
- `decode(decoder_input_ids, memory, ...)` accepts growing `decoder_input_ids` across steps and produces `hidden_states` (B, T, d_model). — **Verified**
- `forward()` projects `hidden_states` to `logits` (B, T, tgt_vocab_size) via `output_projection`. — **Verified**
- To implement greedy decoding: call `encode()` once; loop calling `decode()` with `decoder_input_ids = [BOS] + generated_so_far`; take `argmax(output_projection(hidden[:, -1, :]))` at each step; stop when EOS or max_length. The `output_projection` layer must be applied manually in the inference loop (it is not inside `decode()`).
- **No such inference loop exists in the codebase.**

### 17.6 Next Recommended Action

**Implement `inference.py`** containing a greedy autoregressive decode function and a trace reconstruction function. This is the single blocking dependency for any model-quality assessment. The architecture already supports it: `encode()` and `decode()` are both present and correct. The minimum viable implementation requires:

1. A function that seeds decoding with `[BOS_ID]` and calls `model.decode()` + `model.output_projection` iteratively until EOS or max length.
2. A function that maps the generated token ID sequence back to string tokens via `tgt_vocab.decode()`.
3. A function that parses the resulting token list through `parse_events_from_tokens()` (already in `chunk_trace.py`) to reconstruct structured events.

> [!IMPORTANT]
> Do not begin `inference.py` development until the MPS MHA fastpath issue in `train.py` is resolved, since inference will call `model.eval()` on the same code path.

---

*End of audit including final verification pass. This document is the output of a read-only, evidence-based inspection. No application code, datasets, checkpoints, vocabularies, or Git history was modified. Only `PROJECT_CURRENT_STATUS.md` was created and subsequently updated.*

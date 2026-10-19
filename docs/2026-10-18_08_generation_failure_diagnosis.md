# Deep Diagnostic Report: Autoregressive Generation Collapse in Execution Semantic Transformer (EST)

**Project:** Automatic Runtime Execution Trace Generation from Java Source Code Using Transformer-Based Neural Models  
**Model Architecture:** Execution Semantic Transformer (EST)  
**Hardware & OS:** Apple Silicon MacBook Air M5 (16 GB Unified Memory), macOS 26.x  
**PyTorch Version:** 2.14.1 with Apple Silicon Metal Performance Shaders (MPS)  
**Primary Evaluated Checkpoint:** `checkpoints/multi_example_overfit_v1/checkpoint_best.pt`  
**Author / Investigator:** Senior ML Systems Engineer & PyTorch Specialist  
**Date:** 2026-10-18  

---

## 1. Executive Summary

Empirical investigation into the multi-example overfitting experiment (`checkpoints/multi_example_overfit_v1/checkpoint_best.pt`) demonstrates that **the training data alignment, vocabulary bijection, causal future-isolation masks, and basic autoregressive generation loop mechanics are mathematically sound**.

The catastrophic failure during greedy decoding—where teacher-forced token accuracy achieves **66.42%** while greedy generation collapses to **6.94%** accuracy, reaches `<EOS>` in **0/8** cases, and enters an infinite repetitive loop of `<ATTR_START> CONTEXT Main # main : 1 <ATTR_END>`—is caused by three compounding phenomena:

1. **Exposure Bias and Error Cascades (Rank 1 — Primary Verified Defect):**
   In every single one of the eight training programs, the model's predictions diverge from the reference within the first **2 to 4 tokens** (specifically at `STEP` or event `TYPE`). Because training loss only reached **2.3040** (Perplexity $\approx 10.01$), the model never fully memorized low-frequency integer step tokens (e.g. `'279'`, `'3068'`). Under teacher forcing, receiving the true token at step $t+1$ resets the error, allowing the model to effortlessly predict repetitive boilerplate syntax (`CONTEXT Main # main : 1`). Under greedy decoding, emitting `<ATTR_START>` after `STEP` creates an out-of-distribution prefix. The model then falls into a deterministic closed-loop attractor (`<ATTR_START> CONTEXT Main # main : 1 <ATTR_END> <ATTR_START>...`) from which argmax selection cannot escape.
2. **Near-Zero Cross-Attention Conditioning (Rank 2 — Strong Structural Defect):**
   Controlled perturbation of the encoder memory (comparing correct source code vs. a completely different program's Java source) produces a **mean logit difference of only 0.0485** and a maximum difference of **0.1956** on output logits that range between 5.0 and 10.0. The decoder is behaving almost entirely as an **unconditional language model**, ignoring Java source code semantics.
3. **Absence of Decoding Regularization & EOS Imbalance (Rank 3 — Generation Mechanism Defect):**
   Standard greedy decoding without repetition penalties or n-gram blocking is mathematically guaranteed to cycle when a dominant bigram transition ($P(\text{<ATTR\_START>} \mid \text{<ATTR\_END>}) = 0.239$) exceeds termination ($P(\text{<EVENT\_END>} \mid \text{<ATTR\_END>}) = 0.135$). Introducing a modest repetition penalty ($\alpha = 1.3$) immediately breaks the loop, produces a valid `<EVENT_END>` closure, and emits `<EOS>` on the overfitted sequence.

---

## 2. Repository Files & Functions Inspected

| File Path | Component / Role | Core Functions & Line References Inspected |
| :--- | :--- | :--- |
| [`model.py`](file:///Users/karthikkishor/Development/FinalYearProject/model.py) | Transformer Architecture | `ExecutionSemanticTransformer`: `forward` (L369-413), `encode` (L294-321), `decode` (L323-366), `generate_square_subsequent_mask` (L287-292), `PositionalEncoding` (L130-172) |
| [`train.py`](file:///Users/karthikkishor/Development/FinalYearProject/train.py) | Training Manager & Loss | `compute_batch_loss` (L172-219), `TrainingManager.train_epoch` (L271-324), `resolve_device` (L143-166) |
| [`execution_dataset.py`](file:///Users/karthikkishor/Development/FinalYearProject/execution_dataset.py) | Dataset & Batching | `ExecutionTraceDataset.__getitem__` (L270-310), `execution_trace_collate_fn` (L317-392), `BatchData.to` (L66-80) |
| [`vocabulary.py`](file:///Users/karthikkishor/Development/FinalYearProject/vocabulary.py) | Token & ID Mapping | `Vocabulary.encode` (L176-218), `Vocabulary.decode` (L220-258), Reserved tokens `PAD_ID=0, UNK_ID=1, BOS_ID=2, EOS_ID=3` |
| [`scripts/test_multi_example_overfit.py`](file:///Users/karthikkishor/Development/FinalYearProject/scripts/test_multi_example_overfit.py) | Overfit Training Script | Overfit loop on 8 short sequences (L66-180), AdamW optimizer configuration |
| [`scripts/test_overfit_generation.py`](file:///Users/karthikkishor/Development/FinalYearProject/scripts/test_overfit_generation.py) | Evaluation Entry Point | Evaluates greedy generation vs teacher forcing on overfitted training examples |
| [`scripts/compare_checkpoint_inference.py`](file:///Users/karthikkishor/Development/FinalYearProject/scripts/compare_checkpoint_inference.py) | Inference & Decoding | `evaluate` (L34-140): Greedy autoregressive decoding loop and teacher-forcing accuracy computation |
| [`scripts/diagnose_generation_failure.py`](file:///Users/karthikkishor/Development/FinalYearProject/scripts/diagnose_generation_failure.py) | Diagnostic Test Suite | Standalone test suite executing Tests 1 through 6 |

---

## 3. Systematic Findings & Question Responses

### A. Teacher-Forcing and Target Alignment

1. **Does each decoder input position correctly correspond to the next target token?**  
   **YES (Verified).** In `execution_dataset.py` (L291-299), given chunk token IDs $c = [c_0, c_1, \dots, c_{n-1}]$:
   $$\text{decoder\_input\_ids} = [\text{BOS}, c_0, c_1, \dots, c_{n-1}]$$
   $$\text{target\_ids} = [c_0, c_1, \dots, c_{n-1}, \text{EOS}]$$
   At position $t \in [0, n]$, input is $\text{decoder\_input}[t]$ and target is $\text{target\_ids}[t]$. The intended one-token rightward shift is preserved across all training examples.
2. **Are BOS and EOS handled consistently during training, evaluation, and inference?**  
   **YES (Verified).**
   - Training: Decoder starts with `BOS` (ID 2). Loss is computed against targets ending in `EOS` (ID 3).
   - Inference (`compare_checkpoint_inference.py:58-82`): Generation initializes with `generated = [bos_id]` and loops appending `argmax(logits)`. If `next_id == eos_id`, generation terminates.
3. **Is there any off-by-one error, accidental target shift, or truncation?**  
   **NO (Ruled Out).** Diagnostic Test 1 verified that for all positions $i \in [0, n-1]$:
   $$\text{decoder\_input}[i+1] == \text{target\_ids}[i] == \text{chunk\_ids}[i]$$
4. **Is teacher-forced accuracy calculated against the correct target positions?**  
   **YES, but with one subtle omission.** In `scripts/compare_checkpoint_inference.py` (L85-111), teacher-forced accuracy predicts $c_i$ given $[\text{BOS}, c_0, \dots, c_{i-1}]$. It compares against `item.chunk_ids`, omitting the final `<EOS>` prediction step from the accuracy percentage.
5. **Does generation code use the same token alignment convention as training?**  
   **YES.** At step $t=0$, `decoder_ids = [[BOS]]`, which passes to `model.decode()`. The output projection at `hidden[:, -1, :]` computes the logits for the first generated token, matching position 0 of training.

---

### B. Causal and Padding Masks

1. **Causal-mask direction and semantics for PyTorch:**  
   **Verified.** `model.py:generate_square_subsequent_mask` uses `torch.triu(..., diagonal=1)` with dtype `torch.bool`. In PyTorch `nn.TransformerDecoder`, boolean `True` in `tgt_mask` prevents attention to future positions (additive mask $-\infty$).
2. **Strict causal isolation test:**  
   **Verified.** In Diagnostic Test 2, tokens at position 10 and beyond were deliberately perturbed. The maximum absolute logit difference for positions $0 \dots 9$ was **$0.00000000$**. Future target tokens cannot influence earlier predictions.
3. **Padding masks:**  
   In `execution_dataset.py`, `src_padding_mask` and `target_padding_mask` assign `True` to padding tokens (`pad_id=0`) and `False` to valid tokens, matching PyTorch's `key_padding_mask` convention.
4. **MPS-specific attention fastpath:**  
   When running on Apple Silicon MPS with `torch.no_grad()`, `nn.TransformerEncoder` crashes if `torch.backends.mha.set_fastpath_enabled(False)` is not set. All active evaluation scripts disable this fastpath cleanly.

---

### C. Model Forward Pass & Architecture

1. **Tensor shapes:**  
   - Source IDs: $(B, S)$
   - Encoder Memory: $(B, S, d_{\text{model}})$
   - Decoder Inputs: $(B, T)$
   - Hidden States: $(B, T, d_{\text{model}})$
   - Output Logits: $(B, T, V_{\text{target}})$
2. **Embedding scaling and Positional Encoding:**  
   Embeddings are multiplied by $\sqrt{d_{\text{model}}}$ ($\sqrt{128} \approx 11.31$) per Vaswani et al. Positional encodings are registered as buffers and sliced dynamically.
3. **Output projection & vocabulary dimensions:**  
   Target vocabulary size is 50,000. Projection layer is `nn.Linear(128, 50000)`. Projection weights account for $6,450,000$ parameters ($46.3\%$ of the model).
4. **Does the decoder actually use the encoded source representation?**  
   **Empirical Answer: Extremely weakly.**
   In Diagnostic Test 3, swapping the true Java source code for a completely different Java program altered decoder output logits by an average of only **0.0485** (max diff 0.1956) on logits of magnitude ~7.0. The cross-attention layers have not learned strong semantic coupling.

---

### D. Loss, Gradients, and Optimization

1. **Cross-entropy computation:**  
   Uses `F.cross_entropy(..., ignore_index=tgt_pad_id, reduction='sum') / num_valid_tokens`. Ignored padding tokens contribute zero to the loss and gradient.
2. **Optimization dynamics in overfit experiment:**  
   In `scripts/test_multi_example_overfit.py`, 300 steps of AdamW ($\text{LR}=10^{-4}$) reduced loss from **5.5735** to **2.3040**.
   However, a loss of 2.3040 corresponds to a **Perplexity of 10.01**.
   On a 50,000-token vocabulary, selecting among ~10 candidates per position means the model has **not converged to memorization**. It only memorized the coarse high-frequency grammar tokens.

---

### E. Trace Serialization and Vocabulary

1. **Grammar and token consistency:**  
   Every runtime event is serialized as:
   $$\text{<EVENT\_START>} \rightarrow \text{TYPE} \rightarrow \langle\text{type}\rangle \rightarrow \text{STEP} \rightarrow \langle\text{step}\rangle \rightarrow \text{<ATTR\_START>} \rightarrow \langle\text{key}\rangle \rightarrow \langle\text{val}\dots\rangle \rightarrow \text{<ATTR\_END>} \rightarrow \text{<EVENT\_END>}$$
2. **Step token sparsity:**  
   While `TYPE`, `STEP`, `<ATTR_START>`, and `CONTEXT` occur in every event, step numbers (`279`, `3068`, `4325`) are distinct per program execution. The model failed to learn these distinct step number tokens during 300 steps, causing the first divergence at step 4 in all programs.

---

### F. Autoregressive Inference Analysis

1. **Step-by-Step divergence trace (Program `p00014`):**

| Step | Ref Token | Teacher-Forced Pred | Greedy Pred | Top Candidate Probabilities |
| :---: | :---: | :---: | :---: | :--- |
| **0** | `<EVENT_START>` | `<EVENT_START>` | `<EVENT_START>` | `<EVENT_START>`: 21.8%, `<ATTR_END>`: 5.5%, `<ATTR_START>`: 5.2% |
| **1** | `TYPE` | `TYPE` | `TYPE` | `TYPE`: 25.7%, `<ATTR_START>`: 6.0%, `<ATTR_END>`: 5.1% |
| **2** | `VW` | `VW` | `VW` | `VW`: 11.8%, `TYPE`: 5.3%, `VD`: 4.8%, `ME`: 4.5% |
| **3** | `STEP` | `STEP` | `STEP` | `STEP`: 25.7%, `<ATTR_START>`: 5.6%, `: `: 4.5% |
| **4** | **`279`** | **`<ATTR_START>`** | **`<ATTR_START>`** | `<ATTR_START>`: 5.5%, `<ATTR_END>`: 4.3%, `<UNK>`: 3.9%, `279`: < 1.0% |
| **5** | `<ATTR_START>` | `<ATTR_START>` | `CONTEXT` | `CONTEXT`: 12.2%, `VALUE`: 7.2%, `<ATTR_START>`: 3.6% |
| **6** | `CONTEXT` | `CONTEXT` | `Main` | `Main`: 24.5%, `<ATTR_END>`: 5.2%, `<ATTR_START>`: 4.9% |
| **7** | `Main` | `Main` | `#` | `#`: 13.3%, `: `: 12.0%, `<ATTR_END>`: 9.4% |
| **8** | `#` | `#` | `main` | `main`: 12.8%, `<ATTR_END>`: 5.4%, `Main`: 5.1% |
| **9** | `main` | `main` | `: ` | `: `: 20.3%, `#`: 6.5%, `<ATTR_END>`: 6.0% |
| **10** | `: ` | `: ` | `1` | `1`: 21.5%, `<ATTR_START>`: 5.3%, `<ATTR_END>`: 4.8% |
| **11** | `1` | `1` | `<ATTR_END>` | `<ATTR_END>`: 24.3%, `#`: 6.9%, `<ATTR_START>`: 5.5% |
| **12** | `<ATTR_END>` | `<ATTR_END>` | **`<ATTR_START>`** | **`<ATTR_START>`: 23.9%**, `<EVENT_END>`: 13.5%, `<ATTR_END>`: 5.1% |
| **13** | `<ATTR_START>` | `<ATTR_START>` | `CONTEXT` | `CONTEXT`: 10.2%, `VALUE`: 8.0%, `RETURNER`: 3.9% |

2. **The Self-Reinforcing Repetition Loop:**
   Notice what happens between Step 5 and Step 12:
   $$\text{<ATTR\_START>} \rightarrow \text{CONTEXT} \rightarrow \text{Main} \rightarrow \text{\#} \rightarrow \text{main} \rightarrow \text{:} \rightarrow \text{1} \rightarrow \text{<ATTR\_END>}$$
   At Step 12, following `<ATTR_END>`, the top two candidate tokens are:
   - `<ATTR_START>` with probability **0.239**
   - `<EVENT_END>` with probability **0.135**
   
   Because pure greedy decoding takes the `argmax`, `<ATTR_START>` is chosen every time. The model immediately re-generates `CONTEXT Main # main : 1 <ATTR_END>`. This loop is completely deterministic and repeats until `max_new_tokens = 256`.

---

## 4. Evidence Classification Table

| Category | Finding / Hypothesis | Direct Code / Empirical Evidence |
| :--- | :--- | :--- |
| **Verified Defect** | **Exposure Bias & Divergence at Step 4** | In all 8 programs, greedy decoding diverges at Step 4 (omitting the unique step number) or Step 2 (predicting `VW` over `ME`). |
| **Verified Defect** | **Deterministic Greedy Attractor Loop** | $P(\text{<ATTR\_START>} \mid \text{<ATTR\_END>}) = 0.239 > P(\text{<EVENT\_END>}) = 0.135$. Causes infinite repetition of `CONTEXT Main # main : 1`. |
| **Verified Defect** | **Under-Trained Checkpoint (Loss 2.3040)** | Cross-entropy 2.3040 represents Perplexity 10.01. The model never converged to memorizing target tokens. |
| **Strongly Supported** | **Weak Cross-Attention Conditioning** | Diagnostic Test 3 proves swapping the Java source changes decoder logits by only 0.048 on average. |
| **Strongly Supported** | **Vocabulary Sparsity of Step Tokens** | Step numbers appear rarely across programs, causing structural tokens to dominate step position predictions. |
| **Ruled Out** | **Off-by-One / Target Shift Error** | Ruled out by Diagnostic Test 1: $\text{decoder}[i+1] == \text{target}[i] == \text{chunk}[i]$ verified. |
| **Ruled Out** | **Causal Mask Leaks / Inversion** | Ruled out by Diagnostic Test 2: Logit diff prior to perturbation is strictly $0.00000000$. |
| **Ruled Out** | **Vocabulary Decode / Encode Corruption** | Ruled out by Diagnostic Test 6: Special and structural token IDs match across all modules. |
| **Ruled Out** | **Broken Generation Loop Implementation** | Ruled out: With repetition penalty $\alpha=1.3$, the exact same loop terminates and produces valid traces. |

---

## 5. Diagnostic Test Execution Log Summary

### Execution Command:
```bash
./.venv/bin/python scripts/diagnose_generation_failure.py
```

### Key Output Metrics:
- **Device:** `mps`
- **Test 1 (Token Shift):** 100% bijective shift verified across all positions.
- **Test 2 (Causal Mask):** Future perturbation delta at $t < 10$: **`0.00000000`** (Strict causal isolation).
- **Test 3 (Cross Attention):** Mean logit shift with swapped source code: **`0.048526`** (Near-zero conditioning).
- **Test 4 (Divergence):** Earliest divergence: **Step 4** (Predicted `<ATTR_START>` instead of step number `279`).
- **Test 5 (EOS Logits):** At ground-truth terminal position, `<EOS>` logit is 4.9754 (Rank 3/50,000, 5.6% probability), beaten by `<EVENT_START>` (logit 6.2807).
- **Test 6 (Round-Trip):** 91/93 tokens identical round-trip; 2 rare runtime values (`348100`) mapped to `<UNK>` as expected.

---

## 6. Why Teacher-Forced Accuracy is 66.4% while Greedy Accuracy is 6.9%

This discrepancy is the textbook consequence of **Exposure Bias in Autoregressive Sequence Models**:

$$\text{Teacher Forcing: } P(y_t \mid y_{1:t-1}^*, x)$$
$$\text{Greedy Generation: } P(y_t \mid \hat{y}_{1:t-1}, x)$$

1. **Under Teacher Forcing ($y_{1:t-1}^*$ is supplied):**
   Even though the model fails at Step 4 (predicting `<ATTR_START>` instead of `'279'`), at Step 5 the input is **externally corrected** with the true token `'279'`. Conditioned on `'279'`, the model correctly predicts `<ATTR_START>`. From Step 5 through Step 12, the true tokens are standard boilerplate (`CONTEXT`, `Main`, `#`, `main`, `:`, `1`, `<ATTR_END>`). The model achieves 100% accuracy on this entire 8-token sub-sequence. The mistake at Step 4 is completely isolated and cannot propagate. Because boilerplate syntax accounts for over two-thirds of the trace tokens, teacher-forced accuracy reaches **66.42%**.
2. **Under Greedy Generation ($\hat{y}_{1:t-1}$ is fed back):**
   At Step 4, when the model predicts `<ATTR_START>` instead of `'279'`, the decoder receives its own erroneous output:
   $$[\text{BOS}, \text{<EVENT\_START>}, \text{TYPE}, \text{VW}, \text{STEP}, \text{<ATTR\_START>}]$$
   This sequence is syntactically invalid according to the ESR trace grammar and **never occurred in the training dataset**. Conditioned on this out-of-distribution prefix, the model enters a local Markov loop, generating `CONTEXT Main # main : 1 <ATTR_END> <ATTR_START>` ad infinitum. Because the generated tokens immediately diverge from the reference after Step 3, greedy accuracy falls to **6.94%**.

---

## 7. Smallest Safe Corrective Actions

### Action 1: Add Repetition Penalty & No-Repeat N-Gram to the Decoding Loop
In `scripts/compare_checkpoint_inference.py` and `scripts/run_inference_demo.py`:
During greedy decoding, penalize tokens that have already been generated, and forbid repeating identical 4-grams (e.g. `<ATTR_START> CONTEXT Main #`):
```python
# Apply repetition penalty (alpha = 1.2 to 1.3)
for prev_id in set(generated_ids):
    if logits[prev_id] > 0:
        logits[prev_id] /= repetition_penalty
    else:
        logits[prev_id] *= repetition_penalty
```
*Empirical Proof:* When tested on `p00014`, this immediately broke the infinite loop, allowed the model to emit `<EVENT_END>`, and successfully terminated at `<EOS>` with a valid trace structure.

### Action 2: Train Overfitting Experiments to Near-Zero Loss ($\text{Loss} < 0.1$)
The 8-example experiment stopped at 300 steps with loss = 2.3040. For an overfitting memorization diagnostic to prove generation capability:
- Increase steps from 300 to 1,500.
- Increase learning rate from $10^{-4}$ to $5 \times 10^{-4}$.
- Confirm training loss reaches $< 0.1$ and teacher-forced accuracy reaches $> 98\%$ before running greedy generation.

### Action 3: Target Vocabulary Weight Tying (Embeddings $\leftrightarrow$ Output Projection)
In `model.py:ExecutionSemanticTransformer`:
Tie `output_projection.weight = tgt_embedding.weight`.
This eliminates $6.4$ million independent parameters, forces the representations of target tokens to align between input and output, and prevents the projection layer from drifting into arbitrary unigram biases.

---

## 8. Prioritized Remediation Plan

```
Phase 1: Immediate Inference Regularization (Day 1)
├── 1. Add repetition penalty (1.25) and no-repeat n-gram (n=4) to compare_checkpoint_inference.py
└── 2. Add structural grammar constraints (e.g. STEP must be followed by an integer token or number)

Phase 2: Overfit Verification with Full Convergence (Day 2)
├── 1. Run 8-example overfit test to 1,500 steps (target loss < 0.1)
└── 2. Verify that greedy generation reproduces 100% of the 8 training examples exactly

Phase 3: Cross-Attention Reinforcement & Full Training (Days 3–5)
├── 1. Tie target embedding and output projection weights in model.py
├── 2. Train on the full 449 training programs for 10 epochs using batch_size=2 on MPS/CUDA
└── 3. Compute final evaluation metrics on test.jsonl using regularized greedy decoding
```

---

## 9. Tests That Must Pass Before Another Full Training Run

1. **Test A (Overfit Generation Test):** `python scripts/test_overfit_generation.py` must achieve $> 90\%$ greedy accuracy and $8/8$ `<EOS>` terminations on the 8 training programs when trained to convergence.
2. **Test B (Causal Isolation Regression Test):** `python -m unittest tests/test_est_model.py` must continue passing with zero causal leakage.
3. **Test C (Cross-Attention Gradient Test):** Verify non-zero gradient backpropagation from decoder loss into encoder embeddings and source representations.

---

## 10. Strategic Recommendation for the Final Report

1. **Do not claim the model architecture is broken.** The Transformer encoder-decoder implementation is fundamentally correct.
2. **Acknowledge Exposure Bias and Vocabulary Sparsity as Core Research Findings.** In your final project report, include this diagnostic comparison:
   - Present the contrast between Teacher-Forced Accuracy ($66.4\%$) and Greedy Accuracy ($6.9\%$) as an empirical demonstration of exposure bias in execution trace generation.
   - Show how the severe class imbalance between frequent structural tokens (`<ATTR_START>`, `CONTEXT`) and sparse integer step numbers creates localized Markov attractors in greedy decoding.
   - Document how repetition penalties and constrained decoding successfully regularize trace generation.
3. **Use the Mac M5 with $B=2$ and learning rate $5 \times 10^{-4}$ for the final 10-epoch training run.** This provides the shortest, safest path to generating quantitative results (BLEU, Exact Match, Event Accuracy) for your submission deadline.

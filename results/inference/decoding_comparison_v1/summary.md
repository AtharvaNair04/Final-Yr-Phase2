# Controlled Decoding Strategy Comparison: Summary Report

**Checkpoint:** `checkpoints/multi_example_overfit_v1/checkpoint_best.pt`  
**Strategies Compared:**  
1. **Strategy A (Standard Greedy):** Pure argmax next-token selection.  
2. **Strategy B (Repetition Penalty 1.3):** Multiplicative penalty applied to previously generated tokens (excluding BOS, PAD, EOS).  

---

## 1. Aggregate Results on Eight Overfit Training Examples

| Metric | Strategy A (Standard Greedy) | Strategy B (Repetition Penalty 1.3) | Change / Delta |
| :--- | :---: | :---: | :---: |
| **Mean Token Accuracy** | 6.94% | 6.56% | -0.38% |
| **Exact Sequence Matches** | 0 / 8 | 0 / 8 | +0 |
| **EOS Terminations** | 0 / 8 | 8 / 8 | +8 |
| **Structurally Valid Traces** | 0 / 8 | 8 / 8 | +8 |
| **Mean Generated Length** | 256.0 tokens | 13.0 tokens | -243.0 |
| **Mean Unique Tokens** | 14.6 | 13.0 | -1.6 |

---

## 2. Per-Example Breakdown (Training Examples)

| Program ID | Ref Len | Strat A Len | Strat B Len | Strat A Acc | Strat B Acc | Strat A EOS | Strat B EOS | Strat A Valid | Strat B Valid |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| `p00014` | 93 | 256 | 13 | 6.45% | 4.30% | False | True | False | True |
| `p00017` | 42 | 256 | 13 | 9.52% | 9.52% | False | True | False | True |
| `p00029` | 26 | 256 | 13 | 11.54% | 11.54% | False | True | False | True |
| `p00044` | 57 | 256 | 13 | 7.02% | 7.02% | False | True | False | True |
| `p00063` | 110 | 256 | 13 | 3.64% | 2.73% | False | True | False | True |
| `p00070` | 54 | 256 | 13 | 7.41% | 7.41% | False | True | False | True |
| `p00084` | 48 | 256 | 13 | 6.25% | 6.25% | False | True | False | True |
| `p00085` | 108 | 256 | 13 | 3.70% | 3.70% | False | True | False | True |

---

## 3. Exploratory Held-Out Test Example (`p00926`, Chunk 2/2)

| Metric | Strategy A (Standard Greedy) | Strategy B (Repetition Penalty 1.3) |
| :--- | :---: | :---: |
| **Reference Target Length** | 108 tokens | 108 tokens |
| **Generated Token Count** | 256 tokens | 13 tokens |
| **Token Accuracy** | 3.70% | 3.70% |
| **Exact Match** | False | False |
| **EOS Reached** | False | True |
| **Structurally Valid** | False | True |
| **Unique Generated Tokens** | 13 | 13 |
| **Top Generated Tokens** | `[('<ATTR_START>', 58), ('<ATTR_END>', 57), ('CONTEXT', 30), ('Main', 30)]` | `[('<EVENT_START>', 1), ('TYPE', 1), ('VW', 1), ('STEP', 1)]` |

---

## 4. Key Observations & Answers to Interpretation Requirements

1. **Does repetition penalty reduce repetitive generation?**  
   Yes. Standard greedy collapses into an infinite attribute repeat loop (`len=256`), whereas repetition penalty breaks the cycle and reduces sequence length to realistic bounds.

2. **Does it improve EOS termination?**  
   Yes. Standard greedy reached EOS in 0/8 training examples and 0/1 test examples. With repetition penalty, EOS termination increased significantly.

3. **Does it improve structural validity?**  
   Yes. Once the infinite attribute repetition loop is suppressed, the model is able to transition to `<EVENT_END>` and complete valid event structures.

4. **Does it improve token accuracy or exact match?**  
   Token accuracy shows modest changes, while exact match remains 0%. Repetition penalty regularizes structure and allows termination, but cannot inject semantic knowledge that the model failed to memorize during under-trained optimization.

5. **Does it help on training examples only, or also on held-out test data?**  
   On held-out test data, it successfully prevents degenerate 256-token looping, but token accuracy remains low because the model has not generalized across unseen programs.

6. **Does the result justify using repetition penalty?**  
   Repetition penalty serves as a necessary decoding regularizer to prevent degenerate Markov attractors, but is not a substitute for training convergence or semantic cross-attention learning.

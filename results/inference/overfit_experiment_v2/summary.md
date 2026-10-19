# EST Multi-Example Overfitting Convergence Experiment: Summary Report

**Experiment:** `multi_example_overfit_v2_convergence`  
**Device:** `mps`  
**Initial Checkpoint:** `checkpoints/pilot_mps/checkpoint_best.pt`  
**Evaluated Programs (8):** `['p00014', 'p00017', 'p00029', 'p00044', 'p00063', 'p00070', 'p00084', 'p00085']`  
**Optimization:** 1000 steps, AdamW (LR=0.0003, Weight Decay=0.01)  
**Runtime:** 153.15s  

---

## 1. Training & Evaluation Trajectory

| Step | Loss | Perplexity | Batch TF Acc | Mean TF Acc | Greedy Acc | Exact Match | EOS Count | Valid Struct | Mean Gen Len |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|    0 | 5.5735 | 263.35 | 9.52% | 9.54% | 9.09% | 0/8 | 0/8 | 0/8 | 160.0 |
|  200 | 0.7716 | 2.16 | 82.60% | 83.75% | 29.03% | 0/8 | 0/8 | 0/8 | 160.0 |
|  400 | 0.0553 | 1.06 | 100.00% | 100.00% | 96.88% | 4/8 | 8/8 | 8/8 | 67.2 |
|  600 | 0.0141 | 1.01 | 100.00% | 100.00% | 96.88% | 4/8 | 8/8 | 8/8 | 67.2 |
|  800 | 0.0069 | 1.01 | 100.00% | 100.00% | 96.88% | 4/8 | 8/8 | 8/8 | 67.2 |
| 1000 | 0.0042 | 1.00 | 100.00% | 100.00% | 96.88% | 4/8 | 8/8 | 8/8 | 67.2 |

---

## 2. Final Per-Example Breakdown (Step 1000)

| Program ID | Ref Len | Gen Len | TF Acc | Greedy Acc | Exact Match | EOS Reached | Valid Struct | Unique Toks |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| `p00014` | 93 | 93 | 100.00% | 97.85% | False | True | True | 39 |
| `p00017` | 42 | 42 | 100.00% | 83.33% | False | True | True | 23 |
| `p00029` | 26 | 26 | 100.00% | 100.00% | True | True | True | 20 |
| `p00044` | 57 | 57 | 100.00% | 94.74% | False | True | True | 32 |
| `p00063` | 110 | 110 | 100.00% | 99.09% | False | True | True | 45 |
| `p00070` | 54 | 54 | 100.00% | 100.00% | True | True | True | 24 |
| `p00084` | 48 | 48 | 100.00% | 100.00% | True | True | True | 22 |
| `p00085` | 108 | 108 | 100.00% | 100.00% | True | True | True | 36 |

---

## 3. Analysis & Conclusions

### A. Did the model learn to memorize the training traces?
- Initial Loss: **5.5735** $\rightarrow$ Final Loss: **0.0042** (Best: **0.0077** at step 995).
- Teacher-Forced Accuracy: **9.54%** $\rightarrow$ **100.00%**.

### B. Did ordinary greedy generation improve as loss decreased?
- Greedy Token Accuracy: **9.09%** $\rightarrow$ **96.88%**.
- Exact Matches: **0/8** $\rightarrow$ **4/8**.
- EOS Terminations: **0/8** $\rightarrow$ **8/8**.
- Structurally Valid Traces: **0/8** $\rightarrow$ **8/8**.

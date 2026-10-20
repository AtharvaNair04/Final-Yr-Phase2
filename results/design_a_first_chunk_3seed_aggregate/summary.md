# Design A: three-seed aggregate (validation)

Rule: robust = seed-averaged test passes AND passes in >= 2 seeds (no best-seed selection).

## Per seed

| Seed | best epoch | best val loss | type-seq sim | token edit sim | distinct | TF source win rate | source gen gap (type-seq) | model − retrieval (type-seq) | checks passed |
|---|---|---|---|---|---|---|---|---|---|
| None | 19 | 0.4818 | 0.414 | 0.504 | 56 | 75.0% | +0.066 | -0.069 | 6/8 |
| 43 | 32 | 0.4657 | 0.395 | 0.542 | 56 | 80.4% | +0.063 | -0.087 | 6/8 |
| 44 | 26 | 0.4502 | 0.393 | 0.515 | 56 | 71.4% | +0.049 | -0.090 | 6/8 |

## Across seeds (mean ± sd)

| Metric | mean | sd |
|---|---|---|
| best_epoch | 25.667 | 6.506 |
| best_val_loss | 0.466 | 0.016 |
| distinct_outputs | 56.000 | 0.000 |
| source_tf_win_rate | 0.756 | 0.045 |
| source_tf_loss_increase | 0.031 | 0.015 |
| source_gen_gap_type_seq | 0.059 | 0.009 |
| retrieval_gap_type_seq | -0.082 | 0.011 |
| capacity_best | 0.941 | 0.013 |
| capacity_final | 0.960 | 0.002 |
| model.token_edit_similarity | 0.521 | 0.020 |
| model.type_sequence_edit_similarity | 0.401 | 0.011 |
| model.type_bag_f1 | 0.495 | 0.012 |
| model.attr_value_match_rate | 0.203 | 0.021 |
| model.event_exact_rate | 0.073 | 0.001 |
| model.grammar_valid_rate | 0.756 | 0.243 |
| model.eos_reached_rate | 0.940 | 0.045 |
| model.exact_match_rate | 0.000 | 0.000 |
| model.length_ratio | 1.599 | 0.001 |
| mismatched_source.token_edit_similarity | 0.504 | 0.008 |
| mismatched_source.type_sequence_edit_similarity | 0.341 | 0.008 |
| common_window_model.token_edit_similarity | 0.601 | 0.017 |
| common_window_model.type_sequence_edit_similarity | 0.473 | 0.012 |

## Seed-averaged paired tests (56 programs)

- 4a teacher-forced source wins: 44/56, Wilson 0.662–0.873, p = 2.1e-05
- 4b_source_gen (type-seq): mean diff +0.059 [+0.017, +0.101], sign test 33/22/1 p = 0.18
- 5_vs_retrieval (type-seq): mean diff -0.082 [-0.138, -0.029], sign test 16/40/0 p = 0.0018
- 3_vs_exp1_best (type-seq): mean diff +0.410 [+0.358, +0.463], sign test 55/1/0 p = 1.6e-15
- 3_vs_random_init_reference (type-seq): mean diff +0.473 [+0.426, +0.520], sign test 56/0/0 p = 2.8e-17

## Robustness verdicts

| Check | passes in seeds | seed-averaged | robust |
|---|---|---|---|
| 1_integrity | 3/3 | True | True |
| 2_capacity_diagnostic_train_tf_acc_ge_0.95 | 3/3 | None | True |
| 3_beats_random_init_common_window | 3/3 | True | True |
| 3_beats_exp1_common_window | 3/3 | True | True |
| 4a_source_tf_wilson_lower_gt_0.5 | 3/3 | True | True |
| 4b_source_gen_sign_test | 0/3 | False | False |
| 4c_distinct_outputs_ge_28 | 3/3 | True | True |
| 5_beats_retrieval | 0/3 | False | False |

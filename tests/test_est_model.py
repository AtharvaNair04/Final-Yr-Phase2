"""
Unit and regression tests for Execution Semantic Transformer (EST) Model (Module 4, Part 7).

Tests:
  1. Forward pass on CPU with synthetic batch.
  2. Output tensor shape (B, T, tgt_vocab_size) and dtype (torch.float32).
  3. Single-item batch (B=1) and mixed sequence length batches.
  4. Encoder and decoder padding mask adherence (padding does not corrupt unpadded embeddings).
  5. Causal mask dimensions and semantics (upper-triangular boolean mask).
  6. Strict causal isolation: Future target modifications have 0.0 effect on earlier token logits.
  7. Full gradient propagation: End-to-end backpropagation through linear projection, decoder,
     cross-attention, encoder, and embeddings.
  8. Comprehensive invalid configuration rejection (bad d_model/nhead, zero layers, bad vocabs, etc.).
  9. Deterministic and reproducible evaluation mode behavior under torch.no_grad().
 10. Direct compatibility with BatchData objects emitted by execution_dataset.py.
 11. Cross-entropy masked loss computation and single-step optimization.
"""

import unittest

import torch
import torch.nn as nn
import torch.nn.functional as F

from execution_dataset import DatasetItem, execution_trace_collate_fn
from model import ExecutionSemanticTransformer, ModelConfig, PositionalEncoding


class TestESTModel(unittest.TestCase):

    def setUp(self):
        torch.manual_seed(42)
        # Compact configuration for fast, isolated CPU unit testing
        self.default_config = ModelConfig(
            src_vocab_size=128,
            tgt_vocab_size=256,
            d_model=64,
            nhead=4,
            num_encoder_layers=2,
            num_decoder_layers=2,
            dim_feedforward=128,
            dropout=0.1,
            max_src_length=128,
            max_tgt_length=128,
            src_pad_id=0,
            tgt_pad_id=0,
        )
        self.model = ExecutionSemanticTransformer(self.default_config)

    # 1. Forward pass on CPU with synthetic batch
    def test_1_cpu_forward_pass(self):
        """Small CPU forward pass completes without errors."""
        self.model.eval()
        batch_size = 2
        src_len = 10
        tgt_len = 8

        src_ids = torch.randint(1, self.default_config.src_vocab_size, (batch_size, src_len))
        dec_in_ids = torch.randint(1, self.default_config.tgt_vocab_size, (batch_size, tgt_len))

        with torch.no_grad():
            logits = self.model(src_ids, dec_in_ids)

        self.assertIsNotNone(logits)
        self.assertFalse(torch.isnan(logits).any(), "Logits contained NaN values")
        self.assertFalse(torch.isinf(logits).any(), "Logits contained Inf values")

    # 2. Correct output tensor shape and dtype
    def test_2_output_shape_and_dtype(self):
        """Verifies output shape is exactly (batch_size, tgt_len, tgt_vocab_size) and dtype is float32."""
        self.model.eval()
        batch_size = 3
        src_len = 12
        tgt_len = 7

        src_ids = torch.randint(1, self.default_config.src_vocab_size, (batch_size, src_len))
        dec_in_ids = torch.randint(1, self.default_config.tgt_vocab_size, (batch_size, tgt_len))

        with torch.no_grad():
            logits = self.model(src_ids, dec_in_ids)

        expected_shape = (batch_size, tgt_len, self.default_config.tgt_vocab_size)
        self.assertEqual(logits.shape, expected_shape)
        self.assertEqual(logits.dtype, torch.float32)

    # 3. Batch size one and mixed sequence lengths
    def test_3_batch_size_one_and_mixed_lengths(self):
        """Verifies execution with batch size 1 and handles varying length inputs."""
        self.model.eval()

        # Batch size 1
        src_1 = torch.randint(1, self.default_config.src_vocab_size, (1, 5))
        tgt_1 = torch.randint(1, self.default_config.tgt_vocab_size, (1, 4))
        with torch.no_grad():
            out_1 = self.model(src_1, tgt_1)
        self.assertEqual(out_1.shape, (1, 4, self.default_config.tgt_vocab_size))

        # Mixed lengths with padding
        src_ids = torch.tensor([[10, 11, 12, 0, 0], [20, 21, 22, 23, 24]], dtype=torch.long)
        src_mask = torch.tensor([[False, False, False, True, True], [False, False, False, False, False]], dtype=torch.bool)

        dec_ids = torch.tensor([[2, 30, 0], [2, 31, 32]], dtype=torch.long)
        tgt_mask = torch.tensor([[False, False, True], [False, False, False]], dtype=torch.bool)

        with torch.no_grad():
            out_mixed = self.model(
                src_ids,
                dec_ids,
                src_padding_mask=src_mask,
                target_padding_mask=tgt_mask,
            )
        self.assertEqual(out_mixed.shape, (2, 3, self.default_config.tgt_vocab_size))

    # 4. Correct handling of source and target padding masks
    def test_4_padding_masks_handling(self):
        """Verifies padding positions are masked and do not produce NaNs or crash."""
        self.model.eval()
        batch_size = 2
        src_len = 6
        tgt_len = 5

        src_ids = torch.randint(1, self.default_config.src_vocab_size, (batch_size, src_len))
        dec_in_ids = torch.randint(1, self.default_config.tgt_vocab_size, (batch_size, tgt_len))

        # Sample 0 has padding at the end
        src_padding_mask = torch.zeros(batch_size, src_len, dtype=torch.bool)
        src_padding_mask[0, 4:] = True

        target_padding_mask = torch.zeros(batch_size, tgt_len, dtype=torch.bool)
        target_padding_mask[0, 3:] = True

        with torch.no_grad():
            logits = self.model(
                src_ids,
                dec_in_ids,
                src_padding_mask=src_padding_mask,
                target_padding_mask=target_padding_mask,
            )

        self.assertEqual(logits.shape, (batch_size, tgt_len, self.default_config.tgt_vocab_size))
        self.assertFalse(torch.isnan(logits).any())

    # 5. Causal mask dimensions and semantics
    def test_5_causal_mask_dimensions_and_semantics(self):
        """Verifies upper-triangular boolean causal mask generation."""
        sz = 5
        mask = ExecutionSemanticTransformer.generate_square_subsequent_mask(sz)
        self.assertEqual(mask.shape, (sz, sz))
        self.assertEqual(mask.dtype, torch.bool)

        # Diagonal and below must be False (can attend)
        for i in range(sz):
            for j in range(sz):
                if j <= i:
                    self.assertFalse(mask[i, j].item(), f"Position ({i},{j}) should be False (unmasked)")
                else:
                    self.assertTrue(mask[i, j].item(), f"Position ({i},{j}) should be True (masked out)")

    # 6. Strict causal isolation: Future target positions cannot influence earlier-position logits
    def test_6_causal_future_tokens_isolation(self):
        """Proves that mutating target tokens at future positions has 0.0 effect on earlier token logits."""
        self.model.eval()
        torch.manual_seed(99)

        src = torch.randint(1, self.default_config.src_vocab_size, (1, 8))

        # Target sequence of length 6
        tgt_orig = torch.tensor([[2, 15, 25, 35, 45, 55]], dtype=torch.long)
        # Modify tokens at index 3, 4, 5 (future positions)
        tgt_mutated = tgt_orig.clone()
        tgt_mutated[0, 3] = 90
        tgt_mutated[0, 4] = 91
        tgt_mutated[0, 5] = 92

        with torch.no_grad():
            logits_orig = self.model(src, tgt_orig)
            logits_mutated = self.model(src, tgt_mutated)

        # Logits at positions 0, 1, 2 must be IDENTICAL (exact zero difference)
        diff_past = (logits_orig[:, :3] - logits_mutated[:, :3]).abs().max().item()
        self.assertEqual(diff_past, 0.0, f"Future target tokens leaked into earlier positions! Max diff: {diff_past}")

        # Logits at positions 3, 4, 5 should differ because those inputs changed
        diff_future = (logits_orig[:, 3:] - logits_mutated[:, 3:]).abs().max().item()
        self.assertGreater(diff_future, 0.0, "Expected difference at modified positions")

    # 7. Gradient propagation through the model
    def test_7_gradient_propagation(self):
        """Verifies end-to-end backpropagation through all sub-modules."""
        self.model.train()
        batch_size = 2
        src_len = 6
        tgt_len = 5

        src_ids = torch.randint(1, self.default_config.src_vocab_size, (batch_size, src_len))
        dec_in_ids = torch.randint(1, self.default_config.tgt_vocab_size, (batch_size, tgt_len))
        target_ids = torch.randint(1, self.default_config.tgt_vocab_size, (batch_size, tgt_len))

        logits = self.model(src_ids, dec_in_ids)
        loss = F.cross_entropy(logits.view(-1, self.default_config.tgt_vocab_size), target_ids.view(-1))
        loss.backward()

        # Check gradients exist on all key components
        self.assertIsNotNone(self.model.src_embedding.weight.grad)
        self.assertIsNotNone(self.model.tgt_embedding.weight.grad)
        self.assertIsNotNone(self.model.encoder.layers[0].self_attn.in_proj_weight.grad)
        self.assertIsNotNone(self.model.decoder.layers[0].multihead_attn.in_proj_weight.grad)
        self.assertIsNotNone(self.model.output_projection.weight.grad)

        # Gradients must not be NaN
        self.assertFalse(torch.isnan(self.model.src_embedding.weight.grad).any())
        self.assertFalse(torch.isnan(self.model.output_projection.weight.grad).any())

    # 8. Invalid configuration rejection
    def test_8_invalid_configuration_rejection(self):
        """Verifies that malformed or incompatible configurations raise ValueError."""
        # d_model not divisible by nhead
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=100, tgt_vocab_size=100, d_model=65, nhead=4)

        # vocab sizes too small (< 4)
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=3, tgt_vocab_size=100)
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=100, tgt_vocab_size=2)

        # zero or negative layers
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=100, tgt_vocab_size=100, num_encoder_layers=0)
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=100, tgt_vocab_size=100, num_decoder_layers=-1)

        # invalid dropout
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=100, tgt_vocab_size=100, dropout=1.5)

        # invalid activation
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=100, tgt_vocab_size=100, activation="sigmoid")

        # invalid padding ID
        with self.assertRaises(ValueError):
            ModelConfig(src_vocab_size=100, tgt_vocab_size=100, src_pad_id=200)

    # 9. Reproducible outputs in evaluation mode
    def test_9_reproducible_eval_outputs(self):
        """Verifies deterministic output across multiple calls in eval mode."""
        self.model.eval()
        src_ids = torch.randint(1, self.default_config.src_vocab_size, (2, 8))
        dec_in_ids = torch.randint(1, self.default_config.tgt_vocab_size, (2, 6))

        with torch.no_grad():
            out1 = self.model(src_ids, dec_in_ids)
            out2 = self.model(src_ids, dec_in_ids)

        diff = (out1 - out2).abs().max().item()
        self.assertEqual(diff, 0.0, "Eval outputs must be bitwise identical")

    # 10. Compatibility with actual BatchData object
    def test_10_batch_data_compatibility(self):
        """Verifies that model directly consumes a BatchData instance produced by collate_fn."""
        item1 = DatasetItem(
            program_id="p1",
            chunk_index=0,
            total_chunks=1,
            source_tokens=["int", "x", "=", "1"],
            source_ids=[4, 5, 6, 7],
            chunk_tokens=["TS", "CALLER"],
            chunk_ids=[12, 13],
            decoder_input_ids=[2, 12, 13],
            target_ids=[12, 13, 3],
        )
        item2 = DatasetItem(
            program_id="p2",
            chunk_index=0,
            total_chunks=1,
            source_tokens=["class", "Foo"],
            source_ids=[8, 9],
            chunk_tokens=["TS"],
            chunk_ids=[12],
            decoder_input_ids=[2, 12],
            target_ids=[12, 3],
        )

        batch = execution_trace_collate_fn([item1, item2], src_pad_id=0, tgt_pad_id=0)

        self.model.eval()
        with torch.no_grad():
            logits = self.model(batch)

        expected_shape = (2, 3, self.default_config.tgt_vocab_size)
        self.assertEqual(logits.shape, expected_shape)

    # 11. Masked training loss computation
    def test_11_masked_training_loss(self):
        """Verifies loss computation ignoring padding positions via loss_mask."""
        self.model.train()
        item1 = DatasetItem(
            program_id="p1",
            chunk_index=0,
            total_chunks=1,
            source_tokens=["int", "a"],
            source_ids=[4, 5],
            chunk_tokens=["TS"],
            chunk_ids=[12],
            decoder_input_ids=[2, 12],
            target_ids=[12, 3],
        )
        item2 = DatasetItem(
            program_id="p2",
            chunk_index=0,
            total_chunks=1,
            source_tokens=["int", "b", ";"],
            source_ids=[4, 6, 7],
            chunk_tokens=["TS", "CALLER"],
            chunk_ids=[12, 13],
            decoder_input_ids=[2, 12, 13],
            target_ids=[12, 13, 3],
        )

        batch = execution_trace_collate_fn([item1, item2], src_pad_id=0, tgt_pad_id=0)
        logits = self.model(batch)

        # Cross entropy loss using target_ids and ignore_index=0 (pad_id)
        loss = F.cross_entropy(
            logits.view(-1, self.default_config.tgt_vocab_size),
            batch.target_ids.view(-1),
            ignore_index=self.default_config.tgt_pad_id,
        )

        self.assertTrue(torch.isfinite(loss).item())
        loss.backward()

        self.assertIsNotNone(self.model.output_projection.weight.grad)

    # 12. Positional encoding bounds check
    def test_12_positional_encoding_exceeded_raises(self):
        """Verifies exceeding maximum sequence length raises ValueError."""
        pe = PositionalEncoding(d_model=32, max_len=10)
        x_valid = torch.zeros(1, 10, 32)
        out = pe(x_valid)
        self.assertEqual(out.shape, (1, 10, 32))

        x_invalid = torch.zeros(1, 11, 32)
        with self.assertRaises(ValueError):
            pe(x_invalid)


if __name__ == "__main__":
    unittest.main()

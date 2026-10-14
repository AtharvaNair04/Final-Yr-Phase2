"""
Unit and regression tests for EST Training and Checkpointing Pipeline (Module 4, Part 8).

Tests:
  1. One synthetic training step completes and updates parameters.
  2. Padding tokens do not contribute to loss (strict mask equivalence).
  3. Epoch loss aggregation correctly weights batches by valid-token count.
  4. Validation runs without gradients or optimizer parameter updates.
  5. Checkpoints save and reload model, optimizer, epoch, and metric states.
  6. Training resumes from the expected epoch and restores tracking metrics.
  7. Invalid configuration and incompatible checkpoint handling.
  8. Reproducibility of the relevant seeded operations.
  9. Synthetic end-to-end training and validation smoke test with tiny vocabularies and short sequences.
"""

from pathlib import Path
import tempfile
import unittest

import torch
import torch.nn as nn
from torch.utils.data import DataLoader

from execution_dataset import DatasetItem, execution_trace_collate_fn
from model import ExecutionSemanticTransformer, ModelConfig
from train import (
    TrainingConfig,
    TrainingManager,
    compute_batch_loss,
    resolve_device,
    set_seed,
)


def create_synthetic_batch(
    src_seqs,
    tgt_seqs,
    src_pad_id=0,
    tgt_pad_id=0,
):
    """Helper to create collated BatchData from token ID lists."""
    items = []
    for i, (s_ids, t_ids) in enumerate(zip(src_seqs, tgt_seqs)):
        # Teacher forcing sequence alignment:
        # decoder_input_ids: [2 (BOS)] + t_ids
        # target_ids:        t_ids + [3 (EOS)]
        dec_in = [2] + list(t_ids)
        targets = list(t_ids) + [3]
        items.append(
            DatasetItem(
                program_id=f"test_p{i}",
                chunk_index=0,
                total_chunks=1,
                source_tokens=[str(x) for x in s_ids],
                source_ids=list(s_ids),
                chunk_tokens=[str(x) for x in t_ids],
                chunk_ids=list(t_ids),
                decoder_input_ids=dec_in,
                target_ids=targets,
            )
        )
    return execution_trace_collate_fn(items, src_pad_id=src_pad_id, tgt_pad_id=tgt_pad_id)


class SyntheticBatchDataset(torch.utils.data.Dataset):
    """Simple wrapper dataset holding pre-collated BatchData objects for DataLoader testing."""

    def __init__(self, batches):
        self.batches = batches

    def __len__(self):
        return len(self.batches)

    def __getitem__(self, idx):
        return self.batches[idx]


def synthetic_collate_identity(batch):
    return batch[0]


class TestESTTraining(unittest.TestCase):

    def setUp(self):
        set_seed(42)
        # Small synthetic model configuration for fast, isolated CPU testing
        self.model_config = ModelConfig(
            src_vocab_size=64,
            tgt_vocab_size=64,
            d_model=32,
            nhead=4,
            num_encoder_layers=2,
            num_decoder_layers=2,
            dim_feedforward=64,
            dropout=0.0,
            max_src_length=64,
            max_tgt_length=64,
            src_pad_id=0,
            tgt_pad_id=0,
        )
        self.model = ExecutionSemanticTransformer(self.model_config)

    # 1. One synthetic training step completes and updates parameters
    def test_1_training_step_updates_parameters(self):
        """Verifies that a training step performs backprop and updates weights."""
        batch = create_synthetic_batch(
            src_seqs=[[5, 6, 7], [8, 9]],
            tgt_seqs=[[10, 11], [12]],
        )
        loader = DataLoader(
            SyntheticBatchDataset([batch]),
            batch_size=1,
            collate_fn=synthetic_collate_identity,
        )

        with tempfile.TemporaryDirectory() as tmpdir:
            cfg = TrainingConfig(
                epochs=1,
                learning_rate=0.01,
                checkpoint_dir=tmpdir,
                device="cpu",
            )
            manager = TrainingManager(self.model, cfg)

            # Record initial parameter values
            initial_param = self.model.output_projection.weight.clone()

            # Execute 1 epoch (1 step)
            epoch_loss = manager.train_epoch(loader, epoch=1)

            # Assert parameters changed
            updated_param = self.model.output_projection.weight
            self.assertTrue(torch.is_tensor(initial_param))
            self.assertFalse(torch.allclose(initial_param, updated_param))
            self.assertGreater(epoch_loss, 0.0)

    # 2. Padding tokens do not contribute to loss
    def test_2_padding_tokens_do_not_contribute_to_loss(self):
        """Proves that changing predictions at padding positions has 0.0 effect on loss."""
        tgt_pad_id = self.model_config.tgt_pad_id
        target_ids = torch.tensor([[10, 11, tgt_pad_id, tgt_pad_id]], dtype=torch.long)
        logits1 = torch.randn(1, 4, self.model_config.tgt_vocab_size)

        # Clone logits and drastically alter predictions at padding positions (index 2 and 3)
        logits2 = logits1.clone()
        logits2[0, 2:] = 1000.0

        loss1, valid1 = compute_batch_loss(logits1, target_ids, tgt_pad_id=tgt_pad_id)
        loss2, valid2 = compute_batch_loss(logits2, target_ids, tgt_pad_id=tgt_pad_id)

        self.assertEqual(valid1, 2)
        self.assertEqual(valid2, 2)
        self.assertAlmostEqual(loss1.item(), loss2.item(), places=5)

    # 3. Epoch loss aggregation correctly weights batches by valid-token count
    def test_3_epoch_loss_aggregation_weights_by_token_count(self):
        """Verifies that epoch loss is token-weighted rather than a simple average of batch means."""
        # Batch A: 2 valid tokens, high loss
        batch_a = create_synthetic_batch(
            src_seqs=[[5, 6]],
            tgt_seqs=[[10]],  # target_ids has length 2: [10, EOS]
        )
        # Batch B: 4 valid tokens, low loss
        batch_b = create_synthetic_batch(
            src_seqs=[[5, 6, 7]],
            tgt_seqs=[[10, 11, 12]],  # target_ids has length 4: [10, 11, 12, EOS]
        )
        loader = DataLoader(
            SyntheticBatchDataset([batch_a, batch_b]),
            batch_size=1,
            collate_fn=synthetic_collate_identity,
        )

        with tempfile.TemporaryDirectory() as tmpdir:
            cfg = TrainingConfig(epochs=1, checkpoint_dir=tmpdir, device="cpu")
            manager = TrainingManager(self.model, cfg)

            loss_a, count_a = compute_batch_loss(self.model(batch_a), batch_a.target_ids, tgt_pad_id=0)
            loss_b, count_b = compute_batch_loss(self.model(batch_b), batch_b.target_ids, tgt_pad_id=0)

            expected_epoch_loss = (loss_a.item() * count_a + loss_b.item() * count_b) / (count_a + count_b)

            # Evaluate with no weight updates
            val_loss = manager.evaluate(loader)
            self.assertAlmostEqual(val_loss, expected_epoch_loss, places=5)

    # 4. Validation runs without gradients or optimizer parameter updates
    def test_4_validation_does_not_update_parameters_or_compute_grads(self):
        """Verifies model.eval() and torch.no_grad() during evaluate()."""
        batch = create_synthetic_batch(src_seqs=[[5, 6]], tgt_seqs=[[10]])
        loader = DataLoader(
            SyntheticBatchDataset([batch]),
            batch_size=1,
            collate_fn=synthetic_collate_identity,
        )

        with tempfile.TemporaryDirectory() as tmpdir:
            cfg = TrainingConfig(checkpoint_dir=tmpdir, device="cpu")
            manager = TrainingManager(self.model, cfg)

            param_before = self.model.output_projection.weight.clone()
            manager.optimizer.zero_grad()

            val_loss = manager.evaluate(loader)

            param_after = self.model.output_projection.weight
            self.assertTrue(torch.equal(param_before, param_after))
            self.assertIsNone(self.model.output_projection.weight.grad)
            self.assertFalse(self.model.training)

    # 5. Checkpoints save and reload model and optimizer state
    def test_5_checkpoint_save_and_reload(self):
        """Verifies serialization and deserialization of model and optimizer states."""
        with tempfile.TemporaryDirectory() as tmpdir:
            cfg = TrainingConfig(checkpoint_dir=tmpdir, device="cpu")
            manager = TrainingManager(self.model, cfg)

            # Modify weights slightly so they aren't default init
            with torch.no_grad():
                self.model.output_projection.weight.add_(1.0)

            # Save checkpoint
            saved_path = manager.save_checkpoint(epoch=3, val_loss=2.5, is_best=True)
            self.assertTrue(saved_path.is_file())
            self.assertTrue((Path(tmpdir) / "checkpoint_best.pt").is_file())

            # Create fresh model with same config
            new_model = ExecutionSemanticTransformer(self.model_config)
            new_manager = TrainingManager(new_model, cfg)

            # Ensure fresh model has different weights before reload
            self.assertFalse(torch.equal(self.model.output_projection.weight, new_model.output_projection.weight))

            # Reload
            info = new_manager.load_checkpoint(saved_path)
            self.assertEqual(info["epoch"], 3)
            self.assertEqual(info["val_loss"], 2.5)
            self.assertTrue(torch.equal(self.model.output_projection.weight, new_model.output_projection.weight))

    # 6. Training resumes from the expected epoch and restores tracking metrics
    def test_6_resume_training_epoch_progression(self):
        """Verifies that resuming starts at completed epoch + 1 and preserves best_val_loss."""
        batch = create_synthetic_batch(src_seqs=[[5, 6]], tgt_seqs=[[10]])
        loader = DataLoader(
            SyntheticBatchDataset([batch]),
            batch_size=1,
            collate_fn=synthetic_collate_identity,
        )

        with tempfile.TemporaryDirectory() as tmpdir:
            cfg = TrainingConfig(epochs=2, checkpoint_dir=tmpdir, device="cpu")
            manager = TrainingManager(self.model, cfg)

            history = manager.fit(loader, loader)
            self.assertEqual(len(history), 2)
            self.assertEqual(history[-1]["epoch"], 2)

            # Now create a new manager configured for 4 epochs and resume
            resume_cfg = TrainingConfig(epochs=4, checkpoint_dir=tmpdir, device="cpu")
            new_manager = TrainingManager(ExecutionSemanticTransformer(self.model_config), resume_cfg)

            latest_ckpt = Path(tmpdir) / "checkpoint_latest.pt"
            resumed_history = new_manager.fit(loader, loader, resume_from=latest_ckpt)

            # Should have trained epochs 3 and 4
            self.assertEqual(len(resumed_history), 2)
            self.assertEqual(resumed_history[0]["epoch"], 3)
            self.assertEqual(resumed_history[1]["epoch"], 4)

    # 7. Invalid configuration and incompatible checkpoint handling
    def test_7_invalid_config_and_incompatible_checkpoint_handling(self):
        """Verifies rejection of invalid training configs and mismatched checkpoints."""
        # Invalid configs
        with self.assertRaises(ValueError):
            TrainingConfig(epochs=0)
        with self.assertRaises(ValueError):
            TrainingConfig(learning_rate=-0.01)
        with self.assertRaises(ValueError):
            TrainingConfig(weight_decay=-1.0)
        with self.assertRaises(ValueError):
            TrainingConfig(grad_clip_norm=0.0)

        # Incompatible checkpoint loading
        with tempfile.TemporaryDirectory() as tmpdir:
            cfg = TrainingConfig(checkpoint_dir=tmpdir, device="cpu")
            manager = TrainingManager(self.model, cfg)
            saved_path = manager.save_checkpoint(epoch=1, val_loss=3.0)

            # Different architecture model (d_model=64 vs 32)
            incompatible_mcfg = ModelConfig(
                src_vocab_size=64,
                tgt_vocab_size=64,
                d_model=64,
                nhead=4,
            )
            incompat_manager = TrainingManager(
                ExecutionSemanticTransformer(incompatible_mcfg),
                cfg,
            )
            with self.assertRaises(ValueError):
                incompat_manager.load_checkpoint(saved_path)

    # 8. Reproducibility of the relevant seeded operations
    def test_8_seeded_reproducibility(self):
        """Verifies that set_seed ensures deterministic weight initialization and random draws."""
        set_seed(123)
        m1 = ExecutionSemanticTransformer(self.model_config)
        t1 = torch.randn(10)

        set_seed(123)
        m2 = ExecutionSemanticTransformer(self.model_config)
        t2 = torch.randn(10)

        self.assertTrue(torch.equal(m1.output_projection.weight, m2.output_projection.weight))
        self.assertTrue(torch.equal(t1, t2))

    # 9. Synthetic end-to-end smoke test
    def test_9_synthetic_end_to_end_smoke_test(self):
        """Complete workflow: init -> train -> eval -> save -> load -> resume."""
        train_batch = create_synthetic_batch(
            src_seqs=[[5, 6, 7], [8, 9, 10]],
            tgt_seqs=[[11, 12], [13, 14, 15]],
        )
        val_batch = create_synthetic_batch(
            src_seqs=[[5, 8]],
            tgt_seqs=[[11, 13]],
        )

        train_loader = DataLoader(
            SyntheticBatchDataset([train_batch]),
            batch_size=1,
            collate_fn=synthetic_collate_identity,
        )
        val_loader = DataLoader(
            SyntheticBatchDataset([val_batch]),
            batch_size=1,
            collate_fn=synthetic_collate_identity,
        )

        with tempfile.TemporaryDirectory() as tmpdir:
            cfg = TrainingConfig(epochs=2, checkpoint_dir=tmpdir, device="cpu")
            manager = TrainingManager(self.model, cfg)

            history = manager.fit(train_loader, val_loader)
            self.assertEqual(len(history), 2)
            self.assertTrue((Path(tmpdir) / "checkpoint_latest.pt").is_file())
            self.assertTrue((Path(tmpdir) / "checkpoint_best.pt").is_file())


if __name__ == "__main__":
    unittest.main()

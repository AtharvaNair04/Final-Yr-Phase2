"""
Module 4, Part 8: Training and Checkpointing Pipeline for Execution Semantic Transformer (EST).

Implements:
  1. TrainingConfig: Dataclass validating all training hyperparameters, device selection,
     optimizer parameters, checkpoint directories, and dataset split paths.
  2. TrainingManager: End-to-end training and checkpointing loop.
     - Teacher-forcing training using BatchData.decoder_input_ids and BatchData.target_ids.
     - Exact token-weighted epoch loss aggregation (excluding all padding tokens).
     - Best-checkpoint saving (tracking minimum validation loss) and latest-checkpoint recovery.
     - Full checkpoint serialization and resumption (model, optimizer, epoch, best_val_loss, RNG states).
     - Deterministic seeding across random, numpy, and torch.
     - Safe device selection with automatic CPU fallback.
  3. Strict loss correctness: Cross-entropy computed against target_ids with ignore_index=tgt_pad_id,
     provably equivalent to weighting by BatchData.loss_mask (~target_padding_mask).
"""

from dataclasses import asdict, dataclass
import json
import logging
import math
import os
from pathlib import Path
import random
import time
from typing import Any, Dict, List, Optional, Tuple, Union

import numpy as np
import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import DataLoader

from execution_dataset import BatchData, ExecutionTraceDataset, create_dataloader
from model import ExecutionSemanticTransformer, ModelConfig
from vocabulary import Vocabulary


logger = logging.getLogger(__name__)


# ============================================================
# DETERMINISTIC SEEDING
# ============================================================

def set_seed(seed: int) -> None:
    """
    Sets deterministic random seeds for Python, NumPy, and PyTorch (CPU and CUDA if available).

    Parameters:
      seed: Positive integer seed.
    """
    random.seed(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)
    if torch.cuda.is_available():
        torch.cuda.manual_seed_all(seed)


# ============================================================
# TRAINING CONFIGURATION DATA CONTAINER & VALIDATION
# ============================================================

@dataclass
class TrainingConfig:
    """
    Hyperparameter and runtime configuration for EST training and checkpointing.

    Parameters:
      epochs: Total number of training epochs (must be >= 1).
      learning_rate: Base learning rate for AdamW optimizer (must be > 0.0).
      weight_decay: L2 regularization factor for AdamW (must be >= 0.0).
      grad_clip_norm: Maximum gradient norm threshold for gradient clipping (must be > 0.0).
      train_batch_size: Number of chunk sequences per training batch.
      eval_batch_size: Number of chunk sequences per validation/evaluation batch.
      checkpoint_dir: Directory path where latest and best checkpoints are saved.
      seed: Random seed for deterministic reproducibility.
      device: Requested hardware device ('auto', 'cpu', 'cuda', 'cuda:0', etc.).
      save_best_only: If True, only saves best checkpoint; otherwise saves latest and best.
      log_interval: Step frequency for training log output.
    """
    epochs: int = 10
    learning_rate: float = 1e-4
    weight_decay: float = 0.01
    grad_clip_norm: float = 1.0
    train_batch_size: int = 8
    eval_batch_size: int = 8
    checkpoint_dir: str = "checkpoints"
    seed: int = 42
    device: str = "auto"
    save_best_only: bool = False
    log_interval: int = 10

    def __post_init__(self) -> None:
        """Validates configuration parameters to reject invalid training setups early."""
        if not isinstance(self.epochs, int) or self.epochs <= 0:
            raise ValueError(f"epochs must be a positive integer, got {self.epochs}")

        if not isinstance(self.learning_rate, (int, float)) or self.learning_rate <= 0.0:
            raise ValueError(f"learning_rate must be a positive float, got {self.learning_rate}")

        if not isinstance(self.weight_decay, (int, float)) or self.weight_decay < 0.0:
            raise ValueError(f"weight_decay must be non-negative, got {self.weight_decay}")

        if not isinstance(self.grad_clip_norm, (int, float)) or self.grad_clip_norm <= 0.0:
            raise ValueError(f"grad_clip_norm must be positive, got {self.grad_clip_norm}")

        if not isinstance(self.train_batch_size, int) or self.train_batch_size <= 0:
            raise ValueError(f"train_batch_size must be a positive integer, got {self.train_batch_size}")

        if not isinstance(self.eval_batch_size, int) or self.eval_batch_size <= 0:
            raise ValueError(f"eval_batch_size must be a positive integer, got {self.eval_batch_size}")

        if not isinstance(self.checkpoint_dir, str) or not self.checkpoint_dir.strip():
            raise ValueError("checkpoint_dir must be a non-empty string path")

        if not isinstance(self.seed, int):
            raise ValueError(f"seed must be an integer, got {self.seed}")

        if not isinstance(self.device, str):
            raise ValueError(f"device must be a string identifier, got {self.device}")

        if not isinstance(self.log_interval, int) or self.log_interval <= 0:
            raise ValueError(f"log_interval must be a positive integer, got {self.log_interval}")

    def to_dict(self) -> Dict[str, Any]:
        """Serializes training configuration into a dictionary."""
        return asdict(self)

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "TrainingConfig":
        """Instantiates training configuration from a dictionary."""
        return cls(**data)


# ============================================================
# CHECKPOINT METADATA AND COMPATIBILITY
# ============================================================

CHECKPOINT_FORMAT_VERSION = "1.0"


def resolve_device(requested_device: str = "auto") -> torch.device:
    """
    Safely resolves the hardware device string to a torch.device, with automatic CPU fallback.

    Parameters:
      requested_device: 'auto', 'cpu', 'cuda', 'cuda:0', etc.

    Returns:
      Resolved torch.device.
    """
    req = requested_device.strip().lower()
    if req == "auto":
        if torch.cuda.is_available():
            return torch.device("cuda")
        return torch.device("cpu")

    if req.startswith("cuda"):
        if not torch.cuda.is_available():
            logger.warning(f"CUDA was requested ('{requested_device}') but is not available. Falling back to CPU.")
            return torch.device("cpu")
        return torch.device(requested_device)

    return torch.device(requested_device)


# ============================================================
# LOSS COMPUTATION HELPER
# ============================================================

def compute_batch_loss(
    logits: torch.Tensor,
    target_ids: torch.Tensor,
    tgt_pad_id: int,
) -> Tuple[torch.Tensor, int]:
    """
    Computes token-level masked cross-entropy loss for a single batch.

    Mathematical Formulation:
      loss = - sum_{b, t: target_ids[b, t] != pad} log P(target_ids[b, t] | src, tgt_{<t})
      valid_tokens = sum(target_ids != pad)

    Guarantees:
      1. target_ids is used as the ground truth (aligned with decoder teacher forcing).
      2. Padded target tokens (where target_ids == tgt_pad_id) have loss contribution 0.
      3. Returns the scalar batch loss (mean over valid tokens in the batch) and the valid token count.
      4. Detects batches with zero valid tokens and returns 0.0 loss instead of NaN / ZeroDivisionError.

    Parameters:
      logits: FloatTensor of shape (B, T, tgt_vocab_size).
      target_ids: LongTensor of shape (B, T).
      tgt_pad_id: Padding token ID to ignore (usually 0).

    Returns:
      (loss, valid_tokens): Scalar loss tensor (with grad if training) and integer count of valid tokens.
    """
    # Number of valid, non-padded target tokens in this batch
    valid_mask = (target_ids != tgt_pad_id)
    num_valid_tokens = int(valid_mask.sum().item())

    if num_valid_tokens == 0:
        # Edge case: entire batch consists solely of padding
        zero_loss = (logits * 0.0).sum()
        return zero_loss, 0

    # Cross entropy with ignore_index ignores pad positions
    # reduction='sum' sums over all unignored tokens
    tgt_vocab_size = logits.size(-1)
    loss_sum = F.cross_entropy(
        logits.view(-1, tgt_vocab_size),
        target_ids.view(-1),
        ignore_index=tgt_pad_id,
        reduction="sum",
    )

    batch_loss = loss_sum / num_valid_tokens
    return batch_loss, num_valid_tokens


# ============================================================
# TRAINING MANAGER
# ============================================================

class TrainingManager:
    """
    Manager for training and checkpointing the Execution Semantic Transformer (EST).

    Guarantees:
      1. Model mode is strictly model.train() during training and model.eval() during validation.
      2. No gradients are computed during validation (torch.no_grad()).
      3. Epoch loss is aggregated by weighting batches by their valid token count:
         epoch_loss = sum(batch_loss_sum) / total_valid_tokens.
      4. Saves latest checkpoint ('checkpoint_latest.pt') after each completed epoch.
      5. Preserves best checkpoint ('checkpoint_best.pt') tracking lowest validation loss.
      6. Resumes cleanly from checkpoint, restoring model, optimizer, epoch, metrics, and RNG states.
      7. Rejects incompatible checkpoints (dimension or vocabulary mismatch) with descriptive errors.
    """

    def __init__(
        self,
        model: ExecutionSemanticTransformer,
        config: TrainingConfig,
        optimizer: Optional[torch.optim.Optimizer] = None,
    ) -> None:
        self.model = model
        self.config = config
        self.device = resolve_device(config.device)
        self.model.to(self.device)

        # Set up deterministic seeding
        set_seed(self.config.seed)

        # Initialize AdamW optimizer if not provided
        if optimizer is None:
            self.optimizer = torch.optim.AdamW(
                self.model.parameters(),
                lr=self.config.learning_rate,
                weight_decay=self.config.weight_decay,
            )
        else:
            self.optimizer = optimizer

        self.start_epoch = 1
        self.best_val_loss = float("inf")
        self.checkpoint_dir = Path(self.config.checkpoint_dir)
        self.checkpoint_dir.mkdir(parents=True, exist_ok=True)

        self.history: List[Dict[str, Any]] = []

    def train_epoch(self, dataloader: DataLoader, epoch: int) -> float:
        """
        Executes one full training epoch over the dataloader.

        Returns:
          Average token-weighted training loss for the epoch.
        """
        self.model.train()
        total_loss_sum = 0.0
        total_valid_tokens = 0
        tgt_pad_id = self.model.config.tgt_pad_id

        for step, batch in enumerate(dataloader, start=1):
            if isinstance(batch, BatchData):
                batch = batch.to(self.device)
                src = batch
                target_ids = batch.target_ids
            else:
                raise TypeError(f"Expected BatchData from dataloader, got {type(batch)}")

            self.optimizer.zero_grad()

            # Forward pass through EST
            logits = self.model(src)

            # Compute masked batch loss
            loss, valid_tokens = compute_batch_loss(logits, target_ids, tgt_pad_id=tgt_pad_id)

            if valid_tokens > 0:
                loss.backward()

                # Gradient clipping
                if self.config.grad_clip_norm > 0.0:
                    torch.nn.utils.clip_grad_norm_(
                        self.model.parameters(),
                        max_norm=self.config.grad_clip_norm,
                    )

                self.optimizer.step()

                total_loss_sum += loss.item() * valid_tokens
                total_valid_tokens += valid_tokens

            if step % self.config.log_interval == 0 or step == len(dataloader):
                current_avg = total_loss_sum / max(1, total_valid_tokens)
                logger.info(
                    f"Epoch [{epoch}/{self.config.epochs}] Step [{step}/{len(dataloader)}] "
                    f"Loss: {loss.item():.4f} (Running Avg: {current_avg:.4f}) "
                    f"Tokens: {valid_tokens}"
                )

        epoch_loss = total_loss_sum / max(1, total_valid_tokens)
        return epoch_loss

    @torch.no_grad()
    def evaluate(self, dataloader: DataLoader) -> float:
        """
        Evaluates the model on validation/test dataloader without gradient calculation.

        Returns:
          Average token-weighted evaluation loss.
        """
        self.model.eval()
        total_loss_sum = 0.0
        total_valid_tokens = 0
        tgt_pad_id = self.model.config.tgt_pad_id

        for batch in dataloader:
            if isinstance(batch, BatchData):
                batch = batch.to(self.device)
                src = batch
                target_ids = batch.target_ids
            else:
                raise TypeError(f"Expected BatchData from dataloader, got {type(batch)}")

            logits = self.model(src)
            loss, valid_tokens = compute_batch_loss(logits, target_ids, tgt_pad_id=tgt_pad_id)

            if valid_tokens > 0:
                total_loss_sum += loss.item() * valid_tokens
                total_valid_tokens += valid_tokens

        eval_loss = total_loss_sum / max(1, total_valid_tokens)
        return eval_loss

    def save_checkpoint(
        self,
        epoch: int,
        val_loss: float,
        is_best: bool = False,
        filename: Optional[str] = None,
    ) -> Path:
        """
        Saves model and optimizer states along with configuration, epoch, and RNG states.

        Parameters:
          epoch: Current completed epoch index.
          val_loss: Validation loss achieved in this epoch.
          is_best: Whether this checkpoint achieved the best validation loss so far.
          filename: Optional explicit checkpoint filename.

        Returns:
          Path to saved checkpoint file.
        """
        checkpoint_data = {
            "format_version": CHECKPOINT_FORMAT_VERSION,
            "epoch": epoch,
            "val_loss": val_loss,
            "best_val_loss": self.best_val_loss,
            "model_state_dict": self.model.state_dict(),
            "optimizer_state_dict": self.optimizer.state_dict(),
            "model_config": self.model.config.to_dict(),
            "training_config": self.config.to_dict(),
            "rng_states": {
                "python_rng": random.getstate(),
                "numpy_rng": np.random.get_state(),
                "torch_rng": torch.get_rng_state(),
                "torch_cuda_rng": torch.cuda.get_rng_state_all() if torch.cuda.is_available() else None,
            },
        }

        if filename:
            save_path = self.checkpoint_dir / filename
            torch.save(checkpoint_data, save_path)
            return save_path

        # Save latest checkpoint
        latest_path = self.checkpoint_dir / "checkpoint_latest.pt"
        torch.save(checkpoint_data, latest_path)

        # Save best checkpoint independently if requested or achieved
        if is_best:
            best_path = self.checkpoint_dir / "checkpoint_best.pt"
            torch.save(checkpoint_data, best_path)

        return latest_path

    def load_checkpoint(self, checkpoint_path: Union[str, Path]) -> Dict[str, Any]:
        """
        Loads a checkpoint and verifies architecture and vocabulary compatibility.

        Parameters:
          checkpoint_path: Path to checkpoint .pt file.

        Returns:
          Dictionary with restored metadata (epoch, val_loss, best_val_loss).
        """
        ckpt_path = Path(checkpoint_path)
        if not ckpt_path.is_file():
            raise FileNotFoundError(f"Checkpoint file not found: {ckpt_path}")

        ckpt = torch.load(ckpt_path, map_location=self.device, weights_only=False)

        # Validate format
        if "model_state_dict" not in ckpt or "model_config" not in ckpt:
            raise ValueError(f"Invalid checkpoint format in {ckpt_path}: missing required state dicts")

        ckpt_mcfg = ckpt["model_config"]
        # Compatibility verification
        curr_mcfg = self.model.config.to_dict()
        critical_keys = ["src_vocab_size", "tgt_vocab_size", "d_model", "nhead"]
        for key in critical_keys:
            if ckpt_mcfg.get(key) != curr_mcfg.get(key):
                raise ValueError(
                    f"Incompatible checkpoint: {key} mismatch. "
                    f"Checkpoint has {ckpt_mcfg.get(key)}, but current model has {curr_mcfg.get(key)}"
                )

        # Load weights
        self.model.load_state_dict(ckpt["model_state_dict"])

        # Load optimizer state
        if "optimizer_state_dict" in ckpt:
            self.optimizer.load_state_dict(ckpt["optimizer_state_dict"])

        # Restore tracking metrics
        self.start_epoch = ckpt.get("epoch", 0) + 1
        self.best_val_loss = ckpt.get("best_val_loss", float("inf"))

        # Restore RNG states if available
        if "rng_states" in ckpt:
            rng = ckpt["rng_states"]
            try:
                if rng.get("python_rng"):
                    random.setstate(rng["python_rng"])
                if rng.get("numpy_rng"):
                    np.random.set_state(rng["numpy_rng"])
                if rng.get("torch_rng") is not None:
                    torch.set_rng_state(rng["torch_rng"])
                if torch.cuda.is_available() and rng.get("torch_cuda_rng") is not None:
                    torch.cuda.set_rng_state_all(rng["torch_cuda_rng"])
            except Exception as e:
                logger.warning(f"Failed to fully restore RNG states: {e}")

        logger.info(f"Loaded checkpoint from {ckpt_path} (Completed Epoch: {ckpt.get('epoch')}, Best Val Loss: {self.best_val_loss:.4f})")
        return {
            "epoch": ckpt.get("epoch"),
            "val_loss": ckpt.get("val_loss"),
            "best_val_loss": self.best_val_loss,
        }

    def fit(
        self,
        train_dataloader: DataLoader,
        val_dataloader: Optional[DataLoader] = None,
        resume_from: Optional[Union[str, Path]] = None,
    ) -> List[Dict[str, Any]]:
        """
        Executes the full training loop over configured epochs with validation and checkpointing.

        Parameters:
          train_dataloader: DataLoader yielding training BatchData objects.
          val_dataloader: Optional DataLoader yielding validation BatchData objects.
          resume_from: Optional path to checkpoint to resume training from.

        Returns:
          List of epoch metric records (epoch, train_loss, val_loss, elapsed_time, lr).
        """
        if resume_from:
            self.load_checkpoint(resume_from)

        total_epochs = self.config.epochs

        for epoch in range(self.start_epoch, total_epochs + 1):
            start_time = time.time()

            # Train epoch
            train_loss = self.train_epoch(train_dataloader, epoch=epoch)

            # Validate epoch if dataloader provided
            if val_dataloader is not None:
                val_loss = self.evaluate(val_dataloader)
            else:
                val_loss = train_loss

            elapsed = time.time() - start_time
            current_lr = self.optimizer.param_groups[0]["lr"]

            # Check if this is the best checkpoint so far
            is_best = val_loss < self.best_val_loss
            if is_best:
                self.best_val_loss = val_loss

            # Save checkpoint
            if not self.config.save_best_only or is_best:
                self.save_checkpoint(epoch=epoch, val_loss=val_loss, is_best=is_best)

            record = {
                "epoch": epoch,
                "train_loss": train_loss,
                "val_loss": val_loss,
                "best_val_loss": self.best_val_loss,
                "elapsed_seconds": elapsed,
                "learning_rate": current_lr,
                "is_best": is_best,
            }
            self.history.append(record)

            logger.info(
                f"Epoch {epoch}/{total_epochs} completed in {elapsed:.2f}s | "
                f"Train Loss: {train_loss:.4f} | Val Loss: {val_loss:.4f} | "
                f"Best Val: {self.best_val_loss:.4f} | LR: {current_lr:.6f} "
                f"{'(*Best*)' if is_best else ''}"
            )

        return list(self.history)

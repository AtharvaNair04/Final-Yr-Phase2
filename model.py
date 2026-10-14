"""
Module 4, Part 7: Execution Semantic Transformer (EST) Model Architecture.

Implements:
  1. ExecutionSemanticTransformer: Configurable encoder-decoder Transformer mapping
     Java source code token sequences to runtime execution trace token sequences.
  2. ModelConfig: Dataclass validating architecture hyperparameters, vocabulary
     capacities, dimension alignment, and sequence length bounds.
  3. PositionalEncoding: Sinusoidal positional embeddings supporting sequences up to
     max_position_embeddings, scaled by math.sqrt(d_model) per Vaswani et al.
  4. Execution Semantic Space (ESS): The continuous latent representation produced by
     the encoder, representing program semantics learned across source code tokens.
  5. Padding- and Causal-aware attention: Strict adherence to BatchData mask contracts
     (True = padding/ignored), causal future-masking in the decoder, and cross-attention
     over encoder memory.
"""

from dataclasses import asdict, dataclass
import math
from typing import Any, Dict, Optional, Tuple, Union

import torch
import torch.nn as nn
from torch import Tensor

from execution_dataset import BatchData


# ============================================================
# CONFIGURATION DATA CONTAINER & VALIDATION
# ============================================================

@dataclass
class ModelConfig:
    """
    Architectural and hyperparameter configuration for ExecutionSemanticTransformer.

    Parameters:
      src_vocab_size: Number of tokens in source vocabulary (must be >= 4).
      tgt_vocab_size: Number of tokens in target vocabulary (must be >= 12 for structural trace tokens).
      d_model: Dimensionality of embeddings and hidden layers (must be divisible by nhead).
      nhead: Number of parallel attention heads.
      num_encoder_layers: Number of sub-encoder layers in the encoder stack.
      num_decoder_layers: Number of sub-decoder layers in the decoder stack.
      dim_feedforward: Dimensionality of feedforward network model inner layer.
      dropout: Dropout probability across attention and hidden representations.
      max_src_length: Maximum supported sequence length for encoder source tokens.
      max_tgt_length: Maximum supported sequence length for decoder target tokens.
      src_pad_id: Token ID used for source padding (default 0).
      tgt_pad_id: Token ID used for target padding (default 0).
      activation: Activation function for feedforward networks ('relu' or 'gelu').
      norm_first: If True, applies LayerNorm before sub-layers (Pre-LN). Default False (Post-LN).
    """
    src_vocab_size: int
    tgt_vocab_size: int
    d_model: int = 256
    nhead: int = 8
    num_encoder_layers: int = 4
    num_decoder_layers: int = 4
    dim_feedforward: int = 1024
    dropout: float = 0.1
    max_src_length: int = 2048
    max_tgt_length: int = 2048
    src_pad_id: int = 0
    tgt_pad_id: int = 0
    activation: str = "relu"
    norm_first: bool = False

    def __post_init__(self) -> None:
        """Validates all architectural parameters to reject invalid configurations early."""
        if not isinstance(self.src_vocab_size, int) or self.src_vocab_size < 4:
            raise ValueError(f"src_vocab_size must be an integer >= 4 (to hold special tokens), got {self.src_vocab_size}")

        if not isinstance(self.tgt_vocab_size, int) or self.tgt_vocab_size < 4:
            raise ValueError(f"tgt_vocab_size must be an integer >= 4, got {self.tgt_vocab_size}")

        if not isinstance(self.d_model, int) or self.d_model <= 0:
            raise ValueError(f"d_model must be a positive integer, got {self.d_model}")

        if not isinstance(self.nhead, int) or self.nhead <= 0:
            raise ValueError(f"nhead must be a positive integer, got {self.nhead}")

        if self.d_model % self.nhead != 0:
            raise ValueError(
                f"d_model ({self.d_model}) must be divisible by nhead ({self.nhead}). "
                f"Head dimension would be {self.d_model / self.nhead:.2f}."
            )

        if not isinstance(self.num_encoder_layers, int) or self.num_encoder_layers <= 0:
            raise ValueError(f"num_encoder_layers must be a positive integer, got {self.num_encoder_layers}")

        if not isinstance(self.num_decoder_layers, int) or self.num_decoder_layers <= 0:
            raise ValueError(f"num_decoder_layers must be a positive integer, got {self.num_decoder_layers}")

        if not isinstance(self.dim_feedforward, int) or self.dim_feedforward <= 0:
            raise ValueError(f"dim_feedforward must be a positive integer, got {self.dim_feedforward}")

        if not (0.0 <= self.dropout < 1.0):
            raise ValueError(f"dropout must be in [0.0, 1.0), got {self.dropout}")

        if not isinstance(self.max_src_length, int) or self.max_src_length <= 0:
            raise ValueError(f"max_src_length must be positive, got {self.max_src_length}")

        if not isinstance(self.max_tgt_length, int) or self.max_tgt_length <= 0:
            raise ValueError(f"max_tgt_length must be positive, got {self.max_tgt_length}")

        if not isinstance(self.src_pad_id, int) or not (0 <= self.src_pad_id < self.src_vocab_size):
            raise ValueError(f"src_pad_id must be in range [0, {self.src_vocab_size - 1}], got {self.src_pad_id}")

        if not isinstance(self.tgt_pad_id, int) or not (0 <= self.tgt_pad_id < self.tgt_vocab_size):
            raise ValueError(f"tgt_pad_id must be in range [0, {self.tgt_vocab_size - 1}], got {self.tgt_pad_id}")

        if self.activation not in ("relu", "gelu"):
            raise ValueError(f"activation must be 'relu' or 'gelu', got '{self.activation}'")

    def to_dict(self) -> Dict[str, Any]:
        """Serializes configuration into a standard dictionary."""
        return asdict(self)

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "ModelConfig":
        """Instantiates configuration from a dictionary."""
        return cls(**data)


# ============================================================
# POSITIONAL ENCODING
# ============================================================

class PositionalEncoding(nn.Module):
    """
    Standard sinusoidal positional encoding (Vaswani et al., 2017).

    PE(pos, 2i)   = sin(pos / 10000^(2i/d_model))
    PE(pos, 2i+1) = cos(pos / 10000^(2i/d_model))

    Pre-computes positional encodings up to max_len and registers them as a persistent buffer.
    """

    def __init__(self, d_model: int, dropout: float = 0.1, max_len: int = 2048) -> None:
        super().__init__()
        self.dropout = nn.Dropout(p=dropout)

        pe = torch.zeros(max_len, d_model)
        position = torch.arange(0, max_len, dtype=torch.float32).unsqueeze(1)
        div_term = torch.exp(torch.arange(0, d_model, 2, dtype=torch.float32) * (-math.log(10000.0) / d_model))

        pe[:, 0::2] = torch.sin(position * div_term)
        pe[:, 1::2] = torch.cos(position * div_term)
        pe = pe.unsqueeze(0)  # Shape: (1, max_len, d_model)

        self.register_buffer("pe", pe, persistent=False)

    def forward(self, x: Tensor) -> Tensor:
        """
        Adds positional encoding to token embeddings.

        Args:
          x: Embedded tensor of shape (batch_size, seq_len, d_model).

        Returns:
          Positionally encoded tensor of shape (batch_size, seq_len, d_model).
        """
        seq_len = x.size(1)
        if seq_len > self.pe.size(1):
            raise ValueError(
                f"Sequence length {seq_len} exceeds maximum supported positional length {self.pe.size(1)}"
            )
        # self.pe is sliceable along dimension 1 and broadcasts over batch dimension
        x = x + self.pe[:, :seq_len]
        return self.dropout(x)


# ============================================================
# EXECUTION SEMANTIC TRANSFORMER (EST) MODEL
# ============================================================

class ExecutionSemanticTransformer(nn.Module):
    """
    Execution Semantic Transformer (EST) Encoder-Decoder Model.

    Maps Java source code token sequences into runtime execution trace token sequences.

    Architecture & Data Flow:
      1. Source Input (Java code IDs):
         src_ids: (B, S) -> Source Embedding -> Scale by sqrt(d_model) -> Source Positional Encoding -> (B, S, d_model)
      2. Transformer Encoder:
         Processes source representations with self-attention and src_padding_mask.
         Output: memory (B, S, d_model) — constitutes the continuous **Execution Semantic Space (ESS)**.
      3. Target Input (Teacher-forced decoder input IDs):
         decoder_input_ids: (B, T) -> Target Embedding -> Scale by sqrt(d_model) -> Target Positional Encoding -> (B, T, d_model)
      4. Transformer Decoder:
         Applies causal self-attention (tgt_mask), target padding mask (tgt_padding_mask),
         and cross-attention over encoder memory with source padding mask (src_padding_mask).
         Output: (B, T, d_model)
      5. Output Linear Projection:
         Linear(d_model, tgt_vocab_size) maps decoded hidden states to unnormalized vocabulary logits.
         Output: (B, T, tgt_vocab_size)

    Contracts:
      - BatchData compatible: Can be invoked directly as `model(batch)`.
      - Padding Mask Convention: True indicates a padded/ignored position (consistent with PyTorch key_padding_mask).
      - Causal Mask: Upper-triangular boolean mask where True indicates positions that cannot be attended to.
    """

    def __init__(self, config: ModelConfig) -> None:
        super().__init__()
        self.config = config

        # 1. Embeddings
        self.src_embedding = nn.Embedding(
            num_embeddings=config.src_vocab_size,
            embedding_dim=config.d_model,
            padding_idx=config.src_pad_id,
        )
        self.tgt_embedding = nn.Embedding(
            num_embeddings=config.tgt_vocab_size,
            embedding_dim=config.d_model,
            padding_idx=config.tgt_pad_id,
        )

        # Scale factor for embeddings (Vaswani et al.)
        self.scale = math.sqrt(config.d_model)

        # 2. Positional Encodings
        self.src_pos_encoder = PositionalEncoding(
            d_model=config.d_model,
            dropout=config.dropout,
            max_len=config.max_src_length,
        )
        self.tgt_pos_encoder = PositionalEncoding(
            d_model=config.d_model,
            dropout=config.dropout,
            max_len=config.max_tgt_length,
        )

        # 3. Transformer Encoder
        encoder_layer = nn.TransformerEncoderLayer(
            d_model=config.d_model,
            nhead=config.nhead,
            dim_feedforward=config.dim_feedforward,
            dropout=config.dropout,
            activation=config.activation,
            batch_first=True,
            norm_first=config.norm_first,
        )
        encoder_norm = nn.LayerNorm(config.d_model)
        self.encoder = nn.TransformerEncoder(
            encoder_layer=encoder_layer,
            num_layers=config.num_encoder_layers,
            norm=encoder_norm,
        )

        # 4. Transformer Decoder
        decoder_layer = nn.TransformerDecoderLayer(
            d_model=config.d_model,
            nhead=config.nhead,
            dim_feedforward=config.dim_feedforward,
            dropout=config.dropout,
            activation=config.activation,
            batch_first=True,
            norm_first=config.norm_first,
        )
        decoder_norm = nn.LayerNorm(config.d_model)
        self.decoder = nn.TransformerDecoder(
            decoder_layer=decoder_layer,
            num_layers=config.num_decoder_layers,
            norm=decoder_norm,
        )

        # 5. Output Projection
        self.output_projection = nn.Linear(config.d_model, config.tgt_vocab_size)

        # Initialize weights with standard Xavier normal
        self._init_weights()

    def _init_weights(self) -> None:
        """Initializes model parameters following standard Transformer practices."""
        for p in self.parameters():
            if p.dim() > 1:
                nn.init.xavier_uniform_(p)
        # Initialize projection bias to zero
        if self.output_projection.bias is not None:
            nn.init.zeros_(self.output_projection.bias)

    @staticmethod
    def generate_square_subsequent_mask(sz: int, device: Optional[torch.device] = None) -> Tensor:
        """
        Generates an upper-triangular boolean causal mask of shape (sz, sz).
        Positions with True are masked out (cannot be attended to).
        """
        return torch.triu(torch.ones(sz, sz, dtype=torch.bool, device=device), diagonal=1)

    def encode(
        self,
        src_ids: Tensor,
        src_padding_mask: Optional[Tensor] = None,
    ) -> Tensor:
        """
        Encodes Java source token IDs into the continuous Execution Semantic Space (ESS).

        Args:
          src_ids: LongTensor of shape (B, S).
          src_padding_mask: BoolTensor of shape (B, S) where True indicates padding.

        Returns:
          memory: FloatTensor of shape (B, S, d_model) representing ESS representations.
        """
        # Validate sequence bounds
        if src_ids.size(1) > self.config.max_src_length:
            raise ValueError(
                f"Source sequence length {src_ids.size(1)} exceeds configured max_src_length {self.config.max_src_length}"
            )

        # Embed and add positional encoding
        src_emb = self.src_embedding(src_ids) * self.scale
        src_emb = self.src_pos_encoder(src_emb)

        # Forward pass through encoder stack
        memory = self.encoder(src_emb, src_key_padding_mask=src_padding_mask)
        return memory

    def decode(
        self,
        decoder_input_ids: Tensor,
        memory: Tensor,
        target_padding_mask: Optional[Tensor] = None,
        src_padding_mask: Optional[Tensor] = None,
        causal_mask: Optional[Tensor] = None,
    ) -> Tensor:
        """
        Decodes target sequence conditioning on encoder memory (ESS).

        Args:
          decoder_input_ids: LongTensor of shape (B, T).
          memory: FloatTensor of shape (B, S, d_model) from encoder.
          target_padding_mask: BoolTensor of shape (B, T) where True indicates padding.
          src_padding_mask: BoolTensor of shape (B, S) where True indicates padding in memory.
          causal_mask: Optional custom (T, T) boolean causal mask. If None, generated automatically.

        Returns:
          hidden_states: FloatTensor of shape (B, T, d_model).
        """
        tgt_len = decoder_input_ids.size(1)
        if tgt_len > self.config.max_tgt_length:
            raise ValueError(
                f"Target sequence length {tgt_len} exceeds configured max_tgt_length {self.config.max_tgt_length}"
            )

        # Embed and add positional encoding
        tgt_emb = self.tgt_embedding(decoder_input_ids) * self.scale
        tgt_emb = self.tgt_pos_encoder(tgt_emb)

        # Generate causal mask if not provided
        if causal_mask is None:
            causal_mask = self.generate_square_subsequent_mask(tgt_len, device=decoder_input_ids.device)

        # Forward pass through decoder stack
        out = self.decoder(
            tgt=tgt_emb,
            memory=memory,
            tgt_mask=causal_mask,
            tgt_key_padding_mask=target_padding_mask,
            memory_key_padding_mask=src_padding_mask,
        )
        return out

    def forward(
        self,
        src_or_batch: Union[Tensor, BatchData],
        decoder_input_ids: Optional[Tensor] = None,
        src_padding_mask: Optional[Tensor] = None,
        target_padding_mask: Optional[Tensor] = None,
    ) -> Tensor:
        """
        Full forward pass mapping source code and teacher-forcing target IDs to vocabulary logits.

        Accepts either:
          1. A BatchData instance: model(batch)
          2. Explicit keyword/positional tensors: model(src_ids, decoder_input_ids, src_padding_mask, target_padding_mask)

        Returns:
          logits: FloatTensor of shape (B, T, tgt_vocab_size).
        """
        if isinstance(src_or_batch, BatchData):
            src_ids = src_or_batch.src_ids
            dec_in_ids = src_or_batch.decoder_input_ids
            s_mask = src_or_batch.src_padding_mask
            t_mask = src_or_batch.target_padding_mask
        elif isinstance(src_or_batch, Tensor):
            src_ids = src_or_batch
            if decoder_input_ids is None:
                raise ValueError("decoder_input_ids must be provided when calling forward with raw Tensors")
            dec_in_ids = decoder_input_ids
            s_mask = src_padding_mask
            t_mask = target_padding_mask
        else:
            raise TypeError(f"Expected BatchData or torch.Tensor, got {type(src_or_batch)}")

        # 1. Encode into Execution Semantic Space (ESS)
        memory = self.encode(src_ids, src_padding_mask=s_mask)

        # 2. Decode conditioned on ESS
        hidden_states = self.decode(
            decoder_input_ids=dec_in_ids,
            memory=memory,
            target_padding_mask=t_mask,
            src_padding_mask=s_mask,
        )

        # 3. Project to target vocabulary logits
        logits = self.output_projection(hidden_states)
        return logits

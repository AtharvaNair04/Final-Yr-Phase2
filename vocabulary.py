"""
Module 4, Part 5: Vocabulary Construction and Special Token Design.

Implements deterministic source and target vocabulary construction,
special and structural token registration, token-to-ID / ID-to-token mappings,
encoding, decoding, serialization, reload validation, and OOV evaluation.
"""

from collections import Counter
import hashlib
import json
import os
from pathlib import Path
from typing import Any, Dict, Iterable, List, Optional, Sequence, Tuple, Union

from source_tokenizer import tokenize_java_code
from tokenize_and_analyze import tokenize_runtime


# ============================================================
# SPECIAL AND STRUCTURAL TOKEN CONSTANTS & RESERVED IDS
# ============================================================

# Shared special tokens (IDs 0 to 3)
PAD_TOKEN = "<PAD>"
UNK_TOKEN = "<UNK>"
BOS_TOKEN = "<BOS>"
EOS_TOKEN = "<EOS>"

PAD_ID = 0
UNK_ID = 1
BOS_ID = 2
EOS_ID = 3

SHARED_SPECIAL_TOKENS: List[str] = [PAD_TOKEN, UNK_TOKEN, BOS_TOKEN, EOS_TOKEN]

# Target-only structural and grammar tokens (IDs 4 to 11)
EVENT_START_TOKEN = "<EVENT_START>"
EVENT_END_TOKEN = "<EVENT_END>"
ATTR_START_TOKEN = "<ATTR_START>"
ATTR_END_TOKEN = "<ATTR_END>"
EMPTY_VAL_TOKEN = "<EMPTY_VAL>"
TYPE_TOKEN = "TYPE"
STEP_TOKEN = "STEP"
ESCAPE_TOKEN = "\\"

EVENT_START_ID = 4
EVENT_END_ID = 5
ATTR_START_ID = 6
ATTR_END_ID = 7
EMPTY_VAL_ID = 8
TYPE_ID = 9
STEP_ID = 10
ESCAPE_ID = 11

TARGET_STRUCTURAL_TOKENS: List[str] = [
    EVENT_START_TOKEN,
    EVENT_END_TOKEN,
    ATTR_START_TOKEN,
    ATTR_END_TOKEN,
    EMPTY_VAL_TOKEN,
    TYPE_TOKEN,
    STEP_TOKEN,
    ESCAPE_TOKEN,
]

DEFAULT_TARGET_MAX_SIZE = 50000


class VocabularyCorruptedError(ValueError):
    """Raised when serialized vocabulary data is invalid, incomplete, or corrupted."""
    pass


class Vocabulary:
    """
    Bidirectional mapping between string tokens and integer IDs.

    Guarantees:
      1. Reserved IDs are immutable and strictly validated.
      2. Encoding maps out-of-vocabulary tokens to <UNK> (ID 1).
      3. In-vocabulary sequences satisfy decode(encode(tokens)) == tokens.
      4. Decoding invalid IDs raises ValueError.
      5. Serialization and reloading are bitwise deterministic and validate integrity.
    """

    def __init__(
        self,
        token_to_id: Dict[str, int],
        id_to_token: List[str],
        vocab_type: str = "generic",
        metadata: Optional[Dict[str, Any]] = None,
    ) -> None:
        self._token_to_id: Dict[str, int] = dict(token_to_id)
        self._id_to_token: List[str] = list(id_to_token)
        self._vocab_type: str = str(vocab_type)
        self._metadata: Dict[str, Any] = dict(metadata or {})
        self._validate_internal_consistency()

    def _validate_internal_consistency(self) -> None:
        """Validates that token_to_id and id_to_token are bijective and match reserved tokens."""
        if len(self._token_to_id) != len(self._id_to_token):
            raise VocabularyCorruptedError(
                f"token_to_id size ({len(self._token_to_id)}) != id_to_token size ({len(self._id_to_token)})"
            )

        # Check bidirectional alignment
        for idx, token in enumerate(self._id_to_token):
            if token not in self._token_to_id:
                raise VocabularyCorruptedError(f"Token '{token}' at index {idx} missing from token_to_id")
            if self._token_to_id[token] != idx:
                raise VocabularyCorruptedError(
                    f"Mismatched ID for '{token}': id_to_token has {idx}, token_to_id has {self._token_to_id[token]}"
                )

        # Validate shared special tokens
        for expected_id, expected_token in enumerate(SHARED_SPECIAL_TOKENS):
            if len(self._id_to_token) <= expected_id or self._id_to_token[expected_id] != expected_token:
                raise VocabularyCorruptedError(
                    f"Expected special token '{expected_token}' at ID {expected_id}, "
                    f"found '{self._id_to_token[expected_id] if expected_id < len(self._id_to_token) else 'EOF'}'"
                )

        # Validate target structural tokens if target vocabulary
        if self._vocab_type == "target":
            for offset, expected_token in enumerate(TARGET_STRUCTURAL_TOKENS):
                expected_id = 4 + offset
                if len(self._id_to_token) <= expected_id or self._id_to_token[expected_id] != expected_token:
                    raise VocabularyCorruptedError(
                        f"Expected target structural token '{expected_token}' at ID {expected_id}, "
                        f"found '{self._id_to_token[expected_id] if expected_id < len(self._id_to_token) else 'EOF'}'"
                    )

    # --------------------------------------------------------
    # Properties
    # --------------------------------------------------------

    @property
    def vocab_type(self) -> str:
        return self._vocab_type

    @property
    def metadata(self) -> Dict[str, Any]:
        return dict(self._metadata)

    @property
    def pad_id(self) -> int:
        return PAD_ID

    @property
    def unk_id(self) -> int:
        return UNK_ID

    @property
    def bos_id(self) -> int:
        return BOS_ID

    @property
    def eos_id(self) -> int:
        return EOS_ID

    # Target structural IDs
    @property
    def event_start_id(self) -> int:
        return EVENT_START_ID

    @property
    def event_end_id(self) -> int:
        return EVENT_END_ID

    @property
    def attr_start_id(self) -> int:
        return ATTR_START_ID

    @property
    def attr_end_id(self) -> int:
        return ATTR_END_ID

    @property
    def empty_val_id(self) -> int:
        return EMPTY_VAL_ID

    @property
    def type_id(self) -> int:
        return TYPE_ID

    @property
    def step_id(self) -> int:
        return STEP_ID

    @property
    def escape_id(self) -> int:
        return ESCAPE_ID

    @property
    def token_to_id(self) -> Dict[str, int]:
        """Returns a copy of the token-to-ID mapping dictionary."""
        return dict(self._token_to_id)

    @property
    def id_to_token(self) -> List[str]:
        """Returns a copy of the ID-to-token list."""
        return list(self._id_to_token)

    # --------------------------------------------------------
    # Core API
    # --------------------------------------------------------

    def __len__(self) -> int:
        return len(self._id_to_token)

    def __contains__(self, token: str) -> bool:
        return token in self._token_to_id

    def get_id(self, token: str, default: Optional[int] = None) -> int:
        """
        Returns the integer ID for a token.
        If the token is unknown, returns default if provided, else unk_id (1).
        """
        if default is None:
            default = self.unk_id
        return self._token_to_id.get(token, default)

    def get_token(self, idx: int) -> str:
        """
        Returns the token for a given integer ID.
        Raises ValueError if idx is out of valid range [0, len(vocab) - 1].
        """
        if not isinstance(idx, int) or idx < 0 or idx >= len(self._id_to_token):
            raise ValueError(f"Invalid token ID {idx}; vocabulary range is [0, {len(self._id_to_token) - 1}]")
        return self._id_to_token[idx]

    def encode(
        self,
        tokens: Sequence[str],
        add_bos: bool = False,
        add_eos: bool = False,
    ) -> List[int]:
        """
        Encodes a sequence of string tokens into integer IDs.

        Parameters:
            tokens: Sequence of string tokens.
            add_bos: If True, prepends the <BOS> token ID (2).
            add_eos: If True, appends the <EOS> token ID (3).

        Returns:
            List of integer token IDs. Unknown tokens map to unk_id (1).
        """
        encoded: List[int] = []
        if add_bos:
            encoded.append(self.bos_id)

        unk = self.unk_id
        t2i = self._token_to_id
        for tok in tokens:
            encoded.append(t2i.get(tok, unk))

        if add_eos:
            encoded.append(self.eos_id)

        return encoded

    def decode(
        self,
        ids: Sequence[int],
        skip_special: bool = False,
    ) -> List[str]:
        """
        Decodes a sequence of integer IDs back into string tokens.

        Parameters:
            ids: Sequence of integer token IDs.
            skip_special: If True, strips <PAD>, <BOS>, and <EOS> tokens.
                          Note: <UNK> is preserved as an explicit token string.

        Returns:
            List of string tokens.

        Raises:
            ValueError: If any ID is out of the valid range [0, len(vocab) - 1].
        """
        special_to_skip = {PAD_TOKEN, BOS_TOKEN, EOS_TOKEN} if skip_special else set()
        decoded: List[str] = []
        n = len(self._id_to_token)
        i2t = self._id_to_token

        for idx in ids:
            if not isinstance(idx, int) or idx < 0 or idx >= n:
                raise ValueError(f"Invalid token ID {idx}; vocabulary range is [0, {n - 1}]")
            tok = i2t[idx]
            if skip_special and tok in special_to_skip:
                continue
            decoded.append(tok)

        return decoded

    # --------------------------------------------------------
    # Serialization and Reload Validation
    # --------------------------------------------------------

    def to_dict(self) -> Dict[str, Any]:
        """
        Serializes vocabulary structure into a deterministic dictionary.
        Does not include non-deterministic runtime timestamps.
        """
        return {
            "vocab_type": self._vocab_type,
            "vocab_size": len(self._id_to_token),
            "special_tokens": {
                PAD_TOKEN: PAD_ID,
                UNK_TOKEN: UNK_ID,
                BOS_TOKEN: BOS_ID,
                EOS_TOKEN: EOS_ID,
            },
            "structural_tokens": (
                {t: 4 + i for i, t in enumerate(TARGET_STRUCTURAL_TOKENS)}
                if self._vocab_type == "target"
                else {}
            ),
            "id_to_token": self._id_to_token,
            "metadata": self._metadata,
        }

    def save(self, filepath: Union[str, Path]) -> None:
        """
        Saves the vocabulary to a JSON file with deterministic formatting and key ordering.
        """
        path = Path(filepath)
        path.parent.mkdir(parents=True, exist_ok=True)
        data = self.to_dict()
        with open(path, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2, ensure_ascii=False)

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "Vocabulary":
        """
        Constructs and validates a Vocabulary from a serialized dictionary.

        Raises:
            VocabularyCorruptedError: On schema errors or invalid token mappings.
        """
        if not isinstance(data, dict):
            raise VocabularyCorruptedError("Serialized vocabulary must be a JSON dictionary")

        for req in ("vocab_type", "vocab_size", "id_to_token"):
            if req not in data:
                raise VocabularyCorruptedError(f"Missing required vocabulary field '{req}'")

        id_to_token = data["id_to_token"]
        if not isinstance(id_to_token, list):
            raise VocabularyCorruptedError("'id_to_token' must be a list of strings")

        if len(id_to_token) != data["vocab_size"]:
            raise VocabularyCorruptedError(
                f"Declared vocab_size ({data['vocab_size']}) != actual list size ({len(id_to_token)})"
            )

        token_to_id: Dict[str, int] = {}
        for idx, token in enumerate(id_to_token):
            if not isinstance(token, str):
                raise VocabularyCorruptedError(f"Token at index {idx} is not a string: {type(token)}")
            if token in token_to_id:
                raise VocabularyCorruptedError(
                    f"Duplicate token '{token}' at index {idx}; previously seen at {token_to_id[token]}"
                )
            token_to_id[token] = idx

        vocab_type = str(data["vocab_type"])
        metadata = data.get("metadata", {})
        if not isinstance(metadata, dict):
            metadata = {}

        return cls(
            token_to_id=token_to_id,
            id_to_token=id_to_token,
            vocab_type=vocab_type,
            metadata=metadata,
        )

    @classmethod
    def load(cls, filepath: Union[str, Path]) -> "Vocabulary":
        """
        Loads and validates a Vocabulary from a JSON file.
        """
        path = Path(filepath)
        if not path.is_file():
            raise FileNotFoundError(f"Vocabulary file not found: {path}")

        with open(path, "r", encoding="utf-8") as f:
            try:
                data = json.load(f)
            except json.JSONDecodeError as e:
                raise VocabularyCorruptedError(f"Corrupted JSON in {path}: {e}") from e

        return cls.from_dict(data)


# ============================================================
# VOCABULARY BUILDERS (TRAINING SPLIT ONLY)
# ============================================================

def _compute_file_sha256(filepath: Union[str, Path]) -> str:
    """Computes SHA-256 hex digest of a file for deterministic fingerprinting."""
    h = hashlib.sha256()
    with open(filepath, "rb") as f:
        while chunk := f.read(65536):
            h.update(chunk)
    return h.hexdigest()


def build_source_vocabulary(
    train_jsonl_path: Union[str, Path],
    java_dir: Union[str, Path],
    max_size: Optional[int] = None,
    min_freq: int = 1,
) -> Tuple[Vocabulary, Dict[str, Any]]:
    """
    Builds the source vocabulary strictly from training Java source files.

    Pipeline:
      1. Reads program_id entries from train_jsonl_path.
      2. For each training program_id, reads selected source code/{program_id}.java.
      3. Tokenizes code with tokenize_java_code(code, preserve_comments=False).
      4. Aggregates token frequencies across the training split.
      5. Registers special tokens (<PAD>=0, <UNK>=1, <BOS>=2, <EOS>=3).
      6. Ranks candidate tokens by descending frequency, breaking ties alphabetically.
      7. Applies min_freq and optional max_size constraints.
      8. Returns the fitted Vocabulary and execution statistics.

    Guarantees:
      - Validation and test splits are NEVER accessed.
      - Never substitutes SPR dictionary for raw Java code.
      - 100% deterministic given identical inputs.
    """
    train_path = Path(train_jsonl_path)
    j_dir = Path(java_dir)

    if not train_path.is_file():
        raise FileNotFoundError(f"Training split file not found: {train_path}")
    if not j_dir.is_dir():
        raise FileNotFoundError(f"Java source directory not found: {j_dir}")

    counter: Counter = Counter()
    total_token_occurrences = 0
    records_processed = 0
    missing_sources: List[str] = []

    # Read train.jsonl
    with open(train_path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            rec = json.loads(line)
            pid = rec["program_id"]
            records_processed += 1

            java_path = j_dir / f"{pid}.java"
            if not java_path.is_file():
                missing_sources.append(pid)
                continue

            with open(java_path, "r", encoding="utf-8", errors="ignore") as jf:
                code = jf.read()

            tokens = tokenize_java_code(code, preserve_comments=False)
            total_token_occurrences += len(tokens)
            counter.update(tokens)

    if missing_sources:
        raise FileNotFoundError(f"Missing Java source files for training programs: {missing_sources[:5]}")

    unique_tokens_observed = len(counter)

    # Filter out any token that collides with reserved special tokens
    reserved_set = set(SHARED_SPECIAL_TOKENS)
    candidate_tokens = [tok for tok, count in counter.items() if tok not in reserved_set and count >= min_freq]

    # Deterministic ranking: descending frequency, then alphabetical ascending
    candidate_tokens.sort(key=lambda tok: (-counter[tok], tok))

    if max_size is not None:
        capacity = max(len(SHARED_SPECIAL_TOKENS), max_size) - len(SHARED_SPECIAL_TOKENS)
        candidate_tokens = candidate_tokens[:capacity]

    # Assemble id_to_token list
    id_to_token: List[str] = list(SHARED_SPECIAL_TOKENS) + candidate_tokens
    token_to_id: Dict[str, int] = {tok: idx for idx, tok in enumerate(id_to_token)}

    train_fingerprint = _compute_file_sha256(train_path)

    stats: Dict[str, Any] = {
        "records_processed": records_processed,
        "total_token_occurrences": total_token_occurrences,
        "unique_tokens_observed": unique_tokens_observed,
        "vocabulary_size": len(id_to_token),
        "min_freq": min_freq,
        "max_size": max_size,
        "train_fingerprint": train_fingerprint,
    }

    metadata: Dict[str, Any] = {
        "description": "Source Java Lexical Vocabulary (Module 4, Part 5)",
        "train_records": records_processed,
        "train_fingerprint": train_fingerprint,
        "total_tokens_trained": total_token_occurrences,
        "unique_tokens_observed": unique_tokens_observed,
        "min_freq": min_freq,
        "max_size": max_size,
        "ordering_policy": "descending_frequency_then_alphabetical",
    }

    vocab = Vocabulary(
        token_to_id=token_to_id,
        id_to_token=id_to_token,
        vocab_type="source",
        metadata=metadata,
    )
    return vocab, stats


def build_target_vocabulary(
    train_jsonl_path: Union[str, Path],
    max_size: int = DEFAULT_TARGET_MAX_SIZE,
    min_freq: int = 1,
) -> Tuple[Vocabulary, Dict[str, Any]]:
    """
    Builds the target runtime vocabulary strictly from training trace events.

    Pipeline:
      1. Reads runtime_trace dictionaries from train_jsonl_path only.
      2. Tokenizes traces with tokenize_runtime(rec["runtime_trace"]).
      3. Aggregates token frequencies across the training split.
      4. Registers shared special tokens:
         - <PAD>=0, <UNK>=1, <BOS>=2, <EOS>=3
      5. Registers target structural and escape tokens:
         - <EVENT_START>=4, <EVENT_END>=5, <ATTR_START>=6, <ATTR_END>=7,
           <EMPTY_VAL>=8, TYPE=9, STEP=10, \\=11
      6. Ranks candidate observed tokens by descending frequency, breaking ties alphabetically.
      7. Fills remaining capacity up to max_size (default: 50,000).
      8. Returns the fitted Vocabulary and execution statistics.

    Guarantees:
      - Validation and test splits are NEVER accessed.
      - Runtime serialization contract and backslash escaping are preserved.
      - Exactly conforms to the 50,000 token maximum vocabulary cap.
      - 100% deterministic given identical inputs.
    """
    train_path = Path(train_jsonl_path)
    if not train_path.is_file():
        raise FileNotFoundError(f"Training split file not found: {train_path}")

    counter: Counter = Counter()
    total_token_occurrences = 0
    records_processed = 0

    with open(train_path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            rec = json.loads(line)
            records_processed += 1
            rt = rec.get("runtime_trace", {})
            tokens = tokenize_runtime(rt)
            total_token_occurrences += len(tokens)
            counter.update(tokens)

    unique_tokens_observed = len(counter)

    # Reserved tokens list (IDs 0 to 11)
    reserved_tokens = list(SHARED_SPECIAL_TOKENS) + list(TARGET_STRUCTURAL_TOKENS)
    reserved_set = set(reserved_tokens)

    # Filter candidate tokens: must not be already reserved, must satisfy min_freq
    candidate_tokens = [tok for tok, count in counter.items() if tok not in reserved_set and count >= min_freq]

    # Deterministic ranking: descending frequency, then alphabetical ascending
    candidate_tokens.sort(key=lambda tok: (-counter[tok], tok))

    # Apply capacity cap
    capacity = max(len(reserved_tokens), max_size) - len(reserved_tokens)
    eligible_tokens = candidate_tokens[:capacity]
    excluded_by_cap = len(candidate_tokens) - len(eligible_tokens)

    # Assemble id_to_token
    id_to_token: List[str] = list(reserved_tokens) + eligible_tokens
    token_to_id: Dict[str, int] = {tok: idx for idx, tok in enumerate(id_to_token)}

    train_fingerprint = _compute_file_sha256(train_path)

    stats: Dict[str, Any] = {
        "records_processed": records_processed,
        "total_token_occurrences": total_token_occurrences,
        "unique_tokens_observed": unique_tokens_observed,
        "vocabulary_size": len(id_to_token),
        "reserved_tokens_count": len(reserved_tokens),
        "eligible_tokens_added": len(eligible_tokens),
        "excluded_by_cap": excluded_by_cap,
        "max_size": max_size,
        "min_freq": min_freq,
        "train_fingerprint": train_fingerprint,
    }

    metadata: Dict[str, Any] = {
        "description": "Target Runtime Trace Vocabulary (Module 4, Part 5)",
        "train_records": records_processed,
        "train_fingerprint": train_fingerprint,
        "total_tokens_trained": total_token_occurrences,
        "unique_tokens_observed": unique_tokens_observed,
        "max_size_cap": max_size,
        "reserved_count": len(reserved_tokens),
        "excluded_by_cap": excluded_by_cap,
        "ordering_policy": "descending_frequency_then_alphabetical",
    }

    vocab = Vocabulary(
        token_to_id=token_to_id,
        id_to_token=id_to_token,
        vocab_type="target",
        metadata=metadata,
    )
    return vocab, stats


# ============================================================
# POST-FIT OOV DIAGNOSTICS (EVALUATION ONLY)
# ============================================================

def calculate_oov_diagnostics(
    vocab: Vocabulary,
    token_sequences: Iterable[List[str]],
) -> Dict[str, Any]:
    """
    Computes out-of-vocabulary (OOV) diagnostics over a collection of token sequences.

    This function is strictly read-only and diagnostic: it evaluates coverage
    without modifying the vocabulary.

    Returns:
      total_tokens: Total token occurrences evaluated.
      known_tokens: Occurrences recognized in vocabulary.
      unknown_tokens: Occurrences mapped to <UNK>.
      oov_rate: unknown_tokens / total_tokens (or 0.0 if total_tokens == 0).
      distinct_unseen_tokens: Number of unique token strings not in vocabulary.
      total_sequences: Total number of evaluated sequences.
      sequences_with_oov: Count of sequences containing at least one unknown token.
      sequence_oov_rate: sequences_with_oov / total_sequences.
    """
    total_tokens = 0
    known_tokens = 0
    unknown_tokens = 0
    unseen_set = set()
    total_sequences = 0
    sequences_with_oov = 0

    t2i = vocab._token_to_id

    for seq in token_sequences:
        total_sequences += 1
        seq_has_oov = False
        for tok in seq:
            total_tokens += 1
            if tok in t2i:
                known_tokens += 1
            else:
                unknown_tokens += 1
                unseen_set.add(tok)
                seq_has_oov = True

        if seq_has_oov:
            sequences_with_oov += 1

    oov_rate = (unknown_tokens / total_tokens) if total_tokens > 0 else 0.0
    seq_oov_rate = (sequences_with_oov / total_sequences) if total_sequences > 0 else 0.0

    return {
        "total_tokens": total_tokens,
        "known_tokens": known_tokens,
        "unknown_tokens": unknown_tokens,
        "oov_rate": round(oov_rate, 6),
        "distinct_unseen_tokens": len(unseen_set),
        "total_sequences": total_sequences,
        "sequences_with_oov": sequences_with_oov,
        "sequence_oov_rate": round(seq_oov_rate, 6),
    }

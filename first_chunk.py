"""
Design A helpers: first-chunk subsets, source swapping, a token-overlap retrieval baseline and the small
statistics used to report uncertainty. No model, vocabulary, cache or training code is modified.
"""

import math
import random
from typing import Dict, List, Optional, Sequence, Tuple

from torch.utils.data import Dataset

from chunk_cache import CachedExecutionTraceDataset
from execution_dataset import DatasetItem


# ============================================================
# FIRST-CHUNK SUBSET
# ============================================================

def first_chunk_indices(dataset: CachedExecutionTraceDataset) -> List[int]:
    """Dataset indices whose chunk_index is 0, in program order (one per program with >= 1 chunk)."""
    meta = dataset._chunk_meta
    return [i for i in range(len(dataset)) if int(meta[i, 1]) == 0]


class FirstChunkDataset(Dataset):
    """
    View of a cached dataset restricted to chunk 0 of every program. Items are returned unchanged
    from the underlying dataset. Exposes the attributes create_dataloader() needs.
    """

    def __init__(self, base: CachedExecutionTraceDataset) -> None:
        self.base = base
        self.indices = first_chunk_indices(base)
        self.source_vocab = base.source_vocab
        self.target_vocab = base.target_vocab
        self.program_ids = [base.program_ids[int(base._chunk_meta[i, 0])] for i in self.indices]
        if len(set(self.program_ids)) != len(self.program_ids):
            raise ValueError("First-chunk subset contains a program twice.")

    def __len__(self) -> int:
        return len(self.indices)

    def __getitem__(self, idx: int) -> DatasetItem:
        item = self.base[self.indices[idx]]
        if item.chunk_index != 0:
            raise AssertionError("FirstChunkDataset returned a non-first chunk")
        return item


def assert_disjoint(*program_id_lists: Sequence[str]) -> None:
    """Raises if any program appears in more than one split."""
    seen: Dict[str, int] = {}
    for split, pids in enumerate(program_id_lists):
        for pid in pids:
            if pid in seen and seen[pid] != split:
                raise ValueError(f"Program {pid} appears in splits {seen[pid]} and {split}")
            seen[pid] = split


# ============================================================
# SOURCE SWAPPING
# ============================================================

def mismatch_assignment(program_ids: Sequence[str]) -> Dict[str, str]:
    """
    Deterministic derangement: programs sorted by id, each receives the source of the next program
    in that order (cyclic). Every program gets a different program's source and every source is used
    exactly once. Requires at least two programs.
    """
    order = sorted(set(program_ids))
    if len(order) < 2:
        raise ValueError("Need at least two programs to build a mismatched assignment")
    return {pid: order[(i + 1) % len(order)] for i, pid in enumerate(order)}


# ============================================================
# RETRIEVAL BASELINE
# ============================================================

def jaccard(a: Sequence[int], b: Sequence[int]) -> float:
    sa, sb = set(a), set(b)
    union = sa | sb
    return len(sa & sb) / len(union) if union else 0.0


def retrieve_nearest(query_source: Sequence[int], train_sources: Dict[str, Sequence[int]]) -> Tuple[str, float]:
    """
    Training program whose source token-ID set has the highest Jaccard similarity with the query.
    Ties are broken by the smallest program id, so the result is deterministic.
    """
    best_pid, best_sim = None, -1.0
    for pid in sorted(train_sources):
        sim = jaccard(query_source, train_sources[pid])
        if sim > best_sim:
            best_pid, best_sim = pid, sim
    return best_pid, best_sim


# ============================================================
# STATISTICS
# ============================================================

def binomial_two_sided_p(k: int, n: int, p: float = 0.5) -> float:
    """Exact two-sided binomial test: sum of probabilities of outcomes no more likely than k."""
    if n == 0:
        return 1.0
    probs = [math.comb(n, i) * p ** i * (1 - p) ** (n - i) for i in range(n + 1)]
    observed = probs[k]
    return min(1.0, sum(q for q in probs if q <= observed * (1 + 1e-12)))


def wilson_interval(k: int, n: int, z: float = 1.959963984540054) -> Tuple[Optional[float], Optional[float]]:
    """Wilson score 95% interval for a proportion k/n."""
    if n == 0:
        return None, None
    phat = k / n
    denom = 1 + z * z / n
    centre = (phat + z * z / (2 * n)) / denom
    half = z * math.sqrt(phat * (1 - phat) / n + z * z / (4 * n * n)) / denom
    return centre - half, centre + half


def sign_test(differences: Sequence[float]) -> Dict[str, float]:
    """Paired sign test on differences (ties dropped). Reports wins, losses, ties and the exact p-value."""
    wins = sum(d > 0 for d in differences)
    losses = sum(d < 0 for d in differences)
    ties = len(differences) - wins - losses
    return {"wins": wins, "losses": losses, "ties": ties, "p_value": binomial_two_sided_p(wins, wins + losses)}


def bootstrap_mean_ci(values: Sequence[float], n_resamples: int = 10000, seed: int = 0) -> Tuple[float, float, float]:
    """Mean and percentile bootstrap 95% interval (fixed seed for reproducibility)."""
    vals = list(values)
    if not vals:
        return float("nan"), float("nan"), float("nan")
    rng = random.Random(seed)
    means = sorted(sum(rng.choice(vals) for _ in vals) / len(vals) for _ in range(n_resamples))
    return sum(vals) / len(vals), means[int(0.025 * n_resamples)], means[int(0.975 * n_resamples) - 1]

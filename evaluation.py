"""
Evaluation module for the Execution Semantic Transformer (EST).

Standalone: imports the existing model / vocabulary / chunking code but does not modify it.

Contents:
  1. Trace grammar parsing (event-level) on top of chunk_trace.parse_events_from_tokens.
  2. Pure metric functions (edit similarity, structural validity, event-level metrics).
  3. Teacher-forced per-chunk statistics.
  4. Batched greedy autoregressive generation.
  5. Checkpoint / vocabulary compatibility checks.

Serialization grammar (tokenize_and_analyze.tokenize_runtime):
  <EVENT_START> TYPE <type> [STEP <n>] (<ATTR_START> <key> (<value tokens>+ | <EMPTY_VAL>) <ATTR_END>)* <EVENT_END>
  A reserved token inside a key/value is preceded by the escape token '\\'.
"""

from collections import Counter
from dataclasses import dataclass, field
import math
from typing import Any, Dict, List, Optional, Sequence, Tuple

from chunk_trace import parse_events_from_tokens

EVENT_START = "<EVENT_START>"
EVENT_END = "<EVENT_END>"
ATTR_START = "<ATTR_START>"
ATTR_END = "<ATTR_END>"
EMPTY_VAL = "<EMPTY_VAL>"
TYPE = "TYPE"
STEP = "STEP"
ESCAPE = "\\"
STRUCTURAL_TOKENS = {EVENT_START, EVENT_END, ATTR_START, ATTR_END, EMPTY_VAL, TYPE, STEP, ESCAPE}


# ============================================================
# 1. EVENT-LEVEL PARSING
# ============================================================

@dataclass
class ParsedEvent:
    """One event read against the serialization grammar."""
    type: Optional[str] = None
    step: Optional[str] = None
    attributes: List[Tuple[str, Tuple[str, ...]]] = field(default_factory=list)
    well_formed: bool = True
    errors: List[str] = field(default_factory=list)
    tokens: Tuple[str, ...] = ()

    def tokens_without_step(self) -> Tuple[str, ...]:
        """Event tokens with the STEP marker and its value removed (for step-insensitive comparison)."""
        out: List[str] = []
        i = 0
        while i < len(self.tokens):
            tok = self.tokens[i]
            if tok == ESCAPE and i + 1 < len(self.tokens):
                out.extend(self.tokens[i:i + 2])
                i += 2
                continue
            if tok == STEP:
                i += 2
                continue
            out.append(tok)
            i += 1
        return tuple(out)


def parse_event(event_tokens: Sequence[str]) -> ParsedEvent:
    """
    Parses one event token list (starting <EVENT_START>, ending <EVENT_END>) into type, step
    and attributes. Malformed events are flagged (well_formed=False) rather than repaired.
    """
    ev = ParsedEvent(tokens=tuple(event_tokens))
    toks = list(event_tokens)

    def fail(msg: str) -> ParsedEvent:
        ev.well_formed = False
        ev.errors.append(msg)
        return ev

    if len(toks) < 2 or toks[0] != EVENT_START or toks[-1] != EVENT_END:
        return fail("missing event boundaries")
    body = toks[1:-1]
    i = 0
    if i + 1 < len(body) and body[i] == TYPE and body[i + 1] not in STRUCTURAL_TOKENS:
        ev.type = body[i + 1]
        i += 2
    else:
        return fail("missing TYPE <type>")
    if i < len(body) and body[i] == STEP:
        if i + 1 < len(body) and body[i + 1] not in STRUCTURAL_TOKENS:
            ev.step = body[i + 1]
            i += 2
        else:
            return fail("STEP without value")
    while i < len(body):
        if body[i] != ATTR_START:
            return fail(f"unexpected token {body[i]!r} outside attribute")
        i += 1
        # key (possibly escaped)
        if i < len(body) and body[i] == ESCAPE and i + 1 < len(body):
            key = body[i + 1]
            i += 2
        elif i < len(body) and body[i] not in STRUCTURAL_TOKENS:
            key = body[i]
            i += 1
        else:
            return fail("attribute without key")
        value: List[str] = []
        closed = False
        while i < len(body):
            tok = body[i]
            if tok == ESCAPE:
                if i + 1 >= len(body):
                    return fail("trailing escape")
                value.append(body[i + 1])
                i += 2
                continue
            if tok == ATTR_END:
                closed = True
                i += 1
                break
            if tok in (ATTR_START, TYPE, STEP):
                return fail(f"unescaped {tok} inside attribute value")
            value.append(tok)
            i += 1
        if not closed:
            return fail("unclosed attribute")
        if not value:
            return fail("attribute without value")
        if EMPTY_VAL in value and value != [EMPTY_VAL]:
            return fail("EMPTY_VAL mixed with value tokens")
        ev.attributes.append((key, tuple(value)))
    return ev


def parse_trace(tokens: Sequence[str]) -> Tuple[List[ParsedEvent], int]:
    """
    Splits a token stream into events (non-strict, so model output never raises) and parses each.
    Returns (events, number of tokens that fell outside any complete event).
    """
    raw_events, malformed = parse_events_from_tokens(list(tokens), strict=False)
    return [parse_event(e) for e in raw_events], len(malformed)


# ============================================================
# 2. METRICS
# ============================================================

def levenshtein(a: Sequence[Any], b: Sequence[Any]) -> int:
    """Token-level edit distance (insert/delete/substitute, unit cost)."""
    if len(a) < len(b):
        a, b = b, a
    prev = list(range(len(b) + 1))
    for i, x in enumerate(a, start=1):
        cur = [i] + [0] * len(b)
        for j, y in enumerate(b, start=1):
            cur[j] = min(prev[j] + 1, cur[j - 1] + 1, prev[j - 1] + (x != y))
        prev = cur
    return prev[-1]


def edit_similarity(a: Sequence[Any], b: Sequence[Any]) -> float:
    """1 - levenshtein / max(len). Two empty sequences have similarity 1."""
    n = max(len(a), len(b))
    return 1.0 if n == 0 else 1.0 - levenshtein(a, b) / n


def positional_accuracy(ref: Sequence[Any], pred: Sequence[Any]) -> float:
    """Matches at equal positions divided by max(len(ref), len(pred)); penalizes both truncation and overrun."""
    n = max(len(ref), len(pred))
    if n == 0:
        return 1.0
    return sum(r == p for r, p in zip(ref, pred)) / n


def bracket_validity(tokens: Sequence[str]) -> bool:
    """
    The structural check used in earlier experiments (scripts/run_inference_demo.validate_trace):
    marker nesting only; does not check TYPE/STEP/key/value placement.
    """
    inside_event = inside_attr = False
    events = 0
    i = 0
    while i < len(tokens):
        tok = tokens[i]
        if tok == ESCAPE:
            if i + 1 >= len(tokens):
                return False
            i += 2
            continue
        if tok == EVENT_START:
            if inside_event or inside_attr:
                return False
            inside_event = True
            events += 1
        elif tok == EVENT_END:
            if not inside_event or inside_attr:
                return False
            inside_event = False
        elif tok == ATTR_START:
            if not inside_event or inside_attr:
                return False
            inside_attr = True
        elif tok == ATTR_END:
            if not inside_attr:
                return False
            inside_attr = False
        i += 1
    return events > 0 and not inside_event and not inside_attr


def grammar_validity(tokens: Sequence[str]) -> bool:
    """Strict check: the whole stream is >= 1 event, nothing outside events, every event well-formed."""
    events, outside = parse_trace(tokens)
    return len(events) > 0 and outside == 0 and all(e.well_formed for e in events)


def event_metrics(ref_tokens: Sequence[str], pred_tokens: Sequence[str]) -> Dict[str, Any]:
    """
    Event-level comparison of a predicted and a reference token stream.

    Alignment is positional (event i vs event i). Definitions:
      type_positional_accuracy: equal types at equal positions / max(n_ref, n_pred)
      type_sequence_edit_similarity: edit_similarity over the two type sequences
      type_bag_precision/recall/f1: multiset overlap of types (order-insensitive)
      For aligned pairs i < min(n_ref, n_pred) where both events are well-formed:
        attr_key_match_rate: fraction of pairs with identical ordered key lists
        attr_value_match_rate: over reference attributes, fraction whose key appears in the
                               predicted event with identical value tokens
        step_match_rate: fraction of pairs with identical STEP value
      event_exact_rate / event_exact_nostep_rate: identical event tokens (with / without STEP)
                               at equal positions / max(n_ref, n_pred)
    """
    ref_events, _ = parse_trace(ref_tokens)
    pred_events, pred_outside = parse_trace(pred_tokens)
    n_ref, n_pred = len(ref_events), len(pred_events)
    n_max = max(n_ref, n_pred)
    ref_types = [e.type for e in ref_events]
    pred_types = [e.type for e in pred_events]

    ref_bag, pred_bag = Counter(ref_types), Counter(pred_types)
    overlap = sum((ref_bag & pred_bag).values())
    precision = overlap / n_pred if n_pred else (1.0 if n_ref == 0 else 0.0)
    recall = overlap / n_ref if n_ref else (1.0 if n_pred == 0 else 0.0)
    f1 = 0.0 if precision + recall == 0 else 2 * precision * recall / (precision + recall)

    key_hits = step_hits = aligned_pairs = 0
    value_hits = value_total = 0
    exact = exact_nostep = 0
    for r, p in zip(ref_events, pred_events):
        if r.tokens == p.tokens:
            exact += 1
        if r.tokens_without_step() == p.tokens_without_step():
            exact_nostep += 1
        if not (r.well_formed and p.well_formed):
            continue
        aligned_pairs += 1
        key_hits += [k for k, _ in r.attributes] == [k for k, _ in p.attributes]
        step_hits += r.step == p.step
        pred_attr = dict(p.attributes)
        for k, v in r.attributes:
            value_total += 1
            value_hits += pred_attr.get(k) == v

    def ratio(a: int, b: int) -> Optional[float]:
        return a / b if b else None

    return {
        "ref_event_count": n_ref,
        "pred_event_count": n_pred,
        "event_count_abs_diff": abs(n_ref - n_pred),
        "pred_malformed_events": sum(not e.well_formed for e in pred_events),
        "pred_tokens_outside_events": pred_outside,
        "type_positional_accuracy": positional_accuracy(ref_types, pred_types),
        "type_sequence_edit_similarity": edit_similarity(ref_types, pred_types),
        "type_bag_precision": precision,
        "type_bag_recall": recall,
        "type_bag_f1": f1,
        "aligned_well_formed_pairs": aligned_pairs,
        "attr_key_match_rate": ratio(key_hits, aligned_pairs),
        "attr_value_match_rate": ratio(value_hits, value_total),
        "step_match_rate": ratio(step_hits, aligned_pairs),
        "event_exact_rate": exact / n_max if n_max else 1.0,
        "event_exact_nostep_rate": exact_nostep / n_max if n_max else 1.0,
    }


def generation_metrics(
    ref_ids: Sequence[int],
    pred_ids: Sequence[int],
    ref_tokens: Sequence[str],
    pred_tokens: Sequence[str],
    eos_reached: bool,
) -> Dict[str, Any]:
    """All per-example generation metrics. ref/pred exclude BOS and EOS."""
    out = {
        "ref_length": len(ref_ids),
        "pred_length": len(pred_ids),
        "length_ratio": len(pred_ids) / len(ref_ids) if ref_ids else None,
        "eos_reached": eos_reached,
        "exact_match": list(ref_ids) == list(pred_ids),
        "token_positional_accuracy": positional_accuracy(ref_ids, pred_ids),
        "token_edit_similarity": edit_similarity(ref_ids, pred_ids),
        "bracket_valid": bracket_validity(pred_tokens),
        "grammar_valid": grammar_validity(pred_tokens),
    }
    out.update(event_metrics(ref_tokens, pred_tokens))
    return out


# ============================================================
# 3. TEACHER-FORCED STATISTICS
# ============================================================

def step_value_positions(target_ids: Sequence[int], step_id: int, escape_id: int) -> List[int]:
    """Positions t whose token is a STEP value (target[t-1] is an unescaped STEP marker)."""
    pos = []
    for t in range(1, len(target_ids)):
        if target_ids[t - 1] == step_id and not (t >= 2 and target_ids[t - 2] == escape_id):
            pos.append(t)
    return pos


def teacher_forced_counts(
    target_ids: Sequence[int],
    pred_ids: Sequence[int],
    token_nll: Sequence[float],
    eos_id: int,
    unk_id: int,
    structural_ids: set,
    step_id: int,
    escape_id: int,
) -> Dict[str, float]:
    """
    Per-chunk counts from one teacher-forced pass. target_ids = chunk_ids + [EOS] (no PAD);
    pred_ids = argmax predictions; token_nll = per-position negative log-likelihood.
    Counts are summed across chunks and turned into rates by aggregate_tf().
    """
    n = len(target_ids)
    correct = [int(p == t) for p, t in zip(pred_ids, target_ids)]
    steps = set(step_value_positions(target_ids, step_id, escape_id))
    c: Dict[str, float] = Counter()
    for t in range(n):
        tgt = target_ids[t]
        c["tokens_incl_eos"] += 1
        c["correct_incl_eos"] += correct[t]
        c["nll_sum_incl_eos"] += token_nll[t]
        if tgt != eos_id:
            c["tokens_excl_eos"] += 1
            c["correct_excl_eos"] += correct[t]
            c["nll_sum_excl_eos"] += token_nll[t]
            if tgt == unk_id:
                # UNK targets are excluded from the step/content accuracies below, because
                # predicting <UNK> for an out-of-vocabulary value is not a correct value.
                c["unk_targets"] += 1
                if t in steps:
                    c["step_value_unk_targets"] += 1
                continue
            c["tokens_excl_eos_unk"] += 1
            c["correct_excl_eos_unk"] += correct[t]
            if t in steps:
                c["step_value_tokens"] += 1
                c["step_value_correct"] += correct[t]
            elif tgt not in structural_ids:
                c["content_tokens"] += 1
                c["content_correct"] += correct[t]
    c["eos_correct"] = correct[-1] if n and target_ids[-1] == eos_id else 0
    return dict(c)


def aggregate_tf(counts: Dict[str, float]) -> Dict[str, Optional[float]]:
    """Turns summed counts into rates. Loss is mean NLL per token (incl. EOS, as in training)."""
    def r(a: str, b: str) -> Optional[float]:
        return counts.get(a, 0) / counts[b] if counts.get(b) else None
    return {
        "loss_incl_eos": r("nll_sum_incl_eos", "tokens_incl_eos"),
        "loss_excl_eos": r("nll_sum_excl_eos", "tokens_excl_eos"),
        "token_acc_incl_eos": r("correct_incl_eos", "tokens_incl_eos"),
        "token_acc_excl_eos": r("correct_excl_eos", "tokens_excl_eos"),
        "token_acc_excl_eos_and_unk_targets": r("correct_excl_eos_unk", "tokens_excl_eos_unk"),
        "unk_target_rate": r("unk_targets", "tokens_excl_eos"),
        "content_token_acc_excl_unk": r("content_correct", "content_tokens"),
        "step_value_acc_excl_unk": r("step_value_correct", "step_value_tokens"),
        "step_value_unk_count": counts.get("step_value_unk_targets", 0),
        "tokens_incl_eos": counts.get("tokens_incl_eos", 0),
    }


def macro_average(per_unit: List[Dict[str, Optional[float]]]) -> Dict[str, Optional[float]]:
    """Unweighted mean of each numeric field across units (programs), ignoring None."""
    keys = {k for d in per_unit for k, v in d.items() if isinstance(v, (int, float)) and not isinstance(v, bool)}
    out: Dict[str, Optional[float]] = {}
    for k in sorted(keys):
        vals = [d[k] for d in per_unit if isinstance(d.get(k), (int, float)) and not isinstance(d.get(k), bool)]
        out[k] = sum(vals) / len(vals) if vals else None
    bool_keys = {k for d in per_unit for k, v in d.items() if isinstance(v, bool)}
    for k in sorted(bool_keys):
        vals = [d[k] for d in per_unit if isinstance(d.get(k), bool)]
        out[k + "_rate"] = sum(vals) / len(vals) if vals else None
    return out


# ============================================================
# 4. GREEDY GENERATION (torch)
# ============================================================

def greedy_generate(model, src_ids, src_padding_mask, bos_id: int, eos_id: int, max_new_tokens: int):
    """
    Batched greedy decoding. Every sequence starts with [BOS]; at each step the argmax token is
    appended. A sequence stops contributing when it emits EOS; decoding ends when all sequences
    have emitted EOS or max_new_tokens steps have run. No sampling, penalties or constraints.

    Returns a list of (generated ids without BOS/EOS, eos_reached) per batch row.
    """
    import torch

    model.eval()
    batch = src_ids.size(0)
    device = src_ids.device
    with torch.no_grad():
        memory = model.encode(src_ids, src_padding_mask=src_padding_mask)
        dec = torch.full((batch, 1), bos_id, dtype=torch.long, device=device)
        finished = torch.zeros(batch, dtype=torch.bool, device=device)
        limit = min(max_new_tokens, model.config.max_tgt_length - 1)
        for _ in range(limit):
            hidden = model.decode(dec, memory, target_padding_mask=None, src_padding_mask=src_padding_mask)
            next_ids = model.output_projection(hidden[:, -1, :]).argmax(dim=-1)
            next_ids = torch.where(finished, torch.full_like(next_ids, eos_id), next_ids)
            dec = torch.cat([dec, next_ids.unsqueeze(1)], dim=1)
            finished |= next_ids.eq(eos_id)
            if bool(finished.all()):
                break
    results = []
    for row in dec[:, 1:].tolist():
        if eos_id in row:
            results.append((row[:row.index(eos_id)], True))
        else:
            results.append((row, False))
    return results


# ============================================================
# 5. CHECKPOINT COMPATIBILITY
# ============================================================

def spearman(x: Sequence[float], y: Sequence[float]) -> Optional[float]:
    """Spearman rank correlation (average ranks for ties). None if either input is constant."""
    def ranks(v):
        order = sorted(range(len(v)), key=lambda i: v[i])
        rk = [0.0] * len(v)
        i = 0
        while i < len(order):
            j = i
            while j + 1 < len(order) and v[order[j + 1]] == v[order[i]]:
                j += 1
            for k in range(i, j + 1):
                rk[order[k]] = (i + j) / 2
            i = j + 1
        return rk
    rx, ry = ranks(list(x)), ranks(list(y))
    mx, my = sum(rx) / len(rx), sum(ry) / len(ry)
    cov = sum((a - mx) * (b - my) for a, b in zip(rx, ry))
    vx = math.sqrt(sum((a - mx) ** 2 for a in rx))
    vy = math.sqrt(sum((b - my) ** 2 for b in ry))
    return None if vx == 0 or vy == 0 else cov / (vx * vy)


def check_compatibility(state_dict, model_config: Dict[str, Any], src_vocab, tgt_vocab) -> Dict[str, Any]:
    """
    Hard checks (must all pass): embedding/projection shapes equal vocabulary sizes; PAD ids agree.
    Heuristic (reported, not enforced): the target vocabulary is ordered by descending training
    frequency, so a trained model's output bias should decrease with token id; we report the
    Spearman correlation between bias and -id over ids >= 12. Checkpoints store no vocabulary
    fingerprint, so exact token-to-id identity cannot be proven from the checkpoint alone.
    """
    src_shape = tuple(state_dict["src_embedding.weight"].shape)
    tgt_shape = tuple(state_dict["tgt_embedding.weight"].shape)
    proj_shape = tuple(state_dict["output_projection.weight"].shape)
    hard = {
        "src_embedding_rows_match": src_shape[0] == len(src_vocab),
        "tgt_embedding_rows_match": tgt_shape[0] == len(tgt_vocab),
        "output_projection_rows_match": proj_shape[0] == len(tgt_vocab),
        "src_pad_id_match": model_config.get("src_pad_id", 0) == src_vocab.pad_id,
        "tgt_pad_id_match": model_config.get("tgt_pad_id", 0) == tgt_vocab.pad_id,
    }
    bias = state_dict["output_projection.bias"].float().tolist()
    ids = list(range(12, len(bias)))
    rho = spearman([bias[i] for i in ids], [-i for i in ids])
    return {
        "hard_checks": hard,
        "hard_checks_pass": all(hard.values()),
        "heuristic_bias_vs_frequency_rank_spearman": rho,
    }

"""
Versioned target representations for runtime traces.

  esr_v1_step    Current format (tokenize_and_analyze.tokenize_runtime):
                 <EVENT_START> TYPE <t> STEP <n> (<ATTR_START> <key> <value...> <ATTR_END>)* <EVENT_END>
  esr_v2_nostep  Identical except that the STEP marker and its value are omitted from every event.

STEP is a synthetic counter (create_esr.read_trace sets step = position, starting at 1), so v1 can be
reconstructed exactly from v2 given the number of events that precede a chunk.

Escaping: an attribute key or value equal to a reserved token (including "STEP") is serialized as
"\\" followed by the token. A STEP *marker* can only occur at the event-header position, directly
after TYPE <t> (event token index 3), so escaped STEP attributes are never mistaken for markers.

Strict functions (strip_steps, insert_steps, insert_steps_chunks) are meant for reference data:
they raise TargetFormatError on malformed input and never repair it. strip_step_markers_lenient()
is the documented canonicalization for model outputs that may be malformed.
"""

from typing import Any, Callable, List, Sequence, Union

from chunk_trace import chunk_trace_tokens, parse_events_from_tokens, MalformedEventStreamError
from execution_dataset import ExecutionTraceDataset
from tokenize_and_analyze import RESERVED_TOKENS, tokenize_runtime

TARGET_FORMAT_V1 = "esr_v1_step"
TARGET_FORMAT_V2 = "esr_v2_nostep"
TARGET_FORMATS = (TARGET_FORMAT_V1, TARGET_FORMAT_V2)

EVENT_START, EVENT_END, TYPE, STEP, ESCAPE = "<EVENT_START>", "<EVENT_END>", "TYPE", "STEP", "\\"


class TargetFormatError(ValueError):
    """Input does not follow the serialization grammar required by a strict conversion."""


# ============================================================
# SERIALIZERS
# ============================================================

def _events_of(runtime_trace: Any) -> List[Any]:
    """Event discovery, mirroring tokenize_runtime exactly."""
    if isinstance(runtime_trace, dict):
        if "events" in runtime_trace:
            return runtime_trace["events"]
        if "runtime_events" in runtime_trace:
            return runtime_trace["runtime_events"]
        if "type" in runtime_trace:
            return [runtime_trace]
        return []
    if isinstance(runtime_trace, list):
        return runtime_trace
    return []


def tokenize_runtime_nostep(runtime_trace: Any) -> List[str]:
    """
    esr_v2_nostep serialization: the unchanged tokenize_runtime applied to events whose 'step' key
    has been removed (tokenize_runtime emits STEP only when 'step' is present), so escaping and every
    other token are identical to v1 by construction.
    """
    events = [
        {k: v for k, v in e.items() if k != "step"} if isinstance(e, dict) else e
        for e in _events_of(runtime_trace)
    ]
    return tokenize_runtime({"events": events})


SERIALIZERS = {TARGET_FORMAT_V1: tokenize_runtime, TARGET_FORMAT_V2: tokenize_runtime_nostep}


# ============================================================
# STRICT CONVERSIONS (reference data)
# ============================================================

def _strict_events(tokens: Sequence[str]) -> List[List[str]]:
    try:
        events, outside = parse_events_from_tokens(list(tokens), strict=True)
    except MalformedEventStreamError as e:
        raise TargetFormatError(str(e)) from e
    if outside:
        raise TargetFormatError(f"{len(outside)} tokens outside events")
    for ev in events:
        if len(ev) < 4 or ev[1] != TYPE or ev[2] in RESERVED_TOKENS:
            raise TargetFormatError(f"event header is not '<EVENT_START> TYPE <type>': {ev[:4]}")
    return events


def _has_step_marker(ev: List[str]) -> bool:
    return len(ev) >= 6 and ev[3] == STEP


def strip_steps(tokens: Sequence[str]) -> List[str]:
    """esr_v1_step -> esr_v2_nostep. Every event must carry a STEP marker with a value."""
    out: List[str] = []
    for ev in _strict_events(tokens):
        if not _has_step_marker(ev) or ev[4] in RESERVED_TOKENS:
            raise TargetFormatError(f"event without a valid 'STEP <n>' header: {ev[:6]}")
        out.extend(ev[:3] + ev[5:])
    return out


def insert_steps(tokens: Sequence[str], start: int = 1) -> List[str]:
    """esr_v2_nostep -> esr_v1_step, numbering events start, start+1, ... Rejects events that already have STEP."""
    if start < 1:
        raise TargetFormatError("step numbering starts at 1")
    out: List[str] = []
    for i, ev in enumerate(_strict_events(tokens)):
        if _has_step_marker(ev):
            raise TargetFormatError(f"event already has a STEP marker: {ev[:6]}")
        out.extend(ev[:3] + [STEP, str(start + i)] + ev[3:])
    return out


def count_events(tokens: Sequence[str]) -> int:
    return len(_strict_events(tokens))


def insert_steps_chunks(chunks: Sequence[Sequence[str]]) -> List[str]:
    """
    Reconstructs the full v1 token stream from ordered v2 chunks of one program. Chunk k is numbered
    from (events in chunks 0..k-1) + 1. Only valid when no events were dropped between chunks
    (StepFreeExecutionTraceDataset enforces this).
    """
    out: List[str] = []
    seen = 0
    for ch in chunks:
        out.extend(insert_steps(ch, start=seen + 1))
        seen += count_events(ch)
    return out


# ============================================================
# LENIENT CANONICALIZATION (model outputs)
# ============================================================

def strip_step_markers_lenient(seq: Sequence[Any], step: Any = STEP, escape: Any = ESCAPE) -> List[Any]:
    """
    Canonical step-free form of a possibly malformed sequence (tokens or ids): removes every unescaped
    STEP marker together with the element that follows it; escaped elements are copied verbatim with
    their escape. Nothing else is changed. On grammar-valid v1 streams this equals strip_steps().
    """
    out: List[Any] = []
    i, n = 0, len(seq)
    while i < n:
        tok = seq[i]
        if tok == escape:
            out.extend(seq[i:i + 2])
            i += 2
            continue
        if tok == step:
            i += 2
            continue
        out.append(tok)
        i += 1
    return out


def first_n_events(seq: Sequence[Any], n_events: int, event_end: Any = EVENT_END, escape: Any = ESCAPE) -> List[Any]:
    """Prefix of a (possibly malformed) sequence up to and including its n-th unescaped EVENT_END."""
    if n_events <= 0:
        return []
    seen = 0
    i = 0
    while i < len(seq):
        if seq[i] == escape:
            i += 2
            continue
        if seq[i] == event_end:
            seen += 1
            if seen == n_events:
                return list(seq[:i + 1])
        i += 1
    return list(seq)


# ============================================================
# DATASET (Option B: subclass; execution_dataset.py is not modified)
# ============================================================

class FormattedExecutionTraceDataset(ExecutionTraceDataset):
    """
    ExecutionTraceDataset with a selectable target serializer. _build_index and _get_record_chunks are
    copies of the parent methods with tokenize_runtime replaced by self.runtime_tokenizer; a regression
    test asserts that the esr_v1_step variant reproduces ExecutionTraceDataset exactly.
    """

    def __init__(self, *args, target_format: str = TARGET_FORMAT_V1, **kwargs) -> None:
        if target_format not in SERIALIZERS:
            raise ValueError(f"Unknown target_format '{target_format}'")
        self.target_format = target_format
        self.runtime_tokenizer: Callable[[Any], List[str]] = SERIALIZERS[target_format]
        super().__init__(*args, **kwargs)
        if target_format == TARGET_FORMAT_V2 and self.total_oversized_events:
            raise TargetFormatError(
                f"{self.total_oversized_events} oversized events were dropped; step positions after them "
                "could not be reconstructed. Refusing to build an esr_v2_nostep dataset."
            )

    def _build_index(self) -> None:
        import json
        cum = 0
        with open(self.jsonl_path, "r", encoding="utf-8") as f:
            while True:
                offset = f.tell()
                line = f.readline()
                if not line:
                    break
                line_str = line.strip()
                if not line_str:
                    continue
                try:
                    rec = json.loads(line_str)
                except json.JSONDecodeError as e:
                    raise ValueError(f"Malformed JSON at offset {offset} in {self.jsonl_path}: {e}") from e
                if "program_id" not in rec:
                    raise KeyError(f"Record at offset {offset} missing required 'program_id' field")
                pid = rec["program_id"]
                java_file = self.java_dir / f"{pid}.java"
                if not java_file.is_file():
                    raise FileNotFoundError(f"Missing required Java file for program_id '{pid}': {java_file}")
                tokens = self.runtime_tokenizer(rec.get("runtime_trace", {}))
                chunk_res = chunk_trace_tokens(tokens, max_chunk_size=self.max_chunk_size,
                                               oversized_policy=self.oversized_policy, strict=True)
                self.record_offsets.append(offset)
                self.program_ids.append(pid)
                self.chunks_per_record.append(chunk_res.chunk_count)
                cum += chunk_res.chunk_count
                self.cum_chunk_counts.append(cum)
                if chunk_res.oversized_events:
                    self.total_oversized_events += len(chunk_res.oversized_events)
                    for ov in chunk_res.oversized_events:
                        self.oversized_metadata.append(
                            {"program_id": pid, "event_token_length": len(ov), "first_tokens": ov[:5]})
        self.total_chunks = cum

    def _get_record_chunks(self, record_idx: int) -> List[List[str]]:
        import json
        if record_idx in self._trace_cache:
            self._trace_cache_order.remove(record_idx)
            self._trace_cache_order.append(record_idx)
            return self._trace_cache[record_idx]
        with open(self.jsonl_path, "r", encoding="utf-8") as f:
            f.seek(self.record_offsets[record_idx])
            rec = json.loads(f.readline().strip())
        tokens = self.runtime_tokenizer(rec.get("runtime_trace", {}))
        chunks = chunk_trace_tokens(tokens, max_chunk_size=self.max_chunk_size,
                                    oversized_policy=self.oversized_policy, strict=True).chunks
        if len(self._trace_cache) >= self.cache_records:
            oldest = self._trace_cache_order.pop(0)
            self._trace_cache.pop(oldest, None)
        self._trace_cache[record_idx] = chunks
        self._trace_cache_order.append(record_idx)
        return chunks


def StepFreeExecutionTraceDataset(*args, **kwargs) -> FormattedExecutionTraceDataset:
    """Convenience constructor for the esr_v2_nostep dataset."""
    return FormattedExecutionTraceDataset(*args, target_format=TARGET_FORMAT_V2, **kwargs)

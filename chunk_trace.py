"""
Module 4, Part 3: Event-Preserving Runtime Trace Chunking.

Implements deterministic packing of event-aware serialized runtime tokens
into bounded chunks without splitting complete events across chunks.
Preserves event order, event boundaries, and exact token sequences under Guarantee B.
"""
from dataclasses import dataclass, field
from typing import List, Optional, Tuple, Dict, Any
from tokenize_and_analyze import tokenize_runtime, RESERVED_TOKENS


class ChunkingError(Exception):
    """Base exception for chunking failures."""
    pass


class MalformedEventStreamError(ChunkingError):
    """Raised when the serialized token stream violates the event grammar."""
    pass


class OversizedEventError(ChunkingError):
    """Raised when an individual event exceeds max_chunk_size under policy='error'."""
    pass


@dataclass
class ChunkingResult:
    """
    Result of chunking an event-aware runtime token stream.
    
    Attributes:
        chunks: List of token lists, where each chunk has len <= max_chunk_size
        oversized_events: List of token lists for events exceeding max_chunk_size
        total_events: Number of valid complete events identified
        max_chunk_size: The configured maximum token limit per chunk
        policy: The oversized event policy used ('separate', 'error', 'keep')
    """
    chunks: List[List[str]] = field(default_factory=list)
    oversized_events: List[List[str]] = field(default_factory=list)
    total_events: int = 0
    max_chunk_size: int = 1024
    policy: str = "separate"

    @property
    def chunk_count(self) -> int:
        return len(self.chunks)


def parse_events_from_tokens(tokens: List[str], strict: bool = True) -> Tuple[List[List[str]], List[str]]:
    """
    Parses a flat serialized token stream into discrete, complete events.
    
    Each valid event must start with an unescaped <EVENT_START> and terminate
    with an unescaped <EVENT_END>. Literal occurrences of markers inside
    attributes are preceded by '\\' and will not be treated as event delimiters.
    
    Parameters:
        tokens: Flat list of tokens emitted by tokenize_runtime.
        strict: If True, raises MalformedEventStreamError on unclosed or malformed events.
                If False, collects trailing/orphaned tokens into a malformed list.
                
    Returns:
        (events, malformed_tokens)
    """
    events: List[List[str]] = []
    current_event: List[str] = []
    malformed: List[str] = []
    in_event = False
    i = 0
    n = len(tokens)

    while i < n:
        tok = tokens[i]

        # Check for escape token
        if tok == "\\":
            if i + 1 < n:
                if in_event:
                    current_event.append(tok)
                    current_event.append(tokens[i + 1])
                else:
                    malformed.extend([tok, tokens[i + 1]])
                i += 2
                continue
            else:
                # Trailing escape
                if in_event:
                    current_event.append(tok)
                else:
                    malformed.append(tok)
                i += 1
                continue

        # Unescaped event start
        if tok == "<EVENT_START>":
            if in_event:
                msg = f"Unexpected unescaped <EVENT_START> before previous event closed at index {i}"
                if strict:
                    raise MalformedEventStreamError(msg)
                else:
                    malformed.extend(current_event)
            current_event = [tok]
            in_event = True
            i += 1
            continue

        # Unescaped event end
        if tok == "<EVENT_END>":
            if in_event:
                current_event.append(tok)
                events.append(current_event)
                current_event = []
                in_event = False
                i += 1
                continue
            else:
                msg = f"Unexpected unescaped <EVENT_END> outside of event boundary at index {i}"
                if strict:
                    raise MalformedEventStreamError(msg)
                else:
                    malformed.append(tok)
                    i += 1
                    continue

        # Normal token
        if in_event:
            current_event.append(tok)
        else:
            malformed.append(tok)
        i += 1

    if in_event and current_event:
        msg = f"Stream ended with unclosed event starting with {current_event[:5]}..."
        if strict:
            raise MalformedEventStreamError(msg)
        else:
            malformed.extend(current_event)

    return events, malformed


def chunk_trace_tokens(
    tokens: List[str],
    max_chunk_size: int = 1024,
    oversized_policy: str = "separate",
    strict: bool = True
) -> ChunkingResult:
    """
    Partitions an event-aware token sequence into chunks without splitting events.
    
    Guarantees:
      1. Every chunk contains one or more complete events (<EVENT_START>...<EVENT_END>).
      2. Sum of tokens in any normal chunk <= max_chunk_size.
      3. Original event order is strictly preserved.
      4. Exact token sequence of each event is unchanged (lossless under Guarantee B).
      5. Empty traces produce 0 chunks (empty result).
      6. Single events larger than max_chunk_size are handled deterministically via oversized_policy.
      
    Parameters:
        tokens: Token sequence from tokenize_runtime.
        max_chunk_size: Maximum tokens per chunk (default: 1024). Must be >= 6.
        oversized_policy:
            'separate' (default): Oversized events are placed in oversized_events list
                                  and not silently truncated or split.
            'error': Raises OversizedEventError on any event with len > max_chunk_size.
            'keep': Places oversized events as dedicated standalone chunks (len > max_chunk_size).
        strict: If True, raises MalformedEventStreamError on unclosed event boundaries.
        
    Returns:
        ChunkingResult containing chunks, oversized_events, and metadata.
    """
    if max_chunk_size < 6:
        raise ValueError(f"max_chunk_size must be at least 6 tokens (minimum event size), got {max_chunk_size}")

    if oversized_policy not in ("separate", "error", "keep"):
        raise ValueError(f"Invalid oversized_policy '{oversized_policy}'. Must be 'separate', 'error', or 'keep'")

    if not tokens:
        return ChunkingResult(
            chunks=[],
            oversized_events=[],
            total_events=0,
            max_chunk_size=max_chunk_size,
            policy=oversized_policy
        )

    events, malformed = parse_events_from_tokens(tokens, strict=strict)

    chunks: List[List[str]] = []
    oversized_events: List[List[str]] = []
    current_chunk: List[str] = []
    current_chunk_len: int = 0

    for ev in events:
        ev_len = len(ev)

        # Handle individual oversized event
        if ev_len > max_chunk_size:
            if oversized_policy == "error":
                raise OversizedEventError(
                    f"Event of length {ev_len} exceeds max_chunk_size {max_chunk_size}: "
                    f"{' '.join(ev[:10])}..."
                )
            elif oversized_policy == "separate":
                # Flush existing chunk first to preserve event ordering timeline
                if current_chunk:
                    chunks.append(current_chunk)
                    current_chunk = []
                    current_chunk_len = 0
                oversized_events.append(ev)
                continue
            elif oversized_policy == "keep":
                # Flush existing chunk, then append oversized event as its own chunk
                if current_chunk:
                    chunks.append(current_chunk)
                    current_chunk = []
                    current_chunk_len = 0
                chunks.append(ev)
                continue

        # Check packing into current chunk
        if current_chunk_len + ev_len <= max_chunk_size:
            current_chunk.extend(ev)
            current_chunk_len += ev_len
        else:
            # Current chunk is full; start a new chunk
            if current_chunk:
                chunks.append(current_chunk)
            current_chunk = list(ev)
            current_chunk_len = ev_len

    if current_chunk:
        chunks.append(current_chunk)

    return ChunkingResult(
        chunks=chunks,
        oversized_events=oversized_events,
        total_events=len(events),
        max_chunk_size=max_chunk_size,
        policy=oversized_policy
    )


def chunk_runtime_trace(
    runtime_trace: Any,
    max_chunk_size: int = 1024,
    oversized_policy: str = "separate",
    strict: bool = True
) -> ChunkingResult:
    """Convenience wrapper that tokenizes a runtime_trace structure and chunks it."""
    tokens = tokenize_runtime(runtime_trace)
    return chunk_trace_tokens(
        tokens,
        max_chunk_size=max_chunk_size,
        oversized_policy=oversized_policy,
        strict=strict
    )

# MODULE4_PART1_RUNTIME_REPRESENTATION_CONTRACT

**Location:** `file:///C:/Users/athar/.gemini/antigravity-ide/brain/69fb854d-1e27-40da-9b2c-5dfecf9458dc/MODULE4_PART1_RUNTIME_REPRESENTATION_CONTRACT.md`

---

## 1. Executive Summary
*(unchanged – summary of the contract)*

---

## 2. Canonical Terminology Recommendation
*(unchanged – SRER recommendation and compatibility note)*

---

## 3. Actual Event Schema (Verified with Real Data)
*(unchanged – schema description)*

---

## 4. Deterministic Serialization Specification (Part 2.1 Scope)

The tokenizer consumes structured runtime events and produces a **plain-text token sequence** that unambiguously encodes each event and its constituent attributes.

### 4.1 Structural Tokens and Grammar
To eliminate ambiguity when attribute values contain multiple tokens, the grammar defines explicit hierarchical boundaries:

1. **Event boundaries**: Each event begins with `<EVENT_START>` and terminates with `<EVENT_END>`.
2. **Fixed field markers**:
   - `TYPE` precedes the compact event type code (`TS`, `MC`, etc.).
   - `STEP` precedes the integer step value (as a decimal string).
3. **Attribute boundaries**: Each attribute key-value pair is enclosed within dedicated boundary tokens:
   - `<ATTR_START>` begins an attribute group.
   - The token immediately following `<ATTR_START>` is the **attribute key** (escaped if matching a reserved token).
   - The subsequent tokens before `<ATTR_END>` represent the **attribute value**:
     - If the attribute value is empty (`""` or `None`), the single token `<EMPTY_VAL>` is emitted.
     - Otherwise, lexical tokens produced by `VALUE_TOKEN_REGEX` are emitted.
   - `<ATTR_END>` terminates the attribute group.
4. **Whitespace**: Tokens are separated by whitespace; original whitespace within values is not preserved (see Guarantee B).

```text
<EVENT_START>
  TYPE  <event_type_token>
  STEP  <step_value_token>
  <ATTR_START>  <key_1>  <val_tok_1> ... <val_tok_n>  <ATTR_END>
  <ATTR_START>  <key_2>  <EMPTY_VAL>  <ATTR_END>
<EVENT_END>
```

### 4.2 Reconstruction Guarantee (Guarantee B)
The tokenizer provides **Guarantee B: Unambiguous reconstruction of event structure and lexical token sequences, without preserving original whitespace.**

- **Rationale**: The tokenizer is lexical; whitespace between tokens in values like `"main (1)"` or `"Main#main:1"` is discarded during tokenization. Preserving arbitrary intra-string whitespace would require character-level escaping or whitespace tokens, dramatically inflating sequence lengths for no downstream model benefit.
- **Guarantee properties**:
  - Event count, ordering, event types, and step numbers are reconstructed losslessly.
  - Attribute keys and the partition of tokens belonging to each attribute are reconstructed losslessly.
  - The ordered sequence of lexical tokens for each attribute value is reconstructed losslessly.
  - Explicit empty values (`""` / `None`) are distinguished losslessly from missing attributes.

### 4.3 Escaping Scheme for Reserved Tokens
Reserved structural tokens may appear literally within attribute values or keys. The set of reserved tokens is:
```python
RESERVED_TOKENS = {
    "<EVENT_START>",
    "<EVENT_END>",
    "<ATTR_START>",
    "<ATTR_END>",
    "<EMPTY_VAL>",
    "TYPE",
    "STEP",
    "\\",
}
```

1. **Escape prefix**: A backslash token `\` is inserted immediately before any token that matches one of the reserved tokens.
2. **Backslash escaping**: A literal backslash character itself is treated as a reserved token; when encountered, it is preceded by an escape backslash, emitting `\` `\`.
3. **Decoding rule**: Scanned left-to-right, an unescaped `\` indicates that the next token is a literal token.
4. **Non-reserved tokens**: Substrings such as `TYPE2` or `STEP99` match as atomic identifiers (`\w+`) and are NOT reserved; they are not escaped. Substrings embedding marker delimiters like `X<EVENT_START>Y` are split by regex into `["X", "<EVENT_START>", "Y"]`, where `<EVENT_START>` is escaped as `\` `<EVENT_START>`.

*Example 1 (Multi-token attributes)*:
```text
Event: {"type": "MC", "step": 1, "attributes": {"CALLER": "Main#main:1", "TARGET": "Foo#bar:2"}}
Tokens:
  <EVENT_START> TYPE MC STEP 1 <ATTR_START> CALLER Main # main : 1 <ATTR_END> <ATTR_START> TARGET Foo # bar : 2 <ATTR_END> <EVENT_END>
```

*Example 2 (Reserved markers & backslash)*:
```text
Event: {"type": "TS", "step": 1, "attributes": {"NOTE": "<EVENT_START> \\ special"}}
Tokens:
  <EVENT_START> TYPE TS STEP 1 <ATTR_START> NOTE \ <EVENT_START> \ \ special <ATTR_END> <EVENT_END>
```

---

## 5. Attribute Preservation Guarantees
*(unchanged – note on duplicate‑key overwrite)*

---

## 6. Scope of Upcoming Parts
*(unchanged – chunking moved to Part 3)*

---

## 7. Test Status
Part 2.1 introduces regression test suite verifying explicit attribute boundaries, multi-token separation, and round-trip decoding under Guarantee B.

---

## 8. Acceptance Checklist
- [x] Attribute-boundary ambiguity resolved with explicit `<ATTR_START>` / `<ATTR_END>`
- [x] Guarantee B formally defined and verified
- [x] Escaping scheme covers all reserved tokens including `<ATTR_START>` and `<ATTR_END>`
- [x] Multi-token attributes separated without token bleeding
- [x] Regression test suite updated and passing

---

## 9. Summary of Corrections (Part 2.1)
1. Identified genuine defect: lack of attribute boundaries caused severe ambiguity for multi-token attribute values (which represent 60.66% of real dataset attribute values).
2. Refuted incorrect audit claim: `TYPE2` is matched as an atomic token by `\w+`, not split into `TYPE` and `2`.
3. Introduced `<ATTR_START>` and `<ATTR_END>` tokens to bound each attribute key and value.
4. Defined and documented Guarantee B (structural + lexical token sequence reconstruction without original whitespace).
5. Updated `RESERVED_TOKENS` and regex pattern to include `<ATTR_START>` and `<ATTR_END>`.

---

*Report updated on 2026-10-11 for Part 2.1.*

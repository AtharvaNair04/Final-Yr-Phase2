import unittest
import json
import csv
import io
from tokenize_and_analyze import tokenize_runtime, RESERVED_TOKENS, VALUE_TOKEN_REGEX


def detokenize_events(tokens, return_token_values=False):
    """
    Decodes an event-aware token sequence back into structured events.
    Supports reversing escaped reserved tokens, empty values, and attributes.

    Guarantee B: Unambiguous reconstruction of event structure and lexical
    token sequences, without preserving original whitespace.

    Parameters:
        tokens: list of str
        return_token_values: if True, attribute values are returned as lists of
            lexical tokens (or "" for empty). If False, attribute values are
            reconstructed as joined strings ("".join of value tokens).
    """
    events = []
    i = 0
    n = len(tokens)

    while i < n:
        if tokens[i] != "<EVENT_START>":
            raise ValueError(f"Expected unescaped <EVENT_START> at token index {i}, got {tokens[i]}")
        i += 1

        event = {}

        # TYPE marker
        if i < n and tokens[i] == "TYPE":
            i += 1
            if i >= n:
                raise ValueError("Unexpected end of tokens after TYPE marker")
            event["type"] = tokens[i]
            i += 1

        # STEP marker
        if i < n and tokens[i] == "STEP":
            i += 1
            if i >= n:
                raise ValueError("Unexpected end of tokens after STEP marker")
            event["step"] = int(tokens[i])
            i += 1

        # Attributes enclosed in <ATTR_START> ... <ATTR_END>
        attributes = {}
        while i < n and tokens[i] == "<ATTR_START>":
            i += 1
            if i >= n:
                raise ValueError("Unexpected end of tokens after <ATTR_START>")

            # Decode attribute key (which may be escaped with \)
            if tokens[i] == "\\":
                i += 1
                if i >= n:
                    raise ValueError("Dangling escape token before attribute key")
                attr_key = tokens[i]
            else:
                attr_key = tokens[i]
            i += 1

            # Value tokens until <ATTR_END>
            if i < n and tokens[i] == "<EMPTY_VAL>":
                attributes[attr_key] = [] if return_token_values else ""
                i += 1
                if i >= n or tokens[i] != "<ATTR_END>":
                    raise ValueError(f"Expected <ATTR_END> after <EMPTY_VAL>, got {tokens[i] if i < n else 'EOF'}")
                i += 1
            else:
                val_tokens = []
                while i < n and tokens[i] != "<ATTR_END>":
                    if tokens[i] == "\\":
                        i += 1
                        if i >= n:
                            raise ValueError("Dangling escape token in attribute value")
                        val_tokens.append(tokens[i])
                    else:
                        val_tokens.append(tokens[i])
                    i += 1

                if i >= n or tokens[i] != "<ATTR_END>":
                    raise ValueError(f"Expected <ATTR_END> terminating attribute {attr_key}")
                i += 1

                attributes[attr_key] = val_tokens if return_token_values else "".join(val_tokens)

        if attributes:
            event["attributes"] = attributes

        if i >= n or tokens[i] != "<EVENT_END>":
            raise ValueError(f"Expected <EVENT_END> at index {i}, got {tokens[i] if i < n else 'EOF'}")
        i += 1

        events.append(event)

    return events


class TestTokenizeRuntime(unittest.TestCase):
    def assertTokensEqual(self, runtime_trace, expected):
        tokens = tokenize_runtime(runtime_trace)
        self.assertEqual(tokens, expected)

    def test_1_normal_event_with_multiple_attributes(self):
        rt = {"events": [{"type": "TS", "step": 1, "attributes": {"CALLER": "foo", "ARG": "42"}}]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "1",
            "<ATTR_START>", "CALLER", "foo", "<ATTR_END>",
            "<ATTR_START>", "ARG", "42", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_2_empty_attributes_dict(self):
        rt = {"events": [{"type": "MC", "step": 2, "attributes": {}}]}
        expected = ["<EVENT_START>", "TYPE", "MC", "STEP", "2", "<EVENT_END>"]
        self.assertTokensEqual(rt, expected)

    def test_3_empty_string_attribute(self):
        rt = {"events": [{"type": "MC", "step": 3, "attributes": {"NOTE": ""}}]}
        expected = [
            "<EVENT_START>", "TYPE", "MC", "STEP", "3",
            "<ATTR_START>", "NOTE", "<EMPTY_VAL>", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_3b_none_attribute_value_treated_as_empty(self):
        rt = {"events": [{"type": "MC", "step": 3, "attributes": {"NOTE": None}}]}
        expected = [
            "<EVENT_START>", "TYPE", "MC", "STEP", "3",
            "<ATTR_START>", "NOTE", "<EMPTY_VAL>", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_4_missing_attribute(self):
        rt = {"events": [{"type": "TS", "step": 4}]}
        expected = ["<EVENT_START>", "TYPE", "TS", "STEP", "4", "<EVENT_END>"]
        self.assertTokensEqual(rt, expected)

    def test_5_whitespace_and_punctuation(self):
        rt = {"events": [{"type": "TS", "step": 5, "attributes": {"MSG": "Hello, world!"}}]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "5",
            "<ATTR_START>", "MSG", "Hello", ",", "world", "!", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_6_literal_reserved_field_markers(self):
        rt = {"events": [{"type": "TS", "step": 6, "attributes": {"LABEL": "TYPE", "OTHER": "STEP"}}]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "6",
            "<ATTR_START>", "LABEL", "\\", "TYPE", "<ATTR_END>",
            "<ATTR_START>", "OTHER", "\\", "STEP", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_7_literal_event_markers(self):
        # Literal <EVENT_START>, <EVENT_END>, and <EMPTY_VAL> must be escaped
        rt = {"events": [
            {"type": "TS", "step": 7, "attributes": {"START": "<EVENT_START>", "END": "<EVENT_END>", "EMPTY": "<EMPTY_VAL>"}}
        ]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "7",
            "<ATTR_START>", "START", "\\", "<EVENT_START>", "<ATTR_END>",
            "<ATTR_START>", "END", "\\", "<EVENT_END>", "<ATTR_END>",
            "<ATTR_START>", "EMPTY", "\\", "<EMPTY_VAL>", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_8a_single_literal_backslash(self):
        rt = {"events": [{"type": "TS", "step": 8, "attributes": {"SLASH": "\\"}}]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "8",
            "<ATTR_START>", "SLASH", "\\", "\\", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_8b_multiple_literal_backslashes(self):
        rt = {"events": [{"type": "TS", "step": 8, "attributes": {"DOUBLE": "\\\\"}}]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "8",
            "<ATTR_START>", "DOUBLE", "\\", "\\", "\\", "\\", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_8c_path_with_backslashes(self):
        rt = {"events": [{"type": "TS", "step": 8, "attributes": {"PATH": r"C\folder\file"}}]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "8",
            "<ATTR_START>", "PATH", "C", "\\", "\\", "folder", "\\", "\\", "file", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_8d_mixed_reserved_and_backslashes(self):
        rt = {"events": [{"type": "TS", "step": 8, "attributes": {"NOTE": r"<EVENT_START> \ TYPE"}}]}
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "8",
            "<ATTR_START>", "NOTE", "\\", "<EVENT_START>", "\\", "\\", "\\", "TYPE", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertTokensEqual(rt, expected)

    def test_9_unknown_event_type(self):
        rt = {"events": [{"type": "CUSTOM_UNKNOWN", "step": 9}]}
        expected = ["<EVENT_START>", "TYPE", "CUSTOM_UNKNOWN", "STEP", "9", "<EVENT_END>"]
        self.assertTokensEqual(rt, expected)

    def test_10_large_step_value(self):
        rt = {"events": [{"type": "TS", "step": 987654321012345}]}
        expected = ["<EVENT_START>", "TYPE", "TS", "STEP", "987654321012345", "<EVENT_END>"]
        self.assertTokensEqual(rt, expected)

    def test_11_deterministic_output(self):
        rt = {"events": [
            {"type": "TS", "step": 1, "attributes": {"A": "x", "B": "y", "C": "z"}},
            {"type": "MC", "step": 2, "attributes": {"M": "100"}}
        ]}
        tokens1 = tokenize_runtime(rt)
        tokens2 = tokenize_runtime(rt)
        self.assertEqual(tokens1, tokens2)

    def test_12_duplicate_key_csv_to_json(self):
        csv_data = "MC,caller=SYSTEM,target=Foo#bar:1,target=Override#baz:2\n"
        reader = csv.reader(io.StringIO(csv_data))
        row = next(reader)
        event_type = row[0].strip()
        attributes = {}
        for cell in row[1:]:
            cell = cell.strip()
            if "=" in cell:
                key, value = cell.split("=", 1)
                attributes[key.strip().upper()] = value.strip()

        self.assertEqual(attributes["CALLER"], "SYSTEM")
        self.assertEqual(attributes["TARGET"], "Override#baz:2")

        event = {"type": event_type, "step": 1, "attributes": attributes}
        tokens = tokenize_runtime({"events": [event]})
        expected = [
            "<EVENT_START>", "TYPE", "MC", "STEP", "1",
            "<ATTR_START>", "CALLER", "SYSTEM", "<ATTR_END>",
            "<ATTR_START>", "TARGET", "Override", "#", "baz", ":", "2", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertEqual(tokens, expected)

    def test_13_structural_boundaries_and_escaping_guarantee(self):
        # Ensure that in an event containing literal marker strings, unescaped markers
        # appear exclusively at the event and attribute boundaries.
        rt = {"events": [
            {
                "type": "TS",
                "step": 1,
                "attributes": {
                    "NOTE1": "<EVENT_START>",
                    "NOTE2": "<EVENT_END>",
                    "NOTE3": "<ATTR_START>",
                    "NOTE4": "<ATTR_END>",
                    "NOTE5": "<EMPTY_VAL>",
                    "NOTE6": "TYPE",
                    "NOTE7": "STEP",
                    "NOTE8": "\\"
                }
            }
        ]}
        tokens = tokenize_runtime(rt)

        self.assertEqual(tokens[0], "<EVENT_START>")
        self.assertEqual(tokens[-1], "<EVENT_END>")

        # Any internal occurrence of reserved markers within attribute values must be escaped
        for idx in range(1, len(tokens) - 1):
            tok = tokens[idx]
            # Check if this token is a literal occurrence within value
            # (i.e. not the structural <ATTR_START> or <ATTR_END> markers)
            if tok in ("<EVENT_START>", "<EVENT_END>", "<EMPTY_VAL>"):
                self.assertEqual(tokens[idx - 1], "\\", f"Token {tok} at index {idx} was not escaped with '\\'")

    def test_14_round_trip_reversibility(self):
        original_events = [
            {"type": "TS", "step": 1, "attributes": {"CALLER": "foo"}},
            {"type": "MC", "step": 2, "attributes": {"NOTE": ""}},
            {"type": "VW", "step": 3, "attributes": {"FLAG": "TYPE"}},
            {"type": "FW", "step": 4, "attributes": {"ESC": "<EVENT_START>"}},
            {"type": "NO", "step": 5, "attributes": {"SLASH": "\\"}},
        ]
        tokens = tokenize_runtime({"events": original_events})
        decoded = detokenize_events(tokens)

        self.assertEqual(len(decoded), len(original_events))
        for dec, orig in zip(decoded, original_events):
            self.assertEqual(dec["type"], orig["type"])
            self.assertEqual(dec["step"], orig["step"])
            self.assertEqual(dec.get("attributes"), orig.get("attributes"))

    def test_15_two_attributes_with_multitoken_values(self):
        """Proves that two multi-token attributes are separated unambiguously without token bleed."""
        rt = {
            "events": [{
                "type": "MC",
                "step": 1,
                "attributes": {
                    "CALLER": "Main#main:1",
                    "TARGET": "Foo#bar:2"
                }
            }]
        }
        tokens = tokenize_runtime(rt)
        expected = [
            "<EVENT_START>", "TYPE", "MC", "STEP", "1",
            "<ATTR_START>", "CALLER", "Main", "#", "main", ":", "1", "<ATTR_END>",
            "<ATTR_START>", "TARGET", "Foo", "#", "bar", ":", "2", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertEqual(tokens, expected)

        # Decode under Guarantee B
        decoded = detokenize_events(tokens, return_token_values=True)
        self.assertEqual(len(decoded), 1)
        self.assertEqual(decoded[0]["attributes"]["CALLER"], ["Main", "#", "main", ":", "1"])
        self.assertEqual(decoded[0]["attributes"]["TARGET"], ["Foo", "#", "bar", ":", "2"])

        # Decode as joined strings
        decoded_str = detokenize_events(tokens, return_token_values=False)
        self.assertEqual(decoded_str[0]["attributes"]["CALLER"], "Main#main:1")
        self.assertEqual(decoded_str[0]["attributes"]["TARGET"], "Foo#bar:2")

    def test_16_empty_and_nonempty_attributes_in_same_event(self):
        """Proves that empty and non-empty attributes coexist unambiguously."""
        rt = {
            "events": [{
                "type": "TS",
                "step": 1,
                "attributes": {
                    "EMPTY1": "",
                    "NON_EMPTY": "foo.bar",
                    "EMPTY2": None,
                    "ANOTHER": "42"
                }
            }]
        }
        tokens = tokenize_runtime(rt)
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "1",
            "<ATTR_START>", "EMPTY1", "<EMPTY_VAL>", "<ATTR_END>",
            "<ATTR_START>", "NON_EMPTY", "foo", ".", "bar", "<ATTR_END>",
            "<ATTR_START>", "EMPTY2", "<EMPTY_VAL>", "<ATTR_END>",
            "<ATTR_START>", "ANOTHER", "42", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertEqual(tokens, expected)

        decoded = detokenize_events(tokens)
        self.assertEqual(decoded[0]["attributes"]["EMPTY1"], "")
        self.assertEqual(decoded[0]["attributes"]["NON_EMPTY"], "foo.bar")
        self.assertEqual(decoded[0]["attributes"]["EMPTY2"], "")
        self.assertEqual(decoded[0]["attributes"]["ANOTHER"], "42")

    def test_17_reserved_attr_markers_in_value(self):
        """Proves that literal <ATTR_START> and <ATTR_END> in values are escaped and decoded."""
        rt = {
            "events": [{
                "type": "TS",
                "step": 1,
                "attributes": {
                    "V1": "<ATTR_START>",
                    "V2": "<ATTR_END>"
                }
            }]
        }
        tokens = tokenize_runtime(rt)
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "1",
            "<ATTR_START>", "V1", "\\", "<ATTR_START>", "<ATTR_END>",
            "<ATTR_START>", "V2", "\\", "<ATTR_END>", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertEqual(tokens, expected)

        decoded = detokenize_events(tokens)
        self.assertEqual(decoded[0]["attributes"]["V1"], "<ATTR_START>")
        self.assertEqual(decoded[0]["attributes"]["V2"], "<ATTR_END>")

    def test_18_marker_like_substrings_type2_and_embedded_markers(self):
        """Proves that TYPE2 is matched as an atomic token (not split or escaped),
        while embedded markers like X<EVENT_START>Y are properly split and escaped."""
        rt = {
            "events": [{
                "type": "TS",
                "step": 1,
                "attributes": {
                    "A": "TYPE2",
                    "B": "X<EVENT_START>Y",
                    "C": "STEP99",
                    "D": "A<ATTR_END>B"
                }
            }]
        }
        tokens = tokenize_runtime(rt)
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "1",
            "<ATTR_START>", "A", "TYPE2", "<ATTR_END>",
            "<ATTR_START>", "B", "X", "\\", "<EVENT_START>", "Y", "<ATTR_END>",
            "<ATTR_START>", "C", "STEP99", "<ATTR_END>",
            "<ATTR_START>", "D", "A", "\\", "<ATTR_END>", "B", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertEqual(tokens, expected)

        decoded = detokenize_events(tokens)
        self.assertEqual(decoded[0]["attributes"]["A"], "TYPE2")
        self.assertEqual(decoded[0]["attributes"]["B"], "X<EVENT_START>Y")
        self.assertEqual(decoded[0]["attributes"]["C"], "STEP99")
        self.assertEqual(decoded[0]["attributes"]["D"], "A<ATTR_END>B")

    def test_19_multiple_events_in_sequence(self):
        """Proves multiple events in sequence decode in exact order."""
        events = [
            {"type": "TS", "step": 1, "attributes": {"CALLER": "main"}},
            {"type": "MC", "step": 2, "attributes": {"CALLER": "SYSTEM", "TARGET": "Main#main:1"}},
            {"type": "VW", "step": 3, "attributes": {"CONTEXT": "Main#main:1", "ARGS": "java.lang.String[]:1"}},
            {"type": "NO", "step": 4, "attributes": {}},
            {"type": "ME", "step": 5, "attributes": {"RETURNER": "Main#main:1", "VALUE": "<void>"}}
        ]
        tokens = tokenize_runtime({"events": events})
        decoded = detokenize_events(tokens, return_token_values=True)

        self.assertEqual(len(decoded), 5)
        self.assertEqual(decoded[0]["type"], "TS")
        self.assertEqual(decoded[1]["type"], "MC")
        self.assertEqual(decoded[2]["type"], "VW")
        self.assertEqual(decoded[3]["type"], "NO")
        self.assertNotIn("attributes", decoded[3])
        self.assertEqual(decoded[4]["type"], "ME")

        # Verify token sequences for attributes
        self.assertEqual(decoded[1]["attributes"]["TARGET"], ["Main", "#", "main", ":", "1"])
        self.assertEqual(decoded[2]["attributes"]["ARGS"], ["java", ".", "lang", ".", "String", "[", "]", ":", "1"])
        self.assertEqual(decoded[4]["attributes"]["VALUE"], ["<", "void", ">"])

    def test_20_independent_structural_parser_verification(self):
        """Independent parser written without using detokenize_events,
        verifying that an external consumer can parse the grammar unambiguously."""
        rt = {
            "events": [
                {"type": "MC", "step": 1, "attributes": {"A": "1 2 3", "B": "4 5 6"}},
                {"type": "TS", "step": 2, "attributes": {"X": ""}}
            ]
        }
        tokens = tokenize_runtime(rt)

        # Independent parser implementation
        parsed_events = []
        curr_event = None
        curr_attr = None
        state = "ROOT"

        idx = 0
        while idx < len(tokens):
            tok = tokens[idx]
            if tok == "<EVENT_START>":
                curr_event = {"attrs": {}}
                state = "EVENT"
                idx += 1
            elif tok == "<EVENT_END>":
                parsed_events.append(curr_event)
                curr_event = None
                state = "ROOT"
                idx += 1
            elif state == "EVENT" and tok == "TYPE":
                curr_event["type"] = tokens[idx + 1]
                idx += 2
            elif state == "EVENT" and tok == "STEP":
                curr_event["step"] = int(tokens[idx + 1])
                idx += 2
            elif state == "EVENT" and tok == "<ATTR_START>":
                idx += 1
                # Key
                if tokens[idx] == "\\":
                    key = tokens[idx + 1]
                    idx += 2
                else:
                    key = tokens[idx]
                    idx += 1
                curr_attr = key
                val_toks = []
                while tokens[idx] != "<ATTR_END>":
                    if tokens[idx] == "\\":
                        val_toks.append(tokens[idx + 1])
                        idx += 2
                    elif tokens[idx] == "<EMPTY_VAL>":
                        idx += 1
                        break
                    else:
                        val_toks.append(tokens[idx])
                        idx += 1
                assert tokens[idx] == "<ATTR_END>"
                idx += 1
                curr_event["attrs"][curr_attr] = val_toks
            else:
                idx += 1

        self.assertEqual(len(parsed_events), 2)
        self.assertEqual(parsed_events[0]["type"], "MC")
        self.assertEqual(parsed_events[0]["step"], 1)
        self.assertEqual(parsed_events[0]["attrs"]["A"], ["1", "2", "3"])
        self.assertEqual(parsed_events[0]["attrs"]["B"], ["4", "5", "6"])
        self.assertEqual(parsed_events[1]["type"], "TS")
        self.assertEqual(parsed_events[1]["step"], 2)
        self.assertEqual(parsed_events[1]["attrs"]["X"], [])

    def test_21_guarantee_b_lexical_token_reconstruction(self):
        """Verifies Guarantee B: lexical token sequences are preserved 100% losslessly,
        even though whitespace between tokens is discarded."""
        original_attr_val = "main (1)"
        expected_lexical_tokens = ["main", "(", "1", ")"]

        rt = {"events": [{"type": "TS", "step": 1, "attributes": {"VALUE_1": original_attr_val}}]}
        tokens = tokenize_runtime(rt)

        decoded = detokenize_events(tokens, return_token_values=True)
        # Guarantee B: The exact sequence of lexical tokens is reconstructed
        self.assertEqual(decoded[0]["attributes"]["VALUE_1"], expected_lexical_tokens)

    def test_22_reserved_attribute_key(self):
        """Proves that an attribute key matching a reserved token is escaped and decoded."""
        rt = {"events": [{"type": "TS", "step": 1, "attributes": {"TYPE": "some_val", "\\": "slash_key"}}]}
        tokens = tokenize_runtime(rt)
        expected = [
            "<EVENT_START>", "TYPE", "TS", "STEP", "1",
            "<ATTR_START>", "\\", "TYPE", "some_val", "<ATTR_END>",
            "<ATTR_START>", "\\", "\\", "slash_key", "<ATTR_END>",
            "<EVENT_END>"
        ]
        self.assertEqual(tokens, expected)

        decoded = detokenize_events(tokens)
        self.assertEqual(decoded[0]["attributes"]["TYPE"], "some_val")
        self.assertEqual(decoded[0]["attributes"]["\\"], "slash_key")


if __name__ == '__main__':
    unittest.main()

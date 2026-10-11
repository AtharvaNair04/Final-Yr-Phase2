"""
Module 4, Part 4: Deterministic Java Source Code Tokenizer.

Implements lexical tokenization for Java source code (strings) and structured
Static Program Representations (SPR dicts/lists).
"""
import re
from typing import Any, List, Union

# ============================================================
# REGEX PATTERNS FOR JAVA LEXICAL SPECIFICATION
# ============================================================

# 1. Comments
LINE_COMMENT_PATTERN = r'//[^\r\n]*'
BLOCK_COMMENT_PATTERN = r'/\*[\s\S]*?\*/'
COMMENT_REGEX = re.compile(f'{LINE_COMMENT_PATTERN}|{BLOCK_COMMENT_PATTERN}')

# 2. String and Character Literals (including text blocks and escape sequences)
# Java 15+ text blocks: """ ... """
TEXT_BLOCK_PATTERN = r'"""[\s\S]*?"""'
STRING_LITERAL_PATTERN = r'"(?:[^"\\]|\\.)*"'
CHAR_LITERAL_PATTERN = r"'(?:[^'\\]|\\.)*'"

# 3. Multi-character Operators and Punctuation (ordered from longest to shortest)
MULTI_CHAR_OPS_PATTERN = (
    r'>>>='
    r'|\.\.\.'
    r'|>>>|>>=|<<='
    r'|\+\+|--|->|::'
    r'|==|!=|<=|>=|&&|\|\|'
    r'|\+=|-=|\*=|/=|%=|&=|\|=|\^='
    r'|<<|>>'
)

# 4. Numeric Literals (Hex, Binary, Floating-point, Integers with underscores and suffixes)
HEX_LITERAL_PATTERN = r'0[xX][0-9a-fA-F_]+(?:\.[0-9a-fA-F_]+)?(?:[pP][+-]?[0-9_]+)?[fFdDlL]?'
BIN_LITERAL_PATTERN = r'0[bB][01_]+[lL]?'
DEC_NUM_PATTERN = (
    r'(?:'
    r'\d[\d_]*(?:\.[\d_]+)?(?:[eE][+-]?[\d_]+)?[fFdDlL]?'
    r'|\.\d[\d_]*(?:[eE][+-]?[\d_]+)?[fFdDlL]?'
    r')'
)

# 5. Identifiers and Keywords (Java permits Unicode letters, currency symbols, underscores, and digits)
# Python's \w matches Unicode letters, digits, and underscore; adding $ for Java currency symbol support.
# The first character must not be a digit (handled by the negative lookahead (?!\d)).
IDENTIFIER_PATTERN = r'(?!\d)[\w$][\w$]*'

# 6. Single-character Operators and Punctuation
SINGLE_CHAR_PATTERN = r'[{}()\[\].,;:+\-*/%=<>&|^~!?:@]'

# Combined Master Lexical Regex
JAVA_LEXICAL_REGEX = re.compile(
    rf'{LINE_COMMENT_PATTERN}'
    rf'|{BLOCK_COMMENT_PATTERN}'
    rf'|{TEXT_BLOCK_PATTERN}'
    rf'|{STRING_LITERAL_PATTERN}'
    rf'|{CHAR_LITERAL_PATTERN}'
    rf'|{MULTI_CHAR_OPS_PATTERN}'
    rf'|{HEX_LITERAL_PATTERN}'
    rf'|{BIN_LITERAL_PATTERN}'
    rf'|{DEC_NUM_PATTERN}'
    rf'|{IDENTIFIER_PATTERN}'
    rf'|{SINGLE_CHAR_PATTERN}'
)


def tokenize_java_code(text: str, preserve_comments: bool = False) -> List[str]:
    """
    Lexically tokenizes a raw Java source code string.

    Parameters:
        text: Java source code text.
        preserve_comments: If False (default), comments are skipped during tokenization.
                           If True, comment tokens are retained in the output.

    Returns:
        List of lexical tokens preserving source sequence order.
    """
    if not text or not isinstance(text, str):
        return []

    tokens: List[str] = []
    for m in JAVA_LEXICAL_REGEX.finditer(text):
        tok = m.group(0)
        if tok.startswith('//') or tok.startswith('/*'):
            if preserve_comments:
                tokens.append(tok)
        else:
            tokens.append(tok)

    return tokens


def tokenize_source(obj: Any, preserve_comments: bool = False) -> List[str]:
    """
    Converts a Java source input into a deterministic lexical token sequence.

    Handles:
      1. Raw Java source code string: directly tokenized via tokenize_java_code.
      2. Structured Static Program Representation (SPR dict/list): recursively
         extracts keys and values and tokenizes them with full operator/delimiter fidelity.
      3. Empty / None input: returns [].

    Parameters:
        obj: str, dict, list, or None.
        preserve_comments: Whether comments in raw code should be retained.

    Returns:
        Deterministic list of string tokens.
    """
    if obj is None:
        return []

    if isinstance(obj, str):
        return tokenize_java_code(obj, preserve_comments=preserve_comments)

    tokens: List[str] = []

    def process(value: Any):
        if isinstance(value, dict):
            for key, val in value.items():
                tokens.append(str(key))
                process(val)
        elif isinstance(value, list):
            for item in value:
                process(item)
        elif value is not None:
            text = str(value)
            parts = tokenize_java_code(text, preserve_comments=preserve_comments)
            tokens.extend(parts)

    process(obj)
    return tokens

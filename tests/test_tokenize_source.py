"""
Unit and regression tests for Java Source Tokenizer (Module 4, Part 4).
"""
import unittest
from source_tokenizer import (
    tokenize_source,
    tokenize_java_code,
    JAVA_LEXICAL_REGEX,
    COMMENT_REGEX
)


class TestTokenizeSource(unittest.TestCase):

    def test_1_normal_java_class(self):
        """Tokenizes a complete Java class with package, imports, fields, and methods."""
        code = """
        package com.example.math;
        import java.util.Scanner;

        public class Calculator {
            private int accumulator = 0;

            public int add(int a, int b) {
                return a + b;
            }
        }
        """
        expected = [
            "package", "com", ".", "example", ".", "math", ";",
            "import", "java", ".", "util", ".", "Scanner", ";",
            "public", "class", "Calculator", "{",
            "private", "int", "accumulator", "=", "0", ";",
            "public", "int", "add", "(", "int", "a", ",", "int", "b", ")", "{",
            "return", "a", "+", "b", ";",
            "}",
            "}"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_2_keywords_and_identifiers(self):
        """Handles Java keywords, standard identifiers, and currency symbols."""
        code = "public abstract class $MyClass_123 extends Thread implements Runnable"
        expected = [
            "public", "abstract", "class", "$MyClass_123", "extends", "Thread",
            "implements", "Runnable"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_3_qualified_names(self):
        """Qualified names and package access are separated by dot delimiters."""
        code = "java.lang.System.out.println(Math.PI);"
        expected = [
            "java", ".", "lang", ".", "System", ".", "out", ".", "println",
            "(", "Math", ".", "PI", ")", ";"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_4_multi_character_operators(self):
        """Preserves all Java multi-character operators without splitting."""
        code = "a == b != c <= d >= e && f || g ++ h -- i -> j :: k += l -= m *= n /= o %= p >>= q <<= r >>>= s"
        expected = [
            "a", "==", "b", "!=", "c", "<=", "d", ">=", "e", "&&", "f", "||",
            "g", "++", "h", "--", "i", "->", "j", "::", "k", "+=", "l", "-=",
            "m", "*=", "n", "/=", "o", "%=", "p", ">>=", "q", "<<=", "r", ">>>=", "s"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_5_string_and_character_literals_with_escapes(self):
        """Handles string and character literals with embedded escaped quotes, newlines, and backslashes."""
        code = '''String s = "Hello \\"World\\" \\\\ \\n"; char c = '\\''; char nl = '\\n';'''
        expected = [
            "String", "s", "=", '"Hello \\"World\\" \\\\ \\n"', ";",
            "char", "c", "=", "'\\''", ";",
            "char", "nl", "=", "'\\n'", ";"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_6_numeric_literals_comprehensive(self):
        """Tokenizes decimal, hex, binary, floating-point, exponents, underscores, and suffixes."""
        code = "int a = 123; long b = 100_000L; double c = 3.14159f; double d = 1.0e-5; int hex = 0xAF_01L; int bin = 0b1010_1100;"
        expected = [
            "int", "a", "=", "123", ";",
            "long", "b", "=", "100_000L", ";",
            "double", "c", "=", "3.14159f", ";",
            "double", "d", "=", "1.0e-5", ";",
            "int", "hex", "=", "0xAF_01L", ";",
            "int", "bin", "=", "0b1010_1100", ";"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_7_comments_policy_stripped_by_default(self):
        """Comments are stripped by default to focus on executable program semantics."""
        code = """
        // This is a line comment
        int x = 1; /* inline block comment */
        /*
         * Multi-line block comment
         */
        int y = 2; // trailing comment
        """
        expected = ["int", "x", "=", "1", ";", "int", "y", "=", "2", ";"]
        tokens = tokenize_java_code(code, preserve_comments=False)
        self.assertEqual(tokens, expected)

    def test_7b_comments_policy_preserved_when_requested(self):
        """Comments can be preserved if requested via preserve_comments=True."""
        code = "int x = 1; // note\nint y = 2;"
        tokens = tokenize_java_code(code, preserve_comments=True)
        self.assertIn("// note", tokens)
        self.assertIn("x", tokens)
        self.assertIn("y", tokens)

    def test_8_nested_delimiters_and_punctuation(self):
        """Preserves nested parentheses, braces, brackets, and generic type delimiters."""
        code = "Map<String, List<Integer>> map = new HashMap<>(); int[] arr = new int[((1 + 2) * 3)];"
        # NOTE: A pure lexical tokenizer matches ">>" as a right-shift operator.
        # Java resolves ">>" vs "> >" in generics at the parser level, not the lexer level.
        expected = [
            "Map", "<", "String", ",", "List", "<", "Integer", ">>",
            "map", "=", "new", "HashMap", "<", ">", "(", ")", ";",
            "int", "[", "]", "arr", "=", "new", "int", "[",
            "(", "(", "1", "+", "2", ")", "*", "3", ")", "]", ";"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_9_unicode_input(self):
        """Handles Unicode identifiers and non-ASCII characters gracefully."""
        code = 'String greeting = "こんにちは"; int variable_α = 1;'
        expected = [
            "String", "greeting", "=", '"こんにちは"', ";",
            "int", "variable_α", "=", "1", ";"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_10_deterministic_output(self):
        """Tokenization is strictly deterministic across repeated invocations."""
        code = "public void test() { int a = 1; int b = 2; System.out.println(a + b); }"
        t1 = tokenize_java_code(code)
        t2 = tokenize_java_code(code)
        self.assertEqual(t1, t2)

    def test_11_empty_and_whitespace_only_source(self):
        """Empty or whitespace-only source returns an empty token list."""
        self.assertEqual(tokenize_java_code(""), [])
        self.assertEqual(tokenize_java_code("   \n\t  \r\n  "), [])
        self.assertEqual(tokenize_source(None), [])
        self.assertEqual(tokenize_source(""), [])
        self.assertEqual(tokenize_source({}), [])

    def test_12_structured_spr_input(self):
        """Tokenizes structured SPR dictionaries and lists preserving keys and values."""
        spr = {
            "program_id": "p00001",
            "classes": [{"name": "Main", "methods": [{"name": "main", "return_type": "void"}]}],
            "variables": [{"name": "x", "type": "int"}]
        }
        tokens = tokenize_source(spr)
        self.assertIn("program_id", tokens)
        self.assertIn("p00001", tokens)
        self.assertIn("classes", tokens)
        self.assertIn("Main", tokens)
        self.assertIn("methods", tokens)
        self.assertIn("main", tokens)
        self.assertIn("variables", tokens)
        self.assertIn("int", tokens)

    def test_13_incomplete_and_invalid_source_behavior(self):
        """Incomplete or invalid syntax tokenizes available tokens without crashing."""
        broken_code = "public class { int x = ; // unclosed string: \"broken"
        # Does not raise an unhandled exception, tokenizes valid prefixes
        tokens = tokenize_java_code(broken_code)
        self.assertIn("public", tokens)
        self.assertIn("class", tokens)
        self.assertIn("{", tokens)
        self.assertIn("int", tokens)
        self.assertIn("x", tokens)
        self.assertIn("=", tokens)
        self.assertIn(";", tokens)

    def test_14_ternary_and_bitwise_operators(self):
        """Handles ternary ? : and bitwise operators & | ^ ~."""
        code = "int r = (a & b | ~c ^ d) ? 1 : 0;"
        expected = [
            "int", "r", "=", "(", "a", "&", "b", "|", "~", "c", "^", "d", ")",
            "?", "1", ":", "0", ";"
        ]
        tokens = tokenize_java_code(code)
        self.assertEqual(tokens, expected)

    def test_15_string_containing_url_with_slashes(self):
        """String literals containing // (such as URLs) must not be corrupted by line comment stripping."""
        code = 'String url = "http://example.com";'
        expected = ["String", "url", "=", '"http://example.com"', ";"]
        self.assertEqual(tokenize_java_code(code), expected)

    def test_16_string_containing_block_comment_markers(self):
        """String literals containing /* ... */ must not be stripped as block comments."""
        code = 'String marker = "/* not a comment */";'
        expected = ["String", "marker", "=", '"/* not a comment */"', ";"]
        self.assertEqual(tokenize_java_code(code), expected)

    def test_17_string_with_escaped_quotes_and_comment_text(self):
        """String literals with escaped quotes followed by comment markers are preserved losslessly."""
        code = 'String escaped = "quote: \\" and // text";'
        expected = ["String", "escaped", "=", '"quote: \\" and // text"', ";"]
        self.assertEqual(tokenize_java_code(code), expected)

    def test_18_char_literal_slash(self):
        """Character literal '/' is preserved and not mistaken for comment start."""
        code = "char slash = '/';"
        expected = ["char", "slash", "=", "'/'", ";"]
        self.assertEqual(tokenize_java_code(code), expected)

    def test_19_multiline_block_comment_between_tokens(self):
        """Multiline block comments between tokens are cleanly skipped without token concatenation."""
        code = "int a = 1; /*\n * multi\n * line\n */ int b = 2;"
        expected = ["int", "a", "=", "1", ";", "int", "b", "=", "2", ";"]
        self.assertEqual(tokenize_java_code(code, preserve_comments=False), expected)

    def test_20_line_comment_ending_at_newline(self):
        """Line comment ending at newline is skipped without dropping subsequent tokens."""
        code = "int a = 1; // comment\nint b = 2;"
        expected = ["int", "a", "=", "1", ";", "int", "b", "=", "2", ";"]
        self.assertEqual(tokenize_java_code(code, preserve_comments=False), expected)

    def test_21_comments_preserved_when_requested(self):
        """Comments are retained as distinct tokens when preserve_comments=True."""
        code = 'String s = "text"; // note\n/* block */ int x = 0;'
        tokens = tokenize_java_code(code, preserve_comments=True)
        self.assertIn("// note", tokens)
        self.assertIn("/* block */", tokens)
        self.assertIn('"text"', tokens)
        self.assertIn("x", tokens)

    def test_22_varargs_ellipsis_token(self):
        """Java varargs ellipsis '...' is tokenized as a single atomic token."""
        code = "void f(int... values) {}"
        expected = ["void", "f", "(", "int", "...", "values", ")", "{", "}"]
        self.assertEqual(tokenize_java_code(code), expected)

    def test_23_leading_dot_float_literals(self):
        """Leading-dot floating point literals (.5, .5f, .5e2) are recognized as numeric tokens."""
        code = "double a = .5; float b = .5f; double c = .5e2; double d = .5e-2f;"
        expected = [
            "double", "a", "=", ".5", ";",
            "float", "b", "=", ".5f", ";",
            "double", "c", "=", ".5e2", ";",
            "double", "d", "=", ".5e-2f", ";"
        ]
        self.assertEqual(tokenize_java_code(code), expected)

    def test_24_overlapping_operators_and_dot_punctuation(self):
        """Overlapping multi-character operators and dot member accesses maintain correct boundaries."""
        code = "obj.field; a...b; a >>>= b; a >>> b; a >>= b; a >> b; a <<= b; a << b; a >= b; a > b;"
        expected = [
            "obj", ".", "field", ";",
            "a", "...", "b", ";",
            "a", ">>>=", "b", ";",
            "a", ">>>", "b", ";",
            "a", ">>=", "b", ";",
            "a", ">>", "b", ";",
            "a", "<<=", "b", ";",
            "a", "<<", "b", ";",
            "a", ">=", "b", ";",
            "a", ">", "b", ";"
        ]
        self.assertEqual(tokenize_java_code(code), expected)


if __name__ == '__main__':
    unittest.main()

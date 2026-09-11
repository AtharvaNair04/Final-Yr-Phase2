import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.NoSuchElementException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author Egor Kulikov (egor@egork.net)
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        TaskE solver = new TaskE();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskE {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int n = in.readInt();
            int k = in.readInt();
            String s = in.readString();
            if (k >= 14) {
                char c = new CharArray(s.toCharArray()).min();
                for (int i = 0; i < n; i++) {
                    out.print(c);
                }
                out.printLine();
                return;
            }
            s = s + StringUtils.reverse(s);
            int[] order = StringUtils.suffixArray(s);
            order = ArrayUtils.reversePermutation(order);
            int start = -1;
            for (int i = 0; i < order.length; i++) {
                if (order[i] <= n) {
                    start = order[i];
                    break;
                }
            }
            int qty = 1;
            char c = s.charAt(start);
            for (int i = 1; i < n; i++) {
                if (s.charAt(i + start) == c) {
                    qty++;
                } else {
                    break;
                }
            }
            start += qty;
            qty *= 1 << (k - 1);
            for (int i = 0; i < n && i < qty; i++) {
                out.print(c);
            }
            if (qty < n) {
                out.printLine(s.substring(start, start + n - qty));
            } else {
                out.printLine();
            }
        }

    }

    static class StringUtils {
        public static String reverse(String sample) {
            StringBuilder result = new StringBuilder(sample);
            result.reverse();
            return result.toString();
        }

        public static int[] suffixArray(CharSequence s) {
            int length = s.length();
            int[] position = new int[length];
            int[] count = new int[Math.max(256, length)];
            int[] order = new int[length];
            for (int i = 0; i < length; ++i) {
                count[s.charAt(i)]++;
            }
            for (int i = 1; i < 256; ++i) {
                count[i] += count[i - 1];
            }
            for (int i = 0; i < length; ++i) {
                position[--count[s.charAt(i)]] = i;
            }
            order[position[0]] = 0;
            int currentClass = 0;
            for (int i = 1; i < length; ++i) {
                if (s.charAt(position[i]) != s.charAt(position[i - 1])) {
                    currentClass++;
                }
                order[position[i]] = currentClass;
            }
            int[] nextPosition = new int[length];
            int[] nextOrder = new int[length];
            for (int h = 0; (1 << h) < length; h++) {
                for (int i = 0; i < length; ++i) {
                    nextPosition[i] = position[i] - (1 << h);
                    if (nextPosition[i] < 0) {
                        nextPosition[i] += length;
                    }
                }
                Arrays.fill(count, 0);
                for (int i = 0; i < length; ++i) {
                    count[order[nextPosition[i]]]++;
                }
                for (int i = 1; i <= currentClass; ++i) {
                    count[i] += count[i - 1];
                }
                for (int i = length - 1; i >= 0; --i) {
                    position[--count[order[nextPosition[i]]]] = nextPosition[i];
                }
                nextOrder[position[0]] = 0;
                currentClass = 0;
                for (int i = 1; i < length; ++i) {
                    int mid1 = (position[i] + (1 << h));
                    if (mid1 >= length) {
                        mid1 -= length;
                    }
                    int mid2 = (position[i - 1] + (1 << h));
                    if (mid2 >= length) {
                        mid2 -= length;
                    }
                    if (order[position[i]] != order[position[i - 1]] || order[mid1] != order[mid2]) {
                        currentClass++;
                    }
                    nextOrder[position[i]] = currentClass;
                }
                System.arraycopy(nextOrder, 0, order, 0, length);
            }
            return order;
        }

    }

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void print(Object... objects) {
            for (int i = 0; i < objects.length; i++) {
                if (i != 0) {
                    writer.print(' ');
                }
                writer.print(objects[i]);
            }
        }

        public void printLine() {
            writer.println();
        }

        public void printLine(Object... objects) {
            print(objects);
            writer.println();
        }

        public void print(char i) {
            writer.print(i);
        }

        public void close() {
            writer.close();
        }

    }

    static abstract class CharAbstractStream implements CharStream {
        public String toString() {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (CharIterator it = charIterator(); it.isValid(); it.advance()) {
                if (first) {
                    first = false;
                } else {
                    builder.append(' ');
                }
                builder.append(it.value());
            }
            return builder.toString();
        }

        public boolean equals(Object o) {
            if (!(o instanceof CharStream)) {
                return false;
            }
            CharStream c = (CharStream) o;
            CharIterator it = charIterator();
            CharIterator jt = c.charIterator();
            while (it.isValid() && jt.isValid()) {
                if (it.value() != jt.value()) {
                    return false;
                }
                it.advance();
                jt.advance();
            }
            return !it.isValid() && !jt.isValid();
        }

        public int hashCode() {
            int result = 0;
            for (CharIterator it = charIterator(); it.isValid(); it.advance()) {
                result *= 31;
                result += it.value();
            }
            return result;
        }

    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader.SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int readInt() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String readString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                if (Character.isValidCodePoint(c)) {
                    res.appendCodePoint(c);
                }
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }

    static interface CharIterator {
        public char value() throws NoSuchElementException;

        public boolean advance();

        public boolean isValid();

    }

    static interface CharStream extends Iterable<Character>, Comparable<CharStream> {
        public CharIterator charIterator();

        default public Iterator<Character> iterator() {
            return new Iterator<Character>() {
                private CharIterator it = charIterator();

                public boolean hasNext() {
                    return it.isValid();
                }

                public Character next() {
                    char result = it.value();
                    it.advance();
                    return result;
                }
            };
        }

        default public int compareTo(CharStream c) {
            CharIterator it = charIterator();
            CharIterator jt = c.charIterator();
            while (it.isValid() && jt.isValid()) {
                char i = it.value();
                char j = jt.value();
                if (i < j) {
                    return -1;
                } else if (i > j) {
                    return 1;
                }
                it.advance();
                jt.advance();
            }
            if (it.isValid()) {
                return 1;
            }
            if (jt.isValid()) {
                return -1;
            }
            return 0;
        }

        default public char min() {
            char result = Character.MAX_VALUE;
            for (CharIterator it = charIterator(); it.isValid(); it.advance()) {
                char current = it.value();
                if (current < result) {
                    result = current;
                }
            }
            return result;
        }

    }

    static class CharArray extends CharAbstractStream implements CharList {
        private char[] data;

        public CharArray(char[] arr) {
            data = arr;
        }

        public int size() {
            return data.length;
        }

        public char get(int at) {
            return data[at];
        }

        public void removeAt(int index) {
            throw new UnsupportedOperationException();
        }

    }

    static class ArrayUtils {
        public static int[] reversePermutation(int[] permutation) {
            int[] result = new int[permutation.length];
            for (int i = 0; i < permutation.length; i++) {
                result[permutation[i]] = i;
            }
            return result;
        }

    }

    static interface CharCollection extends CharStream {
        public int size();

    }

    static interface CharReversableCollection extends CharCollection {
    }

    static interface CharList extends CharReversableCollection {
        public abstract char get(int index);

        public abstract void removeAt(int index);

        default public CharIterator charIterator() {
            return new CharIterator() {
                private int at;
                private boolean removed;

                public char value() {
                    if (removed) {
                        throw new IllegalStateException();
                    }
                    return get(at);
                }

                public boolean advance() {
                    at++;
                    removed = false;
                    return isValid();
                }

                public boolean isValid() {
                    return !removed && at < size();
                }

                public void remove() {
                    removeAt(at);
                    at--;
                    removed = true;
                }
            };
        }

    }
}

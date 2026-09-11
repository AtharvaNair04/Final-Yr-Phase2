import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.Objects;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        TaskD solver = new TaskD();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskD {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int n = in.readInt();
            int m = in.readInt();
            int[] a = in.readIntArray(m);

            int[] cost = {0, 2, 5, 5, 4, 5, 6, 3, 7, 6};

            int[] cand = new int[8];
            for (int i = 0; i < m; i++) {
                cand[cost[a[i]]] = Math.max(cand[cost[a[i]]], a[i]);
            }
            for (int i = 2; i < cand.length; i++) {
                if (cand[i] == 0) continue;
                for (int j = 2 * i; j < cand.length; j += i) {
                    cand[j] = 0;
                }
            }
            a = IntStream.of(cand).filter(x -> x != 0).toArray();
            m = a.length;

            LongInteger[] dp = new LongInteger[n + 1];
            dp[0] = LongInteger.ZERO;
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < m; j++) {
                    if (i - cost[a[j]] >= 0 && dp[i - cost[a[j]]] != null) {
                        LongInteger tmp = new LongInteger(dp[i - cost[a[j]]].toString() + (char) ('0' + a[j]));
                        if (dp[i] == null || dp[i].compareTo(tmp) < 0) {
                            dp[i] = tmp;
                        }
                    }
                }
            }

            out.printLine(dp[n]);
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

        public int[] readIntArray(int size) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = readInt();
            }
            return array;
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

    static class LongInteger implements Cloneable, Comparable<LongInteger> {
        public static final LongInteger ZERO = new LongInteger("0");
        private int length;
        private int[] digits;
        private int sign;

        private LongInteger(int length, int[] digits, int sign) {
            this.length = length;
            this.digits = digits;
            this.sign = sign;
        }

        public LongInteger(String s) {
            if (s.isEmpty()) {
                throw new NumberFormatException();
            }
            int from = 0;
            int to = s.length();
            if (s.charAt(0) == '-') {
                sign = -1;
                from = 1;
            } else {
                sign = 1;
                if (s.charAt(0) == '+') {
                    from = 1;
                }
            }
            if (from == to) {
                throw new NumberFormatException();
            }
            length = (to - from + 2) / 3;
            digits = new int[length];
            for (int i = 0; i < length; i++) {
                int start = Math.max(to - 3, from);
                for (int j = start; j < to; j++) {
                    digits[i] *= 10;
                    char c = s.charAt(j);
                    if (!Character.isDigit(c)) {
                        throw new NumberFormatException();
                    }
                    digits[i] += c - '0';
                }
                to = start;
            }
            if (length == 1 && digits[0] == 0) {
                sign = 0;
            }
        }

        public static int absCompare(LongInteger a, LongInteger b) {
            if (a.length != b.length) {
                return Integer.compare(a.length, b.length);
            }
            for (int i = a.length - 1; i >= 0; i--) {
                if (a.digits[i] != b.digits[i]) {
                    return Integer.compare(a.digits[i], b.digits[i]);
                }
            }
            return 0;
        }

        public static int compare(LongInteger a, LongInteger b) {
            if (a.sign != b.sign) {
                return Integer.compare(a.sign, b.sign);
            }
            return absCompare(a, b);
        }

        public LongInteger clone() {
            return new LongInteger(length, digits.clone(), sign);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            LongInteger that = (LongInteger) o;
            return length == that.length &&
                    sign == that.sign &&
                    equals(digits, that.digits, length);
        }

        private static boolean equals(int[] a, int[] b, int length) {
            for (int i = 0; i < length; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            int result = Objects.hash(length, sign);
            result = 31 * result + hashCode(digits, length);
            return result;
        }

        private static int hashCode(int[] digits, int length) {
            int result = 1;
            for (int i = 0; i < length; i++) {
                result = 31 * result + digits[i];
            }
            return result;
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            if (sign < 0) {
                result.append('-');
            }
            result.append(digits[length - 1]);
            for (int i = length - 2; i >= 0; i--) {
                int current = digits[i];
//            if (current < 1000) {
//                result.append('0');
                if (current < 100) {
                    result.append('0');
                    if (current < 10) {
                        result.append('0');
                    }
//                }
                }
                result.append(current);
            }
            return result.toString();
        }

        public int compareTo(LongInteger o) {
            return compare(this, o);
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

        public void printLine(Object... objects) {
            print(objects);
            writer.println();
        }

        public void close() {
            writer.close();
        }

    }
}

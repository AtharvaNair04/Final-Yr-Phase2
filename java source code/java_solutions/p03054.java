import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.InputMismatchException;
import java.io.IOException;
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
        BLRUDGame solver = new BLRUDGame();
        solver.solve(1, in, out);
        out.close();
    }

    static class BLRUDGame {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int h = in.readInt();
            int w = in.readInt();
            int n = in.readInt();
            int sr = in.readInt();
            int sc = in.readInt();
            String s = in.readString();
            String t = in.readString();

            {
                int[] a = new int[n];
                int[] b = new int[n];
                for (int i = 0; i < n; i++) {
                    if (s.charAt(i) == 'R') {
                        a[i] = 1;
                    } else if (s.charAt(i) == 'L') {
                        a[i] = -1;
                    }
                    if (t.charAt(i) == 'R') {
                        b[i] = 1;
                    } else if (t.charAt(i) == 'L') {
                        b[i] = -1;
                    }
                }
                if (!f(w, sc, a, b)) {
                    out.printLine("NO");
                    return;
                }
            }
            {
                int[] a = new int[n];
                int[] b = new int[n];
                for (int i = 0; i < n; i++) {
                    if (s.charAt(i) == 'D') {
                        a[i] = 1;
                    } else if (s.charAt(i) == 'U') {
                        a[i] = -1;
                    }
                    if (t.charAt(i) == 'D') {
                        b[i] = 1;
                    } else if (t.charAt(i) == 'U') {
                        b[i] = -1;
                    }
                }
                if (!f(h, sr, a, b)) {
                    out.printLine("NO");
                    return;
                }
            }
            out.printLine("YES");
        }

        boolean f(int l, int s, int[] a, int[] b) {
            int n = a.length;
            int min = 1;
            int max = l;
            for (int i = 0; i < n; i++) {
                if (b[n - 1 - i] == 1) {
                    min = Math.max(min - 1, 1);
                } else if (b[n - 1 - i] == -1) {
                    max = Math.min(max + 1, l);
                }
                if (a[n - 1 - i] == 1) {
                    max--;
                } else if (a[n - 1 - i] == -1) {
                    min++;
                }
                if (min > max) return false;
            }
            return min <= s && s <= max;
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
}

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;
import java.io.IOException;
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
        EMajorityOfBalls solver = new EMajorityOfBalls();
        solver.solve(1, in, out);
        out.close();
    }

    static class EMajorityOfBalls {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int n = in.readInt();

            int[] a1 = new int[n];
            for (int j = 0; j < n; j++) {
                a1[j] = j + 1;
            }
            out.print("? ");
            out.printLine(a1);
            out.flush();
            String s1 = in.readString();
            boolean now1 = s1.equals("Red");

            int high = n;
            int low = 0;
            while (high - low > 1) {
                int mid = (high + low) / 2;
                int[] a = new int[n];
                for (int j = 0; j < n; j++) {
                    a[j] = mid + j + 1;
                }
                out.print("? ");
                out.printLine(a);
                out.flush();

                String s = in.readString();
                boolean now = s.equals("Red");
                if (now == now1) low = mid;
                else high = mid;
            }

            boolean[] ans = new boolean[2 * n];
            ans[high - 1] = now1;
            ans[high - 1 + n] = !now1;

            for (int i = 0; i < n - 1; i++) {
                int[] a = new int[n];
                for (int j = 0; j < n - 1; j++) {
                    a[j] = high + 1 + j;
                }
                a[n - 1] = (high + n + i) % (2 * n) + 1;
                out.print("? ");
                out.printLine(a);
                out.flush();

                String s = in.readString();
                boolean now = s.equals("Red");
                ans[(high + n + i) % (2 * n)] = now;
            }

            for (int i = 0; i < n - 1; i++) {
                int[] a = new int[n];
                for (int j = 0; j < n - 1; j++) {
                    a[j] = (high + n + j) % (2 * n) + 1;
                }
                a[n - 1] = (high + i) % (2 * n) + 1;
                out.print("? ");
                out.printLine(a);
                out.flush();

                String s = in.readString();
                boolean now = s.equals("Red");
                ans[high + i] = now;
            }

            out.print("! ");
            for (int i = 0; i < 2 * n; i++) {
                out.print(ans[i] ? "R" : "B");
            }
            out.printLine();
            out.flush();
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

        public void print(int[] array) {
            for (int i = 0; i < array.length; i++) {
                if (i != 0) {
                    writer.print(' ');
                }
                writer.print(array[i]);
            }
        }

        public void printLine(int[] array) {
            print(array);
            writer.println();
        }

        public void printLine() {
            writer.println();
        }

        public void close() {
            writer.close();
        }

        public void flush() {
            writer.flush();
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

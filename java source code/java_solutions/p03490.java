import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.BitSet;
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
        DFTRobot solver = new DFTRobot();
        solver.solve(1, in, out);
        out.close();
    }

    static class DFTRobot {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            final int MAX = 16010;
            String s = in.readString();
            int n = s.length();
            int x = in.readInt() + MAX / 2;
            int y = in.readInt() + MAX / 2;

            int i = 0;
            for (; i < n; i++) {
                if (s.charAt(i) == 'T') break;
                x--;
            }
            List<Integer> yList = new ArrayList<>();
            List<Integer> xList = new ArrayList<>();
            int tCnt = 0;
            for (; i < n; i++) {
                if (s.charAt(i) == 'T') {
                    tCnt++;
                } else {
                    int j = i;
                    while (j < n - 1 && s.charAt(j + 1) == 'F') j++;
                    if (tCnt % 2 == 1) yList.add(j - i + 1);
                    else xList.add(j - i + 1);
                    i = j;
                }
            }

            BitSet[] can = new BitSet[2];
            for (int j = 0; j < 2; j++) can[j] = new BitSet(MAX);

            int cur = 0;
            can[cur].set(MAX / 2);
            for (int j = 0; j < yList.size(); j++) {
                int next = 1 - cur;
                can[next].clear();
                for (int k = 0; k < MAX; k++) {
                    if (can[cur].get(k)) {
                        can[next].set(k + yList.get(j));
                        can[next].set(k - yList.get(j));
                    }
                }
                cur = next;
            }
            if (!can[cur].get(y)) {
                out.printLine("No");
                return;
            }

            cur = 0;
            can[cur].clear();
            can[cur].set(MAX / 2);
            for (int j = 0; j < xList.size(); j++) {
                int next = 1 - cur;
                can[next].clear();
                for (int k = 0; k < MAX; k++) {
                    if (can[cur].get(k)) {
                        can[next].set(k + xList.get(j));
                        can[next].set(k - xList.get(j));
                    }
                }
                cur = next;
            }
            out.printLine(x >= 0 && can[cur].get(x) ? "Yes" : "No");
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

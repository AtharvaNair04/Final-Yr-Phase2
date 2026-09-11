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
 *
 * @author Egor Kulikov (egor@egork.net)
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        TaskC solver = new TaskC();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskC {
        int[] queue = new int[100_000_000];
        boolean[] inQueue;
        int size = 0;
        int n;
        int[] a;
        int[] b;

        public void solve(int testNumber, InputReader in, OutputWriter out) {
            n = in.readInt();
            a = in.readIntArray(n);
            b = in.readIntArray(n);
            inQueue = new boolean[n];
            for (int i = 0; i < n; i++) {
                tryAdd(i);
            }
            long answer = 0;
            for (int j = 0; j < size; j++) {
                int i = queue[j];
                inQueue[i] = false;
                int last = i == 0 ? n - 1 : i - 1;
                int next = i == n - 1 ? 0 : i + 1;
                int delta = b[i] - a[i];
                int dif = b[last] + b[next];
                int times = delta / dif;
                answer += times;
                b[i] -= dif * times;
                tryAdd(last);
                tryAdd(next);
            }
            for (int i = 0; i < n; i++) {
                if (a[i] != b[i]) {
                    out.printLine(-1);
                    return;
                }
            }
            out.printLine(answer);
        }

        void tryAdd(int i) {
            if (inQueue[i]) {
                return;
            }
            int last = i == 0 ? n - 1 : i - 1;
            int next = i == n - 1 ? 0 : i + 1;
            if (b[i] - a[i] >= b[last] + b[next]) {
                queue[size++] = i;
                inQueue[i] = true;
            }
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

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void close() {
            writer.close();
        }

        public void printLine(long i) {
            writer.println(i);
        }

        public void printLine(int i) {
            writer.println(i);
        }

    }
}

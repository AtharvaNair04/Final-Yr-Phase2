import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 27);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            TaskC solver = new TaskC();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskC {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int a = in.readInt();
            int b = in.readInt();

            int[][] mat = new int[n][m];
            if (m % 2 == 1) {
                for (int i = 0; i < n - 1 && b > 0; i += 2) {
                    if (n % 2 == 1 && i == 2) {
                        i = 3;
                    }
                    mat[i][0] = '^';
                    mat[i + 1][0] = 'v';
                    b--;
                }
            }
            if (n % 2 == 1) {
                for (int i = m % 2; i < m - 1 && a > 0; i += 2) {
                    mat[0][i] = '<';
                    mat[0][i + 1] = '>';
                    a--;
                }
            }

            for (int i = n % 2; i < n - 1; i += 2) {
                for (int j = m % 2; j < m - 1; j += 2) {
                    if (i == 1 && j == 1 && a % 2 == 1 && b % 2 == 1) {
                        mat[i + 1][j - 1] = '<';
                        mat[i + 1][j] = '>';
                        mat[i][j + 1] = '^';
                        mat[i + 1][j + 1] = 'v';
                        a--;
                        b--;
                    } else if (a > 0) {
                        a--;
                        mat[i][j] = '<';
                        mat[i][j + 1] = '>';
                        if (a > 0) {
                            a--;
                            mat[i + 1][j] = '<';
                            mat[i + 1][j + 1] = '>';
                        }
                    } else if (b > 0) {
                        b--;
                        mat[i][j] = '^';
                        mat[i + 1][j] = 'v';
                        if (b > 0) {
                            b--;
                            mat[i][j + 1] = '^';
                            mat[i + 1][j + 1] = 'v';
                        }
                    }
                }
            }

            if (a + b != 0) {
                out.println("NO");
                return;
            }

            out.println("YES");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    out.append((char) (mat[i][j] == 0 ? '.' : mat[i][j]));
                }
                out.println();
            }
        }

    }
    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 13];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastInput(InputStream is) {
            this.is = is;
        }

        private int read() {
            while (bufLen == bufOffset) {
                bufOffset = 0;
                try {
                    bufLen = is.read(buf);
                } catch (IOException e) {
                    bufLen = -1;
                }
                if (bufLen == -1) {
                    return -1;
                }
            }
            return buf[bufOffset++];
        }

        public void skipBlank() {
            while (next >= 0 && next <= 32) {
                next = read();
            }
        }

        public int readInt() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            int val = 0;
            if (sign == 1) {
                while (next >= '0' && next <= '9') {
                    val = val * 10 + next - '0';
                    next = read();
                }
            } else {
                while (next >= '0' && next <= '9') {
                    val = val * 10 - next + '0';
                    next = read();
                }
            }

            return val;
        }

    }
    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput println() {
            cache.append('\n');
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
                os.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }
}

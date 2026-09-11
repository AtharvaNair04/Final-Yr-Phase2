import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.Objects;
import java.util.Map;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author prakharjain
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        FSquare solver = new FSquare();
        solver.solve(1, in, out);
        out.close();
    }

    static class FSquare {
        int mod = 998244353;

        public void solve(int testNumber, InputReader in, OutputWriter out) {
            long n = in.nextInt();

            int ni = (int) n;

            int m = in.nextInt();

            long extra = (n * n - n - 2 * (n - 1) - 2 * (n - 2)) / 2;

            Map<pair, Integer> pairs = new HashMap<>();
            Map<pair, Integer> expairs = new HashMap<>();
            for (int i = 0; i < m; i++) {
                int x = in.nextInt() - 1;
                int y = in.nextInt() - 1;
                int c = in.nextInt();

                if (Math.abs(x - y) < 3) {
                    pairs.put(new pair(x, y), c);
                } else {
                    pair cp = new pair(x, y);
                    pair rp = new pair(y, x);
                    if (expairs.containsKey(rp)) {
                        int cr = expairs.get(rp);

                        if (cr != c) {
                            out.println(0);
                            return;
                        }
                    } else
                        extra--;
                    expairs.put(cp, c);
                }
            }

            long ans = pow(2, extra, mod);

            long[][] dp = new long[ni][8];

            for (int i = 0; i < 8; i++) {
                if (isp(i, ni - 1, pairs)) {
                    dp[ni - 1][i] += 1;
                }
            }

            for (int j = ni - 2; j >= 1; j--) {
                pair pair0 = new pair(j - 1, j + 1);
                pair pair1 = new pair(j + 1, j - 1);
                for (int k = 0; k < 8; k++) {
                    int popCnt = Integer.bitCount(k);
                    long pa = 1;
                    if (pairs.containsKey(pair0) && pairs.containsKey(pair1)) {
                        int val1 = pairs.get(pair0);
                        int val2 = pairs.get(pair1);

                        if ((val1 + val2) % 2 == 0 && popCnt % 2 == 1) {
                            continue;
                        } else if ((val1 + val2) % 2 == 0 && popCnt % 2 == 0) {

                        } else if ((val1 + val2) % 2 == 1 && popCnt % 2 == 1) {

                        } else if ((val1 + val2) % 2 == 1 && popCnt % 2 == 0) {
                            continue;
                        }
                    } else if (pairs.containsKey(pair0)) {

                    } else if (pairs.containsKey(pair1)) {

                    } else {
                        pa = 2;
                    }
                    if (dp[j + 1][k] > 0) {
                        for (int l = 0; l < 8; l++) {

                            boolean isSetCenter = ((l & 2) != 0);
                            if (isp(l, j, pairs)) {
                                if ((isSetCenter && popCnt % 2 == 1) || (!isSetCenter && popCnt % 2 == 0)) {
                                    long ca = pa;
                                    dp[j][l] += (ca * dp[j + 1][k]) % mod;
                                    dp[j][l] %= mod;
                                }
                            }
                        }
                    }
                }
            }

            long fans = 0;

            pair pair0 = new pair(0, 0);

            int val = -1;

            if (pairs.containsKey(pair0)) {
                val = pairs.get(pair0);
            }

            for (int i = 0; i < 8; i++) {
                int pop = Integer.bitCount(i);

                pop %= 2;

                if (val != -1) {
                    if ((pop ^ val) == 0) {
                        fans += (ans * dp[1][i]) % mod;
                        fans %= mod;
                    }
                } else {
                    fans += (ans * dp[1][i]) % mod;
                    fans %= mod;
                }
            }

            out.println(fans);
        }

        boolean isp(int mask, int i, Map<pair, Integer> pairs) {
            pair pair0 = new pair(i, i - 1);
            pair pair1 = new pair(i, i);
            pair pair2 = new pair(i - 1, i);
            //j == 0
            if (pairs.containsKey(pair0)) {
                int val = pairs.get(pair0);
                if (val == 0 && (mask & 1) != 0)
                    return false;
                if (val != 0 && (mask & 1) == 0)
                    return false;
            }

            if (pairs.containsKey(pair1)) {
                int val = pairs.get(pair1);
                if (val == 0 && (mask & 2) != 0)
                    return false;
                if (val != 0 && (mask & 2) == 0)
                    return false;
            }

            if (pairs.containsKey(pair2)) {
                int val = pairs.get(pair2);
                if (val == 0 && (mask & 4) != 0)
                    return false;
                if (val != 0 && (mask & 4) == 0)
                    return false;
            }

            return true;
        }

        long pow(long a, long p, int mod) {
            if (p == 0) {
                return 1;
            }

            long t = pow(a, p / 2, mod);

            if (p % 2 != 0) {
                return (((t * t) % mod) * a) % mod;
            } else {
                return (t * t) % mod;
            }
        }

        class pair {
            int x;
            int y;

            public pair(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                pair pair = (pair) o;
                return x == pair.x &&
                        y == pair.y;
            }

            public int hashCode() {
                return Objects.hash(x, y);
            }

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

        public void println(long i) {
            writer.println(i);
        }

        public void println(int i) {
            writer.println(i);
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

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
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

        public int nextInt() {
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

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }
}

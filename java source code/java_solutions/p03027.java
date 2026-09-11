import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author cunbidun
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        EProductOfArithmeticProgression solver = new EProductOfArithmeticProgression();
        solver.solve(1, in, out);
        out.close();
    }

    static class EProductOfArithmeticProgression {
        private static final int MOD = (int) 1e6 + 3;
        private InputReader in;
        private PrintWriter out;

        public void solve(int testNumber, InputReader in, PrintWriter out) {
            this.in = in;
            this.out = out;
            int q = in.nextInt();

            int[] fac = new int[MOD + 1];
            int[] inv = new int[MOD + 1];
            fac[0] = 1;
            inv[0] = Op.inv(1, MOD);
            for (int i = 1; i <= MOD; i++) {
                fac[i] = Op.mulMod(fac[i - 1], i, MOD);
                inv[i] = Op.inv(fac[i], MOD);
            }

            while (q-- > 0) {
                int x = in.nextInt();
                int d = in.nextInt();
                int n = in.nextInt();
                if (d == 0) {
                    out.println(Op.powMod(x, n, MOD));
                    continue;
                }
                x = Op.mulMod(x, Op.inv(d, MOD), MOD);
                int ans;
                if (x + n - 1 >= MOD || x == 0) ans = 0;
                else
                    ans = Op.mulMod(fac[x + n - 1], inv[x - 1], MOD);
                out.println(Op.mulMod(ans, Op.powMod(d, n, MOD), MOD));
            }
        }

    }

    static class Op {
        public static int mulMod(int x, int y, int MOD) {
            return (int) (((long) x * y) % MOD);
        }

        public static int powMod(int x, long p, int MOD) {
            if (p == 0) return 1;
            int t = powMod(x, p / 2, MOD);
            if ((p & 1) == 1)
                return mulMod(mulMod(t, t, MOD), x, MOD);
            return mulMod(t, t, MOD);
        }

        public static int inv(int x, int MOD) {
            return powMod(x, MOD - 2, MOD);
        }

    }

    static class InputReader extends InputStream {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;

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

        public int nextInt() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
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

        private static boolean isSpaceChar(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

    }
}

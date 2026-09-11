import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        MyInput in = new MyInput(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        CDivisionByTwoWithSomething solver = new CDivisionByTwoWithSomething();
        solver.solve(1, in, out);
        out.close();
    }

    static class CDivisionByTwoWithSomething {
        static final int mod = 998244353;
        int n;
        char[] digits;
        long[] powmod;

        public void solve(int testNumber, MyInput in, PrintWriter out) {
//        test();

            n = in.nextInt();
            digits = in.nextChars();
            powmod = new long[n + 1];
            powmod[0] = 1;
            for (int i = 0; i < n; i++) {
                powmod[i + 1] = (powmod[i] * 2) % mod;
            }

            long m = 0;
            for (int i = 0; i < digits.length; i++) {
                m = (m * 2 + (digits[i] - '0')) % mod;
            }
            m++;
            m %= mod;

            long sum = 2L * n % mod * m % mod;
            long[] f = new long[n + 1];
            for (int k = 2; k <= n; k += 2) {
                if (n % k != 0 && n % (k / 2) == 0) {
                    long c = dp(k) - f[k];
//                dump(sum, k, c);
                    if (c < 0) c += mod;
                    if (c >= mod) c -= mod;
                    sum -= c * 2L % mod * n % mod;
                    sum += c * k % mod;
                    sum = (sum % mod + mod) % mod;
                    for (int i = 2 * k; i <= n; i += k) {
                        f[i] += c;
                        if (f[i] >= mod) f[i] -= mod;
                        if (f[i] < 0) f[i] += mod;
                    }
                }
            }

            out.println((sum % mod + mod) % mod);
        }

        long dp(int k) {
            long ans = 0;
            for (int i = 0; i < k / 2; i++) {
                if (digits[i] == '1') {
                    ans += powmod[k / 2 - 1 - i];
                    if (ans >= mod) ans -= mod;
                }
            }
//        dump(k, ans, powmod);
            LOOP:
            for (int i = k / 2; i < n; i += k / 2) {
                for (int j = 0; j < k / 2; j++) {
                    if (digits[i + j] > (digits[i + j - k / 2] ^ 1)) {
                        break LOOP;
                    }
                    if (digits[i + j] < (digits[i + j - k / 2] ^ 1)) {
                        return ans % mod;
                    }
                }
            }
            ans++;
            return ans % mod;
        }

    }

    static class MyInput {
        private final BufferedReader in;
        private static int pos;
        private static int readLen;
        private static final char[] buffer = new char[1024 * 8];
        private static char[] str = new char[500 * 8 * 2];
        private static boolean[] isDigit = new boolean[256];
        private static boolean[] isSpace = new boolean[256];
        private static boolean[] isLineSep = new boolean[256];

        static {
            for (int i = 0; i < 10; i++) {
                isDigit['0' + i] = true;
            }
            isDigit['-'] = true;
            isSpace[' '] = isSpace['\r'] = isSpace['\n'] = isSpace['\t'] = true;
            isLineSep['\r'] = isLineSep['\n'] = true;
        }

        public MyInput(InputStream is) {
            in = new BufferedReader(new InputStreamReader(is));
        }

        public int read() {
            if (pos >= readLen) {
                pos = 0;
                try {
                    readLen = in.read(buffer);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
                if (readLen <= 0) {
                    throw new MyInput.EndOfFileRuntimeException();
                }
            }
            return buffer[pos++];
        }

        public int nextInt() {
            int len = 0;
            str[len++] = nextChar();
            len = reads(len, isSpace);
            int i = 0;
            int ret = 0;
            if (str[0] == '-') {
                i = 1;
            }
            for (; i < len; i++) ret = ret * 10 + str[i] - '0';
            if (str[0] == '-') {
                ret = -ret;
            }
            return ret;
        }

        public char nextChar() {
            while (true) {
                final int c = read();
                if (!isSpace[c]) {
                    return (char) c;
                }
            }
        }

        int reads(int len, boolean[] accept) {
            try {
                while (true) {
                    final int c = read();
                    if (accept[c]) {
                        break;
                    }
                    if (str.length == len) {
                        char[] rep = new char[str.length * 3 / 2];
                        System.arraycopy(str, 0, rep, 0, str.length);
                        str = rep;
                    }
                    str[len++] = (char) c;
                }
            } catch (MyInput.EndOfFileRuntimeException e) {
            }
            return len;
        }

        public char[] nextChars() {
            int len = 0;
            str[len++] = nextChar();
            len = reads(len, isSpace);
            return Arrays.copyOf(str, len);
        }

        static class EndOfFileRuntimeException extends RuntimeException {
        }

    }
}

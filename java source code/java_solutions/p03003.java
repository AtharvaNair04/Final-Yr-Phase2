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
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        FastReader in = new FastReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        ECommonSubsequence solver = new ECommonSubsequence();
        solver.solve(1, in, out);
        out.close();
    }

    static class ECommonSubsequence {
        public void solve(int testNumber, FastReader s, PrintWriter out) {
            int n = s.nextInt();
            int m = s.nextInt();
            int[] arr = s.nextIntArray(n);
            int[] brr = s.nextIntArray(m);
            out.println(CommomSubsequencesCount(arr, brr) + 1L);

        }

        static long CommomSubsequencesCount(int[] arr, int[] brr) {
            int n1 = arr.length;
            int n2 = brr.length;
            long dp[][] = new long[n1 + 1][n2 + 1];

            for (int i = 0; i <= n1; i++) {
                for (int j = 0; j <= n2; j++) {
                    dp[i][j] = 0L;
                }
            }

            // for each character of S
            for (int i = 1; i <= n1; i++) {
                for (int j = 1; j <= n2; j++) {

                    // if character are same in both
                    // the string
                    if (arr[i - 1] == brr[j - 1])
                        dp[i][j] = (1L + dp[i][j - 1] % 1000000007 + dp[i - 1][j] % 1000000007) % 1000000007;
                    else
                        dp[i][j] = (1000000007 + (dp[i][j - 1] % 1000000007 + dp[i - 1][j] % 1000000007 - dp[i - 1][j - 1] % 1000000007)) % 1000000007;
                }
            }

            return dp[n1][n2];
        }

    }

    static class FastReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private FastReader.SpaceCharFilter filter;

        public FastReader(InputStream stream) {
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

        public int[] nextIntArray(int n) {
            int[] array = new int[n];
            for (int i = 0; i < n; ++i) array[i] = nextInt();
            return array;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }
}

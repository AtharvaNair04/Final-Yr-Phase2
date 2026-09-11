import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
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
        EGetEverything solver = new EGetEverything();
        solver.solve(1, in, out);
        out.close();
    }

    static class EGetEverything {
        public void solve(int testNumber, FastReader s, PrintWriter out) {
            int n = s.nextInt();
            int m = s.nextInt();
            int toBeDone = (1 << n) - 1;
            EGetEverything.pair[] arr = new EGetEverything.pair[m];

            for (int i = 0; i < m; i++) {
                arr[i] = new EGetEverything.pair(s.nextInt());
                int k = s.nextInt();
                for (int j = 0; j < k; j++) {
                    int pos = s.nextInt();
                    arr[i].does += (1 << (pos - 1));
                }
            }

            int[][] ans = new int[m][toBeDone + 1];
            for (int i = 0; i < m; i++) {
                Arrays.fill(ans[i], -1);
            }
            int a = func(arr, 0, 0, toBeDone, ans);
            out.println(a == Integer.MAX_VALUE ? -1 : a);

        }

        private int func(EGetEverything.pair[] arr, int pos, int ansTillNow, int toBeDone, int[][] ans) {
            if (pos == arr.length) {
                if (ansTillNow == toBeDone) {
                    return 0;
                } else {
                    return Integer.MAX_VALUE;
                }
            }

            if (ans[pos][ansTillNow] != -1) {
                return ans[pos][ansTillNow];
            }

            int a1 = func(arr, pos + 1, ansTillNow | arr[pos].does, toBeDone, ans);
            int a2 = func(arr, pos + 1, ansTillNow, toBeDone, ans);

            if (a1 == Integer.MAX_VALUE && a2 == Integer.MAX_VALUE) {
                return ans[pos][ansTillNow] = Integer.MAX_VALUE;
            }

            if (a1 != Integer.MAX_VALUE && a2 != Integer.MAX_VALUE) {
                return ans[pos][ansTillNow] = Math.min(a1 + arr[pos].cost, a2);
            }

            if (a1 == Integer.MAX_VALUE) {
                return ans[pos][ansTillNow] = a2;
            }

            return ans[pos][ansTillNow] = a1 + arr[pos].cost;
        }

        private static class pair {
            int cost;
            int does;

            public pair(int cost) {
                this.cost = cost;
                this.does = 0;
            }

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

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }
}

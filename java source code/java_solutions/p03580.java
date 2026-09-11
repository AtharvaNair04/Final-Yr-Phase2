// package other2017.codefestival2017.qualb;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt();
        char[] s = in.nextToken().toCharArray();
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = s[i] - '0';
        }

        List<int[]> part = new ArrayList<>();
        boolean[] isw = new boolean[n];

        for (int i = 1; i+1 < n ; i++) {
            if (w[i-1] == 1 && w[i] == 0 && w[i+1] == 1) {
                isw[i] = true;
                int left = 0;
                int head = i-1;
                while (head >= 0 && w[head] == 1) {
                    left++;
                    head--;
                }
                int tail = i+1;
                int right = 0;
                while (tail < n && w[tail] == 1) {
                    right++;
                    tail++;
                }
                part.add(new int[]{i, left, right, 0, 0});
            }
        }

        for (int[] wi : part) {
            int wl = wi[0] - wi[1] - 1;
            if (wl >= 0 && isw[wl]) {
                wi[3] = 1;
            }
            wl = wi[0] + wi[2] + 1;
            if (wl < n && isw[wl]) {
                wi[4] = 1;
            }
        }

        int dn = part.size();
        int[][] dp = new int[dn+1][4];
        for (int i = 0; i <= dn; i++) {
            Arrays.fill(dp[i], -1);
        }
        dp[0][0] = 0;

        for (int i = 0; i < dn ; i++) {
            int[] wi = part.get(i);
            for (int noleft = 0; noleft <= 3 ; noleft++) {
                int base = dp[i][noleft];
                if (base == -1) {
                    continue;
                }

                int maxLeft = wi[1];
                int maxRight = wi[2];
                if (wi[3] == 1) {
                    if (noleft == 3) {
                        maxLeft = maxRight = 0;
                    } else if (noleft == 2) {
                        maxLeft = 1;
                    } else if (noleft == 1) {
                        maxLeft -= 1;
                    }
                    if (maxLeft == 0) {
                        maxRight = 0;
                    }
                }

                dp[i+1][0] = Math.max(dp[i+1][0], base);

                for (int al = maxLeft ; al <= maxLeft ; al++) {
                    if (al == 0) {
                        continue;
                    }
                    int tl = 1;
                    dp[i+1][tl] = Math.max(dp[i+1][tl], base + al);
                }

                // right
                for (int al = maxRight-1 ; al <= maxRight ; al++) {
                    if (al == 0) {
                        continue;
                    }
                    int tl = 2;
                    if (al == maxRight) {
                        tl = 3;
                    }
                    dp[i+1][tl] = Math.max(dp[i+1][tl], base + al);
                }
            }
        }

        int ma = 0;
        for (int i = 0; i < 4 ; i++) {
            ma = Math.max(ma, dp[dn][i]);
        }
        out.println(ma);
        out.flush();
    }


    public static void debug(Object... o) {
        System.err.println(Arrays.deepToString(o));
    }

    public static class InputReader {
        private static final int BUFFER_LENGTH = 1 << 12;
        private InputStream stream;
        private byte[] buf = new byte[BUFFER_LENGTH];
        private int curChar;
        private int numChars;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        private int next() {
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
                if (numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public char nextChar() {
            return (char) skipWhileSpace();
        }

        public String nextToken() {
            int c = skipWhileSpace();
            StringBuilder res = new StringBuilder();
            do {
                res.append((char) c);
                c = next();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public int nextInt() {
            return (int) nextLong();
        }

        public long nextLong() {
            int c = skipWhileSpace();
            long sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = next();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = next();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public double nextDouble() {
            return Double.valueOf(nextToken());
        }

        int skipWhileSpace() {
            int c = next();
            while (isSpaceChar(c)) {
                c = next();
            }
            return c;
        }

        boolean isSpaceChar(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }
    }
}

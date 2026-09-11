// package atcoder.other2016.codefestival2016.round2;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
    private static final long INF = (long) 4e9;

    public static void main(String[] args) {
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt();
        int m = in.nextInt();

        long[][] table = in.nextLongTable(n, m);



        long sum = 0;
        long last = 0;
        for (int i = 0; i+1 < n; i++) {
            if (m == 1) {
                if (table[i][0] < table[i+1][0]) {
                    // ok
                } else {
                    sum = -1;
                    break;
                }
                continue;
            }

            int k = -1;
            for (int j = 0; j < m ; j++) {
                if (table[i][j] == table[i+1][j]) {
                } else {
                    k = j;
                    break;
                }
            }

            if (k == -1) {
                // completely same.
                sum += last+1;
                last = last+1;
            } else if (k == 0) {
                // different at first.
                if (table[i][k] < table[i+1][k]) {
                    last = 0;
                } else {
                    // NG
                    sum = -1;
                    break;
                }
            } else {
                // how many func required actually?
                long head = table[i][0];
                long second = head * last + table[i][1];
                if (second < table[i+1][1]) {
                    last = 0;
                    continue;
                }
                long req = (second - table[i+1][1] + head - 1) / head;
                long tos = head * req + table[i+1][1];
                if (second < tos) {
                    sum += req;
                    last = req;
                    continue;
                }

                // operation required: req or req+1 times.
                if (compare(table[i], last, table[i+1], req) == -1) {
                    sum += req;
                    last = req;
                } else {
                    sum += req+1;
                    last = req+1;
                }
            }
        }

        out.println(sum);
        out.flush();
    }

    private static int compare(long[] a, long opA, long[] b, long opB) {
        if (opA == 0) {
            return compare(a, b, opB);
        } else if (opB == 0) {
            return -compare(b, opB, a, opA);
        } else {
            long dec = Math.min(opA, opB);
            return compare(a, opA-dec, b, opB-dec);
        }
    }

    private static int compare(long[] a, long[] b, long opB) {
        int n = b.length;
        long[] tob = b.clone();
        if (opB <= 32) {
            for (int f = 0 ; f < opB ; f++) {
                operate(tob);
            }
            for (int i = 0 ; i < n ; i++) {
                if (a[i] != tob[i]) {
                    return (a[i] < tob[i]) ? -1 : 1;
                }
            }
        } else {
            for (int k = 1 ; k < n ; k++) {
                long sum = 0;
                int d = 0;
                for (int part = k ; part >= 0 ; part--) {
                    long left = opB+d-1;
                    long right = d;
                    long comb = comb(left, right);
                    if (comb >= INF || b[part] >= INF / comb) {
                        sum = INF;
                        break;
                    }
                    sum += b[part] * comb;
                    if (sum >= INF) {
                        sum = INF;
                        break;
                    }
                    d++;
                }
                tob[k] = sum;
                if (a[k] != tob[k]) {
                    return (a[k] < tob[k]) ? -1 : 1;
                }
            }
        }
        return 0;
    }

    static final int MOD = 1000000007;

    static long pow(long a, long x) {
        long res = 1;
        while (x > 0) {
            if (x%2 != 0) {
                res = (res*a)%MOD;
            }
            a = (a*a)%MOD;
            x /= 2;
        }
        return res;
    }


    static long comb(long n, long r) {
        BigInteger up = BigInteger.ONE;
        BigInteger dw = BigInteger.ONE;
        for (int i = 0; i < r ; i++) {
            up = up.multiply(BigInteger.valueOf(n-i));
            dw = dw.multiply(BigInteger.valueOf(i+1));
        }
        BigInteger ret = up.divide(dw);
        if (ret.compareTo(BigInteger.valueOf(INF)) == -1) {
            return ret.longValue();
        }
        return INF;
    }


    private static void operate(long[] a) {
        long psum = a[0];
        for (int i = 1  ; i < a.length ; i++) {
            psum += a[i];
            if (psum >= INF) {
                psum = INF;
            }
            a[i] = psum;
        }
    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        private int[] nextInts(int n) {
            int[] ret = new int[n];
            for (int i = 0; i < n; i++) {
                ret[i] = nextInt();
            }
            return ret;
        }

        private int[][] nextIntTable(int n, int m) {
            int[][] ret = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    ret[i][j] = nextInt();
                }
            }
            return ret;
        }

        private long[] nextLongs(int n) {
            long[] ret = new long[n];
            for (int i = 0; i < n; i++) {
                ret[i] = nextLong();
            }
            return ret;
        }

        private long[][] nextLongTable(int n, int m) {
            long[][] ret = new long[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    ret[i][j] = nextLong();
                }
            }
            return ret;
        }

        private double[] nextDoubles(int n) {
            double[] ret = new double[n];
            for (int i = 0; i < n; i++) {
                ret[i] = nextDouble();
            }
            return ret;
        }

        private int next() {
            if (numChars == -1)
                throw new InputMismatchException();
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
            int c = next();
            while (isSpaceChar(c))
                c = next();
            if ('a' <= c && c <= 'z') {
                return (char) c;
            }
            if ('A' <= c && c <= 'Z') {
                return (char) c;
            }
            throw new InputMismatchException();
        }

        public String nextToken() {
            int c = next();
            while (isSpaceChar(c))
                c = next();
            StringBuilder res = new StringBuilder();
            do {
                res.append((char) c);
                c = next();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public int nextInt() {
            int c = next();
            while (isSpaceChar(c))
                c = next();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = next();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c-'0';
                c = next();
            } while (!isSpaceChar(c));
            return res*sgn;
        }

        public long nextLong() {
            int c = next();
            while (isSpaceChar(c))
                c = next();
            long sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = next();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c-'0';
                c = next();
            } while (!isSpaceChar(c));
            return res*sgn;
        }

        public double nextDouble() {
            return Double.valueOf(nextToken());
        }

        public boolean isSpaceChar(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);
        }
    }

    static void debug(Object... o) {
        System.err.println(Arrays.deepToString(o));
    }
}

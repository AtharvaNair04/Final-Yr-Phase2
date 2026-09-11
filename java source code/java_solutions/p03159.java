import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
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
        TaskE solver = new TaskE();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskE {
        int n;
        int[] dp;
        int[] dp1;
        long[][] dp2;
        List<Integer>[] g;
        int[] a;
        int[] size = new int[10000];

        public void solve(int testNumber, MyInput in, PrintWriter out) {
            n = in.nextInt();
            a = in.nextIntArray(n);
            g = new List[n];
            for (int i = 0; i < n; i++) {
                g[i] = new ArrayList<>();
            }
            for (int i = 0; i < n - 1; i++) {
                int a = in.nextInt() - 1;
                int b = in.nextInt() - 1;
                g[a].add(b);
                g[b].add(a);
            }
            dp = new int[n];
            dp1 = new int[n];
            dp2 = new long[n][];
            Arrays.fill(dp, -1);
            Arrays.fill(dp1, -1);
            out.println(rec(0, -1));
        }

        int rec(int v, int p) {
            if (dp[v] != -1) {
                return dp[v];
            }

            int ans = rec1(v, p);
            for (int i = 0; i < size(v, p); i++) {
                if (rec2(v, i, p) < 0) {
                    ans = Math.min(ans, i);
                    break;
                }
            }

//        dump(v, ans);
            return dp[v] = ans;
        }

        int rec1(int v, int p) {
            if (dp1[v] != -1) {
                return dp1[v];
            }
            if (a[v] < 0) return 1 << 29;
            int ans = 0;
            for (int t : g[v]) {
                if (t == p) continue;
                ans += Math.min(rec1(t, v), rec(t, v) + 1);
            }
//        if (v == 2) dump("come", v, ans);
            return dp1[v] = ans;
        }

        long rec2(int v, int e, int p) {
            e = Math.min(e, size(v, p) - 1);
            if (dp2[v] == null) {
                long[] dp = new long[size(v, p)];
                Arrays.fill(dp, 1L << 60);
                dp[0] = a[v];
                int num = 1;
                for (int t : g[v]) {
                    if (t == p) continue;
                    for (int k = num - 1; k >= 0; k--) {
                        long cur = dp[k];
                        dp[k] += rec2(t, 0, v);
                        if (k + rec(t, v) + 1 < size(v, p)) {
                            dp[k + rec(t, v) + 1] = Math.min(dp[k + rec(t, v) + 1], cur);
                        }
                        for (int i = size(t, v) - 1; i > 0; i--) {
                            dp[k + i] = Math.min(dp[k + i], cur + rec2(t, i, v));
                        }
                    }
                    num += size(t, v);
//                if (v == 0) {
//                    dump(t, rec(t, v), dp);
//                }
                }
                dp2[v] = dp;
//            if (v == 2) dump("come", v, dp);
            }
            return dp2[v][e];
        }

        int size(int v, int p) {
            if (size[v] != 0) {
                return size[v];
            }
            for (int t : g[v]) {
                if (t == p) continue;
                size[v] += size(t, v);
            }
            return ++size[v];
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

        public int[] nextIntArray(final int n) {
            final int[] res = new int[n];
            for (int i = 0; i < n; i++) {
                res[i] = nextInt();
            }
            return res;
        }

        static class EndOfFileRuntimeException extends RuntimeException {
        }

    }
}

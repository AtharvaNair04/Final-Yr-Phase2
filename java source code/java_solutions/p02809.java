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
        DArrangement solver = new DArrangement();
        solver.solve(1, in, out);
        out.close();
    }

    static class DArrangement {
        RMQInt seg = new RMQInt(1 << 29);
        RMQInt seg1 = new RMQInt(1 << 29);

        public void solve(int testNumber, MyInput in, PrintWriter out) {
//        for (int i = 0; i < 10000; i++) {
//            System.out.print(((i == 0 ? 1 : 0) + 1) + " ");
//        }
//        System.out.println();
//        if (true) return;

            int n = in.nextInt();
            int[] a = in.nextIntArray(n);
            int[] inDeg = new int[n];
            seg.build(n);
            seg1.build(n);

            if (n == 2) {
                out.println(-1);
                return;
            }

            for (int i = 0; i < n; i++) {
                inDeg[--a[i]]++;
            }
            for (int i = 0; i < n; i++) {
                seg.Update(-inDeg[i], i);
                seg1.Update(i, i);
            }

            int prev = -1;
            boolean[] used = new boolean[n];
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                int next = -1;
                if (n - i == 3) {
                    int[] unused = new int[3];
                    for (int j = 0, k = 0; j < n; j++) {
                        if (!used[j]) {
                            unused[k++] = j;
                        }
                    }
                    if (!(prev == unused[0] || a[unused[1]] == unused[2] && a[unused[2]] == unused[1] || inDeg[unused[1]] == 2 || inDeg[unused[2]] == 2)) {
                        next = unused[0];
                    } else if (!(prev == unused[1] || a[unused[0]] == unused[2] && a[unused[2]] == unused[0] || inDeg[unused[0]] == 2 || inDeg[unused[2]] == 2)) {
                        next = unused[1];
                    } else if (!(prev == unused[2] || a[unused[0]] == unused[1] && a[unused[1]] == unused[0] || inDeg[unused[0]] == 2 || inDeg[unused[1]] == 2)) {
                        next = unused[2];
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    int bad = seg.QueryIndex(0, n);
                    int best = seg1.QueryIndex(0, n);

                    if (best == prev) {
                        int ng = best;
                        seg1.Update(1 << 29, ng);
                        best = seg1.QueryIndex(0, n);
                        seg1.Update(ng, ng);
                    }

                    next = best;
                    if (inDeg[bad] == n - i - 1) {
                        next = bad;
                    }
                }

                used[next] = true;
                inDeg[a[next]]--;
                if (!used[a[next]]) {
                    seg.Update(-inDeg[a[next]], a[next]);
                }
                seg.Update(1 << 29, next);
                seg1.Update(1 << 29, next);
                ans[i] = next + 1;
                prev = a[next];
            }
            printList(out, ans);
        }

        static void printList(PrintWriter out, int[] p) {
            for (int i = 0; i < p.length; i++) {
                out.print(p[i] + (i == p.length - 1 ? "\n" : " "));
            }
        }

        class RMQInt {
            int n;
            int[] xs;
            int[] ys;
            final int INF;

            public RMQInt(int inf) {
                INF = inf;
            }

            int Min(int a, int b) {
                return a == INF ? b : (b == INF ? a : (a <= b ? a : b));
            }

            int minIndex(int a, int b) {
                return a == -1 ? b : (b == -1 ? a : (Min(xs[a], xs[b]) == xs[a] ? a : b));
            }

            public void build(int m) {
                for (n = 1; n < m; n <<= 1) ;
                xs = new int[2 * n];
                ys = new int[2 * n];
                for (int i = 0; i < 2 * n; i++) {
                    ys[i] = i;
                }
                clear();
            }

            public void clear() {
                Arrays.fill(xs, INF);
            }

            public void Update(int v, int idx) {
                xs[idx += n - 1] = v;
                while (idx > 0) {
                    idx = (idx - 1) / 2;
                    xs[idx] = Min(xs[idx * 2 + 1], xs[idx * 2 + 2]);
                    ys[idx] = ys[minIndex(idx * 2 + 1, idx * 2 + 2)];
                }
            }

            public int QueryIndex(int l, int r) {
                return QueryIndex(l, r, 0, n, 0) - (n - 1);
            }

            int QueryIndex(int l, int r, int a, int b, int k) {
                if (l <= a && b <= r) return ys[k];
                if (b <= l || r <= a) return -1;
                final int m = (a + b) / 2;
                return minIndex(
                        QueryIndex(l, r, a, m, 2 * k + 1),
                        QueryIndex(l, r, m, b, 2 * k + 2));
            }

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

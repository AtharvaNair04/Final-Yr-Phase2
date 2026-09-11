import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;
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
        FastScanner in = new FastScanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        B_DoNotDuplicate solver = new B_DoNotDuplicate();
        solver.solve(1, in, out);
        out.close();
    }

    static class B_DoNotDuplicate {
        public void solve(int testNumber, FastScanner in, PrintWriter out) {
            final int N = (int) (2e5 + 1);
            int n = in.nextInt();
            long k = in.nextLong();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
            }

            int[] whoLeavesIfStartAt = new int[n + 1];
            int[] latest = new int[N + 1];
            Arrays.fill(latest, n);
            whoLeavesIfStartAt[n] = N;
            for (int i = n - 1; i >= 0; i--) {
                int x = a[i];
                if (latest[x] == n) {
                    whoLeavesIfStartAt[i] = x;
                } else {
                    whoLeavesIfStartAt[i] = whoLeavesIfStartAt[latest[x] + 1];
                }
                latest[x] = i;
            }

            int[] whoLeavesIfXEnters = new int[N + 1];
            for (int x = 0; x < N; x++) {
                if (latest[x] == n) {
                    continue;
                }
                whoLeavesIfXEnters[x] = whoLeavesIfStartAt[latest[x] + 1];
            }
            whoLeavesIfXEnters[N] = whoLeavesIfStartAt[0];

            --k;
            int[] seen = new int[N + 1];
            Arrays.fill(seen, -1);
            int cur = N;
            int step;
            for (step = 0; step < k; step++) {
                if (seen[cur] >= 0) {
                    break;
                }
                seen[cur] = step;
                cur = whoLeavesIfXEnters[cur];
            }

            int cycleLength = step - seen[cur];
            long stepsLeft = step == k ? 0 : (k - step) % cycleLength;
            while (stepsLeft > 0) {
                cur = whoLeavesIfXEnters[cur];
                --stepsLeft;
            }

            int pos = Math.min(n, cur == N ? 0 : latest[cur] + 1);
            int[] c = simulate(Arrays.copyOfRange(a, pos, n));

            for (int i = 0; i < c.length; i++) {
                if (i > 0) {
                    out.print(" ");
                }
                out.print(c[i]);
            }
            out.println();
        }

        private int[] simulate(int[] a) {
            int[] b = new int[a.length];
            int p = 0;
            Set<Integer> set = new HashSet<>();
            for (int x : a) {
                if (!set.contains(x)) {
                    b[p++] = x;
                    set.add(x);
                } else while (set.contains(x)) {
                    set.remove(b[--p]);
                }
            }
            return Arrays.copyOf(b, p);
        }

    }

    static class FastScanner {
        private BufferedReader in;
        private StringTokenizer st;

        public FastScanner(InputStream stream) {
            in = new BufferedReader(new InputStreamReader(stream));
        }

        public String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(in.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

    }
}

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
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
        FastScanner in = new FastScanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        C_KnapsackQueriesOnATree solver = new C_KnapsackQueriesOnATree();
        solver.solve(1, in, out);
        out.close();
    }

    static class C_KnapsackQueriesOnATree {
        public void solve(int testNumber, FastScanner in, PrintWriter out) {
            final int W = (int) (1e5 + 100);
            int n = in.nextInt();
            int[] value = new int[n];
            int[] weight = new int[n];
            for (int i = 0; i < n; i++) {
                value[i] = in.nextInt();
                weight[i] = in.nextInt();
            }

            int log = 32 - Integer.numberOfLeadingZeros(n);
            int k = Math.min(n, 1 << (log / 2));
            int[][] d = new int[k][W];
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < W; j++) {
                    if (i == 0) {
                        if (j >= weight[i]) {
                            d[i][j] = value[i];
                        }
                    } else {
                        int p = (i - 1) / 2;
                        d[i][j] = d[p][j];
                        if (j >= weight[i]) {
                            d[i][j] = Math.max(d[i][j], d[p][j - weight[i]] + value[i]);
                        }
                    }
                }
            }

            int numQueries = in.nextInt();
            int[] path = new int[log + 1];
            int[] maskValue = new int[1 << ((log + 1) / 2)];
            int[] maskWeight = new int[1 << ((log + 1) / 2)];
            for (int query = 0; query < numQueries; query++) {
                int v = in.nextInt() - 1;
                int L = in.nextInt();
                int t = 0;
                while (v >= k) {
                    path[t++] = v;
                    v = (v - 1) / 2;
                }
                int ans = d[v][L];
                for (int mask = 1; mask < 1 << t; mask++) {
                    int i = Integer.numberOfTrailingZeros(mask);
                    maskValue[mask] = value[path[i]] + maskValue[mask ^ (1 << i)];
                    maskWeight[mask] = weight[path[i]] + maskWeight[mask ^ (1 << i)];
                    if (L >= maskWeight[mask]) {
                        ans = Math.max(ans, maskValue[mask] + d[v][L - maskWeight[mask]]);
                    }
                }
                out.println(ans);
            }
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

    }
}

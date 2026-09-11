import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
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
        ProblemC_Swaps solver = new ProblemC_Swaps();
        solver.solve(1, in, out);
        out.close();
    }

    static class ProblemC_Swaps {
        public void solve(int testNumber, FastScanner in, PrintWriter out) {
            int n = in.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
            }
            int[] b = new int[n];
            for (int i = 0; i < n; i++) {
                b[i] = in.nextInt();
            }

            Pair[] sortedA = new Pair[n];
            for (int i = 0; i < n; i++) {
                sortedA[i] = new Pair(a[i], i);
            }
            Arrays.sort(sortedA);
            int[] p = new int[n];
            for (int i = 0; i < n; i++) {
                p[sortedA[i].b] = i;
            }

            Pair[] sortedB = new Pair[n];
            for (int i = 0; i < n; i++) {
                sortedB[i] = new Pair(b[i], i);
            }
            Arrays.sort(sortedB);
            int[] q = new int[n];
            for (int i = 0; i < n; i++) {
                q[sortedB[i].b] = i;
            }

            int[] invQ = new int[n];
            for (int i = 0; i < n; i++) {
                invQ[q[i]] = i;
            }

            for (int i = 0; i < n; i++) {
                if (sortedA[i].a > sortedB[i].a) {
                    out.println("No");
                    return;
                }
            }

            for (int i = 0; i < n; i++) {
                p[i] = invQ[p[i]];
            }
            int cycleLen = 0;
            {
                int i = 0;
                do {
                    i = p[i];
                    ++cycleLen;
                } while (i != 0);
            }
            if (cycleLen < n) {
                out.println("Yes");
                return;
            }

            for (int i = 0; i + 1 < n; i++) {
                if (sortedA[i + 1].a <= sortedB[i].a) {
                    out.println("Yes");
                    return;
                }
            }

            out.println("No");
        }

        class Pair implements Comparable<Pair> {
            int a;
            int b;

            Pair(int a, int b) {
                this.a = a;
                this.b = b;
            }

            public int compareTo(Pair o) {
                if (a != o.a) {
                    return a < o.a ? -1 : 1;
                }
                if (b != o.b) {
                    return b < o.b ? -1 : 1;
                }
                return 0;
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
                    String rl = in.readLine();
                    if (rl == null) {
                        return null;
                    }
                    st = new StringTokenizer(rl);
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

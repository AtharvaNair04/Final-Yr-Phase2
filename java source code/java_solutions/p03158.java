import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.InputMismatchException;
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
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskD solver = new TaskD();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskD {
        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt();
            int q = in.nextInt();

            int[] a = in.nextIntArray(n);

            long[] sum = new long[n + 1];
            long[] sum2 = new long[n + 2];
            for (int i = 0; i < n; i++) {
                sum[i + 1] = sum[i] + a[i];
                sum2[i + 2] = sum2[i] + a[i];
            }

            // 最大は必ずとれる

            for (int i = 0; i < q; i++) {
                int x = in.nextInt();

                int cannot = n - 1;
                int can = 0;
                while (cannot - can > 1) {
                    int mid = (cannot + can) / 2;

                    if (n - 1 - 2 * mid >= 0 && Math.abs(x - a[n - 1 - mid]) >= Math.abs(x - a[n - 1 - 2 * mid])) {
                        can = mid;
                    } else {
                        cannot = mid;
                    }
                }

                long ans = a[n - 1];
                ans += sum[n - 1] - sum[n - can - 1];
                ans += sum2[n - 2 * can - 1];

                out.println(ans);
            }
        }

    }

    static class InputReader {
        private BufferedReader br;
        private StringTokenizer st;

        public InputReader(InputStream inputStream) {
            br = new BufferedReader(new InputStreamReader(inputStream));
            st = new StringTokenizer("");
        }

        public String nextString() {
            while (!st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine(), " ");
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(nextString());
        }

        public int[] nextIntArray(int n) {
            int[] res = new int[n];
            for (int i = 0; i < n; i++) {
                res[i] = nextInt();
            }
            return res;
        }

    }
}

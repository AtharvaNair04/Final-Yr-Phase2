import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashSet;
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
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskD solver = new TaskD();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskD {
        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt(), m = in.nextInt();
            int[] arr = new int[n];
            int cmatch = 0;

            HashSet<Integer> hs = new HashSet<>();

            int[] matched = new int[m];
            int[] excess = new int[m];
            for (int i = 0; i < n; i++) {
                arr[i] = in.nextInt();
                int b = arr[i] % m;
                if (hs.contains(arr[i])) {
                    hs.remove(arr[i]);
                    cmatch++;
                    matched[b] += 2;
                    excess[b]--;
                } else {
                    hs.add(arr[i]);
                    excess[b]++;
                }
            }
            cmatch += excess[0] / 2;

            for (int i = 1; i < m - i; i++) {
                int wmatch = Math.min(excess[i], excess[m - i]);
                cmatch += wmatch;
                excess[i] -= wmatch;
                excess[m - i] -= wmatch;
                while (true) {
                    boolean changed = false;
                    if (excess[i] > 0 && matched[m - i] > 0) {
                        matched[m - i] -= 2;
                        excess[m - i]++;
                        excess[i]--;
                        changed = true;
                    }
                    wmatch = Math.min(excess[i], excess[m - i]);
                    cmatch += wmatch;
                    excess[i] -= wmatch;
                    excess[m - i] -= wmatch;
                    if (excess[m - i] > 0 && matched[i] > 0) {
                        matched[i] -= 2;
                        excess[i]++;
                        excess[m - i]--;
                        changed = true;
                    }
                    wmatch = Math.min(excess[i], excess[m - i]);
                    cmatch += wmatch;
                    excess[i] -= wmatch;
                    excess[m - i] -= wmatch;

                    if (!changed) break;
                }
            }

            if (m % 2 == 0) {
                cmatch += excess[m / 2] / 2;
            }

            out.println(cmatch);
        }

    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}

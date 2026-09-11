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
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskI solver = new TaskI();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskI {
        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt();
            if (n == 2) {
                out.println(-1);
                return;
            }
            int[][] res = solve(n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - 1; j++) {
                    if (j != 0) out.print(" ");
                    out.print(res[i][j]);
                }
                out.println();
            }
        }

        public static int[][] solve(int size) {
            if (size == 3) {
                return new int[][]{
                        {2, 3},
                        {3, 1},
                        {1, 2},
                };
            }
            int[][] mat = new int[size][size - 1];
            int[][] sub = solve(size - 1);
            for (int i = 0; i < size - 1; i++) {
                System.arraycopy(sub[i], 0, mat[i], 0, sub[i].length);
            }
            for (int i = 0; i < size - 1; i++) {
                mat[size - 1][i] = i + 2;
            }

            for (int i = 0; i < size - 1; i++) {
                mat[i][size - 2] = size;
            }
            mat[size - 1][size - 2] = 1;
            int t = mat[0][size - 2];
            mat[0][size - 2] = mat[0][size - 3];
            mat[0][size - 3] = t;
            return mat;
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

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
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
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskE solver = new TaskE();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskE {
        long p;

        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt();
            p = in.nextLong();
            long sum = 0;
            Man[] men = new Man[n];
            for (int i = 0; i < n; i++) {
                int w = in.nextInt();
                int b = in.nextInt();
                men[i] = new Man(w, b);
                sum -= p * b;
            }
            Arrays.sort(men);

            for (int i = 0; i < n; i++) {
                sum += men[i].score;
                if (sum >= 0) {
                    out.println(i + 1);
                    return;
                }
            }

            throw new RuntimeException();
        }

        private class Man implements Comparable<Man> {
            long score;

            public Man(int w, int b) {
                score = (100 - p) * w + p * b;
            }


            public int compareTo(Man o) {
                return -1 * Long.compare(score, o.score);
            }

        }

    }

    static class InputReader {
        BufferedReader in;
        StringTokenizer tok;

        public String nextString() {
            while (!tok.hasMoreTokens()) {
                try {
                    tok = new StringTokenizer(in.readLine(), " ");
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
            }
            return tok.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(nextString());
        }

        public long nextLong() {
            return Long.parseLong(nextString());
        }

        public InputReader(InputStream inputStream) {
            in = new BufferedReader(new InputStreamReader(inputStream));
            tok = new StringTokenizer("");
        }

    }
}

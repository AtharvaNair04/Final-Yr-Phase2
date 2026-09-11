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
 *
 * @author __shangu__
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        DAllYourPathsAreDifferentLengths solver = new DAllYourPathsAreDifferentLengths();
        solver.solve(1, in, out);
        out.close();
    }

    static class DAllYourPathsAreDifferentLengths {
        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt(), bitn = 0;
            bitn = Integer.toBinaryString(n).length();
            out.printf("%d %d\n", bitn, bitn * 2 + Integer.bitCount(n) - 3);

            for (int i = 0; i < bitn - 1; ++i) {
                out.printf("%d %d %d\n", i + 1, i + 2, 0);
                out.printf("%d %d %d\n", i + 1, i + 2, 1 << i);

                if ((n >> i & 1) == 1) {
                    out.printf("%d %d %d\n", i + 1, bitn, (-1 << i + 1) & n);
                }
            }
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

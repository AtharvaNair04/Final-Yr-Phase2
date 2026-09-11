import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.stream.IntStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.LongStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.io.InputStreamReader;
import java.util.OptionalInt;
import java.io.UncheckedIOException;
import java.util.stream.Stream;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author mikit
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        LightScanner in = new LightScanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        DRobotArms solver = new DRobotArms();
        solver.solve(1, in, out);
        out.close();
    }

    static class DRobotArms {
        public void solve(int testNumber, LightScanner in, PrintWriter out) {
            int n = in.ints();
            long[] x = new long[n];
            long[] y = new long[n];
            in.longs(x, y);

            boolean even = isEven(x[0] + y[0]);
            if (IntStream.range(1, n).anyMatch(i -> even ^ isEven(x[i] + y[i]))) {
                out.println(-1);
                return;
            }

            long[] pows = new long[35];
            pows[pows.length - 1] = 1;
            for (int i = pows.length - 2; i >= 0; i--) {
                pows[i] = pows[i + 1] * 2L;
            }

            if (even) {
                out.println(pows.length + 1);
                out.print("1 ");
            } else {
                out.println(pows.length);
            }
            out.println(Arrays.stream(pows).mapToObj(Long::toString).collect(Collectors.joining(" ")));

            for (int i = 0; i < n; i++) {
                if (even) {
                    out.print('R');
                    x[i]--;
                }
                for (int j = 0; j < pows.length; j++) {
                    if (x[i] + y[i] > 0) {
                        if (x[i] - y[i] > 0) {
                            out.print('R');
                            x[i] -= pows[j];
                        } else {
                            out.print('U');
                            y[i] -= pows[j];
                        }
                    } else {
                        if (x[i] - y[i] > 0) {
                            out.print('D');
                            y[i] += pows[j];
                        } else {
                            out.print('L');
                            x[i] += pows[j];
                        }
                    }
                }
                if (x[i] > 0 || y[i] > 0) {
                    System.out.println("WARN: " + x[i] + "," + y[i] + " REMAINS");
                }
                out.println();
            }
        }

        private static boolean isEven(long v) {
            return ((v % 2) + 2) % 2 == 0;
        }

    }

    static class LightScanner {
        private BufferedReader reader = null;
        private StringTokenizer tokenizer = null;

        public LightScanner(InputStream in) {
            reader = new BufferedReader(new InputStreamReader(in));
        }

        public String string() {
            if (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int ints() {
            return Integer.parseInt(string());
        }

        public long longs() {
            return Long.parseLong(string());
        }

        public void longs(long[]... arrays) {
            int l = Arrays.stream(arrays).mapToInt(a -> a.length).min().orElse(0);
            for (int i = 0; i < l; i++) {
                for (int j = 0; j < arrays.length; j++) {
                    arrays[j][i] = longs();
                }
            }
        }

    }
}

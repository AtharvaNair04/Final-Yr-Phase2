import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.io.Writer;
import java.io.OutputStreamWriter;
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
        LightWriter out = new LightWriter(outputStream);
        EOrand solver = new EOrand();
        solver.solve(1, in, out);
        out.close();
    }

    static class EOrand {
        public void solve(int testNumber, LightScanner in, LightWriter out) {
            int n, max, m = 0;
            int[] a;
            {
                int n0 = in.ints();
                max = in.ints();
                int s = in.ints(), t = in.ints();
                int[] a0 = in.ints(n0);
                a = new int[n0];
                n = 0;
                for (int i = 0; i < 20; i++) {
                    if (((s >> i) & 1) == 1 || ((t >> i) & 1) == 0) continue;
                    m++;
                }
                for (int i = 0; i < n0; i++) {
                    if ((a0[i] & s) != s || (a0[i] | t) != t) continue;
                    int tmp = 0, bit = 1;
                    for (int j = 0; j < 18; j++) {
                        if (((s >> j) & 1) == 1 || ((t >> j) & 1) == 0) continue;
                        if (((a0[i] >> j) & 1) == 1) tmp |= bit;
                        bit <<= 1;
                    }
                    a[n++] = tmp;
                }
            }

            long[][] comb = new long[51][51];
            long[] coeff = new long[51];
            for (int i = 0; i <= 50; i++) comb[i][0] = 1;
            for (int i = 1; i <= 50; i++) comb[i][i] = 1;
            for (int i = 1; i <= 50; i++) {
                for (int j = 1; j < i; j++) {
                    comb[i][j] = comb[i - 1][j] + comb[i - 1][j - 1];
                }
            }
            for (int i = 0; i <= 50; i++) {
                for (int j = 1; j < max && j <= i; j++) {
                    coeff[i] += comb[i][j];
                }
            }

            long ans = 0;
            for (int i = 1; i <= max; i++) ans += comb[n][i];
            if (m == 0) {
                out.ans(ans).ln();
                return;
            } else if (n == 0) {
                out.ans(0).ln();
                return;
            }

            //System.out.println(Arrays.toString(coeff));
            int all = 1 << m;
            for (int i = 0; i < n; i++) {
                long d = 1;
                for (int j = 1; j < all; j++) {
                    int count = 0;
                    for (int k = i + 1; k < n; k++) {
                        if ((j & (a[i] ^ a[k])) == 0) count++;
                    }
                    if ((Integer.bitCount(j) & 1) == 1) d += coeff[count];
                    else d -= coeff[count];
                    //System.out.println(i + "," + Integer.toBinaryString(j) + "," + count);
                }
                //System.out.println(i + " - " + d);
                ans -= Math.abs(d);
            }
            out.ans(ans).ln();
        }

    }

    static class LightWriter implements AutoCloseable {
        private final Writer out;
        private boolean autoflush = false;
        private boolean breaked = true;

        public LightWriter(Writer out) {
            this.out = out;
        }

        public LightWriter(OutputStream out) {
            this(new OutputStreamWriter(new BufferedOutputStream(out), Charset.defaultCharset()));
        }

        public LightWriter print(char c) {
            try {
                out.write(c);
                breaked = false;
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            return this;
        }

        public LightWriter print(String s) {
            try {
                out.write(s, 0, s.length());
                breaked = false;
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            return this;
        }

        public LightWriter ans(String s) {
            if (!breaked) {
                print(' ');
            }
            return print(s);
        }

        public LightWriter ans(long l) {
            return ans(Long.toString(l));
        }

        public LightWriter ans(int i) {
            return ans(Integer.toString(i));
        }

        public LightWriter ln() {
            print(System.lineSeparator());
            breaked = true;
            if (autoflush) {
                try {
                    out.flush();
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
            return this;
        }

        public void close() {
            try {
                out.close();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

    }

    static class LightScanner implements AutoCloseable {
        private BufferedReader reader = null;
        private StringTokenizer tokenizer = null;

        public LightScanner(InputStream in) {
            reader = new BufferedReader(new InputStreamReader(in));
        }

        public String string() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
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

        public int[] ints(int length) {
            return IntStream.range(0, length).map(x -> ints()).toArray();
        }

        public void close() {
            try {
                this.reader.close();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

    }
}

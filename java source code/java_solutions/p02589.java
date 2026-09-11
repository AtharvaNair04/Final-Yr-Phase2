import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.SplittableRandom;
import java.math.BigInteger;
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
        B_FirstSecond solver = new B_FirstSecond();
        solver.solve(1, in, out);
        out.close();
    }

    static class B_FirstSecond {
        SplittableRandom random = new SplittableRandom(0);
        final int N = (int) (1e6 + 100);
        long MOD0;
        long X0;
        long[] pow0;
        long MOD1;
        long X1;
        long[] pow1;

        {
            MOD0 = (long) 1e9 + random.nextInt((int) 1e8);
            while (!BigInteger.valueOf(MOD0).isProbablePrime(100)) {
                ++MOD0;
            }
            X0 = 1 + random.nextInt((int) (MOD0 - 1));

            pow0 = new long[N + 1];
            pow0[0] = 1;
            for (int i = 1; i < pow0.length; i++) {
                pow0[i] = X0 * pow0[i - 1] % MOD0;
            }
        }

        {
            MOD1 = (long) 1e9 + random.nextInt((int) 1e8);
            while (!BigInteger.valueOf(MOD1).isProbablePrime(111)) {
                ++MOD1;
            }
            X1 = 1 + random.nextInt((int) (MOD1 - 1));

            pow1 = new long[N + 1];
            pow1[0] = 1;
            for (int i = 1; i < pow1.length; i++) {
                pow1[i] = X1 * pow1[i - 1] % MOD1;
            }
        }

        public void solve(int testNumber, FastScanner in, PrintWriter out) {
            int n = in.nextInt();
            char[][] s = new char[n][];
            StringHasher[] h = new StringHasher[n];
            Set<Long> fullStrings = new HashSet<>();
            for (int i = 0; i < n; i++) {
                s[i] = in.next().toCharArray();
                h[i] = new StringHasher(s[i]);
                long fh = h[i].get(0, s[i].length - 1);
                if (fullStrings.contains(fh)) {
                    throw new AssertionError();
                }
                fullStrings.add(fh);
            }

            long ans = 0;
            for (int i = 0; i < n; i++) {
                boolean[] got = new boolean[26];
                for (int j = 0; j < s[i].length; j++) {
                    got[s[i][j] - 'a'] = true;
                    for (int c = 0; c < 26; c++) {
                        if (!got[c]) {
                            continue;
                        }
                        long suff0 = h[i].get0(j + 1, s[i].length - 1);
                        long suff1 = h[i].get1(j + 1, s[i].length - 1);
                        int suffLen = s[i].length - j - 1;
                        long full0 = ((c + 'a') * pow0[suffLen] + suff0) % MOD0;
                        long full1 = ((c + 'a') * pow1[suffLen] + suff1) % MOD1;
                        long full = (full0 << 32) | full1;
                        if (fullStrings.contains(full)) {
                            ++ans;
                        }
                    }
                }
            }

            out.println(ans - n);
        }

        class StringHasher {
            long[] h0;
            long[] h1;

            StringHasher(char[] s) {
                int n = s.length;
                h0 = new long[n + 1];
                h1 = new long[n + 1];
                for (int i = 0; i < n; i++) {
                    h0[i + 1] = (X0 * h0[i] + s[i]) % MOD0;
                    h1[i + 1] = (X1 * h1[i] + s[i]) % MOD1;
                }
            }

            long get(int l, int r) {
                long u = get0(l, r);
                long v = get1(l, r);
                return (u << 32) | v;
            }

            private long get0(int l, int r) {
                long res = (h0[r + 1] - pow0[r - l + 1] * h0[l]) % MOD0;
                if (res < 0) {
                    res += MOD0;
                }
                return res;
            }

            private long get1(int l, int r) {
                long res = (h1[r + 1] - pow1[r - l + 1] * h1[l]) % MOD1;
                if (res < 0) {
                    res += MOD1;
                }
                return res;
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

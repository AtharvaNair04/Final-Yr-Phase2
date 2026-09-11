import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static int N;
    static int K;
    static int[] A;

    static int MOD = 1_000_000_007;

    public static void main(String[] args) {
        FastScanner sc = new FastScanner(System.in);
        N = sc.nextInt();
        K = sc.nextInt();
        A = sc.nextIntArray(N);

        System.out.println(solve());
    }

    static int solve() {
        int[][] prev = new int[256][N+1];
        int[][] dp = new int[256][N+1];
        boolean[] done = new boolean[256];

        for (int i = 0; i < N; i++) {
            int a = A[i];

            int[][] t = dp;
            dp = prev;
            prev = t;
            Arrays.fill(done, false);

            for (int b = 0; b < 256; b++) {
                int c = a ^ b;

                if( done[b] ) continue;

                System.arraycopy(prev[b], 0, dp[b], 0, N+1);
                System.arraycopy(prev[c], 0, dp[c], 0, N+1);

                for (int j = 1; j < N; j++) {
                    dp[b][j+1] += prev[c][j];
                    dp[b][j+1] %= MOD;
                    dp[c][j+1] += prev[b][j];
                    dp[c][j+1] %= MOD;
                }

                done[b] = true;
                done[c] = true;
            }

            dp[a][1]++;
            dp[a][1] %= MOD; // まぁ一応...
        }

        long ans = 0;
        long p = 1;
        for (int i = 1; i <= N; i++) {
            p = p * i % MOD;
            ans += dp[K][i] * p % MOD;
            ans %= MOD;
        }
        return (int)ans;
    }

    @SuppressWarnings("unused")
    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        FastScanner(InputStream in) {
            reader = new BufferedReader(new InputStreamReader(in));
            tokenizer = null;
        }

        String next() {
            if (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        String nextLine() {
            if (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    return reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken("\n");
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        int[] nextIntArray(int n) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++)
                a[i] = nextInt();
            return a;
        }

        long[] nextLongArray(int n) {
            long[] a = new long[n];
            for (int i = 0; i < n; i++)
                a[i] = nextLong();
            return a;
        }
    }
}

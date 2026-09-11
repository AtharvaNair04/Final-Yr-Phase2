import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        Butterfly solver = new Butterfly();
        try {
            int testNumber = 1;
            while (true)
                solver.solve(testNumber++, in, out);
        } catch (UnknownError e) {
            out.close();
        }
    }

    static class Butterfly {
        final int MAX = 1 << 16;
        long[] dp = new long[MAX];

        public void solve(int testNumber, Scanner in, PrintWriter out) {
            int N = in.nextInt();
            if (N == 0) throw new UnknownError();
            Arrays.fill(dp, 0);
            for (int i = 0; i < N; i++) {
                int M = in.nextInt();
                long L = in.nextLong();
                int mask = 0;
                for (int j = 0; j < M; j++) {
                    int s = in.nextInt() - 6;
                    int e = in.nextInt() - 6;
                    for (int bit = s; bit < e; bit++) {
                        mask |= (1 << bit);
                    }
                }
                for (int bit = MAX - 1; bit >= 0; bit--) {
                    if ((bit & mask) == 0) {
                        dp[bit | mask] = Math.max(dp[bit | mask], dp[bit] + L);
                    } else {
                        dp[bit | mask] = Math.max(dp[bit | mask], dp[bit]);
                    }
                }
            }
            long ans = 0;
            for (int i = 0; i < MAX; i++) {
                ans = Math.max(ans, dp[i]);
            }
            out.println(ans);
        }

    }
}

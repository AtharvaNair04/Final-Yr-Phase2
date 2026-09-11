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
        ErraticSleepHabits solver = new ErraticSleepHabits();
        try {
            int testNumber = 1;
            while (true)
                solver.solve(testNumber++, in, out);
        } catch (UnknownError e) {
            out.close();
        }
    }

    static class ErraticSleepHabits {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            final int INF = (int) 1e9;
            int T = in.nextInt();
            if (T == 0) {
                throw new UnknownError();
            }
            int[] t = new int[T]; // cycle
            for (int i = 0; i < T; i++) {
                t[i] = in.nextInt();
            }
            int N = in.nextInt();
            int[] d = new int[101];
            Arrays.fill(d, INF);
            for (int i = 0; i < N; i++) {
                int day = in.nextInt() - 1;
                d[day] = Math.min(d[day], in.nextInt());
            }
            final int MAX_DAY = 100;
            int[][] dp = new int[MAX_DAY][T];
            for (int i = 0; i < MAX_DAY; i++) {
                Arrays.fill(dp[i], INF);
            }
            dp[0][0] = 0;
            for (int i = 0; i < MAX_DAY - 1; i++) {
                for (int j = 0; j < T; j++) {
                    if (dp[i][j] < INF) {
                        int nxtCycle = (j + 1) % T;
                        if (t[nxtCycle] <= d[i + 1]) {
                            // サイクルリセットしない
                            dp[i + 1][nxtCycle] = Math.min(dp[i + 1][nxtCycle], dp[i][j]);
                        }
                        if (t[0] <= d[i + 1]) {
                            // サイクルリセットする
                            dp[i + 1][0] = Math.min(dp[i + 1][0], dp[i][j] + 1);
                        }
                    }
                }
            }
            int ans = 100;
            for (int i = 0; i < T; i++) {
                ans = Math.min(ans, dp[MAX_DAY - 1][i]);
            }
            out.println(ans);
        }

    }
}

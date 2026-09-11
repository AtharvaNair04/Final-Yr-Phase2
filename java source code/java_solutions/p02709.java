import java.util.*;

public class Main {
    void run() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] arr = new int[n][2];
        for (int i = 0; i < n; i++) {
            arr[i] = new int[]{i, sc.nextInt()};
        }

        Arrays.sort(arr, (a1, a2) -> a2[1] - a1[1]);
//        debug(arr);

        long[][] dp = new long[n + 1][n + 1];
        for (long[] x : dp) {
            Arrays.fill(x, -1);
        }
        dp[0][0] = 0;
        // dp[i][j] ... 左からi人、右からj人並べた場合のうれしさの最大値

        for (int k = 0; k < n; k++) {
            int x = arr[k][0];
            long a = (long) arr[k][1];
            for (int i = 0; i <= k; i++) {
                int j = k - i;
                if (dp[i][j] == -1) continue;
                dp[i + 1][j] = Math.max(dp[i + 1][j], dp[i][j] + a * Math.abs(x - i));
                dp[i][j + 1] = Math.max(dp[i][j + 1], dp[i][j] + a * Math.abs((n - 1 - j) - x));
            }
//            debug(dp);
        }
//        debug(dp);
        long ans = -1;
        for (int i = 0; i <= n; i++) {
            ans = Math.max(ans, dp[i][n - i]);
        }
        System.out.println(ans);
    }

    void debug(Object... os) {
        System.err.println(Arrays.deepToString(os));
    }

    public static void main(String[] args) {
        new Main().run();
    }
}

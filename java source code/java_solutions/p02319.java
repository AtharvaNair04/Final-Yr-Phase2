import java.util.Arrays;
import java.util.Scanner;

public class Main {
    int n;
    long w;
    Item[] is;
    private final static int MAX_V = 100;

    public static void main(String[] args) {
        Main m = new Main();
        m.read();
        m.solve();
    }

    private void read() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        w = sc.nextLong();
        is = new Item[n];
        for (int i = 0; i < n; i++) {
            int v = sc.nextInt();
            long w = sc.nextLong();
            is[i] = new Item(v, w);
        }
    }

    private void solve() {
        long[][] dp = new long[n+1][MAX_V*(n+1)];
        for (int i = 0;  i < dp.length; i++) {
            Arrays.fill(dp[i], 1000000001);
        }
        dp[0][0] = 0;
        for (int i = 1; i < dp.length; i++) {
            long localW = is[i-1].w;
            int localV = is[i-1].v;
            for (int j = 0; j < dp[0].length; j++) {
                if (j - localV < 0) {
                    dp[i][j] = dp[i-1][j];
                }
                else {
                    dp[i][j] = Math.min(dp[i-1][j], dp[i-1][j-localV] + localW);
                }
            }
//            System.out.println();
        }
        int ans = 0;
        for (int i = 0; i < dp[0].length; i++) {
            if (dp[n][i] != 0 && dp[n][i] <= w && ans < i)
                ans = i;
        }
        System.out.println(ans);
    }

    private class Item {
        int v;
        long w;
        Item (int v, long w) {
            this.v = v;
            this.w = w;
        }
    }
}

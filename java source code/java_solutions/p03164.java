import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int W = sc.nextInt();
        int[] w = new int[N];
        int[] v = new int[N];

        for (int i = 0; i < N; i++) {
            w[i] = sc.nextInt();
            v[i] = sc.nextInt();
        }
        int maxV = 100000;
        long[] dp = new long[maxV + 1];
        Arrays.fill(dp, W + 1);
        dp[0] = 0;

        for (int i = 0; i < N; i++) {
            for (int j = dp.length - 1; j >= v[i]; j--) {
                dp[j] = Math.min(dp[j], dp[j - v[i]] + w[i]);
            }
        }

        for (int j = dp.length - 1; j >= 0; j--) {
            if (dp[j] <= W) {
                System.out.println(j);
                break;
            }
        }
    }
}

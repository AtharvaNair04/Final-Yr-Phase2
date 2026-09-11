import java.util.Scanner;

public class Main {
    static final int mod = 998244353;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int S = sc.nextInt();
        int[] A = new int[N];
        for (int i = 0; i < N; i++) {
            A[i] = sc.nextInt();
        }

        long res = 0;
        long[] dp = new long[S + 1];
        for (int i = 0; i < N; i++) {
            dp[0] += 1;
            for (int j = S; j >= 0; j--) {
                long c1 = dp[j], c2 = 0;
                if (j >= A[i]) {
                    c2 = dp[j - A[i]];
                }
                dp[j] = c1 + c2;
                dp[j] %= mod;
            }
            res += dp[S];
            res %= mod;
        }
        System.out.println(res);
    }
}

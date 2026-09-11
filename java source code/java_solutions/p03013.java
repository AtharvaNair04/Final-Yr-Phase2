import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static int MOD = 1000000007;
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer N = Integer.parseInt(scanner.next());
        Integer M = Integer.parseInt(scanner.next());

        boolean[] isSafe = new boolean[N];
        Arrays.fill(isSafe, true);
        for(int i = 0; i < M; i++) {
            int a = Integer.parseInt(scanner.next());
            isSafe[a] = false;
        }

        Long[] dp = new Long[N+1];
        Arrays.fill(dp, 0L);
        dp[0] = 1L;
        if(M == 0 || isSafe[1]) dp[1] = 1L;

        for(int i = 2; i <= N; i++) {
            if (isSafe[i-1]) dp[i] += dp[i-1];
            if (isSafe[i-2]) dp[i] += dp[i-2];
            dp[i] %= MOD; // 1000000007 で割る
        }
        System.out.println(dp[N]);
    }
}

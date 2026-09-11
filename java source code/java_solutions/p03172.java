import java.util.*;
import java.io.*;
class Main {
    static long MOD = (long)(1e9+7);
    static long prefSum(long[] a, int left, int right) {
        long ans = a[right];
        if(left-1>=0)
            ans = ((ans-a[left-1])%MOD+MOD)%MOD;
        return ans;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] a = new int[n];
        for(int i = 0; i<n; i++)
            a[i] = sc.nextInt();
        long[][] dp = new long[n+1][k+1];
        for(int i = 0; i<=n; i++)
            dp[i][0] = 1;
        long[][] pref = new long[n+1][k+1];
        for(int i = 0; i<=n; i++)
            pref[i][0] = 1;
        for(int i = 1; i<=k; i++)
            pref[0][i] = pref[0][i-1];
        for(int i = 1; i<=n; i++) {
            for(int j = 1; j<=k; j++) {
                int numCandies = Math.min(a[i-1], j);
                dp[i][j] = prefSum(pref[i-1], j-numCandies, j);
                pref[i][j] = (pref[i][j-1] + dp[i][j])%MOD;
            }
        }
        System.out.println(dp[n][k]);
    }
}

import java.util.*;
 
class Main{
 
    void solve(){
        Scanner sc = new Scanner(System.in);
 
        double INF = Double.MAX_VALUE;
 
        while(true){
            int n = sc.nextInt();
            if(n==0) break;
 
            int[] a = new int[n+1];
            for(int i=1; i<=n; i++) a[i] = sc.nextInt();
            double b = sc.nextDouble();
            int r = sc.nextInt();
            double v = sc.nextDouble(), e = sc.nextDouble(), f = sc.nextDouble();
 
            double[][] times = new double[n+1][n+1];
            for(int i=0; i<=n; i++){
                for(int j=0; j<=n; j++){
                    double time = 0.0;
                    for(int k=0; k+a[i]<a[j]; k++){
                        if(k>=r) time += 1.0/(v - e * (k-r));
                        else time += 1.0/(v - f * (r-k));
                    }
                    times[i][j] = time;
                }
            }
 
            double[][] dp = new double[n+1][n+1];
            for(int i=0; i<=n; i++) Arrays.fill(dp[i], INF);
 
            for(int i=0; i<=n; i++){
                dp[0][i] = times[0][i];
                if(i!=n) dp[1][i] = dp[0][i] + b;
                else dp[1][i] = dp[0][i];
                dp[1][n] = Math.min(dp[1][n], dp[1][i] + times[i][n]);
            }
 
            for(int i=2; i<=n; i++){
                for(int j=0; j<n; j++){
                    dp[i][j] = Math.min(dp[i][j], dp[i-1][j]);
                    for(int l=0; l<=j; l++){
                        if(dp[i-1][l]==INF) continue;
                        dp[i][j] = Math.min(dp[i][j], dp[i-1][l] + times[l][j] + b);
                        dp[i][n] = Math.min(dp[i][n], dp[i-1][n]);
                        dp[i][n] = Math.min(dp[i][n], dp[i][j] + times[j][n]);
                    }
                }
            }
 
            System.out.printf("%.4f\n",dp[n][n]);
        }
    }
 
    public static void main(String[] args){
        new Main().solve();
    }
}

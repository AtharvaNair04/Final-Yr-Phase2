import java.util.*;

class Main{

    int[] cnt;
    int n,m,kk;
    int INF = Integer.MAX_VALUE;

    void solve(){
        Scanner sc = new Scanner(System.in);

        while(true){
            n = sc.nextInt();
            m = sc.nextInt();
            kk = sc.nextInt();
            if(n==0 && m==0 && kk==0) break;

            double mn = Math.pow(m,n);

            int[][] dp = new int[2][n*m+1];

            for(int i=1; i<=m; i++) dp[0][i] = 1;
            for(int i=0; i<n-1; i++){
                for(int k=1; k<=m; k++){
                    for(int j=0; j+k<=n*m; j++){
                        dp[(i+1)%2][j+k] += dp[i%2][j];
                    }
                }
                Arrays.fill(dp[i%2], 0);
            }

            double ans = 0.0;
            for(int i=1; i<=n*m; i++){
                if(dp[(n-1)%2][i]==0) continue;
                ans += ((double)dp[(n-1)%2][i]/mn) * Math.max(1, i-kk);
            }

            System.out.println(ans);
        }
    }

    public static void main(String[] args){
        new Main().solve();

    }
}

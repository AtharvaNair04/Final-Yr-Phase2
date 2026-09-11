import java.util.*;

public class Main {

    public static long dp[][] = new long[22][(1<<21)+1];
    public static long mod = (long) 1000000007;

    public static long solve(int i,int womenSet,int n,int arr[][]){
        if(i == n+1){
            if(womenSet == 0) return 1;
            else return 0;
        }
        if(dp[i][womenSet] != -1) return dp[i][womenSet];
        long ans = 0;
        for(int w=0;w<n;w++){
            boolean availble = ( (1<<w) & (womenSet) ) == 0 ? false : true;
            if( availble && (arr[i][w+1] == 1)  ) {
                ans = (ans + solve(i+1,womenSet ^ (1<<w),n,arr) ) % mod;
            }
        }

        return dp[i][womenSet] = ans;
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int arr[][] = new int[n+1][n+1];

        for(int i=1;i<=n;i++){
            for(int j=1;j<=n;j++){
                arr[i][j] = sc.nextInt();
            }
        }

        int womenSet = ( (1<<n) -1 );

        for(int i=0;i<=n;i++){
            for(int j=0;j<=womenSet;j++){
                dp[i][j] = -1;
            }
        }

        System.out.print(solve(1,womenSet,n,arr));
    }
}

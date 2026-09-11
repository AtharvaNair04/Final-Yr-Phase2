import java.util.*;
 
class Main{
 
    void solve(){
        Scanner sc = new Scanner(System.in);
         
         
        long[][][] dp = new long[1001][101][10];
        for(int i=0; i<=100; i++) dp[i][i][1] = 1;
           
        for(int i=2; i<=9; i++){
            for(int j=0; j<=100; j++){
                for(int k=j; k<=1000; k++){
                    for(int l=0; l<j; l++){
                        dp[k][j][i] += dp[k-j][l][i-1];
                    }
                }
            }
        }
         
 
        while(true){
            int n = sc.nextInt();
            int s = sc.nextInt();
            if(n==0 && s==0) break;
 
            long cnt = 0;
            for(int i=0; i<=100; i++) cnt += dp[s][i][n];
             
            System.out.println(cnt);
        }
    }
     
    public static void main(String[] args){
        new Main().solve();
    }
}

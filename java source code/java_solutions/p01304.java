import java.util.Scanner;
 
public class Main {
 
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int INF = Integer.MAX_VALUE;
 
    public static void main(String[] args) {
        doIt();
    }
 
    public static void doIt(){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
 
        for(int i = 0; i < n; i++){
            int gx = sc.nextInt();
            int gy = sc.nextInt();
            int p = sc.nextInt();
            boolean[][][] isM = new boolean[gx + 1][gy + 1][2];
           
            for(int j = 0; j < p; j++){
                int[][] point = new int[2][2];
                point[0][0] = sc.nextInt();
                point[0][1] = sc.nextInt();
                point[1][0] = sc.nextInt();
                point[1][1] = sc.nextInt();
                int from = 0,to = 0;
                if(point[0][0] < point[1][0] || point[0][1] < point[1][1]){
                    to = 1;
                }
                else{
                    from = 1;
                }
                isM[point[from][0]][point[from][1]][point[from][0] < point[to][0]? RIGHT : UP] = true;
            }
            int[][] dp = new int[gx + 1][gy + 1];
            dp[0][0] = 1;
          
            for(int j = 1; j < gy + 1; j++){
                
                if(isM[0][j - 1][UP] == false){
                    dp[0][j] = dp[0][j - 1];
                }
                else{
                    dp[0][j] = INF;
                }
            }
            for(int j = 1; j < gx + 1; j++){
                for(int k = 0; k < gy + 1; k++){
                    boolean bOK = false;
                  
                    if(0 <= j - 1 && isM[j - 1][k][RIGHT] == false && dp[j - 1][k] < INF){
                        dp[j][k] += dp[j - 1][k];
                        bOK = true;
                    }
                   
                    if(0 <= k - 1 && isM[j][k - 1][UP] == false && dp[j][k - 1] < INF){
                        dp[j][k] += dp[j][k - 1];
                        bOK = true;
                    }
                    if(bOK == false){
                        dp[j][k] = INF;
                    }
                }
            }
            //print(dp);
            if(dp[gx][gy] == INF){
                System.out.println("Miserable Hokusai!");
            }
            else{
                System.out.println(dp[gx][gy]);
            }
        }
    }
 
    public static void print(int[][] a){
        for(int[] n : a){
            for(int l : n){
                System.out.print(l + " ");
            }
            System.out.println();
        }
    }
 
}

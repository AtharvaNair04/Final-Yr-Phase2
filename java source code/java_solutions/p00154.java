import java.util.*;

public class Main{
    void run(){
        Scanner sc = new Scanner(System.in);
        while(true){
            int[] card_num = new int[101];
            int m = sc.nextInt();
            if(m==0){
                break;
            }
            int[] value = new int[m];
            int[] num = new int[m];
            for(int i = 0; i < m; i++){
                value[i] = sc.nextInt();
                num[i] = sc.nextInt();
            }
            int[] dp = new int[1001];
            dp[0] = 1;
            for(int i = 0; i < m; i++){
                for(int ii = 1000 - value[i] -1; ii >= 0; ii--){
                    for(int iii = 1; iii <= num[i]; iii++){
                        if(ii + value[i]*iii <= 1000){
                            dp[ii + value[i]*iii] += dp[ii];
                        }
                    }
                }
            }
            /*
              System.out.println(Arrays.toString(dp));
              for(int i = 0; i < 100; i++){
              System.out.println(i + " : " + dp[i]);
              }
            */
            int g = sc.nextInt();
            for(int i = 0; i < g; i++){
                System.out.println(dp[sc.nextInt()]);
            }
        }
    }
    public static void main(String[] args){
        new Main().run();
    }
}

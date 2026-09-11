import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        int len = str.length();
        long mod = (int)Math.pow(10,9)+7;
        long[][] dp = new long[(int)(Math.pow(10, 5))+1][13];
        for(int i=0; i<13; i++) {
            dp[0][i] = 0;
        }
        dp[0][0] = 1;
        int cnt = 0;
        long total = 1;
        
        for(int i=0; i<len; i++) {
            char s = str.charAt(i);
            if(s == '?') {
                for(int j=0; j<13; j++) {
                    dp[i+1][j] = (total+mod*10-dp[i][4-(int)((j+2)/3)]-dp[i][8-(int)((j+1)/3)]-dp[i][12-(int)(j/3)])%mod;
                }
                total = (total*10)%mod;
            } else {
                for(int j=0; j<13; j++) {
                    dp[i+1][j] = 0;
                }
                for(int j=0; j<13; j++) {
                    dp[i+1][(j*10+Character.getNumericValue(s))%13] += dp[i][j];
                }
            }
        }
        System.out.println(dp[len][5]%mod);
        sc.close();
    }
}

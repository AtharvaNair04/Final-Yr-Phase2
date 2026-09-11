import java.util.*;

public class Main {
    public static void main(String[] args) {
        final int DIV = 1000000007;
        Scanner sc = new Scanner(System.in);
        int h = sc.nextInt();
        int w = sc.nextInt();
        int k = sc.nextInt();
        sc.close();

        if(w == 1){
            System.out.println(1);
            return;
        }else if(w == 2){
            System.out.println((int)Math.pow(2, h-1));
            return;
        }

        long[] row = new long[w-1];
        long num = 0;
        out: for(int i=0; i<(int)Math.pow(2, w-1); i++){
            char[] set = String.format("%" + (w-1) + "s", Integer.toString(i, 2)).replace(' ', '0').toCharArray();
            for(int j=0; j<w-2; j++){
                if(set[j] == '1' && set[j+1] == '1') continue out;
            }
            num++;
            for(int j=0; j<w-1; j++){
                if(set[j] == '1') row[j]++;
            }
        }

        long[][] dp = new long[h+1][w];
        // Arrays.fill(dp[0], 1);
        dp[0][0] = 1;

        for(int i=0; i<h; i++){
            dp[i+1][0] = dp[i][0] * (num - row[0]) + dp[i][1] * row[0];
            dp[i+1][w-1] = dp[i][w-1] * (num - row[w-2]) + dp[i][w-2] * row[w-2];
            for(int j=1; j<w-1; j++){
                dp[i+1][j] = dp[i][j] * (num - row[j-1] - row[j])
                            + dp[i][j-1] * row[j-1] + dp[i][j+1] * row[j];
            }
            for(int j=0; j<w; j++){
                dp[i+1][j] %= DIV;
            }
        }

        System.out.println(dp[h][k-1]);
    }
}

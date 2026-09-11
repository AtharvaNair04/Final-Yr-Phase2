import java.util.Scanner;

public class Main {
    Scanner sc = new Scanner(System.in);
    
    void run() {
        for (;;) {
            String in = sc.next();
            if (in.equals("#END")) {
                return;
            }
            String ans = "";
            for (int i = 1; i < in.length(); i++) {
                String lcs = lcs(in.substring(0, i), in.substring(i, in.length()));
                if (lcs.length() > ans.length()) {
                    ans = lcs;
                }
            }
            System.out.println(ans);
        }
    }
    
    String lcs(String s, String t) {
        int n = s.length();
        int m = t.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (s.charAt(i) == t.charAt(j)) {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                } else {
                    dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }
        char[] res = new char[dp[n][m]];
        while (n > 0 && m > 0) {
            if (dp[n][m] == dp[n - 1][m]) {
                n--;
            } else if (dp[n][m] == dp[n][m - 1]) {
                m--;
            } else {
                res[dp[n - 1][m - 1]] = s.charAt(n - 1);
                n--;
                m--;
            }
        }
        return String.valueOf(res);
    }
    
    public static void main(String[] args) {
        new Main().run();
    }
}

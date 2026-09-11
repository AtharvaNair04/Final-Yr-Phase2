import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    resolve(System.in, System.out);
  }

  static void resolve(InputStream is, PrintStream pw) {
    Scanner sc = new Scanner(is);
    int n = Integer.parseInt(sc.next());
    long[] a = new long[n];
    for (int i = 0; i < n; i++) {
      a[i] = Long.parseLong(sc.next());
    }
    long[][] dp = new long[n + 1][2];
    dp[0][0] = 0;
    dp[0][1] = Integer.MIN_VALUE;

    for (int i = 0; i < n; i++) {
      dp[i + 1][0] = Math.max(dp[i][0] + a[i], dp[i][1] - a[i]);
      dp[i + 1][1] = Math.max(dp[i][0] - a[i], dp[i][1] + a[i]);
    }
    pw.println(dp[n][0]);
  }
}

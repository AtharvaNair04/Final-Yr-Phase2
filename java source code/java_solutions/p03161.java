import java.util.Arrays;
import java.util.Scanner;

class Main {

  private static long INF = Long.MAX_VALUE / 4;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int N = Integer.parseInt(scanner.next());
    int K = Integer.parseInt(scanner.next());
    int[] h = new int[N];
    for (int i = 0; i < N; i++) {
      h[i] = Integer.parseInt(scanner.next());
    }
    scanner.close();
    long[] dp = new long[N];
    Arrays.fill(dp, INF);
    dp[0] = 0;
    for (int i = 0; i < N; i++) {
      for (int j = 1; j <= K && i - j >= 0; j++) {
        dp[i] = Math.min(dp[i], dp[i - j] + (long) Math.abs(h[i] - h[i - j]));
      }
    }
    System.out.println(dp[N - 1]);
  }

}

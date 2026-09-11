import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int N = scanner.nextInt();
    int K = scanner.nextInt();
    int[] h = new int[N + 1];
    for (int i = 1; i <= N; i++) h[i] = scanner.nextInt();

    long[][] dp = new long[N + 1][N + 1];
    final long INF = 1_000_000_000_000L;
    for (int i = 0; i <= N; i++) for (int j = 0; j <= N; j++) dp[i][j] = INF;
    dp[0][0] = 0;
    for (int i = 1; i <= N; i++) {
      for (int j = 1; j <= i; j++) {
        for (int k = 0; k < i; k++)
          dp[i][j] = Math.min(dp[i][j], dp[k][j - 1] + Math.max(h[i] - h[k], 0));
      }
    }

    long min = INF;
    for (int i = 0; i <= N; i++) min = Math.min(min, dp[i][N - K]);
    System.out.println(min);
  }
}

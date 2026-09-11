import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int N = sc.nextInt();
		int T = sc.nextInt();
		int S = sc.nextInt();
		int[] A = new int[N];
		int[] B = new int[N];
		for (int i = 0; i < N; ++i) {
			A[i] = sc.nextInt();
			B[i] = sc.nextInt();
		}
		int[][] dp = new int[T + 1][N + 1];
		for (int i = 0; i < T; ++i) {
			for (int j = 0; j < N; ++j) {
				int n = i + B[j];
				if ((S <= i || n <= S) && n <= T) {
					dp[n][j + 1] = Math.max(dp[n][j + 1], dp[i][j] + A[j]);
				}
				dp[i][j + 1] = Math.max(dp[i][j + 1], dp[i][j]);
				dp[i + 1][j + 1] = Math.max(dp[i + 1][j + 1], dp[i][j + 1]);
			}
		}
		int ans = 0;
		for (int i = 0; i <= T; ++i) {
			for (int j = 0; j <= N; ++j) {
				ans = Math.max(ans, dp[i][j]);
			}
		}
		System.out.println(ans);
	}
}

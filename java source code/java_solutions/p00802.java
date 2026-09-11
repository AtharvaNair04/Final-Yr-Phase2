import java.util.Arrays;
import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		while (true) {
			int N = sc.nextInt();
			int M = sc.nextInt();
			if (N == 0) break;
			double[] p = new double[N];
			for (int i = 0; i < N; ++i) {
				p[i] = sc.nextDouble();
			}
			double[][][] dp = new double[N][N][M + 1];
			for (double[][] aa : dp) {
				for (double[] a : aa) {
					Arrays.fill(a, -9);
				}
			}
			for (int i = 0; i < N; ++i) {
				dp[i][i][1] = 0;
				for (int j = 0; j < i; ++j) {
					for (int k = 0; k <= j; ++k) {
						for (int l = 1; l < M; ++l) {
							if (dp[j][k][l] < -7) continue;
							dp[i][k][l + 1] = Math.max(dp[i][k][l + 1], dp[j][k][l] + 0.5 * Math.sin((p[i] - p[j]) * 2.0 * Math.PI));
						}
					}
				}
			}
			double ans = 0;
			for (int i = 0; i < N; ++i) {
				for (int j = 0; j < i; ++j) {
					if (dp[i][j][M - 1] < -7) continue;
					for (int k = i + 1; k < N; ++k) {
						ans = Math.max(ans, dp[i][j][M - 1] + 0.5	* (Math.sin((1.0 + p[j] - p[k]) * 2.0 * Math.PI) + Math.sin((p[k] - p[i]) * 2.0 * Math.PI)));
					}
				}
			}
			System.out.printf("%.6f\n", ans);
		}
	}
}

import java.util.Arrays;
import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);
	static int S, D, M;
	static int[][] P, W;

	public static void main(String[] args) {
		while (sc.hasNext()) {
			S = sc.nextInt();
			D = sc.nextInt();
			M = sc.nextInt();
			W = new int[S][];
			P = new int[S][];
			for (int i = 0; i < S; ++i) {
				int K = sc.nextInt();
				W[i] = new int[K];
				P[i] = new int[K];
				for (int j = 0; j < K; ++j) {
					W[i][j] = sc.nextInt();
					P[i][j] = sc.nextInt();
				}
			}
			int[] dp = new int[M + 1];
			Arrays.fill(dp, -1);
			dp[M] = 0;
			for (int i = 0; i < D; ++i) {
				int F = sc.nextInt();
				for (int j = 0; j < P[F].length; ++j) {
					for (int k = 0; k <= M - P[F][j]; ++k) {
						if (dp[k + P[F][j]] == -1) continue;
						dp[k] = Math.max(dp[k], dp[k + P[F][j]] + W[F][j]);
					}
				}
			}
			int maxW = 0;
			int ansP = 0;
			for (int i = M; i >= 0; --i) {
				if (dp[i] > maxW) {
					maxW = dp[i];
					ansP = M - i;
				}
			}
			System.out.println(maxW + " " + ansP);
		}
	}
}

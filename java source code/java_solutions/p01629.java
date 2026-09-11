import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int N = sc.nextInt();
		int D = sc.nextInt();
		int[][] P = new int[N][D];
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < D; ++j) {
				P[i][j] = sc.nextInt();
			}
		}
		int INF = 1 << 28;
		int[][][] dp = new int[D + 1][N][D + 1];
		int[][][] prev = new int[D + 1][N][D + 1];
		for (int[][] aa : dp) {
			for (int[] a : aa) {
				Arrays.fill(a, INF);
			}
		}
		for (int i = 0; i < N; ++i) {
			dp[D][i][0] = 0;
		}
		for (int i = D - 1; i >= 0; --i) {
			for (int j = 0; j < N; ++j) {
				for (int k = 0; k < N; ++k) {
					for (int l = 0; l < D - i; ++l) {
						if (dp[i + 1][k][l] == INF) continue;
						if (j == k) {
							if (dp[i + 1][k][l] + P[j][i] < dp[i][j][l]) {
								dp[i][j][l] = dp[i + 1][k][l] + P[j][i];
								prev[i][j][l] = k;
							}
						} else {
							if (dp[i + 1][k][l] + P[j][i] < dp[i][j][l + 1]) {
								dp[i][j][l + 1] = dp[i + 1][k][l] + P[j][i];
								prev[i][j][l + 1] = k;
							}
						}
					}
				}
			}
		}

		int min = INF;
		int minM = 0;
		int minH = 0;
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j <= D; ++j) {
				if (dp[0][i][j] < min || (dp[0][i][j] == min && j < minM)) {
					min = dp[0][i][j];
					minM = j;
					minH = i;
				}
			}
		}
		System.out.println(min + " " + minM);
		for (int i = 0; i < D; ++i) {
			System.out.println(minH + 1);
			int p = prev[i][minH][minM];
			if (p != minH) --minM;
			minH = p;
		}
	}
}

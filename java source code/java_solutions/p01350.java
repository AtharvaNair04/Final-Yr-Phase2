import java.util.Arrays;
import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);
	static int N;
	static boolean[][][] ok;
	static double[][] dist;

	public static void main(String[] args) {
		N = sc.nextInt();
		double R = sc.nextDouble();
		double theta = sc.nextDouble();
		int[] X = new int[N];
		int[] Y = new int[N];
		for (int i = 0; i < N; ++i) {
			X[i] = sc.nextInt();
			Y[i] = sc.nextInt();
		}
		ok = new boolean[N][N][N];
		dist = new double[N][N];
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				dist[i][j] = Math.sqrt((X[i] - X[j]) * (X[i] - X[j]) + (Y[i] - Y[j]) * (Y[i] - Y[j]));
			}
		}
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				if (i == j) continue;
				for (int k = 0; k < N; ++k) {
					if (k == i || k == j) continue;
					int dx1 = X[j] - X[i];
					int dx2 = X[k] - X[j];
					int dy1 = Y[j] - Y[i];
					int dy2 = Y[k] - Y[j];
					double arg = Math.abs(Math.acos((dx1 * dx2 + dy1 * dy2) / dist[i][j] / dist[j][k])) * 180 / Math.PI;
					ok[i][j][k] = arg <= theta;
				}
			}
		}
		double[][][] dp = new double[2][N][N];
		for (double[][] a : dp) {
			for (double[] aa : a) {
				Arrays.fill(aa, -1);
			}
		}
		boolean any = false;
		for (int i = 1; i < N; ++i) {
			if (dist[0][i] <= R) {
				any = true;
				dp[0][0][i] = R - dist[0][i];
			}
		}
		if (!any) {
			System.out.println(0);
			return;
		}
		int t = 1;
		for (int turn = 1;; ++turn) {
			for (double[] a : dp[t]) {
				Arrays.fill(a, -1);
			}
			boolean update = false;
			for (int i = 0; i < N; ++i) {
				for (int j = 0; j < N; ++j) {
					if (dp[1 - t][i][j] < 0) continue;
					for (int k = 0; k < N; ++k) {
						if (!ok[i][j][k] || dp[1 - t][i][j] < dist[j][k]) continue;
						update = true;
						dp[t][j][k] = Math.max(dp[t][j][k], dp[1 - t][i][j] - dist[j][k]);
					}
				}
			}
			if (!update) {
				System.out.println(turn);
				break;
			}
			t = 1 - t;
		}
	}

}

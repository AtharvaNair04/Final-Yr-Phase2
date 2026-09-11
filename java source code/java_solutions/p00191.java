import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		new Main().run();
	}

	private void run() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			int n = scanner.nextInt();
			int m = scanner.nextInt();
			if ((n | m) == 0)
				break;
			double[][] map = new double[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					map[i][j] = scanner.nextDouble();
				}
			}
			double[][] dp = new double[m][n];
			for (int i = 0; i < n; i++) {
				dp[0][i] = 1.0;
			}
			for (int i = 1; i < m; i++) {
				for (int j = 0; j < n; j++) {
					for (int k = 0; k < n; k++) {
						dp[i][j] = Math.max(dp[i][j], dp[i - 1][k] * map[k][j]);
					}
				}
			}
			double max = 0;
			for (int i = 0; i < n; i++) {
				max = Math.max(max, dp[m - 1][i]);
			}
			System.out.println(String.format("%.2f", max));

		}
	}
}

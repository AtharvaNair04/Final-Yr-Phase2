import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		new Main().solver();
	}

	void solver() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		double[] a = new double[n];
		for (int i = 0; i < n; i++) {
			a[i] = sc.nextDouble();
		}
		final int max = 200001;
		double[][] dp = new double[2][max];
		double div = 1.0 / a[0];
		for (int i = 0; i < max; i++) {
			dp[0][i] = Math.abs(a[0] - i) * div;
		}
		for (int i = 1; i < n; i++) {
			int t = i & 1;
			Arrays.fill(dp[t], 1e9);
			div = 1.0 / a[i];
			for (int j = 1; j < max; j++) {
				for (int k = j; k < max; k += j) {
					dp[t][k] = Math.min(dp[t][k], Math.max(dp[1 - t][j], Math.abs(a[i] - k) * div));
				}
			}
		}
		double ans = 1e9;
		for (int i = 1; i < max; i++) {
			ans = Math.min(ans, dp[(n - 1) & 1][i]);
		}
		System.out.printf("%.9f\n",ans);
	}
}

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long k = sc.nextLong();

		int d = 0;
		long x = 1;
		while (x <= k) {
			d++;
			x = x << 1;
		}

		long answer = 0;
		int[] ones = new int[d + 1];
		for (int i = 0; i < n; i++) {
			long a = sc.nextLong();
			answer += a;

			int j = 0;
			while (a > 0 && j <= d) {
				ones[j++] += a % 2;
				a /= 2;
			}
		}

		long[] delta = new long[d + 1];
		for (int i = 0; i <= d; i++) {
			delta[i] = (n - 2L * ones[i]) << i;
		}

		long[][] dp = new long[d + 1][2];
		dp[d][1] = answer;

		for (int i = d; i > 0; i--) {
			dp[i - 1][1] = dp[i][1];
			if (dp[i][0] > 0) dp[i - 1][0] = Math.max(
					dp[i][0], dp[i][0] + delta[i - 1]);

			if (((k >> (i - 1)) & 1) == 1) {
				dp[i - 1][0] = Math.max(dp[i - 1][0], dp[i][1]);
				dp[i - 1][1] += delta[i - 1];
			}
		}

		System.out.println(Math.max(dp[0][0], dp[0][1]));
	}
}

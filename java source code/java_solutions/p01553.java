import java.util.Arrays;
import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		run();
	}

	static void run() {
		new Main().solver();
	}

	long MOD = 1_000_000_000 + 7;

	void solver() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			String s = sc.next();
			if (s.charAt(0) == 'U')
				c[i] = 1;
			else if (s.charAt(0) == 'D')
				c[i] = -1;
			else if (s.charAt(0) == '-')
				c[i] = 0;
		}
		c = reduce(c);
		n = c.length;
		long[][] dp = new long[n + 1][n + 1];
		dp[0][0] = 1;
		for (int i = 0; i < n; i++) {
			if (c[i] == -1) {// D
				for (int j = 0; j <= i; j++) {
					if (j >= 1) {
						dp[i + 1][j - 1] += j * j * dp[i][j];
						dp[i + 1][j - 1] %= MOD;
					}
					dp[i + 1][j] += j * dp[i][j];
					dp[i + 1][j] %= MOD;
				}
			} else if (c[i] == 1) {// U
				for (int j = 0; j <= i; j++) {
					dp[i + 1][j + 1] += dp[i][j];
					dp[i + 1][j] += j * dp[i][j];
					dp[i + 1][j + 1] %= MOD;
					dp[i + 1][j] %= MOD;
				}
			}
		}
		System.out.println(dp[n][0]);
	}

	int[] reduce(int[] c) {
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0)
				len++;
		}
		int[] ret = new int[len];
		int count = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0) {
				ret[count++] = c[i];
			}
		}
		return ret;
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

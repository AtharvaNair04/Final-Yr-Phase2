import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String a = scanner.nextBigInteger().subtract(BigInteger.ONE).toString();
		String b = scanner.next();
		M = scanner.nextInt();

		System.out.println((slove(b) - slove(a) + mod) % mod);
	}

	private int slove(String s) {
		n = s.length();
		num = new int[n + 1];
		for (int i = 0; i < n; i++)
			num[n - i - 1] = s.charAt(i) - '0';
		dp = new int[2][2][10][n][M];
		for (int[][][][] dp1 : dp)
			for (int[][][] dp2 : dp1)
				for (int[][] dp3 : dp2)
					for (int[] dp4 : dp3)
						Arrays.fill(dp4, -1);
		int res = 0;
		for (int i = n - 1; i >= 0; i--)
			for (int j = 1; j <= (i == n - 1 ? num[i] : 9); j++) {
				res = (res + slove(i, j % M, j, 1, i == n - 1 && j == num[i]))
						% mod;
				if (i != 0)
					res = (res + slove(i, j % M, j, 0, i == n - 1
							&& j == num[i]))
							% mod;

			}

		return res % mod;
	}

	private int slove(int p, int m, int pre, int s, boolean b) {
		if (p == 0)
			return m == 0 ? 1 : 0;
		if (dp[s][b ? 1 : 0][pre][p][m] != -1)
			return dp[s][b ? 1 : 0][pre][p][m];
		int ans = 0;

		if (s == 0 || s == 2) {
			int size = (b ? num[p - 1] : 9);
			for (int i = pre + 1; i <= size; i++)
				ans += slove(p - 1, (m * 10 + i) % M, i, 1, b
						&& i == num[p - 1]);
		}
		if (s == 1 || s == 2) {
			int size = Math.min(b ? num[p - 1] : 9, pre - 1);
			for (int i = 0; i <= size; i++)
				ans += slove(p - 1, (m * 10 + i) % M, i, 0, b
						&& i == num[p - 1]);
		}
		return dp[s][b ? 1 : 0][pre][p][m]= ans % mod;

	}

	int M, n;
	int mod = 10000;
	int[] num;
	int[][][][][] dp;
}

import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	static long MOD = 1000000007;
	static char[] A, B, C;

	public static void main(String[] args) {
		while (sc.hasNext()) {
			A = sc.next().toCharArray();
			if (!sc.hasNext()) break;
			B = sc.next().toCharArray();
			C = sc.next().toCharArray();
			System.out.println(solve());
		}
	}

	static long solve() {
		long[][] dp = new long[2][A.length + 1];
		dp[0][A.length] = 1;
		for (int i = A.length - 1; i >= 0; --i) {
			for (int j = 0; j < 10; ++j) {
				if (A[i] != '?' && A[i] - '0' != j) continue;
				for (int k = 0; k < 10; ++k) {
					if (B[i] != '?' && B[i] - '0' != k) continue;
					if (i == 0 && (j == 0 || k == 0)) continue;
					for (int carry = 0; carry <= 1; ++carry) {
						int sum = j + k + carry;
						if (C[i] != '?' && sum % 10 != C[i] - '0') continue;
						dp[sum / 10][i] += dp[carry][i + 1];
						dp[sum / 10][i] %= MOD;
					}
				}
			}
		}
		return dp[0][0];
	}
}

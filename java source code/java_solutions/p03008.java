import java.io.*;
import java.util.*;

class Main {

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] a = new int[3];
		int[] b = new int[3];
		for (int i = 0; i < 3; i++) {
			a[i] = sc.nextInt();
		}
		for (int i = 0; i < 3; i++) {
			b[i] = sc.nextInt();
		}

		int[] dp = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			dp[i] = i;
			for (int j = 0; j < 3; j++) {
				if (i >= a[j]) dp[i] = Math.max(dp[i], dp[i - a[j]] + b[j]);
			}
		}

		int m = dp[n];
		long[] dp2 = new long[m + 1];
		for (int i = 1; i <= m; i++) {
			dp2[i] = i;
			for (int j = 0; j < 3; j++) {
				if (i >= b[j]) dp2[i] = Math.max(dp2[i], dp2[i - b[j]] + a[j]);
			}
		}

		System.out.println(dp2[m]);
	}
}

import java.util.Scanner;

public class Main {
	static int n;
	static int top[];
	static int bot[];
	static long dp[][];

	public static long f(int left, int right) {
		if (0 < dp[left][right]) {
			return dp[left][right];
		}
		if (left == right) {
			return dp[left][right] = 0;
		}
		
		long min = Long.MAX_VALUE;
		for (int i = left; i < right; i++) {
			long costL = f(left, i);
			long costR = f(i + 1, right);
			long costM = top[left] * bot[i] * top[i + 1] * bot[right];
			min = Math.min(min, costL + costR + costM);
		}
		
		return dp[left][right] = min;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		n = sc.nextInt();
		top = new int[n];
		bot = new int[n];
		dp = new long[n][n];
		for (int i = 0; i < n; i++) {
			top[i] = sc.nextInt();
			bot[i] = sc.nextInt();
		}

		System.out.println(f(0, n - 1));
	}
}

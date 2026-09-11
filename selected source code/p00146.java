import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static double dp[][];
	static int n;
	static int num[];
	static double dir[];
	static double val[];
	static int next[][];

	public static double f(int k, int bit) {
		if (0 < dp[k][bit]) {
			return dp[k][bit];
		}
		if ((bit | (1 << k)) == ((1 << n) - 1)) {
			return dp[k][bit] = 0;
		}

		double weight = 0;
		for (int i = 0; i < n; i++) {
			if (0 < (bit & (1 << i))) {
				weight += 20 * val[i];
			}
		}
		weight += 20 * val[k];

		double min = Double.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			if ((bit & (1 << i)) == 0 && i != k) {
				double s = f(i, bit | (1 << k));
				double t = 1.0 * Math.abs(dir[k] - dir[i]) * (70 + weight) / 2000;
				if (s + t < min) {
					min = s + t;
					next[k][bit] = i;
				}
			}
		}

		return dp[k][bit] = min;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		n = sc.nextInt();
		num = new int[n];
		dir = new double[n];
		val = new double[n];
		dp = new double[n][1 << n];
		next = new int[n][1 << n];
		for (int i = 0; i < n; i++) {
			num[i] = sc.nextInt();
			dir[i] = sc.nextInt();
			val[i] = sc.nextInt();
		}
		for (int i = 0; i < n; i++) {
			Arrays.fill(dp[i], -1.0);
			Arrays.fill(next[i], -1);
		}

		for (int i = 0; i < n; i++) {
			f(i, 0);
		}

		int id = 0;
		for (int i = 0; i < n; i++) {
			if (0 <= dp[i][0] && dp[i][0] < dp[id][0]) {
				id = i;
			}
		}

		String s = "";
		int bit = 0;
		for (int i = 0; i < n; i++) {
			System.out.print(s + num[id]);
			int nid = next[id][bit];
			bit |= (1 << id);
			s = " ";
			id = nid;
		}
		System.out.println();
	}
}

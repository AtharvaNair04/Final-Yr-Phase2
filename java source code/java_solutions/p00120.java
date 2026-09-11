import java.util.Scanner;

public class Main {
	static double dp[][][];
	static int n, a;
	static double r[];

	public static double f(int left, int right, int bit) {
		if (0 < dp[left][right][bit]) {
			return dp[left][right][bit];
		}
		int k = check(bit);
		if (k == 1) {
			return dp[left][right][bit] = 2.0 * r[left];
		}
		if (k == 2) {
			return dp[left][right][bit] = r[left] + r[right] + 2.0 * Math.sqrt(r[left] * r[right]);
		}

		double min = Double.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			if (i == left || i == right || (bit & (1 << i)) == 0) {
				continue;
			}
			for (int j = 0; j < n; j++) {
				if (j == left || j == right || (bit & (1 << j)) == 0) {
					continue;
				}
				if (3 < k && i == j) {
					continue;
				}
				double sub = f(i, j, (bit ^ (1 << left)) ^ (1 << right));
				sub -= r[i] + r[j];
				sub += 2.0 * (Math.sqrt(r[left] * r[i]) + Math.sqrt(r[right] * r[j]));
				sub += r[left] + r[right];
				min = Math.min(min, sub);
			}
		}
		return dp[left][right][bit] = min;
	}

	public static int check(int bit) {
		int count = 0;

		for (int i = 0; i < 12; i++) {
			if (0 < (bit & (1 << i))) {
				count++;
			}
		}
		return count;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (sc.hasNext()) {
			String b[] = sc.nextLine().split(" ");
			a = Integer.parseInt(b[0]);
			n = b.length - 1;
			r = new double[n];
			dp = new double[n][n][1 << n];
			for (int i = 0; i < n; i++) {
				r[i] = Integer.parseInt(b[i + 1]);
			}
			double min = Double.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (n == 1 || i != j) {
						min = Math.min(min, f(i, j, (1 << n) - 1));
					}
				}
			}
			System.out.println(min <= a ? "OK" : "NA");
		}
	}
}

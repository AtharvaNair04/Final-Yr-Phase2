import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	int n, m;
	int[] c, x;
	int[][] dp;

	int dp(int k, int pre) {
		if (n <= k) return 0;
		if (0 <= dp[k][pre]) return dp[k][pre];

		int res = 1 << 30;

		for (int i = 0; i < m; i++) {
			int y = pre + c[i];
			if (255 < y) {
				y = 255;
			}
			if (y < 0) {
				y = 0;
			}
			int diff = Math.abs(x[k] - y);
			res = Math.min(res, dp(k + 1, y) + diff*diff);
		}

		return dp[k][pre] = res;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}
			c = sc.nextIntArray(m);
			x = sc.nextIntArray(n);
			dp = new int[n][300];
			for (int i = 0; i < n; i++) {
				Arrays.fill(dp[i], -1);
			}

			/*
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < 256; j++) {
					for (int k = 0; k < m; k++) {
						dp[i][j] = 0;
					}
				}
			}
			 */

			for (int i = 255; 0 <= i; i--) {
				for (int j = n - 1; 0 <= j; j--) {
					dp(j, i);
				}
			}

			System.out.println(dp(0, 128));
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

	public void mapDebug(int[][] a) {
		System.out.println("--------map display---------");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.printf("%3d ", a[i][j]);
			}
			System.out.println();
		}
		System.out.println("----------------------------" + '\n');
	}

	class MyScanner {
		int read() {
			try {
				return System.in.read();
			} catch (IOException e) {
				throw new InputMismatchException();
			}
		}

		boolean isSpaceChar(int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		boolean isEndline(int c) {
			return c == '\n' || c == '\r' || c == -1;
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		int[] nextIntArray(int n) {
			int[] array = new int[n];
			for (int i = 0; i < n; i++)
				array[i] = nextInt();
			return array;
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		long[] nextLongArray(int n) {
			long[] array = new long[n];
			for (int i = 0; i < n; i++)
				array[i] = nextLong();
			return array;
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}

		double[] nextDoubleArray(int n) {
			double[] array = new double[n];
			for (int i = 0; i < n; i++)
				array[i] = nextDouble();
			return array;
		}

		String next() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		String[] nextStringArray(int n) {
			String[] array = new String[n];
			for (int i = 0; i < n; i++)
				array[i] = next();

			return array;
		}

		String nextLine() {
			int c = read();
			while (isEndline(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isEndline(c));
			return res.toString();
		}
	}
}

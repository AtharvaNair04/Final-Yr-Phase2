import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	int n, m, r;
	int[] perm;
	int[][] ld, sd;
	int[][] dp;
	int INF = 1 << 20;

	int dp(int k, int s) {
		if (k == r - 1) {
			return 0;
		}
		if (0 <= dp[k][s]) {
			return dp[k][s];
		}

		int res = INF;

		int pos = perm[k];
		int npos = perm[k + 1];

		if (pos == s) {
			res = Math.min(res, ld[pos][npos] + dp(k + 1, s));
			res = Math.min(res, sd[pos][npos] + dp(k + 1, npos));
			for (int i = 0; i < n; i++) {
				res = Math.min(res, sd[pos][i] + ld[i][npos] + dp(k + 1, i));
			}
		} else {
			res = ld[pos][npos] + dp(k + 1, s);
			res = Math.min(res, ld[pos][s] + sd[s][npos] + dp(k + 1, npos));
			for (int i = 0; i < n; i++) {
				res = Math.min(res, ld[pos][s] + sd[s][i] + ld[i][npos] + dp(k + 1, i));
			}
		}

		return dp[k][s] = res;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}
			ld = new int[n][n];
			sd = new int[n][n];
			for (int i = 0; i < n; i++) {
				Arrays.fill(ld[i], INF);
				Arrays.fill(sd[i], INF);
				ld[i][i] = sd[i][i] = 0;
			}
			for (int i = 0; i < m; i++) {
				int x = sc.nextInt() - 1;
				int y = sc.nextInt() - 1;
				int t = sc.nextInt();
				char c = sc.next().charAt(0);
				if (c == 'L') {
					ld[x][y] = ld[y][x] = t;
				} else {
					sd[x][y] = sd[y][x] = t;
				}
			}
			r = sc.nextInt();
			perm = new int[r];
			for (int i = 0; i < r; i++) {
				perm[i] = sc.nextInt() - 1;
			}

			for (int k = 0; k < n; k++) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						ld[i][j] = Math.min(ld[i][j], ld[i][k] + ld[k][j]);
						sd[i][j] = Math.min(sd[i][j], sd[i][k] + sd[k][j]);
					}
				}
			}

			dp = new int[r][n];
			for (int i = 0; i < r; i++) {
				Arrays.fill(dp[i], -1);
			}
			int ans = dp(0, perm[0]);
			System.out.println(ans);
			System.gc();
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

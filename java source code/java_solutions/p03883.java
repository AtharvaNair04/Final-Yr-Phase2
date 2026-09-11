import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Main {

	public void run() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long[][] a = new long[n][2];
		for (int i = 0; i < n; ++i) {
			a[i][0] = sc.nextLong();
			a[i][1] = sc.nextLong();
		}
		Arrays.sort(a, new Comparator<long[]>() {
			@Override
			public int compare(long[] arg0, long[] arg1) {
				return -Long.compare(arg0[0] + arg0[1], arg1[0] + arg1[1]);
			}
		});
		long ans = Long.MAX_VALUE / 3;
		long[][][] g = new long[2][n / 2 + 10][n / 2 + 10];
		for (int i = 0; i < g.length; ++i)
			for (int j = 0; j < g[i].length; ++j)
				for (int k = 0; k < g[i][j].length; ++k)
					g[i][j][k] = Long.MAX_VALUE / 10;
		g[0][0][0] = 0;
		for (int i = 0; i < n; ++i) {
			for (int k = 0; k <= 1; ++k) {
				for (int j = Math.max(0, -n / 2 - k + i); j <= i - k && j <= n / 2; ++j) {
					g[k][i - j - k][j + 1] = Math.min(g[k][i - j - k][j + 1],
							g[k][i - j][j] + a[i][0] + j * (a[i][0] + a[i][1]));
					g[k][i - j - k + 1][j] = Math.min(g[k][i - j - k + 1][j],
							g[k][i - j - k][j] + a[i][1] + (i - j - k) * (a[i][0] + a[i][1]));
				}
			}
			if (n % 2 == 1) {
				for (int j = Math.max(0, -n / 2 + i); j <= Math.min(n / 2, i); ++j) {
					g[1][i - j][j] = Math.min(g[1][i - j][j],
							g[0][i - j][j] + (n - 1) / 2 * (a[i][0] + a[i][1]));
				}
			}
		}
		for (int i = 0; i < g.length; ++i)
			for (int j = 0; j < g[i].length; ++j)
				for (int k = 0; k < g[i][j].length; ++k) {
					if (i + j + k == n) {
						ans = Math.min(ans, g[i][j][k]);
					}
				}
		System.out.println(ans);
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Main().run();
	}
}

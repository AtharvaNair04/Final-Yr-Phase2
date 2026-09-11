import java.util.Arrays;
import java.util.Scanner;

class Main {

	@SuppressWarnings("unchecked")
	public void run() {
		Scanner sc = new Scanner(System.in);
		int[] p = new int[100];
		long[][][] f = new long[100 + 1][100 * 500 + 1][2];
		while (true) {
			int n = sc.nextInt();
			if (n == 0)
				break;
			for (int i = 0; i < n; ++i) {
				p[i] = sc.nextInt();
			}
			for (int i = 0; i < f.length; ++i) {
				for (int j = 0; j < f[i].length; ++j) {
					f[i][j][0] = -Integer.MAX_VALUE / 3;//500yen硬貨の枚数
					f[i][j][1] = Integer.MAX_VALUE / 3;//支払い金額
				}
			}
			f[0][0][0] = 0;
			f[0][0][1] = 0;
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < 500 * n + 1; ++j) {
					f[i + 1][j][0] = f[i][j][0];
					f[i + 1][j][1] = f[i][j][1];
				}
				for (int j = 0; j < 500 * n + 1; ++j) {
					if (f[i][j][0] < 0)
						continue;
					if (1 <= p[i] % 1000 && p[i] % 1000 <= 500) {
						int coin = (1000 - p[i] % 1000) % 500;
						f[i + 1][j + coin] = choice(f[i + 1][j + coin],
								new long[] { f[i][j][0] + 1, p[i] + f[i][j][1] });
					} else {
						int coin = (1000 - p[i] % 1000) % 500;
						f[i + 1][j + coin] = choice(f[i + 1][j + coin], new long[] { f[i][j][0], p[i] + f[i][j][1] });
						int need = (p[i] + 500) % 1000;
						if (need == 0)
							need = 500;
						if (need <= j) {
							f[i + 1][j - need] = choice(f[i + 1][j - need],
									new long[] { f[i][j][0] + 1, p[i] + f[i][j][1] });
						}
					}
				}
			}

			long[] ans = new long[] { 0, 0 };
			for (int i = 0; i < f[n].length; ++i) {
				ans = choice(ans, f[n][i]);
			}
			System.out.println(ans[0] + " " + ans[1]);
		}

	}

	long[] choice(long[] a, long[] b) {
		if (a[0] != b[0]) {
			if (a[0] > b[0]) {
				return a;
			} else {
				return b;
			}
		} else {
			if (a[1] < b[1]) {
				return a;
			} else {
				return b;
			}
		}
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

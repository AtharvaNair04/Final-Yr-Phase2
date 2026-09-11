import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;

class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	int n, m, l;
	String[] s;
	int[][] p;
	long[] p3 = new long[10];
	long[][] c = new long[1000][10];

	void run() {
		{
			p3[0] = 1;
			for (int i = 1; i < p3.length; ++i) {
				p3[i] = p3[i - 1] * 3;
			}
			c[0][0] = 1;
			for (int i = 1; i < c.length; ++i) {
				for (int j = 0; j < 10; ++j) {
					c[i][j] = c[i - 1][j] + (j > 0 ? c[i - 1][j - 1] : 0);
				}
			}
		}
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.gc();
			n = sc.nextInt();
			m = sc.nextInt();
			l = sc.nextInt();
			if (n == 0 && m == 0 && l == 0)
				break;
			s = new String[l];
			p = new int[3][l];
			for (int i = 0; i < l; ++i) {
				s[i] = sc.next();
				if (s[i].equals("*"))
					continue;
				++p[s[i].charAt(0) - 'a'][s[i].length() - 1];
			}
			solve();
		}
	}

	void solve() {
		long tot = 0;
		for (int S = 1; S < p3[l]; S += 3) {
			int[] a = new int[l];
			for (int i = 0; i < l; i++) {
				a[i] = (int) ((S / p3[i]) % 3);
			}
			if (check(a)) {
				long comb = 1;
				int[] cnt = cnt(arr(a));
				for (int v : cnt) {
					comb *= c[n][v];
				}
				int wall = 1;
				int res = m;
				for (int v : a) {
					if (v == 2) {
						++wall;
						res -= 2;
					} else if (v == 1) {
						res -= 1;
					}
				}
				comb *= c[res + wall][wall];
				tot += comb;
			}
		}
		System.out.printf("%.20f\n", (double) tot / c[m * n][l]);

	}

	boolean check(int[] a) {
		int[] arr = arr(a);
		if (arr[arr.length - 1] > m)
			return false;
		int[] cnt = cnt(arr);
		if (cnt.length > 20) {
			throw new AssertionError();
		}
		cnt = Arrays.copyOf(cnt, 40);
		for (int x = 0; x < arr.length; ++x) {
			for (int y = 0; y < arr.length; ++y) {
				for (int z = 0; z < arr.length; ++z) {
					// cnt[x]???a,cnt[y]???b,cnt[z]???c?????¨????????????
					int[] cnt2 = new int[40];
					for (int shift = 0; shift < l; ++shift) {
						cnt2[arr[x] + shift] += p[0][shift];
						cnt2[arr[y] + shift] += p[1][shift];
						cnt2[arr[z] + shift] += p[2][shift];
					}
					boolean f = true;
					for (int i = 0; i < cnt2.length; ++i) {
						f &= (cnt[i] >= cnt2[i]);
					}
					if (f)
						return true;
				}
			}
		}
		return false;
	}

	int[] arr(int[] a) {
		int[] seq = new int[l];
		seq[0] = a[0];// =1
		for (int i = 1; i < a.length; ++i) {
			seq[i] = seq[i - 1] + a[i];
		}
		return seq;
	}

	int[] cnt(int[] arr) {
		int[] cnt = new int[arr[arr.length - 1] + 1];
		for (int v : arr) {
			++cnt[v];
		}
		return cnt;
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

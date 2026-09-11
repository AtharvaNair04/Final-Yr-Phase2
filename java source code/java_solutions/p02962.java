import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	void run() {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		String t = sc.next();
		s = s + s;
		while (s.length() < 2 * t.length()) {
			s = s + s;
		}
		int[] dp1 = Arrays.copyOfRange(zalgo(t + s), t.length(), t.length() + s.length());
		int n = s.length();
		int ans = 0;
		boolean exist = false;
		for (int i = 0; i < dp1.length; ++i)
			exist |= dp1[i] >= t.length();
		if (!exist) {
			System.out.println(0);
			return;
		}
		DJSet ds = new DJSet(n);
		for (int i = n / 2 - 1; i >= 0; --i) {
			int a = i;
			int b = (i + t.length()) % (n / 2);
			if (dp1[a] >= t.length() && dp1[b] >= t.length()) {
				if (ds.equiv(a, b)) {
					System.out.println(-1);
					return;
				} else {
					ds.setUnion(a, b);
				}
			}
			ans = Math.max(ans, ds.sz(i));
		}
		System.out.println(ans);
	}

	int[] zalgo(String s) {
		int n = s.length();
		int[] ret = new int[n];
		ret[0] = n;
		int i = 1, j = 0;
		while (i < n) {
			while (i + j < n && s.charAt(j) == s.charAt(i + j))
				++j;
			ret[i] = j;
			if (j == 0) {
				++i;
				continue;
			}
			int k = 1;
			while (i + k < n && k + ret[k] < j) {
				ret[i + k] = ret[k];
				++k;
			}
			i += k;
			j -= k;
		}
		return ret;
	}

	class DJSet {
		int n;
		int[] upper;

		public DJSet(int n_) {
			n = n_;
			upper = new int[n_];
			Arrays.fill(upper, -1);
		}

		int root(int x) {
			return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
		}

		boolean equiv(int x, int y) {
			return root(x) == root(y);
		}

		void setUnion(int x, int y) {
			x = root(x);
			y = root(y);
			if (x == y)
				return;
			if (upper[x] < upper[y]) {
				x ^= y;
				y ^= x;
				x ^= y;
			}
			upper[y] += upper[x];
			upper[x] = y;
		}

		int sz(int x) {
			return -upper[root(x)];
		}
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

}

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

	void run() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long[] p = new long[n];
		long[] l = new long[n];
		long[][] cum = new long[n][2];
		for (int i = 0; i < n; ++i) {
			p[i] = sc.nextLong();
			cum[i][0] = (i > 0 ? cum[i - 1][0] : 0) + p[i];
			cum[i][1] = i;
		}
		for (int i = 0; i < n; ++i) {
			l[i] = sc.nextLong();
		}
		Arrays.sort(cum, new Comparator<long[]>() {
			@Override
			public int compare(long[] o1, long[] o2) {
				return Long.compare(o1[0], o2[0]);
			}
		});
		boolean f = true;
		for (int i = 0; i < n; ++i) {
			f &= cum[i][0] - (i > 0 ? cum[i - 1][0] : 0) >= l[i];
		}
		long ans = 0;
		BIT bit = new BIT(n + 1);
		for (int i = 1; i <= n; ++i) {
			bit.add(i, 1);
		}
		for (int i = 0; i < n; ++i) {
			ans += bit.sum((int) cum[i][1]);
			bit.add((int) cum[i][1] + 1, -1);
		}

		System.out.println(f ? ans : -1);
	}

	class BIT {
		int n;
		int[] v;

		public BIT(int n_) {
			n = n_;
			v = new int[n + 1];
		}

		void add(int k, int val) {
			while (k <= n) {
				v[k] += val;
				k += k & -k;
			}
		}

		int sum(int k) {
			int ret = 0;
			while (k > 0) {
				ret += v[k];
				k -= k & -k;
			}
			return ret;
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

}

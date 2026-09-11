import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		FastScanner in = new FastScanner(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		TaskE solver = new TaskE();
		solver.solve(1, in, out);
		out.close();
	}

	static class TaskE {
		public void solve(int testNumber, FastScanner in, PrintWriter out) {
			int n = in.nextInt();
			int[] a = new int[n];
			int[] b = new int[n];
			int[] c = new int[n];
			for (int i = 0; i < n; i++) {
				a[i] = in.nextInt();
				b[i] = in.nextInt();
				c[i] = -in.nextInt();
			}
			double x = findX(n, a, b, c);
			double y = findX(n, b, a, c);
			out.printf("%.15f %.15f\n", x, y);
		}

		private double findX(int n, int[] a, int[] b, int[] c) {
			for (int i = 0; i < n; i++) {
				if (b[i] < 0 || b[i] == 0 && a[i] < 0) {
					a[i] = -a[i];
					b[i] = -b[i];
					c[i] = -c[i];
				}
			}
			final double BOUND = 1e9 + 100;
			double l = -BOUND;
			double r = +BOUND;
			long k = (long) n * (n - 1) / 2;
			long need = (k + 1) / 2;
			Integer[] p = new Integer[n];
			for (int i = 0; i < n; i++) {
				p[i] = i;
			}
			Arrays.sort(p, (u, v) -> (a[u] * b[v] - a[v] * b[u]));
			int[] tree = new int[n];
			Pair[] pairs = new Pair[n];
			for (int i = 0; i < n; i++) {
				pairs[i] = new Pair();
			}
			for (int step = 0; step < 100; step++) {
				double m = 0.5 * (l + r);
				Arrays.fill(tree, 0);
				for (int i = 0; i < n; i++) {
					pairs[i].a = -(a[p[i]] * m + c[p[i]]) / b[p[i]];
					pairs[i].b = i;
				}
				long inversions = 0;
				Arrays.sort(pairs, (u, v) -> Double.compare(u.a, v.a));
				for (int i = 0; i < n; i++) {
					inversions += sum(tree, pairs[i].b, n - 1);
					add(tree, pairs[i].b, 1);
				}
				if (inversions < need) {
					l = m;
				} else {
					r = m;
				}
			}
			return 0.5 * (l + r);
		}

		public void add(int[] tree, int pos, int val) {
			while (pos < tree.length) {
				tree[pos] += val;
				pos |= pos + 1;
			}
		}

		public long sum(int[] tree, int l, int r) {
			if (l > r) {
				return 0;
			}
			return sum(tree, r) - sum(tree, l - 1);
		}

		public long sum(int[] tree, int r) {
			long res = 0;
			while (r >= 0) {
				res += tree[r];
				r = (r & (r + 1)) - 1;
			}
			return res;
		}

		class Pair {
			double a;
			int b;

		}

	}

	static class FastScanner {
		private BufferedReader in;
		private StringTokenizer st;

		public FastScanner(InputStream stream) {
			in = new BufferedReader(new InputStreamReader(stream));
		}

		public String next() {
			while (st == null || !st.hasMoreTokens()) {
				try {
					String rl = in.readLine();
					if (rl == null) {
						return null;
					}
					st = new StringTokenizer(rl);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

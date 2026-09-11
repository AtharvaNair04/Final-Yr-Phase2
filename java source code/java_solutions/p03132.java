import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
		D_Ears solver = new D_Ears();
		solver.solve(1, in, out);
		out.close();
	}

	static class D_Ears {
		public void solve(int testNumber, FastScanner in, PrintWriter out) {
			int n = in.nextInt();
			int[] a = new int[n];
			long[] s = new long[n + 1];
			long[] sEven = new long[n + 1];
			for (int i = 0; i < n; i++) {
				a[i] = in.nextInt();
				s[i + 1] = s[i] + a[i];
				sEven[i + 1] = sEven[i] + (a[i] % 2 == 0 ? 1 : 0);
			}

			long[] pref = new long[n];
			for (int i = 1; i < n; i++) {
				pref[i] = Math.min(s[i], f(a[i - 1]) + pref[i - 1]);
			}

			long[] suff = new long[n];
			for (int i = n - 2; i >= 0; i--) {
				suff[i] = Math.min(s[n] - s[i + 1], f(a[i + 1]) + suff[i + 1]);
			}

			long ans = s[n];
			long best = Long.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				best = Math.min(best, -sEven[i] + pref[i]);
				ans = Math.min(ans, sEven[i + 1] + suff[i] + best);
				if (i > 0) {
					ans = Math.min(ans, suff[i - 1] + pref[i]);
				}
			}
			ans = Math.min(ans, f(a[0]) + suff[0]);

			out.println(ans);
		}

		private long f(int x) {
			x -= 2;
			if (x < 0) {
				return Math.abs(x);
			}
			return x % 2;
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

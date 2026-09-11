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
		TaskC solver = new TaskC();
		solver.solve(1, in, out);
		out.close();
	}

	static class TaskC {
		FastScanner in;
		PrintWriter out;
		int n;
		boolean local = false;
		int[] seq = {0, 1, -1};

		public void solve(int testNumber, FastScanner in, PrintWriter out) {
			this.in = in;
			this.out = out;
			if (local) {
				n = seq.length;
			} else {
				n = in.nextInt();
			}
			int l = 0;
			int r = n - 1;
			int lv = send(l);
			if (lv < 0) {
				return;
			}
			int rv = send(r);
			if (rv < 0) {
				return;
			}
			while (r - l > 2) {
				int m = (l + r) / 2;
				if (m % 2 != 0) {
					++m;
				}
				int mv = send(m);
				if (mv < 0) {
					return;
				}
				if (mv == lv) {
					l = m;
				} else {
					r = m;
				}
			}
			if (send(l + 1) < 0) {
				return;
			}
			if (send(l + 2) < 0) {
				return;
			}
			throw new AssertionError();
		}

		private int send(int i) {
			if (local) {
				return seq[i % n];
			}

			out.println(i % n);
			out.flush();
			String s = in.next();
			if (s.equals("Vacant")) {
				return -1;
			}
			if (s.equals("Male")) {
				return 0;
			}
			if (s.equals("Female")) {
				return 1;
			}
			throw new AssertionError();
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

import java.io.*;
import java.util.*;

public class Main {
	FastScanner in;
	PrintWriter out;

	long pw(long x, int y) {
		long res = 1;
		for (int i = 0; i < y; i++) {
			if (res * 1.0 * x > 1e16) {
				return (long) 1e16;
			}
			res *= x;
		}
		return res;
	}

	void solve() {
		long n = in.nextLong();
		long a = in.nextLong();
		long res = Long.MAX_VALUE;
		for (int add = 0; add < 100; add++) {
			long cur = add * 1L * a;
			int cnt = add + 1;
			long from = (long) Math.pow(n, 1. / cnt) - 3;
			from = Math.max(from, 1);
			while (pw(from + 1, cnt) <= n) {
				from++;
			}
			long now = pw(from, cnt);
			cur += cnt * from;
			while (now < n) {
				cur++;
				now /= from;
				now *= from + 1;
			}
			res = Math.min(res, cur);
		}
		out.println(res);
	}

	void run() {
		try {
			in = new FastScanner(new File("object.in"));
			out = new PrintWriter(new File("object.out"));

			solve();

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	void runIO() {

		in = new FastScanner(System.in);
		out = new PrintWriter(System.out);

		solve();

		out.close();
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner(File f) {
			try {
				br = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public FastScanner(InputStream f) {
			br = new BufferedReader(new InputStreamReader(f));
		}

		String next() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return null;
				st = new StringTokenizer(s);
			}
			return st.nextToken();
		}

		boolean hasMoreTokens() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return false;
				st = new StringTokenizer(s);
			}
			return true;
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}
	}

	public static void main(String[] args) {
		new Thread(null, new Runnable() {
			public void run() {
				new Main().runIO();
			}
		}, "1", 1 << 26).start();
	}
}

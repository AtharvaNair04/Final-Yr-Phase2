import java.io.*;
import java.util.*;

public class Main {
	FastScanner in;
	PrintWriter out;

	int[] p;

	int get(int x) {
		return p[x] == x ? x : (p[x] = get(p[x]));
	}

	void unite(int x, int y) {
		p[get(x)] = get(y);
	}

	class Q implements Comparable<Q> {
		int fr, to, cost;

		public Q(int fr, int to, int cost) {
			super();
			this.fr = fr;
			this.to = to;
			this.cost = cost;
		}

		public int compareTo(Q arg0) {
			return Integer.compare(cost, arg0.cost);
		}

	}

	void solve() {
		int n = in.nextInt();
		int q = in.nextInt();
		p = new int[n];
		for (int i = 0; i < n; i++) {
			p[i] = i;
		}
		PriorityQueue<Q> pq = new PriorityQueue<Main.Q>();
		for (int i = 0; i < q; i++) {
			int from = in.nextInt();
			int to = in.nextInt();
			int co = in.nextInt();
			pq.add(new Q(from, to, co));
			pq.add(new Q((from + 1) % n, to, co + 1));
		}
		long res = 0;
		while (pq.size() > 0) {
			Q cur = pq.poll();
			if (get(cur.fr) == get(cur.to)) {
				continue;
			}
			unite(cur.fr, cur.to);
			res += cur.cost;
			cur.fr = (cur.fr + 1) % n;
			cur.to = (cur.to + 1) % n;
			cur.cost += 2;
			pq.add(cur);
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

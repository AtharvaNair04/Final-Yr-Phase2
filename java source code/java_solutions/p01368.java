import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";

	public static void main(String[] args) throws Exception {
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);

		new Main().solver();

		out.flush();
	}

	static long nl() {
		try {
			long num = 0;
			boolean minus = false;
			while ((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'))
				;
			if (num == '-') {
				num = 0;
				minus = true;
			} else {
				num -= '0';
			}

			while (true) {
				int b = is.read();
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}

	static char nc() {
		try {
			int b = skip();
			if (b == -1)
				return 0;
			return (char) b;
		} catch (IOException e) {
		}
		return 0;
	}

	static double nd() {
		try {
			return Double.parseDouble(ns());
		} catch (Exception e) {
		}
		return 0;
	}

	static String ns() {
		try {
			int b = skip();
			StringBuilder sb = new StringBuilder();
			if (b == -1)
				return "";
			sb.append((char) b);
			while (true) {
				b = is.read();
				if (b == -1)
					return sb.toString();
				if (b <= ' ')
					return sb.toString();
				sb.append((char) b);
			}
		} catch (IOException e) {
		}
		return "";
	}

	public static char[] ns(int n) {
		char[] buf = new char[n];
		try {
			int b = skip(), p = 0;
			if (b == -1)
				return null;
			buf[p++] = (char) b;
			while (p < n) {
				b = is.read();
				if (b == -1 || b <= ' ')
					break;
				buf[p++] = (char) b;
			}
			return Arrays.copyOf(buf, p);
		} catch (IOException e) {
		}
		return null;
	}

	public static byte[] nse(int n) {
		byte[] buf = new byte[n];
		try {
			int b = skip();
			if (b == -1)
				return null;
			is.read(buf);
			return buf;
		} catch (IOException e) {
		}
		return null;
	}

	static int skip() throws IOException {
		int b;
		while ((b = is.read()) != -1 && !(b >= 33 && b <= 126))
			;
		return b;
	}

	static boolean eof() {
		try {
			is.mark(1000);
			int b = skip();
			is.reset();
			return b == -1;
		} catch (IOException e) {
			return true;
		}
	}

	static int ni() {
		try {
			int num = 0;
			boolean minus = false;
			while ((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'))
				;
			if (num == '-') {
				num = 0;
				minus = true;
			} else {
				num -= '0';
			}

			while (true) {
				int b = is.read();
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	void solver() {

		int[][] dist = new int[100][100];

		while (true) {
			int n = ni();
			int m = ni();
			int l = ni();
			if (n == 0 && m == 0 && l == 0)
				break;
			for (int i = 0; i < 100; i++) {
				Arrays.fill(dist[i], 1 << 28);
				dist[i][i] = 0;
			}

			for (int i = 0; i < m; i++) {
				int u = ni(), v = ni(), d = ni();
				dist[u][v] = d;
				dist[v][u] = d;
			}

			int[] p = new int[l];
			int[] t = new int[l];
			for (int i = 0; i < l; i++) {
				p[i] = ni();
				t[i] = ni();
			}

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					for (int k = 0; k < n; k++) {
						dist[j][k] = Math.min(dist[j][k], dist[j][i] + dist[i][k]);
					}
				}
			}

			ArrayList<Integer>[] g = new ArrayList[2 * l];
			for (int i = 0; i < 2 * l; i++) {
				g[i] = new ArrayList<>();
			}

			for (int i = 0; i < l; i++) {
				for (int j = 0; j < l; j++) {
					if (i == j)
						continue;
					if (dist[p[i]][p[j]] + t[i] <= t[j]) {
						g[i].add(l + j);
						// System.out.println(dist[p[i]][p[j]] + t[i]);
					}
				}
			}

			// for (int i = 0; i < l; i++) {
			// g[2 * l].add(new Edge(2 * l, i, 1));
			// }
			// for (int i = 0; i < l; i++) {
			// g[i + l].add(new Edge(i + l, 2 * l + 1, 1));
			// }

			System.out.println(l - bipartiteMatching(g, l));
			// Preflow_Relabel pr = new Preflow_Relabel(2 * l, 2 * l + 1, g);
			// System.out.println((l - pr.calc()));
			// out.println((l - new Edmonds_Kerp_MaxFlow(2 * l, 2 * l + 1,
			// g).MaxFlow()));
		}
	}

	boolean augment(ArrayList<Integer>[] g, int u, Integer[] matchTo, Boolean[] visited) {
		if (u < 0)
			return true;
		for (int v : g[u]) {
			if (!visited[v]) {
				visited[v] = true;
				if (augment(g, matchTo[v], matchTo, visited)) {
					matchTo[u] = v;
					matchTo[v] = u;
					return true;
				}
			}
		}
		return false;
	}

	int bipartiteMatching(ArrayList<Integer>[] g, int L) {
		final int n = g.length;
		Integer[] matchTo = new Integer[n];
		Arrays.fill(matchTo, -1);
		int match = 0;
		for (int i = 0; i < L; i++) {
			Boolean[] visited = new Boolean[n];
			Arrays.fill(visited, false);
			if (augment(g, i, matchTo, visited))
				match++;
		}
		return match;
	}

	class Edmonds_Kerp_MaxFlow {
		int n;
		int s;
		int t;
		ArrayList<Edge>[] g;
		int[][] flow;
		int[][] capacity;

		@SuppressWarnings("unchecked")
		public Edmonds_Kerp_MaxFlow(int s, int t, ArrayList<Edge>[] g) {
			this.g = g;
			this.s = s;
			this.t = t;
			n = g.length;
			flow = new int[n][n];
			capacity = new int[n][n];

			for (int i = 0; i < n; i++) {
				for (Edge e : g[i]) {
					if (e.weight > 0) {
						g[e.dst].add(new Edge(e.dst, e.src, 0));
						capacity[e.src][e.dst] += e.weight;
					} else if (e.weight == 0) {
						continue;
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		int MaxFlow() {
			int total = 0;
			while (true) {
				int[] prev = bfs();
				if (prev[t] == -1)
					return total;
				int inc = 1 << 30;
				for (int i = t; prev[i] != -1; i = prev[i]) {
					inc = Math.min(inc, residue(prev[i], i));
				}
				for (int i = t; prev[i] != -1; i = prev[i]) {
					push(prev[i], i, inc);
				}
				total += inc;
			}
		}

		int residue(int s, int t) {
			return capacity[s][t] - flow[s][t];
		}

		int[] bfs() {
			int[] prev = new int[n];
			Arrays.fill(prev, -1);
			ArrayDeque<Integer> que = new ArrayDeque<>();
			que.add(s);
			out: while (!que.isEmpty()) {
				int v = que.poll();
				for (Edge e : g[v]) {
					if (prev[e.dst] == -1 && e.dst != s && residue(v, e.dst) > 0) {
						prev[e.dst] = v;
						que.add(e.dst);
						if (e.dst == t)
							break out;
					}
				}
			}
			return prev;
		}

		void push(int s, int t, int inc) {
			flow[s][t] += inc;
			flow[t][s] -= inc;
		}
	}

	class Preflow_Relabel {
		int n;
		int s;
		int t;
		ArrayList<Edge>[] g;
		int[][] flow;
		int[][] capacity;
		int[] excess;
		int[] height;
		ArrayDeque<Integer>[] B;

		void global_relabeling() {
			ArrayDeque<Integer> que = new ArrayDeque<>();
			boolean[] arrived = new boolean[n];
			que.add(t);
			while (!que.isEmpty()) {
				int v = que.poll();
				for (Edge e : g[v]) {
					if (residue(e.dst, v) > 0 && (height[v] + 1 < height[e.dst] || !arrived[e.dst])) {
						height[e.dst] = height[v] + 1;
						arrived[e.dst] = true;
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		public Preflow_Relabel(int s, int t, ArrayList<Edge>[] g) {
			this.g = g;
			this.s = s;
			this.t = t;
			n = g.length;
			flow = new int[n][n];
			capacity = new int[n][n];
			height = new int[n];
			height[s] = n;
			excess = new int[n];
			B = new ArrayDeque[n + 1];
			for (int i = 0; i <= n; i++) {
				B[i] = new ArrayDeque<>();
			}
			B[height[s]].add(s);

			for (int i = 0; i < n; i++) {
				for (Edge e : g[i]) {
					if (e.weight > 0) {
						g[e.dst].add(new Edge(e.dst, e.src, 0));
						capacity[e.src][e.dst] += e.weight;
					} else if (e.weight == 0) {
						continue;
					}
				}
			}
		}

		int b = 0;

		@SuppressWarnings("unchecked")
		int calc() {
			int count = 0;
			for (Edge e : g[s]) {
				excess[s] += e.weight;
				push(s, e.dst);
				B[height[e.dst]].add(e.dst);
			}
			global_relabeling();
			for (b = height[s]; b >= 0;) {
				if (B[b].isEmpty()) {
					b--;
					continue;
				}
				int v = B[b].poll();
				if (excess[v] == 0 || v == t || v == s || height[v] > n) {
					continue;
				}

				for (Edge e : g[v]) {
					if (height[e.src] == height[e.dst] + 1) {
						if (residue(e.src, e.dst) > 0) {
							push(e.src, e.dst);
							if (excess[v] == 0)
								break;
						}
					}
				}
				if (excess[v] > 0) {
					int min = 1 << 30;
					for (Edge e : g[v]) {
						if (residue(e.src, e.dst) > 0) {
							min = Math.min(min, height[e.dst]);
						}
					}
					height[v] = min + 1;
					if (height[v] < n) {
						B[height[v]].add(v);
						if (height[v] > b)
							b = height[v];
					}
				}
				if (++count % n == 0) {
					global_relabeling();
				}
			}
			return excess[t];
		}

		int residue(int s, int t) {
			return capacity[s][t] - flow[s][t];
		}

		void push(int u, int v) {
			int delta = Math.min(excess[u], residue(u, v));
			flow[u][v] += delta;
			flow[v][u] -= delta;
			excess[v] += delta;
			excess[u] -= delta;
			if (v != t && v != s && height[v] < n) {
				B[height[v]].add(v);
				if (b < height[v]) {
					b = height[v];
				}
			}
		}
	}

	class Edge {
		int src;
		int dst;
		int weight;

		Edge(int src, int dst, int weight) {
			this.src = src;
			this.dst = dst;
			this.weight = weight;
		}
	}

	class DJSet {
		int n;// the number of vertices
		int[] d;

		DJSet(int n) {
			this.n = n;
			d = new int[n];
			Arrays.fill(d, -1);
		}

		int root(int x) {
			return d[x] < 0 ? x : (d[x] = root(d[x]));
		}

		boolean setUnion(int x, int y) {
			x = root(x);
			y = root(y);
			if (x != y) {
				if (d[x] < d[y]) {
					int d = x;
					x = y;
					y = d;
				}
				// x>y
				d[y] += d[x];
				d[x] = y;
			}
			return x != y;
		}

		boolean equiv(int x, int y) {
			return root(x) == root(y);
		}

		int size(int x) {
			return d[root(x)] * (-1);
		}

		int count() {
			int ct = 0;
			for (int u : d) {
				if (u < 0)
					ct++;
			}
			return ct;
		}
	}

	void tr(Object... o) {
		System.out.println(Arrays.deepToString(o));
	}
}

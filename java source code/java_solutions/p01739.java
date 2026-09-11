import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

class Main {
	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	void run() throws IOException {
		long s = 0;
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		++n;
		long[][] c = new long[n][n];
		long[] excess = new long[n];
		boolean[][] vis = new boolean[n][n];
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] g = new ArrayList[n];
		for (int i = 0; i < n; ++i) {
			g[i] = new ArrayList<>();
		}
		long[][] u = new long[n][n];
		for (int src = 0; src < n - 1; ++src) {
			int k = sc.nextInt();
			for (int i = 0; i < k; ++i) {
				int dst = sc.nextInt();
				--dst;
				int cost = sc.nextInt();
				s += cost;
				if (!vis[src][dst] || cost < c[src][dst]) {
					c[src][dst] = cost;
					c[dst][src] = -cost;
				}
				if (!vis[src][dst]) {
					g[src].add(dst);
					vis[src][dst] = true;
				}
				u[src][dst] = 999999;
				--excess[src];
				++excess[dst];
			}
		}
		for (int src = 1; src < n - 1; ++src) {
			g[src].add(n - 1);// ?§????0????????????0???????????????
			u[src][n - 1] = 999999;
		}
		g[n - 1].add(0);
		u[n - 1][0] = 999999;
		for (int src = 0; src < n; ++src) {
			for (int dst : g[src]) {
				if (g[dst].contains(src))
					continue;
				g[dst].add(src);
			}
		}
		long[][] f = new long[n][n];
		System.out.println(minCostFlow(n, u, f, c, g, excess) + s);
	}

	long minCostFlow(int n, long[][] u, long[][] f, long[][] c, ArrayList<Integer>[] g, long[] excess) {
		long[] p = new long[n];
		out: for (int i = 0; i < n; ++i) {
			if (excess[i] <= 0) {
				if (i == n - 1)
					break out;
				else
					continue out;
			}
			long[] d = new long[n];
			int[] pre = new int[n];
			Arrays.fill(pre, -1);
			Arrays.fill(d, Integer.MAX_VALUE / 16);
			d[i] = 0;
			class P implements Comparable<P> {
				int id;
				long d;

				public P(int id, long d) {
					this.id = id;
					this.d = d;
				}

				public int compareTo(P o) {
					return Long.compare(d, o.d);
				};
			}
			PriorityQueue<P> pq = new PriorityQueue<>();
			pq.add(new P(i, 0));
			while (!pq.isEmpty()) {
				P v = pq.poll();
				if (d[v.id] < v.d)
					continue;
				for (int dst : g[v.id]) {
					if (u[v.id][dst] - f[v.id][dst] <= 0)
						continue;
					if (c[v.id][dst] - (p[v.id] - p[dst]) + d[v.id] < d[dst]) {
						d[dst] = c[v.id][dst] - (p[v.id] - p[dst]) + d[v.id];
						pq.add(new P(dst, d[dst]));
						pre[dst] = v.id;
					}
				}
			}
			for (int j = 0; j < n; ++j) {
				if (excess[j] >= 0)
					continue;
				long ep = Math.min(excess[i], -excess[j]);
				for (int k = j; pre[k] != -1; k = pre[k]) {
					ep = Math.min(ep, u[pre[k]][k] - f[pre[k]][k]);
				}
				for (int k = j; pre[k] != -1; k = pre[k]) {
					f[pre[k]][k] += ep;
					f[k][pre[k]] -= ep;
				}
				excess[i] -= ep;
				excess[j] += ep;
				break;
			}

			for (int j = 0; j < n; ++j) {
				p[j] -= d[j];
			}
			i = -1;
		}
		long ans = 0;
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				if (u[i][j] > 0 && f[i][j] > 0) {
					ans += c[i][j] * f[i][j];
				}
			}
		}
		return ans;
	}

	// ?°??´??????¨????????????????????????????????????
	void check(int[][] c, long[] p, int[][] u, int[][] f, int n) {
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				if (!(u[i][j] - f[i][j] > 0))
					continue;
				if (c[i][j] - (p[i] - p[j]) < 0) {
					tr("p", p);
					throw new AssertionError();
				}
			}
		}
		System.out.println("OK");
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

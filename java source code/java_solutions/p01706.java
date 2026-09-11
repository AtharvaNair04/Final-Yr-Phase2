import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		new Main().solver();
	}

	@SuppressWarnings("unchecked")
	void solver() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			int n = sc.nextInt();// the number of cities
			int m = sc.nextInt();// the number of roads
			int s = sc.nextInt() - 1;
			int t = sc.nextInt() - 1;
			if (n == 0 && m == 0 && s + 1 == 0 && t + 1 == 0)
				break;
			ArrayList<Edge>[] g = new ArrayList[n];
			ArrayList<Edge>[] g_rev = new ArrayList[n];
			for (int i = 0; i < n; i++) {
				g[i] = new ArrayList<>();
				g_rev[i] = new ArrayList<>();
			}
			int[] A = new int[m];
			int[] B = new int[m];
			for (int i = 0; i < m; i++) {
				int a = sc.nextInt() - 1;
				int b = sc.nextInt() - 1;
				A[i] = a;
				B[i] = b;
				g[a].add(new Edge(a, b, 1));
				g_rev[b].add(new Edge(b, a, 1));
			}
			Edmonds_Kerp_MaxFlow mf = new Edmonds_Kerp_MaxFlow(s, t, g);
			long tmp = mf.MaxFlow();
			boolean[] reachble_from_s = new boolean[n];
			boolean[] reachble_from_t = new boolean[n];
			ArrayDeque<Integer> S = new ArrayDeque<>();
			ArrayDeque<Integer> T = new ArrayDeque<>();
			S.add(s);
			T.add(t);
			while (!S.isEmpty()) {
				int v = S.poll();
				if (reachble_from_s[v]) {
					continue;
				}
				reachble_from_s[v] = true;
				for (Edge e : g[v]) {
					int u = e.dst;
					if (mf.residue(v, u) > 0)
						S.add(u);
				}
			}
			while (!T.isEmpty()) {
				int v = T.poll();
				if (reachble_from_t[v]) {
					continue;
				}
				reachble_from_t[v] = true;
				for (Edge e : g_rev[v]) {
					int u = e.dst;
					if (mf.residue(u, v) > 0)
						T.add(u);
				}
			}
			boolean flag = false;
			int count = 0;
			for (int i = 0; i < m; i++) {
				if (reachble_from_s[B[i]] && reachble_from_t[A[i]]) {
					count++;
				}
			}
			System.out.println((tmp + (count > 0 ? 1 : 0)) + " " + count);
		}
	}

	class Edmonds_Kerp_MaxFlow {
		@SuppressWarnings("unchecked")
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
}

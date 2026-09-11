import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] sa = br.readLine().split(" ");
		int n = Integer.parseInt(sa[0]);
		int m = Integer.parseInt(sa[1]);
		char[][] s = new char[n][m];
		for (int i = 0; i < n; i++) {
			s[i] = br.readLine().toCharArray();
		}
		br.close();

		long BIG = 1000000000;
		MinCostFlow mf = new MinCostFlow(n * m + 2);
		int x = n * m;
		int y = x + 1;

		int cnt = 0;
		int start = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				int idx = i * m + j;
				if (s[i][j] == 'o') {
					mf.addEdge(x, idx, 1, 0);
					cnt++;
					start += i + j;
				}
				if (i < n - 1 && s[i + 1][j] != '#') {
					mf.addEdge(idx, idx + m, 100, 0);
				}
				if (j < m - 1 && s[i][j + 1] != '#') {
					mf.addEdge(idx, idx + 1, 100, 0);
				}
				if (s[i][j] != '#') {
					mf.addEdge(idx, y, 1, BIG - i - j);
				}
			}
		}

		long[] result = mf.flow(x, y);
		long ans = BIG * cnt - result[1] - start;
		System.out.println(ans);
	}
}

/**
 * 最小費用流
 */
class MinCostFlow {
	private final int n;
	private List<int[]> pos;
	private List<List<Edge2>> g;

	class Edge {
		/** 有向辺の始点 */
		final int from;
		/** 有向辺の終点 */
		final int to;
		/** 最大容量 */
		long cap;
		/** 流量 */
		long flow;
		/** コスト */
		long cost;

		public Edge(int from, int to, long cap, long flow, long cost) {
			this.from = from;
			this.to = to;
			this.cap = cap;
			this.flow = flow;
			this.cost = cost;
		}
	}

	private class Edge2 {
		final int to, rev;
		long cap, cost;

		public Edge2(int to, int rev, long cap, long cost) {
			this.to = to;
			this.rev = rev;
			this.cap = cap;
			this.cost = cost;
		}
	}

	private class Q {
		final long key;
		final int to;

		public Q(long key, int to) {
			this.key = key;
			this.to = to;
		}
	}

	/**
	 * n頂点0辺のグラフを作る。<br>
	 * O(n)
	 * 
	 * @param n 頂点数
	 */
	public MinCostFlow(int n) {
		this.n = n;
		pos = new ArrayList<>();
		g = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			g.add(new ArrayList<>());
		}
	}

	/**
	 * fromからtoへ最大容量cap、コストcostの辺を追加する。<br>
	 * ならしO(1)
	 * 
	 * @param from 有向辺の始点（0≦from＜n）
	 * @param to 有向辺の終点（0≦to＜n）
	 * @param cap 最大容量（0≦cap）
	 * @param cost コスト（0≦cost）
	 * @return 何番目に追加された辺か
	 */
	int addEdge(int from, int to, long cap, long cost) {
		assert 0 <= from && from < n : "from=" + from;
		assert 0 <= to && to < n : "to=" + to;
		assert 0 <= cap : "cap=" + cap;
		assert 0 <= cost : "cost=" + cost;

		int m = pos.size();
		pos.add(new int[] { from, g.get(from).size() });
		g.get(from).add(new Edge2(to, g.get(to).size(), cap, cost));
		g.get(to).add(new Edge2(from, g.get(from).size() - 1, 0, -cost));
		return m;
	}

	/**
	 * i番目に追加された辺を取得する。<br>
	 * O(1)
	 * 
	 * @param i 辺のインデックス（0≦i＜辺数）
	 * @return 辺情報
	 */
	Edge getEdge(int i) {
		assert 0 <= i && i < pos.size() : "i=" + i + ", size=" + pos.size();

		Edge2 e = g.get(pos.get(i)[0]).get(pos.get(i)[1]);
		Edge2 re = g.get(e.to).get(e.rev);
		return new Edge(pos.get(i)[0], e.to, e.cap + re.cap, re.cap, e.cost);
	}

	/**
	 * 全ての辺を取得する。<br>
	 * O(辺数)
	 * 
	 * @return 辺情報リスト
	 */
	List<Edge> edges() {
		int m = pos.size();
		List<Edge> result = new ArrayList<>(m);
		for (int i = 0; i < m; i++) {
			result.add(getEdge(i));
		}
		return result;
	}

	/**
	 * 最小費用流。頂点sからtへ流せるだけ流す。<br>
	 * Fを流量、mを辺数として、O(F(n + m) log n)
	 * 
	 * @param s 始点（0≦s＜n）
	 * @param t 終点（0≦t＜n）
	 * @return [0]：流せた量、[1]：その時のコスト
	 */
	long[] flow(int s, int t) {
		return flow(s, t, Long.MAX_VALUE);
	}

	/**
	 * 最小費用流。頂点sからtへflowLimitに達するまで流せるだけ流す。<br>
	 * Fを流量、mを辺数として、O(F(n + m) log n)
	 * 
	 * @param s 始点（0≦s＜n）
	 * @param t 終点（0≦t＜n）
	 * @param flowLimit 流量制限
	 * @return [0]：流せた量、[1]：その時のコスト
	 */
	long[] flow(int s, int t, long flowLimit) {
		List<long[]> result = slope(s, t, flowLimit);
		return result.get(result.size() - 1);
	}

	/**
	 * <pre>
	 * 流量とコストの関係の折れ線を取得する。
	 * 戻り値の最初の要素は(0, 0)
	 * 戻り値の[0]、[1]は共に狭義単調増加
	 * 辺のコストの最大をxとして、最後の要素の[0]はx
	 * 
	 * ■制約
	 * sからtへ流したフローの流量がlongに収まる。
	 * 流したフローのコストの総和がlongに収まる。
	 * 0≦nx≦8×10^18＋1000
	 * </pre>
	 * 
	 * @param s 始点（0≦s＜n、s≠t）
	 * @param t 終点（0≦t＜n、s≠t）
	 * @return ＜[0]：流量、[1]：その時のコスト＞のリスト
	 */
	List<long[]> slope(int s, int t) {
		return slope(s, t, Long.MAX_VALUE);
	}

	/**
	 * <pre>
	 * 流量とコストの関係の折れ線を取得する。
	 * 戻り値の最初の要素は(0, 0)
	 * 戻り値の[0]、[1]は共に狭義単調増加
	 * 辺のコストの最大をxとして、最後の要素の[0]はmin(x, flowLimit)
	 * 
	 * ■制約
	 * sからtへ流したフローの流量がlongに収まる。
	 * 流したフローのコストの総和がlongに収まる。
	 * 0≦nx≦8×10^18＋1000
	 * </pre>
	 * 
	 * @param s 始点（0≦s＜n、s≠t）
	 * @param t 終点（0≦t＜n、s≠t）
	 * @param flowLimit 流量制限
	 * @return ＜[0]：流量、[1]：その時のコスト＞のリスト
	 */
	List<long[]> slope(int s, int t, long flowLimit) {
		assert 0 <= s && s < n : "s=" + s;
		assert 0 <= t && t < n : "t=" + t;
		assert s != t : "s=t=" + s;

		long[] dual = new long[n];
		long[] dist = new long[n];
		int[] pv = new int[n];
		int[] pe = new int[n];
		boolean[] vis = new boolean[n];

		long flow = 0;
		long cost = 0;
		long prevCost = -1;
		List<long[]> result = new ArrayList<>();
		result.add(new long[] { flow, cost });
		while (flow < flowLimit) {
			if (!dualRef(s, t, dual, dist, pv, pe, vis)) {
				break;
			}
			long c = flowLimit - flow;
			for (int v = t; v != s; v = pv[v]) {
				c = Math.min(c, g.get(pv[v]).get(pe[v]).cap);
			}
			for (int v = t; v != s; v = pv[v]) {
				Edge2 e = g.get(pv[v]).get(pe[v]);
				e.cap -= c;
				g.get(v).get(e.rev).cap += c;
			}
			long d = -dual[s];
			flow += c;
			cost += c * d;
			if (prevCost == d) {
				result.remove(result.size() - 1);
			}
			result.add(new long[] { flow, cost });
			prevCost = cost;
		}
		return result;
	}

	private boolean dualRef(int s, int t, long[] dual, long[] dist, int[] pv, int[] pe, boolean[] vis) {
		Arrays.fill(dist, Long.MAX_VALUE);
		Arrays.fill(pv, -1);
		Arrays.fill(pe, -1);
		Arrays.fill(vis, false);
		PriorityQueue<Q> que = new PriorityQueue<>((o1, o2) -> Long.compare(o1.key, o2.key));
		dist[s] = 0;
		que.add(new Q(0, s));
		while (!que.isEmpty()) {
			int v = que.poll().to;
			if (vis[v]) {
				continue;
			}
			vis[v] = true;
			if (v == t) {
				break;
			}
			for (int i = 0; i < g.get(v).size(); i++) {
				Edge2 e = g.get(v).get(i);
				if (vis[e.to] || e.cap == 0) {
					continue;
				}
				long cost = e.cost - dual[e.to] + dual[v];
				if (dist[e.to] - dist[v] > cost) {
					dist[e.to] = dist[v] + cost;
					pv[e.to] = v;
					pe[e.to] = i;
					que.add(new Q(dist[e.to], e.to));
				}
			}
		}
		if (!vis[t]) {
			return false;
		}

		for (int v = 0; v < n; v++) {
			if (vis[v]) {
				dual[v] -= dist[t] - dist[v];
			}
		}
		return true;
	}
}

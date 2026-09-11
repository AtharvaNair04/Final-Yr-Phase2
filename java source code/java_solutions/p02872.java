import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static int v, e, tmax;
	static int[][] dist;
	static List<Integer>[][] route;
	static List<List<Hen>> nextList;
	static Order[] newArr;
	static int t = 0;
	static long tmax2 = 0;
	static Map<Integer, List<Order>> carMap = new HashMap<>();

	public static void main(String[] args) throws Exception {
		String[] sa = br.readLine().split(" ");
		v = Integer.parseInt(sa[0]);
		e = Integer.parseInt(sa[1]);
		dist = new int[v][v];
		route = new List[v][v];
		nextList = new ArrayList<>(v);
		for (int i = 0; i < v; i++) {
			nextList.add(new ArrayList<>());
		}
		for (int i = 0; i < e; i++) {
			sa = br.readLine().split(" ");
			int a = Integer.parseInt(sa[0]) - 1;
			int b = Integer.parseInt(sa[1]) - 1;
			int c = Integer.parseInt(sa[2]);
			nextList.get(a).add(new Hen(b, c));
			nextList.get(b).add(new Hen(a, c));
		}

		sa = br.readLine().split(" ");
		int[] f = new int[v];
		for (int i = 0; i < v; i++) {
			f[i] = Integer.parseInt(sa[i]);
		}
		tmax = Integer.parseInt(br.readLine());
		tmax2 = tmax * tmax;
		newArr = new Order[tmax];

		for (int i = 0; i < v; i++) {
			dijkstra(i);
		}

		int now = 0;
		int next = 0;
		SimuInfo info = null;
		input();
		while (t < tmax) {
			if (now == 0) {
				// 荷物なしの場合は店で待機
				if (carMap.isEmpty()) {
					System.out.println(-1);
					input2();
					t++;
					input();
					continue;
				}
				// ルート探索
				info = simulate(t, now, true);
			} else {
				// 荷物がなくなった場合は店に戻る
				if (carMap.isEmpty()) {
					moveAll(now, 0);
					now = 0;
					continue;
				}
			}

			next = info.his.get(info.hisIdx++);
			boolean reached = moveAll(now, next);
			if (reached) {
				now = next;
			} else {
				now = 0;
			}
		}
	}

	/**
	 * 引数の荷物を持って出発した場合の評価を計算（carMapに変更は加わらない）
	 * @param tt 出発時刻
	 * @param now 出発地点
	 * @paaram doSA falseの場合貪欲のみ
	 * @return 結果
	 */
	static SimuInfo simulate(int tt, int now, boolean doSA) {
		SimuInfo info = new SimuInfo();
		info.tt = tt;
		info.goal = now;
		Set<Integer> set = new HashSet<>();
		set.addAll(carMap.keySet());
		info.his = new ArrayList<>(set.size());
		if (set.isEmpty()) {
			return info;
		}
		int from = now;
		while (!set.isEmpty()) {
			int minD = Integer.MAX_VALUE;
			for (int o : set) {
				int d = dist[from][o];
				if (d < minD) {
					minD = d;
					info.goal = o;
				}
			}
			set.remove(info.goal);
			info.his.add(info.goal);
			info.tt += dist[from][info.goal];
			if (info.tt > tmax) {
				break;
			}
			for (Order o : carMap.get(info.goal)) {
				int w = info.tt - o.time;
				info.val += tmax2 - w * w;
			}
			from = info.goal;
		}

		if (doSA) {
			info = simulatedAnnealing(info, tt, now);
		}
		return info;
	}

	/**
	 * 焼きなまし法でより良い配達順を探索
	 */
	static SimuInfo simulatedAnnealing(SimuInfo info0, int tt, int now) {
		int time = 50000000; // 50ミリ秒
		Instant limit = Instant.now().plusNanos(time);
		Instant ins = Instant.now();
		SimuInfo temp = info0;
		SimuInfo ret = info0;
		int size = temp.his.size();
		while (ins.compareTo(limit) < 0) {
			SimuInfo info = new SimuInfo();
			info.tt = tt;
			info.goal = now;
			info.his = new ArrayList<>(size);
			info.his.addAll(temp.his);
			int i1 = (int) (Math.random() * size);
			int i2 = (int) (Math.random() * size);
			Collections.swap(info.his, i1, i2);
			for (int e : info.his) {
				info.goal = e;
				info.tt += dist[now][info.goal];
				if (info.tt > tmax) {
					break;
				}
				for (Order o : carMap.get(info.goal)) {
					int w = info.tt - o.time;
					info.val += tmax2 - w * w;
				}
				now = info.goal;
			}

			ins = Instant.now();
			int cmp = Long.compare(ins.getEpochSecond(), limit.getEpochSecond());
			if (cmp != 0) {
				cmp = 1000000000;
			}
			cmp += limit.getNano() - ins.getNano();
			if (info.val > temp.val || (int) (Math.random() * time) < cmp) {
				temp = info;
			}
			if (info.val > ret.val) {
				ret = info;
			}
		}
		if (temp.val > ret.val) {
			ret = temp;
		}
		return ret;
	}

	static void input() throws Exception {
		if (t >= tmax) {
			System.exit(0);
		}
		int n = Integer.parseInt(br.readLine());
		for (int j = 0; j < n; j++) {
			String[] sa = br.readLine().split(" ");
			Order o = new Order();
			o.id = Integer.parseInt(sa[0]);
			o.dst = Integer.parseInt(sa[1]) - 1;
			o.time = t;
			newArr[o.id] = o;
		}

		n = Integer.parseInt(br.readLine());
		for (int j = 0; j < n; j++) {
			int p = Integer.parseInt(br.readLine());
			Order o = newArr[p];
			List<Order> list = carMap.get(o.dst);
			if (list == null) {
				list = new ArrayList<>();
				carMap.put(o.dst, list);
			}
			list.add(o);
		}
	}

	static void input2() throws Exception {
		String v = br.readLine();
		if ("NG".equals(v)) {
			System.exit(0);
		}
		int n = Integer.parseInt(br.readLine());
		for (int j = 0; j < n; j++) {
			int a = Integer.parseInt(br.readLine());
			carMap.remove(newArr[a].dst);
		}
	}

	/**
	 * 出発地点から最終目的地まで移動
	 */
	static boolean moveAll(int from, int to) throws Exception {
		List<Integer> list = route[from][to];
		for (int i = 1; i < list.size(); i++) {
			move(list.get(i - 1), list.get(i));
			if (list.get(i) == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 地点から次の地点まで移動
	 */
	static void move(int from, int to) throws Exception {
		int d = dist[from][to];
		d = Math.min(d, tmax - t);
		int to1 = to + 1;
		for (int i = 0; i < d; i++) {
			System.out.println(to1);
			input2();
			t++;
			input();
		}
	}

	static void dijkstra(int s) {
		Arrays.fill(dist[s], Integer.MAX_VALUE);
		dist[s][s] = 0;
		for (int i = 0; i < v; i++) {
			route[s][i] = new ArrayList<>();
		}
		route[s][s].add(s);
		PriorityQueue<Node> que = new PriorityQueue<Node>();
		Node first = new Node(s, 0);
		que.add(first);

		while (!que.isEmpty()) {
			Node cur = que.poll();
			for (Hen hen : nextList.get(cur.v)) {
				int alt = dist[s][cur.v] + hen.c;
				if (alt < dist[s][hen.v]) {
					dist[s][hen.v] = alt;
					route[s][hen.v].clear();
					route[s][hen.v].addAll(route[s][cur.v]);
					route[s][hen.v].add(hen.v);
					Node next = new Node(hen.v, alt);
					que.add(next);
				}
			}
		}
	}

	static class Hen {
		int v, c;

		public Hen(int v, int c) {
			this.v = v;
			this.c = c;
		}
	}

	static class Node implements Comparable<Node> {
		int v, d;

		public Node(int v, int d) {
			this.v = v;
			this.d = d;
		}

		public int compareTo(Node o) {
			return d - o.d;
		}
	}

	static class Order {
		int id, dst, time;
	}

	static class SimuInfo {
		long val;
		int tt, goal, hisIdx;
		List<Integer> his;
	}
}

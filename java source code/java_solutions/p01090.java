import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	static int n, m, k;
	static int[] u, v;
	static int[] w;
	static char[] l;

	static class Edge implements Comparable<Edge> {
		int src, dst;
		int cost;
		char c;

		public Edge(int src, int dst, int cost, char c) {
			this.src = src;
			this.dst = dst;
			this.cost = cost;
			this.c = c;
		}

		public int compareTo(Edge o) {
			if (cost != o.cost)
				return Integer.compare(cost, o.cost);
			else {
				return c - o.c;
			}
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			k = sc.nextInt();
			if (n == 0 && m == 0 && k == 0)
				break;
			u = new int[m];
			v = new int[m];
			w = new int[m];
			l = new char[m];
			DJSet ds = new DJSet(n);
			for (int i = 0; i < m; ++i) {
				u[i] = sc.nextInt() - 1;
				v[i] = sc.nextInt() - 1;
				w[i] = sc.nextInt();
				l[i] = sc.next().toCharArray()[0];
				ds.setUnion(u[i], v[i]);
			}
			if (ds.upper[ds.root(0)] != -n) {
				System.out.println(-1);
				continue;
			}
			int left = -1 << 20;
			int right = 1 << 20;
			while (right - left > 1) {
				int middle = (right + left) / 2;
				int[] ret = kruskal(middle);
				if (ret[1] >= k) {
					left = middle;
				} else {
					right = middle;
				}
			}
			int[] ret1 = kruskal(right);
			int[] ret2 = kruskal(left);
			if (!(ret1[1] <= k && k <= ret2[1])) {
				System.out.println(-1);
			} else
				System.out.println(ret2[0]);
		}
	}

	static int[] kruskal(int add) {
		PriorityQueue<Edge> que = new PriorityQueue<Main.Edge>();
		for (int i = 0; i < m; ++i) {
			Edge e = new Edge(u[i], v[i], w[i], l[i]);
			if (l[i] == 'A')
				e.cost += add;
			que.add(e);
		}

		DJSet ds = new DJSet(n);
		int weight = 0;
		int cnt = 0;
		while (!que.isEmpty()) {
			Edge e = que.poll();
			if (ds.equiv(e.src, e.dst))
				continue;
			weight += e.cost;
			ds.setUnion(e.src, e.dst);
			if (e.c == 'A') {
				++cnt;
			}
		}
		return new int[] { weight - k * add, cnt };
	}

	static class DJSet {
		int n;
		int[] upper;

		public DJSet(int n) {
			this.n = n;
			upper = new int[n];
			Arrays.fill(upper, -1);
		}

		boolean equiv(int x, int y) {
			return root(x) == root(y);
		}

		int root(int x) {
			return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
		}

		void setUnion(int x, int y) {
			x = root(x);
			y = root(y);
			if (x == y)
				return;
			if (upper[x] > upper[y]) {
				int tmp = x;
				x = y;
				y = tmp;
			}
			upper[x] += upper[y];
			upper[y] = x;
		}
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

}

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

class Main {
	int n;
	long[] x;
	long[] y;
	long[] a;
	double[][] dist;
	double[] weight;
	long[] water;

	class Edge implements Comparable<Edge> {
		int u;
		int v;
		double cost;

		public Edge(int u_, int v_, double dist) {
			u = u_;
			v = v_;
			cost = dist;
		}

		@Override
		public int compareTo(Edge o) {
			return Double.compare(cost, o.cost);
		}
	}

	class UnionFind {
		int n;
		int[] upper;

		public UnionFind(int n_) {
			n = n_;
			upper = new int[n];
			Arrays.fill(upper, -1);
		}

		int root(int x) {
			return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
		}

		boolean equiv(int x, int y) {
			return root(x) == root(y);
		}

		void setUnion(int x, int y) {
			x = root(x);
			y = root(y);
			if (x == y)
				return;
			if (upper[x] < upper[y]) {
				x ^= y;
				y ^= x;
				x ^= y;
			}
			upper[y] += upper[x];
			upper[x] = y;
		}
	}

	void run() {
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		x = new long[n];
		y = new long[n];
		a = new long[n];
		dist = new double[n][n];
		weight = new double[1 << n];
		water = new long[1 << n];
		for (int i = 0; i < n; ++i) {
			x[i] = sc.nextInt();
			y[i] = sc.nextInt();
			a[i] = sc.nextInt();
		}
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				if (i == j)
					continue;
				dist[i][j] = Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
			}
		}
		for (int s = 1; s < 1 << n; ++s) {
			UnionFind uf = new UnionFind(n);
			PriorityQueue<Edge> pq = new PriorityQueue<>();
			for (int i = 0; i < n; ++i) {
				if ((s >> i) % 2 == 0)
					continue;
				water[s] += a[i];
				for (int j = i + 1; j < n; ++j) {
					if ((s >> j) % 2 == 0)
						continue;
					pq.add(new Edge(i, j, dist[i][j]));
				}
			}
			while (!pq.isEmpty()) {
				Edge e = pq.poll();
				if (uf.equiv(e.u, e.v))
					continue;
				weight[s] += e.cost;
				uf.setUnion(e.u, e.v);
			}
		}
		double[] a = new double[1 << n];
		a[0] = 1 << 30;
		for (int s = 0; s < 1 << n; ++s) {
			for (int s2 = ((1 << n) - 1) & ~s; s2 > 0; s2 = (s2 - 1) & ~s) {
				int m = Integer.bitCount(s2);
				int ns = s | s2;
				a[ns] = Math.max(a[ns], Math.min(a[s], ((water[s2] - weight[s2]) / m)));
			}
		}
		System.out.printf("%.20f", a[(1 << n) - 1]);
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

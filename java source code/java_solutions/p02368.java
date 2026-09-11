import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	// void run() {
	// Scanner sc = new Scanner(System.in);
	// int N = sc.nextInt();
	// int[][] x = new int[N][2];
	// for (int i = 0; i < N; ++i) {
	// x[i][0] = sc.nextInt();
	// x[i][1] = sc.nextInt();
	// }
	//
	// int ng = 1_000_000_000 + 1;
	// int ok = 0;
	// while (ng - ok > 1) {
	// int middle = (ok + ng) / 2;
	// if (f(N, x, middle)) {
	// ok = middle;
	// } else {
	// ng = middle;
	// }
	// }
	// }
	//
	// boolean f(int N, int[][] x, int d) {
	// int sz = 1;
	// while (sz < 2 * N)
	// sz *= 2;
	// int[][] arr = new int[2 * N][];
	// for (int i = 0; i < N; ++i) {
	// arr[i] = new int[] { x[i][0], i };
	// arr[i + N] = new int[] { x[i][1], i };
	// }
	// Arrays.sort(arr, new Comparator<int[]>() {
	// @Override
	// public int compare(int[] o1, int[] o2) {
	// return Integer.compare(o1[0], o2[0]);
	// }
	// });
	// ArrayList<Integer>[] lis = new ArrayList[N];
	// for (int i = 0; i < N; ++i) {
	// lis[i] = new ArrayList<>();
	// }
	// for (int i = 0; i < 2 * N; ++i) {
	// lis[arr[i][1]].add(i);
	// }
	// ArrayList<Integer>[] g = new ArrayList[2 * sz + sz - 1];
	// for (int i = 0; i < g.length; ++i) {
	// g[i] = new ArrayList<>();
	// }
	// // 0...2N:true
	// // i + 2N:false
	// for (int i = 0; i < N; ++i) {
	// int xId = lis[i].get(0);
	// int yId = lis[i].get(1);
	// g[xId + sz - 1].add(yId + 2 * sz - 1);
	// g[xId + 2 * sz - 1].add(yId + sz - 1);
	// g[yId + sz - 1].add(xId + 2 * sz - 1);
	// g[yId + 2 * sz - 1].add(xId + sz - 1);
	// }
	//
	// for (int i = 0; i < sz - 1; ++i) {
	// g[i].add(2 * i + 1);
	// g[i].add(2 * i + 2);
	// }
	//
	// for (int i = 0; i < 2 * N; ++i) {
	// int v = arr[i][0];
	// IntervalConnect(0, sz, v - d + 1, v, 0, g, arr, arr[i][1] + 2 * sz - 1);
	// }
	//
	// }
	//
	// void IntervalConnect(int a, int b, int l, int r, int k,
	// ArrayList<Integer>[] g, int[][] arr, int src) {
	// int L = arr[l][0];
	// int R = r < arr.length ? arr[r][0] : Integer.MAX_VALUE;
	// if (a <= L && R <= b) {
	// g[src].add(k);
	// } else if (b <= L || R <= a) {
	// return;
	// } else {
	// IntervalConnect(a, b, l, (l + r) / 2, 2 * k + 1, g, arr, src);
	// IntervalConnect(a, b, (L + R) / 2, R, 2 * k + 2, g, arr, src);
	// }
	// }

	@SuppressWarnings("unchecked")
	void run() {
		Scanner sc = new Scanner(System.in);
		int V = sc.nextInt();
		int E = sc.nextInt();
		ArrayList<Integer>[] g = new ArrayList[V];
		for (int i = 0; i < V; ++i) {
			g[i] = new ArrayList<>();
		}
		for (int i = 0; i < E; ++i) {
			int s = sc.nextInt();
			int t = sc.nextInt();
			g[s].add(t);
		}
		int[] col = new int[V];
		int cols = stronglyConnectedComponent(g, col);
		int Q = sc.nextInt();
		for (int i = 0; i < Q; ++i) {
			int u = sc.nextInt();
			int v = sc.nextInt();
			System.out.println(col[u] == col[v] ? 1 : 0);
		}
	}

	class State {
		int i;
		int j;
		int parent;

		public State(int i_, int j_, int parent_) {
			i = i_;
			j = j_;
			parent = parent_;
		}
	}

	int stronglyConnectedComponent(ArrayList<Integer>[] g, int[] col) {
		int n = g.length;
		int order = 0;
		int cols = 0;
		int[] ord = new int[n];
		int[] low = new int[n];
		Arrays.fill(col, -1);
		Arrays.fill(ord, -1);
		Arrays.fill(low, -1);
		ArrayDeque<State> stk = new ArrayDeque<>();
		ArrayDeque<Integer> pnd = new ArrayDeque<>();
		for (int ii = 0; ii < n; ++ii) {
			if (ord[ii] != -1) {
				continue;
			}
			stk.add(new State(ii, 0, -1));
			while (!stk.isEmpty()) {
				State s = stk.pollFirst();
				if (ord[s.i] == -1) {
					low[s.i] = (ord[s.i] = order++);
					pnd.addFirst(s.i);
				}
				if (s.j - 1 >= 0 && g[s.i].get(s.j - 1) != s.parent && col[g[s.i].get(s.j - 1)] == -1) {
					low[s.i] = Math.min(low[s.i], low[g[s.i].get(s.j - 1)]);
				}
				if (s.j == g[s.i].size() && low[s.i] == ord[s.i]) {
					while (true) {
						int v = pnd.pollFirst();
						col[v] = cols;
						if (v == s.i)
							break;
					}
					++cols;
					continue;
				}
				if (s.j == g[s.i].size()) {
					continue;
				}
				stk.addFirst(new State(s.i, s.j + 1, s.parent));
				int dst = g[s.i].get(s.j);
				if (ord[dst] == -1) {
					stk.addFirst(new State(dst, 0, s.i));
				} else if (col[dst] == -1) {
					low[s.i] = Math.min(low[s.i], low[dst]);
				}
			}
		}
		return cols;
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

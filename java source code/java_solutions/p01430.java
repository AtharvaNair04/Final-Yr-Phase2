import java.util.Arrays;
import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		new Main().run();
	}

	void run() {
		solver();
	}

	int[][] flow;
	boolean[][] g;
	int N;

	@SuppressWarnings("unchecked")
	void solver() {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		int E = sc.nextInt();
		int Q = sc.nextInt();
		g = new boolean[N][N];
		for (int i = 0; i < E; i++) {
			int F = sc.nextInt() - 1;
			int T = sc.nextInt() - 1;
			g[F][T] = true;
			g[T][F] = true;
		}
		flow = new int[N][N];

		int maxflow = 0;
		while (true) {
			Integer[] prev_v = dfs(0, N - 1, false);
			if (prev_v[N - 1] == -1)
				break;
			else {
				maxflow++;
				for (int i = N - 1; i != 0; i = prev_v[i]) {
					set_flow(prev_v[i], i, 1);
				}
			}
		}
		for (int i = 0; i < Q; i++) {
			int M = sc.nextInt();
			int A = sc.nextInt() - 1;
			int B = sc.nextInt() - 1;
			if (M == 1) {// joinet
				g[A][B] = true;
				g[B][A] = true;
				maxflow = calc_maxflow(maxflow);
			} else if (M == 2) { // disjoint
				g[A][B] = false;
				g[B][A] = false;
				if (flow[A][B] != 0) {
					if (maxflow == 0) {
						for (int j = 0; j < N; j++) {
							Arrays.fill(flow[j], 0);
						}
					} else {
						if (flow[A][B] < 0) {
							int tmp = A;
							A = B;
							B = tmp;
						}
						flow[A][B] = 0;
						flow[B][A] = 0;
						Integer[] prev_v1 = dfs(B, N - 1, true);
						Integer[] prev_v2 = dfs(0, A, true);
						if ((prev_v1[N - 1] != -1 || B == N - 1) && (A == 0 || prev_v2[A] != -1)) {
							maxflow--;
							if (B != N - 1) {
								for (int j = N - 1; j != B; j = prev_v1[j]) {
									set_flow(prev_v1[j], j, -1);
								}
							}
							if (A != 0) {
								for (int j = A; j != 0; j = prev_v2[j]) {
									set_flow(prev_v2[j], j, -1);
								}
							}
						} else {
							System.out.println("!");
							Integer[] prev_v = dfs(B, A, true);
							for (int j = A; j != 0; j = prev_v[j]) {
								if (prev_v[j] == -1)
									break;
								set_flow(prev_v[j], j, -1);
							}
						}
						maxflow = calc_maxflow(maxflow);
					}
				}
			}
			System.out.println(maxflow);
		}

	}

	int calc_maxflow(int maxflow) {
		while (true) {
			Integer[] prev_v = dfs(0, N - 1, false);
			if (prev_v[N - 1] == -1)
				break;
			else {
				maxflow++;
				for (int i = N - 1; i != 0; i = prev_v[i]) {
					set_flow(prev_v[i], i, 1);
				}
			}
		}
		return maxflow;
	}

	void set_flow(int src, int dst, int delta) {
		flow[src][dst] += delta;
		flow[dst][src] -= delta;
	}

	int residue(int src, int dst) {
		return 1 - flow[src][dst];
	}

	Integer[] dfs(int s, int t, boolean onflow) {
		Boolean[] arrived = new Boolean[N];
		Arrays.fill(arrived, false);
		Integer[] prev_v = new Integer[N];
		Arrays.fill(prev_v, -1);
		calc_dfs(t, s, prev_v, arrived, onflow);
		return prev_v;
	}

	boolean calc_dfs(int t, int cur, Integer[] prev_v, Boolean[] arrived, boolean onflow) {
		arrived[cur] = true;
		for (int i = 0; i < N; i++) {
			if (!arrived[i] && g[cur][i] && ((!onflow && residue(cur, i) > 0) || (onflow && flow[cur][i] > 0))) {
				prev_v[i] = cur;
				if (i == t)
					return true;
				if (calc_dfs(t, i, prev_v, arrived, onflow))
					return true;
			}
		}
		return false;
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

}

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int N = sc.nextInt();
		int M = sc.nextInt();
		int[][] g = new int[N * 2 + 1][N * 2 + 1];
		for (int i = 0; i < N; ++i) {
			g[i + N][i] = 1;
		}
		MaxFlow mf = new MaxFlow(N * 2 + 1);
		for (int i = 0; i < M; ++i) {
			int A = sc.nextInt() - 1;
			int B = sc.nextInt() - 1;
			g[A][N * 2] = g[B][N * 2] = 1;
			mf.set(g);
			System.out.println(mf.calc(0, N * 2) >= 2 ? "Yes" : "No");
			g[A][N * 2] = g[B][N * 2] = 0;
			g[A][B + N] = g[B][A + N] = 1;
		}
	}

	static class MaxFlow {

		int[][] g;
		int[][] res;

		MaxFlow(int size) {
			g = new int[size][size];
			res = new int[size][size];
		}

		void set(int[][] g) {
			for (int i = 0; i < g.length; ++i) {
				for (int j = 0; j < g.length; ++j) {
					g[i][j] = res[i][j] = g[i][j];
				}
			}
		}

		MaxFlow(int[][] g) {
			this.g = g;
			this.res = new int[g.length][];
			for (int i = 0; i < g.length; ++i) {
				this.res[i] = g[i].clone();
			}
		}

		int calc(int src, int sink) {
			int result = 0;
			int[] prev = new int[g.length];
			while (true) {
				Arrays.fill(prev, -1);
				boolean[] visited = new boolean[g.length];
				Queue<Integer> q = new LinkedList<Integer>();
				q.add(src);
				visited[src] = true;
				OUT: while (!q.isEmpty()) {
					int cur = q.poll();
					for (int i = 0; i < g.length; ++i) {
						if (!visited[i] && res[cur][i] > 0) {
							prev[i] = cur;
							visited[i] = true;
							if (i == sink) {
								break OUT;
							}
							q.add(i);
						}
					}
				}
				if (!visited[sink]) {
					break;
				}
				int pos = sink;
				int pathCap = Integer.MAX_VALUE;
				while (pos != src) {
					int p = prev[pos];
					pathCap = Math.min(pathCap, res[p][pos]);
					pos = p;
				}
				pos = sink;
				while (pos != src) {
					int p = prev[pos];
					res[p][pos] -= pathCap;
					res[pos][p] += pathCap;
					pos = p;
				}
				result += pathCap;
			}
			return result;
		}
	}

}

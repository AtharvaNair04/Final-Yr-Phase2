import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static final int MAX_DIST = 20000;
	public static final int INF = Integer.MAX_VALUE / 2 - 1;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			final int n = sc.nextInt();
			final int m = sc.nextInt();
			final int c = sc.nextInt();
			final int s = sc.nextInt();
			final int g = sc.nextInt();
			
			if(n == 0 && m == 0 && c == 0 && s == 0 && g == 0){
				break;
			}
			
			System.gc();
			
			int[][][] adj_dist = new int[c][n][n];
			for (int i = 0; i < c; i++) {
				for (int j = 0; j < n; j++) {
					for (int k = 0; k < n; k++) {
						if (j == k) {
							adj_dist[i][j][k] = 0;
						} else {
							adj_dist[i][j][k] = INF;
						}
					}
				}
			}

			for (int i = 0; i < m; i++) {
				final int xi = sc.nextInt();
				final int yi = sc.nextInt();
				final int di = sc.nextInt();
				final int ci = sc.nextInt();

				adj_dist[ci - 1][yi - 1][xi - 1] = Math.min(
						adj_dist[ci - 1][yi - 1][xi - 1], di);
				adj_dist[ci - 1][xi - 1][yi - 1] = Math.min(
						adj_dist[ci - 1][xi - 1][yi - 1], di);
			}

			int[] graph = new int[c];
			for (int i = 0; i < c; i++) {
				graph[i] = sc.nextInt();
			}

			int dist_in[][] = new int[c][];
			int cost_in[][] = new int[c][];

			for (int i = 0; i < c; i++) {
				dist_in[i] = new int[graph[i]];
				cost_in[i] = new int[graph[i]];

				dist_in[i][0] = 0;
				for (int j = 1; j < graph[i]; j++) {
					dist_in[i][j] = sc.nextInt();
				}

				for (int j = 0; j < graph[i]; j++) {
					cost_in[i][j] = sc.nextInt();
				}
			}

			/* dist cost */
			int[][] costs = new int[c][MAX_DIST + 1];
			for (int i = 0; i < c; i++) {
				int sum = 0;

				for (int dist = 0; dist <= MAX_DIST; dist++) {
					int pos = Arrays.binarySearch(dist_in[i], dist);

					if (pos < 0) {
						pos = -(pos + 2);
					} else {
						pos--;
					}

					if (pos >= 0) {
						sum += cost_in[i][pos];
					}

					costs[i][dist] = sum;
				}
			}

			/* calc_dist */
			for (int line = 0; line < c; line++) {
				for (int k = 0; k < n; k++) {
					for (int i = 0; i < n; i++) {
						for (int j = 0; j < n; j++) {
							adj_dist[line][i][j] = Math.min(
									adj_dist[line][i][j],
									adj_dist[line][i][k] + adj_dist[line][k][j]);
						}
					}
				}
			}

			/* replace_cost */
			int[][][] adj_cost = new int[c][n][n];
			for (int i = 0; i < c; i++) {
				for (int j = 0; j < n; j++) {
					for (int k = 0; k < n; k++) {
						adj_cost[i][j][k] = INF;
					}
				}
			}

			for (int line = 0; line < c; line++) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						if (adj_dist[line][i][j] < INF) {
							adj_cost[line][i][j] = costs[line][adj_dist[line][i][j]];
						} else {
							adj_cost[line][i][j] = INF;
						}
					}
				}
			}

			/* create graph */
			int[][] adj = new int[n][n];

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					adj[i][j] = INF;
				}
			}

			for (int line = 0; line < c; line++) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						adj[i][j] = Math.min(adj[i][j], adj_cost[line][i][j]);
					}
				}
			}

			for (int k = 0; k < n; k++) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						adj[i][j] = Math.min(adj[i][j], adj[i][k] + adj[k][j]);
					}
				}
			}

			System.out.println(adj[s - 1][g - 1] < INF ? adj[s - 1][g - 1] : -1);
		}
		
		sc.close();
	}
}

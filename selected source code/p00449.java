import java.util.Scanner;

public class Main {
	public static class Vertex {
		int     digree;
		Vertex  neighbor[];
		int     cost[];
		int     minCost;
		boolean flag;
		int label;

		Vertex(int n, int k) {
			digree = 0;
			neighbor = new Vertex[n];
			cost = new int[n];
			minCost = Integer.MAX_VALUE;
			flag = false;
			label = k;
		}
	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n, k;
		int order;
		Vertex v[];

		n = sc.nextInt();
		k = sc.nextInt();
		while (n + k != 0) {
			v = new Vertex[n];
			for (int i = 0; i < n; i++) {
				v[i] = new Vertex(n, i);
			}

			for (int i = 0; i < k; i++) {
				order = sc.nextInt();
				if (order == 1) {
					int v1 = sc.nextInt() - 1;
					int v2 = sc.nextInt() - 1;
					int cost = sc.nextInt();
					boolean flag = true;

					for (int j = 0; j < v[v1].digree && flag; j++) {
						if (v[v1].neighbor[j] == v[v2]) {
							v[v1].cost[j] = (cost < v[v1].cost[j]) ? cost : v[v1].cost[j];
							flag = false;
						}
					}
					for (int j = 0; j < v[v2].digree && !flag; j++) {
						if (v[v2].neighbor[j] == v[v1]) {
							v[v2].cost[j] = (cost < v[v2].cost[j]) ? cost : v[v2].cost[j];
						}
					}
					if (flag) {
						v[v1].neighbor[v[v1].digree] = v[v2];
						v[v1].cost[v[v1].digree++] = cost;
						v[v2].neighbor[v[v2].digree] = v[v1];
						v[v2].cost[v[v2].digree++] = cost;
					}
				} else if (order == 0) {
					int v1 = sc.nextInt() - 1;
					int v2 = sc.nextInt() - 1;
					System.out.println(dijkstra(v, n, v1, v2));
				}
			}
			n = sc.nextInt();
			k = sc.nextInt();
		}
	}

	public static int dijkstra(Vertex v[], int n, int start, int goal) {
		boolean flag = true;

		for (int i = 0; i < n; i++) {
			v[i].flag = false;
			v[i].minCost = Integer.MAX_VALUE;
		}
		v[start].minCost = 0;
		v[start].flag = true;

		while (!v[goal].flag) {
			flag = false;
			int minIndex  = -1;
			for (int i = 0; i < n; i++) {
				if (v[i].flag) {
					for (int j = 0; j < v[i].digree; j++) {
						if (!v[i].neighbor[j].flag) {
							if (v[i].minCost + v[i].cost[j] < v[i].neighbor[j].minCost) {
								v[i].neighbor[j].minCost = v[i].minCost + v[i].cost[j];
							}
							if (minIndex == -1) {
								minIndex  = v[i].neighbor[j].label;
							} else if (v[i].neighbor[j].minCost < v[minIndex].minCost) {
								minIndex  = v[i].neighbor[j].label;
							}
						}
					}
				}
			}
			if (minIndex != -1) {
				v[minIndex].flag = true;
				flag = true;
			}
			if (!flag) {
				return -1;
			}
		}
		return v[goal].minCost;
	}
}

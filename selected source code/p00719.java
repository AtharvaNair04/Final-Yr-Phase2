import java.util.HashSet;
import java.util.Scanner;

public class Main {
	static int n, m, p, s, g;
	static double ticket[];
	static Node city[];

	static class Node {
		int num;
		int degree;
		Node neighbor[] = new Node[30];
		double cost[] = new double[30];
		double minCost[] = new double[257];

		Node(int n) {
			this.num = n;
		}

		void joint(Node n, double cost) {
			this.neighbor[this.degree] = n;
			this.degree++;
			this.cost[n.num] = cost;
			n.neighbor[n.degree] = this;
			n.degree++;
			n.cost[this.num] = cost;
		}
	}

	static class Pair {
		int num;
		int ticket;

		Pair(int num, int ticket) {
			this.num = num;
			this.ticket = ticket;
		}
		
		@Override
		public int hashCode() {
			// TODO 自動生成されたメソッド・スタブ
			return (num * ticket) ^ ticket;
		}
		
		@Override
		public boolean equals(Object obj) {
			// TODO 自動生成されたメソッド・スタブ
			Pair p = (Pair)obj;
			return p.num == num && p.ticket == ticket;
		}
	}

	public static void dijkstra(int s) {
		int id = s;
		int t = (1 << n) - 1;

		HashSet<Pair> set = new HashSet<Pair>();

		while (true) {
			for (int i = 0; i < city[id].degree; i++) {
				Node nextNode = city[id].neighbor[i];
				for (int j = 0; j < n; j++) {
					if (0 < (t & (1 << j))) {
						int nticket = t ^ (1 << j);
						if (nextNode.minCost[nticket] == 0 || city[id].minCost[t] + city[id].cost[nextNode.num] / ticket[j] < nextNode.minCost[nticket]) {
							nextNode.minCost[nticket] = city[id].minCost[t] + city[id].cost[nextNode.num] / ticket[j];
							set.add(new Pair(nextNode.num, nticket));
						}
					}
				}
			}
			if (set.isEmpty()) {
				break;
			}
			Pair min = null;
			for (Pair p: set) {
				if (min == null || city[p.num].minCost[p.ticket] < city[min.num].minCost[min.ticket]) {
					min = p;
				}
			}
			if (min.num == g - 1 && min.ticket == 0) {
				break;
			}
			id = min.num;
			t = min.ticket;
			set.remove(min);
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			p = sc.nextInt();
			s = sc.nextInt();
			g = sc.nextInt();
			if ((n | m | p | s | g) == 0) {
				break;
			}

			ticket = new double[n];
			for (int i = 0; i < n; i++) {
				ticket[i] = sc.nextInt();
			}

			city = new Node[m];
			for (int i = 0; i < m; i++) {
				city[i] = new Node(i);
			}
			for (int i = 0; i < p; i++) {
				int x = sc.nextInt();
				int y = sc.nextInt();
				double z = sc.nextInt();
				city[x - 1].joint(city[y - 1], z);
			}

			dijkstra(s - 1);

			double min = Double.MAX_VALUE;
			for (int j = 0; j < 257; j++) {
				if (city[g - 1].minCost[j] != 0) {
					min = Math.min(min, city[g - 1].minCost[j]);
				}
			}

			if (min == Double.MAX_VALUE) {
				System.out.println("Impossible");
			} else {
				System.out.println(min);
			}
		}
	}
}

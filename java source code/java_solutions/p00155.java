import java.util.Scanner;

public class Main {
	static class Building {
		int x, y;
		double cost;
		boolean mark;
		int preIndex;

		Building(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		Building buil[];

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}

			buil = new Building[n + 1];
			for (int i = 0; i < n; i++) {
				int number = sc.nextInt();
				int x = sc.nextInt();
				int y = sc.nextInt();
				buil[number] = new Building(x, y);
			}

			m = sc.nextInt();
			for (int i = 0; i < m; i++) {
				int start = sc.nextInt();
				int goal = sc.nextInt();
				for (int j = 1; j <= n; j++) {
					buil[j].cost = -1;
					buil[j].mark = false;
					buil[j].preIndex = -1;
				}
				buil[start].cost = 0;
				buil[start].mark = true;
				myDijkstra(buil, n, start, goal);

				if (buil[goal].preIndex == -1) {
					System.out.println("NA");
				} else {
					String s = "";
					String str = "";
					int index = goal;
					while (true) {
						str = index + s + str;
						if (index == start) {
							break;
						}
						index = buil[index].preIndex;
						s = " ";
					}
					System.out.println(str);
				}
			}
		}
	}

	public static void myDijkstra(Building buil[], int n, int start, int goal) {
		for (int i = 1; i <= n; i++) {
			double length = Math.hypot(buil[i].x - buil[start].x, buil[i].y - buil[start].y);
			if (length <= 50.0) {
				if (buil[i].cost == -1 || buil[start].cost + length < buil[i].cost) {
					buil[i].cost = buil[start].cost + length;
					buil[i].preIndex = start;
				}
			}
		}
		int minIndex = -1;
		for (int i = 1; i <= n; i++) {
			double length = Math.hypot(buil[i].x - buil[start].x, buil[i].y - buil[start].y);
			if (!buil[i].mark && buil[i].cost != -1 && (minIndex == -1 || buil[i].cost < buil[minIndex].cost)) {
				minIndex = i;
			}
		}
		if (minIndex != -1) {
			buil[minIndex].mark = true;
			myDijkstra(buil, n, minIndex, goal);
		}
	}
}

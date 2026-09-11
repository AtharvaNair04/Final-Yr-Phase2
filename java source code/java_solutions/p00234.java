import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		new Main().run();
	}

	private void run() {

		Scanner sc = new Scanner(System.in);

		int dir[][] = { { 0, -1 }, { 0, 1 }, { 1, 0 } };

		while (true) {
			final int w = sc.nextInt();
			final int h = sc.nextInt();

			if (w == 0 && h == 0) {
				break;
			}

			final int amount = sc.nextInt();
			final int capacity = sc.nextInt();
			final int init = sc.nextInt();

			int[][] map = new int[h][w];

			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					map[i][j] = sc.nextInt();
				}
			}

			PriorityQueue<Walk> queue = new PriorityQueue<Walk>();
			for (int i = 0; i < w; i++) {
				boolean[][] b = new boolean[h][w];
				Walk walk = new Walk(0, init, 0, i, b);
				walk.sanso--;
				queue.add(walk);
			}

			boolean flag = false;
			while (!queue.isEmpty()) {
				Walk walk = queue.poll();
				int y = walk.y;
				int x = walk.x;

				if (walk.sanso <= 0) {
					continue;
				} else if (walk.y >= h) {
					flag = true;
					System.out.println(walk.cost);
					break;
				}

				walk.visited[y][x] = true;

				if (map[y][x] <= 0) {
					walk.cost += (-map[y][x]);
				} else {
					walk.sanso = Math.min(map[y][x] + walk.sanso, capacity);
				}

				if (walk.cost > amount) {
					continue;
				}

				for (int i = 0; i < 3; i++) {
					int ny = y + dir[i][0];
					int nx = x + dir[i][1];
					if (nx >= 0 && nx < w && ny >= 0 && ny <= h - 1
							&& !walk.visited[ny][nx]) {
						boolean[][] nb = new boolean[h][w];
						for(int j = 0;j<h;j++){
							nb[j] = Arrays.copyOf(walk.visited[j], w);
						}
						Walk wa = new Walk(walk.cost, walk.sanso-1, ny, nx, nb);
						queue.add(wa);
					} else if (i == 2 && walk.y == h - 1) {
						Walk wa = new Walk(walk);
						wa.y++;
						queue.add(wa);
					}
				}
			}

			if (!flag) {
				System.out.println("NA");
			}

		}
	}
}

class Walk implements Comparable<Walk> {
	int cost;
	int sanso;
	int x, y;
	boolean[][] visited;

	public Walk(int cost, int sanso, int y, int x, boolean[][] visited) {
		super();
		this.cost = cost;
		this.sanso = sanso;
		this.x = x;
		this.y = y;
		this.visited = visited;
	}

	public Walk(Walk w) {
		this.cost = w.cost;
		this.sanso = w.sanso;
		this.x = w.x;
		this.y = w.y;
		visited = new boolean[w.visited.length][w.visited[0].length];
		for (int i = 0; i < w.visited.length; i++) {
			visited[i] = Arrays.copyOf(w.visited[i], w.visited[0].length);
		}
	}

	@Override
	public int compareTo(Walk arg0) {
		return this.cost - arg0.cost;
	}
}

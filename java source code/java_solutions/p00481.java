import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Main {
	public static char[][] maze;
	public final static int INF = 1000000;
	public final static int[] dx = { 0, 1, 0, -1 };
	public final static int[] dy = { 1, 0, -1, 0 };
	public static int gx;
	public static int gy;
	public static int h, w, n;
	public static int[][] d;
	public static Pair pair = null;
	public static int ans = 0;

	public static void main(String[] args) {
		read();
		for (int i = 1; i <= n; i++) {
			ans += bfs(i);
		}
		System.out.println(ans);
	}

	public static int bfs(int count) {
		init(count);

		Queue<Pair> queue = new ArrayDeque<Pair>();
		queue.add(pair);

		while (!queue.isEmpty()) {
			Pair p = queue.poll();
			// if goal
			if (p.i == gx && p.j == gy) {
				return d[p.i][p.j];
			}
			// move
			for (int k = 0; k < 4; k++) {

				int nx = p.i + dx[k];
				int ny = p.j + dy[k];

				// check it is enable to move
				if (0 <= nx && nx < h && 0 <= ny && ny < w) {
					if (maze[nx][ny] != 'X' && d[nx][ny] == INF) {
						// add distance +1
						queue.add(new Pair(nx, ny));
						d[nx][ny] = d[p.i][p.j] + 1;
					}
				}
			}

		}
		return 0;
	}

	public static void init(int goal) {
		String start = String.valueOf(goal - 1);
		if (goal == 1) {
			start = "S";
		}
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				d[i][j] = INF;
				// search "start"
				if (String.valueOf(maze[i][j]).equals(start)) {
					pair = new Pair(i, j);
					// start of distance = 0
					d[i][j] = 0;
				}

				// search "goal"
				if (String.valueOf(maze[i][j]).equals(String.valueOf(goal))) {
					gx = i;
					gy = j;
				}

			}
		}

	}

	public static void read() {
		Scanner s = new Scanner(System.in);
		h = s.nextInt();
		w = s.nextInt();
		n = s.nextInt();
		d = new int[h][w];
		maze = new char[h][w];
		for (int i = 0; i < h; i++) {
			maze[i] = s.next().toCharArray();
		}

	}
}

class Pair {
	int i, j;

	public Pair(int i, int j) {
		this.i = i;
		this.j = j;
	}

}

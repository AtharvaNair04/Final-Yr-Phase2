import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			h = scanner.nextInt();
			w = scanner.nextInt();
			if ((h | w) == 0)
				break;
			char[][] map = new char[h][w];
			for (int i = 0; i < h; i++) {
				map[i] = scanner.next().toCharArray();
				for (int j = 0; j < w; j++) {
					if (map[i][j] == 'A') {
						ay = i;
						ax = j;
					} else if (map[i][j] == 'B') {
						by = i;
						bx = j;
					}
				}
			}
			int[][] dp = new int[h][w];
			for (int[] dp1 : dp)
				Arrays.fill(dp1, 1 << 30);
			Deque<Point> deque = new ArrayDeque<Main.Point>();
			deque.push(new Point(ay, ax, 0));
			dp[ay][ax] = 0;
			while (!deque.isEmpty()) {
				Point p = deque.poll();
				int y = p.y;
				int x = p.x;
				int step = p.step + 1;

				for (int[] m : move) {
					int ny = y + m[0];
					int nx = x + m[1];
					if (!isOK(ny, nx))
						continue;
					if (map[ny][nx] == '#')
						continue;
					if (dp[ny][nx] <= step)
						continue;
					dp[ny][nx] = step;
					deque.offer(new Point(ny, nx, step));
				}

			}
			char[] p = scanner.next().toCharArray();
			int e;
			for (e = 0; e < 200; e++) {
				if (dp[by][bx] <= e)
					break;
				char o = p[e % p.length];
				int ny = by + (o == '2' ? 1 : 0) - (o == '8' ? 1 : 0);
				int nx = bx + (o == '6' ? 1 : 0) - (o == '4' ? 1 : 0);
				if (!isOK(ny, nx))
					continue;
				by = ny;
				bx = nx;
			}
			System.out
					.println(e < 200 ? e + " " + by + " " + bx : "impossible");
		}
	}

	private boolean isOK(int ny, int nx) {
		return 0 <= ny && ny < h && 0 <= nx && nx < w;
	}

	int ay, ax, by, bx, h, w;
	int[][] move = { { -1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 } };

	class Point {
		int y, x, step;

		public Point(int y, int x, int step) {
			super();
			this.y = y;
			this.x = x;
			this.step = step;
		}

		@Override
		public String toString() {
			return "Point [y=" + y + ", x=" + x + ", step=" + step + "]";
		}

	}
}

import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			w = scanner.nextInt();
			h = scanner.nextInt();
			n = scanner.nextInt();
			if ((w | h | n) == 0)
				break;
			size = new int[n + 1];
			for (int i = 0; i < n; i++) {
				int b = scanner.nextInt();
				int k = scanner.nextInt();
				size[b] = k;
			}
			map = new int[h][w];
			pos = new int[n + 1][2];
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					int s = scanner.nextInt();
					map[i][j] = s;
					if (s > 0) {
						pos[s][0] = i;
						pos[s][1] = j;
					}
				}
			}
			count = 0;
			ans = new int[h][w];
			f(1);
			if (count == 1) {
				for (int i = 0; i < h; i++) {
					for (int j = 0; j < w; j++) {
						System.out.print(ans[i][j]);
						System.out.print(j == w - 1 ? '\n' : ' ');
					}
				}
			} else {
				System.out.println("NA");
			}
		}
	}

	private void f(int k) {
		PriorityQueue<Point> pq = new PriorityQueue<Point>();
		pq.add(new Point(map, 1));
		while (!pq.isEmpty()) {
			Point point = pq.poll();
			int[][] map = point.map;
			k = point.k;
			if (k == n + 1) {
				count++;
				if (count == 2)
					return;
				ans = map;
				continue;
			}
			int s = size[k];
			for (int r = 1; r <= s; r++) {
				if (s % r != 0)
					continue;
				int y = pos[k][0];
				int x = pos[k][1];
				for (int ui = y + 1 - s / r; ui <= y; ui++) {
					if (ui < 0)
						continue;
					loop: for (int lj = x + 1 - r; lj <= x; lj++) {
						if (lj < 0)
							continue;
						int di = ui + s / r;
						int rj = lj + r;
						if (di > h || rj > w)
							continue;
						for (int i = ui; i < di; i++) {
							for (int j = lj; j < rj; j++) {
								if (map[i][j] != 0 && map[i][j] != k)
									continue loop;
							}
						}
						int[][] tmp = new int[h][w];
						for (int i = 0; i < h; i++)
							tmp[i] = Arrays.copyOf(map[i], w);
						for (int i = ui; i < di; i++) {
							for (int j = lj; j < rj; j++) {
								if (tmp[i][j] != k)
									tmp[i][j] = k;
							}
						}
						pq.add(new Point(tmp, k + 1));

					}
				}
			}
		}
	}
	class Point implements Comparable<Point> {
		int[][] map;
		int k;

		public Point(int[][] map, int k) {
			super();
			this.map = map;
			this.k = k;
		}

		@Override
		public int compareTo(Point o) {
			return o.k - this.k;
		}

	}

	int count;
	int h, w, n;
	int[] size;
	int[][] map;
	int[][] pos;
	int[][] ans;

}

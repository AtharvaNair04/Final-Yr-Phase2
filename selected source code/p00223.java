import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			int w = scanner.nextInt();
			int h = scanner.nextInt();
			if ((w | h) == 0)
				break;
			int a = scanner.nextInt();
			int b = scanner.nextInt();
			int c = scanner.nextInt();
			int d = scanner.nextInt();
			Pair kazuyuki = new Pair(b, a);
			Pair takayuki = new Pair(d, c);
			boolean[][] map = new boolean[h + 2][w + 2];
			for (int i = 1; i <= h; i++) {
				for (int j = 1; j <= w; j++) {
					map[i][j] = scanner.nextInt() == 0;
				}
			}
			/*PriorityQueue<Pair> pq = new PriorityQueue<Pair>();*/
			Deque<Pair> pq = new ArrayDeque<Pair>();
			int dis = dist(takayuki, kazuyuki);
			pq.add(new Pair(takayuki.y, takayuki.x, kazuyuki.y, kazuyuki.x, 0,
					dis));
			String ans = "NA";
			boolean[][][][] mem = new boolean[h + 2][w + 2][h + 2][w + 2];
			mem[takayuki.y][takayuki.x][kazuyuki.y][kazuyuki.x] = true;
			while (!pq.isEmpty()) {
				Pair pair = pq.poll();
				if (pair.y == pair.dy && pair.x == pair.dx) {
					ans = String.valueOf(pair.cost / 2);
					break;
				}

				for (int i = 0; i < 4; i++) {
					int nty = pair.y + move[i][0];
					int ntx = pair.x + move[i][1];
					int nky = pair.dy + move[i][2];
					int nkx = pair.dx + move[i][3];
					if (!map[nty][ntx]) {
						nty = pair.y;
						ntx = pair.x;
					}
					if (!map[nky][nkx]) {
						nky = pair.dy;
						nkx = pair.dx;
					}
					if (mem[nty][ntx][nky][nkx])
						continue;
					mem[nty][ntx][nky][nkx] = true;
					int newdis = dist(new Pair(nty, ntx), new Pair(nky, nkx));
					pq.offer(new Pair(nty, ntx, nky, nkx, pair.cost + 2,
							pair.cost + 2 + newdis));
				}
			}
			System.out.println(ans);
		}
	}

	int[][] move = { { -1, 0, 1, 0 }, { 0, -1, 0, 1 }, { 1, 0, -1, 0 },
			{ 0, 1, 0, -1 } };

	int dist(Pair a, Pair b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}

	class Pair implements Comparable<Pair> {
		int y;
		int x;
		int dy;
		int dx;
		int cost;
		int md;

		public Pair(int y, int x, int dy, int dx, int cost, int md) {
			super();
			this.y = y;
			this.x = x;
			this.dy = dy;
			this.dx = dx;
			this.cost = cost;
			this.md = md;
		}

		public Pair(int y, int x) {
			super();
			this.y = y;
			this.x = x;
		}

		@Override
		public String toString() {
			return "Pair [y=" + y + ", x=" + x + ", dy=" + dy + ", dx=" + dx
					+ ", cost=" + cost + ", md=" + md + "]";
		}

		@Override
		public int compareTo(Pair o) {
			if (this.md == o.md)
				return this.cost - o.cost;
			return this.md - o.md;
		}

	}
}

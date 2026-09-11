import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
	int n, w, h;
	char[][] f;
	int sx, sy;
	int[] gx, gy;
	int[][] g;

	class D implements Comparable<D>{
		int pos;
		int min;
		int bit;

		D(int pos, int min, int bit) {
			this.pos = pos;
			this.min = min;
			this.bit = bit;
		}

		@Override
		public int compareTo(D o) {
			if (this.min != o.min)
				return this.min - o.min;
			if (this.pos != o.pos)
				return this.pos - o.pos;
			return this.bit - o.bit;
		}
	}

	int dist(int sx, int sy, int gx, int gy) {
		int[][] visit = new int[h][w];
		int[] dx = {-1, 0, 0, 1};
		int[] dy = {0, -1, 1, 0};
		for (int i = 0; i < h; i++) {
			Arrays.fill(visit[i], -1);
		}
		visit[sy][sx] = 0;

		Queue<Integer> queueX = new LinkedList<Integer>();
		Queue<Integer> queueY = new LinkedList<Integer>();
		queueX.add(sx);
		queueY.add(sy);
		while (!queueX.isEmpty()) {
			int x = queueX.poll();
			int y = queueY.poll();
			if (x == gx && y == gy) {
				return visit[y][x];
			}
			for (int i = 0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				if (nx < 0 || w <= nx || ny < 0 || h <= ny) continue;
				if (f[ny][nx] == 'x') continue;
				if (visit[ny][nx] != -1) continue;

				queueX.add(nx);
				queueY.add(ny);
				visit[ny][nx] = visit[y][x] + 1;
			}
		}
		return -1;
	}

	int dijkstra(int sx, int sy) {
		PriorityQueue<D> queue = new PriorityQueue<D>();
		int mask = (1 << n) - 1;
		queue.add(new D(0, 0, 0));

		while (!queue.isEmpty()) {
			D d = queue.poll();
			int pos = d.pos;
			int min = d.min;
			int bit = d.bit;
			if (bit == mask) {
				return min;
			}
			for (int i = 0; i < n; i++) {
				if (0 < (bit & (1 << i))) continue;
				int npos = i + 1;
				if (g[pos][npos] < 0) continue;
				int nmin = min + g[pos][npos];
				int nbit = bit | (1 << i);
				queue.add(new D(npos, nmin, nbit));
			}
		}

		return -1;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			w = sc.nextInt();
			h = sc.nextInt();
			if ((w | h) == 0) {
				break;
			}
			n = 0;
			f = new char[h][];
			for (int i = 0; i < h; i++) {
				f[i] = sc.next().toCharArray();
				for (int j = 0; j < w; j++) {
					if (f[i][j] == '*') n++;
				}
			}
			gx = new int[n];
			gy = new int[n];
			int id = 0;
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					if (f[i][j] == '*') {
						gx[id] = j;
						gy[id] = i;
						id++;
					} else if (f[i][j] == 'o') {
						sx = j;
						sy = i;
					}
				}
			}

			g = new int[n + 1][n + 1];
			for (int i = 0; i < n; i++) {
				int dist = dist(sx, sy, gx[i], gy[i]);
				g[0][i + 1] = g[i + 1][0] = dist;
			}
			for (int i = 0; i < n; i++) {
				for (int j = i + 1; j < n; j++) {
					int dist = dist(gx[i], gy[i], gx[j], gy[j]);
					g[i + 1][j + 1] = g[j + 1][i + 1] = dist;
				}
			}

			System.out.println(dijkstra(sx, sy));
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

	public void mapDebug(int[][] a) {
		System.out.println("--------map display---------");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.printf("%3d ", a[i][j]);
			}
			System.out.println();
		}
		System.out.println("----------------------------" + '\n');
	}

	class MyScanner {
		int read() {
			try {
				return System.in.read();
			} catch (IOException e) {
				throw new InputMismatchException();
			}
		}

		boolean isSpaceChar(int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		boolean isEndline(int c) {
			return c == '\n' || c == '\r' || c == -1;
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		int[] nextIntArray(int n) {
			int[] array = new int[n];
			for (int i = 0; i < n; i++)
				array[i] = nextInt();
			return array;
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		long[] nextLongArray(int n) {
			long[] array = new long[n];
			for (int i = 0; i < n; i++)
				array[i] = nextLong();
			return array;
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}

		double[] nextDoubleArray(int n) {
			double[] array = new double[n];
			for (int i = 0; i < n; i++)
				array[i] = nextDouble();
			return array;
		}

		String next() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		String[] nextStringArray(int n) {
			String[] array = new String[n];
			for (int i = 0; i < n; i++)
				array[i] = next();

			return array;
		}

		String nextLine() {
			int c = read();
			while (isEndline(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isEndline(c));
			return res.toString();
		}
	}
}

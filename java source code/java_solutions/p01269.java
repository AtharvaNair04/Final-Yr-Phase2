import java.io.IOException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
	int n, m, l;
	List<Integer>[] e;
	int[][] dist, ene;

	class D implements Comparable<D> {
		int pos;
		int rem;
		int min;

		public D(int pos, int rem, int min) {
			this.pos = pos;
			this.rem = rem;
			this.min = min;
		}

		@Override
		public int compareTo(D o) {
			if (this.min != o.min) {
				return this.min - o.min;
			}
			if (this.pos != o.pos) {
				return this.pos - o.pos;
			}
			if (this.rem != o.rem) {
				return this.rem - o.rem;
			}
			return 0;
		}
	}

	int dijkstra() {
		PriorityQueue<D> queue = new PriorityQueue<D>();
		queue.add(new D(0, l, 0));
		boolean[][] vis = new boolean[n][l + 1];

		while (!queue.isEmpty()) {
			D d = queue.poll();
			int pos = d.pos;
			int rem = d.rem;
			int min = d.min;

			if (vis[pos][rem]) {
				continue;
			}
			vis[pos][rem] = true;

			if (pos == n - 1) {
				return min;
			}

			for (Integer to : e[pos]) {
				int npos = to;
				int nrem = rem;
				int nmin = min + ene[pos][npos];
				if (!vis[npos][nrem]) {
					queue.add(new D(npos, nrem, nmin));
				}

				nrem = rem - dist[pos][npos];
				nmin = min;
				if (nrem < 0) {
					continue;
				}
				if (!vis[npos][nrem]) {
					queue.add(new D(npos, nrem, nmin));
				}
			}
		}

		return -1;
	}

	@SuppressWarnings("unchecked")
	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			l = sc.nextInt();
			if ((n | m | l) == 0) {
				break;
			}
			e = new LinkedList[n];
			for (int i = 0; i < n; i++) {
				e[i] = new LinkedList<Integer>();
			}
			dist = new int[n][n];
			ene = new int[n][n];
			for (int i = 0; i < m; i++) {
				int a = sc.nextInt() - 1;
				int b = sc.nextInt() - 1;
				int d = sc.nextInt();
				int f = sc.nextInt();
				e[a].add(b);
				e[b].add(a);
				dist[a][b] = dist[b][a] = d;
				ene[a][b] = ene[b][a] = f;
			}

			System.out.println(dijkstra());
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

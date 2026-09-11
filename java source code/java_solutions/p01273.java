import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	int[] par;
	int count;

	void union(int x, int y) {
		x = root(x);
		y = root(y);
		par[x] = y;
	}

	boolean find(int x, int y) {
		return root(x) == root(y);
	}

	int root(int x) {
		if (par[x] == x) return x;
		return root(par[x]);
	}

	class Data implements Comparable<Data>{
		int t, u, v;

		Data(int t, int u, int v) {
			this.t = t;
			this.u = u;
			this.v = v;
		}

		@Override
		public int compareTo(Data o) {
			return this.t - o.t;
		}
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			int n = sc.nextInt();
			int m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}
			Data[] d = new Data[m];
			for (int i = 0; i < m; i++) {
				d[i] = new Data(sc.nextInt(), sc.nextInt() - 1, sc.nextInt() - 1);
			}
			Arrays.sort(d);
			par = new int[n];
			for (int i = 0; i < n; i++) {
				par[i] = i;
			}

			count = 1;
			for (int i = 0; i < m; i++) {
				int u = d[i].u;
				int v = d[i].v;

				if (find(0, u) && !find(u, v)) {
					union(u, v);
					count++;
				}
			}

			System.out.println(count);
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

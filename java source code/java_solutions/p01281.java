import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
	int w, h;
	int[][] f;
	int[] dx = {0, 1};
	int[] dy = {1, 0};
	int count;

	void bt(int k, int x, int y) {
		if (x == 0 && y == h) {
			count++;
			return;
		}
		int nx = x + 1;
		int ny = y;
		if (w <= nx) {
			nx = 0;
			ny++;
		}
		if (f[y][x] == 0) {
			for (int i = 0; i < 2; i++) {
				int mx = x + dx[i];
				int my = y + dy[i];
				if (w <= mx || h <= my || f[my][mx] != 0) continue;
				f[y][x] = f[my][mx] = k;
				if (check(x, y)) {
					bt(k + 1, nx, ny);
				}
				f[y][x] = f[my][mx] = 0;
			}
		} else {
			bt(k, nx, ny);
		}
		return;
	}

	boolean check(int x, int y) {
		int[] dx = {-1, -1, 0, 0};
		int[] dy = {-1, 0, -1, 0};
		int[] color = new int[4];
		for (int i = 0; i < 4; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			if (nx < 0 || w <= nx || ny < 0 || h <= ny) return true;
			color[i] = f[ny][nx];
		}
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 4; j++) {
				if (color[i] == color[j]) {
					return true;
				}
			}
		}

		return false;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			w = sc.nextInt();
			h = sc.nextInt();
			if ((w | h) == 0) {
				break;
			}
			count = 0;
			f = new int[h][w];
			bt(1, 0, 0);
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

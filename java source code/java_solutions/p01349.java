import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
	int h, w, n;
	char[][] f;

	char[][] swap(int x1, int y1, int x2, int y2) {
		char[][] res = new char[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				res[i][j] = f[i][j];
			}
		}
		char tmp = res[y1][x1];
		res[y1][x1] = res[y2][x2];
		res[y2][x2] = tmp;
		return res;
	}

	void fall(char[][] f) {
		for (int j = 0; j < w; j++) {
			int id = h - 1;
			for (int i = h - 1; 0 <= i; i--) {
				if (f[i][j] != '.') {
					f[id--][j] = f[i][j];
				}
			}
			for (int i = id; 0 <= i; i--) {
				f[i][j] = '.';
			}
		}
	}

	void dis(char[][] f) {
		int[][] disW = new int[h][w];
		for (int i = 0; i < h; i++) {
			int cnt = 1;
			for (int j = 1; j < w; j++) {
				if (f[i][j] == f[i][j - 1]) {
					cnt++;
				} else {
					cnt = 1;
				}
				if (n <= cnt) {
					for (int k = 0; k < n; k++) {
						if (disW[i][j - k] == 1) break;
						disW[i][j - k] = 1;
					}
				}
			}
		}
		int[][] disH = new int[h][w];
		for (int j = 0; j < w; j++) {
			int cnt = 1;
			for (int i = 1; i < h; i++) {
				if (f[i][j] == f[i - 1][j]) {
					cnt++;
				} else {
					cnt = 1;
				}
				if (n <= cnt) {
					for (int k = 0; k < n; k++) {
						if (disH[i - k][j] == 1) break;
						disH[i - k][j] = 1;
					}
				}
			}
		}

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (disW[i][j] == 0 && disH[i][j] == 0)
					continue;
				f[i][j] = '.';
			}
		}
	}

	int count(char[][] f) {
		int cnt = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (f[i][j] != '.') cnt++;
			}
		}
		return cnt;
	}

	boolean check(char[][] f) {
		int pre = count(f);
		while (true) {
			fall(f);
			dis(f);
			int tmp = count(f);
			if (pre == tmp) break;
			pre = tmp;
		}
		return pre == 0;
	}

	void run() {
		MyScanner sc = new MyScanner();

		h = sc.nextInt();
		w = sc.nextInt();
		n = sc.nextInt();
		f = new char[h][];
		for (int i = 0; i < h; i++) {
			f[i] = sc.next().toCharArray();
		}

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int nx = j + 1;
				int ny = i;
				if (w <= nx || h <= ny) continue;
				char[][] nf = swap(j, i, nx, ny);
				if (check(nf)) {
					System.out.println("YES");
					return;
				}
			}
		}
		System.out.println("NO");
	}

	public static void main(String[] args) {
		new Main().run();
	}

	public void mapDebug(char[][] a) {
		System.out.println("--------map display---------");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.printf("%c", a[i][j]);
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

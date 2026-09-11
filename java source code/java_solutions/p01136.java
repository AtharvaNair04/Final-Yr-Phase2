import java.io.IOException;
import java.util.InputMismatchException;
import java.util.TreeSet;

public class Main {
	@SuppressWarnings("unchecked")
	void run() {
		MyScanner sc = new MyScanner();

		L: while (true) {
			int n = sc.nextInt();
			if (n == 0) {
				break;
			}
			int[][] f = new int[n][31];
			for (int i = 0; i < n; i++) {
				int m = sc.nextInt();
				for (int j = 0; j < m; j++) {
					f[i][sc.nextInt()] = 1;
				}
			}

			TreeSet<Integer>[] set = new TreeSet[n];
			for (int i = 0; i < n; i++) {
				set[i] = new TreeSet<Integer>();
				set[i].add(i);
			}
			for (int day = 0; day < 31; day++) {
				for (int i = 0; i < n; i++) {
					if (f[i][day] == 0) continue;
					for (int j = 0; j < n; j++) {
						if (f[j][day] == 0) continue;
						set[i].addAll(set[j]);
						set[j].addAll(set[i]);
					}
				}

				for (int i = 0; i < n; i++) {
					if (set[i].size() == n) {
						System.out.println(day);
						continue L;
					}
				}
			}
			System.out.println(-1);
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

import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	class Pair implements Comparable<Pair> {
		int num;
		char color;

		Pair(int num, char color) {
			this.num = num;
			this.color = color;
		}

		@Override
		public int compareTo(Pair o) {
			return this.color != o.color ? this.color - o.color : this.num - o.num;
		}
	}

	boolean check(Pair a, Pair b, Pair c) {
		if (a.color != b.color || b.color != c.color) {
			return false;
		}
		if (a.num == b.num && b.num == c.num) {
			return true;
		}
		if (a.num + 1 == b.num && b.num + 1 == c.num) {
			return true;
		}
		return false;
	}

	boolean check(Pair[] p, int k, boolean[] use) {
		if (9 <= k) {
			return true;
		}
		if (use[k]) {
			if (check(p, k + 1, use)) {
				return true;
			}
		} else {
			for (int i = 0; i < 9; i++) {
				if (use[i]) {
					continue;
				}
				for (int j = i + 1; j < 9; j++) {
					if (use[j]) {
						continue;
					}
					if (check(p[k], p[i], p[j])) {
						use[k] = use[i] = use[j] = true;
						if (check(p, k + 1, use)) {
							return true;
						}
						use[k] = use[i] = use[j] = false;
					}
				}
			}
		}
		return false;
	}

	void run() {
		MyScanner sc = new MyScanner();

		for (int T = sc.nextInt() - 1; 0 <= T; T--) {
			int n = 9;
			Pair[] p = new Pair[n];
			int[] num = new int[n];
			for (int i = 0; i < n; i++) {
				num[i] = sc.nextInt();
			}
			char[] color = new char[n];
			for (int i = 0; i < n; i++) {
				color[i] = sc.next().charAt(0);
			}
			for (int i = 0; i < n; i++) {
				p[i] = new Pair(num[i], color[i]);
			}
			Arrays.sort(p);
			if (check(p, 0, new boolean[9])) {
				System.out.println(1);
			} else {
				System.out.println(0);
			}
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

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.TreeSet;

public class Main {
	int n;
	TreeSet<Integer> dp;
	int[] x, y, r, c;

	void dp(int bit) {
		if (dp.contains(bit)) {
			return;
		}
		boolean[] f = new boolean[n];
		boolean[] flag = new boolean[n];
		for (int i = 0; i < n; i++) {
			if (0 < (bit & (1 << i))) {
				f[i] = true;
				flag[i] = true;
			}
		}
		for (int i = 0; i < n; i++) {
			if (!f[i])
				continue;
			for (int j = i + 1; j < n; j++) {
				if (!f[j])
					continue;
				if (isIntersect(i, j)) {
					flag[j] = false;
				}
			}
		}

		int res = 0;
		for (int i = 0; i < n; i++) {
			if (!flag[i]) {
				continue;
			}
			for (int j = i + 1; j < n; j++) {
				if (!flag[j]) {
					continue;
				}
				if (c[i] != c[j]) {
					continue;
				}
				if (isIntersect(i, j)) {
					continue;
				}

				int nbit = bit;
				nbit ^= (1 << i);
				nbit ^= (1 << j);
				dp(nbit);
			}
		}
		dp.add(bit);
	}

	boolean isIntersect(int i, int j) {
		int len = (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]);
		return len < (r[i] + r[j]) * (r[i] + r[j]);
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			x = new int[n];
			y = new int[n];
			r = new int[n];
			c = new int[n];
			for (int i = 0; i < n; i++) {
				int xi = sc.nextInt();
				int yi = sc.nextInt();
				int ri = sc.nextInt();
				int ci = sc.nextInt();
				x[i] = xi;
				y[i] = yi;
				r[i] = ri;
				c[i] = ci;
			}

			int mask = (1 << n) - 1;
			dp = new TreeSet<Integer>();
			dp(mask);
			int max = 0;
			for (Integer bit: dp) {
				max = Math.max(max, n - Integer.bitCount(bit));
			}
			System.out.println(max);
			System.gc();
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

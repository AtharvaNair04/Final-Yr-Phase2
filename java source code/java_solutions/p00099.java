import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
	int[] seg;

	void add(int k, int low, int high, int id, int v) {
		if (high <= id || id < low) {
			return;
		}
		if (low + 1 == high) {
			seg[k] += v;
			return;
		}
		int mid = (low + high) / 2;
		if (id < mid) {
			add(2 * k, low, mid, id, v);
		} else {
			add(2 * k + 1, mid, high, id, v);
		}
		seg[k] = Math.max(seg[2 * k], seg[2 * k + 1]);
	}

	int get(int k, int low, int high) {
		if (low + 1 == high) {
			return low;
		}
		int mid = (low + high) / 2;
		if (seg[2 * k] >= seg[2 * k + 1]) {
			return get(2 * k, low, mid);
		} else {
			return get(2 * k + 1, mid, high);
		}
	}

	void run() {
		MyScanner sc = new MyScanner();

		int n = sc.nextInt();
		int max = 1;
		while (max < n) {
			max *= 2;
		}
		
		seg = new int[max * 2];
		
		int q = sc.nextInt();
		for (int i = 0; i < q; i++) {
			int a = sc.nextInt() - 1;
			int v = sc.nextInt();
			add(1, 0, max, a, v);
			int id = get(1, 0, max) + 1;
			int val = seg[1];
			System.out.println(id + " " + val);
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

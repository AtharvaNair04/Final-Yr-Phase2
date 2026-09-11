import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
	int f(int[] a, int fin) {
		int n = a.length;
		int sum = 0;
		for (int i = 0; i < fin; i++) {
			int k = a[i];
			if (i != 0) {
				k += 1;
				k %= 2;
			}
			sum += k;
		}
		for (int i = n - 1; fin < i; i--) {
			int k = a[i];
			if (i != n - 1) {
				k += 1;
				k %= 2;
			}
			sum += k;
		}
		if (fin == 0 && fin == n - 1) {
			sum += a[fin];
		} else if (fin == 0 || fin == n - 1) {
			sum += (a[fin] + 1) % 2;
		} else {
			sum += a[fin];
		}
		return sum;
	}

	void run() {
		MyScanner sc = new MyScanner();

		int h = sc.nextInt();
		int w = sc.nextInt();
		int[][] a = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				a[i][j] = sc.nextInt();
			}
		}
		for (int i = 1; i < h; i++) {
			for (int j = 0; j < w; j++) {
				a[i][j] = (a[i][j] + 1) % 2;
			}
		}

		int sum = 0;
		for (int i = 0; i < h; i++) {
			int max = 0;
			for (int j = 0; j < w; j++) {
				int k = f(a[i], j);
				max = Math.max(max, k);
			}
			sum += max;
		}
		System.out.println(sum);
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

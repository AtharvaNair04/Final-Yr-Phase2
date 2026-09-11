import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
	int[] coin = { 1, 5, 10, 50, 100, 500 };
	int[] mul = { 0, 5, 2, 5, 2, 5 };
	int[] a;

	int calc(int k) {
		int[] res = new int[6];
		for (int i = 6 - 1; 0 <= i; i--) {
			res[i] = k / coin[i];
			k %= coin[i];
		}
		for (int i = 6 - 1; 0 < i; i--) {
			if (a[i] < res[i]) {
				res[i - 1] += (res[i] - a[i]) * mul[i];
				res[i] = a[i];
			}
		}
		if (a[0] < res[0]) {
			return 1 << 20;
		}
		int sum = 0;
		for (int i = 0; i < 6; i++) {
			sum += res[i];
		}
		return sum;
	}

	int opt(int k) {
		int[] res = new int[6];
		for (int i = 6 - 1; 0 <= i; i--) {
			res[i] = k / coin[i];
			k %= coin[i];
		}
		int sum = 0;
		for (int i = 0; i < 6; i++) {
			sum += res[i];
		}
		return sum;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			int n = sc.nextInt();
			a = new int[6];
			int has = 0;
			for (int i = 0; i < 6; i++) {
				a[i] = sc.nextInt();
				has += a[i] * coin[i];
			}
			if ((n | a[0] | a[1] | a[2] | a[3] | a[4] | a[5]) == 0) {
				break;
			}
			int min = 1 << 20;
			int a = 0;
			for (int i = n; i <= has; i++) {
				int sum = 0;
				sum += calc(i);
				sum += opt(i - n);
				if (sum < min) {
					min = Math.min(min, sum);
					a = i;
				}
			}
			System.out.println(min);
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

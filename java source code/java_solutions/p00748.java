import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	int[] tetra;
	int ans;
	int[] dp1, dp2;

	int dp1(int sum) {
		if (sum < 0) {
			return 200;
		}
		if (sum == 0) {
			return 0;
		}
		if (0 <= dp1[sum]) {
			return dp1[sum];
		}
		int res = 200;
		for (int i = 199; 1 <= i; i--) {
			res = Math.min(res, dp1(sum - tetra[i]) + 1);
		}
		return dp1[sum] = res;
	}

	int dp2(int sum) {
		if (sum < 0) {
			return 200;
		}
		if (sum == 0) {
			return 0;
		}
		if (0 <= dp2[sum]) {
			return dp2[sum];
		}
		int res = 200;
		for (int i = 199; 1 <= i; i--) {
			if (tetra[i] % 2 == 0) continue;
			res = Math.min(res, dp2(sum - tetra[i]) + 1);
		}
		return dp2[sum] = res;
	}

	void run() {
		MyScanner sc = new MyScanner();

		tetra = new int[200];
		dp1 = new int[2000000];
		dp2 = new int[2000000];
		for (int i = 0; i < 200; i++) {
			tetra[i] = i*(i+1)*(i+2)/6;
		}

		Arrays.fill(dp1, -1);
		Arrays.fill(dp2, -1);
		while (true) {
			int n = sc.nextInt();
			if (n == 0) {
				break;
			}
			System.out.print(dp1(n) + " ");
			System.out.println(dp2(n));
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	InputStream is;

	boolean __FILE_DEBUG_FLAG__ = false;
	String __DEBUG_FILE_NAME__ = "src/T6";

	FastScanner in;
	PrintWriter out;
	
	int lower_bound(int[] a, int key) {
		int lo = -1, hi = a.length;
		while (hi - lo > 1) {
			int mid = (hi + lo) / 2;
			if (a[mid] < key)
				lo = mid;
			else
				hi = mid;
		}
		return hi;
	}
	
	public void solve() {	
		int n = in.nextInt(), m = in.nextInt();
		int[] s = new int[n];
		int[] t = new int[n];
		for (int i = 0; i < n; i++) {
			s[i] = in.nextInt();
			t[i] = in.nextInt();
		}
		
		int[] p = in.nextIntArray(m);
		Arrays.sort(p);
		
		int[] sum = new int[m];
		for (int i = 1; i < m; i++) {
			if (i <= 2) 
				sum[i] = p[i] - p[i-1];
			else
				sum[i] = sum[i-2] + p[i] - p[i-1];
		}

		long res = 0;
		for (int i = 0; i < n; i++) {
			boolean swapped = s[i] > t[i];
			if (s[i] > t[i]) {
				int tmp = s[i]; s[i] = t[i]; t[i] = tmp; 
			}
			
			int a = lower_bound(p, s[i]);
			int b = lower_bound(p, t[i]) - 1;
			if (b == -1 || a == m || (a > b) || (p[a] <= s[i] && p[b] <= s[i]) || (p[a] >= t[i] && p[b] >= t[i])) 
				res += t[i] - s[i];
			else {
				if ((b - a) % 2 == 0) {
					long val = sum[b] - sum[a] + (p[a] - s[i]);
					res += swapped ? t[i] - s[i] - val : val;
				} else {
					long val = sum[b-1] - sum[a] + (p[a] - s[i]) + (t[i] - p[b]);
					res += val;
				}
			}
		}
		System.out.println(res);
	}

	
	public void run() {
		if (__FILE_DEBUG_FLAG__) {
			try {
				is = new FileInputStream(__DEBUG_FILE_NAME__);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("FILE_INPUT!!");
		} else {
			is = System.in;
		}
		in = new FastScanner(is);
		out = new PrintWriter(System.out);

		solve();
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

		System.out.println("----------------------------");
		System.out.println();
	}

	public void debug(Object... obj) {
		System.out.println(Arrays.deepToString(obj));
	}

	class FastScanner {
		private InputStream stream;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int numChars;

		public FastScanner(InputStream stream) {
			this.stream = stream;
			//stream = new FileInputStream(new File("dec.in"));

		}

		int read() {
			if (numChars == -1)
				throw new InputMismatchException();
			if (curChar >= numChars) {
				curChar = 0;
				try {
					numChars = stream.read(buf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (numChars <= 0)
					return -1;
			}
			return buf[curChar++];
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

		int[][] nextIntMap(int n, int m) {
			int[][] map = new int[n][m];
			for (int i = 0; i < n; i++) {
				map[i] = in.nextIntArray(m);
			}
			return map;
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

		long[][] nextLongMap(int n, int m) {
			long[][] map = new long[n][m];
			for (int i = 0; i < n; i++) {
				map[i] = in.nextLongArray(m);
			}
			return map;
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

		double[][] nextDoubleMap(int n, int m) {
			double[][] map = new double[n][m];
			for (int i = 0; i < n; i++) {
				map[i] = in.nextDoubleArray(m);
			}
			return map;
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

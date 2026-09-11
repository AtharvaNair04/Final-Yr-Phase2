import java.io.*;
import java.util.*;

public class Main {
	FastScanner in = new FastScanner(System.in);
	PrintWriter out = new PrintWriter(System.out);
	
	static int[] sq = new int[20001];
	
	public void run() {
		for (int i = 1; i * i <= 20000; i++) 
			sq[i * i] = i;
		
		int T = in.nextInt();
		outer : 
		for (int times = 0; times < T; times++) {
			int m = in.nextInt(), n = in.nextInt();
			int A = m * m + n * n;
			solve(m, n);
			for (int i = 2; i * i <= A; i++) {
				if (A % i != 0) continue;
				for (int p = 1; p * p <= i; p++) {
					int q = sq[i - p * p];
					if (p * p != i && q == 0) continue;
					
					if ((m * p + n * q) % i == 0 && (m * q - n * p) % i == 0) {
						System.out.println("C");
						continue outer;
					}
				}
			}
			System.out.println("P");
		}
		out.close();
	}
	
	static boolean solve(int M, int N) {
        int D = M * M + N * N;
        for (int i = 2; i * i <= D; ++i) {
            if (D % i != 0) continue;
            for (int p = 1; p * p <= i; ++p) {
                int q = sq[i - p * p];
                if (p * p != i && q == 0) continue;
                if ((M * p + N * q) % i == 0 && (M * q - N * p) % i == 0) {
                	return false;
                }
            }
        }
        return true;
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

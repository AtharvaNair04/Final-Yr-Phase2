import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Main {

	static long sx = 0, sy = 0, m = (long) (1e9 + 7);

	static ArrayList<Integer>[] a;
	static long[] dp;

	static boolean b = true;
	static HashMap<Long, Integer> hm = new HashMap<>();
	static ArrayList<Integer> p = new ArrayList<>();

	static ArrayList<pair> ans = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		Reader scn = new Reader();

		int h = scn.nextInt();
		int n = scn.nextInt();

		dp = new long[(int) (h + 1)];

		Arrays.fill(dp, -1);

		int[] a = new int[n];
		int[] b = new int[n];

		for (int i = 0; i < n; i++) {
			a[i] = scn.nextInt();
			b[i] = scn.nextInt();
		}

		long ans = f(a, b, h);

		System.out.println(ans);

	}

	private static long f(int[] a, int[] b, int h) {
		if (h <= 0)
			return 0;

		if (dp[h] != -1)
			return dp[h];

		long ans = (long) 1e17;

		for (int i = 0; i < a.length; i++) {

			if (h-a[i]>=0 && dp[h - a[i]] != -1)
				ans = Math.min(ans, dp[h - a[i]] + b[i]);
			else
				ans = Math.min(ans, f(a, b, h - a[i]) + b[i]);
		}

		return dp[h] = ans;
	}

	// _________________________TEMPLATE_____________________________________________________________

	// private static int gcd(int a, int b) {
	// if(a== 0)
	// return b;
	//
	// return gcd(b%a,a);
	// }

	// static class comp implements Comparator<pair> {
	//
	// @Override
	// public int compare(pair o1, pair o2) {
	//
	// return (int) (o1.y - o1.y);
	// }
	//
	// }

	private static class pair implements Comparable<pair> {
		char ch;
		int cnt;

		pair(char a) {
			ch = a;
		}

		@Override
		public int compareTo(pair o) {
			return 1;
		}

	}

	public static class Reader {
		final private int BUFFER_SIZE = 1 << 16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer, bytesRead;

		public Reader() {
			din = new DataInputStream(System.in);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public Reader(String file_name) throws IOException {
			din = new DataInputStream(new FileInputStream(file_name));
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public String readLine() throws IOException {
			byte[] buf = new byte[100000 + 1]; // line length
			int cnt = 0, c;
			while ((c = read()) != -1) {
				if (c == '\n')
					break;
				buf[cnt++] = (byte) c;
			}
			return new String(buf, 0, cnt);
		}

		public int nextInt() throws IOException {
			int ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (neg)
				return -ret;
			return ret;
		}

		public long nextLong() throws IOException {
			long ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');
			if (neg)
				return -ret;
			return ret;
		}

		public double nextDouble() throws IOException {
			double ret = 0, div = 1;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();

			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (c == '.') {
				while ((c = read()) >= '0' && c <= '9') {
					ret += (c - '0') / (div *= 10);
				}
			}

			if (neg)
				return -ret;
			return ret;
		}

		private void fillBuffer() throws IOException {
			bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			if (bytesRead == -1)
				buffer[0] = -1;
		}

		private byte read() throws IOException {
			if (bufferPointer == bytesRead)
				fillBuffer();
			return buffer[bufferPointer++];
		}

		public void close() throws IOException {
			if (din == null)
				return;
			din.close();
		}

		public int[] nextIntArray(int n) throws IOException {
			int[] arr = new int[n];
			for (int i = 0; i < n; i++) {
				arr[i] = nextInt();
			}
			return arr;
		}

		public long[] nextLongArray(int n) throws IOException {
			long[] arr = new long[n];
			for (int i = 0; i < n; i++) {
				arr[i] = nextLong();
			}
			return arr;
		}

		public int[][] nextInt2DArray(int m, int n) throws IOException {
			int[][] arr = new int[m][n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++)
					arr[i][j] = nextInt();
			}
			return arr;
		}
		// kickstart - Solution
		// atcoder - Main
	}

}

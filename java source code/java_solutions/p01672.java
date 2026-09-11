import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	PrintWriter pw;
	// Scanner sc;
	FastScanner sc;

	void run() {
		pw = new PrintWriter(System.out);
		// sc = new Scanner(System.in);
		sc = new FastScanner();
		solve();
		pw.close();
	}

	void solve() {
		int n = sc.nextInt();
		long[][] a = new long[n][n];
		long[][] b = new long[n][n];
		int[][] C = new int[n][n];
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				int v = sc.nextInt();
				C[i][j] = v;
				a[i][j] = v;
				b[n - 1 - i][n - 1 - j] = v;
			}
		}
		a = mul2D(a, b);
		long[][] cnt = new long[n][n];
		for (int i = 0; i < 2 * n; ++i) {
			for (int j = 0; j < 2 * n; ++j) {
				if (a[i][j] == 0)
					continue;
				int di = Math.abs(n - 1 - i);
				int dj = Math.abs(n - 1 - j);
				cnt[di][dj] += (int) a[i][j];
			}
		}
		double ave = 0;
		HashMap<Integer, Long> map = new HashMap<>();
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				if (cnt[i][j] == 0 || (i | j) == 0)
					continue;
				ave += Math.sqrt(i * i + j * j) * cnt[i][j];
				if (map.containsKey(i * i + j * j)) {
					map.put(i * i + j * j, map.get(i * i + j * j) + cnt[i][j]);
				} else {
					map.put(i * i + j * j, cnt[i][j]);
				}
			}
		}
		{
			long s = 0;
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < n; ++j) {
					if (C[i][j] > 1)
						s += C[i][j] * (C[i][j] - 1);
				}
			}
			if (s > 0)
				map.put(0, s);
		}

		long sz = 0;
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				sz += C[i][j];
			}
		}
		System.out.println(ave / (sz * (sz - 1)));
		long[][] ans = new long[map.size()][2];
		{
			int p = 0;
			for (Entry<Integer, Long> es : map.entrySet()) {
				ans[p][0] = es.getKey();
				ans[p][1] = es.getValue() / 2;
				++p;
			}
		}
		Arrays.sort(ans, new Comparator<long[]>() {
			@Override
			public int compare(long[] o1, long[] o2) {
				return Long.compare(o1[0], o2[0]);
			}
		});
		for (int i = 0; i < Math.min(10000, ans.length); ++i) {
			pw.println(ans[i][0] + " " + ans[i][1]);
		}
	}

	final long MODULO = 1012924417L;
	final long root = 5;

	long[][] mul2D(long[][] a, long[][] b) {
		int n1 = Integer.highestOneBit(a.length + b.length) << 1;
		int n2 = Integer.highestOneBit(a[0].length + b[0].length) << 1;
		long[][] a2 = new long[n1][n2];
		long[][] b2 = new long[n1][n2];
		for (int i = 0; i < a.length; ++i)
			for (int j = 0; j < a[i].length; ++j)
				a2[i][j] = a[i][j];
		for (int i = 0; i < b.length; ++i)
			for (int j = 0; j < b[i].length; ++j)
				b2[i][j] = b[i][j];
		a2 = fft2D(a2, false);
		b2 = fft2D(b2, false);
		for (int i = 0; i < a2.length; ++i)
			for (int j = 0; j < a2[i].length; ++j)
				a2[i][j] = a2[i][j] * b2[i][j] % MODULO;
		a2 = fft2D(a2, true);
		long invn1 = pow(n1, MODULO - 2);
		long invn2 = pow(n2, MODULO - 2);
		for (int i = 0; i < a2.length; ++i)
			for (int j = 0; j < a2[i].length; ++j)
				a2[i][j] = a2[i][j] * invn1 % MODULO * invn2 % MODULO;
		return a2;
	}

	long[][] fft2D(long[][] a, boolean inv) {
		int n = a.length;
		for (int i = 0; i < n; ++i) {
			a[i] = fft(a[i], inv);
		}
		for (int i = 0; i < n; ++i) {
			for (int j = i; j < n; ++j) {
				long d = a[i][j];
				a[i][j] = a[j][i];
				a[j][i] = d;
			}
		}
		for (int i = 0; i < n; ++i) {
			a[i] = fft(a[i], inv);
		}
		for (int i = 0; i < n; ++i) {
			for (int j = i; j < n; ++j) {
				long d = a[i][j];
				a[i][j] = a[j][i];
				a[j][i] = d;
			}
		}
		return a;
	}

	long[] mul(long[] a, long[] b) {
		int n = Integer.highestOneBit(a.length + b.length) << 1;
		a = Arrays.copyOf(a, n);
		b = Arrays.copyOf(b, n);
		a = fft(a, false);
		b = fft(b, false);
		long ninv = pow(n, MODULO - 2);
		for (int i = 0; i < n; ++i) {
			a[i] = a[i] * b[i] % MODULO;
		}
		a = fft(a, true);
		for (int i = 0; i < n; ++i)
			a[i] = a[i] * ninv % MODULO;
		return a;
	}

	long[] fft(long[] a, boolean inv) {
		int n = a.length;
		int c = 0;
		for (int i = 1; i < n; ++i) {
			for (int j = n >> 1; j > (c ^= j); j >>= 1)
				;
			if (c > i) {
				long d = a[c];
				a[c] = a[i];
				a[i] = d;
			}
		}

		for (int i = 1; i < n; i <<= 1) {
			long w = pow(root, (MODULO - 1) / (2 * i));
			if (inv)
				w = pow(w, MODULO - 2);
			for (int j = 0; j < n; j += 2 * i) {
				long wn = 1;
				for (int k = 0; k < i; ++k) {
					long u = a[k + j];
					long v = a[k + j + i] * wn % MODULO;
					a[k + j] = (u + v) % MODULO;
					a[k + j + i] = (u - v + MODULO) % MODULO;
					wn = wn * w % MODULO;
				}
			}
		}
		return a;
	}

	long pow(long a, long n) {
		long ret = 1;
		for (; n > 0; n >>= 1, a = a * a % MODULO) {
			if (n % 2 == 1)
				ret = ret * a % MODULO;
		}
		return ret;
	}

	class FastScanner {
		private final InputStream in = System.in;
		private final byte[] buffer = new byte[1024];
		private int ptr = 0;
		private int buflen = 0;

		private boolean hasNextByte() {
			if (ptr < buflen) {
				return true;
			} else {
				ptr = 0;
				try {
					buflen = in.read(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (buflen <= 0) {
					return false;
				}
			}
			return true;
		}

		private int readByte() {
			if (hasNextByte())
				return buffer[ptr++];
			else
				return -1;
		}

		private boolean isPrintableChar(int c) {
			return 33 <= c && c <= 126;
		}

		private void skipUnprintable() {
			while (hasNextByte() && !isPrintableChar(buffer[ptr]))
				ptr++;
		}

		public boolean hasNext() {
			skipUnprintable();
			return hasNextByte();
		}

		public String next() {
			if (!hasNext())
				throw new NoSuchElementException();
			StringBuilder sb = new StringBuilder();
			int b = readByte();
			while (isPrintableChar(b)) {
				sb.appendCodePoint(b);
				b = readByte();
			}
			return sb.toString();
		}

		public long nextLong() {
			if (!hasNext())
				throw new NoSuchElementException();
			long n = 0;
			boolean minus = false;
			int b = readByte();
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			if (b < '0' || '9' < b) {
				throw new NumberFormatException();
			}
			while (true) {
				if ('0' <= b && b <= '9') {
					n *= 10;
					n += b - '0';
				} else if (b == -1 || !isPrintableChar(b)) {
					return minus ? -n : n;
				} else {
					throw new NumberFormatException();
				}
				b = readByte();
			}
		}

		int nextInt() {
			return (int) nextLong();
		}
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

}

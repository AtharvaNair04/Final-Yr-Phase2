import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	@SuppressWarnings("unchecked")
	void run() {
		Scanner sc = new Scanner();

		int N = sc.nextInt();
		int M = sc.nextInt();//合計M個以下選ぶ
		int C = sc.nextInt();
		int[] l = new int[C];//l[i]=色iの選べる数
		int[] c = new int[N];//色
		int[] w = new int[N];//価値
		for (int i = 0; i < C; ++i) {
			l[i] = sc.nextInt();
		}
		PriorityQueue<Long> pq = new PriorityQueue();
		for (int i = 0; i < N; ++i) {
			c[i] = sc.nextInt();
			w[i] = sc.nextInt();
			--c[i];
			pq.add(-((long) w[i] << 32 | c[i]));
		}
		long ans = 0;
		while (M > 0 && !pq.isEmpty()) {
			long ball = pq.poll();
			int color = (int) -ball;
			int value = (int) ((-ball) >> 32);
			if (l[color] > 0) {
				--l[color];
				ans += value;
				--M;
			}
		}

		PrintWriter pw = new PrintWriter(System.out);
		pw.println(ans);
		pw.close();
	}

	class Scanner {
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

		public boolean hasNext() {
			while (hasNextByte() && !isPrintableChar(buffer[ptr]))
				ptr++;
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

		public int nextInt() {
			long nl = nextLong();
			if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE)
				throw new NumberFormatException();
			return (int) nl;
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

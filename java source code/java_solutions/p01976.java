import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
		long[] A = new long[N];
		for (int i = 0; i < N; ++i)
			A[i] = sc.nextInt();
		PriorityQueue<Long> pq1 = new PriorityQueue();
		PriorityQueue<Long> pq2 = new PriorityQueue();
		ArrayList<Integer> ans = new ArrayList();
		for (int i = 0; i < N; ++i) {
			pq1.add(A[i]);
			pq2.add(A[N - 1 - i]);
			while (!pq1.isEmpty() && pq1.peek().equals(pq2.peek())) {
				pq1.poll();
				pq2.poll();
			}
			if (pq1.isEmpty()) {
				ans.add(i + 1);
			}
		}
		PrintWriter pw = new PrintWriter(System.out);
		for (int i = 0; i < ans.size(); ++i) {
			pw.print(ans.get(i) + (i == ans.size() - 1 ? "\n" : " "));
		}
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Main {

	public static void main(String[] args) throws IOException {
		new Main().solve();
	}

	private void solve() throws IOException {
		try {
			solveA();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}

	private void solveA() {
		int v = nextInt();
		int e = nextInt();

		//		final long INF = (2000000000);
		final long INF = (Integer.MAX_VALUE);

		//		int[][] wk = Stream.generate(() -> new int[] { nextInt(), nextInt(), nextInt() }).limit(e - 1)
		//				.toArray(int[][]::new);

		long[][] graph = new long[v][v];

		for (int i = 0; i < graph.length; i++) {
			Arrays.fill(graph[i], INF);
			graph[i][i] = 0;
		}
		for (int i = 0; i < e; i++) {
			int from = nextInt();
			int to = nextInt();
			graph[from][to] = nextInt();
		}

		for (int i = 0; i < v; i++) {
			for (int j = 0; j < v; j++) {
				if (graph[j][i] == INF) {
					continue;
				}
				for (int k = 0; k < v; k++) {
					if (graph[i][k] == INF) {
						continue;
					}
					graph[j][k] = Long.min(graph[j][k], graph[j][i] + graph[i][k]);
				}
			}
		}
		for (int i = 0; i < graph.length; i++) {
			if (graph[i][i] < 0) {
				out.println("NEGATIVE CYCLE");
				return;
			}
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[i].length; j++) {
				if (builder.length() > 0) {
					builder.append(" ");
				}
				if (graph[i][j] == INF) {
					builder.append("INF");
				} else {
					builder.append(graph[i][j]);
				}
			}
			out.println(builder.toString());
			builder.setLength(0);
		}
	}

	private final PrintWriter out = new PrintWriter(System.out);
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

	private static boolean isPrintableChar(int c) {
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

	public int nextInt() {
		return Integer.parseInt(next());
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
}

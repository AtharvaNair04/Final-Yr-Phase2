import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Main implements Runnable {
	public static void main(String[] args) {
		new Thread(null, new Main(), "", Runtime.getRuntime().maxMemory()).start();
	}

	class Edge {
		int src;
		int dst;
		long cost;

		public Edge(int src_, int dst_, long cost_) {
			src = src_;
			dst = dst_;
			cost = cost_;
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {
		Scanner sc = new Scanner();
		int N = sc.nextInt();
		int M = sc.nextInt();
		int s = sc.nextInt();
		int t = sc.nextInt();
		--s;
		--t;
		ArrayList<Edge>[] g = new ArrayList[N + M];
		for (int i = 0; i < N + M; ++i)
			g[i] = new ArrayList();
		for (int i = 0; i < M; ++i) {
			int a = sc.nextInt();
			int b = sc.nextInt();
			int c = sc.nextInt();
			long d = sc.nextLong();
			--a;
			--b;
			--c;
			int middle = N + i;
			g[a].add(new Edge(a, middle, d));
			g[middle].add(new Edge(middle, a, d));
			g[b].add(new Edge(b, middle, d));
			g[middle].add(new Edge(middle, b, d));
			g[c].add(new Edge(c, middle, d));
			g[middle].add(new Edge(middle, c, d));
		}

		long[] dist = new long[N + M];
		Arrays.fill(dist, Long.MAX_VALUE / 3);
		dist[s] = 0;
		class State implements Comparable<State> {
			int v;
			long dist;

			public State(int v_, long dist_) {
				v = v_;
				dist = dist_;
			}

			public int compareTo(State o) {
				return Long.compare(dist, o.dist);
			};
		}
		PriorityQueue<State> pq = new PriorityQueue<>();
		pq.add(new State(s, 0));
		while (!pq.isEmpty()) {
			State state = pq.poll();
			if (dist[state.v] < state.dist)
				continue;
			for (Edge e : g[state.v]) {
				if (dist[e.dst] > dist[state.v] + e.cost) {
					dist[e.dst] = dist[state.v] + e.cost;
					pq.add(new State(e.dst, dist[e.dst]));
				}
			}
		}
		PrintWriter pw = new PrintWriter(System.out);
		pw.println(dist[t] / 2);
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

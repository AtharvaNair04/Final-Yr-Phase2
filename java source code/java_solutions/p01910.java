import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.PriorityQueue;

public class Main {
	InputStream is;

	int __t__ = 1;
	int __f__ = 0;
	int __FILE_DEBUG_FLAG__ = __f__;
	String __DEBUG_FILE_NAME__ = "src/H1";

	FastScanner in;
	PrintWriter out;
	

	/* -----------Dijkstra---------- */
	public class Edge {
		int to;
		int c;

		Edge(int to, int c) {
			this.to = to;
			this.c = c;
		}
	}

	public class Dijkstra {
		protected final static int INF = 1_000_000_000;
				
		class State implements Comparable<State> {
			int n;
			
			State(int n) {
				this.n = n;
			}

			public int compareTo(State s) {
				long c1 = res.minCost[n], c2 = res.minCost[s.n];
				if (c1 < c2) return -1;
				else if (c1 > c2) return 1;
				else return 0;
			}
		}
		
		DijkstraResult res;
		
		Dijkstra() {}
		
		/**
		 * This function is for Fixed Graph.
		 * The second Variable "edge" should be fixed graph which is prepared by called method.
		 * @param start root node.
		 * @param edge fixed edges.
		 * @return dijkstra result.
		 */
		DijkstraResult doit(int start, int[][] edge) {
			int n = edge.length;
			res = new DijkstraResult(n);
			Arrays.fill(res.minCost, Dijkstra.INF);
			
			PriorityQueue<State> pq = new PriorityQueue<State>();
			pq.add(new State(start));
			res.minCost[start] = 0;
			res.path[start] = start;
			
			while (!pq.isEmpty()) {
				State s = pq.poll();
				for (int i = 0; i < n; i++) {
					if (res.minCost[i] > res.minCost[s.n] + edge[s.n][i]) {
						res.minCost[i] = res.minCost[s.n] + edge[s.n][i];
						res.path[i] = s.n;
						pq.add(new State(i));
					}
				}
			}
			
			return res;
		}
		
		/**
		 * return the result of Dijkstra.
		 * This function is for Sparse Graph.
		 * The second Variable "edge" should be sparse graph which is prepared by called method.
		 * @param start root node
		 * @edge sparse graph.
		 * @return dijkstra result
		 */
		DijkstraResult doit(int start, ArrayList<Edge>[] edge) {
			int n = edge.length;
			res = new DijkstraResult(n);
			Arrays.fill(res.minCost, Dijkstra.INF);
			
			PriorityQueue<State> pq = new PriorityQueue<State>();
			pq.add(new State(start));
			res.minCost[start] = 0;
			res.path[start] = start;
			
			while (!pq.isEmpty()) {
				State s = pq.poll();
				for (Edge e : edge[s.n]) {
					if (res.minCost[e.to] > res.minCost[s.n] + e.c) {
						res.minCost[e.to] = res.minCost[s.n] + e.c;
						res.path[e.to] = s.n;
						pq.add(new State(e.to));
					}
				}
			}
			
			return res;
		}
	}

	/**
	 * it contains minCost and path from start node to each nodes.
	 * @author hiro116s
	 *
	 */
	class DijkstraResult {
		long[] minCost;
		int[] path;
		
		DijkstraResult(int n) {
			minCost = new long[n];
			path = new int[n];
		}
	}
	/*-------------end--------------*/

	ArrayList<Edge>[] g;
	@SuppressWarnings("unchecked")
	public void solve() {
		int N = in.nextInt(), M = in.nextInt(), R = in.nextInt(), Q = in.nextInt();
		g = new ArrayList[N+R];
		for (int i = 0; i < N + R; i++)
			g[i] = new ArrayList<Edge>();
		
		for (int i = 0; i < N; i++) {
			g[N+i%R].add(new Edge(i, 0));
		}
		for (int i = 0; i < M; i++) {
			int a = in.nextInt() - 1, b = in.nextInt() - 1, c = in.nextInt();
			g[b].add(new Edge(a, c));
		}
		
		long[][] costs = new long[R][];
		for (int i = 0; i < R; i++) {
			costs[i] = new Dijkstra().doit(N+i, g).minCost;
		}
		
		long res = 0;
		for (int q = 0; q < Q; q++) {
			int x = in.nextInt() - 1, y = in.nextInt() - 1, z = in.nextInt();
			long c = Long.MAX_VALUE;
			for (int i = 0; i < R; i++) {
				c = Math.min(c, costs[i][x] + costs[i][y]);
			}
			if (c <= z)
				res += z - c;
		}
		System.out.println(res);
	}
	
	public void run() {
		if (__FILE_DEBUG_FLAG__ == __t__) {
			try {
				is = new FileInputStream(__DEBUG_FILE_NAME__);
			} catch (FileNotFoundException e) {
				// TODO ????????????????????? catch ????????????
				e.printStackTrace();
			}
			System.out.println("FILE_INPUT!");
		} else {
			is = System.in;
		}
		in = new FastScanner(is);
		out = new PrintWriter(System.out);

		Thread t = new Thread(null, new Runnable() {
			@Override
			public void run() {
				solve();
			}
		}, "lul", 1 << 30);
		t.start();
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

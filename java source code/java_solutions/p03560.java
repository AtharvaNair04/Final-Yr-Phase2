import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractCollection;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		FastScanner in = new FastScanner(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		TaskD solver = new TaskD();
		solver.solve(1, in, out);
		out.close();
	}

	static class TaskD {
		public void solve(int testNumber, FastScanner in, PrintWriter out) {
			int k = in.nextInt();
			final int N = (int) 1e5 + 1;
			Edge[][] edges = new Edge[10][N];
			for (int i = 0; i < N; i++) {
				for (int digit = 0; digit < 10; digit++) {
					int v = i + k * digit;
					edges[digit][i] = new Edge(v / 10, v % 10);
				}
			}
			PriorityQueue<Vertex> pq = new PriorityQueue<>();
			int[] dist = new int[N];
			Arrays.fill(dist, Integer.MAX_VALUE / 3);
			dist[k] = 0;
			pq.offer(new Vertex(k, dist[k]));
			while (!pq.isEmpty()) {
				Vertex v = pq.poll();
				if (v.d != dist[v.id]) {
					continue;
				}
				for (int digit = 0; digit < 10; digit++) {
					Edge e = edges[digit][v.id];
					int u = e.dst;
					if (dist[u] > dist[v.id] + e.len) {
						dist[u] = dist[v.id] + e.len;
						pq.offer(new Vertex(u, dist[u]));
					}
				}
			}
			int ans = Integer.MAX_VALUE;
			for (int i = 0; i < N; i++) {
				ans = Math.min(ans, dist[i] + sumDigits(i));
			}
			out.println(ans);
		}

		private int sumDigits(int n) {
			int res = 0;
			while (n > 0) {
				res += n % 10;
				n /= 10;
			}
			return res;
		}

		class Vertex implements Comparable<Vertex> {
			int id;
			int d;

			Vertex(int id, int d) {
				this.id = id;
				this.d = d;
			}

			public int compareTo(Vertex o) {
				return d - o.d;
			}

		}

		class Edge {
			int dst;
			int len;

			Edge(int dst, int len) {
				this.dst = dst;
				this.len = len;
			}

		}

	}

	static class FastScanner {
		private BufferedReader in;
		private StringTokenizer st;

		public FastScanner(InputStream stream) {
			in = new BufferedReader(new InputStreamReader(stream));
		}

		public String next() {
			while (st == null || !st.hasMoreTokens()) {
				try {
					String rl = in.readLine();
					if (rl == null) {
						return null;
					}
					st = new StringTokenizer(rl);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

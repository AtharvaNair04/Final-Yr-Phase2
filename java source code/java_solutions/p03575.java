import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		Task task = new Task();
		task.solve();
	}

	public static class Task {
		static final long MOD = (long) 1e9 + 7;
		static int[][] adjMat;
		static int res, n, m;
		static int[] visited;
		static int[] time;
		static Set<Edge> set;
		static int timer;

		public void solve() {

			Scan sc = new Scan();
			n = (int) sc.scanLong();
			m = (int) sc.scanLong();
			adjMat = new int[n][n];
			visited = new int[n];
			time = new int[n];
			Arrays.fill(time, Integer.MAX_VALUE);
			timer = 0;
			set = new HashSet<Edge>();
			for (int i = 0; i < m; ++i) {
				int a = (int) sc.scanLong();
				int b = (int) sc.scanLong();
				adjMat[a - 1][b - 1] = 1;
				adjMat[b - 1][a - 1] = 1;
			}
			dfs(0, -1);
			System.out.println(res);
		}

		private int dfs(int source, int parent) {
			timer++;
			time[source] = Math.min(timer, time[source]);
			// System.out.println("Src : " + (source + 1) + " : Tgt : " + (parent + 1));
			if (visited[source] == 0) {
				visited[source] = 1;
			} else {
				return Math.min(time[parent],time[source]);
			}
			boolean flag = false;
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < n; ++i) {
				int is_adj = adjMat[source][i];
				if (i == parent)
					continue;
				//if ((time[i] == Integer.MAX_VALUE || time[i] < time[source])) {
					if (is_adj == 1) {
						flag = true;
						int got = dfs(i, source);
						if (got < min)
							min = got;
						if (got > time[source])
							++res;
				//	}
				}
			}
			if (!flag)
				return time[source];
			else
				return min;
		}

		class Edge {
			int from, to;

			Edge(int from, int to) {
				this.from = from;
				this.to = to;
			}

			@Override
			public int hashCode() {
				if (this.from < this.to)
					return Objects.hash(this.from, this.to);
				else
					return Objects.hash(this.to, this.from);
			}

			@Override
			public boolean equals(Object obj) {
				if (obj == null)
					return false;
				if (!(obj instanceof Edge))
					return false;
				if (obj == this)
					return true;
				return (this.from == ((Edge) obj).from && this.to == ((Edge) obj).to)
						|| (this.from == ((Edge) obj).to && this.from == ((Edge) obj).from);
			}
		}
	}

	public static class Scan {
		private byte[] buf = new byte[1024];
		private int index;
		private InputStream in;
		private int total;

		public Scan() {
			in = System.in;
		}

		public int scan() {
			if (total < 0)
				throw new InputMismatchException();
			if (index >= total) {
				index = 0;
				try {
					total = in.read(buf);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (total <= 0)
					return -1;
			}
			return buf[index++];
		}

		public long scanLong() {
			long integer = 0;
			int n = scan();
			while (isWhiteSpace(n))
				n = scan();
			int neg = 1;
			if (n == '-') {
				neg = -1;
				n = scan();
			}
			while (!isWhiteSpace(n)) {
				if (n >= '0' && n <= '9') {
					integer *= 10;
					integer += n - '0';
					n = scan();
				} else
					throw new InputMismatchException();
			}
			return neg * integer;
		}

		public double scanDouble() throws IOException {
			double doub = 0;
			int n = scan();
			while (isWhiteSpace(n))
				n = scan();
			int neg = 1;
			if (n == '-') {
				neg = -1;
				n = scan();
			}
			while (!isWhiteSpace(n) && n != '.') {
				if (n >= '0' && n <= '9') {
					doub *= 10;
					doub += n - '0';
					n = scan();
				} else
					throw new InputMismatchException();
			}
			if (n == '.') {
				n = scan();
				double temp = 1;
				while (!isWhiteSpace(n)) {
					if (n >= '0' && n <= '9') {
						temp /= 10;
						doub += (n - '0') * temp;
						n = scan();
					} else
						throw new InputMismatchException();
				}
			}
			return doub * neg;
		}

		public String scanString() {
			StringBuilder sb = new StringBuilder();
			int n = scan();
			while (isWhiteSpace(n))
				n = scan();
			while (!isWhiteSpace(n)) {
				sb.append((char) n);
				n = scan();
			}
			return sb.toString();
		}

		public char scanChar() {
			int n = scan();
			while (isWhiteSpace(n))
				n = scan();
			return (char) n;
		}

		private boolean isWhiteSpace(int n) {
			if (n == ' ' || n == '\n' || n == '\r' || n == '\t' || n == -1)
				return true;
			return false;
		}
	}
}

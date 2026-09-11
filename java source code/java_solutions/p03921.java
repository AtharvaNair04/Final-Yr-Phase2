import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		TaskX solver = new TaskX();
		solver.solve(1, in, out);
		out.close();
	}

	static int INF = 1 << 30;
	static int modP = 1000000007;

	static class TaskX {
		public void solve(int testNumber, InputReader in, PrintWriter out) {

			int n = in.nextInt();
			int m = in.nextInt();
			UnionFind uf = new UnionFind(n + m);

			for (int i = 0; i < n; i++) {
				int k = in.nextInt();
				for (int j = 0; j < k; j++) {
					int l = in.nextInt() - 1;
					uf.link(i,n + l);
				}
			}

			Set<Integer> set = new HashSet<>();
			for (int i = 0; i < n; i++) {
				set.add(uf.root(i));
			}

			out.println(set.size() > 1 ? "NO" : "YES");

		}
	}

	public static class UnionFind {
		int[] data;

		public UnionFind(int n) {
			data = new int[n];
			Arrays.fill(data, -1);
		}

		boolean link(int x, int y) {
			x = root(x);
			y = root(y);
			if (x != y) {
				if (data[y] < data[x]) {
					data[y] += data[x];
					data[x] = y;
				} else {
					data[x] += data[y];
					data[y] = x;
				}
			}
			return x != y;
		}

		int root(int x) {
			return data[x] < 0 ? x : (data[x] = root(data[x]));
		}
	}

	static class InputReader {
		BufferedReader in;
		StringTokenizer tok;

		public String nextString() {
			while (!tok.hasMoreTokens()) {
				try {
					tok = new StringTokenizer(in.readLine(), " ");
				} catch (IOException e) {
					throw new InputMismatchException();
				}
			}
			return tok.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(nextString());
		}

		public long nextLong() {
			return Long.parseLong(nextString());
		}

		public double nextDouble() {
			return Double.parseDouble(nextString());
		}

		public int[] nextIntArray(int n) {
			int[] res = new int[n];
			for (int i = 0; i < n; i++) {
				res[i] = nextInt();
			}
			return res;
		}

		public long[] nextLongArray(int n) {
			long[] res = new long[n];
			for (int i = 0; i < n; i++) {
				res[i] = nextLong();
			}
			return res;
		}

		public InputReader(InputStream inputStream) {
			in = new BufferedReader(new InputStreamReader(inputStream));
			tok = new StringTokenizer("");
		}

	}

}

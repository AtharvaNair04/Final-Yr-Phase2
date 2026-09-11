import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";

	class WeightedUnionFind {
		int[] par;
		long[] wd;

		public WeightedUnionFind(int n) {
			par = new int[n];
			wd = new long[n];
			Arrays.fill(par, -1);
		}

		int find(int x) {
			if (par[x] < 0)
				return x;
			else {
				final int parent = find(par[x]);
				wd[x] += wd[par[x]];
				par[x] = parent;
				return parent;
			}
		}

		boolean same(int x, int y) {
			return find(x) == find(y);
		}

		long weight(int x) {
			find(x);
			return wd[x];
		}

		// y - x = w ??? y = x + w;
		boolean union(int x, int y, long w) {
			w += (weight(x) - weight(y));
			x = find(x);
			y = find(y);
			if (x == y)
				return false;
			else {
				if (par[y] < par[x]) {
					int tmp = x;
					x = y;
					y = tmp;
					w = -w;
				}
				par[x] += par[y];
				par[y] = x;
				wd[y] = w;
				return true;
			}
		}

		// return x-y
		long diff(int x, int y) {
			if (!same(x, y)) {
				throw new AssertionError();
			} else {
				return this.weight(x) - this.weight(y);
			}
		}
	}

	void solver() {
		while (true) {
			int N = ni();
			int M = ni();
			if (N == 0 && M == 0)
				break;
			WeightedUnionFind uf = new WeightedUnionFind(N);
			for (int i = 0; i < M; i++) {
				String s = ns();
				int x = ni() - 1;
				int y = ni() - 1;
				if (s.equals("!")) {
					long w = nl();
					uf.union(x, y, w);
				} else {
					if (!uf.same(x, y)) {
						out.println("UNKNOWN");
					} else {
						out.println(-uf.diff(x, y));
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);

		new Main().solver();
		out.flush();
	}

	static long nl() {
		try {
			long num = 0;
			boolean minus = false;
			while ((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'))
				;
			if (num == '-') {
				num = 0;
				minus = true;
			} else {
				num -= '0';
			}

			while (true) {
				int b = is.read();
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}

	static char nc() {
		try {
			int b = skip();
			if (b == -1)
				return 0;
			return (char) b;
		} catch (IOException e) {
		}
		return 0;
	}

	static double nd() {
		try {
			return Double.parseDouble(ns());
		} catch (Exception e) {
		}
		return 0;
	}

	static String ns() {
		try {
			int b = skip();
			StringBuilder sb = new StringBuilder();
			if (b == -1)
				return "";
			sb.append((char) b);
			while (true) {
				b = is.read();
				if (b == -1)
					return sb.toString();
				if (b <= ' ')
					return sb.toString();
				sb.append((char) b);
			}
		} catch (IOException e) {
		}
		return "";
	}

	public static char[] ns(int n) {
		char[] buf = new char[n];
		try {
			int b = skip(), p = 0;
			if (b == -1)
				return null;
			buf[p++] = (char) b;
			while (p < n) {
				b = is.read();
				if (b == -1 || b <= ' ')
					break;
				buf[p++] = (char) b;
			}
			return Arrays.copyOf(buf, p);
		} catch (IOException e) {
		}
		return null;
	}

	public static byte[] nse(int n) {
		byte[] buf = new byte[n];
		try {
			int b = skip();
			if (b == -1)
				return null;
			is.read(buf);
			return buf;
		} catch (IOException e) {
		}
		return null;
	}

	static int skip() throws IOException {
		int b;
		while ((b = is.read()) != -1 && !(b >= 33 && b <= 126))
			;
		return b;
	}

	static boolean eof() {
		try {
			is.mark(1000);
			int b = skip();
			is.reset();
			return b == -1;
		} catch (IOException e) {
			return true;
		}
	}

	static int ni() {
		try {
			int num = 0;
			boolean minus = false;
			while ((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'))
				;
			if (num == '-') {
				num = 0;
				minus = true;
			} else {
				num -= '0';
			}

			while (true) {
				int b = is.read();
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}
}

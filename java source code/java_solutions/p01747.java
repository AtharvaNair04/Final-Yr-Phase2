import java.awt.geom.Line2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";

	public static void main(String[] args) throws Exception {
		new Main().run();
	}

	void solver() {
		int n = ni();
		double[] x = new double[n];
		double[] y = new double[n];
		for (int i = 0; i < n; i++) {
			x[i] = nd();
			y[i] = nd();
		}
		boolean f = (checker(x, y, n)) && (checker(reverse(x), reverse(y), n));
		out.println(f ? "Possible" : "Impossible");
	}

	double[] reverse(double[] a) {
		double[] ret = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			ret[i] = a[a.length - 1 - i];
		}
		return ret;
	}

	double eps = 1e-6;

	boolean checker(double[] x, double[] y, int n) {
		boolean f = false;
		int[] ord = convexHull(x, y);
		for (int i = 0; i < ord.length; i++) {
			if (ord[i] == 0) {
				f = true;
				break;
			}
		}
		for (int i = 1; i < n; i++) {
			double INF = 999999;
			double r = -INF, l = -INF;
			ArrayList<Integer> lisr = new ArrayList<>();
			ArrayList<Integer> lisl = new ArrayList<>();
			int rv = -1, lv = -1;
			for (int j = i + 1; j < n; j++) {
				double cos = cos(new double[] { x[i - 1] - x[i], y[i - 1] - y[i] },
						new double[] { x[j] - x[i], y[j] - y[i] });
				int t = Line2D.relativeCCW(x[i], y[i], x[i - 1], y[i - 1], x[j], y[j]);
				if (t == -1) {
					if (Math.abs(r - cos) < eps) {
						lisr.add(j);
					} else if (r + eps < cos) {
						r = cos;
						rv = j;
						lisr.clear();
						lisr.add(j);
					}
				} else if (t == 1) {
					if (Math.abs(l - cos) < eps) {
						lisl.add(j);
					} else if (l + eps < cos) {
						l = cos;
						lv = j;
						lisl.clear();
						lisl.add(j);
					}
				}
			}
			if (rv != -1 && lv != -1) {
				for (int v : lisr) {
					for (int u : lisl) {
						if (Line2D.linesIntersect(x[v], y[v], x[u], y[u], x[i], y[i], x[i - 1], y[i - 1])) {
							f &= false;
						}
					}
				}
			}
		}
		return f;
	}

	double cos(double[] v, double[] u) {
		double dot = 0;
		for (int i = 0; i < v.length; i++)
			dot = v[i] * u[i];
		double d1 = 0, d2 = 0;
		for (int i = 0; i < v.length; i++) {
			d1 += v[i] * v[i];
			d2 += u[i] * u[i];
		}
		return dot / (Math.sqrt(d1) * Math.sqrt(d2));
	}

	int[] convexHull(final double[] x, final double[] y) {
		int n = x.length;
		Integer[] ord = new Integer[n];
		for (int i = 0; i < n; i++)
			ord[i] = i;
		Arrays.sort(ord, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (x[o1] != x[o2])
					return Double.compare(x[o1], x[o2]);
				if (y[o1] != y[o2]) {
					return Double.compare(y[o1], y[o2]);
				}
				return 0;
			}
		});

		int[] ret = new int[n + 1];
		int p = 0;
		for (int i = 0; i < n; i++) {
			if (p >= 1 && x[ret[p - 1]] == x[ord[i]] && y[ret[p - 1]] == y[ord[i]])
				continue;
			while (p >= 2 && Line2D.relativeCCW(x[ret[p - 2]], y[ret[p - 2]], x[ret[p - 1]], y[ret[p - 1]], x[ord[i]],
					y[ord[i]]) == 1) {
				p--;
			}
			ret[p++] = ord[i];
		}

		int inf = p + 1;
		for (int i = n - 2; i >= 0; i--) {
			if (x[ret[p - 1]] == x[ord[i]] && y[ret[p - 1]] == y[ord[i]])
				continue;
			while (p >= inf && Line2D.relativeCCW(x[ret[p - 2]], y[ret[p - 2]], x[ret[p - 1]], y[ret[p - 1]], x[ord[i]],
					y[ord[i]]) == 1)
				p--;
			ret[p++] = ord[i];
		}
		return Arrays.copyOf(ret, p - 1);
	}

	void run() {
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		solver();
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

	static void tr(Object... o) {
		System.out.println(Arrays.deepToString(o));
	}
}

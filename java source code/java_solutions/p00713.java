import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
	double EPS = 1e-12;
	double INF = 1e12;

	class Point {
		double x, y;

		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	class Segment {
		Point a, b;

		Segment(Point a, Point b) {
			this.a = a;
			this.b = b;
		}
	}

	class Line {
		Point a, b;

		Line(Point a, Point b) {
			this.a = a;
			this.b = b;
		}
	}

	class Circle {
		Point c;
		double r;

		Circle(Point c, double r) {
			this.c = c;
			this.r = r;
		}
	}

	double norm(Point a) {
		return Math.sqrt(a.x * a.x + a.y * a.y);
	}

	Point sub(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	double dot(Point a, Point b) {
		return a.x * b.x + a.y * b.y;
	}

	double cross(Point a, Point b) {
		return a.x * b.y - a.y * b.x;
	}

	double abs(double a) {
		return 0 < a ? a : -a;
	}

	// 交点の個数が0または無限の時はreturn null;
	Point[] crossPoint(Circle c1, Circle c2) {
		double d = norm(sub(c1.c, c2.c));
		if (d < EPS && abs(c1.r - c2.r) < EPS) {
			return null;
		} else if (c1.r + c2.r < d - EPS || d + EPS < abs(c1.r - c2.r)) {
			return null;
		} else {
			double a = (c1.r * c1.r - c2.r * c2.r + d * d) / (2 * d);
			double h = Math.sqrt(Math.max(c1.r * c1.r - a * a, 0));
			Point p1 = new Point(c1.c.x + a / d * (c2.c.x - c1.c.x), c1.c.y + a / d * (c2.c.y - c1.c.y));
			Point p2 = new Point(h / d * (c2.c.x - c1.c.x), h / d * (c2.c.y - c1.c.y));
			if (norm(p2) < EPS) {
				return new Point[] { p1 };
			} else {
				return new Point[] { new Point(p1.x - p2.y, p1.y + p2.x), new Point(p1.x + p2.y, p1.y - p2.x) };
			}
		}
	}

	boolean isCover(Circle c, Point p) {
		return norm(sub(c.c, p)) <= c.r + EPS;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			int n = sc.nextInt();
			if (n == 0) {
				break;
			}
			Point[] p = new Point[n];
			Circle[] c = new Circle[n];
			for (int i = 0; i < n; i++) {
				p[i] = new Point(sc.nextDouble(), sc.nextDouble());
				c[i] = new Circle(p[i], 1.);
			}

			int max = 1;
			for (int i = 0; i < n; i++) {
				for (int j = i + 1; j < n; j++) {
					if (2 <= norm(sub(c[i].c, c[j].c))) {
						continue;
					}
					Point[] cp = crossPoint(c[i], c[j]);
					if (cp == null) {
						continue;
					}
					for (int k = 0; k < cp.length; k++) {
						Circle nc = new Circle(cp[k], 1.);
						int cnt = 0;
						for (int l = 0; l < n; l++) {
							if (isCover(nc, p[l])) {
								cnt++;
							}
						}
						max = Math.max(max, cnt);
					}
				}
			}
			System.out.println(max);
		}
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
		System.out.println("----------------------------" + '\n');
	}

	class MyScanner {
		int read() {
			try {
				return System.in.read();
			} catch (IOException e) {
				throw new InputMismatchException();
			}
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

		long nextLong() {
			return Long.parseLong(next());
		}

		long[] nextLongArray(int n) {
			long[] array = new long[n];
			for (int i = 0; i < n; i++)
				array[i] = nextLong();
			return array;
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

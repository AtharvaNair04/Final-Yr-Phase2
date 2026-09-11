import java.util.*;

class Main {

	static class Line {
		P src;
		P dir;

		public Line(P src, P dir) {
			this.src = src;
			this.dir = dir;
		}

	}

	static class P {
		double x, y;

		public P(double x, double y) {
			this.x = x;
			this.y = y;
		}

		P subtruct(P p) {
			return new P(x - p.x, y - p.y);
		}

		P add(P p) {
			return new P(x + p.x, y + p.y);
		}

		void show() {
			System.out.println(x + " " + y);
		}
	}

	static P ori = new P(0, 0);
	static ArrayList<P> poly = new ArrayList<>();
	static ArrayList<P> vs = new ArrayList<>();
	static ArrayList<P>[] vor;
	static int M, N;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		M = sc.nextInt();
		N = sc.nextInt();
		vor = new ArrayList[N];
		for (int i = 0; i < N; ++i) {
			vor[i] = new ArrayList<>();
		}
		for (int i = 0; i < M; ++i) {
			poly.add(new P(sc.nextDouble(), sc.nextDouble()));
		}
		for (int i = 0; i < N; ++i) {
			vs.add(new P(sc.nextDouble(), sc.nextDouble()));
		}

		double ans = 0;
		for (int i = 0; i < vs.size(); ++i) {
			ArrayList<P> lis = makeVoronoiDiagram(i);
			ans += calc(lis, vs.get(i));
		}
		System.out.println(ans / area(poly));

	}

	static double calc(ArrayList<P> poly, P center) {
		double ret = 0;
		for (int i = 0; i < poly.size(); ++i) {
			P p1 = poly.get(i).subtruct(center);
			P p2 = poly.get((i + 1) % poly.size()).subtruct(center);
			ret += 1.0 / 12 * ((p2.y - p1.y) * (p2.x * p2.x + p1.x * p1.x) * (p2.x + p1.x)
					- (p2.x - p1.x) * (p2.y * p2.y + p1.y * p1.y) * (p2.y + p1.y));
		}
		return ret;
	}

	static double area(ArrayList<P> lis) {
		double area = 0;
		for (int i = 0; i < poly.size(); ++i) {
			area += crossProduct(poly.get(i), poly.get((i + 1) % poly.size()));
		}
		area /= 2;
		return area;
	}

	static ArrayList<P> makeVoronoiDiagram(int idx) {
		ArrayList<P> ret = new ArrayList<>();
		for (P p : poly) {
			ret.add(new P(p.x, p.y));
		}
		P center = vs.get(idx);
		for (int i = 0; i < vs.size(); ++i) {
			if (i == idx)
				continue;
			P outer = vs.get(i);
			P middle = new P((center.x + outer.x) / 2, (center.y + outer.y) / 2);
			Line l = new Line(middle, rot(outer.subtruct(center), Math.PI / 2));
			for (int j = 0; j < ret.size(); ++j) {
				P p1 = ret.get(j);
				P p2 = ret.get((j + 1) % ret.size());
				if (intersect(l.src, l.src.add(l.dir), p1, p2)) {
					P crossP = crossPoint(p1, p2, l.src, l.src.add(l.dir));
					ret.add(j + 1, crossP);
				}
			}

			for (int j = 0; j < ret.size(); ++j) {
				if (intersect(l.src, l.src.add(l.dir), center, ret.get(j))) {
					ret.remove(j);
					--j;
				}
			}

		}
		return ret;
	}

	static P crossPoint(P l1, P l2, P seg1, P seg2) {
		double[][] mx = new double[2][2];
		mx[0][0] = -(l1.y - l2.y);
		mx[0][1] = l1.x - l2.x;
		mx[1][0] = -(seg1.y - seg2.y);
		mx[1][1] = seg1.x - seg2.x;
		double[][] v = { { crossProduct(l1, l2) }, { crossProduct(seg1, seg2) } };
		if (Math.abs(mx[0][0] * mx[1][1] - mx[0][1] * mx[1][0]) < 1e-6) {
			throw new AssertionError();
		}
		double[][] rev = rev(mx);
		v = mul(rev, v);
		return new P(v[0][0], v[1][0]);
	}

	static double[][] rev(double[][] mx) {
		double[][] ret = new double[2][2];
		double det = mx[0][0] * mx[1][1] - mx[0][1] * mx[1][0];
		ret[0][0] = mx[1][1] / det;
		ret[0][1] = -mx[0][1] / det;
		ret[1][0] = -mx[1][0] / det;
		ret[1][1] = mx[0][0] / det;
		return ret;
	}

	static double[][] mul(double[][] m1, double[][] m2) {
		double[][] ret = new double[m1.length][m2[0].length];
		for (int i = 0; i < m1.length; ++i) {
			for (int j = 0; j < m2[i].length; ++j) {
				for (int k = 0; k < m1[0].length; ++k) {
					ret[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return ret;
	}

	static P rot(P p, double ang) {
		double[][] v = { { p.x }, { p.y } };
		double[][] m = { { Math.cos(ang), -Math.sin(ang) }, { Math.sin(ang), Math.cos(ang) } };
		v = mul(m, v);
		return new P(v[0][0], v[1][0]);
	}

	static boolean intersect(P lineSrc, P lineDst, P segSrc, P segDst) {
		double c1 = crossProduct(lineDst.subtruct(lineSrc), segSrc.subtruct(lineSrc));
		double c2 = crossProduct(lineDst.subtruct(lineSrc), segDst.subtruct(lineSrc));
		if (Math.abs(c1) < 1e-6 || Math.abs(c2) < 1e-6)
			return false;
		if (c1 * c2 < 0)
			return true;
		else
			return false;

	}

	static double crossProduct(P p1, P p2) {
		return p1.x * p2.y - p1.y * p2.x;
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

}

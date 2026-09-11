import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			int n = sc.nextInt();
			double R = sc.nextDouble();
			if (n == 0 && R == 0)
				break;
			double[] x = new double[n];
			double[] y = new double[n];
			double[] r = new double[n];
			Circle[] cs = new Circle[n];
			for (int i = 0; i < n; ++i) {
				x[i] = sc.nextDouble();
				y[i] = sc.nextDouble();
				r[i] = sc.nextDouble();
				cs[i] = new Circle(new P(x[i], y[i]), r[i]);
			}
			solve(n, R, x, y, r, cs);
		}
	}

	void solve(int n, double R, double[] x, double[] y, double[] r, Circle[] cs) {
		int curIdx = -1;
		int preIdx = -1;
		int dirIdx = -1;
		@SuppressWarnings("unchecked")
		ArrayList<Circle>[][] enCs = new ArrayList[n][n];
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				enCs[i][j] = new ArrayList<>();
			}
		}
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				if (i == j)
					continue;
				if (cs[i].center.dist(cs[j].center) + cs[i].r + cs[j].r > 2 * R)
					continue;
				if (cs[i].center.dist(cs[j].center) + Math.min(cs[i].r, cs[j].r) <= Math.max(cs[i].r, cs[j].r))
					continue;

				ArrayList<Circle> tmp = calc(cs[i], cs[j], R);
				for (Circle c : tmp) {
					if (encircleAll(cs, c) && noDumy(cs, cs[i], c) && noDumy(cs, cs[j], c)) {
						enCs[i][j].add(c);
					}
				}
				for (int k = 0; k < enCs[i][j].size(); ++k) {
					curIdx = i;
					preIdx = j;
					dirIdx = k;
				}
			}
		}

		if (curIdx == -1 && preIdx == -1 && dirIdx == -1) {
			for (int i = 0; i < n; ++i) {
				if (encircleAll(cs, cs[i])) {
					System.out.println((2 * R - r[i]) * 2 * Math.PI);
					return;
				}
			}

			System.out.println("0.0");
			return;
		}

		double ans = 0;

		int nIdx = -1;
		int nDirIdx = -1;
		double totAng = 0;
		do {
			for (int i = 0; i < n; ++i) {
				if (i == curIdx)
					continue;
				for (int j = 0; j < enCs[i][curIdx].size(); ++j) {
					if ((nIdx == -1 && nDirIdx == -1)) {
						nIdx = i;
						nDirIdx = j;
						continue;
					}
					double candAng = enCs[curIdx][preIdx].get(dirIdx).center.sub(cs[curIdx].center)
							.ang(enCs[i][curIdx].get(j).center.sub(cs[curIdx].center));
					double curAng = enCs[curIdx][preIdx].get(dirIdx).center.sub(cs[curIdx].center)
							.ang(enCs[nIdx][curIdx].get(nDirIdx).center.sub(cs[curIdx].center));
					if (curAng < 1e-6 || (candAng > 1e-6 && candAng < curAng)) {
						nIdx = i;
						nDirIdx = j;
					} else if (Math.abs(curAng - candAng) < 1e-6) {
						double curAng2 = cs[nIdx].center.sub(cs[curIdx].center)
								.ang(enCs[nIdx][curIdx].get(nDirIdx).center.sub(cs[curIdx].center));
						double candAng2 = cs[i].center.sub(cs[curIdx].center)
								.ang(enCs[i][curIdx].get(j).center.sub(cs[curIdx].center));
						if (candAng2 < curAng2) {
							nIdx = i;
							nDirIdx = j;
						}
					}
				}
			}
			double ang = enCs[curIdx][preIdx].get(dirIdx).center.sub(cs[curIdx].center)
					.ang(enCs[nIdx][curIdx].get(nDirIdx).center.sub(cs[curIdx].center));
			ans += (2 * R - r[curIdx]) * ang;
			ans += R * (enCs[nIdx][curIdx].get(nDirIdx).center.sub(cs[curIdx].center)
					.ang(enCs[nIdx][curIdx].get(nDirIdx).center.sub(cs[nIdx].center)));
			totAng += ang + (enCs[nIdx][curIdx].get(nDirIdx).center.sub(cs[curIdx].center)
					.ang(enCs[nIdx][curIdx].get(nDirIdx).center.sub(cs[nIdx].center)));
			preIdx = curIdx;
			curIdx = nIdx;
			dirIdx = nDirIdx;
			nIdx = -1;
			nDirIdx = -1;
		} while (2 * Math.PI - totAng > 1e-6);
		System.out.println(ans);
	}

	boolean encircleAll(Circle[] cs, Circle en) {
		for (Circle c : cs) {
			if (en.center.dist(c.center) + c.r > en.r + 1e-6)
				return false;
		}
		return true;
	}

	boolean noDumy(Circle[] cs, Circle c, Circle enC) {
		ArrayList<Circle> lis = new ArrayList<>();
		for (Circle a : cs) {
			if (Math.abs(a.center.dist(enC.center) + a.r - enC.r) < 1e-6) {
				lis.add(a);
			}
		}
		Collections.sort(lis, new Comparator<Circle>() {
			@Override
			public int compare(Circle o1, Circle o2) {
				return Double.compare(o1.center.sub(enC.center).crossPrd(o2.center.sub(enC.center)), 0);
			}
		});
		ArrayList<Circle> cand = new ArrayList<>();
		cand.add(lis.get(0));
		cand.add(lis.get(lis.size() - 1));
		for (Circle a : cand) {
			if (Math.abs(a.center.x - c.center.x) + Math.abs(a.center.y - c.center.y) < 1e-6)
				return true;
		}
		return false;

	}

	class Circle {
		P center;
		double r;
		int idx = -1;

		public Circle(P center, double r) {
			this.center = center;
			this.r = r;
		}

		public Circle(P center, double r, int idx) {
			this.center = center;
			this.r = r;
			this.idx = idx;
		}
	}

	ArrayList<Circle> calc(Circle c1, Circle c2, double R) {
		Circle C1 = new Circle(c1.center, R - c1.r);
		Circle C2 = new Circle(c2.center, R - c2.r);
		ArrayList<Circle> ret = new ArrayList<>();
		double d = C1.center.dist(C2.center);
		double cos = (C1.r * C1.r + d * d - C2.r * C2.r) / (2 * d * C1.r);
		P p12 = C2.center.sub(C1.center);
		P midP = C1.center.add(p12.normalize().mul(C1.r * cos));
		double h = C1.r * Math.sqrt(1 - cos * cos);
		ret.add(new Circle(midP.add(p12.normalize().rot(Math.PI / 2).mul(h)), R));
		if (Math.abs(h) > 1e-6)
			ret.add(new Circle(midP.add(p12.normalize().rot(Math.PI / 2).mul(-h)), R));
		return ret;
	}

	class P implements Comparable<P> {
		double x, y;

		public P(double x, double y) {
			this.x = x;
			this.y = y;
		}

		double norm() {
			return Math.sqrt(x * x + y * y);
		}

		P normalize() {
			return this.mul(1 / this.norm());
		}

		P add(P p) {
			return new P(x + p.x, y + p.y);
		}

		P sub(P p) {
			return this.add(p.mul(-1));
		}

		P mul(double coe) {
			return new P(x * coe, y * coe);
		}

		double dist(P p) {
			return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
		}

		P rot(double ang) {
			double c = Math.cos(ang);
			double s = Math.sin(ang);
			return new P(c * x - s * y, s * x + c * y);
		}

		double crossPrd(P p) {
			return x * p.y - y * p.x;
		}

		double innerPrd(P o) {
			return x * o.x + y * o.y;
		}

		double ang(P o) {
			double cos = innerPrd(o) / (norm() * o.norm());
			if (Math.abs(cos) > 1 + 1e-6)
				throw new AssertionError();
			if (Math.abs(cos) > 1) {
				cos = Math.signum(cos);
			}
			return Math.acos(cos);
		}

		@Override
		public int compareTo(P o) {
			if (Math.abs(x - o.x) > 1e-6)
				return Double.compare(x, o.x);
			else {
				return Double.compare(y, o.y);
			}
		}

	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

}

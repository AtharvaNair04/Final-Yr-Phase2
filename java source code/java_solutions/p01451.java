import java.util.Arrays;
import java.util.Scanner;

class Main {
	double[][] p = new double[2][6];

	void run() {
		Scanner sc = new Scanner(System.in);
		int Na = sc.nextInt();
		int Nb = sc.nextInt();
		long[] xa = new long[Na];
		long[] ya = new long[Na];
		long[] xb = new long[Nb];
		long[] yb = new long[Nb];
		for (int i = 0; i < Na; ++i) {
			xa[i] = sc.nextLong();
			ya[i] = sc.nextLong();
		}
		for (int i = 0; i < Nb; ++i) {
			xb[i] = sc.nextLong();
			yb[i] = sc.nextLong();
		}

		double ans = Double.MAX_VALUE / 3;
		for (int t = 0; t < 2; ++t) {
			for (int i = 0; i < Na; ++i) {
				for (int j = 0; j < Na; ++j) {
					if (cross(xa[0], ya[0], xa[i], ya[i], xb[0], yb[0], xb[1], yb[1])) {
						continue;
					}
					if (cross(xa[i], ya[i], xa[j], ya[j], xb[0], yb[0], xb[1], yb[1])) {
						continue;
					}
					if (cross(xa[j], ya[j], xa[1], ya[1], xb[0], yb[0], xb[1], yb[1])) {
						continue;
					}
					ans = Math.min(ans, dist(xa[0], ya[0], xa[i], ya[i]) + dist(xa[i], ya[i], xa[j], ya[j])
							+ dist(xa[j], ya[j], xa[1], ya[1]) + dist(xb[0], yb[0], xb[1], yb[1]));
				}
			}
			Na ^= Nb;
			Nb ^= Na;
			Na ^= Nb;
			long[] tmp = new long[Na];
			tmp = Arrays.copyOf(xa, xa.length);
			xa = Arrays.copyOf(xb, xb.length);
			xb = Arrays.copyOf(tmp, tmp.length);
			tmp = Arrays.copyOf(ya, ya.length);
			ya = Arrays.copyOf(yb, yb.length);
			yb = Arrays.copyOf(tmp, tmp.length);
		}
		System.out.println(ans >= Double.MAX_VALUE / 4 ? "-1" : ans);
	}

	double dist(long xa, long ya, long xb, long yb) {
		long dx = xa - xb;
		long dy = ya - yb;
		return Math.sqrt(dx * dx + dy * dy);
	}

	boolean cross(long xa1, long ya1, long xb1, long yb1, long xa2, long ya2, long xb2, long yb2) {
		long v = f(xa1, ya1, xb1, yb1, xa2, ya2) * f(xa1, ya1, xb1, yb1, xb2, yb2);
		long u = f(xa2, ya2, xb2, yb2, xa1, ya1) * f(xa2, ya2, xb2, yb2, xb1, yb1);
		return v <= 0 && u <= 0;
	}

	long f(long xa, long ya, long xb, long yb, long x, long y) {
		return (ya - yb) * (x - xa) - (y - ya) * (xa - xb);
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	static int N, K;
	static double[][] a;

	public static void main(String[] args) {
		N = sc.nextInt();
		K = sc.nextInt();
		a = new double[N][2];
		for (int i = 0; i < N; ++i) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			int r = sc.nextInt();
			double angle = Math.atan2(y, x);
			double diff = Math.asin(r / Math.sqrt(x * x + y * y));
			if (angle - diff <= -Math.PI) {
				a[i][0] = angle - diff + 2 * Math.PI;
				a[i][1] = Math.PI;
			} else {
				a[i][0] = Math.max(angle - diff, 0);
				a[i][1] = Math.max(Math.min(angle + diff, Math.PI), 0);
			}
		}
		ArrayList<Range> ranges = new ArrayList<Range>();
		for (int i = 0; i < N; ++i) {
			boolean ok = true;
			for (int j = 0; j < N; ++j) {
				if (a[i][0] > a[j][0] && a[i][1] < a[j][1]) {
					ok = false;
					break;
				}
			}
			if (ok) ranges.add(new Range(a[i][0], a[i][1]));
		}
		ranges.add(new Range(-1, -1));
		Collections.sort(ranges);
		double[][] dp1 = new double[ranges.size()][K + 1];
		double[][] dp2 = new double[ranges.size()][K + 1];
		for (int i = 0; i < ranges.size(); ++i) {
			Arrays.fill(dp1[i], -999);
			Arrays.fill(dp2[i], -999);
			dp1[i][0] = dp2[i][0] = 0;
		}
		for (int i = 1; i < ranges.size(); ++i) {
			int prev = i;
			for (int j = i - 1; j >= 0; --j) {
				if (ranges.get(j).e < ranges.get(i).s) break;
				prev = j;
			}
			if (prev != i) {
				for (int j = 1; j <= K; ++j) {
					dp1[i][j] = Math.max(dp1[i][j], dp1[prev][j - 1] + ranges.get(i).length(ranges.get(prev).e));
				}
			}
			for (int j = 1; j <= K; ++j) {
				dp1[i][j] = Math.max(dp1[i][j], dp2[prev - 1][j - 1] + ranges.get(i).length());
			}
			for (int j = 0; j <= K; ++j) {
				dp2[i][j] = Math.max(dp2[i - 1][j], dp1[i][j]);
			}
		}
		double ans = 0;
		for (int j = 0; j <= K; ++j) {
			ans = Math.max(ans, dp2[ranges.size() - 1][j]);
		}
		System.out.printf("%.12f\n", ans / Math.PI);
	}

	static class Range implements Comparable<Range> {
		double s, e;

		public Range(double s, double e) {
			this.s = s;
			this.e = e;
		}

		double length() {
			return e - s;
		}

		double length(double prev) {
			if (prev > e) return 0;
			return e - Math.max(s, prev);
		}

		public int compareTo(Range o) {
			if (this.s < o.s - 1e-9) return -1;
			if (this.s > o.s + 1e-9) return 1;
			return this.e < o.e ? -1 : 1;
		}
	}

}

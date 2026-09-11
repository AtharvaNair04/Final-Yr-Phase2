import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		new Main().solver();
	}

	double EPS = 1e-11;

	void solver() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			double w = sc.nextDouble(), h = sc.nextDouble();
			int n = sc.nextInt();
			if (w == 0 && h == 0 && n == 0)
				break;
			n *= 2;
			double[] x = new double[n + 2];
			double[] y = new double[n + 2];
			for (int i = 0; i < n; i++) {
				x[i] = sc.nextDouble();
				y[i] = sc.nextDouble();
			}
			x[n] = w;
			y[n] = 0;
			x[n + 1] = w;
			y[n + 1] = h;
			ArrayList<Double> y1 = new ArrayList<>();
			for (int i = 0; i < n + 2; i++) {
				for (int j = i + 1; j < n + 2; j++) {
					if (x[i] != x[j]) {
						double tmp = (y[i] - y[j]) / (x[i] - x[j]) * (-x[j]) + y[j];
						if (0 < tmp && tmp < h)
							y1.add(tmp);
					}
				}
			}
			y1.add(0.0);
			y1.add(h);
			y1.sort(null);
			for (int i = 0; i < y1.size() - 1; i++) {
				while (i < y1.size() - 1 && y1.get(i) == y1.get(i + 1)) {
					y1.remove(i);
				}
			}

			double ans = 0;

			for (int i = 0; i < y1.size() - 1; i++) {
				double[] y2 = new double[n];
				if (Math.abs(y1.get(i) - y1.get(i + 1)) < EPS)
					continue;
				double y_mid = (y1.get(i) + y1.get(i + 1)) / 2;
				for (int j = 0; j < n; j++) {
					double tmp = (y[j] - y_mid) / ((x[j] + EPS) - 0) * w + y_mid;
					if (tmp < 0)
						tmp = 0;
					if (tmp > h)
						tmp = h;
					y2[j] = tmp;
				}
				Arrays.sort(y2);
				ans += (y2[n / 2] - y2[n / 2 - 1]) * (y1.get(i + 1) - y1.get(i));
				// System.out.println(ans / (h * h));
			}
			System.out.println(ans / (h * h));
		}
	}

	void tr(Object... o) {
		System.out.println(Arrays.deepToString(o));
	}
}

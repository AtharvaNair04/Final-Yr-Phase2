import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static double EPS = 1e-5;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			int N = sc.nextInt();
			if (N == 0) {
				break;
			}
			double[] x = new double[2 * N];
			double[] y = new double[2 * N];
			double[] r = new double[2 * N];
			double[] m = new double[N];
			for (int i = 0; i < N; ++i) {
				x[i] = sc.nextDouble();
				y[i] = sc.nextDouble();
				r[i] = sc.nextDouble();
				m[i] = sc.nextDouble();
				x[i + N] = x[i];
				y[i + N] = y[i];
				r[i + N] = r[i] + m[i];
			}
			if (N == 1) {
				System.out.println("1");
				continue;
			}
			int ans = 0;
			for (int i = 0; i < 2 * N; ++i) {
				for (int j = 0; j < 2 * N; ++j) {
					if (i % N == j % N)
						continue;
					double theta = (x[i] == x[j] ? 0 : Math.atan((y[j] - y[i]) / (x[j] - x[i])));
					double w = Math.sqrt((x[j] - x[i]) * (x[j] - x[i]) + (y[j] - y[i]) * (y[j] - y[i]));
					theta*=-1;
					for (int sign = -1; sign <= 1; sign += 2) {

						double cos = (r[i] + r[j] * sign) / w;
						for (int sign2 = -1; sign2 <= 1; sign2 += 2) {
							double sin = Math.sqrt(1 - cos * cos) * sign2;
							if (cos != cos || sin != sin) {
								continue;
							}
							double a = cos * Math.cos(theta) + sin * Math.sin(theta);
							double b = -cos * Math.sin(theta) + sin * Math.cos(theta);
							double c = -r[i] - a * x[i] - b * y[i];
							int count = 0;
							for (int k = 0; k < N; ++k) {
								double dis = Math.abs(a * x[k] + b * y[k] + c) / Math.sqrt(a * a + b * b) - r[k];
								if (-EPS <= dis && dis <= m[k] + EPS) {
									++count;
								}
							}
							ans = Math.max(ans, count);

						}
					}
				}
			}
			System.out.println(ans);
		}
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

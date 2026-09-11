import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Main {
	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	void run() throws IOException {
		Scanner sc = new Scanner(System.in);
		while (true) {
			ans_t = null;
			int n = sc.nextInt();
			if (n == 0)
				break;
			double[][] v = new double[n][2];
			for (int i = 0; i < n; ++i) {
				v[i][0] = sc.nextDouble();
				v[i][1] = sc.nextDouble();
			}
			double X = sc.nextDouble();
			double Y = sc.nextDouble();
			Arrays.sort(v, new Comparator<double[]>() {
				@Override
				public int compare(double[] o1, double[] o2) {
					return -Double.compare(o1[1] / o1[0], o2[1] / o2[0]);
				}
			});
			// v???vy/vx???????????????????????????
			double g = 9.8;
			int left = 0;// [0...left...]??????ti>0??§?????§???
			int right = n; // [0....r](r<right)??????ti>0??§?????§???
			// vx>0??????px=???vix*ti????????????{t}????????¨???????????¨???????¨????????????????
			do {
				int middle = (right + left) / 2;
				if (check(X, Y, g, v, middle)) {
					left = middle;
				} else {
					right = middle;
				}
			} while (right - left > 1);
			check(X, Y, g, v, left);
			double max = 0;
			for (int i = 0; i < ans_t.length; ++i) {
				max += (v[i][1] * ans_t[i] - 0.5 * g * ans_t[i] * ans_t[i]);
			}

			double min = max;
			for (int i = 0; i < n; ++i) {
				double t = X / v[i][0];
				min = Math.min(min, v[i][1] * t - 0.5 * g * t * t);
			}
			if (min <= Y && Y <= max) {
				System.out.println("Yes");
			} else {
				System.out.println("No");
			}
		}
	}

	double[] ans_t;

	boolean check(double X, double Y, double g, double[][] v, int middle) {
		int n = middle + 1;
		double px = X;
		double coe = v[0][0];
		double[] t = new double[n];
		for (int i = 1; i < n; ++i) {
			// i???i
			// j???0
			double a = 1 / g * (v[i][1] / v[i][0] - v[0][1] / v[0][0]);
			a *= v[i][0] * v[i][0];
			px -= a;
			a = v[i][0] * v[i][0] / v[0][0];
			coe += a;
		}
		if (px / coe < 0)
			return false;
		t[0] = px / coe;
		for (int i = 1; i < n; ++i) {
			t[i] = v[i][0] * (1 / g * (v[i][1] / v[i][0] - v[0][1] / v[0][0]) + t[0] / v[0][0]);
			if (t[i] < 0)
				return false;
		}
		ans_t = Arrays.copyOf(t, t.length);
		return true;
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

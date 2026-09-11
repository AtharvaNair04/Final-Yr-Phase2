import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Main {
	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	long INF = (long) (1e19);

	void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			int n = sc.nextInt();
			if (n == 0)
				break;
			long[] k = new long[n];
			for (int i = 0; i < n; ++i) {
				k[i] = sc.nextLong();
			}
			solve(n, k);
		}
	}

	void solve(int n, long[] k) {
		ArrayList<List<Long>> lis = new ArrayList<>();
		int ans = 0;
		out: for (long w = k[0] * 2 - 1; w <= 2 * k[0] + 1; ++w) {
			long A = 0, B = 0;
			for (int i = n - 1; i >= 0; --i) {
				if (k[i] == w) {
					B += 1L << i;
					if (i > 0)
						continue;
				} else if (k[i] == 0) {
					if (i > 0)
						continue;
				} else {
					if (i == 0) {
						B += k[0];
					} else {
						B += k[i] + (1L << i) - 1;
					}
				}
				A = B - w + 1;
				if (check(A, B, k, n)) {
					++ans;
					lis.add(Arrays.asList(A, B));
				}
				continue out;
			}
		}
		if (ans == 0) {
			System.out.println("None");
		} else if (ans == 1) {
			System.out.println(lis.get(0).get(0) + " " + lis.get(0).get(1));
		} else {
			System.out.println("Many");
		}
	}

	boolean check(long A, long B, long[] k, int n) {
		if ((1L << n) <= A || A <= 0 || (long) (1e18) < A)
			return false;
		if ((1L << n) <= B || B <= 0 || (long) (1e18) < B)
			return false;
		for (int i = n - 1; i >= 0; --i) {
			if (g(A, B, i) != k[i])
				return false;
		}
		return true;
	}

	long g(long A, long B, int i) {
		return f(B, i) - f(A - 1, i);
	}

	long f(long a, int i) {
		if (a <= 0)
			return 0;
		if (i == 0) {
			return (a + 1) / 2;
		}
		if ((1L << i) > a)
			return 0;

		int l = 0;
		while (1L << (l + 1) <= a) {
			++l;
		}
		if (i == l) {
			return a - (1L << i) + 1;
		}
		long d = a - ((1L << l) - 1);
		if (i == l) {
			return (1L << (l - 1)) + d;
		} else {
			return (1L << (l - 1)) + f(d - 1, i);
		}
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int q = sc.nextInt();
		StringBuilder sb = new StringBuilder();
		while (q-- > 0) {
			long a = sc.nextLong();
			long b = sc.nextLong();
			sb.append(solve(a, b));
			sb.append("\n");
		}
		System.out.print(sb.toString());
	}

	private static long solve(long a, long b) {
		if (a == b) return 2 * (a - 1);
		if (a > b) {
			long t = a;
			a = b;
			b = t;
		}
		long l = 0;
		long r = b - 1;

		while (r > l) {
			long m = (l + r + 1) / 2;
			long l2 = 0;
			long r2 = m - 1;
			while (r2 > l2) {
				long m2 = (l2 + r2) / 2;
				if ((m - m2) * (a + 1 + m2) <= (m - (m2 + 1)) * (a + 2 + m2)) {
					l2 = m2 + 1;
				} else {
					r2 = m2;
				}
			}

			if ((m - l2) * (a + 1 + l2) < a * b) {
				l = m;
			} else {
				r = m - 1;
			}
		}

		return (a - 1) + l;
	}
}

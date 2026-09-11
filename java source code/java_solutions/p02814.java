import java.io.*;
import java.util.*;

class Main {


	private static long lcm(long a, long b) {
		return a / gcd(a, b) * b;
	}

	private static long gcd(long a, long b) {
		if (b == 0) return a;
		return gcd(b, a % b);
	}

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long m = sc.nextLong();
		long[] a = new long[n];

		boolean ok = true;

		for (int i = 0; i < n; i++) {
			a[i] = sc.nextLong();
			if (a[i] % 2 == 1) ok = false;
			a[i] /= 2;

		}

		if (!ok) {
			System.out.println(0);
			return;
		}

		int b = f(a[0]);
		for (int i = 0; i < n; i++) {
			if (f(a[i]) != b) {
				ok = false;
				break;
			}
			a[i] >>= b;
		}

		if (!ok) {
			System.out.println(0);
			return;
		}

		m >>= b;

		long lcm = 1;
		for (int i = 0; i < n; i++) {
			lcm = lcm(lcm, a[i]);
			if (lcm > m) {
				System.out.println(0);
				return;
			}
		}

		m /= lcm;
		System.out.println((m + 1) / 2);
	}

	private static int f(long a) {
		int count = 0;
		while (a % 2 == 0) {
			a /= 2;
			count++;
		}

		return count;
	}
}

import java.io.*;
import java.util.*;

class Main {

	static final int M = 1000000007;


	private static long pow(long x, long n) {
		if (x == 0) return 0;
		long a = 1;
		while (n > 0) {
			if (n % 2 == 1) {
				a = a * x % M;
			}
			x = x * x % M;
			n >>= 1;
		}
		return a;
	}

	private static long inv(long x, long M) {
		return pow(x, M - 2);
	}

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		long[] x = new long[N];
		for (int i = 0; i < N; i++) {
			x[i] = sc.nextLong();
		}

		long[] fact = new long[N];
		fact[0] = 1;
		for (int i = 1; i < N; i++) {
			fact[i] = fact[i - 1] * i % M;
		}

		long a = 0;
		long c = 0;
		for (int i = 0; i < N - 1; i++) {
			c = (c + fact[N - 1] * inv(i + 1, M) % M) % M;
			long d = x[i + 1] - x[i];
			a = (a + c * d % M) % M;
		}

		System.out.println(a);
	}
}

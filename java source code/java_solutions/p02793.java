import java.io.*;
import java.util.*;

class Main {

	static final int M = 1000000007;

	private static Map<Long, Long> factorize(long n) {
		Map<Long, Long> a = new HashMap<>();
		for (long i = 2; i * i <= n; i++) {
			if (n % i != 0) continue;
			long count = 0;
			while (n % i == 0) {
				count++;
				n /= i;
			}
			a.putIfAbsent(i, count);
		}

		if (n != 1) a.putIfAbsent(n, 1L);
		return a;
	}

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		Map<Long, Long> lcm = new HashMap<>();
		long[] A = new long[N];
		for (int i = 0; i < N; i++) {
			A[i] = sc.nextLong();
			for (Map.Entry<Long, Long> e : factorize(A[i]).entrySet()) {
				lcm.putIfAbsent(e.getKey(), e.getValue());
				if (lcm.get(e.getKey()) < e.getValue()) {
					lcm.put(e.getKey(), e.getValue());
				}
			}
		}

		long LCM = 1;
		for (Map.Entry<Long, Long> e : lcm.entrySet()) {
			for (int i = 0; i < e.getValue(); i++) {
				LCM *= e.getKey();
				LCM %= M;
			}
		}

		long answer = 0;
		for (int i = 0; i < N; i++) {
			answer += LCM * pow(A[i], M - 2, M);
			answer %= M;
		}

		System.out.println(answer);
	}

	private static long pow(long a, long n, long M) {
		long x = 1;
		while (n > 0) {
			if ((n & 1) == 1) {
				x *= a;
				x %= M;
			}
			a *= a;
			a %= M;
			n >>= 1;
		}
		return x;
	}
}

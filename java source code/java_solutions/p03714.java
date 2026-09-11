import java.io.*;
import java.util.*;

class Main {

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		long[] a = new long[(3 * N)];
		for (int i = 0; i < 3 * N; i++) {
			a[i] = sc.nextLong();
		}
		solve(N, a);
	}

	static void solve(int N, long[] a) {
		PriorityQueue<Long> first = new PriorityQueue<>();
		PriorityQueue<Long> second = new PriorityQueue<>(Comparator.reverseOrder());
		long fsum = 0;
		long ssum = 0;
		for (int i = 0; i < N; i++) {
			first.offer(a[i]);
			fsum += a[i];
			second.offer(a[3 * N - i - 1]);
			ssum += a[3 * N - i - 1];
		}

		long[] f = new long[N + 1];
		long[] s = new long[N + 1];

		f[0] = fsum;
		s[N] = ssum;
		for (int k = 0; k < N; k++) {
			int l = N + k;
			int r = 2 * N - 1 - k;

			first.offer(a[l]);
			fsum += a[l];
			fsum -= first.poll();
			f[k + 1] = fsum;

			second.offer(a[r]);
			ssum += a[r];
			ssum -= second.poll();
			s[N - 1 - k] = ssum;
		}

		long answer = Long.MIN_VALUE;
		for (int i = 0; i <= N; i++) {
			answer = Math.max(answer, f[i] - s[i]);
		}

		System.out.println(answer);
	}
}

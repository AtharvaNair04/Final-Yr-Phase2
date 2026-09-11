import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long k = sc.nextLong();

		long[] a = new long[n];
		long sum = 0;
		for (int i = 0; i < a.length; i++) {
			a[i] = sc.nextLong();
			sum += a[i];
		}

		Set<Long> s = new HashSet<>();
		for (long i = 1; i * i < sum; i++) {
			if (sum % i == 0) {
				s.add(i);
				s.add(sum / i);
			}
		}

		long answer = 1;
		for (long x : s) {
			long[] b = new long[n];
			long lsum = 0;
			for (int i = 0; i < n; i++) {
				b[i] = a[i] % x;
				lsum += (x - b[i]);
			}
			Arrays.sort(b);
			long needs = Long.MAX_VALUE;
			long rsum = 0;
			for (int i = 0; i < n; i++) {
				rsum += b[i];
				lsum -= (x - b[i]);
				if (rsum == lsum) needs = Math.min(needs, rsum);
			}

			if (needs <= k) answer = Math.max(answer, x);
		}

		System.out.println(answer);
	}
}

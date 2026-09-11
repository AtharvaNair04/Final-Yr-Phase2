import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	static long[] reverse(long a[], int n) {
		long[] b = new long[n];
		int j = n;
		for (int i = 0; i < n; i++) {
			b[j - 1] = a[i];
			j = j - 1;
		}
		return b;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long m = sc.nextLong();
		long v = sc.nextLong();
		int p = sc.nextInt();
		long[] a = new long[n];
		for (int i = 0; i < a.length; i++) {
			a[i] = sc.nextLong();
		}
		sc.close();
		Arrays.sort(a);
		a = reverse(a, a.length);

		boolean flag = true;

		int l = 0;
		int r = n;
		while (l < r) {
//			System.out.printf("Checking l=%d r=%d \n", l, r);
			int mid = (l + r) >>> 1;
			boolean able = true;
			if (mid < p) {
				flag = true;
			} else if (a[mid] + m < a[p - 1]) {
				flag = false;
			} else {
				long sum = (p - 1) * m + (n - mid - 1) * m + m;

				for (int i = p - 1; i < mid; i++) {
					sum += a[mid] + m - a[i];
					if (a[mid] + m < a[i]) {
						able = false;
					}
				}
//				System.out.println(sum);
				flag = sum >= m * v;
			}

			if (able && flag) {
				l = mid + 1;
			} else {
				r = mid;
			}
		}
		System.out.println(l);
	}
}

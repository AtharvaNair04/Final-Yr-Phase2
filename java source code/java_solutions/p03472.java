import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long h = sc.nextLong();

		long[] a = new long[n];
		long[] b = new long[n];
		for (int i = 0; i < n; i++) {
			a[i] = sc.nextLong();
			b[i] = sc.nextLong();
		}

		Arrays.sort(a);
		Arrays.sort(b);

		long answer = 0;
		int bi = n - 1;
		while (h > 0 && bi >= 0 && b[bi] > a[n - 1]) {
			h -= b[bi];
			bi--;
			answer++;
		}

		if (h > 0) {
			answer += h / a[n - 1];
			answer += (h % a[n - 1] > 0) ? 1 : 0;
		}

		System.out.println(answer);
	}
}

import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		int a[];
		int max;

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}

			a = new int[n + m];
			for (int i = 0; i < n; i++) {
				a[i] = sc.nextInt();
			}
			for (int i = 0; i < m; i++) {
				a[n + i] = sc.nextInt();
			}
			Arrays.sort(a);
			max = a[0];
			for (int i = 1; i < n + m; i++) {
				max = Math.max(max, a[i] - a[i - 1]);
			}
			System.out.println(max);
		}
	}
}

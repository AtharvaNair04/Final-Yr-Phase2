import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = sc.nextInt();
		}
		Arrays.sort(a);

		int b = 1000000010;
		for (int i = 0; i < n - 1; i++) {
			if (Math.abs((double) a[n - 1] / 2 - (double) a[i]) < Math.abs((double) a[n - 1] / 2.0 - (double) b)) {
				b = a[i];
			}
		}
		System.out.println(a[n - 1] + " " + b);

	}
}

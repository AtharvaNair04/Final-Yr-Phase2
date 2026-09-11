import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long[] a = new long[n];
		long[] b = new long[n];
		for (int i = 0; i < n; i++) {
			a[i] = sc.nextLong();
		}
		for (int i = 0; i < n; i++) {
			b[i] = sc.nextLong();
		}

		long ac = 0;
		long bc = 0;
		for (int i = 0; i < n; i++) {
			if (a[i] > b[i]) {
				bc += a[i] - b[i];
			} else if (b[i] - a[i] >= 2) {
				ac += (b[i] - a[i]) / 2;
			}
		}
		if (ac >= bc) {
			System.out.println("Yes");
		} else {
			System.out.println("No");
		}
	}
}

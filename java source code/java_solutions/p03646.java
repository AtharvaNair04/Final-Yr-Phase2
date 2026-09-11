import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		long k = sc.nextLong();
		long[] a = new long[50];
		long t = k / 50;
		long m = k % 50;
		for (int i = 0; i < 50; i++) {
			a[i] = 49 + t;
			if (i < m) {
				a[i] += 51 - k % 50;
			} else {
				a[i] -= k % 50;
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append(50).append("\n");
		for (int i = 0; i < 50; i++) {
			sb.append(a[i]).append(" ");
		}

		System.out.println(sb.toString());
	}
}

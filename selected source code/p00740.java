import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		while (true) {
			int n = scan.nextInt();
			int p = scan.nextInt();
			int mem = 0;
			int b = 0;
			if (n == 0 && p == 0)
				break;

			int[] a = new int[n];

			for (int i = 0; i < n; i++) {
				a[i] = 0;
			}

			while (true) {
				if (p != 0) {
					a[b]++;
					p--;
					if (a[b] == 1)
						mem++;
					b++;
					if (b == n)
						b = 0;
				} else {
					p = a[b];
					a[b] = 0;
					if (p != 0)
						mem--;
					b++;
					if (b == n)
						b = 0;
				}
				if (p == 0 && mem == 1) {
					if (b == 0) {
						b = n - 1;
					} else {
						b = b - 1;
					}
					break;
				}

			}
			System.out.println(b);
		}
	}
}

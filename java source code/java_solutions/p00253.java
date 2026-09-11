import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n;
		int a[];
		int b[];
		int d;

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}

			a = new int[n + 1];
			b = new int[n];
			for (int i = 0; i < n + 1; i++) {
				a[i] = sc.nextInt();
			}
			for (int i = 0; i < n + 1; i++) {
				int id = 0;
				for (int j = 0; j < n; j++) {
					if (id == i) {
						id++;
					}
					b[j] = a[id++];
				}
				boolean f = true;
				d = b[1] - b[0];
				for (int j = 1; j < n; j++) {
					if (b[j] - b[j - 1] != d) {
						f = false;
					}
				}
				if (f) {
					System.out.println(a[i]);
					break;
				}
			}
		}
	}
}

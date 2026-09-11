import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int a, b, c;
		int parts[];
		int m;
		int p[], q[], r[], s[];

		a = sc.nextInt();
		b = sc.nextInt();
		c = sc.nextInt();
		while (a + b + c != 0) {
			m = sc.nextInt();
			parts = new int[a + b + c];
			p = new int[m];
			q = new int[m];
			r = new int[m];
			s = new int[m];
			for (int i = 0; i < a + b + c; i++) {
				parts[i] = 2;
			}
			for (int i = 0; i < m; i++) {
				p[i] = sc.nextInt();
				q[i] = sc.nextInt();
				r[i] = sc.nextInt();
				s[i] = sc.nextInt();
				if (s[i] == 1) {
					parts[p[i] - 1] = 1;
					parts[q[i] - 1] = 1;
					parts[r[i] - 1] = 1;
				}
			}
			for (int i = 0; i < m; i++) {
				if (s[i] == 0) {
					if (parts[p[i] - 1] * parts[q[i] - 1] * parts[r[i] - 1] == 2) {
						if (parts[p[i] - 1] == 2) {
							parts[p[i] - 1] = 0;
						} else if (parts[q[i] - 1] == 2) {
							parts[q[i] - 1] = 0;
						} else if (parts[r[i] - 1] == 2) {
							parts[r[i] - 1] = 0;
						}
					}
				}
			}

			for (int i = 0; i < a + b + c; i++) {
				System.out.println(parts[i]);
			}

			a = sc.nextInt();
			b = sc.nextInt();
			c = sc.nextInt();
		}
	}
}

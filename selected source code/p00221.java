import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		int p[];
		int id;
		int num;

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}

			p = new int[n];
			Arrays.fill(p, 1);
			id = 0;
			num = n;

			for (int i = 1; i <= m; i++) {
				String s = sc.next();
				String c = Integer.toString(i);
				if (1 < num) {
					if (i % 15 == 0) {
						c = "FizzBuzz";
					} else if (i % 3 == 0) {
						c = "Fizz";
					} else if (i % 5 == 0) {
						c = "Buzz";
					}
					if (!s.equals(c)) {
						p[id] = 0;
						num--;
					}
					id = (id + 1) % n;
					while (p[id] == 0) {
						id = (id + 1) % n;
					}
				}
			}

			String s = "";
			for (int i = 0; i < n; i++) {
				if (p[i] == 1) {
					System.out.print(s + (i + 1));
					s = " ";
				}
			}
			System.out.println();
		}
	}
}

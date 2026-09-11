import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int n, m;
		int location[];
		int count;
		boolean flag;

		n = scanner.nextInt();
		m = scanner.nextInt();
		while (n + m != 0) {
			location = new int[m];
			count = n;
			flag = true;
			for (int i = 0; i < m; i++) {
				location[i] = 0;
			}
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					location[j] += scanner.nextInt();
				}
			}
			while (0 <= count) {
				for (int i = 0; i < m; i++) {
					if (location[i] == count) {
						if (flag) {
							System.out.print(i + 1);
							flag = false;
						} else {
							System.out.print(" " + (i + 1));
						}
					}
				}
				count--;
			}
			System.out.println();
			n = scanner.nextInt();
			m = scanner.nextInt();
		}
	}
}

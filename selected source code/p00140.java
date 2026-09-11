import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 5, 4, 3, 2, 1, 0 };
		while (n-- > 0) {
			int a = scanner.nextInt();
			int b = scanner.nextInt();
			if (a > b && a < 6) {
				a = 15 - a;
			}
			while (true) {
				System.out.print(x[a]);
				if (x[a] == b) {
					break;
				}
				System.out.print(' ');
				if (a == x.length - 1) {
					a = 1;
				} else {
					a++;
				}
			}
			System.out.println();
		}
	}
}

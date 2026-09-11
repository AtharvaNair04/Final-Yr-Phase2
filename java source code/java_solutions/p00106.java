import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		int min;

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			min = Integer.MAX_VALUE;
			for (int i = 0; i < 100; i++) {
				for (int j = 0; j < 100; j++) {
					for (int k = 0; k < 100; k++) {
						if (n == i * 200 + j * 300 + k * 500) {
							int sum = 0;
							sum += i * 380;
							sum -= (i / 5) * 380 * 5 * 0.20;
							sum += j * 550;
							sum -= (j / 4) * 550 * 4 * 0.15;
							sum += k * 850;
							sum -= (k / 3) * 850 * 3 * 0.12;
							if (sum < min) {
								min = sum;
							}
						}
					}
				}
			}
			System.out.println(min);
		}
	}
}

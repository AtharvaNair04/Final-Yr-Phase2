import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		double x, y;
		int n;

		n = sc.nextInt();
		while (n != -1) {
			x = 1.0;
			y = 0.0;

			for (int i = 0; i < n - 1; i++) {
				if (x == 0.0) {
					if (0 < y) {
						x -= 1.0;
					} else {
						x += 1.0;
					}
				} else if (y == 0.0) {
					if (0 < x) {
						y += 1.0;
					} else {
						y -= 1.0;
					}
				} else {
					double theta = Math.atan2(y, x) + Math.PI / 2;
					x += Math.cos(theta);
					y += Math.sin(theta);
				}
			}
			System.out.println(x);
			System.out.println(y);
			n = sc.nextInt();
		}		
	}
}

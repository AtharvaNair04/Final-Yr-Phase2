import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		int sumAngle;
		double area1, area2;

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			area1= 0;
			sumAngle = 0;
			for (int i = 0; i < n - 1; i++) {
				int angle = sc.nextInt();
				area1 += Math.sin(Math.toRadians(angle));
				sumAngle += angle;
			}
			area1 += Math.sin(Math.toRadians(360 - sumAngle));

			m = sc.nextInt();
			area2 = 0;
			sumAngle = 0;
			for (int i = 0; i < m - 1; i++) {
				int angle = sc.nextInt();
				area2 += Math.sin(Math.toRadians(angle));
				sumAngle += angle;
			}
			area2 += Math.sin(Math.toRadians(360 - sumAngle));

			if ((int)(area1 * 10000000) == (int)(area2 * 10000000)) {
				System.out.println("0");
			} else if (area2 < area1) {
				System.out.println("1");
			} else {
				System.out.println("2");
			}
		}
	}
}

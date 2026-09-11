import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int R = sc.nextInt();
		double X = sc.nextInt();
		double Y = sc.nextInt();
		int N = sc.nextInt();
		int sum = 0;
		for (int i = 0; i < N; ++i) {
			int P = sc.nextInt();
			double x1 = R * Math.cos(Math.PI / 2 - sum / 100.0 * Math.PI * 2);
			double y1 = R * Math.sin(Math.PI / 2 - sum / 100.0 * Math.PI * 2);
			sum += P;
			double x2 = R * Math.cos(Math.PI / 2 - sum / 100.0 * Math.PI * 2);
			double y2 = R * Math.sin(Math.PI / 2 - sum / 100.0 * Math.PI * 2);
			double SO = Math.PI * R * R * P / 100.0;
			double DS1 = area(0, 0, x2, y2, X, Y);
			double DS2 = area(0, 0, X, Y, x1, y1);
			double SN = SO + DS1 + DS2;
			if (i != 0) System.out.print(" ");
			System.out.print((int) (100 * SN / SO));
		}
		System.out.println();
	}

	static double area(double x0, double y0, double x1, double y1, double x2, double y2) {
		double dx1 = x1 - x0;
		double dy1 = y1 - y0;
		double dx2 = x2 - x0;
		double dy2 = y2 - y0;
		return (-dy2 * dx1 + dy1 * dx2) / 2;
	}

}

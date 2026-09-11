import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int[][] num = new int[4][2];
		while (scanner.hasNext()) {
			double a = scanner.nextDouble();
			double b = scanner.nextDouble();
			slove(a, 0, num);
			slove(b, 1, num);
		}
		for (int[] b : num) {
			System.out.println(b[0] + " " + b[1]);
		}
	}

	private static void slove(double a, int n, int[][] num) {
		if (a >= 1.1)
			num[0][n]++;
		else if (a >= 0.6)
			num[1][n]++;
		else if (a >= 0.2)
			num[2][n]++;
		else
			num[3][n]++;
	}
}

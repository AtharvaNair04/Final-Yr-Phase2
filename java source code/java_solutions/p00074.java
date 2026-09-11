import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int h, m, s;

		h = sc.nextInt();
		m = sc.nextInt();
		s = sc.nextInt();
		while (h != -1 || m != -1 || s != -1) {
			int h2, m2, s2;
			int h3, m3, s3;
			int second;
			second = 60 * 60 * h + 60 * m + s;
			second = 60 * 60 * 2 - second;
			h2 = second / 3600;
			second %= 3600;
			m2 = second / 60;
			second %= 60;
			s2 = second;

			second = 60 * 60 * h + 60 * m + s;
			second = 60 * 60 * 2 * 3 - second * 3;
			h3 = second / 3600;
			second %= 3600;
			m3 = second / 60;
			second %= 60;
			s3 = second;
			System.out.printf("%02d:%02d:%02d\n", h2, m2, s2);
			System.out.printf("%02d:%02d:%02d\n", h3, m3, s3);
			h = sc.nextInt();
			m = sc.nextInt();
			s = sc.nextInt();
		}
	}
}

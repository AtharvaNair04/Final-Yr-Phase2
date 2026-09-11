import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		// 回数
		int n = in.nextInt();
		// 今までに払った金額
		int x = 800;
		// レストランからもらう金額
		int y = 200;
		// answer
		int ans = 0;
		if (n < 15) {
			System.out.println(x * n);
		} else if (n == 15) {
			System.out.println(x * n - y);
		} else if (15 < n) {
//			if (n % 15 == 0) {
				System.out.println(x * n - (y * (n / 15)));
//			}else {
//				System.out.println(x * n - (y * (n / 15)));
//			}

		}
	}
}

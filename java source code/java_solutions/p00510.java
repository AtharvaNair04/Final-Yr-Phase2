import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		int ans = m;
		for (int i = 0; i < n; i++) {
			int a = scanner.nextInt();
			int b = scanner.nextInt();
			m += a - b;
			if (m < 0) {
				System.out.println(0);
				return;
			}
			ans = Math.max(ans, m);
		}
		System.out.println(ans);
	}
}

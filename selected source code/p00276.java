import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		new Main().run();
	}

	private void run() {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		for (int i = 0; i < n; i++) {
			int C = scanner.nextInt();
			int A = scanner.nextInt();
			int N = scanner.nextInt();
			int ans = 0;
			int min = Math.min(C, Math.min(A, N));
			C -= min;
			A -= min;
			N -= min;
			ans += min;
			int mi = Math.min(C / 2, A);
			ans += mi;
			C -= mi * 2;
			ans += C / 3;

			System.out.println(ans);
		}
	}
}

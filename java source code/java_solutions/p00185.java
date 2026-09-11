import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	private void run() {
		Scanner scanner = new Scanner(System.in);
		int N = 1000000;
		boolean[] primes = new boolean[N + 1];
		primes[0] = true;
		primes[1] = true;
		for (int i = 2; i <= Math.sqrt(N); i++) {
			if (!primes[i]) {
				for (int j = i * 2; j <= N; j += i) {
					primes[j] = true;
				}
			}

		}
		while (true) {
			int n = scanner.nextInt();
			if (n == 0)
				break;
			int count = 0;
			for (int i = n - 1; i >= n / 2; i -= 2) {
				if (!primes[i] && !primes[n - i]) {
					count++;
				}
			}
			System.out.println(count);
		}

	}
}

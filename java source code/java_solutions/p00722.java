import java.util.Arrays;
import java.util.Scanner;

public class Main {
	int MAX = 1000000;
	int prime[];

	void eratosthenes(int max) {
		prime = new int[max + 1];
		Arrays.fill(prime, 1);
		prime[0] = prime[1] = 0;
		for (int i = 2; i * i <= max; i++) {
			if (prime[i] == 1) {
				for (int j = i * i; j <= max; j += i) {
					prime[j] = 0;
				}
			}
		}
	}

	void run() {
		Scanner sc = new Scanner(System.in);

		eratosthenes(MAX);

		L: while (true) {
			int a = sc.nextInt();
			int d = sc.nextInt();
			int n = sc.nextInt();
			if ((a | d | n) == 0) {
				break;
			}
			int cnt = 0;
			for (int i = a; ; i += d) {
				if (prime[i] == 1) {
					cnt++;
					if (cnt == n) {
						System.out.println(i);
						continue L;
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

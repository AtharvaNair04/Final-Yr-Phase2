import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str[] = br.readLine().split(" ");

		long n = Long.parseLong(str[0]);
		long k = Long.parseLong(str[1]);

		if (k == 0) {
			System.out.println(n * n);
			return;
		}

		long ans = 0;

		for (int i = 1; i <= n; i++) {
			ans += (n / i) * Math.max(0, i - k);
			if (n % i != 0) {
				ans += Math.max(n % i - k + 1, 0);
			}
		}

		System.out.println(ans);
	}
}

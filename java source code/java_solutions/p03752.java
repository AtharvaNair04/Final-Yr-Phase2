import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str[] = br.readLine().split(" ");

		int n = Integer.parseInt(str[0]);
		int k = Integer.parseInt(str[1]);

		long a[] = new long[n];

		str = br.readLine().split(" ");
		for (int i = 0; i < n; i++) {
			a[i] = Long.parseLong(str[i]);
		}

		long ans = Long.MAX_VALUE;
		for (int bit = 0; bit < (1 << n); bit++) {
			long b[] = a.clone();
			boolean flag[] = new boolean[n];
			flag[0] = true;

			int count = 0;
			for (int i = 0; i < n; i++) {
				if ((bit & (1 << i)) != 0) {
					flag[i] = true;
					count++;
				}
			}

			if (count < k)
				continue;

			long sum = 0;
			for (int i = 0; i < n; i++) {
				long height = 0;
				if (flag[i]) {
					for (int j = 0; j < i; j++) {
						height = Math.max(b[j], height);
					}
					if (height >= b[i]) {
						sum += height + 1 - b[i];
						b[i] += height + 1 - b[i];
					}
				}
			}
			ans = Math.min(ans, sum);
		}
		System.out.println(ans);
	}
}

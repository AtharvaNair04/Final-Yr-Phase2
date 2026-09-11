import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] sa = br.readLine().split(" ");
		int n = Integer.parseInt(sa[0]);
		int x = Integer.parseInt(sa[1]);
		sa = br.readLine().split(" ");
		int[] s = new int[n];
		for (int i = 0; i < n; i++) {
			s[i] = Integer.parseInt(sa[i]);
		}
		br.close();

		Arrays.sort(s);

		int mx = 100000;
		int mod = 1000000007;
		long total = 1;
		for (int i = 2; i <= n; i++) {
			total *= i;
			total %= mod;
		}

		Set<Integer> set = new HashSet<>();
		set.add(x);
		long[] dp = new long[mx + 1];
		dp[x] = 1;
		for (int i = n - 1; i >= 0; i--) {
			Integer[] arr = set.toArray(new Integer[0]);
			for (int o : arr) {
				int next = o % s[i];
				long val = dp[o] * modinv(i + 1, mod) % mod;
				dp[next] += val;
				dp[next] %= mod;
				dp[o] += mod - val;
				dp[o] %= mod;
				set.add(next);
			}
		}

		long ans = 0;
		for (int i = 1; i < s[0]; i++) {
			ans += i * dp[i] % mod * total;
			ans %= mod;
		}
		System.out.println(ans);
	}

	static long modinv(long a, int m) {
		long b = m;
		long u = 1;
		long v = 0;
		long tmp = 0;

		while (b > 0) {
			long t = a / b;
			a -= t * b;
			tmp = a;
			a = b;
			b = tmp;

			u -= t * v;
			tmp = u;
			u = v;
			v = tmp;
		}

		u %= m;
		if (u < 0) u += m;
		return u;
	}
}

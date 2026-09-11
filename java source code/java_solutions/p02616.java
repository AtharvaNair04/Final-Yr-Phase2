// practice with kaiboy
import java.io.*;
import java.util.*;

public class Main extends PrintWriter {
	Main() { super(System.out, true); }
	Scanner sc = new Scanner(System.in);
	public static void main(String[] $) {
		Main o = new Main(); o.main(); o.flush();
	}

	static final int MD = 1000000007;
	void main() {
		int n = sc.nextInt();
		int k = sc.nextInt();
		if (n == k) {
			long ans = 1;
			while (n-- > 0) {
				int a = sc.nextInt();
				ans = ans * a % MD;
			}
			if (ans < 0)
				ans += MD;
			println(ans);
			return;
		}
		int[] pp = new int[n];
		int[] qq = new int[n];
		int np = 0, nq = 0;
		while (n-- > 0) {
			int a = sc.nextInt();
			if (a >= 0)
				pp[np++] = a;
			else
				qq[nq++] = -a;
		}
		pp = Arrays.stream(pp, 0, np).boxed().sorted().mapToInt($->$).toArray();
		qq = Arrays.stream(qq, 0, nq).boxed().sorted().mapToInt($->$).toArray();
		if (np == 0) {
			if (k % 2 == 1)
				for (int i = 0, j = nq - 1; i < j; i++, j--) {
					int tmp = qq[i]; qq[i] = qq[j]; qq[j] = tmp;
				}
			long ans = 1;
			for (int j = nq - 1; j >= nq - k; j--)
				ans = ans * qq[j] % MD;
			if (k % 2 == 1)
				ans = (MD - ans) % MD;
			println(ans);
			return;
		}
		long ans = 1;
		if (k % 2 == 1) {
			ans = pp[--np];
			k--;
		}
		k /= 2;
		while (k-- > 0)
			if (np >= 2 && nq >= 2) {
				long p = (long) pp[np - 1] * pp[np - 2];
				long q = (long) qq[nq - 1] * qq[nq - 2];
				if (p > q) {
					ans = ans * (p % MD) % MD;
					np -= 2;
				} else {
					ans = ans * (q % MD) % MD;
					nq -= 2;
				}
			} else if (np >= 2) {
				long p = (long) pp[np - 1] * pp[np - 2];
				ans = ans * (p % MD) % MD;
				np -= 2;
			} else {
				long q = (long) qq[nq - 1] * qq[nq - 2];
				ans = ans * (q % MD) % MD;
				nq -= 2;
			}
		println(ans);
	}
}

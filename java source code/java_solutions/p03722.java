// practice with kaiboy
import java.io.*;
import java.util.*;

public class Main extends PrintWriter {
	Main() { super(System.out, true); }
	Scanner sc = new Scanner(System.in);
	public static void main(String[] $) {
		Main o = new Main(); o.main(); o.flush();
	}

	static final long INF = 0x3f3f3f3f3f3f3f3fL;
	void main() {
		int n = sc.nextInt();
		int m = sc.nextInt();
		int[] ii = new int[m];
		int[] jj = new int[m];
		int[] ww = new int[m];
		for (int h = 0; h < m; h++) {
			ii[h] = sc.nextInt() - 1;
			jj[h] = sc.nextInt() - 1;
			ww[h] = sc.nextInt();
		}
		long[] dp = new long[n]; Arrays.fill(dp, -INF); dp[0] = 0;
		for (int k = 1; k <= n * 2; k++) {
			boolean updated = false;
			for (int h = 0; h < m; h++) {
				int i = ii[h], j = jj[h];
				if (dp[i] != -INF) {
					long d = dp[i] + ww[h];
					if (dp[j] < d) {
						dp[j] = d;
						if (j == n - 1)
							updated = true;
					}
				}
			}
			if (updated && k >= n) {
				println("inf");
				return;
			}
		}
		println(dp[n - 1]);
	}
}

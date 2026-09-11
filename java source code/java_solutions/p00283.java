import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			d = scanner.nextInt();
			n = scanner.nextInt();
			if ((d | n) == 0)
				break;
			aer = new AER[n];
			for (int i = 0; i < n; i++) {
				aer[i] = new AER(scanner.nextInt(), scanner.nextInt(),
						scanner.nextInt());
			}
			dp = new int[d + 1][101];
			for (int[] dpp : dp)
				Arrays.fill(dpp, INF);
			PriorityQueue<AER> pq = new PriorityQueue<AER>();
			pq.offer(new AER(d, 0, 0));
			ans = INF;
			while (!pq.isEmpty()) {
				AER now = pq.poll();
				if (now.a == 0) {
					ans = Math.min(ans, now.r);
					break;
				}
				for (AER a : aer) {
					if (now.e < a.r)
						continue;
					int nhp = Math.max(0, now.a - a.a);
					int nexp = Math.min(100, now.e + a.e);
					if (now.a == nhp && now.e == nexp)
						continue;
					if (dp[nhp][nexp] <= now.r + 1)
						continue;
					dp[nhp][nexp] = now.r + 1;
					pq.offer(new AER(nhp, nexp, now.r + 1));

				}
			}
			System.out.println(ans == INF ? "NA" : ans);
		}
	}

	int d, n;
	int[][] dp;
	int INF = 1 << 30;
	AER[] aer;
	int ans;

	class AER implements Comparable<AER> {
		int a, e, r;

		public AER(int a, int e, int r) {
			super();
			this.a = a;
			this.e = e;
			this.r = r;
		}

		@Override
		public String toString() {
			return "AER [a=" + a + ", e=" + e + ", r=" + r + "]";
		}

		@Override
		public int compareTo(AER o) {
			if (this.r == o.r)
				return this.a - o.a;
			return this.r - o.r;
		}
	}
}

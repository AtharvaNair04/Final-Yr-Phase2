import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		new Main().run();
	}

	@SuppressWarnings("unchecked")
	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		loop: while (true) {
			int n = scanner.nextInt();
			if (n == 0)
				break;
			int[] k = new int[n];
			Map<String, Integer>[] tbl = new Map[n];
			for (int i = 0; i < n; i++)
				tbl[i] = new HashMap<String, Integer>();
			Map<String, Integer> minPrice = new HashMap<String, Integer>();
			int[][] ad = new int[n + 1][n + 1];
			for (int i = 0; i < n; i++) {
				k[i] = scanner.nextInt();
				for (int j = 0; j < k[i]; j++) {
					String b = scanner.next();
					int p = scanner.nextInt();
					tbl[i].put(b, p);
					if (!minPrice.containsKey(b) || minPrice.get(b) > p)
						minPrice.put(b, p);
				}
			}
			int q = scanner.nextInt();
			String[] req = new String[q];
			for (int i = 0; i < q; i++)
				req[i] = scanner.next();
			int m = scanner.nextInt();
			for (int i = 0; i < m; i++) {
				int s = scanner.nextInt();
				int t = scanner.nextInt();
				int d = scanner.nextInt();
				ad[s][t] = ad[t][s] = d;
			}
			int sum = 0;
			for (int i = 0; i < q; i++) {
				if (!minPrice.containsKey(req[i])) {
					System.out.println("impossible");
					continue loop;
				}
				sum += minPrice.get(req[i]);
			}
			PriorityQueue<Pair> deque = new PriorityQueue<Pair>();
			int[][] dp = new int[n + 1][1 << q];
			for (int[] dp1 : dp)
				Arrays.fill(dp1, 1 << 20);
			dp[0][0] = 0;
			deque.offer(new Pair(0, 0, 0));
			int ans = 0;
			while (!deque.isEmpty()) {
				Pair pair = deque.poll();
				int c = pair.f;
				int cbit = pair.s;
				int nbit = cbit;
				int cost = pair.cost;
				if (c == 0 && nbit == (1 << q) - 1) {
					ans = cost;
					break;
				}
				if(dp[c][nbit]<cost)
					continue;
				dp[c][nbit] = cost;

				if (c > 0)
					for (int i = 0; i < q; i++) {
						if (tbl[c - 1].containsKey(req[i])
								&& tbl[c - 1].get(req[i]).equals(
										minPrice.get(req[i]))) {
							nbit |= 1 << i;
						}
					}
				for (int i = 0; i < n + 1; i++) {
					if (ad[c][i] == 0)
						continue;
					deque.offer(new Pair(i, nbit, cost + ad[c][i]));
				}
			}
			System.out.println(sum + " " + ans);
		}

	}

	class Pair implements Comparable<Pair> {
		int f, s, cost;

		public Pair(int f, int s, int cost) {
			super();
			this.f = f;
			this.s = s;
			this.cost = cost;
		}

		@Override
		public String toString() {
			return "Pair [f=" + f + ", s=" + s + ", cost=" + cost + "]";
		}

		@Override
		public int compareTo(Pair o) {
			return cost - o.cost;
		}

	}
}

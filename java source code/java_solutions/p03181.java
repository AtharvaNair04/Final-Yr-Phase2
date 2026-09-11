import java.io.*;
import java.util.*;

class Main {

	private static long[] dp;
	private static long[] answer;
	private static List<Integer>[] g;
	private static int m;

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		m = sc.nextInt();

		g = new ArrayList[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<>();
		}

		for (int i = 0; i < n - 1; i++) {
			int x = sc.nextInt() - 1;
			int y = sc.nextInt() - 1;
			g[x].add(y);
			g[y].add(x);
		}

		dp = new long[n];
		answer = new long[n];
		dfs1(0, -1);
		dfs2(0, -1);

		StringBuilder sb = new StringBuilder();
		for (int v = 0; v < n; v++) {
			sb.append(answer[v]).append("\n");
		}

		System.out.print(sb.toString());
	}

	private static void dfs1(int v, int p) {
		dp[v] = 1;
		for (int to : g[v]) {
			if (to == p) continue;
			dfs1(to, v);
			dp[v] = (dp[v] * (dp[to] + 1)) % m;
		}
	}

	private static void dfs2(int v, int p) {
		List<Long> lm = new ArrayList<>();
		List<Long> rm = new ArrayList<>();
		lm.add(1L);
		rm.add(1L);
		for (int i = 0; i < g[v].size(); i++) {
			long last = lm.get(lm.size() - 1);
			lm.add(last * (1 + dp[g[v].get(i)]) % m);
		}

		for (int i = g[v].size() - 1; i >= 0; i--) {
			long last = rm.get((rm.size() - 1));
			rm.add(last * (1 + dp[g[v].get(i)]) % m);
		}

		answer[v] = rm.get(rm.size() - 1);
		for (int i = 0; i < g[v].size(); i++) {
			if (g[v].get(i) == p) continue;
			long t1 = dp[v];
			long t2 = dp[g[v].get(i)];
			long a = lm.get(i) * rm.get(rm.size() - 1 - i - 1) % m;
			dp[v] = a;
			dp[g[v].get(i)] = dp[g[v].get(i)] * (dp[v] + 1) % m;
			dfs2(g[v].get(i), v);
			dp[v] = t1;
			dp[g[v].get(i)] = t2;
		}
	}
}

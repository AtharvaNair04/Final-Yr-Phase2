import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	static ArrayList<Integer>[] adj;
	static long dp[][];
	static int m = 1000000007;
	// dp[i][j] number of ways to print the i rooted tree with or without
	// constraints
	// there can be only two possibilities that parent of i is black than we have to
	// print i with W otherwise we have no constraints

	public static long Solve(int u, int constraint, int parent) {

		if (dp[u][constraint] != -1)
			return dp[u][constraint];

		long ans = 0;
		// we will always colour white whether parent is white or black
		long w1 = 1;
		for (int v : adj[u]) {
			if (v != parent) {
				w1 = (w1 % m * Solve(v, 0, u) % m) % m;
			}
		}
		ans = (ans % m + w1 % m) % m;

		// we can print black only when parent is white
		if (constraint == 0) {
			long w2 = 1;
			for (int v : adj[u]) {
				if (v != parent) {
					w2 = (w2 % m * Solve(v, 1, u) % m) % m;
				}
			}
			ans = (ans % m + w2 % m) % m;
		}

		dp[u][constraint] = ans;
		return dp[u][constraint];

	}

	public static void main(String[] args) {
		FastScanner sc = new FastScanner();
		int n = sc.nextInt();
		adj = new ArrayList[n + 1];
		for (int i = 0; i <= n; i++)
			adj[i] = new ArrayList<>();

		for (int i = 0; i < n - 1; i++) {
			int x = sc.nextInt(), y = sc.nextInt();
			adj[x].add(y);
			adj[y].add(x);
		}

		dp = new long[100005][2];
		for (int i = 0; i < 100005; i++)
			Arrays.fill(dp[i], -1);

		long ans = Solve(1, 0, -1);
		System.out.println(ans);

	}

	static class FastScanner {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer("");

		String next() {
			while (!st.hasMoreTokens())
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		int[] readArray(int n) {
			int a[] = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = nextInt();
			return a;
		}

	}

}

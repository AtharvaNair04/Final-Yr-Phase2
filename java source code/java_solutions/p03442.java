import java.io.*;
import java.util.*;

public class Main {
	
	int[][] g;

	void submit() {
		int n = nextInt();
		g = new int[n][];
		int[] a = new int[3 * n - 3];
		int[] deg = new int[n];
		for (int i = 0; i < n - 1; i++) {
			a[3 * i] = nextInt();
			a[3 * i + 1] = nextInt();
			a[3 * i + 2] = nextInt();
			deg[a[3 * i]]++;
			deg[a[3 * i + 1]]++;
		}

		for (int i = 0; i < n; i++) {
			g[i] = new int[2 * deg[i]];
		}

		for (int i = 0; i < 3 * n - 3; i += 3) {
			int v = a[i];
			int u = a[i + 1];
			int lbl = a[i + 2];
			--deg[v];
			g[v][2 * deg[v]] = u;
			g[v][2 * deg[v] + 1] = lbl;
			--deg[u];
			g[u][2 * deg[u]] = v;
			g[u][2 * deg[u] + 1] = lbl;
		}
		
		dfs(0, -1);
		
		int groups = 0;
		groups += cnt[0];
		cnt[0] = 0;
		for (int i = 1; i < 16; i++) {
			groups += cnt[i] / 2;
			cnt[i] %= 2;
		}
		
		groups += go(cnt);
		out.println(n - 1 - groups);
	}
	
	int go(int[] arr) {
		int[] xor = new int[1 << 15];
		for (int i = 0; i < 15; i++) {
			xor[1 << i] = i + 1;
		}
		for (int i = 1; i < xor.length; i++) {
			int low = i & -i;
			xor[i] = xor[low] ^ xor[i ^ low];
		}
		
		int[] dp = new int[1 << 15];
		dp[0] = 0;
		for (int mask = 1; mask < 1 << 15; mask++) {
			for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
				dp[mask] = Math.max(dp[mask], dp[mask ^ sub] + (xor[sub] == 0 ? 1 : 0));
			}
		}
		
		int look = 0;
		for (int i = 0; i < 15; i++) {
			if (cnt[i + 1] > 0) {
				look |= 1 << i;
			}
		}
		return dp[look];
	}
	
	int[] cnt = new int[16];
	
	int dfs(int v, int p) {
		int up = 0;
		int allChild = 0;
		for (int i = 0; i < g[v].length; i += 2) {
			int u = g[v][i];
			int x = g[v][i + 1];
			if (u == p) {
				up = x;
			} else {
				allChild ^= dfs(u, v);
			}
		}
		if (p != -1) {
//			System.err.println(ret);
			cnt[up ^ allChild]++;
		}
		return up;
	}

	void preCalc() {

	}

	void stress() {

	}

	void test() {

	}

	Main() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		preCalc();
		submit();
		//stress();
		//test();
		out.close();
	}

	static final Random rng = new Random();

	static int rand(int l, int r) {
		return l + rng.nextInt(r - l + 1);
	}

	public static void main(String[] args) throws IOException {
		new Main();
	}

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;

	String nextToken() {
		while (st == null || !st.hasMoreTokens()) {
			try {
				st = new StringTokenizer(br.readLine());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return st.nextToken();
	}

	String nextString() {
		try {
			return br.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	int nextInt() {
		return Integer.parseInt(nextToken());
	}

	long nextLong() {
		return Long.parseLong(nextToken());
	}

	double nextDouble() {
		return Double.parseDouble(nextToken());
	}
}

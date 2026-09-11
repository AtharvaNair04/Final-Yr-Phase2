import java.util.*;
import java.io.*;
import java.awt.geom.*;
import java.math.*;

public class Main implements Runnable {

	static final Scanner in = new Scanner(System.in);
	static final PrintWriter out = new PrintWriter(System.out,false);
	static boolean debug = false;


	static int[][][] undirectedGraph(int n, int[] v1, int[] v2, int[] cost) {
		int[] cnt = new int[n];
		for (int i : v1) cnt[i]++;
		for (int i : v2) cnt[i]++;

		int[][][] g = new int[n][][];
		for (int i=0; i<n; i++) g[i] = new int[cnt[i]][2];
		for (int i=0; i<v1.length; i++) {
			int s = v1[i];
			int t = v2[i];

			g[s][--cnt[s]][0] = t;
			g[s][cnt[s]][1] = cost[i];

			g[t][--cnt[t]][0] = s;
			g[t][cnt[t]][1] = cost[i];
		}

		return g;
	}

	static int[] height(int[][][] g) {
		int n = g.length;

		int[][][] dp = new int[n][][];
		for (int i=0; i<n; i++) {
			dp[i] = new int[g[i].length][2];
			for (int j=0; j<g[i].length; j++) {
				dp[i][j][1] = -1;
			}
		}
		for (int i=0; i<n; i++)
			for (int j=0; j<g[i].length; j++)
				if (dp[i][j][1] < 0)
					dp[i][j][1] = dfs(g, dp, i, j);

		int[] res = new int[n];
		for (int i=0; i<n; i++)
			for (int j=0; j<g[i].length; j++)
				res[i] = Math.max(res[i], dp[i][j][1]);

		return res;
	}

	static int dfs(int[][][] g, int[][][] dp, int src, int idx) {
		if (dp[src][idx][1] >= 0) return dp[src][idx][1];
		dp[src][idx][1] = g[src][idx][1];
		int dst = g[src][idx][0];
		for (int k=0; k<g[dst].length; k++) {
			int next = g[dst][k][0];
			if (next == src) continue;
			dp[src][idx][1] = Math.max(dp[src][idx][1], dfs(g, dp, dst, k) + g[src][idx][1]);
		}
		return dp[src][idx][1];
	}

	public void run() {
		int n = in.nextInt();
		int[] s = new int[n-1];
		int[] t = new int[n-1];
		int[] w = new int[n-1];
		for (int i=0; i<n-1; i++) {
			s[i] = in.nextInt();
			t[i] = in.nextInt();
			w[i] = in.nextInt();
		}
		int[][][] g = undirectedGraph(n, s, t, w);
		int[] h = height(g);
		for (int i=0; i<n; i++) {
			out.println(h[i]);
		}

		out.flush();
		in.close();
		out.close();
	}

	public static void main(String[] args) {
		debug = args.length > 0;
		new Thread(null, new Main(), "", 16 * 1024 * 1024).start();
	}

	static void dump(Object... o) { if (debug) System.err.println(Arrays.deepToString(o)); }
}

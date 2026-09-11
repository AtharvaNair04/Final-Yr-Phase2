import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
public class Main implements Runnable{
	public static void main(String[] args) 
	{
		new Thread(null, new Main(), " ",Runtime.getRuntime().maxMemory()).start();
	}
	public void run(){
		solver();
	}
	
	int[] depth;
	ArrayList<Edge>[] g;
	@SuppressWarnings("unchecked")
	void solver(){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		depth = new int[n];
		g = new ArrayList[n];
		for (int i = 0; i < n; ++i)
			g[i] = new ArrayList<>();
		for (int i = 0; i < n - 1; ++i) {
			int s = sc.nextInt();
			int t = sc.nextInt();
			int w = sc.nextInt();
			g[s].add(new Edge(s, t, w));
			g[t].add(new Edge(t, s, w));
		}

		dfs(0, -1);

		int d = -1;
		int v = -1;
		for (int i = 0; i < n; ++i) {
			if (d < depth[i]) {
				d = depth[i];
				v = i;
			}
		}

		Arrays.fill(depth, 0);

		dfs(v, -1);

		int ans = -1;
		for (int i = 0; i < n; ++i) {
			ans = Math.max(ans, depth[i]);
		}

		System.out.println(ans);

	}

	void dfs(int cur, int pre) {
		for (Edge e : g[cur]) {
			if (e.dst != pre) {
				depth[e.dst] = depth[e.src] + e.cost;
				dfs(e.dst, cur);
			}
		}
	}

	static class Edge {
		int src;
		int dst;
		int cost;

		public Edge(int src, int dst, int cost) {
			this.src = src;
			this.dst = dst;
			this.cost = cost;
		}
	}
}

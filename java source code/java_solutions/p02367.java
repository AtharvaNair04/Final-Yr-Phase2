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

	static ArrayList<int[]> bridge(int[][][] g) {
		int n = g.length;
		int[] ord = new int[n];
		Arrays.fill(ord, -1);

		ArrayList<int[]> res = new ArrayList<>();

		for (int cur=0; cur<n; cur++) {
			if (ord[cur] == -1) {
				dfsBridge(g, cur, -1, 0, res, ord, new boolean[n], new ArrayDeque<Integer>(), new ArrayDeque<Integer>());
				res.remove(res.size()-1);
			}
		}

		return res;
	}

	static void dfsBridge(int[][][] g, int cur, int prev, int d, ArrayList<int[]> res, int[] ord, boolean[] visited, ArrayDeque<Integer> roots, ArrayDeque<Integer> stack) {
		ord[cur] = d++;
		visited[cur] = true;
		roots.addFirst(cur);
		stack.addFirst(cur);

		for (int[] to : g[cur]) {
			int next = to[0];
			if (next == prev) continue;
			if (ord[next] == -1) dfsBridge(g, next, cur, d+1, res, ord, visited, roots, stack);
			else if (visited[next]) while (ord[roots.peek()] > ord[next]) roots.pollFirst();
		}

		if (cur == roots.peek()) {
			res.add(new int[]{cur, prev});
			while (true) {
				int v = stack.pollFirst();
				visited[v] = false;
				if (cur == v) break;
			}
			roots.pollFirst();
		}
	}

	public void run() {
		int v = in.nextInt();
		int e = in.nextInt();
		int[] s = new int[e];
		int[] t = new int[e];
		int[] c = new int[e];
		for (int i=0; i<e; i++) {
			s[i] = in.nextInt();
			t[i] = in.nextInt();
			c[i] = 0;
		}
		int[][][] g = undirectedGraph(v, s, t, c);
		ArrayList<int[]> ans = bridge(g);

		for (int i=0; i<ans.size(); i++) {
			if (ans.get(i)[0] > ans.get(i)[1]) {
				int tmp = ans.get(i)[0];
				ans.get(i)[0] = ans.get(i)[1];
				ans.get(i)[1] = tmp;
			}
		}

		Collections.sort(ans, new Comparator<int[]>(){
			public int compare(int[] a, int[] b) {
				if (a[0] != b[0]) return a[0] - b[0];
				return a[1] - b[1];
			}
		});
		for (int i=0; i<ans.size(); i++) {
			out.println(ans.get(i)[0]+" "+ans.get(i)[1]);
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

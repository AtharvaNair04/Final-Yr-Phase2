// javac Main.java && java _

import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) { new Main(); }
	FS in = new FS();
	PrintWriter out = new PrintWriter(System.out);
	
	int n;
	int[] deg;
	ArrayList<int[]>[] adj;
	
	Main() {
		n = in.nextInt();
		deg = new int[n];
		adj = new ArrayList[n];
		for (int i = 0; i < n; i++)
			adj[i] = new ArrayList<>();

		int k = 0;
		for (int i = 0; i < n - 1; i++) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			deg[a]++; adj[a].add(new int[]{b, i});
			deg[b]++; adj[b].add(new int[]{a, i});
		
			k = max(k, deg[a]);
			k = max(k, deg[b]);
		}

		int[] color = new int[n - 1];
		int st = 0;
		for (int i = 0; i < n; i++)
			if (deg[i] == k)
				st = i;

		ArrayDeque<Integer> q = new ArrayDeque<>();
		q.add(st);

		boolean[] vis = new boolean[n];
		vis[st] = true;
		while (q.size() > 0) {
			int node = q.poll();

			int use = 1;
			HashSet<Integer> track = new HashSet<>();
			for (int[] e : adj[node]) {
				if (!vis[e[0]]) {
					vis[e[0]] = true;
					q.add(e[0]);
				}
				if (color[e[1]] != 0) {
					track.add(color[e[1]]);
					continue;
				}
			}

			for (int[] e : adj[node]) {
				if (color[e[1]] != 0)
					continue;
				while (track.contains(use))
					use++;
				color[e[1]] = use;
				track.add(color[e[1]]);
				use++;
			}
		}

		out.println(k);
		for (int i = 0; i < n - 1; i++)
			out.println(color[i]);
		out.close();
	}
	
	

	int min(int a, int b) { if (a < b) return a; return b; }	
	int max(int a, int b) { if (a > b) return a; return b; }	
	long min(long a, long b) { if (a < b) return a; return b; }	
	long max(long a, long b) { if (a > b) return a; return b; }	

	boolean z(int x) { if (x == 0) return true; return false; }
	boolean z(long x) { if (x == 0) return true; return false; }
	
	class FS {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer("");
		String next() {
			while (!st.hasMoreTokens()) {
				try { st = new StringTokenizer(br.readLine()); }
				catch (Exception e) {}
			} return st.nextToken();
		}
		int nextInt() { return Integer.parseInt(next()); }
		long nextLong() { return Long.parseLong(next()); }
		double nextDouble() { return Double.parseDouble(next()); }
	}
}

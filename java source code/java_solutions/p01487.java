import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		new Main().solver();
	}

	@SuppressWarnings("unchecked")
	void solver() {
		Scanner sc = new Scanner(System.in);
		int V = sc.nextInt();
		int E = sc.nextInt();
		ArrayList<Integer>[] g = new ArrayList[V];
		for (int i = 0; i < g.length; i++)
			g[i] = new ArrayList<>();
		for (int i = 0; i < E; i++) {
			int a = sc.nextInt() - 1;
			int b = sc.nextInt() - 1;
			g[a].add(b);
			g[b].add(a);
		}
		Integer[] color = new Integer[V];
		Arrays.fill(color, -1);
		int base = 0;
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < V; i++) {
			if (color[i] == -1) {
				color[i] = 0;
				counter[0] = 1;
				counter[1] = 0;
				if (!dfs(g, color, i)) {
					System.out.println(-1);
					return;
				} else {
					base += Math.min(counter[0], counter[1]);
					if (counter[0] != counter[1]) {
						int value = Math.abs(counter[0] - counter[1]);
						map.put(Math.abs(value), map.containsKey(value) ? map.get(value) + 1 : 1);
					}
				}
			}
		}
		boolean[] dp = new boolean[V / 2 - base + 1];
		dp[0] = true;
		Iterator<Entry<Integer, Integer>> it = map.entrySet().iterator();
		int x = 0;
		while (it.hasNext()) {
			Entry<Integer, Integer> entry = it.next();
			int value = entry.getKey();
			int c = entry.getValue();
			for (int i = dp.length - 1; i >= 0; i--) {
				if (dp[i]) {
					for (int j = 1; j <= c; j++) {
						if (i + j * value < dp.length) {
							dp[i + j * value] = true;
							if (dp[i])
								x = Math.max(x, i + j * value);
						} else {
							break;
						}
					}
				}
			}
		}
		System.out.println((long)(base + x) * (long)(V - x - base) - E);
	}

	int[] counter = new int[2];

	boolean dfs(ArrayList<Integer>[] g, Integer[] color, int cur) {
		for (int v : g[cur]) {
			if (color[v] == -1) {
				color[v] = 1 ^ color[cur];
				counter[color[v]]++;
				if (!dfs(g, color, v)) {
					return false;
				}
			} else {
				if (color[cur] != (1 ^ color[v]))
					return false;
			}
		}
		return true;
	}
}

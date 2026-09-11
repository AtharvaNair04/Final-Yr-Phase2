import java.io.*;
import java.util.*;

class Main {

	public static int[] color;
	public static int red, blue;
	public static List<Integer>[] e;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();

		e = new List[n];
		for (int i = 0; i < n; i++) e[i] = new ArrayList<>();
		for (int i = 0; i < n - 1; i++) {
			int v = sc.nextInt() - 1;
			int w = sc.nextInt() - 1;
			e[v].add(w);
			e[w].add(v);
		}

		color = new int[n];
		color[0] = 1;
		red = 1;
		blue = 0;

		dfs(0);
		StringBuilder sb = new StringBuilder();
		int a1 = 1, a2 = 2, a3 = 3;
		if (red > n / 3 && blue > n / 3) {
			for (int i = 0; i < n; i++) {
				if (color[i] == 1) {
					if (a1 <= n) {
						sb.append(a1);
						a1 += 3;
					} else {
						sb.append(a3);
						a3 += 3;
					}
				} else {
					if (a2 <= n) {
						sb.append(a2);
						a2 += 3;
					} else {
						sb.append(a3);
						a3 += 3;
					}
				}
				sb.append(" ");
			}
		} else if (red < blue) {
			for (int i = 0; i < n; i++) {
				if (color[i] == 1) {
					sb.append(a3);
					a3 += 3;
				} else {
					if (a2 <= n) {
						sb.append(a2);
						a2 += 3;
					} else if (a1 <= n) {
						sb.append(a1);
						a1 += 3;
					} else {
						sb.append(a3);
						a3 += 3;
					}
				}
				sb.append(" ");
			}
		} else {
			for (int i = 0; i < n; i++) {
				if (color[i] == 2) {
					sb.append(a3);
					a3 += 3;
				} else {
					if (a1 <= n) {
						sb.append(a1);
						a1 += 3;
					} else if (a2 <= n) {
						sb.append(a2);
						a2 += 3;
					} else {
						sb.append(a3);
						a3 += 3;
					}
				}
				sb.append(" ");
			}
		}

		System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
	}

	private static void dfs(int v) {
		for (int i = 0; i < e[v].size(); i++) {
			if (color[e[v].get(i)] != 0) continue;
			if (color[v] == 1) {
				color[e[v].get(i)] = 2;
				blue++;
			} else if (color[v] == 2) {
				color[e[v].get(i)] = 1;
				red++;
			}
			dfs(e[v].get(i));
		}
	}
}

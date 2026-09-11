import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		int M = sc.nextInt();
		int[] A = new int[6];
		int pos, goal;
		for (int i = 0; i < 6; ++i) {
			A[i] = sc.nextInt();
		}
		pos = sc.nextInt() - 1;
		goal = sc.nextInt() - 1;
		ArrayList<ArrayList<Integer>> prev = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < M; ++i) {
			prev.add(new ArrayList<Integer>());
		}
		int[] N = new int[M];
		for (int i = 0; i < M; ++i) {
			N[i] = sc.nextInt();
			prev.get(i + N[i]).add(i);
		}
		int[] dist = new int[M];
		Arrays.fill(dist, 9999);
		ArrayList<Integer> cur = prev.get(goal);
		for (int i = 0; !cur.isEmpty(); ++i) {
			ArrayList<Integer> next = new ArrayList<Integer>();
			for (int c : cur) {
				if (dist[c] <= i) continue;
				dist[c] = i;
				for (int j = 0; j < 6; ++j) {
					int n = c - A[j];
					if (n < 0 || M <= n) continue;
					for (int cand : prev.get(n)) {
						if (dist[cand] <= i) continue;
						next.add(cand);
					}
				}
				for (int j = 0; j < 6; ++j) {
					int n = c + A[j];
					if (n < 0 || M <= n) continue;
					for (int cand : prev.get(n)) {
						if (dist[cand] <= i) continue;
						next.add(cand);
					}
				}
			}
			cur = next;
		}
		//				System.err.println(Arrays.toString(dist));
		int cd = dist[pos];
		while (true) {
			int dice = sc.nextInt() - 1;
			int move = A[dice];
			int ld = pos - move < 0 ? 9999 : dist[pos - move];
			int rd = pos + move >= M ? 9999 : dist[pos + move];
			if (ld < cd) {
				System.out.println(-1);
				pos = pos - move + N[pos - move];
				cd = ld;
			} else if (rd < cd) {
				System.out.println(1);
				pos = pos + move + N[pos + move];
				cd = rd;
			} else {
				System.out.println(0);
			}
			System.out.flush();
			//						System.err.println("pos:" + pos + " move:" + move + " cd:" + cd + " ld:" + ld + " rd:" + rd);
			if (pos == goal) return;
		}
	}
}

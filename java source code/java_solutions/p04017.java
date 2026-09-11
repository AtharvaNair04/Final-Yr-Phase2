import java.util.*;
import java.lang.*;

class Main {

	public static void main(String[] args) {
		final Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] xs = new int[n];
		for (int i = 0; i < n; i++) {
			xs[i] = sc.nextInt();
		}

		int L = sc.nextInt();
		int num = 1;
		int t = 1;
		while (num < n) {
			num <<= 1;
			t++;
		}

		int[][] next = new int[t][n];
		for (int i = 0; i < n; i++) {
			next[0][i] = lowerBound(xs, xs[i] + L);
		}

		for (int k = 0; k < t - 1; k++) {
			for (int i = 0; i < n; i++) {
				next[k + 1][i] = next[k][next[k][i]];
			}
		}

		StringBuilder sb = new StringBuilder();
		int q = sc.nextInt();
		while (q-- > 0) {
			int a = sc.nextInt() - 1;
			int b = sc.nextInt() - 1;
			if (a > b) {
				int c = a;
				a = b;
				b = c;
			}

			int answer = 1;
			for (int i = t - 1; i >= 0; i--) {
				if (xs[b] > xs[next[i][a]]) {
					a = next[i][a];
					answer += Math.pow(2, i);
				}
			}
			sb.append(answer).append("\n");
		}

		System.out.print(sb);
	}

	private static int lowerBound(int[] a, int x) {
		int l = 0, r = a.length;
		while (r - l > 1) {
			int m = (l + r) / 2;
			if (a[m] <= x) l = m;
			else r = m;
		}
		return l;
	}
}

import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	static String g;
	static String[] a, b;
	static final int INF = 999;
	static int N, M, A, B, C, T, RT;

	public static void main(String[] args) {
		while (true) {
			N = sc.nextInt();
			if (N == 0) break;
			M = sc.nextInt();
			A = sc.nextInt();
			B = sc.nextInt();
			C = sc.nextInt();
			T = sc.nextInt();
			RT = (int) Math.sqrt(T);
			int[] ans = solve();
			for (int i = 0; i < N; ++i) {
				if (i != 0) System.out.print(" ");
				System.out.print(ans[i]);
			}
			System.out.println();
		}
	}

	static int[] solve() {
		int[] v = new int[N];
		for (int i = 0; i < N; ++i) {
			v[i] = sc.nextInt();
		}
		if (RT == 0) return v;
		int[][] count = new int[N + 2][N + 2];
		for (int i = 0; i < N; ++i) {
			int[][] c = new int[2][N + 2];
			c[0][i + 1] = 1;
			int t = 0;
			for (int j = 0; j < RT; ++j) {
				for (int k = 0; k < N; ++k) {
					c[1 - t][k + 1] = 0;
				}
				for (int k = 0; k < N; ++k) {
					c[1 - t][k] += c[t][k + 1] * C;
					c[1 - t][k + 1] += c[t][k + 1] * B;
					c[1 - t][k + 2] += c[t][k + 1] * A;
				}
				for (int k = 0; k < N; ++k) {
					c[1 - t][k + 1] %= M;
				}
				t = 1 - t;
			}
			for (int j = 0; j < N; ++j) {
				count[i + 1][j + 1] = c[t][j + 1];
			}
		}
		int[][] c = new int[2][N + 2];
		for (int i = 0; i < N; ++i) {
			c[0][i + 1] = v[i];
		}
		int t = 0;
		for (int i = 0; i < RT; ++i) {
			for (int j = 0; j < N; ++j) {
				c[1 - t][j + 1] = 0;
			}
			for (int j = 0; j < N; ++j) {
				for (int k = 0; k < N; ++k) {
					c[1 - t][k + 1] += c[t][j + 1] * count[j + 1][k + 1];
				}
			}
			for (int j = 0; j < N; ++j) {
				c[1 - t][j + 1] %= M;
			}
			t = 1 - t;
		}
		for (int i = RT * RT; i < T; ++i) {
			for (int j = 0; j < N; ++j) {
				c[1 - t][j + 1] = 0;
			}
			for (int j = 0; j < N; ++j) {
				c[1 - t][j] += c[t][j + 1] * C;
				c[1 - t][j + 1] += c[t][j + 1] * B;
				c[1 - t][j + 2] += c[t][j + 1] * A;
			}
			for (int j = 0; j < N; ++j) {
				c[1 - t][j + 1] %= M;
			}
			t = 1 - t;
		}
		for (int i = 0; i < N; ++i) {
			v[i] = c[t][i + 1];
		}
		return v;
	}
}

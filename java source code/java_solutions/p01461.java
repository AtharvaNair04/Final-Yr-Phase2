import java.util.*;

public class Main {
	static int[] U;
	static long[] T;
	static long[] S;
	static int[] D;
	static int N;
	static int[][] g;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		g = new int[N][];
		for (int i = 0; i < N - 1; ++i) {
			int a = sc.nextInt() - 1;
			int b = sc.nextInt() - 1;
			g[i] = new int[] { a, b };
		}

		U = new int[N];
		T = new long[N];
		S = new long[N];
		D = new int[N];

		U[N - 1] = 1;
		for (int i = N - 2; i >= 0; --i) {
			for (int dst : g[i]) {
				U[i] += U[dst];
			}
		}
		for (int i = N - 2; i >= 0; --i) {
			for (int dst : g[i]) {
				T[i] += T[dst] + U[dst];
			}
		}
		for (int i = 0; i < N - 1; ++i) {
			for (int dst : g[i]) {
				D[dst] = D[i] + 1;
			}
		}
		for (int i = N - 2; i >= 0; --i) {
			int l = g[i][0];
			int r = g[i][1];
			S[i] = U[l] + U[r] + T[l] + T[r];
			S[i] = Math.min(S[i], U[l] + T[l] + 1 + S[r]);
			S[i] = Math.min(S[i], U[r] + T[r] + 1 + S[l]);
			S[i] = Math.min(S[i], S[l] + S[r] + 1 + D[r]);
			S[i] = Math.min(S[i], S[l] + S[r] + 1 + D[l]);
		}

		System.out.println(S[0]);

	}
}

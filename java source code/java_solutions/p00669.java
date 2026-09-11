import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);
	static int N, K;
	static int[] C;

	public static void main(String[] args) throws Exception {
		while (true) {
			N = sc.nextInt();
			K = sc.nextInt();
			if (N == 0) break;
			C = new int[N];
			for (int i = 0; i < N; ++i) {
				C[i] = sc.nextInt();
			}
			int prev = calc();
			int best = 0;
			for (int i = 0; i < N; ++i) {
				for (int j = i + 1; j < N; ++j) {
					int tmp = C[i];
					C[i] = C[j];
					C[j] = tmp;
					best = Math.max(best, calc());
					C[j] = C[i];
					C[i] = tmp;
				}
			}
			if (best < prev) {
				System.out.println("NO GAME");
			} else {
				System.out.println(best - prev);
			}
		}
	}

	static int calc() {
		int v = 1;
		for (int i = 0; i < K; ++i) {
			v *= C[i];
		}
		int max = v;
		for (int i = K; i < N; ++i) {
			v /= C[i - K];
			v *= C[i];
			max = Math.max(max, v);
		}
		return max;
	}

}

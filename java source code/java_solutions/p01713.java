import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int INF = 1 << 29;
		int W = sc.nextInt();
		int[] A = new int[W];
		for (int i = 0; i < W; ++i) {
			A[i] = Integer.parseInt(sc.next());
		}
		int[][] c = new int[2][W];
		for (int loop = 0; loop < 2; ++loop) {
			int exit = -1;
			int time = INF;
			for (int i = 0; i < W; ++i) {
				if (A[i] == 0) {
					exit = i;
					time = INF;
				} else if (A[i] < 0) {
					if (exit != -1) time = Math.min(time, -A[i] + i - exit);
				} else {
					if (exit != -1) c[loop][i] = Math.max(0, Math.min(A[i], time - (i - exit)));
				}
			}
			for (int i = 0; i < W / 2; ++i) {
				int tmp = A[i];
				A[i] = A[W - 1 - i];
				A[W - 1 - i] = tmp;
			}
		}
		int ans = 0;
		for (int i = 0; i < W; ++i) {
			ans += Math.max(c[0][i], c[1][W - 1 - i]);
		}
		System.out.println(ans);
	}
}

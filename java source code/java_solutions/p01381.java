import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int N = sc.nextInt();
		int K = sc.nextInt();
		if (K > N / 2) {
			System.out.println(-1);
			return;
		}
		int[] ord = new int[N];
		int f = 1;
		int b = N - 1;
		ord[0] = 0;
		ord[1] = 1;
		for (int i = 2; i < N; ++i) {
			if (i % 2 == 0) {
				ord[i] = b--;
			} else {
				ord[i] = ++f;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < K; ++i) {
			for (int j = 0; j < N - 1; ++j) {
				int v1 = (ord[j] + i) % N + 1;
				int v2 = (ord[j + 1] + i) % N + 1;
				sb.append(v1 + " " + v2 + "\n");
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}

}

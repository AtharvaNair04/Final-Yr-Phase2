import java.io.*;
import java.util.*;

class Main {

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		long M = 1L << sc.nextInt();
		long K = sc.nextLong();
		if (M == 2 && K == 0) {
			System.out.println("0 0 1 1");
			return;
		}

		if (K >= M || M == 2) {
			System.out.println(-1);
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (long i = 0; i < M; i++) {
			if (i == K) continue;
			sb.append(i).append(" ");
		}
		sb.append(K).append(" ");
		for (long i = M - 1; i >= 0; i--) {
			if (i == K) continue;
			sb.append(i).append(" ");
		}
		sb.append(K);
		System.out.println(sb.toString());
	}
}

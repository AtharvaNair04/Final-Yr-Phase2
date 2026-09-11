import java.util.*;
import java.io.*;

class Main {
	public static void main(String... args) {
		int N = IN.nextInt();
		int[] A = new int[N];
		for (int i = 0; i < N; i++) {
			A[i] = IN.nextInt() - (i + 1);
		}

		Arrays.sort(A);
		int m = A[N / 2];
		long ans = 0;
		for (int a: A) {
			ans += Math.abs(a - m);
		}
		puts(ans);
		flush();
	}
	static final Scanner IN = new Scanner(System.in);
	static final PrintWriter OUT = new PrintWriter(System.out);
	static <T> void puts(T arg) { OUT.println(arg); }
	static void flush() { OUT.flush(); }
}

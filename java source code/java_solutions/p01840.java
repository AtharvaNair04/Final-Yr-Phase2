public class Main {

	public static void main(String[] args) {
		try (java.util.Scanner sc = new java.util.Scanner(System.in)) {
			final int N = sc.nextInt();
			final int M = sc.nextInt();
			final int T = sc.nextInt();
			final int[] a = new int[N];
			for (int i = 0; i < N; ++i)
				a[i] = sc.nextInt();
			int rvalue = a[0] - M;
			for (int i = 1; i < N; ++i)
				rvalue += Math.max(0, a[i] - a[i - 1] - 2 * M);
			rvalue += Math.max(0, T - a[N - 1] - M);
			System.out.println(rvalue);
		}
	}
	
}

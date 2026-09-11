public class Main {
	
	public void run(java.io.InputStream in, java.io.PrintStream out) {
		final java.util.Scanner sc = new java.util.Scanner(in);
		for (;;) {
			final int m = sc.nextInt();
			final int nmin = sc.nextInt();
			final int nmax = sc.nextInt();
			if (m == 0 && nmin == 0 && nmax == 0)
				break ;
			final int[] p = new int[m];
			for (int i = 0; i < m; ++i) {
				p[i] = sc.nextInt();
			}
			
			int n = nmin;
			for (int i = nmin + 1; i <= nmax; ++i) {
				if (p[n - 1] - p[n] <= p[i - 1] - p[i])
					n = i;
			}
			System.out.println(n);
		}
		sc.close();
	}

	public static void main(String[] args) {
		(new Main()).run(System.in, System.out);
	}

}

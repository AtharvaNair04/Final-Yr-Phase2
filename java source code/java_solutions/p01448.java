public class Main {

	public static void main(String[] args) {
		try (final java.util.Scanner sc = new java.util.Scanner(System.in)) {
			final int N = sc.nextInt();
			final int[] a = new int[N];
			final int[] b = new int[N];
			for (int i = 0; i < N; ++i) {
				a[i] = sc.nextInt();
				b[i] = sc.nextInt();
			}
			
			final int[] events = new int[N + 2];
			for (int i = 0; i < N; ++i) {
				if (a[i] >= events.length) continue ;
				events[a[i]] += 1;
				if (b[i] + 1 >= events.length) continue ;
				events[b[i] + 1] -= 1;
			}
			
			int rvalue = 0;
			int status = 1;
			for (int i = 0; i < events.length; ++i) {
				status += events[i];
				if (i <= status) {
					rvalue = Math.max(rvalue, i - 1);
				}
			}
			System.out.println(rvalue);
		}
	}

}

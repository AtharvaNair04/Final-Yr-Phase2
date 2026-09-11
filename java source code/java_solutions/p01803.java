public class Main {

	private static final String covconv(String s) {
		final char[] a = s.toCharArray();
		int sp = 0;
		for (int p = 0; p < a.length; ++p) {
			a[sp++] = a[p];
			while (p < a.length && "aiueo".indexOf(a[p]) == -1) ++p;
		}
		return new String(a, 0, sp);
	}
	
	private static final int kenkov(String[] s) {
		int rvalue = 0;
		for (int i = 0; i < s.length; ++i)
			rvalue = Math.max(rvalue, s[i].length());
		return rvalue;
	}
	
	private static final boolean check_disjoint(String[] s, int k) {
		for (int i = 0; i < s.length; ++i) {
			final String s1 = s[i].substring(0, Math.min(s[i].length(), k));
			for (int j = i + 1; j < s.length; ++j) {
				final String s2 = s[j].substring(0, Math.min(s[j].length(), k));
				if (s1.equals(s2))
					return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		try (java.util.Scanner sc = new java.util.Scanner(System.in)) {
			MAINLOOP:
			for (;;) {
				final int n = sc.nextInt();
				if (n == 0) break ;
				final String[] s = new String[n];
				for (int i = 0; i < n; ++i) {
					s[i] = covconv(sc.next());
				}
				final int max_length = kenkov(s);
				int k;
				for (k = 1; k <= max_length; ++k) {
					if (check_disjoint(s, k)) {
						System.out.println(k);
						continue MAINLOOP;
					}
				}
				System.out.println("-1");
			}
		}
	}

}

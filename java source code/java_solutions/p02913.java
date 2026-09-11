import java.util.*;
import java.lang.*;

class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		char[] s = sc.next().toCharArray();
		int l = 0, r = n;
		int B = 1000000007;
		Map<Long, NavigableSet<Integer>> h = new HashMap<>();
		while (r - l > 1) {
			int m = (l + r) / 2;
			h.clear();
			boolean ok = false;
			long rh = 0;
			long t = 1;
			for (int i = 0; i < m; i++) {
				rh = rh * B + s[i];
				t *= B;
			}
			h.putIfAbsent(rh, new TreeSet<>());
			h.get(rh).add(m - 1);
			for (int i = 0; i + m < s.length; i++) {
				rh = rh * B + s[i + m] - t * s[i];
				if (h.containsKey(rh) && h.get(rh).floor(i) != null) {
					ok = true;
					break;
				}
				h.putIfAbsent(rh, new TreeSet<>());
				h.get(rh).add(i + m);
			}
			if (ok) l = m;
			else r = m;
		}

		System.out.println(l);
	}
}

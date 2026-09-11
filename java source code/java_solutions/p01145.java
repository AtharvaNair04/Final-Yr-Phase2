import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	Scanner sc = new Scanner(System.in);

	String s;

	void run() {
		for (;;) {
			s = sc.nextLine();
			if (s.equals("#")) {
				break;
			}
			solve();
		}
	}

	int Boin = 0, ShiBo = 1, Sya = 2, Xtu = 3, Nn = 4, Long = 5;
	String[] memo = { "Boin", "ShiBo", "Sya", "Xtu", "Nn", "Long" };

	void solve() {
		s += "$$$";
		char[] cs = s.toCharArray();

		ArrayList<R> list = new ArrayList<R>();

		for (int at = 0; at < cs.length;) {
			boolean check = false;
			// debug("at", at);
			if (cs[at] == '$') {
				break;
			}
			if (isBoin(cs[at])) {
				check = true;
				// debug(cs[at]);
				list.add(new R(s.substring(at, at + 1), at, Boin));
				at++;
			} else if (isShiin(cs[at]) && isBoin(cs[at + 1])) {
				check = true;
				// debug(cs[at], cs[at + 1]);
				list.add(new R(s.substring(at, at + 2), at, ShiBo));
				at += 2;
			} else if (isShiin(cs[at]) && not(cs[at], "yw")
					&& cs[at + 1] == 'y' && isBoin(cs[at + 2])) {
				// check y,w
				check = true;
				list.add(new R(s.substring(at, at + 3), at, Sya));
				// debug(cs[at], cs[at + 1], cs[at + 2]);
				at += 3;
			} else if (isShiin(cs[at]) && cs[at] == cs[at + 1]
					&& not(cs[at], "nyw")) {
				// debug(cs[at]);
				list.add(new R(s.substring(at, at + 1), at, Xtu));
				at++;
			} else if (cs[at] == 'n' && cs[at + 1] == '\'') {
				// debug(cs[at], cs[at + 1]);
				list.add(new R(s.substring(at, at + 2), at, Nn));
				at += 2;
			} else if (cs[at] == 'n'
					&& (isShiin(cs[at + 1]) || cs[at + 1] == '$')) {
				// debug(cs[at]);
				list.add(new R(s.substring(at, at + 1), at, Nn));
				at += 1;
			}
			if (check) {
				// if (false)
				if (hit(cs[at], "aiu")) {
					// debug(cs[at]);
					// list.add(new R(s.substring(at, at + 1), at, Long));
					R r = list.get(list.size() - 1);
					r.type = Long;
					r.s += cs[at];
					at++;
				}
				if (false)
					if (cs[at - 1] == cs[at]) {
						// debug(cs[at]);
						list.add(new R(s.substring(at, at + 1), at, Long));
						at++;
					}
			}
		}
		// debug();
		for (R r : list) {
//			debug(r.s, memo[r.type], r.shiin, r.boin);
		}
		R[] rs = list.toArray(new R[0]);
		int m = rs.length;
		boolean[] hit = new boolean[m];

		for (int i = 0; i < m; i++) {
			if ((rs[i].type == ShiBo || rs[i].type == Sya)
					&& hit(rs[i].boin, "iu") && isMuseiShiin(rs[i].s.charAt(0))) {
				if (i == m - 1 || isMuseiShiin(rs[i + 1].s.charAt(0))) {
					hit[i] = true;
				}
			}
		}
		// debug(hit);
		for (int i = m - 1; i >= 0; i--) {
			if ((rs[i].type == ShiBo || rs[i].type == Sya)
					&& hit(rs[i].boin, "ao") && isMuseiShiin(rs[i].s.charAt(0))) {
			} else {
				continue;
			}
			int j;
			for (j = i; j < m; j++) {
				if ((rs[j].type == ShiBo || rs[j].type == Sya)
						&& rs[j].boin == rs[i].boin
						&& isMuseiShiin(rs[j].s.charAt(0))) {
					// ok
				} else {
					break;
				}
			}
			if (j < m && rs[j].type == Long && rs[j].boin == rs[i].boin
					&& isMuseiShiin(rs[j].s.charAt(0))) {
				j++;
			}
			// debug(i, j);
			for (int k = i; k < j; k++) {
				hit[k] = true;
			}
			hit[j - 1] = false;
		}
		// debug(hit);
		for (int i = 0; i < m; i++) {
			int at = -1;
			for (int j = i - 1; j >= 0; j--) {
				if (rs[j].boin != '$') {
					at = j;
					break;
				}
			}
			if (at == -1 || !hit[at]) {
			} else {
				hit[i] = false;
			}
		}
		// debug(hit);
		String ans = "";
		for (int i = 0; i < m; i++) {
			String add;
			if (hit[i]) {
				add = rs[i].shiin + "(" + rs[i].boin + ")";
			} else {
				add = rs[i].s;
			}
			ans += add;
		}
		// debug(ans);
		println(ans);
	}

	class R {
		String s, shiin;
		char boin;
		int offset, type;

		R(String s, int offset, int type) {
			this.s = s;
			boin = '$';
			shiin = s;
			for (char c : s.toCharArray()) {
				if (isBoin(c)) {
					boin = c;
					shiin = s.substring(0, s.length() - 1);
				}
			}
			this.offset = offset;
			this.type = type;
		}
	}

	boolean hit(char c, String s) {
		return s.indexOf(c) >= 0;
	}

	boolean not(char c, String s) {
		return s.indexOf(c) == -1;
	}

	boolean isBoin(char c) {
		return "aiueo".indexOf(c) >= 0;
	}

	boolean isShiin(char c) {
		return "kstnhmyrwgzdbp".indexOf(c) >= 0;
	}

	boolean isMuseiShiin(char c) {
		return "ksthp".indexOf(c) >= 0;
	}

	void debug(Object... os) {
		System.err.println(deepToString(os));
	}

	void println(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) {
		new Main().run();
	}

}

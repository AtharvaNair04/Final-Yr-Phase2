import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.min;
import static java.lang.System.exit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {

	static void sort(int a[], int n) {
		if (n == 0) {
			return;
		}
		for (int i = 1; i < n; i++) {
			int j = i;
			int ca = a[i];
			do {
				int nj = (j - 1) >> 1;
				int na = a[nj];
				if (ca <= na) {
					break;
				}
				a[j] = na;
				j = nj;
			} while (j != 0);
			a[j] = ca;
		}
		int ca = a[0];
		for (int i = n - 1; i > 0; i--) {
			int j = 0;
			while ((j << 1) + 2 + Integer.MIN_VALUE < i + Integer.MIN_VALUE) {
				j <<= 1;
				j += (a[j + 2] > a[j + 1]) ? 2 : 1;
			}
			if ((j << 1) + 2 == i) {
				j = (j << 1) + 1;
			}
			int na = a[i];
			a[i] = ca;
			ca = na;
			while (j != 0 && a[j] < ca) {
				j = (j - 1) >> 1;
			}
			while (j != 0) {
				na = a[j];
				a[j] = ca;
				ca = na;
				j = (j - 1) >> 1;
			}
		}
		a[0] = ca;
	}

	static void solve() throws Exception {
		int n = scanInt(), m = scanInt();
		int a[] = new int[m], b[] = new int[m], c[] = new int[m];
		int nIn[] = new int[n], out[] = new int[n], nOut[] = new int[n];
		int ans = 0;
		for (int i = 0; i < m; i++) {
			a[i] = scanInt() - 1;
			b[i] = scanInt() - 1;
			c[i] = scanInt() - 1;
			out[a[i]] = i;
			++nOut[a[i]];
			++nIn[b[i]];
			if (a[i] != b[i]) {
				++ans;
			}
		}
		boolean seen[] = new boolean[n];
		boolean onLoop[] = new boolean[n];
		int nLoops = 0;
		for (int i = 0; i < n; i++) {
			if (nOut[i] == 1 && nIn[i] == 1 && b[out[i]] == i) {
				onLoop[i] = true;
				continue;
			}
			for (int j = i; !seen[j]; j = b[out[j]]) {
				seen[j] = true;
				if (nOut[j] != 1 || nIn[j] != 1) {
					break;
				}
				if (b[out[j]] == i) {
					for (j = i;; j = b[out[j]]) {
						onLoop[j] = true;
						if (b[out[j]] == i) {
							break;
						}
					}
					++nLoops;
					break;
				}
			}
		}
		if (nLoops == 0) {
			Main.out.print(ans);
			return;
		}
		long goodExcess = 0;
		int badExcess[] = new int[m], nBadExcess = 0;
		long loopExcess = 0;
		int badLoopExcess[] = new int[m], nBadLoopExcess = 0;
		for (int i = 0; i < m; i++) {
			if (onLoop[a[i]]) {
				if (a[i] == b[i]) {
					if (c[i] > 1) {
						badLoopExcess[nBadLoopExcess++] = c[i] - 1;
					}
				} else {
					loopExcess += c[i];
				}
			} else if (a[i] != b[i]) {
				goodExcess += c[i];
			} else {
				if (c[i] > 0) {
					badExcess[nBadExcess++] = c[i];
				}
			}
		}
		sort(badExcess, nBadExcess);
		ans += nLoops;
		if (goodExcess == 0) {
			if (nBadExcess == 0) {
				Main.out.print(-1);
				return;
			}
			goodExcess += badExcess[--nBadExcess];
			++ans;
		}
		goodExcess += loopExcess;
		int nBadExcessesCopy = nBadExcess;
		sort(badLoopExcess, nBadLoopExcess);
		while (goodExcess < nLoops) {
			if (nBadExcess == 0 && nBadLoopExcess == 0) {
				Main.out.print(-1);
				return;
			}
			if (nBadExcess > 0) {
				goodExcess += badExcess[--nBadExcess];
				++ans;
			} else {
				goodExcess += badLoopExcess[--nBadLoopExcess];
				ans += 2;
			}
		}
		int cans = ans;
		while (nBadExcess < nBadExcessesCopy || nBadLoopExcess > 0) {
			if (nBadExcess < nBadExcessesCopy && goodExcess - badExcess[nBadExcess] >= nLoops) {
				goodExcess -= badExcess[nBadExcess++];
				ans = min(ans, --cans);
			} else if (nBadLoopExcess > 0) {
				goodExcess += badLoopExcess[--nBadLoopExcess];
				cans += 2;
			} else {
				break;
			}
		}
		Main.out.print(ans);
	}

	static int scanInt() throws IOException {
		return parseInt(scanString());
	}

	static long scanLong() throws IOException {
		return parseLong(scanString());
	}

	static String scanString() throws IOException {
		while (tok == null || !tok.hasMoreTokens()) {
			tok = new StringTokenizer(in.readLine());
		}
		return tok.nextToken();
	}

	static BufferedReader in;
	static PrintWriter out;
	static StringTokenizer tok;

	public static void main(String[] args) {
		try {
			in = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(System.out);
			solve();
			in.close();
			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
			exit(1);
		}
	}
}

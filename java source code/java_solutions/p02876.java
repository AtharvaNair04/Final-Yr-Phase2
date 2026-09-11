import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.max;
import static java.lang.System.exit;
import static java.util.Arrays.binarySearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {

	static void sortBy(int a[], int n, int v[]) {
		if (n == 0) {
			return;
		}
		for (int i = 1; i < n; i++) {
			int j = i;
			int ca = a[i];
			int cv = v[ca];
			do {
				int nj = (j - 1) >> 1;
				int na = a[nj];
				if (cv <= v[na]) {
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
				j += (v[a[j + 2]] > v[a[j + 1]]) ? 2 : 1;
			}
			if ((j << 1) + 2 == i) {
				j = (j << 1) + 1;
			}
			int na = a[i];
			a[i] = ca;
			ca = na;
			int cv = v[ca];
			while (j != 0 && v[a[j]] < cv) {
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

	static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	static void solve() throws Exception {
		int n = scanInt();
		int a[] = new int[n], b[] = new int[n], ab[] = new int[n], idx[] = new int[n];
		long sumA = 0;
		for (int i = 0; i < n; i++) {
			a[i] = scanInt();
			b[i] = scanInt();
			ab[i] = -max(a[i], b[i]);
			idx[i] = i;
			sumA += a[i];
		}
		sortBy(idx, n, ab);
		long sums[] = new long[n + 1];
		for (int i = 0; i < n; i++) {
			sums[i + 1] = sums[i] - ab[idx[i]];
		}
		int ansInt = n, ansNum = 0, ansDen = 1;
//		System.err.println("ANS " + ansInt + " " + ansNum + "/" + ansDen);
		int jj = binarySearch(sums, sumA);
		if (jj >= 0) {
			ansInt = jj;
		}
		for (int i = 0; i < n; i++) {
			int j = binarySearch(sums, sumA - ab[idx[i]] - b[idx[i]]);
			if (j < 0) {
				j = ~j;
			}
			if (j <= i) {
				j = binarySearch(sums, sumA - b[idx[i]]);
				if (j < 0) {
					j = ~j;
				}
			}
			if (j == n + 1) {
				continue;
			}
			long csum = j <= i ? sums[j] : sums[j] + ab[idx[i]];
			if (csum + b[idx[i]] >= sumA) {
				int nansInt = j <= i ? j : j - 1, nansNum = (int) (sumA - csum), nansDen = b[idx[i]];
				if (nansNum == nansDen) {
					++nansInt;
					nansNum = 0;
				}
//				System.err.println("NANS " + nansInt + " " + nansNum + "/" + nansDen);
				if (nansInt < ansInt || (nansInt == ansInt && (long) nansNum * ansDen < (long) ansNum * nansDen)) {
					ansInt = nansInt;
					ansNum = nansNum;
					ansDen = nansDen;
				}
			}
		}
		long resDen = (long) n * ansDen;
		long resNum = resDen - (long) ansInt * ansDen - ansNum;
		long gcd = gcd(resNum, resDen);
		out.print(resNum / gcd + " " + resDen / gcd);
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

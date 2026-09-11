import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		FastScanner in = new FastScanner(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		ProblemD solver = new ProblemD();
		solver.solve(1, in, out);
		out.close();
	}

	static class ProblemD {
		public void solve(int testNumber, FastScanner in, PrintWriter out) {
			int n = in.nextInt();
			int k = in.nextInt();
			int[][] sb = new int[2 * k + 1][2 * k + 1];
			int[][] sw = new int[2 * k + 1][2 * k + 1];
			for (int i = 0; i < n; i++) {
				int x = in.nextInt();
				int y = in.nextInt();
				char c = in.next().charAt(0);
				x %= 2 * k;
				y %= 2 * k;
				++x;
				++y;
				if (c == 'B') {
					++sb[x][y];
				} else {
					++sw[x][y];
				}
			}

			for (int[][] s : new int[][][]{sb, sw}) {
				for (int i = 1; i <= 2 * k; i++) {
					for (int j = 1; j <= 2 * k; j++) {
						s[i][j] += s[i - 1][j];
						s[i][j] += s[i][j - 1];
						s[i][j] -= s[i - 1][j - 1];
					}
				}
			}

			int ans = 0;
			for (int di = 0; di < 2 * k; di++) {
				for (int dj = 0; dj < 2 * k; dj++) {
					int cur = 0;
					for (int bigI = -2; bigI <= 2; bigI++) {
						for (int bigJ = -2; bigJ <= 2; bigJ++) {
							int i1 = bigI * k + di + 1;
							int i2 = i1 + k - 1;
							int j1 = bigJ * k + dj + 1;
							int j2 = j1 + k - 1;
							int[][] s;
							if ((bigI + bigJ) % 2 == 0) {
								s = sb;
							} else {
								s = sw;
							}
							if (i1 > 2 * k) {
								continue;
							}
							i1 = Math.max(i1, 1);
							if (i2 < 1) {
								continue;
							}
							i2 = Math.min(i2, 2 * k);
							if (j1 > 2 * k) {
								continue;
							}
							j1 = Math.max(j1, 1);
							if (j2 < 1) {
								continue;
							}
							j2 = Math.min(j2, 2 * k);
							if (i1 <= i2 && j1 <= j2) {
								--i1;
								--j1;
								cur += s[i2][j2];
								cur -= s[i2][j1];
								cur -= s[i1][j2];
								cur += s[i1][j1];
							}
						}
					}
					ans = Math.max(ans, cur);
				}
			}
			out.println(ans);
		}

	}

	static class FastScanner {
		private BufferedReader in;
		private StringTokenizer st;

		public FastScanner(InputStream stream) {
			in = new BufferedReader(new InputStreamReader(stream));
		}

		public String next() {
			while (st == null || !st.hasMoreTokens()) {
				try {
					st = new StringTokenizer(in.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

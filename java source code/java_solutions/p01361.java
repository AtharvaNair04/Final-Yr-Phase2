import java.util.*;
import java.lang.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	void run() {
		for (;;) {
			int hpi = sc.nextInt();
			int hpm = sc.nextInt();

			if ((hpi | hpm) == 0) {
				break;
			}

			int h = sc.nextInt();
			int w = sc.nextInt();

			char mtemp[][] = new char[h][];
			int map[][] = new int[h][w];

			for (int i = 0; i < h; i++) {
				mtemp[i] = sc.next().toCharArray();
			}

			int chm[] = new int[256];
			int T = sc.nextInt();

			for (int i = 0; i < T; i++) {
				chm[sc.next().charAt(0)] = sc.nextInt();
			}

			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					map[i][j] = chm[mtemp[i][j]];
				}
			}

			int S = sc.nextInt();

			int[] chp = new int[256];

			int[] dx = { 1, -1, 0, 0 };
			int[] dy = { 0, 0, 1, -1 };

			chp['R'] = 0;
			chp['L'] = 1;
			chp['D'] = 2;
			chp['U'] = 3;

			int d[] = new int[S];
			int s[] = new int[S];

			int t = 0;
			for (int i = 0; i < S; i++) {
				d[i] = chp[sc.next().charAt(0)];
				s[i] = sc.nextInt();
				t += s[i];
			}

			int P = sc.nextInt();
			int p[] = new int[P];
			for (int i = 0; i < P; i++) {
				p[i] = sc.nextInt();
			}

			int trap[] = new int[t + 1];

			int index = 0;
			trap[0] = 0;
			int ii = 0;
			int jj = 0;
			for (int i = 0; i < t;) {
				for (int j = 0; j < s[index]; j++, i++) {
					ii += dy[d[index]];
					jj += dx[d[index]];
					trap[i + 1] = map[ii][jj] + trap[i];
				}
				index++;
			}

			int[][] dp = new int[t + 1][1 << P];

			for (int i = 0; i < t + 1; i++) {
				Arrays.fill(dp[i], -Integer.MAX_VALUE);
			}
			Arrays.fill(dp[0], hpi);

			for (int i = 0; i < t; i++) {
				for (int j = 0; j < 1 << P; j++) {
					if (dp[i][j] > trap[i]) {
						dp[i + 1][j] = Math.max(dp[i + 1][j],
								Math.min(dp[i][j], hpm + trap[i]));
						for (int k = 0; k < P; k++) {
							int shk = 1 << k;
							if ((shk & j) != 0) {
								continue;
							}
							dp[i + 1][j | shk] = Math
									.max(dp[i + 1][j | shk],
											Math.min(dp[i][j] + p[k], hpm
													+ trap[i]));
						}
					}
					// dp[i][j] -= trap[i];
				}
				// System.out.println(trap[i] + " " + Arrays.toString(dp[i]));
			}

			boolean ok = false;
			for (int i = 0; i < 1 << P; i++) {
				if (dp[t][i] > trap[t]) {
					ok = true;
				}
			}
			System.out.println(ok ? "YES" : "NO");
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}
}

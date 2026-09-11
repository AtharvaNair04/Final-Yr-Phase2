import java.util.*;

class Main {
	static int N;
	static int[] low, high;
	static boolean[] f, s;
	static int[] pow = new int[8];

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		pow[0] = 1;
		for (int i = 1; i < pow.length; ++i) {
			pow[i] = pow[i - 1] * 10;
		}
		while (true) {
			int maxLen = 0;
			N = sc.nextInt();
			if (N == 0)
				break;
			low = new int[N];
			high = new int[N];
			for (int i = 0; i < N; ++i) {
				low[i] = sc.nextInt();
				high[i] = sc.nextInt();
				if (high[i] >= 10)
					maxLen += 2;
				else
					maxLen += 1;
			}
			f = new boolean[pow[6]];
			s = new boolean[pow[6]];
			dfs(0, N / 2, f, 0);
			dfs(N / 2, N, s, 0);
			long ans = 0;
			for (int L = N; L <= maxLen; ++L) {
				ans += solve(L);
			}
			System.out.println(ans);
		}
	}

	static long solve(int length) {
		int fFix = Math.max(length - (N - N / 2) * 2, N / 2);
		int sFix = Math.max(length - N / 2 * 2, N - N / 2);
		int middleLen = length - fFix - sFix;
		if (middleLen == 0) {
			long sc = 0, fc = 0;
			for (int i = pow[fFix - 1]; i < pow[fFix]; ++i) {
				if (f[i])
					++sc;
			}
			for (int i = pow[sFix - 1]; i < pow[sFix]; ++i) {
				if (s[i])
					++fc;
			}
			return fc * sc;

		} else {
			long ret = 0;
			for (int middle = 0; middle < pow[middleLen]; ++middle) {
				boolean[][] fok = new boolean[middleLen + 1][pow[fFix]];
				boolean[][] sok = new boolean[middleLen + 1][pow[sFix]];
				for (int i = 0; i <= middleLen; ++i) {
					int add = pow[i];
					int res = middle / pow[middleLen - i];
					for (int cur = res + pow[fFix + i - 1]; cur < pow[fFix + i]; cur += add) {
						if (f[cur])
							fok[i][cur / pow[i]] = true;
					}
				}
				for (int i = 0; i <= middleLen; ++i) {
					if (i > 0 && (middle % pow[i] / pow[i - 1] == 0)) {// leading0
						continue;
					}
					int res = middle % pow[i] * pow[sFix];
					for (int cur = res + (i == 0 ? pow[sFix - 1] : 0); cur < res + pow[sFix]; cur++) {
						if (s[cur])
							sok[middleLen - i][cur % pow[sFix]] = true;
					}
				}
				for (int i = 1; i < (1 << (middleLen + 1)); ++i) {
					long fc = 0;
					long sc = 0;
					out1: for (int j = pow[fFix - 1]; j < pow[fFix]; ++j) {
						for (int k = 0; k < (1 + middleLen); ++k) {
							if ((i & (1 << k)) > 0 && !fok[k][j]) {
								continue out1;
							}
						}
						++fc;
					}
					out2: for (int j = 0; j < pow[sFix]; ++j) {
						for (int k = 0; k < (1 + middleLen); ++k) {
							if ((i & (1 << k)) > 0 && !sok[k][j]) {
								continue out2;
							}
						}
						++sc;
					}
					ret += fc * sc * (Integer.bitCount(i) % 2 == 1 ? 1 : -1);
				}
			}
			return ret;
		}
	}

	static void dfs(int curIdx, int endIdx, boolean[] arr, int sum) {
		if (curIdx == endIdx) {
			arr[sum] = true;
			return;
		}
		for (int i = low[curIdx]; i <= Math.min(9, high[curIdx]); ++i) {
			dfs(curIdx + 1, endIdx, arr, sum * 10 + i);
		}
		for (int i = Math.max(10, low[curIdx]); i <= high[curIdx]; ++i) {
			dfs(curIdx + 1, endIdx, arr, sum * 100 + i);
		}
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

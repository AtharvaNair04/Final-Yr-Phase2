import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	boolean check(TreeSet<int[]> C, int[] S) {
		int[] Sl = C.lower(S);
		if (Sl != null && Sl[1] < S[1]) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	void solve(int N, int[][] S) {
		TreeSet<int[]>[] C = new TreeSet[N];
		for (int i = 0; i < N; ++i) {
			C[i] = new TreeSet<>(new Comparator<int[]>() {
				@Override
				public int compare(int[] o1, int[] o2) {
					if (o1[0] != o2[0]) {
						return Integer.compare(o1[0], o2[0]);
					} else {
						return -Integer.compare(o1[1], o2[1]);
					}
				}
			});
		}
		C[0].add(S[0]);
		int maxLen = 1;
		for (int i = 1; i < N; ++i) {
			int left = -1;
			int right = maxLen;
			while (right - left > 1) {
				int middle = (right + left) / 2;
				if (check(C[middle], S[i])) {
					left = middle;
				} else {
					right = middle;
				}
			}

			// C[left+1]???S???????????????
			if (left + 1 == maxLen) {
				C[left + 1].add(S[i]);
				++maxLen;
				continue;
			} else {
				int[] tmp = C[left + 1].floor(S[i]);
				if (tmp != null && tmp[1] == S[i][1])
					continue;
				tmp = C[left + 1].higher(S[i]);
				while (tmp != null && S[i][1] <= tmp[1]) {
					C[left + 1].remove(tmp);
					tmp = C[left + 1].higher(S[i]);
				}
				C[left + 1].add(S[i]);
			}
		}
		System.out.println(maxLen);
	}

	int nextA(int a, int M) {
		return 36969 * (a & M) + (a >> 16);
	}

	int nextB(int b, int M) {
		return 18000 * (b & M) + (b >> 16);
	}

	int nextVal(int a, int b, int C) {
		return (C & ((a << 16) + b)) % 1000000;
	}

	void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			int m = sc.nextInt();
			int n = sc.nextInt();
			int A = sc.nextInt();
			int B = sc.nextInt();
			if (m == 0 && n == 0 && A == 0 && B == 0)
				break;
			int[][] S = new int[m + n][3];
			for (int i = 0; i < m; ++i) {
				S[i][0] = sc.nextInt();
				S[i][1] = sc.nextInt();
				S[i][2] = sc.nextInt();
			}
			int a = A, b = B, C = ~(1 << 31), M = (1 << 16) - 1;
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < 3; ++j) {
					a = nextA(a, M);
					b = nextB(b, M);
					S[i + m][j] = nextVal(a, b, C);
				}
			}
			Arrays.sort(S, new Comparator<int[]>() {
				@Override
				public int compare(int[] o1, int[] o2) {
					if (o1[0] != o2[0])
						return Integer.compare(o1[0], o2[0]);
					else if (o1[1] != o2[1]) {
						return -Integer.compare(o1[1], o2[1]);
					} else {
						return -Integer.compare(o1[2], o2[2]);
					}
				}
			});

			int[][] tmp = new int[m + n][2];
			for (int i = 0; i < m + n; ++i) {
				tmp[i][0] = S[i][1];
				tmp[i][1] = S[i][2];
			}
			solve(m + n, tmp);
		}
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

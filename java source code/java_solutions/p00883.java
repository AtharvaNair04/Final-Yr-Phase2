import java.util.*;
import java.lang.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);
	// int map[][];

	int n;

	class S implements Comparable<S> {
		int value;
		int cost;

		public int compareTo(S tar) {
			return cost - tar.cost;
		}

		S(int value, int cost) {
			this.value = value;
			this.cost = cost;
		}
	}

	boolean goaled(int arg) {
		int v2 = arg >> 6;
		int bc = Integer.bitCount(v2);
		return bc == n * n - 1;
	}

	int[][] encode(int arg) {
		int[][] ret = new int[n][n];

		int px = arg & 7;
		arg = arg >> 3;
		int py = arg & 7;
		arg = arg >> 3;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				ret[n - i - 1][n - j - 1] = ((arg & 1) == 1 ? '.' : '#');
				arg = arg >> 1;
			}
		}
		ret[py][px] = '@';
		return ret;
	}

	int decode(int map[][]) {
		int ret = 0;
		int py = -1;
		int px = -1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				ret = ret << 1;
				ret += map[i][j] == '.' ? 1 : 0;
				if (map[i][j] == '@') {
					py = i;
					px = j;
				}
			}
		}
		ret = (((ret << 3) + py) << 3) + px;
		// System.out.println(py+" "+px+" "+ Integer.toBinaryString(ret));
		return ret;
	}

	int[][] infect(int m2[][]) {
		int k[][] = new int[n][n];

		int next[][] = new int[n][n];
		for (int ny = 0; ny < n; ny++) {
			for (int nx = 0; nx < n; nx++) {
				if (m2[ny][nx] == '@') {
					next[ny][nx] = '@';
					continue;
				}
				for (int i = ny - 1; i < ny + 2; i++) {
					if (i < 0) {
						continue;
					}
					if (i >= n) {
						continue;
					}
					for (int j = nx - 1; j < nx + 2; j++) {
						if (i == ny && j == nx) {
							continue;
						}
						if (j < 0) {
							continue;
						}
						if (j >= n) {
							continue;
						}
						if (m2[i][j] != '.') {
							k[ny][nx]++;
						}
					}
				}
				if (k[ny][nx] == 3 || (m2[ny][nx] == '#' && k[ny][nx] == 2)) {
					next[ny][nx] = '#';
				} else {
					next[ny][nx] = '.';
				}
			}
		}
		return next;
	}

	void run() {
		for (;;) {
			n = sc.nextInt();

			if (n == 0) {
				break;
			}

			int[][] map = new int[n][n];

			int py = -1;
			int px = -1;
			for (int i = 0; i < n; i++) {
				String buffer = sc.next();
				for (int j = 0; j < n; j++) {
					map[i][j] = buffer.charAt(j);
					if (map[i][j] == '@') {
						py = i;
						px = j;
					}
				}
			}
			TreeSet<Integer> used = new TreeSet<Integer>();
			PriorityQueue<S> q = new PriorityQueue<S>();

			q.add(new S(decode(map), 0));
			for (;;) {
				if (q.isEmpty()) {
					System.out.println(-1);
					break;
				}

				S st = q.poll();
				if (used.contains(st.value)) {
					continue;
				}
				used.add(st.value);
				// System.out.println(Arrays.deepToString(encode(st.value)));
				// System.out.println();
				if (goaled(st.value)) {
					System.out.println(st.cost);
					break;
				}

				int nx = st.value & 7;
				int ny = (st.value >> 3) & 7;

				for (int i = ny - 1; i < ny + 2; i++) {
					if (i < 0) {
						continue;
					}
					if (i >= n) {
						continue;
					}
					for (int j = nx - 1; j < nx + 2; j++) {
						if (i == ny && j == nx) {
							continue;
						}
						if (j < 0) {
							continue;
						}
						if (j >= n) {
							continue;
						}
						// System.out.println("here");
						int[][] m2 = encode(st.value);
						if (m2[i][j] == '.') {
							m2[i][j] = '@';
							m2[ny][nx] = '.';
							// System.out.println(Arrays.deepToString(m2));
							int[][] n2 = infect(m2);
							int next = decode(n2);
							if (!used.contains(next))
								q.add(new S(next, st.cost + 1));
						}
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}
}

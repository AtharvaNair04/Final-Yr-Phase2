import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class Main {
	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	int H, W;
	char[][] map;

	void run() {
		Scanner sc = new Scanner(System.in);
		H = sc.nextInt();
		W = sc.nextInt();
		map = new char[H][W];
		for (int i = 0; i < H; ++i) {
			map[i] = sc.next().toCharArray();
		}
		if (W == 1) {
			int ans = H;
			for (int i = 1; i < H - 1; ++i) {
				if (map[i][0] == '.') {
					System.out.println(-1);
					return;
				}
			}
			if (map[0][0] == '.')
				--ans;
			if (H > 1 && map[H - 1][0] == '.')
				--ans;
			if (H == 2 && map[0][0] == '.' && map[1][0] == '.') {
				System.out.println(-1);
				return;
			}
			System.out.println(ans);
			return;
		} else if (H == 1) {
			int ans = W;
			for (int i = 1; i < W - 1; ++i) {
				if (map[0][i] == '.') {
					System.out.println(-1);
					return;
				}
			}
			if (map[0][0] == '.')
				--ans;
			if (W > 1 && map[0][W - 1] == '.')
				--ans;
			if (W == 2 && map[0][0] == '.' && map[0][1] == '.') {
				System.out.println(-1);
				return;
			}
			System.out.println(ans);
			return;
		} else {
			int[] state = new int[W];
			Arrays.fill(state, -1);
			System.out.println(Math.max(f(0, 0, state), -1));
		}
	}

	HashMap<List<Integer>, Integer> mem = new HashMap<>();

	int f(int w, int h, int[] state) {
		if (h == H) {
			for (int i = 0; i < state.length; ++i) {
				if (state[i] > 1) {
					return -Integer.MAX_VALUE / 16;
				}
			}
			return 0;
		}
		if (mem.containsKey(toList(state, w, h))) {
			return mem.get(toList(state, w, h));
		}
		int ret = -Integer.MAX_VALUE / 16;
		int nw = (w + 1) % W;
		int nh = (nw == 0 ? 1 : 0) + h;
		if (h(w, h, state, 0)) {
			ret = Math.max(ret, f(nw, nh, g(w, h, state, 0)));
		}
		if (h(w, h, state, 1)) {
			ret = Math.max(ret, 1 + f(nw, nh, g(w, h, state, 1)));
		}
		mem.put(toList(state, w, h), ret);
		return ret;
	}

	int[] g(int w, int h, int[] state, int col) {
		int[] ret = new int[state.length];
		for (int i = 0; i < state.length; ++i) {
			ret[i] = state[i];
		}
		if (col == 0) {
			ret[w] = 0;
			ret = reduct(ret);
			return ret;
		} else {
			for (int i = 0; i < ret.length; ++i) {
				if (i == w - 1 || i == w)
					continue;
				if (w > 0 && ret[w - 1] > 0 && ret[w - 1] == ret[i]) {
					ret[i] = 99;
				} else if (ret[w] > 0 && ret[i] == ret[w]) {
					ret[i] = 99;
				}
			}
			if (w > 0 && ret[w - 1] > 0) {
				ret[w - 1] = 99;
			}
			ret[w] = 99;
			ret = reduct(ret);
			return ret;
		}
	}

	// col=0:???
	// col=1:???
	// (w,h)???col??§???????????????????????????
	boolean h(int w, int h, int[] state, int col) {
		if (col == 0 && map[h][w] == '#') {
			return false;
		}
		if (col == 1 && map[h][w] == '.') {
			return false;
		}
		if (col == 0) {
			if ((w > 0 && state[w - 1] == 0) || state[w] == 0) {
				return false;
			} else {
				if (state[w] > 0) {
					for (int i = 0; i < state.length; ++i) {
						if (i == w)
							continue;
						if (state[i] == state[w]) {
							return true;
						}
					}
					return false;
				}
				return true;
			}
		} else if (col == 1) {
			if (w > 0 && state[w - 1] > 0 && state[w] > 0 && state[w - 1] == state[w]) {
				return false;
			} else {
				return true;
			}
		} else {
			throw new AssertionError();
		}
	}

	int[] reduct(int[] state) {
		int[] ret = new int[state.length];
		boolean[] vis = new boolean[state.length];
		for (int i = 0; i < state.length; ++i) {
			if (state[i] == -1 || state[i] == 0) {
				ret[i] = state[i];
				vis[i] = true;
			}
		}
		int id = 1;
		for (int i = 0; i < state.length; ++i) {
			if (state[i] > 0 && !vis[i]) {
				for (int j = 0; j < state.length; ++j) {
					if (state[i] == state[j]) {
						vis[j] = true;
						ret[j] = id;
					}
				}
				++id;
			}
		}
		return ret;
	}

	List<Integer> toList(int[] arr, int w, int h) {
		List<Integer> lis = new ArrayList<>();
		lis.add(h);
		lis.add(w);
		for (int i = 0; i < arr.length; ++i) {
			lis.add(arr[i]);
		}
		return lis;
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

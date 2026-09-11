import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {

	static int[] dx = new int[] { 1, 0, -1, 0 };
	static int[] dy = new int[] { 0, 1, 0, -1 };

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int TESTCASE = 0;
		outer: while (++TESTCASE > 0) {
			System.gc();
			int W = sc.nextInt();
			int H = sc.nextInt();
			if (W == 0 && H == 0)
				break;
			char[][] map = new char[H][W];
			int sh = -1, sw = -1, gh = -1, gw = -1;
			for (int i = 0; i < H; ++i) {
				map[i] = sc.next().toCharArray();
				for (int j = 0; j < W; ++j) {
					if (map[i][j] == 'K') {
						sh = i;
						sw = j;
					} else if (map[i][j] == 'M') {
						gh = i;
						gw = j;
					}
				}
			}
			@SuppressWarnings("unchecked")
			ArrayList<Pos>[][][] forward = new ArrayList[H][W][4];
			@SuppressWarnings("unchecked")
			ArrayList<Pos>[][][] backward = new ArrayList[H][W][4];
			for (int i = 0; i < forward.length; ++i) {
				for (int j = 0; j < forward[i].length; ++j) {
					for (int k = 0; k < forward[i][j].length; ++k) {
						forward[i][j][k] = new ArrayList<>();
						backward[i][j][k] = new ArrayList<>();
					}
				}
			}

			for (int h = 1; h < H - 1; ++h) {
				for (int w = 1; w < W - 1; ++w) {
					for (int dir = 0; dir < 4; ++dir) {
						int nh = h;
						int nw = w;
						while (map[nh + dy[dir]][nw + dx[dir]] != '#') {
							nh += dy[dir];
							nw += dx[dir];
						}
						forward[h][w][dir].add(new Pos(nh, nw));
						backward[nh][nw][dir].add(new Pos(h, w));
					}
				}
			}
			HashSet<List> set = new HashSet<>();
			ArrayDeque<P> que = new ArrayDeque<>();
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					for (Pos p1 : forward[gh][gw][i]) {
						for (Pos p2 : backward[gh][gw][j]) {
							if (map[gh + dy[i]][gw + dx[i]] == '#')
								continue;
							que.add(new P(p1.h, p1.w, i, p2.h, p2.w, j));
							set.add(Arrays.asList(gh, gw, i, gh, gw, j));
							set.add(Arrays.asList(p1.h, p1.w, i, p2.h, p2.w, j));
						}
					}
				}
			}
			boolean f = false;
			while (!que.isEmpty()) {
				P p = que.poll();
				for (int rot = -1; rot <= 1; rot += 2) {
					int ndir1 = (p.dir1 + rot + 4) % 4;
					int ndir2 = (p.dir2 + rot + 4) % 4;
					for (Pos p1 : forward[p.h1][p.w1][ndir1]) {
						for (Pos p2 : backward[p.h2][p.w2][ndir2]) {
							if (set.contains(Arrays.asList(p1.h, p1.w, ndir1, p2.h, p2.w, ndir2))) {
								continue;
							}
							que.add(new P(p1.h, p1.w, ndir1, p2.h, p2.w, ndir2));
							set.add(Arrays.asList(p1.h, p1.w, ndir1, p2.h, p2.w, ndir2));
							if (p2.h == sh && p2.w == sw && map[p2.h + dy[ndir2]][p2.w + dx[ndir2]] != '#')
								f = true;
							if (p1.h == sh && p1.w == sw && p2.h == sh && p2.w == sw
									&& map[p2.h + dy[ndir2]][p2.w + dx[ndir2]] != '#') {
								System.out.println("He can accomplish his mission.");
								continue outer;
							}
						}
					}
				}
			}
			if (f)
				System.out.println("He cannot return to the kitchen.");
			else {
				System.out.println("He cannot bring tea to his master.");
			}
		}
	}

	static class P {
		int h1;
		int w1;
		int h2;
		int w2;
		int dir1;
		int dir2;

		public P(int h1, int w1, int dir1, int h2, int w2, int dir2) {
			this.h1 = h1;
			this.w1 = w1;
			this.h2 = h2;
			this.w2 = w2;
			this.dir1 = dir1;
			this.dir2 = dir2;
		}
	}

	static class Pos {
		int h;
		int w;

		public Pos(int h, int w) {
			this.h = h;
			this.w = w;
		}

	}
}

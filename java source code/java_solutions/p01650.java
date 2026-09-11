import java.util.ArrayList;
import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		new Main().solver();
	}

	int UNUSED = 1 << 60;
	boolean[][][][] reachble;

	@SuppressWarnings("unchecked")
	void solver() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			H = sc.nextInt();
			W = sc.nextInt();
			if (H == 0 && W == 0)
				break;
			char[][] table = new char[H][W];
			memo = new int[H][W][H][W];
			for (int i = 0; i < H; i++) {
				for (int j = 0; j < W; j++) {
					for (int k = 0; k < H; k++) {
						for (int l = 0; l < W; l++) {
							memo[i][j][k][l] = UNUSED;
						}
					}
				}
			}
			reachble = new boolean[H][W][H][W];
			for (int y = 0; y < H; y++) {
				for (int x = 0; x < W; x++) {
					if (table[y][x] == '#')
						continue;
					reachble[y][x][y][x] = true;
					if (y + 1 < H && table[y + 1][x] != '#') {
						for (int src_y = 0; src_y <= y + 1; src_y++) {
							for (int src_x = 0; src_x <= x; src_x++) {
								reachble[src_y][src_x][y + 1][x] |= reachble[src_y][src_x][y][x];
							}
						}
					}
					if (x + 1 < W && table[y][x + 1] != '#') {
						for (int src_y = 0; src_y <= y; src_y++) {
							for (int src_x = 0; src_x <= x + 1; src_x++) {
								reachble[src_y][src_x][y][x + 1] |= reachble[src_y][src_x][y][x];
							}
						}
					}
				}
			}
			p = new ArrayList[58];
			for (int i = 0; i < 58; i++) {
				p[i] = new ArrayList<>();
			}
			for (int i = 0; i < H; i++) {
				table[i] = sc.next().toCharArray();
				for (int j = 0; j < W; j++) {
					if (table[i][j] == '.' || table[i][j] == '#')
						continue;
					if (!isLowerCase(table[i][j]))
						p[table[i][j] - 'A'].add(new Coordinate(j, i));
				}
			}
			int d = rec(0, 0, table, W - 1, H - 1);
			System.out.println(d < 0 ? -1 : d);
		}
	}

	ArrayList<Coordinate>[] p;

	class Coordinate {
		int x;
		int y;

		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	int[][][][] memo;
	int H, W;

	int rec(int curX, int curY, char[][] table, int toX, int toY) {
		if (curY < 0 || curX < 0 || curY >= H || curX >= W || toY < curY || toX < curX || table[curY][curX] == '#'
				|| table[toY][toX] == '#' || toX >= W || toY >= H || toX < 0 || toY < 0
				|| !reachble[curY][curX][toY][toX])
			return -(1 << 20);
		if (curY == toY && curX == toX) {
			return 0;
		}
		if (memo[curY][curX][toY][toX] != UNUSED) {
			return memo[curY][curX][toY][toX];
		}
		int ret = -(1 << 20);
		for (int i = 0; i < 2; i++) {
			ret = Math.max(ret, rec(curX + i, curY + (i ^ 1), table, toX, toY));
		}
		if (table[curY][curX] == '.') {
			memo[curY][curX][toY][toX] = ret;
			return ret;
		}
		if (isLowerCase(table[curY][curX])) {
			for (Coordinate P : p[String.valueOf(table[curY][curX]).toUpperCase().charAt(0) - 'A']) {
				if (!reachble[curY][curX][P.y][P.x])
					continue;
				if (P.x < curX || P.y < curY || toX < P.x || toY < P.y)
					continue;
				int tmp = -1 << 20;
				if ((P.x == curX && P.y == curY + 1) || (P.x == curX + 1 && P.y == curY)) {
					tmp = 1;
				} else {
					for (int i = 0; i < 2; i++) {
						for (int j = 0; j < 2; j++) {
							tmp = Math.max(tmp, rec(curX + i, curY + (i ^ 1), table, P.x - j, P.y - (j ^ 1)) + 1);
							// System.out.println(tmp);
						}
					}
				}
				if (toX == P.x && toY == P.y) {
					ret = Math.max(ret, tmp);
				} else {
					for (int i = 0; i < 2; i++) {
						ret = Math.max(ret, tmp + rec(P.x + i, P.y + (i ^ 1), table, toX, toY));
					}
				}
			}
		}
		memo[curY][curX][toY][toX] = ret;
		return ret;
	}

	boolean isLowerCase(char c) {
		if (String.valueOf(c).toLowerCase().charAt(0) - c == 0) {
			return true;
		} else {
			return false;
		}
	}
}

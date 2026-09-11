import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * AI
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;

		Main main = new Main();

		while ((line = br.readLine()) != null && !line.isEmpty()) {

			int H, W;
			H = parseInt(line.substring(0, line.indexOf(' ')));
			W = parseInt(line.substring(line.indexOf(' ') + 1));

			char[][] map = new char[H][W];
			for (int i = 0; i < H; i++) {
				line = br.readLine();
				for (int j = 0; j < W; j++) {
					map[i][j] = line.charAt(j);
				}
			}

			String prg = br.readLine();

			//solve
			Robot robot = main.new Robot(map, prg);
			robot.run();

			System.out.println(robot.elapsed);
		}
	} //end while

	class Robot {
		private final int[] DIRS = new int[]{0, 1, 2, 3};//N, E, S, W
		private final int N = DIRS[0];
		private final int E = DIRS[1];
		private final int S = DIRS[2];
		private final int W = DIRS[3];
		private final int[][] FORWARD = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
		private final int[][] BACKWARD = new int[][]{{1, 0}, {0, -1}, {-1, 0}, {0, 1}};

		//
		char[][] map;
		String prg;
		int y, x;
		int gy, gx;
		int dir;
		int elapsed = 0;
		boolean[][][][] logs;

		Robot(char[][] map, String prg) {
			this.map = map;
			this.prg = prg;
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j] == 's') {
						y = i;
						x = j;
					} else if (map[i][j] == 'g') {
						gy = i;
						gx = j;
					}
				}
			}
			this.dir = N;
			logs = new boolean[50][50][4][1000];
		}

		void run() {
			sub(0, prg.length());
		}

		void sub(int sta, int end) {

			if (elapsed == -1) return;

			int cur = sta;
			for (; cur < end; cur++) {
				char code = prg.charAt(cur);
				switch (code) {
					//action
					case '^':
					case 'v':
					case '<':
					case '>':
						action(code);
						break;
					//control
					case '[':
					case '{':
						int subSta, subEnd = cur + 1;
						String condStr = "";
						boolean condRes = false;

						condStr += prg.charAt(subEnd);
						if (prg.charAt(subEnd) == '~') {
							subEnd++;
							condStr += prg.charAt(subEnd);
						}
						subEnd++;
						subSta = subEnd;
						condRes = cond(condStr);

						Deque<Character> stack = new ArrayDeque<>();
						stack.offer(code);
						while (!stack.isEmpty()) {
							char _code = prg.charAt(subEnd);
							switch (_code) {
								case '[':
								case '{':
									stack.offer(_code);
									break;
								case ']':
								case '}':
									stack.pollLast();
									break;
							}
							subEnd++;
						}
						subEnd--;
						if (condRes) {
							if (code == '[') {
								sub(subSta, subEnd);
							} else if (code == '{') {
								while (condRes) {
									sub(subSta, subEnd);
									condRes = cond(condStr);
									if (y == gy && x == gx) {
										break;
									}
									if (logs[y][x][dir][subSta] && condRes) {
										elapsed = -1;
										break;
									} else {
										logs[y][x][dir][subSta] = true;
									}
								}
							}
						}
						cur = subEnd;
						break;
				}
				if (y == gy && x == gx) break;
				if (elapsed == -1) break;
			}
			if (cur == prg.length()) elapsed = -1;
		}

		void action(char action) {
			switch (action) {
				case '^':
					if (!cond("C")) {
						y += FORWARD[dir][0];
						x += FORWARD[dir][1];
					}
					break;
				case 'v':
					if (!cond("B")) {
						y += BACKWARD[dir][0];
						x += BACKWARD[dir][1];
					}
					break;
				case '<'://L
					dir = (dir - 1 + 4) % 4;
					break;
				case '>'://R
					dir = (dir + 1) % 4;
					break;
			}
			//System.out.println(y + " " + x + " " + elapsed);
			elapsed++;
		}

		boolean cond(String cond) {
			boolean ret = false;
			switch (cond.charAt(cond.length() - 1)) {
				case 'N':
					if (dir == N) ret = true;
					break;
				case 'E':
					if (dir == E) ret = true;
					break;
				case 'S':
					if (dir == S) ret = true;
					break;
				case 'W':
					if (dir == W) ret = true;
					break;
				case 'C':
					int fy = y + FORWARD[dir][0];
					int fx = x + FORWARD[dir][1];
					if (map[fy][fx] == '#') ret = true;
					break;
				case 'B':
					int by = y + BACKWARD[dir][0];
					int bx = x + BACKWARD[dir][1];
					if (map[by][bx] == '#') ret = true;
					break;
				case 'T':
					ret = true;
					break;
			}
			if (cond.charAt(0) == '~') {
				ret = !ret;
			}
			return ret;
		}
	}
} //end main

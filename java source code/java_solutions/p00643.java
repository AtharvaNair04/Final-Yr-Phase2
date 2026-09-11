import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	MyScanner sc = new MyScanner();
	Scanner sc2 = new Scanner(System.in);
	int mod = 1000000007;
	int[] dx = { 1, 0, 0, -1 };
	int[] dy = { 0, 1, -1, 0 };

	void run() {
		for (;;) {
			int h = sc.nextInt();
			int w = sc.nextInt();
			if ((h | w) == 0) {
				return;
			}
			int[][] field = new int[h][w];
			int[][][][] cost = new int[h][w][6][6]
			;
			int cnt = 0;
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					field[i][j] = sc.nextInt();
					for (int k = 0; k < 6; k++) {
							Arrays.fill(cost[i][j][k], Integer.MAX_VALUE);
					}
				}
			}
			int startH = sc.nextInt();
			int startW = sc.nextInt();
			int destH = sc.nextInt();
			int destW = sc.nextInt();
			
			Dice dice = new Dice(1, 2, 3, 5, 4, 6, startH, startW, 0);
			cost[startH][startW][0][1] = 0;
			Queue<Dice> que = new LinkedList<Dice>();
			que.add(dice);
			while (!que.isEmpty()) {
				int size = que.size();
				for (int i = 0; i < size; i++) {
					cnt++;
					Dice now = que.poll();
					for (int j = 0; j < 4; j++) {
						int nextH = now.h + dy[j];
						int nextW = now.w + dx[j];
						if (0 <= nextH && nextH < h && 0 <= nextW && nextW < w) {
							Dice next = new Dice(now.top, now.front, now.right,
									now.back, now.left, now.reverse, nextH,
									nextW, now.cost);
							switch (j) {
							case 0:
								next.roll_right();
								break;
							case 1:
								next.roll_front();
								break;
							case 2:
								next.roll_back();
								break;
							case 3:
								next.roll_left();
								break;
							default:
								break;
							}
							next.cost += field[next.h][next.w] * next.reverse;
							if (next.cost < cost[next.h][next.w][next.top - 1][next.front - 1]) {
								if (j == 0) {
								}
								cost[next.h][next.w][next.top - 1][next.front - 1]= next.cost;
								que.add(next);
							}
						}
					}
				}
			}
			int ans = Integer.MAX_VALUE;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
						ans = Math.min(ans, cost[destH][destW][i][j]);
				}
			}
			System.out.println(ans);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

	class Dice {
		int top;
		int front;
		int right;
		int back;
		int left;
		int reverse;

		int h;
		int w;
		int cost;

		public Dice(int top, int front, int right, int back, int left,
				int reverse, int h, int w, int cost) {
			super();
			this.top = top;
			this.front = front;
			this.right = right;
			this.back = back;
			this.left = left;
			this.reverse = reverse;
			this.h = h;
			this.w = w;
			this.cost = cost;
		}

		void disp() {
			System.out.print(this.top + " " + this.front + " " + this.right
					+ " " + this.back + " " + this.left + " " + this.reverse);
			System.out.println();
		}

		void rotate_parallel() {
			int tmp = this.back;
			this.back = this.right;
			this.right = this.front;
			this.front = this.left;
			this.left = tmp;
		}

		void roll_back() {
			int tmp = this.top;
			this.top = this.front;
			this.front = this.reverse;
			this.reverse = this.back;
			this.back = tmp;
		}

		void roll_front() {
			int tmp = this.top;
			this.top = this.back;
			this.back = this.reverse;
			this.reverse = this.front;
			this.front = tmp;
		}

		void roll_right() {
			int tmp = this.top;
			this.top = this.left;
			this.left = this.reverse;
			this.reverse = this.right;
			this.right = tmp;
		}

		void roll_left() {
			int tmp = this.top;
			this.top = this.right;
			this.right = this.reverse;
			this.reverse = this.left;
			this.left = tmp;
		}

	}

	void debug(Object... o) {
		System.out.println(Arrays.deepToString(o));
	}

	void debug2(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.printf("%3d ", array[i][j]);
			}
			System.out.println();
		}
	}

	class MyScanner {
		int nextInt() {
			try {
				int c = System.in.read();
				while (c != '-' && (c < '0' || '9' < c))
					c = System.in.read();
				if (c == '-')
					return -nextInt();
				int res = 0;
				do {
					res *= 10;
					res += c - '0';
					c = System.in.read();
				} while ('0' <= c && c <= '9');
				return res;
			} catch (Exception e) {
				return -1;
			}
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}

		String next() {
			try {
				StringBuilder res = new StringBuilder("");
				int c = System.in.read();
				while (Character.isWhitespace(c))
					c = System.in.read();
				do {
					res.append((char) c);
				} while (!Character.isWhitespace(c = System.in.read()));
				return res.toString();
			} catch (Exception e) {
				return null;
			}
		}
	}
}

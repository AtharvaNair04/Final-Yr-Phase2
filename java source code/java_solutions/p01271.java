import java.io.IOException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
	int w, h;
	char[][] l, r;
	int[] ldx = { -1, 0, 0, 1 };
	int[] ldy = { 0, -1, 1, 0 };
	int[] rdx = { 1, 0, 0, -1 };
	int[] rdy = { 0, -1, 1, 0 };
	int slx, sly, srx, sry;

	boolean bfs() {
		Queue<Integer> lxque = new LinkedList<Integer>();
		Queue<Integer> lyque = new LinkedList<Integer>();
		Queue<Integer> rxque = new LinkedList<Integer>();
		Queue<Integer> ryque = new LinkedList<Integer>();

		lxque.add(slx);
		lyque.add(sly);
		rxque.add(srx);
		ryque.add(sry);

		boolean[][][][] vis = new boolean[w][h][w][h];

		while (!lxque.isEmpty()) {
			int lx = lxque.poll();
			int ly = lyque.poll();
			int rx = rxque.poll();
			int ry = ryque.poll();

			if (vis[lx][ly][rx][ry]) {
				continue;
			}
			vis[lx][ly][rx][ry] = true;
			if (l[ly][lx] == '%' && r[ry][rx] == '%') {
				return true;
			}
			if (l[ly][lx] == '%' || r[ry][rx] == '%') {
				continue;
			}

			for (int i = 0; i < 4; i++) {
				int nlx = lx + ldx[i];
				int nly = ly + ldy[i];
				int nrx = rx + rdx[i];
				int nry = ry + rdy[i];

				if (nlx < 0 || w <= nlx || nly < 0 || h <= nly || l[nly][nlx] == '#') {
					nlx -= ldx[i];
					nly -= ldy[i];
				}
				if (nrx < 0 || w <= nrx || nry < 0 || h <= nry || r[nry][nrx] == '#') {
					nrx -= rdx[i];
					nry -= rdy[i];
				}
				if (vis[nlx][nly][nrx][nry]) {
					continue;
				}

				lxque.add(nlx);
				lyque.add(nly);
				rxque.add(nrx);
				ryque.add(nry);
			}
		}

		return false;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			w = sc.nextInt();
			h = sc.nextInt();
			if ((w | h) == 0) {
				break;
			}
			l = new char[h][];
			r = new char[h][];
			for (int i = 0; i < h; i++) {
				l[i] = sc.next().toCharArray();
				r[i] = sc.next().toCharArray();
			}
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					if (l[i][j] == 'L') {
						slx = j;
						sly = i;
						l[i][j] = '.';
					}
					if (r[i][j] == 'R') {
						srx = j;
						sry = i;
						r[i][j] = '.';
					}
				}
			}

			System.out.println(bfs() ? "Yes" : "No");
			System.gc();
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

	public void mapDebug(int[][] a) {
		System.out.println("--------map display---------");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.printf("%3d ", a[i][j]);
			}
			System.out.println();
		}
		System.out.println("----------------------------" + '\n');
	}

	class MyScanner {
		int read() {
			try {
				return System.in.read();
			} catch (IOException e) {
				throw new InputMismatchException();
			}
		}

		boolean isSpaceChar(int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		boolean isEndline(int c) {
			return c == '\n' || c == '\r' || c == -1;
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		int[] nextIntArray(int n) {
			int[] array = new int[n];
			for (int i = 0; i < n; i++)
				array[i] = nextInt();
			return array;
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		long[] nextLongArray(int n) {
			long[] array = new long[n];
			for (int i = 0; i < n; i++)
				array[i] = nextLong();
			return array;
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}

		double[] nextDoubleArray(int n) {
			double[] array = new double[n];
			for (int i = 0; i < n; i++)
				array[i] = nextDouble();
			return array;
		}

		String next() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		String[] nextStringArray(int n) {
			String[] array = new String[n];
			for (int i = 0; i < n; i++)
				array[i] = next();

			return array;
		}

		String nextLine() {
			int c = read();
			while (isEndline(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isEndline(c));
			return res.toString();
		}
	}
}

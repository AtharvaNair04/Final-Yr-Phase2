import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		solver();
	}

	@SuppressWarnings("unchecked")
	static void solver() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int w = sc.nextInt();
		int h = sc.nextInt();
		int[] x = new int[n];
		int[] y = new int[n];
		ArrayList<Integer>[] x_ord = new ArrayList[w];
		ArrayList<Integer>[] y_ord = new ArrayList[h];
		for (int i = 0; i < w; i++)
			x_ord[i] = new ArrayList<>();
		for (int i = 0; i < h; i++)
			y_ord[i] = new ArrayList<>();
		int x0 = 0, xw = 0, y0 = 0, yh = 0;
		for (int i = 0; i < n; i++) {
			x[i] = sc.nextInt() - 1;
			y[i] = sc.nextInt() - 1;
			if (x[i] == 0 && x0 == 0)
				x0++;
			else if (x[i] == w - 1 && xw == 0)
				xw++;
			if (y[i] == 0 && y0 == 0)
				y0++;
			else if (y[i] == h - 1 && yh == 0)
				yh++;
			x_ord[x[i]].add(i);
			y_ord[y[i]].add(i);
		}
		int d = Math.max(Math.max(x0, xw), Math.max(y0, yh));

		DJSet ds = new DJSet(n);
		for (int i = 0; i < w; i++) {
			while (!x_ord[i].isEmpty()) {
				for (int j = 0; j < x_ord[i].size(); j++) {
					ds.setUnion(x_ord[i].get(0), x_ord[i].get(j));
				}
				x_ord[i].clear();
			}
		}
		for (int i = 0; i < h; i++) {
			while (!y_ord[i].isEmpty()) {
				for (int j = 0; j < y_ord[i].size(); j++) {
					ds.setUnion(y_ord[i].get(0), y_ord[i].get(j));
				}
				y_ord[i].clear();
			}
		}
		int sum = n - ds.count();
		if (ds.count() == 1) {
			System.out.println(sum);
		} else {
			sum +=2 * ds.count() - 1 - d;
			System.out.println(sum);
		}

	}

	public static class DJSet {
		int n;// the number of vertices
		int[] d;

		DJSet(int n) {
			this.n = n;
			d = new int[n];
			Arrays.fill(d, -1);
		}

		int root(int x) {
			return d[x] < 0 ? x : root(d[x]);
		}

		boolean setUnion(int x, int y) {
			x = root(x);
			y = root(y);
			if (x != y) {
				if (x < y) {
					int d = x;
					x = y;
					y = d;
				}
				// x>y
				d[y] += d[x];
				d[x] = y;
			}
			return x != y;
		}

		boolean equiv(int x, int y) {
			return root(x) == root(y);
		}

		// the number of nodes of which graph contains x
		int size(int x) {
			return d[root(x)] * (-1);
		}

		// the number of connected graphs
		int count() {
			int ct = 0;
			for (int u : d) {
				if (u < 0)
					ct++;
			}
			return ct;
		}
	}
}

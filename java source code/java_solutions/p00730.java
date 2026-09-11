import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;

public class Main {
	int n, w, h;

	class Cake implements Comparable<Cake>{
		int id;
		int w, h;

		Cake(int id, int w, int h) {
			this.id = id;
			this.w = w;
			this.h = h;
		}

		@Override
		public int compareTo(Cake o) {
			return this.id - o.id;
		}

		@Override
		public String toString() {
			//			return "(" + id + ", " + w + ", " + h + ")";
			return "(" + w + "," + h + ")";
		}
	}

	Cake[] cut(Cake c, int s) {
		s %= (c.w + c.h);

		Cake[] res = new Cake[2];
		if (s < c.w) {
			Cake a = new Cake(10001, s, c.h);
			Cake b = new Cake(10002, c.w - s, c.h);
			if (a.w * a.h < b.w * b.h) {
				res[0] = a;
				res[1] = b;
			} else {
				res[0] = b;
				res[1] = a;
				res[1].id += 100;
			}
		} else {
			Cake a = new Cake(10001, c.w, s - c.w);
			Cake b = new Cake(10002, c.w, c.w + c.h - s);
			if (a.w * a.h < b.w * b.h) {
				res[0] = a;
				res[1] = b;
			} else {
				res[0] = b;
				res[1] = a;
				res[1].id += 100;
			}
		}

		return res;
	}

	void run() {
		MyScanner sc = new MyScanner();

		while (true) {
			n = sc.nextInt();
			w = sc.nextInt();
			h = sc.nextInt();
			if ((n | w | h) == 0) {
				break;
			}

			List<Cake> list = new LinkedList<Cake>();
			list.add(new Cake(0, w, h));

			for (int i = 0; i < n; i++) {
				int p = sc.nextInt() - 1;
				int s = sc.nextInt();

				Collections.sort(list);
				Cake c = list.get(p);
				list.remove(p);
				Cake[] d = cut(c, s);
				list.add(d[0]);
				list.add(d[1]);
				Collections.sort(list);

				Cake[] e = list.toArray(new Cake[0]);
				for (int j = 0; j < e.length; j++) {
					e[j].id = j;
				}
				list.clear();
				for (int j = 0; j < e.length; j++) {
					list.add(e[j]);
				}

				//				System.out.println("-----------------------------");
				//				System.out.println(list);
			}

			Collections.sort(list, new Comparator<Cake>() {
				@Override
				public int compare(Cake o1, Cake o2) {
					int a = o1.w*o1.h;
					int b = o2.w*o2.h;
					return a - b;
				}
			});
			String tab = "";
			for (Cake C: list) {
				System.out.print(tab + C.w*C.h);
				tab = " ";
			}
			System.out.println();
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

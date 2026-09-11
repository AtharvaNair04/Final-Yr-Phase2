import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	void run() {
		Scanner sc = new Scanner(System.in);

		int tc = sc.nextInt();
		sc.nextLine();
		for (int T = 0; T < tc; T++) {
			StringBuilder str = new StringBuilder(sc.nextLine());
			int n = sc.nextInt();
			sc.nextLine();
			int id = 0;
			for (int i = 0; i < n; i++) {
				String s = sc.nextLine();

				if (s.equals("forward char")) {
					id = Math.min(id + 1, str.length());
				}
				if (s.equals("backward char")) {
					id = Math.max(id - 1, 0);
				}
				if (s.equals("forward word")) {
					while (id < str.length() && str.charAt(id) == ' ') {
						id++;
					}
					while (id < str.length() && str.charAt(id) != ' ') {
						id++;
					}
				}
				if (s.equals("backward word")) {
					while (0 < id && str.charAt(id - 1) == ' ') {
						id--;
					}
					while (0 < id && str.charAt(id - 1) != ' ') {
						id--;
					}
				}
				if (s.charAt(0) == 'i') {
					int len = s.length() - 9;
					char[] c = new char[len];
					for (int j = 0; j < len; j++) {
						c[j] = s.charAt(8 + j);
					}
					str.insert(id, c);
					id += len;
				}
				if (s.equals("delete char")) {
					if (id < str.length()) {
						str.deleteCharAt(id);
					}
				}
				if (s.equals("delete word")) {
					boolean f = false;
					int idid = id;
					while (idid < str.length() && str.charAt(idid) == ' ') {
						idid++;
					}
					if (idid < str.length() && str.charAt(idid) != ' ') {
						f = true;
					}
					if (f) {
						while (id < str.length() && str.charAt(id) == ' ') {
							str.deleteCharAt(id);
						}
						while (id < str.length() && str.charAt(id) != ' ') {
							str.deleteCharAt(id);
						}
					}
				}
				//---------------------------------------------------
				//				char[] c = str.toString().toCharArray();
				//				int m = c.length;
				//				boolean f = false;
				//				//				System.out.print(i + ": ");
				//				for (int j = 0; j < m; j++) {
				//					if (j == id) {
				//						System.out.print("^");
				//						f = true;
				//					}
				//					System.out.print(c[j]);
				//				}
				//				if (!f) {
				//					System.out.print("^");
				//				}
				//				System.out.println();
				//---------------------------------------------------
			}
			char[] c = str.toString().toCharArray();
			n = c.length;
			boolean f = false;
			for (int i = 0; i < n; i++) {
				if (i == id) {
					System.out.print("^");
					f = true;
				}
				System.out.print(c[i]);
			}
			if (!f) {
				System.out.print("^");
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

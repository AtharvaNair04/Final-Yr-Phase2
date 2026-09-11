import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Main {
	FastScanner in = new FastScanner(System.in);
	PrintWriter out = new PrintWriter(System.out);

	class Permutation {
		int size;
		
		Set<String> hash;
		
		void dfs(ArrayList<Integer> rest, StringBuilder now) {
			if (rest.size() == 0) {
				hash.add(now.toString());
				return;
			}
			
			int num = rest.size();
			for (int i = 0; i < num; i++) {
				int next = rest.get(i);
				rest.remove(i);
				now.append(next);
				dfs(rest, now);
				now.deleteCharAt(now.length() - 1);
				rest.add(i, next);
			}
		}
		
		public Permutation(int size) {
			this.size = size;
			
			hash = new HashSet<String>();
			
			ArrayList<Integer> rest = new ArrayList<Integer>();
			for (int i = 0; i < size; i++) {
				rest.add(i);
			}
			dfs(rest, new StringBuilder());
			/*
			String origin = "";
			for (int i = 0; i < size; i++) {
				origin += i;
			}

			Queue<String> queue = new LinkedList<String>();
			setHashSet();
			
			queue.add(origin);
			hash.add(origin);
			while (!queue.isEmpty()) {
				char[] next = queue.poll().toCharArray();
				for (int i = 0; i < size - 1; i++) {
					swap(next, i, i + 1);
					String s = String.valueOf(next);
					if (!hash.contains(s)) {
						queue.add(s);
						hash.add(s);
					}
					swap(next, i, i + 1);
				}
			}
			*/
			
		}
		
		void setHashSet() {
			hash = new HashSet<String>();
		}
	
		void swap(char[] array, int x, int y) {
			char t = array[x];
			array[x] = array[y];
			array[y] = t;
		}
		
		Set<String> getPermutationSet() {
			return hash;
		}
	}

	boolean[] isHead;
	long[] rate;
	
	long dfs(long now, int idx, ArrayList<Integer> rest) {
		if (idx == -1) {
			if (now == 0) {
				return 1;
			} else {
				return 0;
			}
		}
		
		int size = rest.size();
		long res = 0;
		for (int i = 0; i < size; i++) {
			int next = rest.get(i);
			if (isHead[idx] && next == 0) continue;
			rest.remove(i);
			now += rate[idx] * next;
			res += dfs(now, idx - 1, rest);
			now -= rate[idx] * next;
			rest.add(i, next);
		}
		return res;
	}
	
	public void run() {
		long[] ten = new long[16];
		ten[1] = 1;
		for (int i = 2; i < 16; i++) {
			ten[i] = ten[i-1] * 10;
		}
		
		while (true) {
			int n = in.nextInt();
			if (n == 0) break;
			
			int size = 0;
			char[][] str = new char[n][];
			isHead = new boolean[10];
			rate = new long[10];
			HashMap<Character, Integer> map = new HashMap<Character, Integer>();
			for (int i = 0; i < n; i++) {
				str[i] = in.next().toCharArray();
				int m = str[i].length;
				for (int j = 0; j < m; j++) {
					if (!map.containsKey(str[i][j])) {
						map.put(str[i][j], size++);
					}
					if (map.containsKey(str[i][j])) {
						int index = map.get(str[i][j]);
						if (m != 1 && j == 0)
							isHead[index] = true;
						
						if (i == n - 1)
							rate[index] -= ten[m-j];
						else
							rate[index] += ten[m-j];
					}
				}
			}
			
			ArrayList<Integer> rest = new ArrayList<Integer>();
			for (int i = 0; i < 10; i++) {
				rest.add(i);
			}
			out.println(dfs(0, size - 1, rest));
		}
		/*
		Permutation perm = new Permutation(10);

		long[] ten = new long[16];
		ten[1] = 1;
		for (int i = 2; i < 16; i++) {
			ten[i] = ten[i-1] * 10;
		}
		
		long[] fact = new long[16];
		fact[0] = 1;
		for (int i = 1; i < 16; i++) {
			fact[i] = fact[i-1] * i;
		}
		
		while (true) {
			int n = in.nextInt();
			if (n == 0) break;
			
			int size = 0;
			char[][] str = new char[n][];
			boolean[] isHead = new boolean[10];
			long[] rate = new long[10];
			HashMap<Character, Integer> map = new HashMap<Character, Integer>();
			for (int i = 0; i < n; i++) {
				str[i] = in.next().toCharArray();
				int m = str[i].length;
				for (int j = 0; j < m; j++) {
					if (!map.containsKey(str[i][j])) {
						map.put(str[i][j], size++);
					}
					if (map.containsKey(str[i][j])) {
						int index = map.get(str[i][j]);
						if (m != 1 && j == 0)
							isHead[index] = true;
						
						if (i == n - 1)
							rate[index] -= ten[m-j];
						else
							rate[index] += ten[m-j];
					}
				}
			}
			
			long res = 0;
			main : 
			for (String p : perm.getPermutationSet()) {
				long sum = 0;
				
				for (int i = 0; i < size; i++) {
					if (isHead[i] && p.charAt(i) == '0') continue main;
					sum += (p.charAt(i) - '0') * rate[i];
				}
				
				if (sum == 0) {			
					res++;
				}
			}
			out.println(res / fact[10-size]);
			out.flush();
		}
		*/
		out.close();
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

		System.out.println("----------------------------");
		System.out.println();
	}

	public void debug(Object... obj) {
		System.out.println(Arrays.deepToString(obj));
	}

	class FastScanner {
		private InputStream stream;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int numChars;

		public FastScanner(InputStream stream) {
			this.stream = stream;
			//stream = new FileInputStream(new File("dec.in"));

		}

		int read() {
			if (numChars == -1)
				throw new InputMismatchException();
			if (curChar >= numChars) {
				curChar = 0;
				try {
					numChars = stream.read(buf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (numChars <= 0)
					return -1;
			}
			return buf[curChar++];
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

		int[][] nextIntMap(int n, int m) {
			int[][] map = new int[n][m];
			for (int i = 0; i < n; i++) {
				map[i] = in.nextIntArray(m);
			}
			return map;
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

		long[][] nextLongMap(int n, int m) {
			long[][] map = new long[n][m];
			for (int i = 0; i < n; i++) {
				map[i] = in.nextLongArray(m);
			}
			return map;
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

		double[][] nextDoubleMap(int n, int m) {
			double[][] map = new double[n][m];
			for (int i = 0; i < n; i++) {
				map[i] = in.nextDoubleArray(m);
			}
			return map;
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

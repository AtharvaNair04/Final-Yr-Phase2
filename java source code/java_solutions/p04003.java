import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.TreeSet;

class Po {
	int at;
	int cost;
	TreeSet<Integer> eki = new TreeSet<Integer>();

	public Po(int at, int cost, int eki) {
		this.at = at;
		this.cost = cost;
		this.eki.add(eki);
	}
}

public class Main {
	private static void solve() {
		int n = nei();
		int m = nei();
		int[][] e = new int[m][3];
		ArrayList<ArrayList<Integer>> es = new ArrayList<ArrayList<Integer>>();
		Po[] c = new Po[n];
		for (int i = 0; i < n; i++) {
			c[i] = new Po(i, 1145141919, 364634931);
		}
		for (int i = 0; i < n; i++) {
			es.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < m; i++) {
			e[i][0] = nei() - 1;
			e[i][1] = nei() - 1;
			e[i][2] = nei();
			es.get(e[i][0]).add(i);
			es.get(e[i][1]).add(i);
		}
		c[0].cost = 0;
		PriorityQueue<Po> q = new PriorityQueue<Po>((a, b) -> a.cost - b.cost);
		q.add(new Po(0, 0, 364364931));
		int[] cnt = new int[n];
		cnt[0]++;
		boolean aaa=Math.random()>0.5;
		while (!q.isEmpty()) {
			Po po = q.poll();
			int at = po.at;
			cnt[at]--;
			int cost = c[at].cost;
			TreeSet<Integer> eki = c[at].eki;
			ArrayList<Integer> edgs = es.get(at);
			for (int edg : edgs) {
				int[] ee = e[edg];
				int another = ee[0] == at ? ee[1] : ee[0];
				int cst = eki.contains(ee[2]) ? 0 : 1;
				if (c[another].cost > cost + cst) {
					c[another].cost = cost + cst;
					c[another].eki.clear();
					c[another].eki.add(ee[2]);
					Po popo = new Po(another, cost + cst, ee[2]);
					q.add(popo);
					cnt[another]++;
				} else if (c[another].cost == cost + cst) {
					if (c[another].eki.add(ee[2]) && aaa) {
						if (cnt[another] == 0) {
							Po popo = new Po(another, cost + cst, ee[2]);
							q.add(popo);
							cnt[another]++;
						}
					}
				}
			}
		}
		out(c[n - 1].cost == 1145141919 ? -1 : c[n - 1].cost);
	}

	static BigInteger combination(int a, int b, BigInteger mod) {
		BigInteger q = BigInteger.ONE;
		BigInteger p = BigInteger.ONE;
		for (int i = a - b + 1; i <= a; i++) {
			q = q.multiply(BigInteger.valueOf(i)).mod(mod);
		}
		for (int j = 1; j <= b; j++) {
			p = p.multiply(BigInteger.valueOf(j)).mod(mod);
		}
		return q.multiply(p.modInverse(mod)).mod(mod);
	}

	static int abs(int x) {
		return x < 0 ? -x : x;
	}

	static int gcd(int x, int y) {
		if (x < y) {
			x ^= y;
			y ^= x;
			x ^= y;
		}
		int z = x % y;
		if (z == 0)
			return y;
		return gcd(y, z);
	}

	static int clamp(int a, int min, int max) {
		return a < min ? min : a > max ? max : a;
	}

	static int max(int a, int b) {
		return a > b ? a : b;
	}

	static int min(int a, int b) {
		return a < b ? a : b;
	}

	static long max(long a, long b) {
		return a > b ? a : b;
	}

	static long min(long a, long b) {
		return a < b ? a : b;
	}

	static void out(String val) {
		IO.out(val);
	}

	static void out(int val) {
		IO.out(String.valueOf(val));
	}

	static void out(long val) {
		IO.out(String.valueOf(val));
	}

	static void out(char val) {
		IO.out(String.valueOf(val));
	}

	static void out(float val) {
		IO.out(String.valueOf(val));
	}

	static void out(double val) {
		IO.out(String.valueOf(val));
	}

	static void out(boolean val) {
		IO.out(String.valueOf(val));
	}

	static String next() {
		return IO.next();
	}

	static int nei() {
		return IO.nextInt();
	}

	static long nel() {
		return IO.nextLong();
	}

	static int parseInt(String val) {
		return Integer.parseInt(val);
	}

	static int parseInt(char val) {
		return Integer.parseInt(String.valueOf(val));
	}

	static long parseLong(String val) {
		return Long.parseLong(val);
	}

	public static void main(String[] args) {
		solve();
		IO.flush();
	}
}

final class UnionFind {
	int[] data;

	UnionFind(int n) {
		data = new int[n];
		for (int i = 0; i < n; i++) {
			data[i] = -1;
		}
	}

	boolean union(int x, int y) {
		x = root(x);
		y = root(y);
		if (x == y)
			return false;
		if (data[y] < data[x]) {
			x ^= y;
			y ^= x;
			x ^= y;
		}
		data[x] += data[y];
		data[y] = x;
		return true;
	}

	boolean find(int x, int y) {
		return root(x) == root(y);
	}

	int root(int x) {
		return data[x] < 0 ? x : (data[x] = root(data[x]));
	}

	int size(int x) {
		return -data[root(x)];
	}
}

final class IO {
	private static final InputStream in = System.in;
	private static final PrintWriter out = new PrintWriter(System.out, false);
	private static final byte[] buffer = new byte[1024];
	private static int ptr = 0;
	private static int len = 0;

	private static boolean hasNextByte() {
		if (ptr < len)
			return true;
		ptr = 0;
		try {
			len = in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return len > 0;
	}

	private static int readByte() {
		if (hasNextByte())
			return buffer[ptr++];
		else
			return -1;
	}

	static boolean hasNext() {
		byte c;
		while (hasNextByte() && ((c = buffer[ptr]) < '!' || c > '~'))
			ptr++;
		return hasNextByte();
	}

	static String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = readByte();
		while (b >= '!' && b <= '~') {
			sb.append((char) b);
			b = readByte();
		}
		return sb.toString();
	}

	static long nextLong() {
		if (!hasNext())
			throw new NoSuchElementException();
		long n = 0;
		int sign = 1;
		int b = readByte();
		if (b == '-') {
			sign = -1;
			b = readByte();
		}
		if (b < '0' || '9' < b)
			throw new NumberFormatException();
		while (true) {
			if ('0' <= b && b <= '9')
				n = n * 10 + b - '0';
			else if (b == -1 || b < '!' || b > '~')
				return n * sign;
			else
				throw new NumberFormatException();
			b = readByte();
		}
	}

	static int nextInt() {
		if (!hasNext())
			throw new NoSuchElementException();
		int n = 0;
		int sign = 1;
		int b = readByte();
		if (b == '-') {
			sign = -1;
			b = readByte();
		}
		if (b < '0' || '9' < b)
			throw new NumberFormatException();
		while (true) {
			if ('0' <= b && b <= '9')
				n = n * 10 + b - '0';
			else if (b == -1 || b < '!' || b > '~')
				return n * sign;
			else
				throw new NumberFormatException();
			b = readByte();
		}
	}

	static void out(String val) {
		out.println(val);
	}

	static void flush() {
		out.flush();
	}
}

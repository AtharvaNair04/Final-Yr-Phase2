import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.UnaryOperator;

public class Main implements Runnable {

	private void solve(FastIO io, String[] args) {
		/*
		 * author: 31536000
		 *
		 * 考察メモ
		 * ライブラリの使い方確認
		 */
		io.setAutoFlush(false);
		io.setAutoOutFlush(false);
		int N = io.nextInt(), K = io.nextInt();
		ACL.MinCostFlow flow = new ACL.MinCostFlow(2 * N + 2);
		final int START = 0, END = 2 * N + 1;
		final long MAX = 1000000001;
		IntBinaryOperator f = (x, y) -> x * N + y;
		int[] edge = new int[N * N + N + 1];
		for (int i = 1;i <= N;++ i) {
			flow.addEdge(START, i, K, 0);
			flow.addEdge(i + N, END, K, 0);
		}
		for (int y = 1;y <= N;++ y) {
			for (int x = 1;x <= N;++ x) {
				int A = io.nextInt();
				edge[f.applyAsInt(x, y)] = flow.addEdge(x, y + N, 1, MAX - A);
			}
		}
		flow.addEdge(START, END, N * K, MAX);
		long[] ans = flow.minCostFlow(START, END, N * K);
		io.println(N * K * MAX - ans[1]);
		for (int y = 1;y <= N;++ y) {
			for (int x = 1;x <= N;++ x) {
				if (flow.getEdge(edge[f.applyAsInt(x, y)]).getFlow() != 0) io.print('X');
				else io.print('.');
			}
			io.println();
		}
	}

	/** デバッグ用コードのお供に */
	private static boolean DEBUG = false;
	/** 確保するメモリの大きさ(単位: MB) */
	private static final long MEMORY = 64;
	private final FastIO io;
	private final String[] args;

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			e.printStackTrace();
			System.exit(1);
		});
		new Thread(null, new Main(args), "", MEMORY * 1048576L).start();
	}

	public Main(String[] args) {
		this(new FastIO(), args);
	}

	public Main(FastIO io, String... args) {
		this.io = io;
		this.args = args;
		if (DEBUG) io.setAutoFlush(true);
	}

	@Override
	public void run() {
		try {
			solve(io, args);
		} catch (Throwable e) {
			throw e;
		} finally {
			io.flush();
		}
	}

	// 以下、ライブラリ

	/**
	 * 指数表記の値を整数で返します。
	 *
	 * @param n 仮数部
	 * @param e 指数部
	 * @return n * 10^e
	 */
	public static int exponent10(int n, int e) {
		return n * pow(10, e);
	}

	/**
	 * 指数表記の値を整数で返します。
	 *
	 * @param n 仮数部
	 * @param e 指数部
	 * @return n * 10^e
	 */
	public static long exponent10L(int n, int e) {
		return n * pow(10L, e);
	}

	/**
	 * aのb乗を返します。
	 *
	 * @param a 整数
	 * @param b 整数
	 * @return aのb乗
	 */
	public static int pow(int a, int b) {
		int ans = 1;
		for (int mul = a; b > 0; b >>= 1, mul *= mul)
			if ((b & 1) != 0) ans *= mul;
		return ans;
	}

	/**
	 * aのb乗をmodを法として計算したものを返します。
	 *
	 * @param a   整数
	 * @param b   整数
	 * @param mod 法
	 * @return aのb乗をmodを法として計算したもの
	 */
	public static int pow(int a, int b, int mod) {
		a %= mod;
		if (a < 0) a += mod;
		b %= mod;
		if (b < 0) b += mod - 1;
		long ans = 1;
		for (long mul = a; b > 0; b >>= 1, mul = mul * mul % mod)
			if ((b & 1) != 0) ans = ans * mul % mod;
		return (int)ans;
	}

	/**
	 * aのb乗を返します。
	 *
	 * @param a 整数
	 * @param b 整数
	 * @return aのb乗
	 */
	public static long pow(long a, long b) {
		long ans = 1;
		for (long mul = a; b > 0; b >>= 1, mul *= mul)
			if ((b & 1) != 0) ans *= mul;
		return ans;
	}

	/**
	 * aのb乗をmodを法として計算したものを返します。
	 *
	 * @param a   整数
	 * @param b   整数
	 * @param mod 法
	 * @return aのb乗をmodを法として計算したもの
	 */
	public static int pow(long a, long b, int mod) {
		a %= mod;
		if (a < 0) a += mod;
		b %= mod;
		if (b < 0) b += mod - 1;
		long ans = 1;
		for (long mul = a; b > 0; b >>= 1, mul = mul * mul % mod)
			if ((b & 1) != 0) ans = ans * mul % mod;
		return (int)ans;
	}

	/**
	 * 高速な入出力を提供します。
	 *
	 * @author 31536000
	 *
	 */
	public static class FastIO implements AutoCloseable {

		private InputStream in;
		private final byte[] buffer = new byte[1024];
		private int read = 0;
		private int length = 0;
		private PrintWriter out;
		private PrintWriter err;
		private boolean autoFlush = false;
		private boolean outFlush = false;
		private boolean autoOutFlush = true;

		public FastIO() {
			this(System.in, System.out, System.err);
		}

		public FastIO(InputStream in, PrintStream out, PrintStream err) {
			this.in = in;
			this.out = new PrintWriter(out, false);
			this.err = new PrintWriter(err, false);
		}

		public final void setInputStream(InputStream in) {
			this.in = in;
		}

		public final void setInputStream(File in) {
			try {
				this.in = new FileInputStream(in);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public final void setOutputStream(PrintStream out) {
			this.out = new PrintWriter(out, false);
		}

		public final void setOutputStream(File out) {
			try {
				this.out = new PrintWriter(new FileOutputStream(out), false);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public final void setErrorStream(PrintStream err) {
			this.err = new PrintWriter(err, false);
		}

		public final void setErrorStream(File err) {
			try {
				this.err = new PrintWriter(new FileOutputStream(err), false);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public final void setAutoFlush(boolean flush) {
			autoFlush = flush;
		}

		public final void setAutoOutFlush(boolean flush) {
			autoOutFlush = flush;
		}

		private boolean hasNextByte() {
			if (read < length) return true;
			read = 0;
			try {
				length = in.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return length > 0;
		}

		private byte readByte() {
			if (hasNextByte()) return buffer[read++ ];
			throw new NoSuchElementException();
		}

		private boolean readNewLine() {
			if (hasNextByte()) {
				if (buffer[read] == '\r') {
					++read;
					if (hasNextByte() && buffer[read] == '\n') ++read;
					return true;
				}
				if (buffer[read] == '\n') {
					++read;
					return true;
				}
			}
			return false;
		}

		private static boolean isPrintableChar(byte c) {
			return 32 < c || c < 0;
		}

		private static boolean isNumber(int c) {
			return '0' <= c && c <= '9';
		}

		public final boolean hasNext() {
			while (hasNextByte() && !isPrintableChar(buffer[read]))
				read++ ;
			return hasNextByte();
		}

		public final char nextChar() {
			if (outFlush) {
				outFlush = false;
				flush();
			}
			byte b = readByte();
			if ((b & 0x80) == 0) return (char)b;
			if ((b & 0x20) == 0) return (char)((b & 0x1F) << 6 | (readByte() & 0x3F));
			return (char)((b & 0xF) << 12 | (readByte() & 0x3F) << 6 | (readByte() & 0x3F));
		}

		public final char[] nextChars() {
			return next().toCharArray();
		}

		public final char[] nextChars(char around) {
			return (around + next() + around).toCharArray();
		}

		public final char[][] nextChars(int height) {
			char[][] ret = new char[height][];
			for (int i = 0; i < ret.length; ++i)
				ret[i] = nextChars();
			return ret;
		}

		public final char[][] nextChars(int height, char around) {
			char[][] ret = new char[height + 2][];
			for (int i = 1; i <= height; ++i)
				ret[i] = nextChars(around);
			Arrays.fill(ret[0] = new char[ret[1].length], around);
			Arrays.fill(ret[ret.length - 1] = new char[ret[0].length], around);
			return ret;
		}

		public final String next() {
			if (outFlush) {
				outFlush = false;
				flush();
			}
			if (!hasNext()) throw new NoSuchElementException();
			StringBuilder sb = new StringBuilder();
			do
				sb.append(nextChar());
			while (hasNextByte() && isPrintableChar(buffer[read]));
			return sb.toString();
		}

		public final String nextLine() {
			if (outFlush) {
				outFlush = false;
				flush();
			}
			StringBuilder sb = new StringBuilder();
			while (!readNewLine())
				sb.append(nextChar());
			return sb.toString();
		}

		public final long nextLong() {
			if (outFlush) {
				outFlush = false;
				flush();
			}
			if (!hasNext()) throw new NoSuchElementException();
			long n = 0;
			try {
				byte b = readByte();
				if (b == '-') {
					while (isNumber(b = readByte()))
						n = n * 10 + '0' - b;
					return n;
				} else if (!isNumber(b)) throw new NumberFormatException();
				do
					n = n * 10 + b - '0';
				while (isNumber(b = readByte()));
				return n;
			} catch (NoSuchElementException e) {
				return n;
			}
		}

		public final int nextInt() {
			if (outFlush) {
				outFlush = false;
				flush();
			}
			if (!hasNext()) throw new NoSuchElementException();
			int n = 0;
			try {
				byte b = readByte();
				if (b == '-') {
					while (isNumber(b = readByte()))
						n = n * 10 + '0' - b;
					return n;
				} else if (!isNumber(b)) throw new NumberFormatException();
				do
					n = n * 10 + b - '0';
				while (isNumber(b = readByte()));
				return n;
			} catch (NoSuchElementException e) {
				return n;
			}
		}

		public final double nextDouble() {
			return Double.parseDouble(next());
		}

		public final int[] nextInt(int width) {
			int[] ret = new int[width];
			for (int i = 0; i < width; ++i)
				ret[i] = nextInt();
			return ret;
		}

		public final int[] nextInts() {
			return nextInts(" ");
		}

		public final int[] nextInts(String parse) {
			String[] get = nextLine().split(parse);
			int[] ret = new int[get.length];
			for (int i = 0; i < ret.length; ++i)
				ret[i] = Integer.valueOf(get[i]);
			return ret;
		}

		public final long[] nextLong(int width) {
			long[] ret = new long[width];
			for (int i = 0; i < width; ++i)
				ret[i] = nextLong();
			return ret;
		}

		public final long[] nextLongs() {
			return nextLongs(" ");
		}

		public final long[] nextLongs(String parse) {
			String[] get = nextLine().split(parse);
			long[] ret = new long[get.length];
			for (int i = 0; i < ret.length; ++i)
				ret[i] = Long.valueOf(get[i]);
			return ret;
		}

		public final int[][] nextInt(int width, int height) {
			int[][] ret = new int[height][width];
			for (int i = 0, j; i < height; ++i)
				for (j = 0; j < width; ++j)
					ret[i][j] = nextInt();
			return ret;
		}

		public final long[][] nextLong(int width, int height) {
			long[][] ret = new long[height][width];
			for (int i = 0, j; i < height; ++i)
				for (j = 0; j < width; ++j)
					ret[j][i] = nextLong();
			return ret;
		}

		public final boolean[] nextBoolean(char T) {
			char[] s = next().toCharArray();
			boolean[] ret = new boolean[s.length];
			for (int i = 0; i < ret.length; ++i)
				ret[i] = s[i] == T;
			return ret;
		}

		public final boolean[][] nextBoolean(char T, int height) {
			boolean[][] ret = new boolean[height][];
			for (int i = 0; i < ret.length; ++i) {
				char[] s = next().toCharArray();
				ret[i] = new boolean[s.length];
				for (int j = 0; j < ret[i].length; ++j)
					ret[i][j] = s[j] == T;
			}
			return ret;
		}

		public final Point nextPoint() {
			return new Point(nextInt(), nextInt());
		}

		public final Point[] nextPoint(int width) {
			Point[] ret = new Point[width];
			for (int i = 0; i < width; ++i)
				ret[i] = nextPoint();
			return ret;
		}

		@Override
		protected void finalize() throws Throwable {
			try {
				super.finalize();
			} finally {
				in.close();
				out.close();
				err.close();
			}
		}

		public final boolean print(boolean b) {
			out.print(b);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return b;
		}

		public final Object print(boolean b, Object t, Object f) {
			return b ? print(t) : print(f);
		}

		public final char print(char c) {
			out.print(c);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return c;
		}

		public final char[] print(char[] s) {
			out.print(s);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return s;
		}

		public final double print(double d) {
			out.print(d);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return d;
		}

		public final double print(double d, int length) {
			if (d < 0) {
				out.print('-');
				d = -d;
			}
			d += Math.pow(10, -length) / 2;
			out.print((long)d);
			out.print('.');
			d -= (long)d;
			for (int i = 0; i < length; ++i) {
				d *= 10;
				out.print((int)d);
				d -= (int)d;
			}
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return d;
		}

		public final float print(float f) {
			out.print(f);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return f;
		}

		public final int print(int i) {
			out.print(i);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return i;
		}

		public final long print(long l) {
			out.print(l);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return l;
		}

		public final Object print(Object obj) {
			if (obj != null && obj.getClass().isArray()) {
				if (obj instanceof boolean[][]) print(obj, "\n", " ");
				else if (obj instanceof byte[][]) print(obj, "\n", " ");
				else if (obj instanceof short[][]) print(obj, "\n", " ");
				else if (obj instanceof int[][]) print(obj, "\n", " ");
				else if (obj instanceof long[][]) print(obj, "\n", " ");
				else if (obj instanceof float[][]) print(obj, "\n", " ");
				else if (obj instanceof double[][]) print(obj, "\n", " ");
				else if (obj instanceof char[][]) print(obj, "\n", " ");
				else if (obj instanceof Object[][]) print(obj, "\n", " ");
				else print(obj, " ");
			} else {
				out.print(obj);
				if (autoFlush) flush();
				else outFlush = autoOutFlush;
			}
			return obj;
		}

		public final String print(String s) {
			out.print(s);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return s;
		}

		public final Object print(Object array, String... parse) {
			print(array, 0, parse);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return array;
		}

		private final Object print(Object array, int check, String... parse) {
			if (check >= parse.length) {
				if (array != null && array.getClass().isArray()) throw new IllegalArgumentException("not equal dimension");
				print(array);
				return array;
			}
			String str = parse[check];
			if (array instanceof Object[]) {
				Object[] obj = (Object[])array;
				if (obj.length == 0) return array;
				print(obj[0], check + 1, parse);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i], check + 1, parse);
				}
				return array;
			}
			if (array instanceof Collection) {
				Iterator<?> iter = ((Collection<?>)array).iterator();
				if (!iter.hasNext()) return array;
				print(iter.next(), check + 1, parse);
				while (iter.hasNext()) {
					print(str);
					print(iter.next(), check + 1, parse);
				}
				return array;
			}
			if (!array.getClass().isArray()) throw new IllegalArgumentException("not equal dimension");
			if (check != parse.length - 1) throw new IllegalArgumentException("not equal dimension");
			if (array instanceof boolean[]) {
				boolean[] obj = (boolean[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
			} else if (array instanceof byte[]) {
				byte[] obj = (byte[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
				return array;
			} else if (array instanceof short[]) {
				short[] obj = (short[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
			} else if (array instanceof int[]) {
				int[] obj = (int[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
			} else if (array instanceof long[]) {
				long[] obj = (long[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
			} else if (array instanceof float[]) {
				float[] obj = (float[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
			} else if (array instanceof double[]) {
				double[] obj = (double[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
			} else if (array instanceof char[]) {
				char[] obj = (char[])array;
				if (obj.length == 0) return array;
				print(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					print(str);
					print(obj[i]);
				}
			} else throw new AssertionError();
			return array;
		}

		public final Object[] print(String parse, Object... args) {
			print(args[0]);
			for (int i = 1; i < args.length; ++i) {
				print(parse);
				print(args[i]);
			}
			return args;
		}

		public final Object[] printf(String format, Object... args) {
			out.printf(format, args);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return args;
		}

		public final Object printf(Locale l, String format, Object... args) {
			out.printf(l, format, args);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return args;
		}

		public final void println() {
			out.println();
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
		}

		public final boolean println(boolean b) {
			out.println(b);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return b;
		}

		public final Object println(boolean b, Object t, Object f) {
			return b ? println(t) : println(f);
		}

		public final char println(char c) {
			out.println(c);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return c;
		}

		public final char[] println(char[] s) {
			out.println(s);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return s;
		}

		public final double println(double d) {
			out.println(d);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return d;
		}

		public final double println(double d, int length) {
			print(d, length);
			println();
			return d;
		}

		public final float println(float f) {
			out.println(f);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return f;
		}

		public final int println(int i) {
			out.println(i);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return i;
		}

		public final long println(long l) {
			out.println(l);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return l;
		}

		public final Object println(Object obj) {
			print(obj);
			println();
			return obj;
		}

		public final String println(String s) {
			out.println(s);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return s;
		}

		public final Object println(Object array, String... parse) {
			print(array, parse);
			println();
			return array;
		}

		public final boolean debug(boolean b) {
			err.print(b);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return b;
		}

		public final Object debug(boolean b, Object t, Object f) {
			return b ? debug(t) : debug(f);
		}

		public final char debug(char c) {
			err.print(c);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return c;
		}

		public final char[] debug(char[] s) {
			err.print(s);
			return s;
		}

		public final double debug(double d) {
			err.print(d);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return d;
		}

		public final double debug(double d, int length) {
			if (d < 0) {
				err.print('-');
				d = -d;
			}
			d += Math.pow(10, -length) / 2;
			err.print((long)d);
			err.print('.');
			d -= (long)d;
			for (int i = 0; i < length; ++i) {
				d *= 10;
				err.print((int)d);
				d -= (int)d;
			}
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return d;
		}

		public final float debug(float f) {
			err.print(f);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return f;
		}

		public final int debug(int i) {
			err.print(i);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return i;
		}

		public final long debug(long l) {
			err.print(l);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return l;
		}

		public final Object debug(Object obj) {
			if (obj != null && obj.getClass().isArray()) {
				if (obj instanceof boolean[][]) debug(obj, "\n", " ");
				else if (obj instanceof byte[][]) debug(obj, "\n", " ");
				else if (obj instanceof short[][]) debug(obj, "\n", " ");
				else if (obj instanceof int[][]) debug(obj, "\n", " ");
				else if (obj instanceof long[][]) debug(obj, "\n", " ");
				else if (obj instanceof float[][]) debug(obj, "\n", " ");
				else if (obj instanceof double[][]) debug(obj, "\n", " ");
				else if (obj instanceof char[][]) debug(obj, "\n", " ");
				else if (obj instanceof Object[][]) debug(obj, "\n", " ");
				else debug(obj, " ");
			} else {
				err.print(obj);
				if (autoFlush) flush();
				else outFlush = autoOutFlush;
			}
			return obj;
		}

		public final String debug(String s) {
			err.print(s);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return s;
		}

		public final Object debug(Object array, String... parse) {
			debug(array, 0, parse);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return array;
		}

		private final Object debug(Object array, int check, String... parse) {
			if (check >= parse.length) {
				if (array != null && array.getClass().isArray()) throw new IllegalArgumentException("not equal dimension");
				debug(array);
				return array;
			}
			String str = parse[check];
			if (array instanceof Object[]) {
				Object[] obj = (Object[])array;
				if (obj.length == 0) return array;
				debug(obj[0], check + 1, parse);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i], check + 1, parse);
				}
				return array;
			}
			if (array instanceof Collection) {
				Iterator<?> iter = ((Collection<?>)array).iterator();
				if (!iter.hasNext()) return array;
				debug(iter.next(), check + 1, parse);
				while (iter.hasNext()) {
					debug(str);
					debug(iter.next(), check + 1, parse);
				}
				return array;
			}
			if (!array.getClass().isArray()) throw new IllegalArgumentException("not equal dimension");
			if (check != parse.length - 1) throw new IllegalArgumentException("not equal dimension");
			if (array instanceof boolean[]) {
				boolean[] obj = (boolean[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
			} else if (array instanceof byte[]) {
				byte[] obj = (byte[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
				return array;
			} else if (array instanceof short[]) {
				short[] obj = (short[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
			} else if (array instanceof int[]) {
				int[] obj = (int[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
			} else if (array instanceof long[]) {
				long[] obj = (long[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
			} else if (array instanceof float[]) {
				float[] obj = (float[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
			} else if (array instanceof double[]) {
				double[] obj = (double[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
			} else if (array instanceof char[]) {
				char[] obj = (char[])array;
				if (obj.length == 0) return array;
				debug(obj[0]);
				for (int i = 1; i < obj.length; ++i) {
					debug(str);
					debug(obj[i]);
				}
			} else throw new AssertionError();
			return array;
		}

		public final Object[] debug(String parse, Object... args) {
			debug(args[0]);
			for (int i = 1; i < args.length; ++i) {
				debug(parse);
				debug(args[i]);
			}
			return args;
		}

		public final Object[] debugf(String format, Object... args) {
			err.printf(format, args);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return args;
		}

		public final Object debugf(Locale l, String format, Object... args) {
			err.printf(l, format, args);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return args;
		}

		public final void debugln() {
			err.println();
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
		}

		public final boolean debugln(boolean b) {
			err.println(b);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return b;
		}

		public final Object debugln(boolean b, Object t, Object f) {
			return b ? debugln(t) : debugln(f);
		}

		public final char debugln(char c) {
			err.println(c);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return c;
		}

		public final char[] debugln(char[] s) {
			err.println(s);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return s;
		}

		public final double debugln(double d) {
			err.println(d);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return d;
		}

		public final double debugln(double d, int length) {
			debug(d, length);
			debugln();
			return d;
		}

		public final float debugln(float f) {
			err.println(f);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return f;
		}

		public final int debugln(int i) {
			err.println(i);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return i;
		}

		public final long debugln(long l) {
			err.println(l);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return l;
		}

		public final Object debugln(Object obj) {
			debug(obj);
			debugln();
			return obj;
		}

		public final String debugln(String s) {
			err.println(s);
			if (autoFlush) flush();
			else outFlush = autoOutFlush;
			return s;
		}

		public final Object debugln(Object array, String... parse) {
			debug(array, parse);
			debugln();
			return array;
		}

		public final void flush() {
			out.flush();
			err.flush();
			outFlush = false;
		}

		@Override
		public final void close() {
			out.close();
			err.close();
		}
	}

	public enum BoundType {
		CLOSED, OPEN;
	}

	public static class Range<C> implements Serializable {

		private static final long serialVersionUID = -4702828934863023392L;
		protected C lower;
		protected C upper;
		protected BoundType lowerType;
		protected BoundType upperType;
		private Comparator<? super C> comparator;

		protected Range(C lower, BoundType lowerType, C upper, BoundType upperType) {
			this(lower, lowerType, upper, upperType, null);
		}

		protected Range(C lower, BoundType lowerType, C upper, BoundType upperType, Comparator<? super C> comparator) {
			this.lower = lower;
			this.upper = upper;
			this.lowerType = lowerType;
			this.upperType = upperType;
			this.comparator = comparator;
		}

		public static <C extends Comparable<? super C>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
			if (lower != null && upper != null) {
				int comp = lower.compareTo(upper);
				if (comp > 0) return new Range<C>(null, BoundType.CLOSED, null, BoundType.CLOSED);
				else if (comp == 0 && (lowerType == BoundType.OPEN || upperType == BoundType.OPEN)) return new Range<C>(null,
						BoundType.CLOSED, null, BoundType.CLOSED);
			}
			return new Range<C>(lower, lowerType, upper, upperType);
		}

		public static <C> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType, Comparator<? super C> comparator) {
			if (lower != null && upper != null) {
				int comp = comparator.compare(lower, upper);
				if (comp > 0) return new Range<C>(null, BoundType.CLOSED, null, BoundType.CLOSED, comparator);
				else if (comp == 0 && (lowerType == BoundType.OPEN || upperType == BoundType.OPEN)) return new Range<C>(null,
						BoundType.CLOSED, null, BoundType.CLOSED, comparator);
			}
			return new Range<C>(lower, lowerType, upper, upperType, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> all() {
			return range((C)null, BoundType.OPEN, null, BoundType.OPEN);
		}

		public static <C> Range<C> all(Comparator<? super C> comparator) {
			return range((C)null, BoundType.OPEN, null, BoundType.OPEN, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> atMost(C upper) {
			return range(null, BoundType.OPEN, upper, BoundType.CLOSED);
		}

		public static <C> Range<C> atMost(C upper, Comparator<? super C> comparator) {
			return range(null, BoundType.OPEN, upper, BoundType.CLOSED, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> lessThan(C upper) {
			return range(null, BoundType.OPEN, upper, BoundType.OPEN);
		}

		public static <C> Range<C> lessThan(C upper, Comparator<? super C> comparator) {
			return range(null, BoundType.OPEN, upper, BoundType.OPEN, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> downTo(C upper, BoundType boundType) {
			return range(null, BoundType.OPEN, upper, boundType);
		}

		public static <C> Range<C> downTo(C upper, BoundType boundType, Comparator<? super C> comparator) {
			return range(null, BoundType.OPEN, upper, boundType, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> atLeast(C lower) {
			return range(lower, BoundType.CLOSED, null, BoundType.OPEN);
		}

		public static <C> Range<C> atLeast(C lower, Comparator<? super C> comparator) {
			return range(lower, BoundType.CLOSED, null, BoundType.OPEN, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> greaterThan(C lower) {
			return range(lower, BoundType.OPEN, null, BoundType.OPEN);
		}

		public static <C> Range<C> greaterThan(C lower, Comparator<? super C> comparator) {
			return range(lower, BoundType.OPEN, null, BoundType.OPEN, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> upTo(C lower, BoundType boundType) {
			return range(lower, boundType, null, BoundType.OPEN);
		}

		public static <C> Range<C> upTo(C lower, BoundType boundType, Comparator<? super C> comparator) {
			return range(lower, boundType, null, BoundType.OPEN, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> open(C lower, C upper) {
			return range(lower, BoundType.OPEN, upper, BoundType.OPEN);
		}

		public static <C> Range<C> open(C lower, C upper, Comparator<? super C> comparator) {
			return range(lower, BoundType.OPEN, upper, BoundType.OPEN, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> openClosed(C lower, C upper) {
			return range(lower, BoundType.OPEN, upper, BoundType.CLOSED);
		}

		public static <C> Range<C> openClosed(C lower, C upper, Comparator<? super C> comparator) {
			return range(lower, BoundType.OPEN, upper, BoundType.CLOSED, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> closedOpen(C lower, C upper) {
			return range(lower, BoundType.CLOSED, upper, BoundType.OPEN);
		}

		public static <C> Range<C> closedOpen(C lower, C upper, Comparator<? super C> comparator) {
			return range(lower, BoundType.CLOSED, upper, BoundType.OPEN, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> closed(C lower, C upper) {
			return range(lower, BoundType.CLOSED, upper, BoundType.CLOSED);
		}

		public static <C> Range<C> closed(C lower, C upper, Comparator<? super C> comparator) {
			return range(lower, BoundType.CLOSED, upper, BoundType.CLOSED, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> singleton(C value) {
			return range(value, BoundType.CLOSED, value, BoundType.CLOSED);
		}

		public static <C> Range<C> singleton(C value, Comparator<? super C> comparator) {
			return range(value, BoundType.CLOSED, value, BoundType.CLOSED, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> empty() {
			return range((C)null, BoundType.CLOSED, null, BoundType.CLOSED);
		}

		public static <C> Range<C> empty(Comparator<? super C> comparator) {
			return range((C)null, BoundType.CLOSED, null, BoundType.CLOSED, comparator);
		}

		public static <C extends Comparable<? super C>> Range<C> encloseAll(Iterable<C> values) {
			C lower = values.iterator().next();
			C upper = lower;
			for (C i : values) {
				if (lower.compareTo(i) > 0) lower = i;
				if (upper.compareTo(i) < 0) upper = i;
			}
			return range(lower, BoundType.CLOSED, upper, BoundType.CLOSED);
		}

		public static <C> Range<C> encloseAll(Iterable<C> values, Comparator<? super C> comparator) {
			C lower = values.iterator().next();
			C upper = lower;
			for (C i : values) {
				if (comparator.compare(lower, i) > 0) lower = i;
				if (comparator.compare(upper, i) < 0) upper = i;
			}
			return range(lower, BoundType.CLOSED, upper, BoundType.CLOSED, comparator);
		}

		protected int compareLower(C value) {
			return compareLower(value, BoundType.CLOSED);
		}

		protected int compareLower(C value, BoundType boundType) {
			return compareLower(lower, lowerType, value, boundType);
		}

		protected int compareLower(C lower, BoundType lowerType, C value) {
			return compareLower(lower, lowerType, value, BoundType.CLOSED);
		}

		protected int compareLower(C lower, BoundType lowerType, C value, BoundType boundType) {
			if (lower == null) return value == null ? 0 : -1;
			else if (value == null) return 1;
			int compare;
			if (comparator == null) {
				@SuppressWarnings("unchecked")
				Comparable<C> comp = (Comparable<C>)lower;
				compare = comp.compareTo(value);
			} else compare = comparator.compare(lower, value);
			if (compare == 0) {
				if (lowerType == BoundType.CLOSED) --compare;
				if (boundType == BoundType.CLOSED) ++compare;
			}
			return compare;
		}

		protected int compareUpper(C value) {
			return compareUpper(value, BoundType.CLOSED);
		}

		protected int compareUpper(C value, BoundType boundType) {
			return compareUpper(upper, upperType, value, boundType);
		}

		protected int compareUpper(C upper, BoundType upperType, C value) {
			return compareUpper(upper, upperType, value, BoundType.CLOSED);
		}

		protected int compareUpper(C upper, BoundType upperType, C value, BoundType boundType) {
			if (upper == null) return value == null ? 0 : 1;
			if (value == null) return -1;
			int compare;
			if (comparator == null) {
				@SuppressWarnings("unchecked")
				Comparable<C> comp = (Comparable<C>)upper;
				compare = comp.compareTo(value);
			} else compare = comparator.compare(upper, value);
			if (compare == 0) {
				if (upperType == BoundType.CLOSED) ++compare;
				if (boundType == BoundType.CLOSED) --compare;
			}
			return compare;
		}

		public boolean hasLowerBound() {
			return lower != null;
		}

		public C lowerEndpoint() {
			if (hasLowerBound()) return lower;
			throw new IllegalStateException();
		}

		public BoundType lowerBoundType() {
			if (hasLowerBound()) return lowerType;
			throw new IllegalStateException();
		}

		public boolean hasUpperBound() {
			return upper != null;
		}

		public C upperEndpoint() {
			if (hasUpperBound()) return upper;
			throw new IllegalStateException();
		}

		public BoundType upperBoundType() {
			if (hasUpperBound()) return upperType;
			throw new IllegalStateException();
		}

		/**
		 * この区間が空集合か判定します。
		 *
		 * @return 空集合ならばtrue
		 */
		public boolean isEmpty() {
			return lower == null && upper == null && lowerType == BoundType.CLOSED;
		}

		/**
		 * 与えられた引数が区間の左側に位置するか判定します。<br>
		 * 接する場合は区間の左側ではないと判定します。
		 *
		 * @param value 調べる引数
		 * @return 区間の左側に位置するならtrue
		 */
		public boolean isLess(C value) {
			return isLess(value, BoundType.CLOSED);
		}

		protected boolean isLess(C value, BoundType boundType) {
			return compareLower(value, boundType) > 0;
		}

		/**
		 * 与えられた引数が区間の右側に位置するか判定します。<br>
		 * 接する場合は区間の右側ではないと判定します。
		 *
		 * @param value 調べる引数
		 * @return 区間の右側に位置するならtrue
		 */
		public boolean isGreater(C value) {
			return isGreater(value, BoundType.CLOSED);
		}

		private boolean isGreater(C value, BoundType boundType) {
			return compareUpper(value, boundType) < 0;
		}

		/**
		 * 与えられた引数が区間内に位置するか判定します。<br>
		 * 接する場合も区間内に位置すると判定します。
		 *
		 * @param value 調べる引数
		 * @return 区間内に位置するならtrue
		 */
		public boolean contains(C value) {
			return !isLess(value) && !isGreater(value) && !isEmpty();
		}

		/**
		 * 与えられた引数すべてが区間内に位置するか判定します。<br>
		 * 接する場合も区間内に位置すると判定します。
		 *
		 * @param value 調べる要素
		 * @return 全ての要素が区間内に位置するならtrue
		 */
		public boolean containsAll(Iterable<? extends C> values) {
			for (C i : values)
				if (!contains(i)) return false;
			return true;
		}

		/**
		 * 与えられた区間がこの区間に内包されるか判定します。<br>
		 *
		 * @param other
		 * @return 与えられた区間がこの区間に内包されるならtrue
		 */
		public boolean encloses(Range<C> other) {
			return !isLess(other.lower, other.lowerType) && !isGreater(other.upper, other.upperType);
		}

		/**
		 * 与えられた区間がこの区間と公差するか判定します。<br>
		 * 接する場合は公差するものとします。
		 *
		 * @param value 調べる引数
		 * @return 区間が交差するならtrue
		 */
		public boolean isConnected(Range<C> other) {
			if (this.isEmpty() || other.isEmpty()) return false;
			C lower, upper;
			BoundType lowerType, upperType;
			if (isLess(other.lower, other.lowerType)) {
				lower = other.lower;
				lowerType = other.lowerType;
			} else {
				lower = this.lower;
				lowerType = this.lowerType;
			}
			if (isGreater(other.upper, other.upperType)) {
				upper = other.upper;
				upperType = other.upperType;
			} else {
				upper = this.upper;
				upperType = this.upperType;
			}
			if (lower == null || upper == null) return true;
			int comp = compareLower(lower, lowerType, upper, upperType);
			return comp <= 0;
		}

		/**
		 * この区間との積集合を返します。
		 *
		 * @param connectedRange 積集合を求める区間
		 * @return 積集合
		 */
		public Range<C> intersection(Range<C> connectedRange) {
			if (this.isEmpty() || connectedRange.isEmpty()) {
				if (comparator == null) return new Range<C>(null, BoundType.CLOSED, null, BoundType.CLOSED);
				return empty(comparator);
			}
			C lower, upper;
			BoundType lowerType, upperType;
			if (isLess(connectedRange.lower, connectedRange.lowerType)) {
				lower = connectedRange.lower;
				lowerType = connectedRange.lowerType;
			} else {
				lower = this.lower;
				lowerType = this.lowerType;
			}
			if (isGreater(connectedRange.upper, connectedRange.upperType)) {
				upper = connectedRange.upper;
				upperType = connectedRange.upperType;
			} else {
				upper = this.upper;
				upperType = this.upperType;
			}
			if (comparator == null) { return new Range<C>(lower, lowerType, upper, upperType); }
			return range(lower, lowerType, upper, upperType, comparator);
		}

		/**
		 * この区間との和集合を返します。
		 *
		 * @param other 和集合を求める区間
		 * @return 和集合
		 */
		public Range<C> span(Range<C> other) {
			if (other.isEmpty()) return new Range<C>(lower, lowerType, upper, upperType);
			C lower, upper;
			BoundType lowerType, upperType;
			if (isLess(other.lower, other.lowerType)) {
				lower = this.lower;
				lowerType = this.lowerType;
			} else {
				lower = other.lower;
				lowerType = other.lowerType;
			}
			if (isGreater(other.upper, other.upperType)) {
				upper = this.upper;
				upperType = this.upperType;
			} else {
				upper = other.upper;
				upperType = other.upperType;
			}
			return new Range<C>(lower, lowerType, upper, upperType, comparator);
		}

		/**
		 * 区間スケジューリングを行います。<br>
		 * 計算量は要素数Nに対してO(NlogN)です。
		 *
		 * @param ranges 区間の集合
		 * @return 区間スケジューリングを行った際の一つの解
		 */
		public static <C> List<Range<C>> scheduling(List<Range<C>> ranges) {
			PriorityQueue<Range<C>> pq = new PriorityQueue<Range<C>>((l, r) -> l.compareUpper(r.upper, r.upperType));
			List<Range<C>> ret = new ArrayList<>();
			Range<C> last = pq.poll();
			if (pq.isEmpty()) return ret;
			ret.add(last);
			while (!pq.isEmpty()) {
				Range<C> tmp = pq.poll();
				if (tmp.compareLower(last.upper, last.upperType) > 0) {
					ret.add(tmp);
					last = tmp;
				}
			}
			return ret;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) return true;
			if (object instanceof Range) {
				@SuppressWarnings("unchecked")
				Range<C> comp = (Range<C>)object;
				return compareLower(comp.lower, comp.lowerType) == 0 && compareUpper(comp.upper,
						comp.upperType) == 0 && lowerType == comp.lowerType && upperType == comp.upperType;
			}
			return false;
		}

		@Override
		public int hashCode() {
			if (lower == null && upper == null) return 0;
			else if (lower == null) return upper.hashCode();
			else if (upper == null) return lower.hashCode();
			return lower.hashCode() ^ upper.hashCode();
		}

		@Override
		public String toString() {
			if (isEmpty()) return "()";
			return (lowerType == BoundType.OPEN ? "(" : "[") + (lower == null ? "" : lower
					.toString()) + ".." + (upper == null ? "" : upper.toString()) + (upperType == BoundType.OPEN ? ")" : "]");
		}
	}

	public static class IterableRange<C> extends Range<C> implements Iterable<C> {

		private static final long serialVersionUID = 9065915259748260688L;
		protected UnaryOperator<C> func;

		protected IterableRange(C lower, BoundType lowerType, C upper, BoundType upperType, UnaryOperator<C> func) {
			super(lower, lowerType, upper, upperType);
			this.func = func;
		}

		public static <C extends Comparable<? super C>> IterableRange<C> range(C lower, BoundType lowerType, C upper, BoundType upperType,
				UnaryOperator<C> func) {
			if (lower == null || upper == null) return new IterableRange<C>(null, BoundType.CLOSED, null, BoundType.CLOSED, func);
			int comp = lower.compareTo(upper);
			if (comp > 0) return new IterableRange<C>(null, BoundType.CLOSED, null, BoundType.CLOSED, func);
			else if (comp == 0 && (lowerType == BoundType.OPEN || upperType == BoundType.OPEN)) return new IterableRange<C>(null,
					BoundType.CLOSED, null, BoundType.CLOSED, func);
			return new IterableRange<C>(lower, lowerType, upper, upperType, func);
		}

		public static <C extends Comparable<? super C>> IterableRange<C> open(C lower, C upper, UnaryOperator<C> func) {
			if (lower == null) return new IterableRange<C>(null, BoundType.CLOSED, null, BoundType.CLOSED, func);
			return range(func.apply(lower), BoundType.CLOSED, upper, BoundType.OPEN, func);
		}

		public static <C extends Comparable<? super C>> IterableRange<C> openClosed(C lower, C upper, UnaryOperator<C> func) {
			if (lower == null) return new IterableRange<C>(null, BoundType.CLOSED, null, BoundType.CLOSED, func);
			return range(func.apply(lower), BoundType.CLOSED, upper, BoundType.CLOSED, func);
		}

		public static <C extends Comparable<? super C>> IterableRange<C> closedOpen(C lower, C upper, UnaryOperator<C> func) {
			return range(lower, BoundType.CLOSED, upper, BoundType.OPEN, func);
		}

		public static <C extends Comparable<? super C>> IterableRange<C> closed(C lower, C upper, UnaryOperator<C> func) {
			return range(lower, BoundType.CLOSED, upper, BoundType.CLOSED, func);
		}

		public static <C extends Comparable<? super C>> IterableRange<C> singleton(C value, UnaryOperator<C> func) {
			return range(value, BoundType.CLOSED, value, BoundType.CLOSED, func);
		}

		protected class Iter implements Iterator<C> {

			C now;

			Iter() {
				now = lower;
			}

			@Override
			public final boolean hasNext() {
				return !isGreater(now);
			}

			@Override
			public final C next() {
				C ret = now;
				now = func.apply(now);
				return ret;
			}

			@Override
			public final void remove() {
				throw new UnsupportedOperationException();
			}
		}

		protected class EmptyIter implements Iterator<C> {

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public C next() {
				return null;
			}

			@Override
			public final void remove() {
				throw new UnsupportedOperationException();
			}

		}

		@Override
		public Iterator<C> iterator() {
			return lower == null || upper == null ? new EmptyIter() : new Iter();
		}

		public int getDistance() {
			C check = upper;
			int ret = 0;
			while (lower != check) {
				check = func.apply(check);
				++ret;
			}
			return ret;
		}
	}

	public static class IntRange extends IterableRange<Integer> {

		private static final long serialVersionUID = 5623995336491967216L;
		private final boolean useFastIter;

		private static class Next implements UnaryOperator<Integer> {

			@Override
			public Integer apply(Integer value) {
				return value + 1;
			}
		}

		protected IntRange() {
			super(null, BoundType.CLOSED, null, BoundType.CLOSED, new Next());
			useFastIter = true;
		}

		protected IntRange(UnaryOperator<Integer> func) {
			super(null, BoundType.CLOSED, null, BoundType.CLOSED, func);
			useFastIter = false;
		}

		protected IntRange(int lower, BoundType lowerType, int upper, BoundType upperType) {
			super(lower, lowerType, upper, upperType, new Next());
			useFastIter = true;
		}

		protected IntRange(int lower, BoundType lowerType, int upper, BoundType upperType, UnaryOperator<Integer> func) {
			super(lower, lowerType, upper, upperType, func);
			useFastIter = false;
		}

		public static IntRange range(int lower, BoundType lowerType, int upper, BoundType upperType) {
			if (lower > upper) return new IntRange();
			if (lowerType == BoundType.OPEN) ++lower;
			if (upperType == BoundType.OPEN) --upper;
			return new IntRange(lower, BoundType.CLOSED, upper, BoundType.CLOSED);
		}

		public static IntRange range(int lower, BoundType lowerType, int upper, BoundType upperType, UnaryOperator<Integer> func) {
			if (lower > upper) return new IntRange(func);
			if (lowerType == BoundType.OPEN) ++lower;
			if (upperType == BoundType.OPEN) --upper;
			return new IntRange(lower, BoundType.CLOSED, upper, BoundType.CLOSED, func);
		}

		public static IntRange open(int lower, int upper) {
			return range(lower, BoundType.OPEN, upper, BoundType.OPEN);
		}

		public static IntRange open(int lower, int upper, UnaryOperator<Integer> func) {
			return range(lower, BoundType.OPEN, upper, BoundType.OPEN, func);
		}

		public static IntRange open(int upper) {
			return range(0, BoundType.CLOSED, upper, BoundType.OPEN);
		}

		public static IntRange open(int upper, UnaryOperator<Integer> func) {
			return range(0, BoundType.CLOSED, upper, BoundType.OPEN, func);
		}

		public static IntRange openClosed(int lower, int upper) {
			return range(lower, BoundType.OPEN, upper, BoundType.CLOSED);
		}

		public static IntRange openClosed(int lower, int upper, UnaryOperator<Integer> func) {
			return range(lower, BoundType.OPEN, upper, BoundType.CLOSED, func);
		}

		public static IntRange closedOpen(int lower, int upper) {
			return range(lower, BoundType.CLOSED, upper, BoundType.OPEN);
		}

		public static IntRange closedOpen(int lower, int upper, UnaryOperator<Integer> func) {
			return range(lower, BoundType.CLOSED, upper, BoundType.OPEN, func);
		}

		public static IntRange closed(int lower, int upper) {
			return range(lower, BoundType.CLOSED, upper, BoundType.CLOSED);
		}

		public static IntRange closed(int lower, int upper, UnaryOperator<Integer> func) {
			return range(lower, BoundType.CLOSED, upper, BoundType.CLOSED, func);
		}

		public static IntRange closed(int upper) {
			return range(0, BoundType.CLOSED, upper, BoundType.CLOSED);
		}

		public static IntRange closed(int upper, UnaryOperator<Integer> func) {
			return range(0, BoundType.CLOSED, upper, BoundType.CLOSED, func);
		}

		public static IntRange singleton(int value) {
			return range(value, BoundType.CLOSED, value, BoundType.CLOSED);
		}

		public static IntRange singleton(int value, UnaryOperator<Integer> func) {
			return range(value, BoundType.CLOSED, value, BoundType.CLOSED, func);
		}

		private class FastIter implements Iterator<Integer> {

			int now;

			public FastIter() {
				now = lower;
			}

			@Override
			public final boolean hasNext() {
				return now <= upper;
			}

			@Override
			public final Integer next() {
				return now++ ;
			}

			@Override
			public final void remove() {
				throw new UnsupportedOperationException();
			}
		}

		private class Iter implements Iterator<Integer> {

			int now;

			public Iter() {
				now = lower;
			}

			@Override
			public final boolean hasNext() {
				return now <= upper;
			}

			@Override
			public final Integer next() {
				int ret = now;
				now = func.apply(now);
				return ret;
			}

			@Override
			public final void remove() {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public Iterator<Integer> iterator() {
			return lower == null || upper == null ? new EmptyIter() : useFastIter ? new FastIter() : new Iter();
		}

		@Override
		public int getDistance() {
			int ret = upper - lower;
			if (upperType == BoundType.CLOSED) ++ret;
			return ret;
		}

		public int getClosedLower() {
			return lower;
		}

		public int getOpenLower() {
			return lower - 1;
		}

		public int getClosedUpper() {
			return upperType == BoundType.CLOSED ? upper : upper - 1;
		}

		public int getOpenUpper() {
			return upperType == BoundType.CLOSED ? upper + 1 : upper;
		}

		/**
		 * 区間スケジューリングを行います。<br>
		 * 計算量は要素数Nに対してO(NlogN)です。
		 *
		 * @param ranges 区間の集合
		 * @return 区間スケジューリングを行った際の一つの解
		 */
		public static List<IntRange> intScheduling(List<IntRange> ranges) {
			PriorityQueue<IntRange> pq = new PriorityQueue<IntRange>((l, r) -> l.compareUpper(r.upper, r.upperType));
			pq.addAll(ranges);
			List<IntRange> ret = new ArrayList<>();
			if (pq.isEmpty()) return ret;
			IntRange last = pq.poll();
			ret.add(last);
			while (!pq.isEmpty()) {
				IntRange tmp = pq.poll();
				if (tmp.compareLower(last.upper, last.upperType) > 0) {
					ret.add(tmp);
					last = tmp;
				}
			}
			return ret;
		}
	}

	/**
	 * 演算が結合法則を満たすことを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface Associative<T> extends BinaryOperator<T> {

		/**
		 * repeat個のelementを順次演算した値を返します。
		 *
		 * @param element 演算する値
		 * @param repeat  繰り返す回数、1以上であること
		 * @return 演算を+として、element + element + ... + elementと演算をrepeat-1回行った値
		 */
		public default T hyper(T element, int repeat) {
			if (repeat < 1) throw new IllegalArgumentException("undefined operation");
			T ret = element;
			--repeat;
			for (T mul = element; repeat > 0; repeat >>= 1, mul = apply(mul, mul))
				if ((repeat & 1) != 0) ret = apply(ret, mul);
			return ret;
		}
	}

	/**
	 * この演算が逆元を持つことを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface Inverse<T> extends BinaryOperator<T> {

		public T inverse(T element);
	}

	/**
	 * 演算が交換法則を満たすことを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface Commutative<T> extends BinaryOperator<T> {

	}

	/**
	 * 演算が単位元を持つことを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface Identity<T> extends BinaryOperator<T> {

		/**
		 * 単位元を返します。
		 *
		 * @return 単位元
		 */
		public T identity();
	}

	/**
	 * 演算が群であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface Group<T> extends Monoid<T>, Inverse<T> {

		/**
		 * repeat個のelementを順次演算した値を返します。
		 *
		 * @param element 演算する値
		 * @param repeat  繰り返す回数
		 * @return 演算を+として、element + element + ... + elementと演算をrepeat-1回行った値
		 */
		@Override
		public default T hyper(T element, int repeat) {
			T ret = identity();
			if (repeat < 0) {
				repeat = -repeat;
				for (T mul = element; repeat > 0; repeat >>= 1, mul = apply(mul, mul))
					if ((repeat & 1) != 0) ret = apply(ret, mul);
				return inverse(ret);
			}
			for (T mul = element; repeat > 0; repeat >>= 1, mul = apply(mul, mul))
				if ((repeat & 1) != 0) ret = apply(ret, mul);
			return ret;
		}
	}

	/**
	 * 演算がモノイドであることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface Monoid<T> extends Associative<T>, Identity<T> {

		/**
		 * repeat個のelementを順次演算した値を返します。
		 *
		 * @param element 演算する値
		 * @param repeat  繰り返す回数、0以上であること
		 * @return 演算を+として、element + element + ... + elementと演算をrepeat-1回行った値
		 */
		@Override
		public default T hyper(T element, int repeat) {
			if (repeat < 0) throw new IllegalArgumentException("undefined operation");
			T ret = identity();
			for (T mul = element; repeat > 0; repeat >>= 1, mul = apply(mul, mul))
				if ((repeat & 1) != 0) ret = apply(ret, mul);
			return ret;
		}
	}

	/**
	 * 演算が可換モノイドであることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface CommutativeMonoid<T> extends Monoid<T>, Commutative<T> {

	}

	/**
	 * 演算がアーベル群(可換群)であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 */
	public interface Abelian<T> extends Group<T>, CommutativeMonoid<T> {

	}

	/**
	 * 演算が半環であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface Semiring<T, A extends CommutativeMonoid<T>, M extends Monoid<T>> {

		public A getAddition();

		public M getMultiplication();

		public default T add(T left, T right) {
			return getAddition().apply(left, right);
		}

		public default T multiply(T left, T right) {
			return getMultiplication().apply(left, right);
		}

		public default T additiveIdentity() {
			return getAddition().identity();
		}

		public default T multipleIdentity() {
			return getMultiplication().identity();
		}

		public default int characteristic() {
			return 0;
		}
	}

	/**
	 * 演算が環であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface Ring<T, A extends Abelian<T>, M extends Monoid<T>> extends Semiring<T, A, M> {

	}

	/**
	 * 演算が可換環に属することを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface CommutativeRing<T, A extends Abelian<T>, M extends CommutativeMonoid<T>> extends Ring<T, A, M> {

	}

	/**
	 * 演算が整域であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface IntegralDomain<T, A extends Abelian<T>, M extends CommutativeMonoid<T>> extends CommutativeRing<T, A, M> {

		public boolean isDivisible(T left, T right);

		public T divide(T left, T right);
	}

	/**
	 * 演算が整閉整域であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface IntegrallyClosedDomain<T, A extends Abelian<T>, M extends CommutativeMonoid<T>> extends IntegralDomain<T, A, M> {

	}

	/**
	 * 演算がGCD整域であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface GCDDomain<T, A extends Abelian<T>, M extends CommutativeMonoid<T>> extends IntegrallyClosedDomain<T, A, M> {

		public T gcd(T left, T right);

		public T lcm(T left, T right);
	}

	/**
	 * 素元を提供します。
	 *
	 * @author 31536000
	 *
	 * @param <T> 演算の型
	 */
	public static class PrimeElement<T> {

		public final T element;

		public PrimeElement(T element) {
			this.element = element;
		}
	}

	public interface MultiSet<E> extends Collection<E> {

		public int add(E element, int occurrences);

		public int count(Object element);

		public Set<E> elementSet();

		public boolean remove(Object element, int occurrences);

		public int setCount(E element, int count);

		public boolean setCount(E element, int oldCount, int newCount);
	}

	/**
	 * 演算が一意分解整域であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface UniqueFactorizationDomain<T, A extends Abelian<T>, M extends CommutativeMonoid<T>> extends GCDDomain<T, A, M> {

		public MultiSet<PrimeElement<T>> PrimeFactorization(T x);
	}

	/**
	 * 演算が主イデアル整域であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface PrincipalIdealDomain<T, A extends Abelian<T>, M extends CommutativeMonoid<T>> extends UniqueFactorizationDomain<T, A, M> {

	}

	/**
	 * 演算がユークリッド整域であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface EuclideanDomain<T, A extends Abelian<T>, M extends CommutativeMonoid<T>> extends PrincipalIdealDomain<T, A, M> {

		public T reminder(T left, T right);
	}

	/**
	 * 演算が体であることを示すために使用するマーカー・インターフェースです。
	 *
	 * @author 31536000
	 *
	 * @param <T> 二項演算の型
	 * @param <A> 和に関する演算
	 * @param <M> 積に関する演算
	 */
	public interface Field<T, A extends Abelian<T>, M extends Abelian<T>> extends EuclideanDomain<T, A, M> {

		@Override
		public default boolean isDivisible(T left, T right) {
			return !right.equals(additiveIdentity());
		}

		@Override
		public default T divide(T left, T right) {
			if (isDivisible(left, right)) throw new ArithmeticException("divide by Additive Identify");
			return multiply(left, getMultiplication().inverse(right));
		}

		@Override
		public default T reminder(T left, T right) {
			if (isDivisible(left, right)) throw new ArithmeticException("divide by Additive Identify");
			return additiveIdentity();
		}

		@Override
		public default T gcd(T left, T right) {
			return multipleIdentity();
		}

		@Override
		public default T lcm(T left, T right) {
			return multipleIdentity();
		}

		@Override
		public default MultiSet<PrimeElement<T>> PrimeFactorization(T x) {
			HashMultiSet<PrimeElement<T>> ret = HashMultiSet.create(1);
			ret.add(new PrimeElement<T>(x));
			return ret;
		}
	}

	public static class HashMultiSet<E> implements MultiSet<E>, Serializable {

		private static final long serialVersionUID = -8378919645386251159L;
		private final transient HashMap<E, Integer> map;
		private transient int size;

		private HashMultiSet() {
			map = new HashMap<>();
			size = 0;
		}

		private HashMultiSet(int distinctElements) {
			map = new HashMap<>(distinctElements);
			size = 0;
		}

		public static <E> HashMultiSet<E> create() {
			return new HashMultiSet<>();
		}

		public static <E> HashMultiSet<E> create(int distinctElements) {
			return new HashMultiSet<>(distinctElements);
		}

		public static <E> HashMultiSet<E> create(Iterable<? extends E> elements) {
			HashMultiSet<E> ret = new HashMultiSet<>();
			for (E i : elements)
				ret.map.compute(i, (v, e) -> e == null ? 1 : ++e);
			return ret;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public boolean isEmpty() {
			return size == 0;
		}

		@Override
		public boolean contains(Object o) {
			return map.containsKey(o);
		}

		private class Iter implements Iterator<E> {

			private final Iterator<Entry<E, Integer>> iter = map.entrySet().iterator();
			private E value;
			private int count = 0;

			@Override
			public boolean hasNext() {
				if (count > 0) return true;
				if (iter.hasNext()) {
					Entry<E, Integer> entry = iter.next();
					value = entry.getKey();
					count = entry.getValue();
					return true;
				}
				return false;
			}

			@Override
			public E next() {
				--count;
				return value;
			}

		}

		@Override
		public Iterator<E> iterator() {
			return new Iter();
		}

		@Override
		public Object[] toArray() {
			Object[] ret = new Object[size];
			int read = 0;
			for (Entry<E, Integer> i : map.entrySet())
				Arrays.fill(ret, read, read += i.getValue(), i.getKey());
			return ret;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			Object[] src = toArray();
			if (a.length < src.length) {
				@SuppressWarnings("unchecked")
				T[] ret = (T[])Arrays.copyOfRange(src, 0, src.length, a.getClass());
				return ret;
			}
			System.arraycopy(src, 0, a, 0, src.length);
			return a;
		}

		@Override
		public boolean add(E e) {
			add(e, 1);
			return true;
		}

		@Override
		public boolean remove(Object o) {
			return remove(o, 1);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			boolean ret = true;
			for (Object i : c)
				ret |= contains(i);
			return ret;
		}

		@Override
		public boolean addAll(Collection<? extends E> c) {
			boolean ret = false;
			for (E i : c)
				ret |= add(i);
			return ret;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			boolean ret = false;
			for (Object i : c)
				ret |= remove(i);
			return ret;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return removeAll(c);
		}

		@Override
		public void clear() {
			map.clear();
			size = 0;
		}

		@Override
		public int add(E element, int occurrences) {
			size += occurrences;
			return map.compute(element, (k, v) -> v == null ? occurrences : v + occurrences) - occurrences;
		}

		@Override
		public int count(Object element) {
			return map.getOrDefault(element, 0);
		}

		@Override
		public Set<E> elementSet() {
			return map.keySet();
		}

		public Set<Entry<E, Integer>> entrySet() {
			return map.entrySet();
		}

		@Override
		public boolean remove(Object element, int occurrences) {
			try {
				@SuppressWarnings("unchecked")
				E put = (E)element;
				return map.compute(put, (k, v) -> {
					if (v == null) return null;
					if (v < occurrences) {
						size -= v;
						return null;
					}
					size -= occurrences;
					return v - occurrences;
				}) != null;
			} catch (ClassCastException E) {
				return false;
			}
		}

		@Override
		public int setCount(E element, int count) {
			Integer ret = map.put(element, count);
			int ret2 = ret == null ? 0 : ret;
			size += count - ret2;
			return ret2;
		}

		@Override
		public boolean setCount(E element, int oldCount, int newCount) {
			boolean ret = map.replace(element, oldCount, newCount);
			if (ret) size += newCount - oldCount;
			return ret;
		}

	}

	public static class ModInteger extends Number implements Field<ModInteger, Abelian<ModInteger>, Abelian<ModInteger>> {

		private static final long serialVersionUID = -8595710127161317579L;
		private final int mod;
		private int num;

		private final Addition add;
		private final Multiplication mul;

		private class Addition implements Abelian<ModInteger> {

			@Override
			public ModInteger identity() {
				return new ModInteger(mod, 0);
			}

			@Override
			public ModInteger inverse(ModInteger element) {
				return new ModInteger(element, element.mod - element.num);
			}

			@Override
			public ModInteger apply(ModInteger left, ModInteger right) {
				return new ModInteger(left).addEqual(right);
			}
		}

		private class Multiplication implements Abelian<ModInteger> {

			@Override
			public ModInteger identity() {
				return new ModInteger(mod, 1);
			}

			@Override
			public ModInteger apply(ModInteger left, ModInteger right) {
				return new ModInteger(left).multiplyEqual(right);
			}

			@Override
			public ModInteger inverse(ModInteger element) {
				return new ModInteger(element, element.inverse(element.num));
			}

		}

		@Override
		public int characteristic() {
			return mod;
		}

		public ModInteger(int mod) {
			this.mod = mod;
			num = 0;
			add = new Addition();
			mul = new Multiplication();
		}

		public ModInteger(int mod, int num) {
			this.mod = mod;
			this.num = validNum(num);
			add = new Addition();
			mul = new Multiplication();
		}

		public ModInteger(ModInteger n) {
			mod = n.mod;
			num = n.num;
			add = n.add;
			mul = n.mul;
		}

		private ModInteger(ModInteger n, int num) {
			mod = n.mod;
			this.num = num;
			add = n.add;
			mul = n.mul;
		}

		private int validNum(int n) {
			n %= mod;
			if (n < 0) n += mod;
			return n;
		}

		private int validNum(long n) {
			n %= mod;
			if (n < 0) n += mod;
			return (int)n;
		}

		protected int inverse(int n) {
			int m = mod, u = 0, v = 1, t;
			while (n != 0) {
				t = m / n;
				m -= t * n;
				u -= t * v;
				if (m != 0) {
					t = n / m;
					n -= t * m;
					v -= t * u;
				} else {
					v %= mod;
					if (v < 0) v += mod;
					return v;
				}
			}
			u %= mod;
			if (u < 0) u += mod;
			return u;
		}

		public boolean isPrime(int n) {
			if ((n & 1) == 0) return false; // 偶数
			for (int i = 3, j = 8, k = 9; k <= n; i += 2, k += j += 8)
				if (n % i == 0) return false;
			return true;
		}

		@Override
		public int intValue() {
			return num;
		}

		@Override
		public long longValue() {
			return num;
		}

		@Override
		public float floatValue() {
			return num;
		}

		@Override
		public double doubleValue() {
			return num;
		}

		protected ModInteger getNewInstance(ModInteger mod) {
			return new ModInteger(mod);
		}

		public ModInteger add(int n) {
			return getNewInstance(this).addEqual(n);
		}

		public ModInteger add(long n) {
			return getNewInstance(this).addEqual(n);
		}

		public ModInteger add(ModInteger n) {
			return getNewInstance(this).addEqual(n);
		}

		public ModInteger addEqual(int n) {
			num = validNum(num + n);
			return this;
		}

		public ModInteger addEqual(long n) {
			num = validNum(num + n);
			return this;
		}

		public ModInteger addEqual(ModInteger n) {
			if ((num += n.num) >= mod) num -= mod;
			return this;
		}

		public ModInteger subtract(int n) {
			return getNewInstance(this).subtractEqual(n);
		}

		public ModInteger subtract(long n) {
			return getNewInstance(this).subtractEqual(n);
		}

		public ModInteger subtract(ModInteger n) {
			return getNewInstance(this).subtractEqual(n);
		}

		public ModInteger subtractEqual(int n) {
			num = validNum(num - n);
			return this;
		}

		public ModInteger subtractEqual(long n) {
			num = validNum(num - n);
			return this;
		}

		public ModInteger subtractEqual(ModInteger n) {
			if ((num -= n.num) < 0) num += mod;
			return this;
		}

		public ModInteger multiply(int n) {
			return getNewInstance(this).multiplyEqual(n);
		}

		public ModInteger multiply(long n) {
			return getNewInstance(this).multiplyEqual(n);
		}

		public ModInteger multiply(ModInteger n) {
			return getNewInstance(this).multiplyEqual(n);
		}

		public ModInteger multiplyEqual(int n) {
			num = (int)((long)num * n % mod);
			if (num < 0) num += mod;
			return this;
		}

		public ModInteger multiplyEqual(long n) {
			return multiplyEqual((int)(n % mod));
		}

		public ModInteger multiplyEqual(ModInteger n) {
			num = (int)((long)num * n.num % mod);
			return this;
		}

		public ModInteger divide(int n) {
			return getNewInstance(this).divideEqual(n);
		}

		public ModInteger divide(long n) {
			return getNewInstance(this).divideEqual(n);
		}

		public ModInteger divide(ModInteger n) {
			return getNewInstance(this).divideEqual(n);
		}

		public ModInteger divideEqual(int n) {
			num = (int)((long)num * inverse(validNum(n)) % mod);
			return this;
		}

		public ModInteger divideEqual(long n) {
			return divideEqual((int)(n % mod));
		}

		public ModInteger divideEqual(ModInteger n) {
			num = (int)((long)num * n.inverse(n.num) % mod);
			return this;
		}

		public ModInteger pow(int n) {
			return getNewInstance(this).powEqual(n);
		}

		public ModInteger pow(long n) {
			return getNewInstance(this).powEqual(n);
		}

		public ModInteger pow(ModInteger n) {
			return getNewInstance(this).powEqual(n);
		}

		public ModInteger powEqual(int n) {
			long ans = 1, num = this.num;
			if (n < 0) {
				n = -n;
				while (n != 0) {
					if ((n & 1) != 0) ans = ans * num % mod;
					n >>>= 1;
					num = num * num % mod;
				}
				this.num = inverse((int)ans);
				return this;
			}
			while (n != 0) {
				if ((n & 1) != 0) ans = ans * num % mod;
				n >>>= 1;
				num = num * num % mod;
			}
			this.num = (int)ans;
			return this;
		}

		public ModInteger powEqual(long n) {
			return powEqual((int)(n % (mod - 1)));
		}

		public ModInteger powEqual(ModInteger n) {
			long num = this.num;
			this.num = 1;
			int mul = n.num;
			while (mul != 0) {
				if ((mul & 1) != 0) this.num *= num;
				mul >>>= 1;
				num *= num;
				num %= mod;
			}
			return this;
		}

		public ModInteger equal(int n) {
			num = validNum(n);
			return this;
		}

		public ModInteger equal(long n) {
			num = validNum(n);
			return this;
		}

		public ModInteger equal(ModInteger n) {
			num = n.num;
			return this;
		}

		public int toInt() {
			return num;
		}

		public int getMod() {
			return mod;
		}

		@Override
		public boolean equals(Object x) {
			if (x instanceof ModInteger) return ((ModInteger)x).num == num && ((ModInteger)x).mod == mod;
			return false;
		}

		@Override
		public int hashCode() {
			return num ^ mod;
		}

		@Override
		public String toString() {
			return String.valueOf(num);
		}

		@Deprecated
		public String debug() {
			int min = num, ans = 1;
			for (int i = 2; i < min; ++i) {
				int tmp = multiply(i).num;
				if (min > tmp) {
					min = tmp;
					ans = i;
				}
			}
			return min + "/" + ans;
		}

		@Override
		public Addition getAddition() {
			return add;
		}

		@Override
		public Multiplication getMultiplication() {
			return mul;
		}
	}

	/**
	 * 素数を法とする演算上で、組み合わせの計算を高速に行います。
	 *
	 * @author 31536000
	 *
	 */
	public static class ModUtility {

		private final int mod;
		private int[] fact, inv, invfact;

		/**
		 * modを法として、演算を行います。
		 *
		 * @param mod 法とする素数
		 */
		public ModUtility(Prime mod) {
			this(mod, 2);
		}

		/**
		 * modを法として、演算を行います。
		 *
		 * @param mod  法とする素数
		 * @param calc 予め前計算しておく大きさ
		 */
		public ModUtility(Prime mod, int calc) {
			this.mod = mod.prime;
			precalc(calc);
		}

		/**
		 * calcの大きさだけ、前計算を行います。
		 *
		 * @param calc 前計算をする大きさ
		 */
		public void precalc(int calc) {
			++calc;
			if (calc < 2) calc = 2;
			if (calc > mod) calc = mod;
			fact = new int[calc];
			inv = new int[calc];
			invfact = new int[calc];
			fact[0] = invfact[0] = fact[1] = invfact[1] = inv[1] = 1;
			for (int i = 2; i < calc; ++i) {
				fact[i] = (int)((long)fact[i - 1] * i % mod);
				inv[i] = (int)(mod - (long)inv[mod % i] * (mod / i) % mod);
				invfact[i] = (int)((long)invfact[i - 1] * inv[i] % mod);
			}
		}

		/**
		 * modを法とする剰余環上で振舞う整数を返します。
		 *
		 * @return modを法とする整数、初期値は0
		 */
		public ModInteger create() {
			return new ModInt();
		}

		/**
		 * modを法とする剰余環上で振舞う整数を返します。
		 *
		 * @param n 初期値
		 * @return modを法とする整数
		 */
		public ModInteger create(int n) {
			return new ModInt(n);
		}

		private class ModInt extends ModInteger {

			private static final long serialVersionUID = -2435281861935422575L;

			public ModInt() {
				super(mod);
			}

			public ModInt(int n) {
				super(mod, n);
			}

			public ModInt(ModInteger mod) {
				super(mod);
			}

			@Override
			protected ModInteger getNewInstance(ModInteger mod) {
				return new ModInt(mod);
			}

			@Override
			protected int inverse(int n) {
				return ModUtility.this.inverse(n);
			}
		}

		/**
		 * modを法として、nの逆元を返します。<br>
		 * 計算量はO(log n)です。
		 *
		 * @param n 逆元を求めたい値
		 * @return 逆元
		 */
		public int inverse(int n) {
			try {
				if (inv.length > n) return inv[n];
				int m = mod, u = 0, v = 1, t;
				while (n != 0) {
					t = m / n;
					m -= t * n;
					u -= t * v;
					if (m != 0) {
						t = n / m;
						n -= t * m;
						v -= t * u;
					} else {
						v %= mod;
						if (v < 0) v += mod;
						return v;
					}
				}
				u %= mod;
				if (u < 0) u += mod;
				return u;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new IllegalArgumentException();
			}
		}

		/**
		 * n!を、modを法として求めた値を返します。<br>
		 * 計算量はO(n)です。
		 *
		 * @param n 階乗を求めたい値
		 * @return nの階乗をmodで割った余り
		 */
		public int factorial(int n) {
			try {
				if (fact.length > n) return fact[n];
				long ret = fact[fact.length - 1];
				for (int i = fact.length; i <= n; ++i)
					ret = ret * i % mod;
				return (int)ret;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new IllegalArgumentException();
			}
		}

		/**
		 * nPkをmodで割った余りを求めます。<br>
		 * 計算量はO(n-k)です。
		 *
		 * @param n 左辺
		 * @param k 右辺
		 * @return nPkをmodで割った余り
		 */
		public int permutation(int n, int k) {
			if (n < 0) throw new IllegalArgumentException();
			if (n < k) return 0;
			if (fact.length > n) return (int)((long)fact[n] * invfact[n - k] % mod);
			long ret = 1;
			for (int i = n - k + 1; i <= n; ++i)
				ret = ret * i % mod;
			return (int)ret;
		}

		/**
		 * nCkをmodで割った余りを求めます。<br>
		 * 計算量はO(min(plogn, n-k))です。
		 *
		 * @param n 左辺
		 * @param k 右辺
		 * @return nCkをmodで割った余り
		 */
		public int combination(int n, int k) {
			if (n < 0) throw new IllegalArgumentException();
			if (n < k) return 0;
			if (fact.length > n) return (int)((long)fact[n] * invfact[k] % mod * invfact[n - k] % mod);
			long ret = 1;
			if (n >= mod) {
				if (mod == 2) return (~n & k) == 0 ? 1 : 0;
				while (n > 0) {
					ret = ret * combination(n % mod, k % mod) % mod;
					n /= mod;
					k /= mod;
				}
				return (int)ret;
			}
			if (n < 2 * k) k = n - k;
			ret = invfact.length > k ? invfact[k] : inverse(factorial(k));
			for (int i = n - k + 1; i <= n; ++i)
				ret = ret * i % mod;
			return (int)ret;
		}

		/**
		 * 他項係数をmodで割った余りを求めます。<br>
		 * ]
		 * 計算量はO(n)です。
		 *
		 * @param n 左辺
		 * @param k 右辺、合計がn以下である必要がある
		 * @return 他項係数
		 */
		public int multinomial(int n, int... k) {
			int sum = 0;
			long ret = factorial(n);
			if (fact.length > n) {
				for (int i : k) {
					if (i < 0) throw new IllegalArgumentException();
					ret = ret * invfact[i] % mod;
					sum += i;
				}
				if (sum > n) return 0;
				ret = ret * invfact[n - sum] % mod;
			} else {
				for (int i : k) {
					if (i < 0) throw new IllegalArgumentException();
					if (invfact.length > i) ret = ret * invfact[i] % mod;
					else ret = ret * inverse(factorial(i)) % mod;
					sum += i;
				}
				if (sum > n) return 0;
				if (invfact.length > n - sum) ret = ret * invfact[n - sum] % mod;
				else ret = ret * inverse(factorial(n - sum)) % mod;
			}
			return (int)ret;
		}

		/**
		 * n個からk個を選ぶ重複組み合わせnHkをmodで割った余りを求めます。<br>
		 * 計算量はO(min(n, k))です。
		 *
		 * @param n 左辺
		 * @param k 右辺
		 * @return nHkをmodで割った余り
		 */
		public int multichoose(int n, int k) {
			return combination(mod(n + k - 1), k);
		}

		/**
		 * カタラン数C(n)をmodで割った余りを求めます。<br>
		 * 計算量はO(n)です。
		 *
		 * @param n 求めたいカタラン数の番号
		 * @return カタラン数
		 */
		public int catalan(int n) {
			return divide(combination(mod(2 * n), n), mod(n + 1));
		}

		/**
		 * 第一種スターリング数S(n, k)をmodで割った余りを求めます。<br>
		 * 計算量はO(nk)です。 // TODO NTTを使うとO(n log n)、未実装
		 *
		 * @param n 左辺
		 * @param k 右辺
		 * @return S(n, k)をmodで割った余り
		 */
		public int firstStirling(int n, int k) {
			int[] stirling = new int[(n + 1) * (k + 1)];
			stirling[0] = 1;
			int h = k + 1;
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < k; ++j) {
					int tmp = stirling[i * h + j] + (int)((long)i * stirling[i * h + j + 1] % mod);
					stirling[(i + 1) * h + j + 1] = tmp >= mod ? tmp - mod : tmp;
				}
			}
			return stirling[stirling.length - 1];
		}

		/**
		 * 第二種スターリング数S(n, k)をmodで割った余りを求めます。<br>
		 * 計算量はO(k)です。
		 *
		 * @param n 左辺
		 * @param k 右辺
		 * @return S(n, k)をmodで割った余り
		 */
		public int secondStirling(int n, int k) {
			if (k == 0) return n == 0 ? 1 : 0;
			int[] sieve = new int[k + 1], prime = new int[k + 1];
			int size = 0;
			sieve[1] = 1;
			for (int i = 2; i <= k; ++i) {
				if (sieve[i] == 0) prime[size++ ] = sieve[i] = i;
				for (int j = 0, s; j < size && prime[j] <= sieve[i] && (s = i * prime[j]) <= k; ++j)
					sieve[s] = prime[j];
			}
			long ans = 0;
			for (int i = 1, s; i <= k; ++i) {
				long tmp = (long)combination(k, i) * (prime[i] = (s = sieve[i]) == i ? pow(i,
						n) : (int)((long)prime[s] * prime[i / s] % mod)) % mod;
				ans += (k - i & 1) != 0 ? -tmp : tmp;
			}
			return (int)((long)mod(ans) * invfact[k] % mod);
		}

		/**
		 * ベル数B(n, k)をmodで割った余りを求めます。<br>
		 * 計算量はO(k)です。
		 *
		 * @param n 左辺
		 * @param k 右辺
		 * @return B(n, k)をmodで割った余り
		 */
		public int bell(int n, int k) {
			if (k == 0) return n == 0 ? 1 : 0;
			int[] sieve = new int[k + 1], prime = new int[k + 1];
			int size = 0;
			sieve[1] = 1;
			long sum = 0;
			for (int i = 2; i <= k; ++i) {
				if (sieve[i] == 0) prime[size++ ] = sieve[i] = i;
				for (int j = 0, s; j < size && prime[j] <= sieve[i] && (s = i * prime[j]) <= k; ++j)
					sieve[s] = prime[j];
				sum += (i & 1) != 0 ? -invfact[i] : invfact[i];
			}
			sum = mod(sum);
			long ans = 0;
			for (int i = 0, s; i <= k; ++i) {
				long tmp = (long)(prime[i] = (s = sieve[i]) == i ? pow(i,
						n) : (int)((long)prime[s] * prime[i / s] % mod)) * invfact[i] % mod;
				ans += tmp * sum % mod;
				if ((sum -= (k - i & 1) != 0 ? -invfact[k - i] : invfact[k - i]) < 0) sum += mod;
			}
			return mod(ans);
		}

		/**
		 * ベル数B(n)をmodで割った余りを求めます。<br>
		 * 計算量はO(n)です。
		 *
		 * @param n 求めたいベル数の番号
		 * @return B(n)
		 */
		public int bell(int n) {
			return bell(n, n);
		}

		/**
		 * 分割数P(n, k)をmodで割った余りを求めます。<br>
		 * 計算量はO(nk)です。 // TODO NTTを使うとO(n log n)、未実装
		 *
		 * @param n 左辺
		 * @param k 右辺
		 * @return P(n, k)をmodで割った余り
		 */
		public int pertition(int n, int k) {
			int[] pertition = new int[(n + 1) * (k + 1)];
			pertition[0] = 1;
			int h = k + 1;
			for (int i = 0; i <= n; ++i) {
				for (int j = 1, l = Math.min(i, k); j <= l; ++j)
					pertition[i * h + j] = pertition[i * h + j - 1] + pertition[(i - j) * h + j];
				for (int j = i; j < k; ++j)
					pertition[i * h + j + 1] = pertition[i * h + j];
			}
			return pertition[n * h + k];
		}

		/**
		 * 分割数P(n)をmodで割った余りを求めます。<br>
		 * 計算量はO(n sqrt(n))です。 // TODO NTTを使うとO(n log n)、未実装
		 *
		 * @param n 求めたい分割数の番号
		 * @return P(n)
		 */
		public int pertition(int n) {
			long[] pertition = new long[n + 1];
			pertition[0] = 1;
			for (int i = 1; i <= n; ++i) {
				for (int j = 1, t; (t = i - (j * (3 * j - 1) >> 1)) >= 0; ++j) {
					pertition[i] += (j & 1) != 0 ? pertition[t] : -pertition[t];
				}
				for (int j = 1, t; (t = i - (j * (3 * j + 1) >> 1)) >= 0; ++j) {
					pertition[i] += (j & 1) != 0 ? pertition[t] : -pertition[t];
				}
				pertition[i] %= mod;
			}
			return (int)pertition[n];
		}

		/**
		 * nのm乗をmodで割った余りを求めます。<br>
		 * 計算量はO(log m)です。
		 *
		 * @param n 床
		 * @param m 冪指数
		 * @return n^mをmodで割った余り
		 */
		public int pow(int n, int m) {
			long ans = 1, num = n;
			if (m < 0) {
				m = -m;
				while (m != 0) {
					if ((m & 1) != 0) ans = ans * num % mod;
					m >>>= 1;
					num = num * num % mod;
				}
				return inverse((int)ans);
			}
			while (m != 0) {
				if ((m & 1) != 0) ans = ans * num % mod;
				m >>>= 1;
				num = num * num % mod;
			}
			return (int)ans;
		}

		/**
		 * nのm乗をmodで割った余りを求めます。<br>
		 * 計算量はO(log m)です。
		 *
		 * @param n 床
		 * @param m 冪指数
		 * @return n^mをmodで割った余り
		 */
		public int pow(long n, long m) {
			return pow((int)(n % mod), (int)(n % (mod - 1)));
		}

		/**
		 * 現在のmod値のトーシェント数を返します。<br>
		 * なお、これはmod-1に等しいです。
		 *
		 * @return トーシェント数
		 */
		public int totient() {
			return mod - 1;
		}

		/**
		 * nのトーシェント数を返します。<br>
		 * 計算量はO(sqrt n)です。
		 *
		 * @param n トーシェント数を求めたい値
		 * @return nのトーシェント数
		 */
		public static int totient(int n) {
			int totient = n;
			for (int i = 2; i * i <= n; ++i) {
				if (n % i == 0) {
					totient = totient / i * (i - 1);
					while ((n %= i) % i == 0);
				}
			}
			if (n != 1) totient = totient / n * (n - 1);
			return totient;
		}

		/**
		 * nをmodで割った余りを返します。
		 *
		 * @param n 演算する値
		 * @return nをmodで割った余り
		 */
		public int mod(int n) {
			return (n %= mod) < 0 ? n + mod : n;
		}

		/**
		 * nをmodで割った余りを返します。
		 *
		 * @param n 演算する値
		 * @return nをmodで割った余り
		 */
		public int mod(long n) {
			return (int)((n %= mod) < 0 ? n + mod : n);
		}

		/**
		 * nをmodで割った余りを返します。
		 *
		 * @param n 演算する値
		 * @return nをmodで割った余り
		 */
		public int mod(PrimeFactor n) {
			int ret = 1;
			for (Entry<Prime, Integer> i : n.primeFactor.entrySet())
				ret = multiply(ret, pow(i.getKey().prime, i.getValue()));
			return ret;
		}

		/**
		 * n+mをmodで割った余りを返します。
		 *
		 * @param n 足される値
		 * @param m 足す値
		 * @return n+mをmodで割った余り
		 */
		public int add(int n, int m) {
			return mod(n + m);
		}

		/**
		 * n-mをmodで割った余りを返します。
		 *
		 * @param n 引かれる値
		 * @param m 引く値
		 * @return n-mをmodで割った余り
		 */
		public int subtract(int n, int m) {
			return mod(n - m);
		}

		/**
		 * n*mをmodで割った余りを返します。
		 *
		 * @param n 掛けられる値
		 * @param m 掛ける値
		 * @return n*mをmodで割った余り
		 */
		public int multiply(int n, int m) {
			int ans = (int)((long)n * m % mod);
			return ans < 0 ? ans + mod : ans;
		}

		/**
		 * n/mをmodで割った余りを返します。
		 *
		 * @param n 割られる値
		 * @param m 割る値
		 * @return n/mをmodで割った余り
		 */
		public int divide(int n, int m) {
			return multiply(n, inverse(m));
		}

		/**
		 * fを通ることが分かっているfの要素数-1次の関数について、xの位置における値をmodで割った余りを返します。<br>
		 * 計算量はO(f)です。
		 *
		 * @param f 関数の形
		 * @param x 求める位置
		 * @return 求めたい値をmodで割った余り
		 */
		public ModInteger lagrangePolynomial(ModInteger[] f, int x) {
			if (f.length > x) return f[x];
			if (x > fact.length) precalc(x);
			ModInteger ret = create(0);
			ModInteger[] dp = new ModInteger[f.length], dp2 = new ModInteger[f.length];
			dp[0] = create(1);
			dp2[f.length - 1] = create(1);
			for (int i = 1; i < f.length; ++i) {
				dp[i] = dp[i - 1].multiply(x - i - 1);
				dp2[f.length - i - 1] = dp2[f.length - i].multiply(x - f.length + i);
			}
			for (int i = 0; i < f.length; ++i) {
				ModInteger tmp = f[i].multiply(dp[i]).multiplyEqual(dp2[i]).multiplyEqual(inv[i]).multiplyEqual(
						inv[f.length - 1 - i]);
				if ((f.length - i & 1) == 0) ret.addEqual(tmp);
				else ret.subtractEqual(tmp);
			}
			return ret;
		}
	}

	/**
	 * 区間における素数を保持する関数です。
	 *
	 * @author 31536000
	 *
	 */
	public static class SegmentPrime {

		private final Prime[] divisor;
		private final int offset;

		private SegmentPrime(Prime[] divisor, int offset) {
			this.divisor = divisor;
			this.offset = offset;
		}

		/**
		 * このクラスが持つ区間の範囲を返します。
		 *
		 * @return 素数を保持している区間
		 */
		public IntRange getRange() {
			return IntRange.closedOpen(offset, offset + divisor.length);
		}

		/**
		 * 素数かどうかを判定します。
		 *
		 * @param n 素数かどうか判定したい数
		 * @return 素数ならばtrue
		 */
		public boolean isPrime(int n) {
			return n <= 1 ? false : divisor[n - offset].prime == n;
		}

		/**
		 * 与えられた数を素因数分解します。<br>
		 * 計算量はO(log n)です。
		 *
		 * @param n 素因数分解したい数
		 * @return 素因数分解した結果
		 */
		public PrimeFactor getPrimeFactor(int n) {
			if (n < 1) throw new IllegalArgumentException("not positive number");
			Map<Prime, Integer> map = new HashMap<>();
			while (n > 1) {
				Prime d = divisor[n - offset];
				map.compute(d, (k, v) -> v == null ? 1 : v + 1);
				n /= d.prime;
			}
			return new PrimeFactor(map);
		}

		@Override
		public String toString() {
			return "SegmentPrime: [" + offset + ", " + (offset + divisor.length) + ")";
		}
	}

	/**
	 * 整数の素因数分解表現を保持します。
	 *
	 * @author 31536000
	 *
	 */
	public static class PrimeFactor extends Number {

		private static final long serialVersionUID = 1363575672283884773L;
		public Map<Prime, Integer> primeFactor;

		private PrimeFactor(Map<Prime, Integer> n) {
			this.primeFactor = n;
		}

		/**
		 * 素因数分解のリスト表現を返します。
		 *
		 * @return 素因数分解のリスト
		 */
		public List<Integer> getFactorizationList() {
			List<Integer> ret = new ArrayList<>();
			for (Entry<Prime, Integer> i : primeFactor.entrySet()) {
				int p = i.getKey().prime, n = i.getValue();
				for (int j = 0; j < n; ++j)
					ret.add(p);
			}
			return ret;
		}

		/**
		 * nとgcdを取った値を保持します。
		 *
		 * @param n gcdを取りたい値
		 */
		public void gcd(PrimeFactor n) {
			for (Entry<Prime, Integer> i : n.primeFactor.entrySet())
				primeFactor.computeIfPresent(i.getKey(), (k, v) -> Math.min(v, i.getValue()));
		}

		/**
		 * gcd(n, m)を返します。
		 *
		 * @param n gcdを取りたい値
		 * @param m gcdを取りたい値
		 * @return gcd(n, m)
		 */
		public static PrimeFactor gcd(PrimeFactor n, PrimeFactor m) {
			Map<Prime, Integer> ret = new HashMap<>(n.primeFactor);
			for (Entry<Prime, Integer> i : m.primeFactor.entrySet())
				ret.computeIfPresent(i.getKey(), (k, v) -> Math.min(v, i.getValue()));
			return new PrimeFactor(ret);
		}

		/**
		 * nとlcmを取った値を保持します。
		 *
		 * @param n lcmを取りたい値
		 */
		public void lcm(PrimeFactor n) {
			for (Entry<Prime, Integer> i : n.primeFactor.entrySet())
				primeFactor.merge(i.getKey(), i.getValue(), (v1, v2) -> Math.max(v1, v2));
		}

		/**
		 * lcm(n, m)を返します。
		 *
		 * @param n lcmを取りたい値
		 * @param m lcmを取りたい値
		 * @return lcm(n, m)
		 */
		public static PrimeFactor lcm(PrimeFactor n, PrimeFactor m) {
			Map<Prime, Integer> ret = new HashMap<>(n.primeFactor);
			for (Entry<Prime, Integer> i : m.primeFactor.entrySet())
				ret.merge(i.getKey(), i.getValue(), (v1, v2) -> Math.max(v1, v2));
			return new PrimeFactor(ret);
		}

		private static int pow(int p, int n) {
			int ans = 1;
			for (int mul = p; n > 0; n >>= 1, mul *= mul)
				if ((n & 1) != 0) ans *= mul;
			return ans;
		}

		private static long pow(long p, long n) {
			long ans = 1;
			for (long mul = p; n > 0; n >>= 1, mul *= mul)
				if ((n & 1) != 0) ans *= mul;
			return ans;
		}

		public BigInteger getValue() {
			BigInteger ret = BigInteger.ONE;
			for (Entry<Prime, Integer> i : primeFactor.entrySet())
				ret = ret.multiply(new BigInteger(i.getKey().toString()).pow(i.getValue()));
			return ret;
		}

		@Override
		public int intValue() {
			int ret = 1;
			for (Entry<Prime, Integer> i : primeFactor.entrySet())
				ret *= pow(i.getKey().prime, i.getValue());
			return ret;
		}

		@Override
		public long longValue() {
			long ret = 1;
			for (Entry<Prime, Integer> i : primeFactor.entrySet())
				ret *= pow((long)i.getKey().prime, i.getValue());
			return ret;
		}

		@Override
		public float floatValue() {
			float ret = 1;
			for (Entry<Prime, Integer> i : primeFactor.entrySet())
				ret *= Math.pow(i.getKey().prime, i.getValue());
			return ret;
		}

		@Override
		public double doubleValue() {
			long ret = 1;
			for (Entry<Prime, Integer> i : primeFactor.entrySet())
				ret *= Math.pow(i.getKey().prime, i.getValue());
			return ret;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof PrimeFactor ? ((PrimeFactor)o).primeFactor.equals(primeFactor) : false;
		}

		@Override
		public int hashCode() {
			return primeFactor.hashCode();
		}

		@Override
		public String toString() {
			return primeFactor.toString();
		}
	}

	/**
	 * 素数を渡すためのクラスです。<br>
	 * 中身が確実に素数であることを保証するときに使ってください。
	 *
	 * @author 31536000
	 *
	 */
	public static class Prime extends Number {

		private static final long serialVersionUID = 8216169308184181643L;
		public final int prime;

		/**
		 * 素数を設定します。
		 *
		 * @param prime 素数
		 * @throws IllegalArgumentException 素数以外を渡した時
		 */
		public Prime(int prime) {
			if (!isPrime(prime)) throw new IllegalArgumentException(prime + " is not prime");
			this.prime = prime;
		}

		private Prime(int prime, boolean none) {
			this.prime = prime;
		}

		private static final int bases[] = {15591, 2018, 166, 7429, 8064, 16045, 10503, 4399, 1949, 1295, 2776, 3620, 560, 3128, 5212, 2657, 2300, 2021, 4652, 1471, 9336, 4018, 2398, 20462, 10277, 8028, 2213, 6219, 620, 3763, 4852, 5012, 3185, 1333, 6227, 5298, 1074, 2391, 5113, 7061, 803, 1269, 3875, 422, 751, 580, 4729, 10239, 746, 2951, 556, 2206, 3778, 481, 1522, 3476, 481, 2487, 3266, 5633, 488, 3373, 6441, 3344, 17, 15105, 1490, 4154, 2036, 1882, 1813, 467, 3307, 14042, 6371, 658, 1005, 903, 737, 1887, 7447, 1888, 2848, 1784, 7559, 3400, 951, 13969, 4304, 177, 41, 19875, 3110, 13221, 8726, 571, 7043, 6943, 1199, 352, 6435, 165, 1169, 3315, 978, 233, 3003, 2562, 2994, 10587, 10030, 2377, 1902, 5354, 4447, 1555, 263, 27027, 2283, 305, 669, 1912, 601, 6186, 429, 1930, 14873, 1784, 1661, 524, 3577, 236, 2360, 6146, 2850, 55637, 1753, 4178, 8466, 222, 2579, 2743, 2031, 2226, 2276, 374, 2132, 813, 23788, 1610, 4422, 5159, 1725, 3597, 3366, 14336, 579, 165, 1375, 10018, 12616, 9816, 1371, 536, 1867, 10864, 857, 2206, 5788, 434, 8085, 17618, 727, 3639, 1595, 4944, 2129, 2029, 8195, 8344, 6232, 9183, 8126, 1870, 3296, 7455, 8947, 25017, 541, 19115, 368, 566, 5674, 411, 522, 1027, 8215, 2050, 6544, 10049, 614, 774, 2333, 3007, 35201, 4706, 1152, 1785, 1028, 1540, 3743, 493, 4474, 2521, 26845, 8354, 864, 18915, 5465, 2447, 42, 4511, 1660, 166, 1249, 6259, 2553, 304, 272, 7286, 73, 6554, 899, 2816, 5197, 13330, 7054, 2818, 3199, 811, 922, 350, 7514, 4452, 3449, 2663, 4708, 418, 1621, 1171, 3471, 88, 11345, 412, 1559, 194};
		private static final byte wheel[] = {10, 2, 4, 2, 4, 6, 2, 6, 4, 2, 4, 6, 6, 2, 6, 4, 2, 6, 4, 6, 8, 4, 2, 4, 2, 4, 8, 6, 4, 6, 2, 4, 6, 2, 6, 6, 4, 2, 4, 6, 2, 6, 4, 2, 4, 2, 10, 2};

		private static boolean isSPRP(int n, long a) {
			int d = n - 1, s = 0;
			while ((d & 1) == 0) {
				++s;
				d >>= 1;
			}
			long cur = 1, pw = d;
			do {
				if ((pw & 1) != 0) cur = (cur * a) % n;
				a = a * a % n;
				pw >>= 1;
			} while (pw != 0);
			if (cur == 1) return true;
			for (int r = 0; r < s; ++r) {
				if (cur == n - 1) return true;
				cur = (cur * cur) % n;
			}
			return false;
		}

		/**
		 * 与えられた値が素数か否かを判定します。<br>
		 * この実装はhttp://ceur-ws.org/Vol-1326/020-Forisek.pdfに基づきます。
		 *
		 * @param x 判定したい値
		 * @return xが素数ならtrue
		 */
		public static boolean isPrime(int x) {
			if (x == 2 || x == 3 || x == 5 || x == 7) return true;
			if ((x & 1) == 0 || x % 3 == 0 || x % 5 == 0 || x % 7 == 0) return false;
			return checkPrime(x);
		}

		private static boolean checkPrime(int x) {
			if (x < 121) return x > 1;
			long h = x;
			h = ((h >> 16) ^ h) * 0x45d9f3b;
			h = ((h >> 16) ^ h) * 0x45d9f3b;
			h = ((h >> 16) ^ h) & 0xFF;
			return isSPRP(x, bases[(int)h]);
		}

		/**
		 * 区間における素数を列挙します。<br>
		 * この実装はエラトステネスの篩に基づきます。
		 *
		 * @param n 素数を求める範囲
		 * @return 1以上n以下の素数を保持する区間素数
		 */
		public static SegmentPrime getSegmentPrime(int n) {
			Prime[] divisor = new Prime[n - 1];
			int sqrt = (int)Math.sqrt(n) + 1;
			for (int i = 0; i < sqrt; ++i) {
				if (divisor[i] != null) continue;
				int p = i + 2;
				divisor[i] = new Prime(p, true);
				for (int j = p * p - 2; j < divisor.length; j += p)
					divisor[j] = divisor[i];
			}
			for (int i = sqrt; i < divisor.length; ++i)
				if (divisor[i] == null) divisor[i] = new Prime(i + 2, true);
			return new SegmentPrime(divisor, 2);
		}

		/**
		 * 与えられた値を素因数分解した結果を返します。
		 *
		 * @param x 素因数分解する値
		 * @return 素因数分解した結果
		 */
		public static PrimeFactor getPrimeFactor(int x) {
			if (x <= 0) throw new IllegalArgumentException("non positive number: " + x);
			Map<Prime, Integer> ret = new TreeMap<>((l, r) -> Integer.compare(l.prime, r.prime));
			int c;
			if ((x & 1) == 0) {
				c = 1;
				for (x >>= 1; (x & 1) == 0; x >>= 1)
					++c;
				ret.put(new Prime(2, false), c);
			}
			if (x % 3 == 0) {
				c = 1;
				for (x /= 3; x % 3 == 0; x /= 3)
					++c;
				ret.put(new Prime(3, false), c);
			}
			if (x % 5 == 0) {
				c = 1;
				for (x /= 5; x % 5 == 0; x /= 5)
					++c;
				ret.put(new Prime(5, false), c);
			}
			if (x % 7 == 0) {
				c = 1;
				for (x /= 7; x % 7 == 0; x /= 7)
					++c;
				ret.put(new Prime(7, false), c);
			}
			if (x < 100000000) { // Wheel Factorization
				for (int i = 11, j = 0; i * i <= x; i += wheel[ ++j % wheel.length]) {
					while (x % i == 0) {
						x /= i;
						ret.compute(new Prime(i, false), (k, v) -> v == null ? 1 : v + 1);
					}
				}
				if (x != 1) ret.put(new Prime(x, false), 1);
			} else {
				int p, count;
				while (x != 1) { // 素因数分解が終わってる
					for (p = x; !checkPrime(p); p = pollardRho(p, 1));
					Prime prime = new Prime(p, false);
					count = 1;
					for (x /= p; x % p == 0; x /= p)
						++count;
					ret.put(prime, count);
				}
			}
			return new PrimeFactor(ret);
		}

		private static int gcd(int n, int m) {
			while (n != 0)
				if ((m %= n) != 0) n %= m;
				else return n;
			return m;
		}

		private static int pollardRho(int x, int c) {
			int n = 2, m = 2, d = 1, next = 4, i = 1;
			do {
				if ( ++i == next) {
					m = n;
					next <<= 1;
				}
				if ((n = (int)(((long)n * n + c) % x)) == m) return pollardRho(x, ++c); // 失敗したので
			} while ((d = gcd(Math.abs(n - m), x)) == 1);// dは約数の一つ
			return d;
		}

		@Override
		public int intValue() {
			return prime;
		}

		@Override
		public long longValue() {
			return prime;
		}

		@Override
		public float floatValue() {
			return prime;
		}

		@Override
		public double doubleValue() {
			return prime;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof Prime ? ((Prime)o).prime == prime : false;
		}

		@Override
		public int hashCode() {
			return prime;
		}

		@Override
		public String toString() {
			return String.valueOf(prime);
		}
	}

	public static class AbstractArray<T> extends AbstractList<T> implements RandomAccess {

		private final Object[] array;

		public AbstractArray(int size) {
			array = new Object[size];
		}

		public AbstractArray(T[] array) {
			this(array.length);
			System.arraycopy(array, 0, this.array, 0, array.length);
		}

		@Override
		public T set(int index, T element) {
			T ret = get(index);
			array[index] = element;
			return ret;
		}

		@Override
		public T get(int index) {
			@SuppressWarnings("unchecked")
			T ret = (T)array[index];
			return ret;
		}

		public Object[] get() {
			return array;
		}

		public T[] get(T[] array) {
			if (array.length < this.array.length) {
				@SuppressWarnings("unchecked")
				T[] ret = (T[])Arrays.copyOfRange(this.array, 0, this.array.length, array.getClass());
				return ret;
			}
			System.arraycopy(this.array, 0, array, 0, this.array.length);
			return array;
		}

		@Override
		public int size() {
			return array.length;
		}

		public int length() {
			return size();
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(array);
		}

		private class Iter implements Iterator<T> {

			private int index;

			private Iter() {
				index = 0;
			}

			@Override
			public boolean hasNext() {
				return index < array.length;
			}

			@Override
			public T next() {
				return get(index++ );
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public Iterator<T> iterator() {
			return new Iter();
		}
	}

	public static class Array<T> extends AbstractArray<T> implements Serializable {

		private static final long serialVersionUID = 2749604433067098063L;

		public Array(int size) {
			super(size);
		}

		public Array(T[] array) {
			super(array);
		}

		public T front() {
			return get(0);
		}

		public T back() {
			return get(size() - 1);
		}
	}

	/**
	 * 要素とそのindexを管理するクラスです。
	 *
	 * @author 31536000
	 *
	 * @param <E> 保持する要素
	 */
	public static class Enumerate<E> {

		public final E value;
		public final int index;

		/**
		 * 要素とそのindexを渡します。<br>
		 * indexは必ずしも元の配列またはコレクションのindexと一致する必要はありませんが、一致する値を返すことが推奨されます。
		 *
		 * @param value
		 * @param index
		 */
		public Enumerate(E value, int index) {
			this.value = value;
			this.index = index;
		}

		/**
		 * 要素を返します。
		 *
		 * @return 要素
		 */
		public E getValue() {
			return value;
		}

		/**
		 * indexを返します。
		 *
		 * @return index
		 */
		public int getIndex() {
			return index;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Enumerate) return ((Enumerate<?>)o).getValue().equals(value) && ((Enumerate<?>)o).getIndex() == index;
			return false;
		}

		@Override
		public int hashCode() {
			return value.hashCode() ^ index;
		}

		@Override
		public String toString() {
			return "{" + value.toString() + ", " + index + "}";
		}
	}

	/**
	 * 要素とそのindexを効率的に取得する関数を提供します。
	 *
	 * @author 31536000
	 *
	 */
	public static class Enumeration {

		private static class IteratorArray<E> implements Iterator<Enumerate<E>> {

			private final E[] array;
			private final int start;
			private int index;

			public IteratorArray(E[] array, int index) {
				this.array = array;
				this.start = index;
				this.index = 0;
			}

			@Override
			public boolean hasNext() {
				return index < array.length;
			}

			@Override
			public Enumerate<E> next() {
				Enumerate<E> ret = new Enumerate<>(array[index], index++ + start);
				return ret;
			}
		}

		private static class IteratorCollection<E> implements Iterator<Enumerate<E>> {

			private final Iterator<E> iter;
			private int start;

			public IteratorCollection(Iterator<E> iter, int index) {
				this.iter = iter;
				this.start = index;
			}

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Enumerate<E> next() {
				Enumerate<E> ret = new Enumerate<>(iter.next(), start++ );
				return ret;
			}
		}

		/**
		 * 配列の各要素とそのindexを順に返すIteratorを生成します。
		 *
		 * @param       <E> 配列の型
		 * @param array 配列
		 * @return Enumerate&lt;E&gt;のIterator
		 */
		public static <E> Iterator<Enumerate<E>> enumerate(E[] array) {
			return enumerate(array, 0);
		}

		/**
		 * 配列の各要素とそのindexを順に返すIteratorを生成します。
		 *
		 * @param       <E> 配列の型
		 * @param array 配列
		 * @param start 添字の初期値、この値だけindexが足されたものが返る
		 * @return Enumerate&lt;E&gt;のIterator
		 */
		public static <E> Iterator<Enumerate<E>> enumerate(E[] array, int start) {
			if (array == null) throw new NullPointerException("array is null");
			return new IteratorArray<E>(array, start);
		}

		/**
		 * Iteratorの各要素とそのindexを順に返すIteratorを生成します。
		 *
		 * @param      <E> Iteratorの型
		 * @param iter Iterator
		 * @return Enumerate&lt;E&gt;のIterator
		 */
		public static <E> Iterator<Enumerate<E>> enumerate(Iterator<E> iter) {
			return enumerate(iter, 0);
		}

		/**
		 * Iteratorの各要素とそのindexを順に返すIteratorを生成します。
		 *
		 * @param       <E> Iteratorの型
		 * @param iter  Iterator
		 * @param start 添字の初期値、この値だけindexが足されたものが返る
		 * @return Enumerate&lt;E&gt;のIterator
		 */
		public static <E> Iterator<Enumerate<E>> enumerate(Iterator<E> iter, int start) {
			if (iter == null) throw new NullPointerException("iterator is null");
			return new IteratorCollection<E>(iter, start);
		}
	}
}

class ACL {

	public static final class Input extends java.io.InputStream {

		private final java.io.InputStream in;
		private final byte[] buffer = new byte[1024];
		private int read = 0;
		private int length = 0;

		public Input(java.io.InputStream in) {
			this.in = in;
		}

		@Override
		public int available() {
			try {
				return in.available();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public void close() {
			try {
				in.close();
				read = length = 0;
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int read() {
			if (hasNextByte()) return nextByte();
			return 0;
		}

		private boolean hasNextByte() {
			if (read < length) return true;
			read = 0;
			try {
				length = in.read(buffer);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return length > 0;
		}

		private static boolean isPrintableChar(byte c) {
			return 32 < c || c < 0;
		}

		private static boolean isNumber(int c) {
			return '0' <= c && c <= '9';
		}

		private boolean readNewLine() {
			if (hasNextByte()) {
				if (buffer[read] == '\r') {
					++read;
					if (hasNextByte() && buffer[read] == '\n') ++read;
					return true;
				}
				if (buffer[read] == '\n') {
					++read;
					return true;
				}
			}
			return false;
		}

		public boolean hasNext() {
			while (hasNextByte() && !isPrintableChar(buffer[read]))
				read++ ;
			return hasNextByte();
		}

		public boolean nextBoolean() {
			return Boolean.valueOf(next());
		}

		public byte nextByte() {
			if (hasNextByte()) return buffer[read++ ];
			throw new java.util.NoSuchElementException();
		}

		public short nextShort() {
			if (!hasNext()) throw new java.util.NoSuchElementException();
			short n = 0;
			try {
				byte b = nextByte();
				if (b == '-') {
					while (isNumber(b = nextByte()))
						n = (short)(n * 10 + '0' - b);
					return n;
				} else if (!isNumber(b)) throw new NumberFormatException();
				do
					n = (short)(n * 10 + b - '0');
				while (isNumber(b = nextByte()));
				return n;
			} catch (java.util.NoSuchElementException e) {
				return n;
			}
		}

		public int nextInt() {
			if (!hasNext()) throw new java.util.NoSuchElementException();
			int n = 0;
			try {
				byte b = nextByte();
				if (b == '-') {
					while (isNumber(b = nextByte()))
						n = n * 10 + '0' - b;
					return n;
				} else if (!isNumber(b)) throw new NumberFormatException();
				do
					n = n * 10 + b - '0';
				while (isNumber(b = nextByte()));
				return n;
			} catch (java.util.NoSuchElementException e) {
				return n;
			}
		}

		public long nextLong() {
			if (!hasNext()) throw new java.util.NoSuchElementException();
			long n = 0;
			try {
				byte b = nextByte();
				if (b == '-') {
					while (isNumber(b = nextByte()))
						n = n * 10 + '0' - b;
					return n;
				} else if (!isNumber(b)) throw new NumberFormatException();
				do
					n = n * 10 + b - '0';
				while (isNumber(b = nextByte()));
				return n;
			} catch (java.util.NoSuchElementException e) {
				return n;
			}
		}

		public float nextFloat() {
			return Float.parseFloat(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public char nextChar() {
			byte b = nextByte();
			if ((b & 0x80) == 0) return (char)b;
			if ((b & 0x20) == 0) return (char)((b & 0x1F) << 6 | (nextByte() & 0x3F));
			return (char)((b & 0xF) << 12 | (nextByte() & 0x3F) << 6 | (nextByte() & 0x3F));
		}

		public String next() {
			if (!hasNext()) throw new java.util.NoSuchElementException();
			StringBuilder sb = new StringBuilder();
			do
				sb.append(nextChar());
			while (hasNextByte() && isPrintableChar(buffer[read]));
			return sb.toString();
		}

		public String nextLine() {
			StringBuilder sb = new StringBuilder();
			while (!readNewLine())
				sb.append(nextChar());
			return sb.toString();
		}

	}

	public static final class Output extends java.io.OutputStream {

		private final java.io.OutputStream out;
		private final byte[] buffer = new byte[1 << 16];
		private int read = 0;
		private boolean autoFlush = true;
		private static final int READ_STRING_TO_BYTE_ARRAY;

		public Output(java.io.OutputStream out) {
			this.out = out;
		}

		public void setAutoFlush(boolean autoFlush) {
			this.autoFlush = autoFlush;
		}

		static {
			int flag = -1;
			try {
				String test = "test";
				java.lang.reflect.Field strValueField = test.getClass().getDeclaredField("value");
				strValueField.setAccessible(true);
				Object value = strValueField.get(test);
				strValueField.setAccessible(false);
				if (value instanceof byte[]) flag = 0;
				else flag = 1;
			} catch (Exception e) {}
			READ_STRING_TO_BYTE_ARRAY = flag;
		}

		@Override
		public void close() {
			try {
				flush();
				out.close();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void flush() {
			try {
				write();
				out.flush();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void write(byte[] b) {
			try {
				out.write(b);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void write(byte[] b, int off, int len) {
			try {
				out.write(b, off, len);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void write(int b) {
			try {
				out.write(b);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}

		private void write() {
			write(buffer, 0, read);
			read = 0;
		}

		public void print(boolean b) {
			if (b) {
				print((byte)'t');
				print((byte)'r');
				print((byte)'u');
				print((byte)'e');
			} else {
				print((byte)'f');
				print((byte)'a');
				print((byte)'l');
				print((byte)'s');
				print((byte)'e');
			}
		}

		public void print(byte b) {
			if (read == buffer.length) write();
			buffer[read++ ] = b;
		}

		public void print(short s) {
			if (s < 0) {
				print((byte)'-');
				if (s == -32768) {
					print((byte)'3');
					print((byte)'2');
					print((byte)'7');
					print((byte)'6');
					print((byte)'8');
					return;
				}
				s = (short)-s;
			}
			if (s >= 100) {
				if (s >= 1000) {
					if (s >= 10000) {
						print((byte)(s / 10000 % 10 + '0'));
					}
					print((byte)(s / 1000 % 10 + '0'));
				}
				print((byte)(s / 100 % 10 + '0'));
				print((byte)(s / 10 % 10 + '0'));
			} else {
				if (s >= 10) print((byte)(s / 10 % 10 + '0'));
			}
			print((byte)(s % 10 + '0'));
		}

		public void print(int i) {
			if (i < 0) {
				print((byte)'-');
				if (i == -2147483648) {
					print((byte)'2');
					print((byte)'1');
					print((byte)'4');
					print((byte)'7');
					print((byte)'4');
					print((byte)'8');
					print((byte)'3');
					print((byte)'6');
					print((byte)'4');
					print((byte)'8');
					return;
				}
				i = -i;
			}
			if (i >= 1000000000) print((byte)(i / 1000000000 % 10 + '0'));
			if (i >= 100000000) print((byte)(i / 100000000 % 10 + '0'));
			if (i >= 10000000) print((byte)(i / 10000000 % 10 + '0'));
			if (i >= 1000000) print((byte)(i / 1000000 % 10 + '0'));
			if (i >= 100000) print((byte)(i / 100000 % 10 + '0'));
			if (i >= 10000) print((byte)(i / 10000 % 10 + '0'));
			if (i >= 1000) print((byte)(i / 1000 % 10 + '0'));
			if (i >= 100) print((byte)(i / 100 % 10 + '0'));
			if (i >= 10) print((byte)(i / 10 % 10 + '0'));
			print((byte)(i % 10 + '0'));
		}

		public void print(long l) {
			if (l < 0) {
				print((byte)'-');
				if (l == -9223372036854775808L) {
					print((byte)'9');
					print((byte)'2');
					print((byte)'2');
					print((byte)'3');
					print((byte)'3');
					print((byte)'7');
					print((byte)'2');
					print((byte)'0');
					print((byte)'3');
					print((byte)'6');
					print((byte)'8');
					print((byte)'5');
					print((byte)'4');
					print((byte)'7');
					print((byte)'7');
					print((byte)'5');
					print((byte)'8');
					print((byte)'0');
					print((byte)'8');
					return;
				}
				l = -l;
			}
			if (l >= 1000000000000000000L) print((byte)(l / 1000000000000000000L % 10 + '0'));
			if (l >= 100000000000000000L) print((byte)(l / 100000000000000000L % 10 + '0'));
			if (l >= 10000000000000000L) print((byte)(l / 10000000000000000L % 10 + '0'));
			if (l >= 1000000000000000L) print((byte)(l / 1000000000000000L % 10 + '0'));
			if (l >= 100000000000000L) print((byte)(l / 100000000000000L % 10 + '0'));
			if (l >= 10000000000000L) print((byte)(l / 10000000000000L % 10 + '0'));
			if (l >= 1000000000000L) print((byte)(l / 1000000000000L % 10 + '0'));
			if (l >= 100000000000L) print((byte)(l / 100000000000L % 10 + '0'));
			if (l >= 10000000000L) print((byte)(l / 10000000000L % 10 + '0'));
			if (l >= 1000000000L) print((byte)(l / 1000000000L % 10 + '0'));
			if (l >= 100000000L) print((byte)(l / 100000000L % 10 + '0'));
			if (l >= 10000000L) print((byte)(l / 10000000L % 10 + '0'));
			if (l >= 1000000L) print((byte)(l / 1000000L % 10 + '0'));
			if (l >= 100000L) print((byte)(l / 100000L % 10 + '0'));
			if (l >= 10000L) print((byte)(l / 10000L % 10 + '0'));
			if (l >= 1000L) print((byte)(l / 1000L % 10 + '0'));
			if (l >= 100L) print((byte)(l / 100L % 10 + '0'));
			if (l >= 10L) print((byte)(l / 10L % 10 + '0'));
			print((byte)(l % 10 + '0'));
		}

		public void print(float f) {
			print(Float.toString(f));
		}

		public void print(double d) {
			print(Double.toString(d));
		}

		public void print(double d, int n) {
			if (d < 0) {
				print((byte)'-');
				d = -d;
			}
			d += Math.pow(10, -n) / 2;
			long l = (long)d;
			print(l);
			print((byte)'.');
			d -= l;
			for (int i = 0; i < n; i++ ) {
				d *= 10;
				int in = (int)d;
				print((byte)(in + '0'));
				d -= in;
			}
		}

		public void print(char c) {
			if (c < 0x80) {
				print((byte)c);
			} else if (c < 0x07FF) {
				print((byte)(c >> 6 & 0x3F | 0x80));
				print((byte)(c & 0x3F | 0x80));
			} else {
				print((byte)(c >> 12 & 0xF | 0xE0));
				print((byte)(c >> 6 & 0x3F | 0x80));
				print((byte)(c & 0x3F | 0x80));
			}
		}

		public void print(char[] s) {
			for (char i : s)
				print(i);
		}

		public void print(String s) {
			try {
				if (READ_STRING_TO_BYTE_ARRAY == 0) {
					java.lang.reflect.Field strValueField = s.getClass().getDeclaredField("value");
					strValueField.setAccessible(true);
					byte[] c = (byte[])strValueField.get(s);
					strValueField.setAccessible(false);
					strValueField = s.getClass().getDeclaredField("coder");
					strValueField.setAccessible(true);
					byte coder = (byte)strValueField.get(s);
					strValueField.setAccessible(false);
					if (coder == 0) {
						for (byte b : c)
							print(b);
					} else if (coder == 1) {
						for (int i = 0; i < c.length; i += 2)
							print((char)(c[i] | c[i + 1] << 8));
					} else {
						print(s.toCharArray());
					}
				} else if (READ_STRING_TO_BYTE_ARRAY == 1) {
					java.lang.reflect.Field strValueField = s.getClass().getDeclaredField("value");
					strValueField.setAccessible(true);
					print((char[])strValueField.get(s));
					strValueField.setAccessible(false);
				} else {
					print(s.toCharArray());
				}
			} catch (Exception e) {
				print(s.toCharArray());
			}
		}

		public void print(Object o) {
			print(o.toString());
		}

		public void printf(java.util.Locale l, String format, Object... args) {
			print(String.format(l, format, args));
		}

		public void printf(String format, Object... args) {
			print(String.format(format, args));
		}

		public void println() {
			print((byte)'\n');
			if (autoFlush) flush();
		}

		public void println(boolean b) {
			print(b);
			println();
		}

		public void println(byte b) {
			print(b);
			println();
		}

		public void println(short s) {
			print(s);
			println();
		}

		public void println(int i) {
			print(i);
			println();
		}

		public void println(long l) {
			print(l);
			println();
		}

		public void println(float f) {
			print(f);
			println();
		}

		public void println(double d) {
			print(d);
			println();
		}

		public void println(double d, int n) {
			print(d, n);
			println();
		}

		public void println(char c) {
			print(c);
			println();
		}

		public void println(char[] s) {
			print(s);
			println();
		}

		public void println(String s) {
			print(s);
			println();
		}

		public void println(Object o) {
			print(o);
			println();
		}

		public Output append(char c) {
			print(c);
			return this;
		}

		public Output append(CharSequence csq) {
			if (csq == null) csq = "null";
			print(csq.toString());
			return this;
		}

		public Output append(CharSequence csq, int start, int end) {
			if (csq == null) csq = "null";
			print(csq.subSequence(start, end).toString());
			return this;
		}
	}

	public static final class DisjointSetUnion {

		private final int[] parent;

		private DisjointSetUnion(int n) {
			parent = new int[n];
			java.util.Arrays.fill(parent, -1);
		}

		public static DisjointSetUnion create(int n) {
			return new DisjointSetUnion(n);
		}

		public int getLeader(int a) {
			int p1, p2;
			while ((p1 = parent[a]) >= 0) {
				if ((p2 = parent[p1]) >= 0) a = parent[a] = p2;
				else return p1;
			}
			return a;
		}

		public int merge(int a, int b) {
			a = getLeader(a);
			b = getLeader(b);
			if (a == b) return a;
			if (parent[a] < parent[b]) {
				parent[b] += parent[a];
				parent[a] = b;
				return b;
			}
			parent[a] += parent[b];
			parent[b] = a;
			return a;
		}

		public boolean isSame(int a, int b) {
			return getLeader(a) == getLeader(b);
		}

		public int getSize(int a) {
			return -parent[getLeader(a)];
		}

		public java.util.ArrayList<java.util.ArrayList<Integer>> getGroups() {
			Object[] group = new Object[parent.length];
			java.util.ArrayList<java.util.ArrayList<Integer>> ret = new java.util.ArrayList<>();
			for (int i = 0; i < parent.length; ++i) {
				int leader = getLeader(i);
				Object put = group[leader];
				if (put == null) {
					java.util.ArrayList<Integer> list = new java.util.ArrayList<>();
					list.add(i);
					ret.add(list);
					group[leader] = list;
				} else {
					@SuppressWarnings("unchecked")
					java.util.ArrayList<Integer> list = (java.util.ArrayList<Integer>)put;
					list.add(i);
				}
			}
			return ret;
		}

		@Override
		public String toString() {
			return getGroups().toString();
		}
	}

	public static final class IntFenwickTree {

		private final int[] array;

		private IntFenwickTree(int n) {
			array = new int[n + 1];
		}

		private IntFenwickTree(int[] array) {
			this(array.length);
			System.arraycopy(array, 0, this.array, 1, array.length);
			for (int i = 1; i < this.array.length; ++i)
				if (i + (i & -i) < this.array.length) this.array[i + (i & -i)] += this.array[i];
		}

		public static IntFenwickTree create(int n) {
			return new IntFenwickTree(n);
		}

		public static IntFenwickTree create(int[] array) {
			return new IntFenwickTree(array);
		}

		public void add(int index, int add) {
			++index;
			while (index < array.length) {
				array[index] += add;
				index += index & -index;
			}
		}

		private int sum(int index) {
			int sum = 0;
			while (index > 0) {
				sum += array[index];
				index -= index & -index;
			}
			return sum;
		}

		public int sum(int l, int r) {
			return sum(r) - sum(l);
		}

		@Override
		public String toString() {
			return java.util.stream.IntStream.range(0, array.length - 1).mapToObj(i -> String.valueOf(sum(i + 1) - sum(i))).collect(
					java.util.stream.Collectors.joining(", ", "[", "]"));
		}
	}

	public static final class LongFenwickTree {

		private final long[] array;

		private LongFenwickTree(int n) {
			array = new long[n + 1];
		}

		private LongFenwickTree(long[] array) {
			this(array.length);
			System.arraycopy(array, 0, this.array, 1, array.length);
			for (int i = 1; i < this.array.length; ++i)
				if (i + (i & -i) < this.array.length) this.array[i + (i & -i)] += this.array[i];
		}

		public static LongFenwickTree create(int n) {
			return new LongFenwickTree(n);
		}

		public static LongFenwickTree create(long[] array) {
			return new LongFenwickTree(array);
		}

		public void add(int index, long add) {
			++index;
			while (index < array.length) {
				array[index] += add;
				index += index & -index;
			}
		}

		private long sum(int index) {
			long sum = 0;
			while (index > 0) {
				sum += array[index];
				index -= index & -index;
			}
			return sum;
		}

		public long sum(int l, int r) {
			return sum(r) - sum(l);
		}

		@Override
		public String toString() {
			return java.util.stream.IntStream.range(0, array.length - 1).mapToObj(i -> String.valueOf(sum(i + 1) - sum(i))).collect(
					java.util.stream.Collectors.joining(", ", "[", "]"));
		}
	}

	public static final class MathLib {

		private static long safe_mod(long x, long m) {
			x %= m;
			if (x < 0) x += m;
			return x;
		}

		private static long[] inv_gcd(long a, long b) {
			a = safe_mod(a, b);
			if (a == 0) return new long[]{b, 0};

			long s = b, t = a;
			long m0 = 0, m1 = 1;
			while (t > 0) {
				long u = s / t;
				s -= t * u;
				m0 -= m1 * u;
				long tmp = s;
				s = t;
				t = tmp;
				tmp = m0;
				m0 = m1;
				m1 = tmp;
			}
			if (m0 < 0) m0 += b / s;
			return new long[]{s, m0};
		}

		public static long pow_mod(long x, long n, long m) {
			assert (n >= 0 && m >= 1);
			long ans = 1;
			while (n > 0) {
				if (n % 2 == 1) ans = (ans * x) % m;
				x = (x * x) % m;
				n /= 2;
			}
			return ans;
		}

		public static long[] crt(long[] r, long[] m) {
			assert (r.length == m.length);
			int n = r.length;

			long r0 = 0, m0 = 1;
			for (int i = 0; i < n; i++ ) {
				assert (1 <= m[i]);
				long r1 = safe_mod(r[i], m[i]), m1 = m[i];
				if (m0 < m1) {
					long tmp = r0;
					r0 = r1;
					r1 = tmp;
					tmp = m0;
					m0 = m1;
					m1 = tmp;
				}
				if (m0 % m1 == 0) {
					if (r0 % m1 != r1) return new long[]{0, 0};
					continue;
				}

				long[] ig = inv_gcd(m0, m1);
				long g = ig[0], im = ig[1];

				long u1 = m1 / g;
				if ((r1 - r0) % g != 0) return new long[]{0, 0};

				long x = (r1 - r0) / g % u1 * im % u1;

				r0 += x * m0;
				m0 *= u1;
				if (r0 < 0) r0 += m0;
				//System.err.printf("%d %d\n", r0, m0);
			}
			return new long[]{r0, m0};
		}

		public static long floor_sum(long n, long m, long a, long b) {
			long ans = 0;
			if (a >= m) {
				ans += (n - 1) * n * (a / m) / 2;
				a %= m;
			}
			if (b >= m) {
				ans += n * (b / m);
				b %= m;
			}

			long y_max = (a * n + b) / m;
			long x_max = y_max * m - b;
			if (y_max == 0) return ans;
			ans += (n - (x_max + a - 1) / a) * y_max;
			ans += floor_sum(y_max, a, m, (a - x_max % a) % a);
			return ans;
		}
	}

	/**
	 * @verified https://atcoder.jp/contests/practice2/tasks/practice2_d
	 */
	public static final class MaxFlow {

		public class CapEdge {

			private final int from, to;
			private long cap;
			private final int rev;

			CapEdge(int from, int to, long cap, int rev) {
				this.from = from;
				this.to = to;
				this.cap = cap;
				this.rev = rev;
			}

			public int getFrom() {
				return from;
			}

			public int getTo() {
				return to;
			}

			public long getCap() {
				return cap;
			}

			public long getFlow() {
				return g[to][rev].cap;
			}
		}

		private static final long INF = Long.MAX_VALUE;

		private final int n;
		private int m;
		private final java.util.ArrayList<CapEdge> edges;
		private final int[] count;
		private final CapEdge[][] g;

		public MaxFlow(int n) {
			this.n = n;
			this.edges = new java.util.ArrayList<>();
			this.count = new int[n];
			this.g = new CapEdge[n][];
		}

		public int addEdge(int from, int to, long cap) {
			rangeCheck(from, 0, n);
			rangeCheck(to, 0, n);
			nonNegativeCheck(cap, "Capacity");
			CapEdge e = new CapEdge(from, to, cap, count[to]);
			count[from]++ ;
			count[to]++ ;
			edges.add(e);
			return m++ ;
		}

		public CapEdge getEdge(int i) {
			rangeCheck(i, 0, m);
			return edges.get(i);
		}

		public java.util.ArrayList<CapEdge> getEdges() {
			return edges;
		}

		public void changeEdge(int i, long newCap, long newFlow) {
			rangeCheck(i, 0, m);
			nonNegativeCheck(newCap, "Capacity");
			if (newFlow > newCap) {
				throw new IllegalArgumentException(String.format("Flow %d is greater than capacity %d.", newCap, newFlow));
			}
			CapEdge e = edges.get(i);
			CapEdge er = g[e.to][e.rev];
			e.cap = newCap - newFlow;
			er.cap = newFlow;
		}

		private void buildGraph() {
			for (int i = 0; i < n; i++ ) {
				g[i] = new CapEdge[count[i]];
			}
			int[] idx = new int[n];
			for (CapEdge e : edges) {
				g[e.to][idx[e.to]++ ] = new CapEdge(e.to, e.from, 0, idx[e.from]);
				g[e.from][idx[e.from]++ ] = e;
			}
		}

		public long maxFlow(int s, int t) {
			return flow(s, t, INF);
		}

		public long flow(int s, int t, long flowLimit) {
			rangeCheck(s, 0, n);
			rangeCheck(t, 0, n);
			buildGraph();
			long flow = 0;
			int[] level = new int[n];
			int[] que = new int[n];
			int[] iter = new int[n];
			while (true) {
				java.util.Arrays.fill(level, -1);
				dinicBFS(s, t, level, que);
				if (level[t] < 0) return flow;
				java.util.Arrays.fill(iter, 0);
				while (true) {
					long d = dinicDFS(t, s, flowLimit - flow, iter, level);
					if (d <= 0) break;
					flow += d;
				}
			}
		}

		private void dinicBFS(int s, int t, int[] level, int[] que) {
			int hd = 0, tl = 0;
			que[tl++ ] = s;
			level[s] = 0;
			while (tl > hd) {
				int u = que[hd++ ];
				for (CapEdge e : g[u]) {
					int v = e.to;
					if (e.cap <= 0 || level[v] >= 0) continue;
					level[v] = level[u] + 1;
					if (v == t) return;
					que[tl++ ] = v;
				}
			}
		}

		private long dinicDFS(int cur, int s, long f, int[] iter, int[] level) {
			if (cur == s) return f;
			long res = 0;
			while (iter[cur] < count[cur]) {
				CapEdge er = g[cur][iter[cur]++ ];
				int u = er.to;
				CapEdge e = g[u][er.rev];
				if (level[u] >= level[cur] || e.cap <= 0) continue;
				long d = dinicDFS(u, s, Math.min(f - res, e.cap), iter, level);
				if (d <= 0) continue;
				e.cap -= d;
				er.cap += d;
				res += d;
				if (res == f) break;
			}
			return res;
		}

		public long fordFulkersonMaxFlow(int s, int t) {
			return fordFulkersonFlow(s, t, INF);
		}

		public long fordFulkersonFlow(int s, int t, long flowLimit) {
			rangeCheck(s, 0, n);
			rangeCheck(t, 0, n);
			buildGraph();
			boolean[] used = new boolean[n];
			long flow = 0;
			while (true) {
				java.util.Arrays.fill(used, false);
				long f = fordFulkersonDFS(s, t, flowLimit - flow, used);
				if (f <= 0) return flow;
				flow += f;
			}
		}

		private long fordFulkersonDFS(int cur, int t, long f, boolean[] used) {
			if (cur == t) return f;
			used[cur] = true;
			for (CapEdge e : g[cur]) {
				if (used[e.to] || e.cap <= 0) continue;
				long d = fordFulkersonDFS(e.to, t, Math.min(f, e.cap), used);
				if (d <= 0) continue;
				e.cap -= d;
				g[e.to][e.rev].cap += d;
				return d;
			}
			return 0;
		}

		public boolean[] minCut(int s) {
			rangeCheck(s, 0, n);
			boolean[] reachable = new boolean[n];
			int[] stack = new int[n];
			int ptr = 0;
			stack[ptr++ ] = s;
			reachable[s] = true;
			while (ptr > 0) {
				int u = stack[ --ptr];
				for (CapEdge e : g[u]) {
					int v = e.to;
					if (reachable[v] || e.cap <= 0) continue;
					reachable[v] = true;
					stack[ptr++ ] = v;
				}
			}
			return reachable;
		}

		private static void rangeCheck(int i, int minInlusive, int maxExclusive) {
			if (i < 0 || i >= maxExclusive) {
				throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for length %d", i, maxExclusive));
			}
		}

		private static void nonNegativeCheck(long cap, java.lang.String attribute) {
			if (cap < 0) { throw new IllegalArgumentException(String.format("%s %d is negative.", attribute, cap)); }
		}
	}

	/**
	 * @verified
	 * 		- https://atcoder.jp/contests/practice2/tasks/practice2_e
	 *           - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=GRL_6_B
	 */
	public static final class MinCostFlow {

		public class WeightedCapEdge {

			private final int from, to;
			private long cap;
			private long cost;
			private final int rev;

			WeightedCapEdge(int from, int to, long cap, long cost, int rev) {
				this.from = from;
				this.to = to;
				this.cap = cap;
				this.cost = cost;
				this.rev = rev;
			}

			public int getFrom() {
				return from;
			}

			public int getTo() {
				return to;
			}

			public long getCap() {
				return cap;
			}

			public long getCost() {
				return cost;
			}

			public long getFlow() {
				return g[to][rev].cap;
			}
		}

		private static final long INF = Long.MAX_VALUE;

		private final int n;
		private int m;
		private final java.util.ArrayList<WeightedCapEdge> edges;
		private final int[] count;
		private final WeightedCapEdge[][] g;
		private final long[] potential;

		private final long[] dist;
		private final WeightedCapEdge[] prev;

		public MinCostFlow(int n) {
			this.n = n;
			this.edges = new java.util.ArrayList<>();
			this.count = new int[n];
			this.g = new WeightedCapEdge[n][];
			this.potential = new long[n];
			this.dist = new long[n];
			this.prev = new WeightedCapEdge[n];
		}

		public int addEdge(int from, int to, long cap, long cost) {
			rangeCheck(from, 0, n);
			rangeCheck(to, 0, n);
			nonNegativeCheck(cap, "Capacity");
			nonNegativeCheck(cost, "Cost");
			WeightedCapEdge e = new WeightedCapEdge(from, to, cap, cost, count[to]);
			count[from]++ ;
			count[to]++ ;
			edges.add(e);
			return m++ ;
		}

		private void buildGraph() {
			for (int i = 0; i < n; i++ ) {
				g[i] = new WeightedCapEdge[count[i]];
			}
			int[] idx = new int[n];
			for (WeightedCapEdge e : edges) {
				g[e.to][idx[e.to]++ ] = new WeightedCapEdge(e.to, e.from, 0, -e.cost, idx[e.from]);
				g[e.from][idx[e.from]++ ] = e;
			}
		}

		private long addFlow;
		private long addCost;

		public long[] minCostMaxFlow(int s, int t) {
			return minCostFlow(s, t, INF);
		}

		public long[] minCostFlow(int s, int t, long flowLimit) {
			rangeCheck(s, 0, n);
			rangeCheck(t, 0, n);
			if (s == t) { throw new IllegalArgumentException(String.format("s = t = %d", s)); }
			nonNegativeCheck(flowLimit, "Flow");
			buildGraph();
			long flow = 0;
			long cost = 0;
			while (true) {
				dijkstra(s, t, flowLimit - flow);
				if (addFlow == 0) break;
				flow += addFlow;
				cost += addFlow * addCost;
			}
			return new long[]{flow, cost};
		}

		public java.util.ArrayList<long[]> minCostSlope(int s, int t) {
			return minCostSlope(s, t, INF);
		}

		public java.util.ArrayList<long[]> minCostSlope(int s, int t, long flowLimit) {
			rangeCheck(s, 0, n);
			rangeCheck(t, 0, n);
			if (s == t) { throw new IllegalArgumentException(String.format("s = t = %d", s)); }
			nonNegativeCheck(flowLimit, "Flow");
			buildGraph();
			java.util.ArrayList<long[]> slope = new java.util.ArrayList<>();
			long prevCost = -1;
			long flow = 0;
			long cost = 0;
			while (true) {
				slope.add(new long[]{flow, cost});
				dijkstra(s, t, flowLimit - flow);
				if (addFlow == 0) return slope;
				flow += addFlow;
				cost += addFlow * addCost;
				if (addCost == prevCost) {
					slope.remove(slope.size() - 1);
				}
				prevCost = addCost;
			}
		}

		private void dijkstra(int s, int t, long maxFlow) {
			final class State implements Comparable<State> {

				final int v;
				final long d;

				State(int v, long d) {
					this.v = v;
					this.d = d;
				}

				@Override
				public int compareTo(State s) {
					return d == s.d ? v - s.v : d > s.d ? 1 : -1;
				}
			}
			java.util.Arrays.fill(dist, INF);
			dist[s] = 0;
			java.util.PriorityQueue<State> pq = new java.util.PriorityQueue<>();
			pq.add(new State(s, 0l));
			while (pq.size() > 0) {
				State st = pq.poll();
				int u = st.v;
				if (st.d != dist[u]) continue;
				for (WeightedCapEdge e : g[u]) {
					if (e.cap <= 0) continue;
					int v = e.to;
					long nextCost = dist[u] + e.cost + potential[u] - potential[v];
					if (nextCost < dist[v]) {
						dist[v] = nextCost;
						prev[v] = e;
						pq.add(new State(v, dist[v]));
					}
				}
			}
			if (dist[t] == INF) {
				addFlow = 0;
				addCost = INF;
				return;
			}
			for (int i = 0; i < n; i++ ) {
				potential[i] += dist[i];
			}
			addCost = 0;
			addFlow = maxFlow;
			for (int v = t; v != s;) {
				WeightedCapEdge e = prev[v];
				addCost += e.cost;
				addFlow = java.lang.Math.min(addFlow, e.cap);
				v = e.from;
			}
			for (int v = t; v != s;) {
				WeightedCapEdge e = prev[v];
				e.cap -= addFlow;
				g[v][e.rev].cap += addFlow;
				v = e.from;
			}
		}

		public void clearFlow() {
			java.util.Arrays.fill(potential, 0);
			for (WeightedCapEdge e : edges) {
				long flow = e.getFlow();
				e.cap += flow;
				g[e.to][e.rev].cap -= flow;
			}
		}

		public WeightedCapEdge getEdge(int i) {
			rangeCheck(i, 0, m);
			return edges.get(i);
		}

		public java.util.ArrayList<WeightedCapEdge> getEdges() {
			return edges;
		}

		private static void rangeCheck(int i, int minInlusive, int maxExclusive) {
			if (i < 0 || i >= maxExclusive) {
				throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for length %d", i, maxExclusive));
			}
		}

		private static void nonNegativeCheck(long cap, java.lang.String attribute) {
			if (cap < 0) { throw new IllegalArgumentException(String.format("%s %d is negative.", attribute, cap)); }
		}
	}

	/**
	 * @verified
	 *           <ul>
	 *           <li>https://atcoder.jp/contests/arc050/tasks/arc050_c
	 *           <li>https://atcoder.jp/contests/abc129/tasks/abc129_f
	 *           </ul>
	 */
	public static final class ModIntFactory {

		private final ModArithmetic ma;
		private final int mod;

		public ModIntFactory(int mod) {
			this.ma = ModArithmetic.of(mod);
			this.mod = mod;
		}

		public ModInt create(long value) {
			if ((value %= mod) < 0) value += mod;
			if (ma instanceof ModArithmetic.ModArithmeticMontgomery) {
				return new ModInt(((ModArithmetic.ModArithmeticMontgomery)ma).generate(value));
			}
			return new ModInt((int)value);
		}

		class ModInt {

			private int value;

			private ModInt(int value) {
				this.value = value;
			}

			public int mod() {
				return mod;
			}

			public int value() {
				if (ma instanceof ModArithmetic.ModArithmeticMontgomery) {
					return ((ModArithmetic.ModArithmeticMontgomery)ma).reduce(value);
				}
				return value;
			}

			public ModInt add(ModInt mi) {
				return new ModInt(ma.add(value, mi.value));
			}

			public ModInt add(ModInt mi1, ModInt mi2) {
				return new ModInt(ma.add(value, mi1.value)).addAsg(mi2);
			}

			public ModInt add(ModInt mi1, ModInt mi2, ModInt mi3) {
				return new ModInt(ma.add(value, mi1.value)).addAsg(mi2).addAsg(mi3);
			}

			public ModInt add(ModInt mi1, ModInt mi2, ModInt mi3, ModInt mi4) {
				return new ModInt(ma.add(value, mi1.value)).addAsg(mi2).addAsg(mi3).addAsg(mi4);
			}

			public ModInt add(ModInt mi1, ModInt... mis) {
				ModInt mi = add(mi1);
				for (ModInt m : mis)
					mi.addAsg(m);
				return mi;
			}

			public ModInt add(long mi) {
				return new ModInt(ma.add(value, ma.remainder(mi)));
			}

			public ModInt sub(ModInt mi) {
				return new ModInt(ma.sub(value, mi.value));
			}

			public ModInt sub(long mi) {
				return new ModInt(ma.sub(value, ma.remainder(mi)));
			}

			public ModInt mul(ModInt mi) {
				return new ModInt(ma.mul(value, mi.value));
			}

			public ModInt mul(ModInt mi1, ModInt mi2) {
				return new ModInt(ma.mul(value, mi1.value)).mulAsg(mi2);
			}

			public ModInt mul(ModInt mi1, ModInt mi2, ModInt mi3) {
				return new ModInt(ma.mul(value, mi1.value)).mulAsg(mi2).mulAsg(mi3);
			}

			public ModInt mul(ModInt mi1, ModInt mi2, ModInt mi3, ModInt mi4) {
				return new ModInt(ma.mul(value, mi1.value)).mulAsg(mi2).mulAsg(mi3).mulAsg(mi4);
			}

			public ModInt mul(ModInt mi1, ModInt... mis) {
				ModInt mi = mul(mi1);
				for (ModInt m : mis)
					mi.mulAsg(m);
				return mi;
			}

			public ModInt mul(long mi) {
				return new ModInt(ma.mul(value, ma.remainder(mi)));
			}

			public ModInt div(ModInt mi) {
				return new ModInt(ma.div(value, mi.value));
			}

			public ModInt div(long mi) {
				return new ModInt(ma.div(value, ma.remainder(mi)));
			}

			public ModInt inv() {
				return new ModInt(ma.inv(value));
			}

			public ModInt pow(long b) {
				return new ModInt(ma.pow(value, b));
			}

			public ModInt addAsg(ModInt mi) {
				this.value = ma.add(value, mi.value);
				return this;
			}

			public ModInt addAsg(ModInt mi1, ModInt mi2) {
				return addAsg(mi1).addAsg(mi2);
			}

			public ModInt addAsg(ModInt mi1, ModInt mi2, ModInt mi3) {
				return addAsg(mi1).addAsg(mi2).addAsg(mi3);
			}

			public ModInt addAsg(ModInt mi1, ModInt mi2, ModInt mi3, ModInt mi4) {
				return addAsg(mi1).addAsg(mi2).addAsg(mi3).addAsg(mi4);
			}

			public ModInt addAsg(ModInt... mis) {
				for (ModInt m : mis)
					addAsg(m);
				return this;
			}

			public ModInt addAsg(long mi) {
				this.value = ma.add(value, ma.remainder(mi));
				return this;
			}

			public ModInt subAsg(ModInt mi) {
				this.value = ma.sub(value, mi.value);
				return this;
			}

			public ModInt subAsg(long mi) {
				this.value = ma.sub(value, ma.remainder(mi));
				return this;
			}

			public ModInt mulAsg(ModInt mi) {
				this.value = ma.mul(value, mi.value);
				return this;
			}

			public ModInt mulAsg(ModInt mi1, ModInt mi2) {
				return mulAsg(mi1).mulAsg(mi2);
			}

			public ModInt mulAsg(ModInt mi1, ModInt mi2, ModInt mi3) {
				return mulAsg(mi1).mulAsg(mi2).mulAsg(mi3);
			}

			public ModInt mulAsg(ModInt mi1, ModInt mi2, ModInt mi3, ModInt mi4) {
				return mulAsg(mi1).mulAsg(mi2).mulAsg(mi3).mulAsg(mi4);
			}

			public ModInt mulAsg(ModInt... mis) {
				for (ModInt m : mis)
					mulAsg(m);
				return this;
			}

			public ModInt mulAsg(long mi) {
				this.value = ma.mul(value, ma.remainder(mi));
				return this;
			}

			public ModInt divAsg(ModInt mi) {
				this.value = ma.div(value, mi.value);
				return this;
			}

			public ModInt divAsg(long mi) {
				this.value = ma.div(value, ma.remainder(mi));
				return this;
			}

			@Override
			public String toString() {
				return String.valueOf(value());
			}

			@Override
			public boolean equals(Object o) {
				if (o instanceof ModInt) {
					ModInt mi = (ModInt)o;
					return mod() == mi.mod() && value() == mi.value();
				}
				return false;
			}

			@Override
			public int hashCode() {
				return (1 * 37 + mod()) * 37 + value();
			}
		}

		private interface ModArithmetic {

			public int mod();

			public int remainder(long value);

			public int add(int a, int b);

			public int sub(int a, int b);

			public int mul(int a, int b);

			public default int div(int a, int b) {
				return mul(a, inv(b));
			}

			public int inv(int a);

			public int pow(int a, long b);

			public static ModArithmetic of(int mod) {
				if (mod <= 0) {
					throw new IllegalArgumentException();
				} else if (mod == 1) {
					return new ModArithmetic1();
				} else if (mod == 2) {
					return new ModArithmetic2();
				} else if (mod == 998244353) {
					return new ModArithmetic998244353();
				} else if (mod == 1000000007) {
					return new ModArithmetic1000000007();
				} else if ((mod & 1) == 1) {
					return new ModArithmeticMontgomery(mod);
				} else {
					return new ModArithmeticBarrett(mod);
				}
			}

			static final class ModArithmetic1 implements ModArithmetic {

				@Override
				public int mod() {
					return 1;
				}

				@Override
				public int remainder(long value) {
					return 0;
				}

				@Override
				public int add(int a, int b) {
					return 0;
				}

				@Override
				public int sub(int a, int b) {
					return 0;
				}

				@Override
				public int mul(int a, int b) {
					return 0;
				}

				@Override
				public int inv(int a) {
					throw new ArithmeticException("divide by zero");
				}

				@Override
				public int pow(int a, long b) {
					return 0;
				}
			}

			static final class ModArithmetic2 implements ModArithmetic {

				@Override
				public int mod() {
					return 2;
				}

				@Override
				public int remainder(long value) {
					return (int)(value & 1);
				}

				@Override
				public int add(int a, int b) {
					return a ^ b;
				}

				@Override
				public int sub(int a, int b) {
					return a ^ b;
				}

				@Override
				public int mul(int a, int b) {
					return a & b;
				}

				@Override
				public int inv(int a) {
					if (a == 0) throw new ArithmeticException("divide by zero");
					return a;
				}

				@Override
				public int pow(int a, long b) {
					if (b == 0) return 1;
					return a;
				}
			}

			static final class ModArithmetic998244353 implements ModArithmetic {

				private final int mod = 998244353;

				@Override
				public int mod() {
					return mod;
				}

				@Override
				public int remainder(long value) {
					return (int)((value %= mod) < 0 ? value + mod : value);
				}

				@Override
				public int add(int a, int b) {
					int res = a + b;
					return res >= mod ? res - mod : res;
				}

				@Override
				public int sub(int a, int b) {
					int res = a - b;
					return res < 0 ? res + mod : res;
				}

				@Override
				public int mul(int a, int b) {
					return (int)(((long)a * b) % mod);
				}

				@Override
				public int inv(int a) {
					int b = mod;
					long u = 1, v = 0;
					while (b >= 1) {
						long t = a / b;
						a -= t * b;
						int tmp1 = a;
						a = b;
						b = tmp1;
						u -= t * v;
						long tmp2 = u;
						u = v;
						v = tmp2;
					}
					u %= mod;
					if (a != 1) { throw new ArithmeticException("divide by zero"); }
					return (int)(u < 0 ? u + mod : u);
				}

				@Override
				public int pow(int a, long b) {
					if (b < 0) throw new ArithmeticException("negative power");
					long res = 1;
					long pow2 = a;
					long idx = 1;
					while (b > 0) {
						long lsb = b & -b;
						for (; lsb != idx; idx <<= 1) {
							pow2 = (pow2 * pow2) % mod;
						}
						res = (res * pow2) % mod;
						b ^= lsb;
					}
					return (int)res;
				}
			}

			static final class ModArithmetic1000000007 implements ModArithmetic {

				private final int mod = 1000000007;

				@Override
				public int mod() {
					return mod;
				}

				@Override
				public int remainder(long value) {
					return (int)((value %= mod) < 0 ? value + mod : value);
				}

				@Override
				public int add(int a, int b) {
					int res = a + b;
					return res >= mod ? res - mod : res;
				}

				@Override
				public int sub(int a, int b) {
					int res = a - b;
					return res < 0 ? res + mod : res;
				}

				@Override
				public int mul(int a, int b) {
					return (int)(((long)a * b) % mod);
				}

				@Override
				public int div(int a, int b) {
					return mul(a, inv(b));
				}

				@Override
				public int inv(int a) {
					int b = mod;
					long u = 1, v = 0;
					while (b >= 1) {
						long t = a / b;
						a -= t * b;
						int tmp1 = a;
						a = b;
						b = tmp1;
						u -= t * v;
						long tmp2 = u;
						u = v;
						v = tmp2;
					}
					u %= mod;
					if (a != 1) { throw new ArithmeticException("divide by zero"); }
					return (int)(u < 0 ? u + mod : u);
				}

				@Override
				public int pow(int a, long b) {
					if (b < 0) throw new ArithmeticException("negative power");
					long res = 1;
					long pow2 = a;
					long idx = 1;
					while (b > 0) {
						long lsb = b & -b;
						for (; lsb != idx; idx <<= 1) {
							pow2 = (pow2 * pow2) % mod;
						}
						res = (res * pow2) % mod;
						b ^= lsb;
					}
					return (int)res;
				}
			}

			static final class ModArithmeticMontgomery extends ModArithmeticDynamic {

				private final long negInv;
				private final long r2, r3;

				private ModArithmeticMontgomery(int mod) {
					super(mod);
					long inv = 0;
					long s = 1, t = 0;
					for (int i = 0; i < 32; i++ ) {
						if ((t & 1) == 0) {
							t += mod;
							inv += s;
						}
						t >>= 1;
						s <<= 1;
					}
					long r = (1l << 32) % mod;
					this.negInv = inv;
					this.r2 = (r * r) % mod;
					this.r3 = (r2 * r) % mod;
				}

				private int generate(long x) {
					return reduce(x * r2);
				}

				private int reduce(long x) {
					x = (x + ((x * negInv) & 0xffff_ffffl) * mod) >>> 32;
					return (int)(x < mod ? x : x - mod);
				}

				@Override
				public int remainder(long value) {
					return generate((value %= mod) < 0 ? value + mod : value);
				}

				@Override
				public int mul(int a, int b) {
					return reduce((long)a * b);
				}

				@Override
				public int inv(int a) {
					a = super.inv(a);
					return reduce(a * r3);
				}

				@Override
				public int pow(int a, long b) {
					return generate(super.pow(a, b));
				}
			}

			static final class ModArithmeticBarrett extends ModArithmeticDynamic {

				private static final long mask = 0xffff_ffffl;
				private final long mh;
				private final long ml;

				private ModArithmeticBarrett(int mod) {
					super(mod);
					/**
					 * m = floor(2^64/mod)
					 * 2^64 = p*mod + q, 2^32 = a*mod + b
					 * => (a*mod + b)^2 = p*mod + q
					 * => p = mod*a^2 + 2ab + floor(b^2/mod)
					 */
					long a = (1l << 32) / mod;
					long b = (1l << 32) % mod;
					long m = a * a * mod + 2 * a * b + (b * b) / mod;
					mh = m >>> 32;
					ml = m & mask;
				}

				private int reduce(long x) {
					long z = (x & mask) * ml;
					z = (x & mask) * mh + (x >>> 32) * ml + (z >>> 32);
					z = (x >>> 32) * mh + (z >>> 32);
					x -= z * mod;
					return (int)(x < mod ? x : x - mod);
				}

				@Override
				public int remainder(long value) {
					return (int)((value %= mod) < 0 ? value + mod : value);
				}

				@Override
				public int mul(int a, int b) {
					return reduce((long)a * b);
				}
			}

			static class ModArithmeticDynamic implements ModArithmetic {

				final int mod;

				public ModArithmeticDynamic(int mod) {
					this.mod = mod;
				}

				@Override
				public int mod() {
					return mod;
				}

				@Override
				public int remainder(long value) {
					return (int)((value %= mod) < 0 ? value + mod : value);
				}

				@Override
				public int add(int a, int b) {
					int sum = a + b;
					return sum >= mod ? sum - mod : sum;
				}

				@Override
				public int sub(int a, int b) {
					int sum = a - b;
					return sum < 0 ? sum + mod : sum;
				}

				@Override
				public int mul(int a, int b) {
					return (int)(((long)a * b) % mod);
				}

				@Override
				public int inv(int a) {
					int b = mod;
					long u = 1, v = 0;
					while (b >= 1) {
						long t = a / b;
						a -= t * b;
						int tmp1 = a;
						a = b;
						b = tmp1;
						u -= t * v;
						long tmp2 = u;
						u = v;
						v = tmp2;
					}
					u %= mod;
					if (a != 1) { throw new ArithmeticException("divide by zero"); }
					return (int)(u < 0 ? u + mod : u);
				}

				@Override
				public int pow(int a, long b) {
					if (b < 0) throw new ArithmeticException("negative power");
					int res = 1;
					int pow2 = a;
					long idx = 1;
					while (b > 0) {
						long lsb = b & -b;
						for (; lsb != idx; idx <<= 1) {
							pow2 = mul(pow2, pow2);
						}
						res = mul(res, pow2);
						b ^= lsb;
					}
					return res;
				}
			}
		}
	}

	/**
	 * Convolution.
	 *
	 * @verified https://atcoder.jp/contests/practice2/tasks/practice2_f
	 * @verified https://judge.yosupo.jp/problem/convolution_mod_1000000007
	 */
	public static final class Convolution {

		/**
		 * Find a primitive root.
		 *
		 * @param m A prime number.
		 * @return Primitive root.
		 */
		private static int primitiveRoot(int m) {
			if (m == 2) return 1;
			if (m == 167772161) return 3;
			if (m == 469762049) return 3;
			if (m == 754974721) return 11;
			if (m == 998244353) return 3;

			int[] divs = new int[20];
			divs[0] = 2;
			int cnt = 1;
			int x = (m - 1) / 2;
			while (x % 2 == 0)
				x /= 2;
			for (int i = 3; (long)(i) * i <= x; i += 2) {
				if (x % i == 0) {
					divs[cnt++ ] = i;
					while (x % i == 0) {
						x /= i;
					}
				}
			}
			if (x > 1) {
				divs[cnt++ ] = x;
			}
			for (int g = 2;; g++ ) {
				boolean ok = true;
				for (int i = 0; i < cnt; i++ ) {
					if (pow(g, (m - 1) / divs[i], m) == 1) {
						ok = false;
						break;
					}
				}
				if (ok) return g;
			}
		}

		/**
		 * Power.
		 *
		 * @param x Parameter x.
		 * @param n Parameter n.
		 * @param m Mod.
		 * @return n-th power of x mod m.
		 */
		private static long pow(long x, long n, int m) {
			if (m == 1) return 0;
			long r = 1;
			long y = x % m;
			while (n > 0) {
				if ((n & 1) != 0) r = (r * y) % m;
				y = (y * y) % m;
				n >>= 1;
			}
			return r;
		}

		/**
		 * Ceil of power 2.
		 *
		 * @param n Value.
		 * @return Ceil of power 2.
		 */
		private static int ceilPow2(int n) {
			int x = 0;
			while ((1L << x) < n)
				x++ ;
			return x;
		}

		/**
		 * Garner's algorithm.
		 *
		 * @param c    Mod convolution results.
		 * @param mods Mods.
		 * @return Result.
		 */
		private static long garner(long[] c, int[] mods) {
			int n = c.length + 1;
			long[] cnst = new long[n];
			long[] coef = new long[n];
			java.util.Arrays.fill(coef, 1);
			for (int i = 0; i < n - 1; i++ ) {
				int m1 = mods[i];
				long v = (c[i] - cnst[i] + m1) % m1;
				v = v * pow(coef[i], m1 - 2, m1) % m1;

				for (int j = i + 1; j < n; j++ ) {
					long m2 = mods[j];
					cnst[j] = (cnst[j] + coef[j] * v) % m2;
					coef[j] = (coef[j] * m1) % m2;
				}
			}
			return cnst[n - 1];
		}

		/**
		 * Pre-calculation for NTT.
		 *
		 * @param mod NTT Prime.
		 * @param g   Primitive root of mod.
		 * @return Pre-calculation table.
		 */
		private static long[] sumE(int mod, int g) {
			long[] sum_e = new long[30];
			long[] es = new long[30];
			long[] ies = new long[30];
			int cnt2 = Integer.numberOfTrailingZeros(mod - 1);
			long e = pow(g, (mod - 1) >> cnt2, mod);
			long ie = pow(e, mod - 2, mod);
			for (int i = cnt2; i >= 2; i-- ) {
				es[i - 2] = e;
				ies[i - 2] = ie;
				e = e * e % mod;
				ie = ie * ie % mod;
			}
			long now = 1;
			for (int i = 0; i < cnt2 - 2; i++ ) {
				sum_e[i] = es[i] * now % mod;
				now = now * ies[i] % mod;
			}
			return sum_e;
		}

		/**
		 * Pre-calculation for inverse NTT.
		 *
		 * @param mod Mod.
		 * @param g   Primitive root of mod.
		 * @return Pre-calculation table.
		 */
		private static long[] sumIE(int mod, int g) {
			long[] sum_ie = new long[30];
			long[] es = new long[30];
			long[] ies = new long[30];

			int cnt2 = Integer.numberOfTrailingZeros(mod - 1);
			long e = pow(g, (mod - 1) >> cnt2, mod);
			long ie = pow(e, mod - 2, mod);
			for (int i = cnt2; i >= 2; i-- ) {
				es[i - 2] = e;
				ies[i - 2] = ie;
				e = e * e % mod;
				ie = ie * ie % mod;
			}
			long now = 1;
			for (int i = 0; i < cnt2 - 2; i++ ) {
				sum_ie[i] = ies[i] * now % mod;
				now = now * es[i] % mod;
			}
			return sum_ie;
		}

		/**
		 * Inverse NTT.
		 *
		 * @param a     Target array.
		 * @param sumIE Pre-calculation table.
		 * @param mod   NTT Prime.
		 */
		private static void butterflyInv(long[] a, long[] sumIE, int mod) {
			int n = a.length;
			int h = ceilPow2(n);

			for (int ph = h; ph >= 1; ph-- ) {
				int w = 1 << (ph - 1), p = 1 << (h - ph);
				long inow = 1;
				for (int s = 0; s < w; s++ ) {
					int offset = s << (h - ph + 1);
					for (int i = 0; i < p; i++ ) {
						long l = a[i + offset];
						long r = a[i + offset + p];
						a[i + offset] = (l + r) % mod;
						a[i + offset + p] = (mod + l - r) * inow % mod;
					}
					int x = Integer.numberOfTrailingZeros(~s);
					inow = inow * sumIE[x] % mod;
				}
			}
		}

		/**
		 * Inverse NTT.
		 *
		 * @param a    Target array.
		 * @param sumE Pre-calculation table.
		 * @param mod  NTT Prime.
		 */
		private static void butterfly(long[] a, long[] sumE, int mod) {
			int n = a.length;
			int h = ceilPow2(n);

			for (int ph = 1; ph <= h; ph++ ) {
				int w = 1 << (ph - 1), p = 1 << (h - ph);
				long now = 1;
				for (int s = 0; s < w; s++ ) {
					int offset = s << (h - ph + 1);
					for (int i = 0; i < p; i++ ) {
						long l = a[i + offset];
						long r = a[i + offset + p] * now % mod;
						a[i + offset] = (l + r) % mod;
						a[i + offset + p] = (l - r + mod) % mod;
					}
					int x = Integer.numberOfTrailingZeros(~s);
					now = now * sumE[x] % mod;
				}
			}
		}

		/**
		 * Convolution.
		 *
		 * @param a   Target array 1.
		 * @param b   Target array 2.
		 * @param mod NTT Prime.
		 * @return Answer.
		 */
		public static long[] convolution(long[] a, long[] b, int mod) {
			int n = a.length;
			int m = b.length;
			if (n == 0 || m == 0) return new long[0];

			int z = 1 << ceilPow2(n + m - 1);
			{
				long[] na = new long[z];
				long[] nb = new long[z];
				System.arraycopy(a, 0, na, 0, n);
				System.arraycopy(b, 0, nb, 0, m);
				a = na;
				b = nb;
			}

			int g = primitiveRoot(mod);
			long[] sume = sumE(mod, g);
			long[] sumie = sumIE(mod, g);

			butterfly(a, sume, mod);
			butterfly(b, sume, mod);
			for (int i = 0; i < z; i++ ) {
				a[i] = a[i] * b[i] % mod;
			}
			butterflyInv(a, sumie, mod);
			a = java.util.Arrays.copyOf(a, n + m - 1);

			long iz = pow(z, mod - 2, mod);
			for (int i = 0; i < n + m - 1; i++ )
				a[i] = a[i] * iz % mod;
			return a;
		}

		/**
		 * Convolution.
		 *
		 * @param a   Target array 1.
		 * @param b   Target array 2.
		 * @param mod Any mod.
		 * @return Answer.
		 */
		public static long[] convolutionLL(long[] a, long[] b, int mod) {
			int n = a.length;
			int m = b.length;
			if (n == 0 || m == 0) return new long[0];

			int mod1 = 754974721;
			int mod2 = 167772161;
			int mod3 = 469762049;

			long[] c1 = convolution(a, b, mod1);
			long[] c2 = convolution(a, b, mod2);
			long[] c3 = convolution(a, b, mod3);

			int retSize = c1.length;
			long[] ret = new long[retSize];
			int[] mods = {mod1, mod2, mod3, mod};
			for (int i = 0; i < retSize; ++i) {
				ret[i] = garner(new long[]{c1[i], c2[i], c3[i]}, mods);
			}
			return ret;
		}

		/**
		 * Convolution by ModInt.
		 *
		 * @param a Target array 1.
		 * @param b Target array 2.
		 * @return Answer.
		 */
		public static java.util.List<ModIntFactory.ModInt> convolution(java.util.List<ModIntFactory.ModInt> a,
				java.util.List<ModIntFactory.ModInt> b) {
			int mod = a.get(0).mod();
			long[] va = a.stream().mapToLong(ModIntFactory.ModInt::value).toArray();
			long[] vb = b.stream().mapToLong(ModIntFactory.ModInt::value).toArray();
			long[] c = convolutionLL(va, vb, mod);

			ModIntFactory factory = new ModIntFactory(mod);
			return java.util.Arrays.stream(c).mapToObj(factory::create).collect(java.util.stream.Collectors.toList());
		}

		/**
		 * Naive convolution. (Complexity is O(N^2)!!)
		 *
		 * @param a   Target array 1.
		 * @param b   Target array 2.
		 * @param mod Mod.
		 * @return Answer.
		 */
		public static long[] convolutionNaive(long[] a, long[] b, int mod) {
			int n = a.length;
			int m = b.length;
			int k = n + m - 1;
			long[] ret = new long[k];
			for (int i = 0; i < n; i++ ) {
				for (int j = 0; j < m; j++ ) {
					ret[i + j] += a[i] * b[j] % mod;
					ret[i + j] %= mod;
				}
			}
			return ret;
		}
	}

	/**
	 * @verified https://atcoder.jp/contests/practice2/tasks/practice2_g
	 */
	public static final class SCC {

		static class Edge {

			int from, to;

			public Edge(int from, int to) {
				this.from = from;
				this.to = to;
			}
		}

		final int n;
		int m;
		final java.util.ArrayList<Edge> unorderedEdges;
		final int[] start;
		final int[] ids;
		boolean hasBuilt = false;

		public SCC(int n) {
			this.n = n;
			this.unorderedEdges = new java.util.ArrayList<>();
			this.start = new int[n + 1];
			this.ids = new int[n];
		}

		public void addEdge(int from, int to) {
			rangeCheck(from);
			rangeCheck(to);
			unorderedEdges.add(new Edge(from, to));
			start[from + 1]++ ;
			this.m++ ;
		}

		public int id(int i) {
			if (!hasBuilt) { throw new UnsupportedOperationException("Graph hasn't been built."); }
			rangeCheck(i);
			return ids[i];
		}

		public int[][] build() {
			for (int i = 1; i <= n; i++ ) {
				start[i] += start[i - 1];
			}
			Edge[] orderedEdges = new Edge[m];
			int[] count = new int[n + 1];
			System.arraycopy(start, 0, count, 0, n + 1);
			for (Edge e : unorderedEdges) {
				orderedEdges[count[e.from]++ ] = e;
			}
			int nowOrd = 0;
			int groupNum = 0;
			int k = 0;
			// parent
			int[] par = new int[n];
			int[] vis = new int[n];
			int[] low = new int[n];
			int[] ord = new int[n];
			java.util.Arrays.fill(ord, -1);
			// u = lower32(stack[i]) : visiting vertex
			// j = upper32(stack[i]) : jth child
			long[] stack = new long[n];
			// size of stack
			int ptr = 0;
			// non-recursional DFS
			for (int i = 0; i < n; i++ ) {
				if (ord[i] >= 0) continue;
				par[i] = -1;
				// vertex i, 0th child.
				stack[ptr++ ] = 0l << 32 | i;
				// stack is not empty
				while (ptr > 0) {
					// last element
					long p = stack[ --ptr];
					// vertex
					int u = (int)(p & 0xffff_ffffl);
					// jth child
					int j = (int)(p >>> 32);
					if (j == 0) { // first visit
						low[u] = ord[u] = nowOrd++ ;
						vis[k++ ] = u;
					}
					if (start[u] + j < count[u]) { // there are more children
						// jth child
						int to = orderedEdges[start[u] + j].to;
						// incr children counter
						stack[ptr++ ] += 1l << 32;
						if (ord[to] == -1) { // new vertex
							stack[ptr++ ] = 0l << 32 | to;
							par[to] = u;
						} else { // backward edge
							low[u] = Math.min(low[u], ord[to]);
						}
					} else { // no more children (leaving)
						while (j-- > 0) {
							int to = orderedEdges[start[u] + j].to;
							// update lowlink
							if (par[to] == u) low[u] = Math.min(low[u], low[to]);
						}
						if (low[u] == ord[u]) { // root of a component
							while (true) { // gathering verticies
								int v = vis[ --k];
								ord[v] = n;
								ids[v] = groupNum;
								if (v == u) break;
							}
							groupNum++ ; // incr the number of components
						}
					}
				}
			}
			for (int i = 0; i < n; i++ ) {
				ids[i] = groupNum - 1 - ids[i];
			}

			int[] counts = new int[groupNum];
			for (int x : ids)
				counts[x]++ ;
			int[][] groups = new int[groupNum][];
			for (int i = 0; i < groupNum; i++ ) {
				groups[i] = new int[counts[i]];
			}
			for (int i = 0; i < n; i++ ) {
				int cmp = ids[i];
				groups[cmp][ --counts[cmp]] = i;
			}
			hasBuilt = true;
			return groups;
		}

		private void rangeCheck(int i) {
			if (i < 0 || i >= n) { throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for length %d", i, n)); }
		}
	}

	/**
	 * @verified https://atcoder.jp/contests/practice2/submissions/16647102
	 */
	public static final class TwoSAT {

		private final int n;
		private final InternalSCC scc;
		private final boolean[] answer;

		private boolean hasCalledSatisfiable = false;
		private boolean existsAnswer = false;

		public TwoSAT(int n) {
			this.n = n;
			this.scc = new InternalSCC(2 * n);
			this.answer = new boolean[n];
		}

		public void addClause(int x, boolean f, int y, boolean g) {
			rangeCheck(x);
			rangeCheck(y);
			scc.addEdge(x << 1 | (f ? 0 : 1), y << 1 | (g ? 1 : 0));
			scc.addEdge(y << 1 | (g ? 0 : 1), x << 1 | (f ? 1 : 0));
		}

		public void addImplication(int x, boolean f, int y, boolean g) {
			addClause(x, !f, y, g);
		}

		public void addNand(int x, boolean f, int y, boolean g) {
			addClause(x, !f, y, !g);
		}

		public boolean satisfiable() {
			hasCalledSatisfiable = true;
			int[] ids = scc.ids();
			for (int i = 0; i < n; i++ ) {
				if (ids[i << 1 | 0] == ids[i << 1 | 1]) return existsAnswer = false;
				answer[i] = ids[i << 1 | 0] < ids[i << 1 | 1];
			}
			return existsAnswer = true;
		}

		public boolean[] answer() {
			if (!hasCalledSatisfiable) {
				throw new UnsupportedOperationException("Call TwoSAT#satisfiable at least once before TwoSAT#answer.");
			}
			if (existsAnswer) return answer;
			return null;
		}

		private void rangeCheck(int x) {
			if (0 < x || x >= n) { throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for length %d", x, n)); }
		}

		private static final class EdgeList {

			long[] a;
			int ptr = 0;

			EdgeList(int cap) {
				a = new long[cap];
			}

			void add(int upper, int lower) {
				if (ptr == a.length) grow();
				a[ptr++ ] = (long)upper << 32 | lower;
			}

			void grow() {
				long[] b = new long[a.length << 1];
				System.arraycopy(a, 0, b, 0, a.length);
				a = b;
			}
		}

		private static final class InternalSCC {

			final int n;
			int m;
			final EdgeList unorderedEdges;
			final int[] start;

			InternalSCC(int n) {
				this.n = n;
				this.unorderedEdges = new EdgeList(n);
				this.start = new int[n + 1];
			}

			void addEdge(int from, int to) {
				unorderedEdges.add(from, to);
				start[from + 1]++ ;
				this.m++ ;
			}

			static final long mask = 0xffff_ffffl;

			int[] ids() {
				for (int i = 1; i <= n; i++ ) {
					start[i] += start[i - 1];
				}
				int[] orderedEdges = new int[m];
				int[] count = new int[n + 1];
				System.arraycopy(start, 0, count, 0, n + 1);
				for (int i = 0; i < m; i++ ) {
					long e = unorderedEdges.a[i];
					orderedEdges[count[(int)(e >>> 32)]++ ] = (int)(e & mask);
				}
				int nowOrd = 0;
				int groupNum = 0;
				int k = 0;
				int[] par = new int[n];
				int[] vis = new int[n];
				int[] low = new int[n];
				int[] ord = new int[n];
				java.util.Arrays.fill(ord, -1);
				int[] ids = new int[n];
				long[] stack = new long[n];
				int ptr = 0;

				for (int i = 0; i < n; i++ ) {
					if (ord[i] >= 0) continue;
					par[i] = -1;
					stack[ptr++ ] = i;
					while (ptr > 0) {
						long p = stack[ --ptr];
						int u = (int)(p & mask);
						int j = (int)(p >>> 32);
						if (j == 0) {
							low[u] = ord[u] = nowOrd++ ;
							vis[k++ ] = u;
						}
						if (start[u] + j < count[u]) {
							int to = orderedEdges[start[u] + j];
							stack[ptr++ ] += 1l << 32;
							if (ord[to] == -1) {
								stack[ptr++ ] = to;
								par[to] = u;
							} else {
								low[u] = Math.min(low[u], ord[to]);
							}
						} else {
							while (j-- > 0) {
								int to = orderedEdges[start[u] + j];
								if (par[to] == u) low[u] = Math.min(low[u], low[to]);
							}
							if (low[u] == ord[u]) {
								while (true) {
									int v = vis[ --k];
									ord[v] = n;
									ids[v] = groupNum;
									if (v == u) break;
								}
								groupNum++ ;
							}
						}
					}
				}
				for (int i = 0; i < n; i++ ) {
					ids[i] = groupNum - 1 - ids[i];
				}
				return ids;
			}
		}
	}

	public static final class StringAlgorithm {

		private static int[] saNaive(int[] s) {
			int n = s.length;
			Integer[] _sa = new Integer[n];
			for (int i = 0; i < n; i++ ) {
				_sa[i] = i;
			}
			java.util.Arrays.sort(_sa, (l, r) -> {
				while (l < n && r < n) {
					if (s[l] != s[r]) return s[l] - s[r];
					l++ ;
					r++ ;
				}
				return -(l - r);
			});
			int[] sa = new int[n];
			for (int i = 0; i < n; i++ ) {
				sa[i] = _sa[i];
			}
			return sa;
		}

		private static int[] saDoubling(int[] s) {
			int n = s.length;
			Integer[] _sa = new Integer[n];
			for (int i = 0; i < n; i++ ) {
				_sa[i] = i;
			}
			int[] rnk = s;
			int[] tmp = new int[n];

			for (int k = 1; k < n; k *= 2) {
				final int _k = k;
				final int[] _rnk = rnk;
				java.util.Comparator<Integer> cmp = (x, y) -> {
					if (_rnk[x] != _rnk[y]) return _rnk[x] - _rnk[y];
					int rx = x + _k < n ? _rnk[x + _k] : -1;
					int ry = y + _k < n ? _rnk[y + _k] : -1;
					return rx - ry;
				};
				java.util.Arrays.sort(_sa, cmp);
				tmp[_sa[0]] = 0;
				for (int i = 1; i < n; i++ ) {
					tmp[_sa[i]] = tmp[_sa[i - 1]] + (cmp.compare(_sa[i - 1], _sa[i]) < 0 ? 1 : 0);
				}
				int[] buf = tmp;
				tmp = rnk;
				rnk = buf;
			}

			int[] sa = new int[n];
			for (int i = 0; i < n; i++ ) {
				sa[i] = _sa[i];
			}
			return sa;
		}

		private static final int THRESHOLD_NAIVE = 10;
		private static final int THRESHOLD_DOUBLING = 40;

		private static int[] sais(int[] s, int upper) {
			int n = s.length;
			if (n == 0) return new int[0];
			if (n == 1) return new int[]{0};
			if (n == 2) {
				return s[0] < s[1] ? new int[]{0, 1} : new int[] {1, 0};
			}
			if (n < THRESHOLD_NAIVE) { return saNaive(s); }
			if (n < THRESHOLD_DOUBLING) { return saDoubling(s); }

			int[] sa = new int[n];
			boolean[] ls = new boolean[n];
			for (int i = n - 2; i >= 0; i-- ) {
				ls[i] = s[i] == s[i + 1] ? ls[i + 1] : s[i] < s[i + 1];
			}

			int[] sumL = new int[upper + 1];
			int[] sumS = new int[upper + 1];

			for (int i = 0; i < n; i++ ) {
				if (ls[i]) {
					sumL[s[i] + 1]++ ;
				} else {
					sumS[s[i]]++ ;
				}
			}

			for (int i = 0; i <= upper; i++ ) {
				sumS[i] += sumL[i];
				if (i < upper) sumL[i + 1] += sumS[i];
			}

			java.util.function.Consumer<int[]> induce = lms -> {
				java.util.Arrays.fill(sa, -1);
				int[] buf = new int[upper + 1];
				System.arraycopy(sumS, 0, buf, 0, upper + 1);
				for (int d : lms) {
					if (d == n) continue;
					sa[buf[s[d]]++ ] = d;
				}
				System.arraycopy(sumL, 0, buf, 0, upper + 1);
				sa[buf[s[n - 1]]++ ] = n - 1;
				for (int i = 0; i < n; i++ ) {
					int v = sa[i];
					if (v >= 1 && !ls[v - 1]) {
						sa[buf[s[v - 1]]++ ] = v - 1;
					}
				}
				System.arraycopy(sumL, 0, buf, 0, upper + 1);
				for (int i = n - 1; i >= 0; i-- ) {
					int v = sa[i];
					if (v >= 1 && ls[v - 1]) {
						sa[ --buf[s[v - 1] + 1]] = v - 1;
					}
				}
			};

			int[] lmsMap = new int[n + 1];
			java.util.Arrays.fill(lmsMap, -1);
			int m = 0;
			for (int i = 1; i < n; i++ ) {
				if (!ls[i - 1] && ls[i]) {
					lmsMap[i] = m++ ;
				}
			}

			int[] lms = new int[m];
			{
				int p = 0;
				for (int i = 1; i < n; i++ ) {
					if (!ls[i - 1] && ls[i]) {
						lms[p++ ] = i;
					}
				}
			}

			induce.accept(lms);

			if (m > 0) {
				int[] sortedLms = new int[m];
				{
					int p = 0;
					for (int v : sa) {
						if (lmsMap[v] != -1) {
							sortedLms[p++ ] = v;
						}
					}
				}
				int[] recS = new int[m];
				int recUpper = 0;
				recS[lmsMap[sortedLms[0]]] = 0;
				for (int i = 1; i < m; i++ ) {
					int l = sortedLms[i - 1], r = sortedLms[i];
					int endL = (lmsMap[l] + 1 < m) ? lms[lmsMap[l] + 1] : n;
					int endR = (lmsMap[r] + 1 < m) ? lms[lmsMap[r] + 1] : n;
					boolean same = true;
					if (endL - l != endR - r) {
						same = false;
					} else {
						while (l < endL && s[l] == s[r]) {
							l++ ;
							r++ ;
						}
						if (l == n || s[l] != s[r]) same = false;
					}
					if (!same) {
						recUpper++ ;
					}
					recS[lmsMap[sortedLms[i]]] = recUpper;
				}

				int[] recSA = sais(recS, recUpper);

				for (int i = 0; i < m; i++ ) {
					sortedLms[i] = lms[recSA[i]];
				}
				induce.accept(sortedLms);
			}
			return sa;
		}

		public static int[] suffixArray(int[] s, int upper) {
			assert (0 <= upper);
			for (int d : s) {
				assert (0 <= d && d <= upper);
			}
			return sais(s, upper);
		}

		public static int[] suffixArray(int[] s) {
			int n = s.length;
			Integer[] idx = new Integer[n];
			for (int i = 0; i < n; i++ ) {
				idx[i] = i;
			}
			java.util.Arrays.sort(idx, (l, r) -> s[l] - s[r]);
			int[] s2 = new int[n];
			int now = 0;
			for (int i = 0; i < n; i++ ) {
				if (i > 0 && s[idx[i - 1]] != s[idx[i]]) {
					now++ ;
				}
				s2[idx[i]] = now;
			}
			return sais(s2, now);
		}

		public static int[] suffixArray(char[] s) {
			int n = s.length;
			int[] s2 = new int[n];
			for (int i = 0; i < n; i++ ) {
				s2[i] = s[i];
			}
			return sais(s2, 255);
		}

		public static int[] suffixArray(java.lang.String s) {
			return suffixArray(s.toCharArray());
		}

		public static int[] lcpArray(int[] s, int[] sa) {
			int n = s.length;
			assert (n >= 1);
			int[] rnk = new int[n];
			for (int i = 0; i < n; i++ ) {
				rnk[sa[i]] = i;
			}
			int[] lcp = new int[n - 1];
			int h = 0;
			for (int i = 0; i < n; i++ ) {
				if (h > 0) h-- ;
				if (rnk[i] == 0) {
					continue;
				}
				int j = sa[rnk[i] - 1];
				for (; j + h < n && i + h < n; h++ ) {
					if (s[j + h] != s[i + h]) break;
				}
				lcp[rnk[i] - 1] = h;
			}
			return lcp;
		}

		public static int[] lcpArray(char[] s, int[] sa) {
			int n = s.length;
			int[] s2 = new int[n];
			for (int i = 0; i < n; i++ ) {
				s2[i] = s[i];
			}
			return lcpArray(s2, sa);
		}

		public static int[] lcpArray(java.lang.String s, int[] sa) {
			return lcpArray(s.toCharArray(), sa);
		}

		public static int[] zAlgorithm(int[] s) {
			int n = s.length;
			if (n == 0) return new int[0];
			int[] z = new int[n];
			for (int i = 1, j = 0; i < n; i++ ) {
				int k = j + z[j] <= i ? 0 : Math.min(j + z[j] - i, z[i - j]);
				while (i + k < n && s[k] == s[i + k])
					k++ ;
				z[i] = k;
				if (j + z[j] < i + z[i]) j = i;
			}
			z[0] = n;
			return z;
		}

		public static int[] zAlgorithm(char[] s) {
			int n = s.length;
			if (n == 0) return new int[0];
			int[] z = new int[n];
			for (int i = 1, j = 0; i < n; i++ ) {
				int k = j + z[j] <= i ? 0 : Math.min(j + z[j] - i, z[i - j]);
				while (i + k < n && s[k] == s[i + k])
					k++ ;
				z[i] = k;
				if (j + z[j] < i + z[i]) j = i;
			}
			z[0] = n;
			return z;
		}

		public static int[] zAlgorithm(String s) {
			return zAlgorithm(s.toCharArray());
		}
	}

	/**
	 * @verified https://atcoder.jp/contests/practice2/tasks/practice2_j
	 */
	public static final class SegTree<S> {

		final int MAX;

		final int N;
		final java.util.function.BinaryOperator<S> op;
		final S E;

		final S[] data;

		@SuppressWarnings("unchecked")
		public SegTree(int n, java.util.function.BinaryOperator<S> op, S e) {
			this.MAX = n;
			int k = 1;
			while (k < n)
				k <<= 1;
			this.N = k;
			this.E = e;
			this.op = op;
			this.data = (S[])new Object[N << 1];
			java.util.Arrays.fill(data, E);
		}

		public SegTree(S[] dat, java.util.function.BinaryOperator<S> op, S e) {
			this(dat.length, op, e);
			build(dat);
		}

		private void build(S[] dat) {
			int l = dat.length;
			System.arraycopy(dat, 0, data, N, l);
			for (int i = N - 1; i > 0; i-- ) {
				data[i] = op.apply(data[i << 1 | 0], data[i << 1 | 1]);
			}
		}

		public void set(int p, S x) {
			exclusiveRangeCheck(p);
			data[p += N] = x;
			p >>= 1;
			while (p > 0) {
				data[p] = op.apply(data[p << 1 | 0], data[p << 1 | 1]);
				p >>= 1;
			}
		}

		public S get(int p) {
			exclusiveRangeCheck(p);
			return data[p + N];
		}

		public S prod(int l, int r) {
			if (l > r) { throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r)); }
			inclusiveRangeCheck(l);
			inclusiveRangeCheck(r);
			S sumLeft = E;
			S sumRight = E;
			l += N;
			r += N;
			while (l < r) {
				if ((l & 1) == 1) sumLeft = op.apply(sumLeft, data[l++ ]);
				if ((r & 1) == 1) sumRight = op.apply(data[ --r], sumRight);
				l >>= 1;
				r >>= 1;
			}
			return op.apply(sumLeft, sumRight);
		}

		public S allProd() {
			return data[1];
		}

		public int maxRight(int l, java.util.function.Predicate<S> f) {
			inclusiveRangeCheck(l);
			if (!f.test(E)) { throw new IllegalArgumentException("Identity element must satisfy the condition."); }
			if (l == MAX) return MAX;
			l += N;
			S sum = E;
			do {
				l >>= Integer.numberOfTrailingZeros(l);
				if (!f.test(op.apply(sum, data[l]))) {
					while (l < N) {
						l = l << 1;
						if (f.test(op.apply(sum, data[l]))) {
							sum = op.apply(sum, data[l]);
							l++ ;
						}
					}
					return l - N;
				}
				sum = op.apply(sum, data[l]);
				l++ ;
			} while ((l & -l) != l);
			return MAX;
		}

		public int minLeft(int r, java.util.function.Predicate<S> f) {
			inclusiveRangeCheck(r);
			if (!f.test(E)) { throw new IllegalArgumentException("Identity element must satisfy the condition."); }
			if (r == 0) return 0;
			r += N;
			S sum = E;
			do {
				r-- ;
				while (r > 1 && (r & 1) == 1)
					r >>= 1;
				if (!f.test(op.apply(data[r], sum))) {
					while (r < N) {
						r = r << 1 | 1;
						if (f.test(op.apply(data[r], sum))) {
							sum = op.apply(data[r], sum);
							r-- ;
						}
					}
					return r + 1 - N;
				}
				sum = op.apply(data[r], sum);
			} while ((r & -r) != r);
			return 0;
		}

		private void exclusiveRangeCheck(int p) {
			if (p < 0 || p >= MAX) {
				throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for the range [%d, %d).", p, 0, MAX));
			}
		}

		private void inclusiveRangeCheck(int p) {
			if (p < 0 || p > MAX) {
				throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for the range [%d, %d].", p, 0, MAX));
			}
		}

		// **************** DEBUG **************** //

		private int indent = 6;

		public void setIndent(int newIndent) {
			this.indent = newIndent;
		}

		@Override
		public String toString() {
			return toString(1, 0);
		}

		private String toString(int k, int sp) {
			if (k >= N) return indent(sp) + data[k];
			String s = "";
			s += toString(k << 1 | 1, sp + indent);
			s += "\n";
			s += indent(sp) + data[k];
			s += "\n";
			s += toString(k << 1 | 0, sp + indent);
			return s;
		}

		private static String indent(int n) {
			StringBuilder sb = new StringBuilder();
			while (n-- > 0)
				sb.append(' ');
			return sb.toString();
		}
	}

	/**
	 * TODO: verify {@link LazySegTree#maxRight} and {@link LazySegTree#minLeft}
	 *
	 * @verified https://atcoder.jp/contests/practice2/tasks/practice2_k
	 */

	public static final class LazySegTree<S, F> {

		final int MAX;

		final int N;
		final int Log;
		final java.util.function.BinaryOperator<S> Op;
		final S E;
		final java.util.function.BiFunction<F, S, S> Mapping;
		final java.util.function.BinaryOperator<F> Composition;
		final F Id;

		final S[] Dat;
		final F[] Laz;

		@SuppressWarnings("unchecked")
		public LazySegTree(int n, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping,
				java.util.function.BinaryOperator<F> composition, F id) {
			this.MAX = n;
			int k = 1;
			while (k < n)
				k <<= 1;
			this.N = k;
			this.Log = Integer.numberOfTrailingZeros(N);
			this.Op = op;
			this.E = e;
			this.Mapping = mapping;
			this.Composition = composition;
			this.Id = id;
			this.Dat = (S[])new Object[N << 1];
			this.Laz = (F[])new Object[N];
			java.util.Arrays.fill(Dat, E);
			java.util.Arrays.fill(Laz, Id);
		}

		public LazySegTree(S[] dat, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping,
				java.util.function.BinaryOperator<F> composition, F id) {
			this(dat.length, op, e, mapping, composition, id);
			build(dat);
		}

		private void build(S[] dat) {
			int l = dat.length;
			System.arraycopy(dat, 0, Dat, N, l);
			for (int i = N - 1; i > 0; i-- ) {
				Dat[i] = Op.apply(Dat[i << 1 | 0], Dat[i << 1 | 1]);
			}
		}

		private void push(int k) {
			if (Laz[k] == Id) return;
			int lk = k << 1 | 0, rk = k << 1 | 1;
			Dat[lk] = Mapping.apply(Laz[k], Dat[lk]);
			Dat[rk] = Mapping.apply(Laz[k], Dat[rk]);
			if (lk < N) Laz[lk] = Composition.apply(Laz[k], Laz[lk]);
			if (rk < N) Laz[rk] = Composition.apply(Laz[k], Laz[rk]);
			Laz[k] = Id;
		}

		private void pushTo(int k) {
			for (int i = Log; i > 0; i-- )
				push(k >> i);
		}

		private void pushTo(int lk, int rk) {
			for (int i = Log; i > 0; i-- ) {
				if (((lk >> i) << i) != lk) push(lk >> i);
				if (((rk >> i) << i) != rk) push(rk >> i);
			}
		}

		private void updateFrom(int k) {
			k >>= 1;
			while (k > 0) {
				Dat[k] = Op.apply(Dat[k << 1 | 0], Dat[k << 1 | 1]);
				k >>= 1;
			}
		}

		private void updateFrom(int lk, int rk) {
			for (int i = 1; i <= Log; i++ ) {
				if (((lk >> i) << i) != lk) {
					int lki = lk >> i;
					Dat[lki] = Op.apply(Dat[lki << 1 | 0], Dat[lki << 1 | 1]);
				}
				if (((rk >> i) << i) != rk) {
					int rki = (rk - 1) >> i;
					Dat[rki] = Op.apply(Dat[rki << 1 | 0], Dat[rki << 1 | 1]);
				}
			}
		}

		public void set(int p, S x) {
			exclusiveRangeCheck(p);
			p += N;
			pushTo(p);
			Dat[p] = x;
			updateFrom(p);
		}

		public S get(int p) {
			exclusiveRangeCheck(p);
			p += N;
			pushTo(p);
			return Dat[p];
		}

		public S prod(int l, int r) {
			if (l > r) { throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r)); }
			inclusiveRangeCheck(l);
			inclusiveRangeCheck(r);
			if (l == r) return E;
			l += N;
			r += N;
			pushTo(l, r);
			S sumLeft = E, sumRight = E;
			while (l < r) {
				if ((l & 1) == 1) sumLeft = Op.apply(sumLeft, Dat[l++ ]);
				if ((r & 1) == 1) sumRight = Op.apply(Dat[ --r], sumRight);
				l >>= 1;
				r >>= 1;
			}
			return Op.apply(sumLeft, sumRight);
		}

		public S allProd() {
			return Dat[1];
		}

		public void apply(int p, F f) {
			exclusiveRangeCheck(p);
			p += N;
			pushTo(p);
			Dat[p] = Mapping.apply(f, Dat[p]);
			updateFrom(p);
		}

		public void apply(int l, int r, F f) {
			if (l > r) { throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r)); }
			inclusiveRangeCheck(l);
			inclusiveRangeCheck(r);
			if (l == r) return;
			l += N;
			r += N;
			pushTo(l, r);
			for (int l2 = l, r2 = r; l2 < r2;) {
				if ((l2 & 1) == 1) {
					Dat[l2] = Mapping.apply(f, Dat[l2]);
					if (l2 < N) Laz[l2] = Composition.apply(f, Laz[l2]);
					l2++ ;
				}
				if ((r2 & 1) == 1) {
					r2-- ;
					Dat[r2] = Mapping.apply(f, Dat[r2]);
					if (r2 < N) Laz[r2] = Composition.apply(f, Laz[r2]);
				}
				l2 >>= 1;
				r2 >>= 1;
			}
			updateFrom(l, r);
		}

		public int maxRight(int l, java.util.function.Predicate<S> g) {
			inclusiveRangeCheck(l);
			if (!g.test(E)) { throw new IllegalArgumentException("Identity element must satisfy the condition."); }
			if (l == MAX) return MAX;
			l += N;
			pushTo(l);
			S sum = E;
			do {
				l >>= Integer.numberOfTrailingZeros(l);
				if (!g.test(Op.apply(sum, Dat[l]))) {
					while (l < N) {
						push(l);
						l = l << 1;
						if (g.test(Op.apply(sum, Dat[l]))) {
							sum = Op.apply(sum, Dat[l]);
							l++ ;
						}
					}
					return l - N;
				}
				sum = Op.apply(sum, Dat[l]);
				l++ ;
			} while ((l & -l) != l);
			return MAX;
		}

		public int minLeft(int r, java.util.function.Predicate<S> g) {
			inclusiveRangeCheck(r);
			if (!g.test(E)) { throw new IllegalArgumentException("Identity element must satisfy the condition."); }
			if (r == 0) return 0;
			r += N;
			pushTo(r - 1);
			S sum = E;
			do {
				r-- ;
				while (r > 1 && (r & 1) == 1)
					r >>= 1;
				if (!g.test(Op.apply(Dat[r], sum))) {
					while (r < N) {
						push(r);
						r = r << 1 | 1;
						if (g.test(Op.apply(Dat[r], sum))) {
							sum = Op.apply(Dat[r], sum);
							r-- ;
						}
					}
					return r + 1 - N;
				}
				sum = Op.apply(Dat[r], sum);
			} while ((r & -r) != r);
			return 0;
		}

		private void exclusiveRangeCheck(int p) {
			if (p < 0 || p >= MAX) { throw new IndexOutOfBoundsException(String.format("Index %d is not in [%d, %d).", p, 0, MAX)); }
		}

		private void inclusiveRangeCheck(int p) {
			if (p < 0 || p > MAX) { throw new IndexOutOfBoundsException(String.format("Index %d is not in [%d, %d].", p, 0, MAX)); }
		}

		// **************** DEBUG **************** //

		private int indent = 6;

		public void setIndent(int newIndent) {
			this.indent = newIndent;
		}

		@Override
		public String toString() {
			return toString(1, 0);
		}

		private String toString(int k, int sp) {
			if (k >= N) return indent(sp) + Dat[k];
			String s = "";
			s += toString(k << 1 | 1, sp + indent);
			s += "\n";
			s += indent(sp) + Dat[k] + "/" + Laz[k];
			s += "\n";
			s += toString(k << 1 | 0, sp + indent);
			return s;
		}

		private static String indent(int n) {
			StringBuilder sb = new StringBuilder();
			while (n-- > 0)
				sb.append(' ');
			return sb.toString();
		}
	}

	public static final class MultiSet<T> extends java.util.TreeMap<T, Long> {

		private static final long serialVersionUID = 1L;

		public MultiSet() {
			super();
		}

		public MultiSet(java.util.List<T> list) {
			super();
			for (T e : list)
				this.addOne(e);
		}

		public long count(Object elm) {
			return getOrDefault(elm, 0L);
		}

		public void add(T elm, long amount) {
			if (!this.containsKey(elm)) put(elm, amount);
			else replace(elm, get(elm) + amount);
			if (this.count(elm) == 0) this.remove(elm);
		}

		public void addOne(T elm) {
			this.add(elm, 1);
		}

		public void removeOne(T elm) {
			this.add(elm, -1);
		}

		public void removeAll(T elm) {
			this.add(elm, -this.count(elm));
		}

		public static <T> MultiSet<T> merge(MultiSet<T> a, MultiSet<T> b) {
			MultiSet<T> c = new MultiSet<>();
			for (T x : a.keySet())
				c.add(x, a.count(x));
			for (T y : b.keySet())
				c.add(y, b.count(y));
			return c;
		}
	}
}

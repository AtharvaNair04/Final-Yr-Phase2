import java.util.*;
import java.io.*;
import java.math.*;
import java.util.function.*;
public class Main implements Runnable {
	static boolean DEBUG;
	public static void main(String[] args) {
		DEBUG = args.length > 0 && args[0].equals("-DEBUG");
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> { e.printStackTrace(); System.exit(1); });
		new Thread(null, new Main(), "", 1 << 31).start();
	}

	public void run() {
		Solver solver = new Solver();
		solver.solve();
		solver.exit();
	}

	static class FastScanner {
		private final InputStream in = System.in;
		private final byte[] buffer = new byte[1024];
		private int pointer = 0;
		private int buflen = 0;
		private boolean hasNextByte() {
			if(pointer < buflen) return true;
			else {
				pointer = 0;
				try {
					buflen = in.read(buffer);
				}catch (IOException e) {
					e.printStackTrace();
				}
				return buflen > 0;
			}
		}
		private int readByte() { if(hasNextByte()) return buffer[pointer ++]; else return -1; }
		private boolean isPrintableChar(int c) { return isPrintableChar(c, false); }
		private boolean isPrintableChar(int c, boolean includingSpace) { return (includingSpace ? 32 : 33) <= c && c <= 126; }
		private void skipUnprintable() { skipUnprintable(false); }
		private void skipUnprintable(boolean includingSpace) { while(hasNextByte() && !isPrintableChar(buffer[pointer], includingSpace)) pointer++; }
		private boolean hasNext() { return hasNext(false); }
		private boolean hasNext(boolean includingSpace) { skipUnprintable(includingSpace); return hasNextByte(); }
		private StringBuilder sb = new StringBuilder();
		public String next() { return next(false); }
		public String next(boolean includingSpace) {
			if(!hasNext(includingSpace)) throw new NoSuchElementException();
			sb.setLength(0);
			int b = readByte();
			while(isPrintableChar(b, includingSpace)) {
				sb.appendCodePoint(b);
				b = readByte();
			}
			return sb.toString();
		}
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			long n = 0;
			boolean minus = false;
			int b = readByte();
			if(b == '-') {
				minus = true;
				b = readByte();
			}
			if(b < '0' || '9' < b) throw new NumberFormatException();
			while(true) {
				if('0' <= b && b <= '9') n = n * 10 + b - '0';
				else if(b == -1 || !isPrintableChar(b)) return minus ? -n : n;
				else throw new NumberFormatException();
				b = readByte();
			}
		}
	}

	static class Solver {
		FastScanner sc = new FastScanner();
		public Solver() { }

		String ns() { return ns(false); }
		String ns(boolean includingSpace) { return sc.next(includingSpace); }
		String[] ns(int n) { return ns(n, false); }
		String[] ns(int n, boolean includingSpace) {
			String a[] = new String[n];
			for(int i = 0; i < n; i ++) a[i] = ns(includingSpace);
			return a;
		}
		String[][] ns(int n, int m) { return ns(n, m, false); }
		String[][] ns(int n, int m, boolean includingSpace) {
			String a[][] = new String[n][m];
			for(int i = 0; i < n; i ++) a[i] = ns(m, includingSpace);
			return a;
		}
		char nc() { return ns().charAt(0); }
		char[] nc(int n) {
			String str = ns();
			if(n < 0) n = str.length();
			char a[] = new char[n];
			for(int i = 0; i < n; i ++) a[i] = str.charAt(i);
			return a;
		}
		char[][] nc(int n, int m) {
			char a[][] = new char[n][m];
			for(int i = 0; i < n; i ++) a[i] = nc(m);
			return a;
		}
		boolean[] nb(int n, char t) {
			char c[] = nc(-1);
			if(n < 0) n = c.length;
			boolean a[] = new boolean[n];
			for(int i = 0; i < n; i ++) a[i] = c[i] == t;
			return a;
		}
		boolean[][] nb(int n, int m, char t) {
			boolean a[][] = new boolean[n][m];
			for(int i = 0; i < n; i ++) a[i] = nb(m, t);
			return a;
		}
		int ni() { return Math.toIntExact(sc.nextLong()); }
		int[] ni(int n) {
			int a[] = new int[n];
			for(int i = 0; i < n; i ++) a[i] = ni();
			return a;
		}
		int[][] ni(int n, int m) {
			int a[][] = new int[n][m];
			for(int i = 0; i < n; i ++) a[i] = ni(m);
			return a;
		}
		long nl() { return sc.nextLong(); }
		long[] nl(int n) {
			long a[] = new long[n];
			for(int i = 0; i < n; i ++) a[i] = nl();
			return a;
		}
		long[][] nl(int n, int m) {
			long a[][] = new long[n][m];
			for(int i = 0; i < n; i ++) a[i] = nl(m);
			return a;
		}
		double nd() { return Double.parseDouble(sc.next()); }
		double[] nd(int n) {
			double a[] = new double[n];
			for(int i = 0; i < n; i ++) a[i] = nd();
			return a;
		}
		double[][] nd(int n, int m) {
			double a[][] = new double[n][m];
			for(int i = 0; i < n; i ++) a[i] = nd(m);
			return a;
		}		PairII npii() { return new PairII(ni(), ni()); }
		PairII[] npii(int n) {
			PairII a[] = new PairII[n];
			for(int i = 0; i < n; i ++) a[i] = npii();
			return a;
		}
		PairII[][] npii(int n, int m) {
			PairII a[][] = new PairII[n][m];
			for(int i = 0; i < n; i ++) a[i] = npii(m);
			return a;
		}
		PairIL npil() { return new PairIL(ni(), nl()); }
		PairIL[] npil(int n) {
			PairIL a[] = new PairIL[n];
			for(int i = 0; i < n; i ++) a[i] = npil();
			return a;
		}
		PairIL[][] npil(int n, int m) {
			PairIL a[][] = new PairIL[n][m];
			for(int i = 0; i < n; i ++) a[i] = npil(m);
			return a;
		}
		PairID npid() { return new PairID(ni(), nd()); }
		PairID[] npid(int n) {
			PairID a[] = new PairID[n];
			for(int i = 0; i < n; i ++) a[i] = npid();
			return a;
		}
		PairID[][] npid(int n, int m) {
			PairID a[][] = new PairID[n][m];
			for(int i = 0; i < n; i ++) a[i] = npid(m);
			return a;
		}
		PairLI npli() { return new PairLI(nl(), ni()); }
		PairLI[] npli(int n) {
			PairLI a[] = new PairLI[n];
			for(int i = 0; i < n; i ++) a[i] = npli();
			return a;
		}
		PairLI[][] npli(int n, int m) {
			PairLI a[][] = new PairLI[n][m];
			for(int i = 0; i < n; i ++) a[i] = npli(m);
			return a;
		}
		PairLL npll() { return new PairLL(nl(), nl()); }
		PairLL[] npll(int n) {
			PairLL a[] = new PairLL[n];
			for(int i = 0; i < n; i ++) a[i] = npll();
			return a;
		}
		PairLL[][] npll(int n, int m) {
			PairLL a[][] = new PairLL[n][m];
			for(int i = 0; i < n; i ++) a[i] = npll(m);
			return a;
		}
		PairLD npld() { return new PairLD(nl(), nd()); }
		PairLD[] npld(int n) {
			PairLD a[] = new PairLD[n];
			for(int i = 0; i < n; i ++) a[i] = npld();
			return a;
		}
		PairLD[][] npld(int n, int m) {
			PairLD a[][] = new PairLD[n][m];
			for(int i = 0; i < n; i ++) a[i] = npld(m);
			return a;
		}
		PairDI npdi() { return new PairDI(nd(), ni()); }
		PairDI[] npdi(int n) {
			PairDI a[] = new PairDI[n];
			for(int i = 0; i < n; i ++) a[i] = npdi();
			return a;
		}
		PairDI[][] npdi(int n, int m) {
			PairDI a[][] = new PairDI[n][m];
			for(int i = 0; i < n; i ++) a[i] = npdi(m);
			return a;
		}
		PairDL npdl() { return new PairDL(nd(), nl()); }
		PairDL[] npdl(int n) {
			PairDL a[] = new PairDL[n];
			for(int i = 0; i < n; i ++) a[i] = npdl();
			return a;
		}
		PairDL[][] npdl(int n, int m) {
			PairDL a[][] = new PairDL[n][m];
			for(int i = 0; i < n; i ++) a[i] = npdl(m);
			return a;
		}
		PairDD npdd() { return new PairDD(nd(), nd()); }
		PairDD[] npdd(int n) {
			PairDD a[] = new PairDD[n];
			for(int i = 0; i < n; i ++) a[i] = npdd();
			return a;
		}
		PairDD[][] npdd(int n, int m) {
			PairDD a[][] = new PairDD[n][m];
			for(int i = 0; i < n; i ++) a[i] = npdd(m);
			return a;
		}

		String booleanToString(boolean b) { return b ? "#" : "."; }

		PrintWriter out = new PrintWriter(System.out);
		PrintWriter err = new PrintWriter(System.err);
		StringBuilder sb4prtln = new StringBuilder();
		void prt() { out.print(""); }
		<T> void prt(T a) { out.print(a); }
		void prtln() { out.println(""); }
		<T> void prtln(T a) { out.println(a); }
		void prtln(int... a) {
			sb4prtln.setLength(0);
			for(int element : a) sb4prtln.append(element+" ");
			prtln(sb4prtln.toString().trim());
		}
		void prtln(long... a) {
			sb4prtln.setLength(0);
			for(long element : a) sb4prtln.append(element+" ");
			prtln(sb4prtln.toString().trim());
		}
		void prtln(double... a) {
			sb4prtln.setLength(0);
			for(double element : a) sb4prtln.append(element+" ");
			prtln(sb4prtln.toString().trim());
		}
		void prtln(String... a) {
			sb4prtln.setLength(0);
			for(String element : a) sb4prtln.append(element+" ");
			prtln(sb4prtln.toString().trim());
		}
		void prtln(char... a) {
			sb4prtln.setLength(0);
			for(char element : a) sb4prtln.append(element);
			prtln(sb4prtln.toString());
		}
		void prtln(boolean... a) {
			sb4prtln.setLength(0);
			for(boolean element : a) sb4prtln.append(booleanToString(element));
			prtln(sb4prtln.toString());
		}
		void prtln(int[][] a) { for(int[] element : a) prtln(element); }
		void prtln(long[][] a) { for(long[] element : a) prtln(element); }
		void prtln(double[][] a) { for(double[] element : a) prtln(element); }
		void prtln(String[][] a) { for(String[] element : a) prtln(element); }
		void prtln(char[][] a) { for(char[] element : a) prtln(element); }
		void prtln(boolean[][] a) { for(boolean[] element : a) prtln(element); }

		String errconvert(int a) { return isINF(a) ? "_" : String.valueOf(a); }
		String errconvert(long a) { return isINF(a) ? "_" : String.valueOf(a); }
		void errprt(int a) { if(DEBUG) err.print(errconvert(a)); }
		void errprt(long a) { if(DEBUG) err.print(errconvert(a)); }
		void errprt() { if(DEBUG) err.print(""); }
		<T> void errprt(T a) { if(DEBUG) err.print(a); }
		void errprt(boolean a) { if(DEBUG) errprt(booleanToString(a)); }
		void errprtln() { if(DEBUG) err.println(""); }
		void errprtln(int a) { if(DEBUG) err.println(errconvert(a)); }
		void errprtln(long a) { if(DEBUG) err.println(errconvert(a)); }
		<T> void errprtln(T a) { if(DEBUG) err.println(a); }
		void errprtln(boolean a) { if(DEBUG) errprtln(booleanToString(a)); }
		void errprtln(int... a) {
			if(DEBUG) {
				sb4prtln.setLength(0);
				for(int element : a) sb4prtln.append(errconvert(element)+" ");
				errprtln(sb4prtln.toString().trim());
			}
		}
		void errprtln(long... a) {
			if(DEBUG) {
				sb4prtln.setLength(0);
				for(long element : a) sb4prtln.append(errconvert(element)+" ");
				errprtln(sb4prtln.toString().trim());
			}
		}
		void errprtln(double... a) {
			if(DEBUG) {
				sb4prtln.setLength(0);
				for(double element : a) sb4prtln.append(element+" ");
				errprtln(sb4prtln.toString().trim());
			}
		}
		void errprtln(String... a) {
			if(DEBUG) {
				sb4prtln.setLength(0);
				for(String element : a) sb4prtln.append(element+" ");
				errprtln(sb4prtln.toString().trim());
			}
		}
		void errprtln(char... a) {
			if(DEBUG) {
				sb4prtln.setLength(0);
				for(char element : a) sb4prtln.append(element);
				errprtln(sb4prtln.toString());
			}
		}
		void errprtln(boolean... a) {
			if(DEBUG) {
				sb4prtln.setLength(0);
				for(boolean element : a) sb4prtln.append(booleanToString(element));
				errprtln(sb4prtln.toString());
			}
		}
		void errprtln(Object[] a) { if(DEBUG) for(Object element : a) errprtln(element); }
		void errprtln(int[][] a) { if(DEBUG) for(int[] element : a) errprtln(element); }
		void errprtln(long[][] a) { if(DEBUG) for(long[] element : a) errprtln(element); }
		void errprtln(double[][] a) { if(DEBUG) for(double[] element : a) errprtln(element); }
		void errprtln(String[][] a) { if(DEBUG) for(String[] element : a) errprtln(element); }
		void errprtln(char[][] a) { if(DEBUG) for(char[] element : a) errprtln(element); }
		void errprtln(boolean[][] a) { if(DEBUG) for(boolean[] element : a) errprtln(element); }
		void errprtln(Object[][] a) { if(DEBUG) for(Object element : a) { errprtln(element); errprtln(); } }

		void reply(boolean b) { prtln(b ? "Yes" : "No"); }
		void REPLY(boolean b) { prtln(b ? "YES" : "NO"); }

		void flush() { out.flush(); if(DEBUG) err.flush(); }
		void assertion(boolean b) { if(!b) { flush(); throw new AssertionError(); } }

		void exit() { flush(); System.exit(0); }
		<T> void exit(T a) { prtln(a); exit(); }
		void exit(int... a) { prtln(a); exit(); }
		void exit(long... a) { prtln(a); exit(); }
		void exit(double... a) { prtln(a); exit(); }
		void exit(String... a) { prtln(a); exit(); }
		void exit(char... a) { prtln(a); exit(); }
		void exit(boolean... a) { prtln(a); exit(); }
		void exit(int[][] a) { prtln(a); exit(); }
		void exit(long[][] a) { prtln(a); exit(); }
		void exit(double[][] a) { prtln(a); exit(); }
		void exit(String[][] a) { prtln(a); exit(); }
		void exit(char[][] a) { prtln(a); exit(); }
		void exit(boolean[][] a) { prtln(a); exit(); }


		final long INF = (long)1e18 + 7;
		boolean isPlusINF(long a) { return a > INF / 10; }
		boolean isMinusINF(long a) { return isPlusINF(- a); }
		boolean isINF(long a) { return isPlusINF(a) || isMinusINF(a); }
		final int I_INF = (int)1e9 + 7;
		boolean isPlusINF(int a) { return a > I_INF / 10; }
		boolean isMinusINF(int a) { return isPlusINF(- a); }
		boolean isINF(int a) { return isPlusINF(a) || isMinusINF(a); }


		int min(int a, int b) { return Math.min(a, b); }
		long min(long a, long b) { return Math.min(a, b); }
		double min(double a, double b) { return Math.min(a, b); }
		<T extends Comparable<T>> T min(T a, T b) { return a.compareTo(b) <= 0 ? a : b; }
		int min(int... x) {
			int min = x[0];
			for(int val : x) min = min(min, val);
			return min;
		}
		long min(long... x) {
			long min = x[0];
			for(long val : x) min = min(min, val);
			return min;
		}
		double min(double... x) {
			double min = x[0];
			for(double val : x) min = min(min, val);
			return min;
		}
		int max(int a, int b) { return Math.max(a, b); }
		long max(long a, long b) { return Math.max(a, b); }
		double max(double a, double b) { return Math.max(a, b); }
		<T extends Comparable<T>> T max(T a, T b) { return a.compareTo(b) >= 0 ? a : b; }
		int max(int... x) {
			int max = x[0];
			for(int val : x) max = max(max, val);
			return max;
		}
		long max(long... x) {
			long max = x[0];
			for(long val : x) max = max(max, val);
			return max;
		}
		double max(double... x) {
			double max = x[0];
			for(double val : x) max = max(max, val);
			return max;
		}
		<T extends Comparable<T>> T max(T[] x) {
			T max = x[0];
			for(T val : x) max = max(max, val);
			return max;
		}
		int max(int[][] a) {
			int max = a[0][0];
			for(int[] ele : a) max = max(max, max(ele));
			return max;
		}
		long max(long[][] a) {
			long max = a[0][0];
			for(long[] ele : a) max = max(max, max(ele));
			return max;
		}
		double max(double[][] a) {
			double max = a[0][0];
			for(double[] ele : a) max = max(max, max(ele));
			return max;
		}
		<T extends Comparable<T>> T max(T[][] a) {
			T max = a[0][0];
			for(T[] ele : a) max = max(max, max(ele));
			return max;
		}
		long sum(int... a) {
			long sum = 0;
			for(int element : a) sum += element;
			return sum;
		}
		long sum(long... a) {
			long sum = 0;
			for(long element : a) sum += element;
			return sum;
		}
		double sum(double... a) {
			double sum = 0;
			for(double element : a) sum += element;
			return sum;
		}
		long sum(boolean... a) {
			long sum = 0;
			for(boolean element : a) sum += element ? 1 : 0;
			return sum;
		}
		long[] sums(int[] a) {
			long sum[] = new long[a.length + 1];
			sum[0] = 0;
			for(int i = 0; i < a.length; i ++) sum[i + 1] = sum[i] + a[i];
			return sum;
		}
		long[] sums(long[] a) {
			long sum[] = new long[a.length + 1];
			sum[0] = 0;
			for(int i = 0; i < a.length; i ++) sum[i + 1] = sum[i] + a[i];
			return sum;
		}
		double[] sums(double[] a) {
			double sum[] = new double[a.length + 1];
			sum[0] = 0;
			for(int i = 0; i < a.length; i ++) sum[i + 1] = sum[i] + a[i];
			return sum;
		}
		long[] sums(boolean[] a) {
			long sum[] = new long[a.length + 1];
			sum[0] = 0;
			for(int i = 0; i < a.length; i ++) sum[i + 1] = sum[i] + (a[i] ? 1 : 0);
			return sum;
		}
		long[][] sums(int[][] a) {
			long sum[][] = new long[a.length + 1][a[0].length + 1];
			fill(sum, 0);
			for(int i = 0; i < a.length; i ++) {
				for(int j = 0; j < a[i].length; j ++) {
					sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + a[i][j];
				}
			}
			return sum;
		}
		long[][] sums(long[][] a) {
			long sum[][] = new long[a.length + 1][a[0].length + 1];
			fill(sum, 0);
			for(int i = 0; i < a.length; i ++) {
				for(int j = 0; j < a[i].length; j ++) {
					sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + a[i][j];
				}
			}
			return sum;
		}
		double[][] sums(double[][] a) {
			double sum[][] = new double[a.length + 1][a[0].length + 1];
			fill(sum, 0);
			for(int i = 0; i < a.length; i ++) {
				for(int j = 0; j < a[i].length; j ++) {
					sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + a[i][j];
				}
			}
			return sum;
		}
		long[][] sums(boolean[][] a) {
			long sum[][] = new long[a.length + 1][a[0].length + 1];
			fill(sum, 0);
			for(int i = 0; i < a.length; i ++) {
				for(int j = 0; j < a[i].length; j ++) {
					sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + (a[i][j] ? 1 : 0);
				}
			}
			return sum;
		}
		int constrain(int x, int l, int r) { return min(max(x, min(l, r)), max(l, r)); }
		long constrain(long x, long l, long r) { return min(max(x, min(l, r)), max(l, r)); }
		double constrain(double x, double l, double r) { return min(max(x, min(l, r)), max(l, r)); }
		int abs(int x) { return x >= 0 ? x : - x; }
		long abs(long x) { return x >= 0 ? x : - x; }
		double abs(double x) { return x >= 0 ? x : - x; }
		int signum(int x) { return x > 0 ? 1 : x < 0 ? -1 : 0; }
		int signum(long x) { return x > 0 ? 1 : x < 0 ? -1 : 0; }
		int signum(double x) { return x > 0 ? 1 : x < 0 ? -1 : 0; }
		long round(double x) { return Math.round(x); }
		long floor(double x) { return (long)Math.floor(x); }
		int divfloor(int a, int b) { return signum(a) == signum(b) ? a / b : - divceil(abs(a), abs(b)); }
		long divfloor(long a, long b) { return signum(a) == signum(b) ? a / b : - divceil(abs(a), abs(b)); }
		long ceil(double x) { return (long)Math.ceil(x); }
		int divceil(int a, int b) { return a >= 0 && b > 0 ? (a + b - 1) / b
											: a < 0 && b < 0 ? divceil(abs(a), abs(b))
											: - divfloor(abs(a), abs(b)); }
		long divceil(long a, long b) { return a >= 0 && b > 0 ? (a + b - 1) / b
											: a < 0 && b < 0 ? divceil(abs(a), abs(b))
											: - divfloor(abs(a), abs(b)); }
		double sqrt(int x) { return Math.sqrt((double)x); }
		double sqrt(long x) { return Math.sqrt((double)x); }
		double sqrt(double x) { return Math.sqrt(x); }
		long fact(int n) {
			long ans = 1;
			for(int i = 1; i <= n; i ++) ans = Math.multiplyExact(ans, i);
			return ans;
		}
		double pow(double x, double y) { return Math.pow(x, y); }
		long pow(long x, long y) {
			long ans = 1;
			while(true) {
				if(y % 2 != 0) ans = Math.multiplyExact(ans, x);
				y /= 2;
				if(y <= 0) return ans;
				x = Math.multiplyExact(x, x);
			}
		}
		int gcd(int a, int b) {
			while(true) {
				if(b == 0) return a;
				int tmp = a;
				a = b;
				b = tmp % b;
			}
		}
		long gcd(long a, long b) {
			while(true) {
				if(b == 0) return a;
				long tmp = a;
				a = b;
				b = tmp % b;
			}
		}
		long lcm(long a, long b) { return a / gcd(a, b) * b; }
		int gcd(int... a) {
			int gcd = 0;
			for(int i = 0; i < a.length; i ++) gcd = gcd(gcd, a[i]);
			return gcd;
		}
		long gcd(long... a) {
			long gcd = 0;
			for(int i = 0; i < a.length; i ++) gcd = gcd(gcd, a[i]);
			return gcd;
		}
		double random() { return Math.random(); }
		int random(int max) { return (int)floor(random() * max); }
		long random(long max) { return floor(random() * max); }
		double random(double max) { return random() * max; }
		int random(int min, int max) { return random(max - min) + min; }
		long random(long min, long max) { return random(max - min) + min; }
		double random(double min, double max) { return random(max - min) + min; }

		boolean isUpper(char a) { return a >= 'A' && a <= 'Z'; }
		boolean isLower(char a) { return a >= 'a' && a <= 'z'; }
		int upperToInt(char a) { return a - 'A'; }
		int lowerToInt(char a) { return a - 'a'; }
		int numToInt(char a) { return a - '0'; }
		int charToInt(char a) { return a >= 'a' ? lowerToInt(a) : a >= 'A' ? upperToInt(a) : numToInt(a); }
		char intToUpper(int a) { return (char)(a + 'A'); }
		char intToLower(int a) { return (char)(a + 'a'); }
		char intToNum(int a) { return (char)(a + '0'); }
		int[] charToInt(char[] a) {
			int array[] = new int[a.length];
			for(int i = 0; i < a.length; i ++) array[i] = charToInt(a[i]);
			return array;
		}

		long[] div(long a) {
			List<Long> divList = new ArrayList<Long>();
			for(long i = 1; i * i <= a; i ++) {
				if(a % i == 0) {
					divList.add(i);
					if(i * i != a) divList.add(a / i);
				}
			}
			long div[] = new long[divList.size()];
			for(int i = 0; i < divList.size(); i ++) div[i] = divList.get(i);
			Arrays.sort(div);
			return div;
		}

		long[][] factor(long a) {
			List<Long> factorList = new ArrayList<Long>();
			List<Long> degreeList = new ArrayList<Long>();
			for(long i = 2; i * i <= a; i ++) {
				if(a % i == 0) {
					long count = 0;
					while(a % i == 0) { a /= i; count ++; }
					factorList.add(i);
					degreeList.add(count);
				}
			}
			if(a > 1) { factorList.add(a); degreeList.add(1L); }
			long factor[][] = new long[factorList.size()][2];
			for(int i = 0; i < factorList.size(); i ++) {
				factor[i][0] = factorList.get(i);
				factor[i][1] = degreeList.get(i);
			}
			Arrays.sort(factor, (sort1, sort2) -> Long.compare(sort1[0], sort2[0]));
			return factor;
		}

		boolean isPrime(long x) {
			boolean ok = x > 1;
			for(long i = 2; i * i <= x; i ++) {
				ok &= x % i != 0;
				if(!ok) return ok;
			}
			return ok;
		}
		boolean[] prime(int num) {
			boolean prime[] = new boolean[num];
			fill(prime, true);
			if(num > 0) prime[0] = false;
			if(num > 1) prime[1] = false;
			for(int i = 2; i < num; i ++) if(prime[i]) for(int j = 2; i * j < num; j ++) prime[i * j] = false;
			return prime;
		}

		long[][] countElements(long[] a, boolean sort) {
			int len = a.length;
			long array[] = new long[len];
			for(int i = 0; i < len; i ++) array[i] = a[i];
			if(sort) Arrays.sort(array);
			List<Long> elem = new ArrayList<Long>();
			List<Long> cnt = new ArrayList<Long>();
			long tmp = 1;
			for(int i = 1; i <= len; i ++) {
				if(i == len || array[i] != array[i - 1]) {
					elem.add(array[i - 1]);
					cnt.add(tmp);
					tmp = 1;
				}else tmp ++;
			}
			long counts[][] = new long[elem.size()][2];
			for(int i = 0; i < elem.size(); i ++) {
				counts[i][0] = elem.get(i);
				counts[i][1] = cnt.get(i);
			}
			return counts;
		}
		long[][] countElements(String str, boolean sort) {
			int len = str.length();
			char array[] = str.toCharArray();
			if(sort) Arrays.sort(array);
			List<Long> elem = new ArrayList<Long>();
			List<Long> cnt = new ArrayList<Long>();
			long tmp = 1;
			for(int i = 1; i <= len; i ++) {
				if(i == len || array[i] != array[i - 1]) {
					elem.add((long)array[i - 1]);
					cnt.add(tmp);
					tmp = 1;
				}else tmp ++;
			}
			long counts[][] = new long[elem.size()][2];
			for(int i = 0; i < elem.size(); i ++) {
				counts[i][0] = elem.get(i);
				counts[i][1] = cnt.get(i);
			}
			return counts;
		}

		int[] baseConvert(long x, int n, int len) {
			int digit[] = new int[len];
			int i = 0;
			long tmp = x;
			while(tmp > 0 && i < len) { digit[i ++] = (int)(tmp % n); tmp /= n; }
			return digit;
		}
		int[] baseConvert(long x, int n) {
			long tmp = x;
			int len = 0;
			while(tmp > 0) { tmp /= n; len ++; }
			return baseConvert(x, n, len);
		}
		int[] baseConvert(int x, int n, int len) {
			int digit[] = new int[len];
			int i = 0;
			int tmp = x;
			while(tmp > 0 && i < len) { digit[i ++] = (int)(tmp % n); tmp /= n; }
			return digit;
		}
		int[] baseConvert(int x, int n) {
			int tmp = x;
			int len = 0;
			while(tmp > 0) { tmp /= n; len ++; }
			return baseConvert(x, n, len);
		}
		long[] baseConvert(long x, long n, int len) {
			long digit[] = new long[len];
			int i = 0;
			long tmp = x;
			while(tmp > 0 && i < len) { digit[i ++] = tmp % n; tmp /= n; }
			return digit;
		}
		long[] baseConvert(long x, long n) {
			long tmp = x;
			int len = 0;
			while(tmp > 0) { tmp /= n; len ++; }
			return baseConvert(x, n, len);
		}

		int numDigits(long a) { return Long.toString(a).length(); }
		long bitFlag(int a) { return 1L << (long)a; }
		boolean isFlagged(long x, int a) { return (x & bitFlag(a)) != 0; }

		long countString(String str, String a) { return (str.length() - str.replace(a, "").length()) / a.length(); }
		long countStringAll(String str, String a) { return str.length() - str.replaceAll(a, "").length(); }


		String reverse(String str) { return (new StringBuilder(str)).reverse().toString(); }
		void reverse(String[] array) { for(int i = 0; i < array.length / 2; i ++) swap(array, i, array.length - i - 1); }
		void reverse(int[] array) { for(int i = 0; i < array.length / 2; i ++) swap(array, i, array.length - i - 1); }
		void reverse(long[] array) { for(int i = 0; i < array.length / 2; i ++) swap(array, i, array.length - i - 1); }
		void reverse(double[] array) { for(int i = 0; i < array.length / 2; i ++) swap(array, i, array.length - i - 1); }
		void reverse(char[] array) { for(int i = 0; i < array.length / 2; i ++) swap(array, i, array.length - i - 1); }
		void reverse(boolean[] array) { for(int i = 0; i < array.length / 2; i ++) swap(array, i, array.length - i - 1); }
		<T> void reverse(T[] array) { for(int i = 0; i < array.length / 2; i ++) swap(array, i, array.length - i - 1); }
		void fill(int[] array, int x) { Arrays.fill(array, x); }
		void fill(long[] array, long x) { Arrays.fill(array, x); }
		void fill(double[] array, double x) { Arrays.fill(array, x); }
		void fill(char[] array, char x) { Arrays.fill(array, x); }
		void fill(boolean[] array, boolean x) { Arrays.fill(array, x); }
		void fill(int[][] array, int x) { for(int[] a : array) fill(a, x); }
		void fill(long[][] array, long x) { for(long[] a : array) fill(a, x); }
		void fill(double[][] array, double x) { for(double[] a : array) fill(a, x); }
		void fill(char[][] array, char x) { for(char[] a : array) fill(a, x); }
		void fill(boolean[][] array, boolean x) { for(boolean[] a : array) fill(a, x); }
		void fill(int[][][] array, int x) { for(int[][] a : array) fill(a, x); }
		void fill(long[][][] array, long x) { for(long[][] a : array) fill(a, x); }
		void fill(double[][][] array, double x) { for(double[][] a : array) fill(a, x); }
		void fill(char[][][] array, char x) { for(char[][] a : array) fill(a, x); }
		void fill(boolean[][][] array, boolean x) { for(boolean[][] a : array) fill(a, x); }

		int[] resize(int[] array, int m, int x) {
			int resized[] = new int[m];
			for(int i = max(0, - x); i < array.length && i + x < m; i ++) resized[i + x] = array[i];
			return resized;
		}
		long[] resize(long[] array, int m, int x) {
			long resized[] = new long[m];
			for(int i = max(0, - x); i < array.length && i + x < m; i ++) resized[i + x] = array[i];
			return resized;
		}
		double[] resize(double[] array, int m, int x) {
			double resized[] = new double[m];
			for(int i = max(0, - x); i < array.length && i + x < m; i ++) resized[i + x] = array[i];
			return resized;
		}
		char[] resize(char[] array, int m, int x) {
			char resized[] = new char[m];
			for(int i = max(0, - x); i < array.length && i + x < m; i ++) resized[i + x] = array[i];
			return resized;
		}
		boolean[] resize(boolean[] array, int m, int x) {
			boolean resized[] = new boolean[m];
			for(int i = max(0, - x); i < array.length && i + x < m; i ++) resized[i + x] = array[i];
			return resized;
		}
		Object[] resize(Object[] array, int m, int x) {
			Object resized[] = new Object[m];
			for(int i = max(0, - x); i < array.length && i + x < m; i ++) resized[i + x] = array[i];
			return resized;
		}

		void shuffleArray(int[] array){
			for(int i = 0; i < array.length; i ++){
				int tmp = array[i];
				int randomPos = random(i, array.length);
				array[i] = array[randomPos];
				array[randomPos] = tmp;
			}
		}
		void shuffleArray(long[] array){
			for(int i = 0; i < array.length; i ++){
				long tmp = array[i];
				int randomPos = random(i, array.length);
				array[i] = array[randomPos];
				array[randomPos] = tmp;
			}
		}
		void shuffleArray(double[] array){
			for(int i = 0; i < array.length; i ++){
				double tmp = array[i];
				int randomPos = random(i, array.length);
				array[i] = array[randomPos];
				array[randomPos] = tmp;
			}
		}
		int[] randomi(int num, int max){
			int array[] = new int[num];
			for(int i = 0; i < num; i ++) array[i] = random(max);
			return array;
		}
		long[] randoml(int num, long max){
			long array[] = new long[num];
			for(int i = 0; i < num; i ++) array[i] = random(max);
			return array;
		}
		double[] randomd(int num, double max){
			double array[] = new double[num];
			for(int i = 0; i < num; i ++) array[i] = random(max);
			return array;
		}
		int[] randomi(int num, int min, int max){
			int array[] = new int[num];
			for(int i = 0; i < num; i ++) array[i] = random(min, max);
			return array;
		}
		long[] randoml(int num, long min, long max){
			long array[] = new long[num];
			for(int i = 0; i < num; i ++) array[i] = random(min, max);
			return array;
		}
		double[] randomd(int num, double min, double max){
			double array[] = new double[num];
			for(int i = 0; i < num; i ++) array[i] = random(min, max);
			return array;
		}

		void swap(String[] array, int i, int j) {
			String tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		void swap(int[] array, int i, int j) {
			int tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		void swap(long[] array, int i, int j) {
			long tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		void swap(double[] array, int i, int j) {
			double tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		void swap(char[] array, int i, int j) {
			char tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		void swap(boolean[] array, int i, int j) {
			boolean tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
		<T> void swap(T[] array, int i, int j) {
			T tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}

		int[] compress(int[] a) {
			int num = a.length;
			Set<Integer> ss = new TreeSet<>();
			for(int i = 0; i < num; i ++) ss.add(a[i]);
			int compressed[] = new int[ss.size()];
			int j = 0;
			for(Integer x : ss) compressed[j ++] = x;
			for(int i = 0; i < num; i ++) a[i] = lowerBound(compressed, a[i]);
			return compressed;
		}
		long[] compress(long[] a) {
			int num = a.length;
			Set<Long> ss = new TreeSet<>();
			for(int i = 0; i < num; i ++) ss.add(a[i]);
			long compressed[] = new long[ss.size()];
			int j = 0;
			for(Long x : ss) compressed[j ++] = x;
			for(int i = 0; i < num; i ++) a[i] = lowerBound(compressed, a[i]);
			return compressed;
		}
		double[] compress(double[] a) {
			int num = a.length;
			Set<Double> ss = new TreeSet<>();
			for(int i = 0; i < num; i ++) ss.add(a[i]);
			double compressed[] = new double[ss.size()];
			int j = 0;
			for(Double x : ss) compressed[j ++] = x;
			for(int i = 0; i < num; i ++) a[i] = lowerBound(compressed, a[i]);
			return compressed;
		}


		int lowerBound(int[] array, int key) {
			return BS(array, key, true, true, true);
		}
		int lowerBound(int[] array, int key, int ng, int ok) {
			return BS(array, key, true, true, true, ng, ok);
		}
		int upperBound(int[] array, int key) {
			return BS(array, key, true, true, false);
		}
		int upperBound(int[] array, int key, int ng, int ok) {
			return BS(array, key, true, true, false, ng, ok);
		}
		int cntBS(int[] array, int key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, true);
		}
		int cntBS(int[] array, int key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, true, ng, ok);
		}
		int BS(int[] array, int key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, false);
		}
		int BS(int[] array, int key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, false, ng, ok);
		}
		int BS(int[] array, int key, boolean ascending, boolean greater, boolean equals, boolean count) {
			int ng = ascending ^ greater ? array.length : -1;
			int ok = ascending ^ greater ? -1 : array.length;
			return BS(array, key, ascending, greater, equals, count, ng, ok);
		}
		int BS(int[] array, int key, boolean ascending, boolean greater, boolean equals, boolean count, int ng, int ok) {
			int index = binarySearch(array, key, greater, equals, ng, ok);
			return count ? (int)abs(ok - index) : index;
		}
		int binarySearch(int[] array, int key, boolean greater, boolean equals, int ng, int ok) {
			while (abs(ok - ng) > 1) {
				int mid = (ok + ng) / 2;
				if(isOKforBinarySearch(array, mid, key, greater, equals)) ok = mid; else ng = mid;
			}
			return ok;
		}
		boolean isOKforBinarySearch(int[] array, int index, int key, boolean greater, boolean equals) {
			return (array[index] > key && greater) || (array[index] < key && !greater) || (array[index] == key && equals);
		}
		int lowerBound(long[] array, long key) {
			return BS(array, key, true, true, true);
		}
		int lowerBound(long[] array, long key, int ng, int ok) {
			return BS(array, key, true, true, true, ng, ok);
		}
		int upperBound(long[] array, long key) {
			return BS(array, key, true, true, false);
		}
		int upperBound(long[] array, long key, int ng, int ok) {
			return BS(array, key, true, true, false, ng, ok);
		}
		int cntBS(long[] array, long key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, true);
		}
		int cntBS(long[] array, long key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, true, ng, ok);
		}
		int BS(long[] array, long key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, false);
		}
		int BS(long[] array, long key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, false, ng, ok);
		}
		int BS(long[] array, long key, boolean ascending, boolean greater, boolean equals, boolean count) {
			int ng = ascending ^ greater ? array.length : -1;
			int ok = ascending ^ greater ? -1 : array.length;
			return BS(array, key, ascending, greater, equals, count, ng, ok);
		}
		int BS(long[] array, long key, boolean ascending, boolean greater, boolean equals, boolean count, int ng, int ok) {
			int index = binarySearch(array, key, greater, equals, ng, ok);
			return count ? (int)abs(ok - index) : index;
		}
		int binarySearch(long[] array, long key, boolean greater, boolean equals, int ng, int ok) {
			while (abs(ok - ng) > 1) {
				int mid = (ok + ng) / 2;
				if(isOKforBinarySearch(array, mid, key, greater, equals)) ok = mid; else ng = mid;
			}
			return ok;
		}
		boolean isOKforBinarySearch(long[] array, int index, long key, boolean greater, boolean equals) {
			return (array[index] > key && greater) || (array[index] < key && !greater) || (array[index] == key && equals);
		}
		int lowerBound(double[] array, double key) {
			return BS(array, key, true, true, true);
		}
		int lowerBound(double[] array, double key, int ng, int ok) {
			return BS(array, key, true, true, true, ng, ok);
		}
		int upperBound(double[] array, double key) {
			return BS(array, key, true, true, false);
		}
		int upperBound(double[] array, double key, int ng, int ok) {
			return BS(array, key, true, true, false, ng, ok);
		}
		int cntBS(double[] array, double key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, true);
		}
		int cntBS(double[] array, double key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, true, ng, ok);
		}
		int BS(double[] array, double key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, false);
		}
		int BS(double[] array, double key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, false, ng, ok);
		}
		int BS(double[] array, double key, boolean ascending, boolean greater, boolean equals, boolean count) {
			int ng = ascending ^ greater ? array.length : -1;
			int ok = ascending ^ greater ? -1 : array.length;
			return BS(array, key, ascending, greater, equals, count, ng, ok);
		}
		int BS(double[] array, double key, boolean ascending, boolean greater, boolean equals, boolean count, int ng, int ok) {
			int index = binarySearch(array, key, greater, equals, ng, ok);
			return count ? (int)abs(ok - index) : index;
		}
		int binarySearch(double[] array, double key, boolean greater, boolean equals, int ng, int ok) {
			while (abs(ok - ng) > 1) {
				int mid = (ok + ng) / 2;
				if(isOKforBinarySearch(array, mid, key, greater, equals)) ok = mid; else ng = mid;
			}
			return ok;
		}
		boolean isOKforBinarySearch(double[] array, int index, double key, boolean greater, boolean equals) {
			return (array[index] > key && greater) || (array[index] < key && !greater) || (array[index] == key && equals);
		}
		<T extends Comparable<? super T>> int lowerBound(T[] array, T key) {
			return BS(array, key, true, true, true);
		}
		<T extends Comparable<? super T>> int lowerBound(T[] array, T key, int ng, int ok) {
			return BS(array, key, true, true, true, ng, ok);
		}
		<T extends Comparable<? super T>> int upperBound(T[] array, T key) {
			return BS(array, key, true, true, false);
		}
		<T extends Comparable<? super T>> int upperBound(T[] array, T key, int ng, int ok) {
			return BS(array, key, true, true, false, ng, ok);
		}
		<T extends Comparable<? super T>> int cntBS(T[] array, T key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, true);
		}
		<T extends Comparable<? super T>> int cntBS(T[] array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, true, ng, ok);
		}
		<T extends Comparable<? super T>> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, false);
		}
		<T extends Comparable<? super T>> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, false, ng, ok);
		}
		<T extends Comparable<? super T>> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals, boolean count) {
			int ng = ascending ^ greater ? array.length : -1;
			int ok = ascending ^ greater ? -1 : array.length;
			return BS(array, key, ascending, greater, equals, count, ng, ok);
		}
		<T extends Comparable<? super T>> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals, boolean count, int ng, int ok) {
			int index = binarySearch(array, key, greater, equals, ng, ok);
			return count ? (int)abs(ok - index) : index;
		}
		<T extends Comparable<? super T>> int binarySearch(T[] array, T key, boolean greater, boolean equals, int ng, int ok) {
			while (abs(ok - ng) > 1) {
				int mid = (ok + ng) / 2;
				if(isOKforBinarySearch(array, mid, key, greater, equals)) ok = mid; else ng = mid;
			}
			return ok;
		}
		<T extends Comparable<? super T>> boolean isOKforBinarySearch(T[] array, int index, T key, boolean greater, boolean equals) {
			int compare = array[index].compareTo(key);
			return (compare > 0 && greater) || (compare < 0 && !greater) || (compare == 0 && equals);
		}
		<T> int lowerBound(T[] array, T key, Comparator<? super T> c) {
			return BS(array, key, true, true, true, c);
		}
		<T> int lowerBound(T[] array, T key, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, true, true, true, ng, ok, c);
		}
		<T> int upperBound(T[] array, T key, Comparator<? super T> c) {
			return BS(array, key, true, true, false, c);
		}
		<T> int upperBound(T[] array, T key, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, true, true, false, ng, ok, c);
		}
		<T> int cntBS(T[] array, T key, boolean ascending, boolean greater, boolean equals, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, true, c);
		}
		<T> int cntBS(T[] array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, true, ng, ok, c);
		}
		<T> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, false, c);
		}
		<T> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, false, ng, ok, c);
		}
		<T> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals, boolean count, Comparator<? super T> c) {
			int ng = ascending ^ greater ? array.length : -1;
			int ok = ascending ^ greater ? -1 : array.length;
			return BS(array, key, ascending, greater, equals, count, ng, ok, c);
		}
		<T> int BS(T[] array, T key, boolean ascending, boolean greater, boolean equals, boolean count, int ng, int ok, Comparator<? super T> c) {
			int index = binarySearch(array, key, greater, equals, ng, ok, c);
			return count ? (int)abs(ok - index) : index;
		}
		<T> int binarySearch(T[] array, T key, boolean greater, boolean equals, int ng, int ok, Comparator<? super T> c) {
			while (abs(ok - ng) > 1) {
				int mid = (ok + ng) / 2;
				if(isOKforBinarySearch(array, mid, key, greater, equals, c)) ok = mid; else ng = mid;
			}
			return ok;
		}
		<T> boolean isOKforBinarySearch(T[] array, int index, T key, boolean greater, boolean equals, Comparator<? super T> c) {
			int compare = c.compare(array[index], key);
			return (compare > 0 && greater) || (compare < 0 && !greater) || (compare == 0 && equals);
		}
		<T extends Comparable<? super T>> int lowerBound(List<T> array, T key) {
			return BS(array, key, true, true, true);
		}
		<T extends Comparable<? super T>> int lowerBound(List<T> array, T key, int ng, int ok) {
			return BS(array, key, true, true, true, ng, ok);
		}
		<T extends Comparable<? super T>> int upperBound(List<T> array, T key) {
			return BS(array, key, true, true, false);
		}
		<T extends Comparable<? super T>> int upperBound(List<T> array, T key, int ng, int ok) {
			return BS(array, key, true, true, false, ng, ok);
		}
		<T extends Comparable<? super T>> int cntBS(List<T> array, T key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, true);
		}
		<T extends Comparable<? super T>> int cntBS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, true, ng, ok);
		}
		<T extends Comparable<? super T>> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals) {
			return BS(array, key, ascending, greater, equals, false);
		}
		<T extends Comparable<? super T>> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok) {
			return BS(array, key, ascending, greater, equals, false, ng, ok);
		}
		<T extends Comparable<? super T>> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, boolean count) {
			int ng = ascending ^ greater ? array.size() : -1;
			int ok = ascending ^ greater ? -1 : array.size();
			return BS(array, key, ascending, greater, equals, count, ng, ok);
		}
		<T extends Comparable<? super T>> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, boolean count, int ng, int ok) {
			int index = binarySearch(array, key, greater, equals, ng, ok);
			return count ? (int)abs(ok - index) : index;
		}
		<T extends Comparable<? super T>> int binarySearch(List<T> array, T key, boolean greater, boolean equals, int ng, int ok) {
			while (abs(ok - ng) > 1) {
				int mid = (ok + ng) / 2;
				if(isOKforBinarySearch(array, mid, key, greater, equals)) ok = mid; else ng = mid;
			}
			return ok;
		}
		<T extends Comparable<? super T>> boolean isOKforBinarySearch(List<T> array, int index, T key, boolean greater, boolean equals) {
			int compare = array.get(index).compareTo(key);
			return (compare > 0 && greater) || (compare < 0 && !greater) || (compare == 0 && equals);
		}
		<T> int lowerBound(List<T> array, T key, Comparator<? super T> c) {
			return BS(array, key, true, true, true, c);
		}
		<T> int lowerBound(List<T> array, T key, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, true, true, true, ng, ok, c);
		}
		<T> int upperBound(List<T> array, T key, Comparator<? super T> c) {
			return BS(array, key, true, true, false, c);
		}
		<T> int upperBound(List<T> array, T key, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, true, true, false, ng, ok, c);
		}
		<T> int cntBS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, true, c);
		}
		<T> int cntBS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, true, ng, ok, c);
		}
		<T> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, false, c);
		}
		<T> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, int ng, int ok, Comparator<? super T> c) {
			return BS(array, key, ascending, greater, equals, false, ng, ok, c);
		}
		<T> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, boolean count, Comparator<? super T> c) {
			int ng = ascending ^ greater ? array.size() : -1;
			int ok = ascending ^ greater ? -1 : array.size();
			return BS(array, key, ascending, greater, equals, count, ng, ok, c);
		}
		<T> int BS(List<T> array, T key, boolean ascending, boolean greater, boolean equals, boolean count, int ng, int ok, Comparator<? super T> c) {
			int index = binarySearch(array, key, greater, equals, ng, ok, c);
			return count ? (int)abs(ok - index) : index;
		}
		<T> int binarySearch(List<T> array, T key, boolean greater, boolean equals, int ng, int ok, Comparator<? super T> c) {
			while (abs(ok - ng) > 1) {
				int mid = (ok + ng) / 2;
				if(isOKforBinarySearch(array, mid, key, greater, equals, c)) ok = mid; else ng = mid;
			}
			return ok;
		}
		<T> boolean isOKforBinarySearch(List<T> array, int index, T key, boolean greater, boolean equals, Comparator<? super T> c) {
			int compare = c.compare(array.get(index), key);
			return (compare > 0 && greater) || (compare < 0 && !greater) || (compare == 0 && equals);
		}

		PairLL binaryRangeSearch(long left, long right, UnaryOperator<Long> op, boolean minimize) {
			long ok1 = right, ng1 = left;
			while(abs(ok1 - ng1) > 1) {
				long mid = (ok1 + ng1) / 2;
				boolean isOK = (op.apply(mid + 1) - op.apply(mid)) * (minimize ? 1 : -1) >= 0;
				if(isOK) ok1 = mid; else ng1 = mid;
			}
			long ok2 = left, ng2 = right;
			while(abs(ok2 - ng2) > 1) {
				long mid = (ok2 + ng2) / 2;
				boolean isOK = (op.apply(mid - 1) - op.apply(mid)) * (minimize ? 1 : -1) >= 0;
				if(isOK) ok2 = mid; else ng2 = mid;
			}
			return new PairLL(ok1, ok2); //[l, r]
		}

		double ternarySearch(double left, double right, UnaryOperator<Double> op, boolean minimize, int loop) {
			for(int cnt = 0; cnt < loop; cnt ++) {
				double m1 = (left * 2 + right) / 3.0;
				double m2 = (left + right * 2) / 3.0;
				if(op.apply(m1) > op.apply(m2) ^ minimize) right = m2; else left = m1;
			}
			return (left + right) / 2.0;
		}


		// mods
		final long MOD = (long)1e9 + 7; // 998244353;
		long mod(long i) { i %= MOD; return i + (i < 0 ? MOD : 0); }
		void mod(long[] a) { for(int i = 0; i < a.length; i ++) a[i] = mod(a[i]); }
		void mod(long[][] a) { for(int i = 0; i < a.length; i ++) mod(a[i]); }
		void mod(long[][][] a) { for(int i = 0; i < a.length; i ++) mod(a[i]); }

		long pow_m(long x, long y) {
			long ans = 1;
			for(; y > 0; y /= 2) {
				if(y % 2 != 0) ans = mod(ans * x);
				x = mod(x * x);
			}
			return ans;
		}
		long[] pows_m(long x, int max) {
			long pow[] = new long[max + 1];
			pow[0] = 1;
			for(int i = 0; i < max; i ++) pow[i + 1] = mod(pow[i] * x);
			return pow;
		}
		long fact_m(int n) {
			long ans = 1;
			for(int i = 1; i <= n; i ++) ans = mod(ans * i);
			return ans;
		}

		final int MAX_INV_SIZE = 100_100;
		Map<Long, Long> invMap = new HashMap<>();
		long inv(long x) {
			x = mod(x);
			if(invMap.containsKey(x)) return invMap.get(x);
			if(invMap.size() >= MAX_INV_SIZE) return calInv(x);
			invMap.put(x, calInv(x));
			return invMap.get(x);
		}
		long calInv(long x) { return pow_m(x, MOD - 2); }

		final int MAX_FACT = 5_000_100;
		long fact[];
		long invFact[];
		boolean isFactPrepared = false;
		Map<Integer, long[]> factMap;
		void prepareFact() {
			fact = new long[MAX_FACT];
			fill(fact, 0);
			invFact = new long[MAX_FACT];
			fill(invFact, 0);
			fact[0] = 1;
			int maxIndex = min(MAX_FACT, (int)MOD);
			for(int i = 1; i < maxIndex; i ++) fact[i] = mod(fact[i - 1] * i);
			invFact[maxIndex - 1] = inv(fact[maxIndex - 1]);
			for(int i = maxIndex - 1; i > 0; i --) invFact[i - 1] = mod(invFact[i] * i);

			factMap = new HashMap<>();
			isFactPrepared = true;
		}

		long P(int n, int r) {
			if(!isFactPrepared) { prepareFact(); }
			if(n < 0 || r < 0 || n < r) { return 0; }
			if(n >= MAX_FACT) {
				if(!factMap.containsKey(n)) {
					long largeFact[] = new long[MAX_FACT];
					factMap.put(n, largeFact);
					fill(largeFact, -INF);
					largeFact[0] = 1;
				}
				long largeFact[] = factMap.get(n);
				int i = r;
				while(isINF(largeFact[i])) i --;
				for(; i < r; i ++) largeFact[i + 1] = mod(largeFact[i] * (n - i));
				return largeFact[r];
			}
			return mod(fact[n] * invFact[n - r]);
		}
		long C(int n, int r) {
			if(!isFactPrepared) prepareFact();
			if(n < 0 || r < 0 || n < r) return 0;
			return mod(P(n, r) * invFact[r]);
		}
		long H(int n, int r) { return C((n - 1) + r, r); }


		// grid
		class Grids {
			int h;
			int w;
			Grid[][] gs;
			Grid[] gi;
			Grids(int h, int w) {
				this.h = h;
				this.w = w;
				gs = new Grid[h][w];
				gi = new Grid[h * w];
				for(int i = 0; i < h; i ++) {
					for(int j = 0; j < w; j ++) {
						gs[i][j] = new Grid(i, j, h, w);
						gi[gs[i][j].i] = gs[i][j];
					}
				}
			}

			void init(boolean[][] b) {
				for(int i = 0; i < h; i ++) for(int j = 0; j < w; j ++) gs[i][j].b = b[i][j];
			}
			void init(long[][] val) {
				for(int i = 0; i < h; i ++) for(int j = 0; j < w; j ++) gs[i][j].val = val[i][j];
			}

			Grid get(int x, int y) { return isValid(x, y, h, w) ? gs[x][y] : null; }
			Grid get(int i) { return get(i / w, i % w); }

			int dx[] = {0, -1, 1, 0, 0, -1, 1, -1, 1};
			int dy[] = {0, 0, 0, -1, 1, -1, -1, 1, 1};
			Grid next(int x, int y, int i) { return next(gs[x][y], i); }
			Grid next(Grid g, int i) {
				return isValid(g.x + dx[i], g.y + dy[i], g.h, g.w)
					? gs[g.x + dx[i]][g.y + dy[i]]
					: null;
			}
		}
		class Grid implements Comparable<Grid> {
			int x;
			int y;
			int h;
			int w;
			int i;
			boolean b;
			long val;

			Grid() {  }
			Grid(int x, int y, int h, int w) { init(x, y, h, w, false, 0); }
			Grid(int x, int y, int h, int w, boolean b) { init(x, y, h, w, b, 0); }
			Grid(int x, int y, int h, int w, long val) { init(x, y, h, w, false, val); }
			Grid(int x, int y, int h, int w, boolean b, long val) { init(x, y, h, w, b, val); }

			void init(int x, int y, int h, int w, boolean b, long val) {
				this.x = x;
				this.y = y;
				this.h = h;
				this.w = w;
				this.b = b;
				this.val = val;
				this.i = x * w + y;
			}

			@Override
			public String toString() { return "("+x+", "+y+")"+" "+booleanToString(b)+" "+val; }
			@Override
			public int hashCode() { return Objects.hash(x, y, h, w, b, val); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				Grid that = (Grid) obj;
				if(this.x != that.x) return false;
				if(this.y != that.y) return false;
				if(this.h != that.h) return false;
				if(this.w != that.w) return false;
				if(this.b != that.b) return false;
				if(this.val != that.val) return false;
				return true;
			}
			@Override
			public int compareTo(Grid that) {
				int c = Long.compare(this.val, that.val);
				if(c == 0) c = Integer.compare(this.x, that.x);
				if(c == 0) c = Integer.compare(this.y, that.y);
				return c;
			}
		}

		boolean isValid(int x, int y, int h, int w) { return x >= 0 && x < h && y >= 0 && y < w; }
		boolean isValid(Grid g) { return isValid(g.x, g.y, g.h, g.w); }

		// graph
		class Graph {
			int numNode;
			int numEdge;
			boolean directed;
			Set<Edge> edges = new HashSet<>();
			Node nodes[];
			Node reversedNodes[];

			Graph(int numNode, int numEdge, boolean directed) {
				this.numNode = numNode;
				this.numEdge = numEdge;
				this.directed = directed;
				nodes = new Node[numNode];
				reversedNodes = new Node[numNode];
				for(int i = 0; i < numNode; i ++) {
					nodes[i] = new Node(i);
					reversedNodes[i] = new Node(i);
				}
			}

			void init(Set<Edge> edges) {
				this.edges = edges;
				for(Edge e : edges) add(e);
			}

			void add(Edge e) {
				edges.add(e);
				nodes[e.source].add(e.target, e.cost);
				if(directed) reversedNodes[e.target].add(e.source, e.cost);
				else nodes[e.target].add(e.source, e.cost);
			}

			void remove(Edge e) {
				edges.remove(e);
				nodes[e.source].remove(e.target, e.cost);
				if(directed) reversedNodes[e.target].remove(e.source, e.cost);
				else nodes[e.target].remove(e.source, e.cost);
			}

			void update(Edge e, long cost) {
				nodes[e.source].update(e.target, cost);
				if(directed) reversedNodes[e.target].update(e.source, cost);
				else nodes[e.target].update(e.source, cost);
				e.cost = cost;
			}
			void update(int source, int target, long cost) { update(new Edge(source, target, cost), cost); }

			void clearNodes() {
				for(Node n : nodes) n.clear();
				for(Node n : reversedNodes) n.clear();
			}
		}

		class Node extends HashSet<Edge> {
			int id;

			Node(int id) { this.id = id; }
			void add(int target, long cost) { add(new Edge(id, target, cost)); }
			void remove(int target, long cost) { remove(new Edge(id, target, cost)); }
			void update(int target, long cost) { remove(target, cost); add(target, cost); }
		}

		class Edge implements Comparable<Edge> {
			int source;
			int target;
			long cost;
			Edge(int source, int target, long cost) {
				this.source = source;
				this.target = target;
				this.cost = cost;
			}


			@Override
			public String toString() { return source+" - "+cost+" -> "+target; }
			@Override
			public int hashCode() { return Objects.hash(source, target); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				Edge that = (Edge) obj;
				if(this.source != that.source) return false;
				if(this.target != that.target) return false;
				return true;
			}
			@Override
			public int compareTo(Edge that) {
				int c = Long.compare(this.cost, that.cost);
				if(c == 0) c = Integer.compare(this.source, that.source);
				if(c == 0) c = Integer.compare(this.target, that.target);
				return c;
			}
		}

		// Pair, Tuple
		class Pair<T extends Comparable<? super T>, U extends Comparable<? super U>> implements Comparable<Pair<T, U>> {
			T a;
			U b;
			Pair() { }
			Pair(T a, U b) {
				this.a = a;
				this.b = b;
			}

			@Override
			public String toString() { return "("+a.toString()+", "+b.toString()+")"; }
			@Override
			public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				Pair that = (Pair) obj;
				if(this.a.getClass() != that.a.getClass()) return false;
				if(this.b.getClass() != that.b.getClass()) return false;
				if(!this.a.equals(that.a)) return false;
				if(!this.b.equals(that.b)) return false;
				return true;
			}
			@Override
			public int compareTo(Pair<T, U> that) {
				int c = (this.a).compareTo(that.a);
				if(c == 0) c = (this.b).compareTo(that.b);
				return c;
			}
		}
		class PairII implements Comparable<PairII> {
			int a; int b;
			PairII() { }
			PairII(int a, int b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairII that = (PairII) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairII that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				return c;
			}
		}
		class PairIL implements Comparable<PairIL> {
			int a; long b;
			PairIL() { }
			PairIL(int a, long b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairIL that = (PairIL) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairIL that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				return c;
			}
		}
		class PairID implements Comparable<PairID> {
			int a; double b;
			PairID() { }
			PairID(int a, double b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairID that = (PairID) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairID that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				return c;
			}
		}
		class PairLI implements Comparable<PairLI> {
			long a; int b;
			PairLI() { }
			PairLI(long a, int b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairLI that = (PairLI) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairLI that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				return c;
			}
		}
		class PairLL implements Comparable<PairLL> {
			long a; long b;
			PairLL() { }
			PairLL(long a, long b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairLL that = (PairLL) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairLL that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				return c;
			}
		}
		class PairLD implements Comparable<PairLD> {
			long a; double b;
			PairLD() { }
			PairLD(long a, double b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairLD that = (PairLD) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairLD that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				return c;
			}
		}
		class PairDI implements Comparable<PairDI> {
			double a; int b;
			PairDI() { }
			PairDI(double a, int b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairDI that = (PairDI) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairDI that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				return c;
			}
		}
		class PairDL implements Comparable<PairDL> {
			double a; long b;
			PairDL() { }
			PairDL(double a, long b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairDL that = (PairDL) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairDL that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				return c;
			}
		}
		class PairDD implements Comparable<PairDD> {
			double a; double b;
			PairDD() { }
			PairDD(double a, double b) { this.a = a; this.b = b; }
			@Override public String toString() { return "("+a+", "+b+")"; }
			@Override public int hashCode() { return Objects.hash(a, b); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				PairDD that = (PairDD) obj;
				if(this.a != that.a || this.b != that.b) return false;
				return true;
			}
			@Override
			public int compareTo(PairDD that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				return c;
			}
		}

		interface ITuple {
			public StringBuilder toStringBuilder();
			@Override
			public String toString();
			@Override
			public int hashCode();
			@Override
			public boolean equals(Object obj);
		}
		class BasicTuple<T extends ITuple & Comparable<? super T>, V extends Comparable<? super V>> implements Comparable<BasicTuple> {
			T t;
			V a;
			BasicTuple() {  }

			StringBuilder sbTuple = new StringBuilder();
			public StringBuilder toStringBuilder() {
				sbTuple.setLength(0);
				return sbTuple.append(t.toStringBuilder()).append(", ").append(a);
			}
			@Override
			public String toString() { return "("+toStringBuilder().toString()+")"; }
			@Override
			public int hashCode() { return Objects.hash(t, a); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				BasicTuple that = (BasicTuple) obj;
				if(this.t.getClass() != that.t.getClass()) return false;
				if(this.a.getClass() != that.a.getClass()) return false;
				if(!this.t.equals(that.t)) return false;
				if(!this.a.equals(that.a)) return false;
				return true;
			}
			@Override
			@SuppressWarnings("unchecked")
			public int compareTo(BasicTuple that) {
				int c = (this.t).compareTo((T) (Object) that.t);
				if(c == 0) c = (this.a).compareTo((V) (Object) that.a);
				return c;
			}
		}
		class UniqueTuple<V extends Comparable<? super V>> implements ITuple, Comparable<UniqueTuple> {
			V a;
			UniqueTuple() {  }

			StringBuilder sbTuple = new StringBuilder();
			public StringBuilder toStringBuilder() {
				sbTuple.setLength(0);
				return sbTuple.append(a);
			}
			@Override
			public String toString() { return "("+toStringBuilder().toString()+")"; }
			@Override
			public int hashCode() { return Objects.hash(a); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null) return false;
				if(this.getClass() != obj.getClass()) return false;
				UniqueTuple that = (UniqueTuple) obj;
				if(this.a.getClass() != that.a.getClass()) return false;
				if(!this.a.equals(that.a)) return false;
				return true;
			}
			@Override
			@SuppressWarnings("unchecked")
			public int compareTo(UniqueTuple that) {
				return (this.a).compareTo((V) (Object) that.a);
			}
		}

		class Tuple1<T0 extends Comparable<? super T0>> extends UniqueTuple<T0> implements ITuple {
			Tuple1() { super(); }
			Tuple1(T0 a0) {
				super();
				this.a = a0;
			}
			T0 get0() { return a; }
			void set0(T0 x) { a = x; }
		}
		class Tuple2<
				T0 extends Comparable<? super T0>,
				T1 extends Comparable<? super T1>>
				extends BasicTuple<Tuple1<T0>, T1> implements ITuple {
			Tuple2() { super(); }
			@SuppressWarnings("unchecked")
			Tuple2(T0 a0, T1 a1) {
				super();
				this.t = new Tuple1(a0);
				this.a = a1;
			}
			T0 get0() { return t.get0(); }
			T1 get1() { return a; }
			void set0(T0 x) { t.set0(x); }
			void set1(T1 x) { a = x; }
		}
		class Tuple3<
				T0 extends Comparable<? super T0>,
				T1 extends Comparable<? super T1>,
				T2 extends Comparable<? super T2>>
				extends BasicTuple<Tuple2<T0, T1>, T2> implements ITuple {
			Tuple3() { super(); }
			@SuppressWarnings("unchecked")
			Tuple3(T0 a0, T1 a1, T2 a2) {
				super();
				this.t = new Tuple2(a0, a1);
				this.a = a2;
			}
			T0 get0() { return t.get0(); }
			T1 get1() { return t.get1(); }
			T2 get2() { return a; }
			void set0(T0 x) { t.set0(x); }
			void set1(T1 x) { t.set1(x); }
			void set2(T2 x) { a = x; }
		}
		class Tuple4<
				T0 extends Comparable<? super T0>,
				T1 extends Comparable<? super T1>,
				T2 extends Comparable<? super T2>,
				T3 extends Comparable<? super T3>>
				extends BasicTuple<Tuple3<T0, T1, T2>, T3> implements ITuple {
			Tuple4() { super(); }
			@SuppressWarnings("unchecked")
			Tuple4(T0 a0, T1 a1, T2 a2, T3 a3) {
				super();
				this.t = new Tuple3(a0, a1, a2);
				this.a = a3;
			}
			T0 get0() { return t.get0(); }
			T1 get1() { return t.get1(); }
			T2 get2() { return t.get2(); }
			T3 get3() { return a; }
			void set0(T0 x) { t.set0(x); }
			void set1(T1 x) { t.set1(x); }
			void set2(T2 x) { t.set2(x); }
			void set3(T3 x) { a = x; }
		}
		class Tuple5<
				T0 extends Comparable<? super T0>,
				T1 extends Comparable<? super T1>,
				T2 extends Comparable<? super T2>,
				T3 extends Comparable<? super T3>,
				T4 extends Comparable<? super T4>>
				extends BasicTuple<Tuple4<T0, T1, T2, T3>, T4> implements ITuple {
			Tuple5() { super(); }
			@SuppressWarnings("unchecked")
			Tuple5(T0 a0, T1 a1, T2 a2, T3 a3, T4 a4) {
				super();
				this.t = new Tuple4(a0, a1, a2, a3);
				this.a = a4;
			}
			T0 get0() { return t.get0(); }
			T1 get1() { return t.get1(); }
			T2 get2() { return t.get2(); }
			T3 get3() { return t.get3(); }
			T4 get4() { return a; }
			void set0(T0 x) { t.set0(x); }
			void set1(T1 x) { t.set1(x); }
			void set2(T2 x) { t.set2(x); }
			void set3(T3 x) { t.set3(x); }
			void set4(T4 x) { a = x; }
		}
		class Tuple6<
				T0 extends Comparable<? super T0>,
				T1 extends Comparable<? super T1>,
				T2 extends Comparable<? super T2>,
				T3 extends Comparable<? super T3>,
				T4 extends Comparable<? super T4>,
				T5 extends Comparable<? super T5>>
				extends BasicTuple<Tuple5<T0, T1, T2, T3, T4>, T5> implements ITuple {
			Tuple6() { super(); }
			@SuppressWarnings("unchecked")
			Tuple6(T0 a0, T1 a1, T2 a2, T3 a3, T4 a4, T5 a5) {
				super();
				this.t = new Tuple5(a0, a1, a2, a3, a4);
				this.a = a5;
			}
			T0 get0() { return t.get0(); }
			T1 get1() { return t.get1(); }
			T2 get2() { return t.get2(); }
			T3 get3() { return t.get3(); }
			T4 get4() { return t.get4(); }
			T5 get5() { return a; }
			void set0(T0 x) { t.set0(x); }
			void set1(T1 x) { t.set1(x); }
			void set2(T2 x) { t.set2(x); }
			void set3(T3 x) { t.set3(x); }
			void set4(T4 x) { t.set4(x); }
			void set5(T5 x) { a = x; }
		}
		class Tuple7<
				T0 extends Comparable<? super T0>,
				T1 extends Comparable<? super T1>,
				T2 extends Comparable<? super T2>,
				T3 extends Comparable<? super T3>,
				T4 extends Comparable<? super T4>,
				T5 extends Comparable<? super T5>,
				T6 extends Comparable<? super T6>>
				extends BasicTuple<Tuple6<T0, T1, T2, T3, T4, T5>, T6> implements ITuple {
			Tuple7() { super(); }
			@SuppressWarnings("unchecked")
			Tuple7(T0 a0, T1 a1, T2 a2, T3 a3, T4 a4, T5 a5, T6 a6) {
				super();
				this.t = new Tuple6(a0, a1, a2, a3, a4, a5);
				this.a = a6;
			}
			T0 get0() { return t.get0(); }
			T1 get1() { return t.get1(); }
			T2 get2() { return t.get2(); }
			T3 get3() { return t.get3(); }
			T4 get4() { return t.get4(); }
			T5 get5() { return t.get5(); }
			T6 get6() { return a; }
			void set0(T0 x) { t.set0(x); }
			void set1(T1 x) { t.set1(x); }
			void set2(T2 x) { t.set2(x); }
			void set3(T3 x) { t.set3(x); }
			void set4(T4 x) { t.set4(x); }
			void set5(T5 x) { t.set5(x); }
			void set6(T6 x) { a = x; }
		}
		class Tuple8<
				T0 extends Comparable<? super T0>,
				T1 extends Comparable<? super T1>,
				T2 extends Comparable<? super T2>,
				T3 extends Comparable<? super T3>,
				T4 extends Comparable<? super T4>,
				T5 extends Comparable<? super T5>,
				T6 extends Comparable<? super T6>,
				T7 extends Comparable<? super T7>>
				extends BasicTuple<Tuple7<T0, T1, T2, T3, T4, T5, T6>, T7> implements ITuple {
			Tuple8() { super(); }
			@SuppressWarnings("unchecked")
			Tuple8(T0 a0, T1 a1, T2 a2, T3 a3, T4 a4, T5 a5, T6 a6, T7 a7) {
				super();
				this.t = new Tuple7(a0, a1, a2, a3, a4, a5, a6);
				this.a = a7;
			}
			T0 get0() { return t.get0(); }
			T1 get1() { return t.get1(); }
			T2 get2() { return t.get2(); }
			T3 get3() { return t.get3(); }
			T4 get4() { return t.get4(); }
			T5 get5() { return t.get5(); }
			T6 get6() { return t.get6(); }
			T7 get7() { return a; }
			void set0(T0 x) { t.set0(x); }
			void set1(T1 x) { t.set1(x); }
			void set2(T2 x) { t.set2(x); }
			void set3(T3 x) { t.set3(x); }
			void set4(T4 x) { t.set4(x); }
			void set5(T5 x) { t.set5(x); }
			void set6(T6 x) { t.set6(x); }
			void set7(T7 x) { a = x; }
		}

		class TupleIII implements Comparable<TupleIII> {
			int a; int b; int c;
			TupleIII() {  }
			TupleIII(int a, int b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleIII that = (TupleIII) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleIII that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleIIL implements Comparable<TupleIIL> {
			int a; int b; long c;
			TupleIIL() {  }
			TupleIIL(int a, int b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleIIL that = (TupleIIL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleIIL that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleIID implements Comparable<TupleIID> {
			int a; int b; double c;
			TupleIID() {  }
			TupleIID(int a, int b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleIID that = (TupleIID) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleIID that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleILI implements Comparable<TupleILI> {
			int a; long b; int c;
			TupleILI() {  }
			TupleILI(int a, long b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleILI that = (TupleILI) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleILI that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleILL implements Comparable<TupleILL> {
			int a; long b; long c;
			TupleILL() {  }
			TupleILL(int a, long b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleILL that = (TupleILL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleILL that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleILD implements Comparable<TupleILD> {
			int a; long b; double c;
			TupleILD() {  }
			TupleILD(int a, long b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleILD that = (TupleILD) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleILD that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleIDI implements Comparable<TupleIDI> {
			int a; double b; int c;
			TupleIDI() {  }
			TupleIDI(int a, double b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleIDI that = (TupleIDI) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleIDI that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleIDL implements Comparable<TupleIDL> {
			int a; double b; long c;
			TupleIDL() {  }
			TupleIDL(int a, double b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleIDL that = (TupleIDL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleIDL that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleIDD implements Comparable<TupleIDD> {
			int a; double b; double c;
			TupleIDD() {  }
			TupleIDD(int a, double b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleIDD that = (TupleIDD) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleIDD that) {
				int c = Integer.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLII implements Comparable<TupleLII> {
			long a; int b; int c;
			TupleLII() {  }
			TupleLII(long a, int b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLII that = (TupleLII) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLII that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLIL implements Comparable<TupleLIL> {
			long a; int b; long c;
			TupleLIL() {  }
			TupleLIL(long a, int b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLIL that = (TupleLIL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLIL that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLID implements Comparable<TupleLID> {
			long a; int b; double c;
			TupleLID() {  }
			TupleLID(long a, int b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLID that = (TupleLID) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLID that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLLI implements Comparable<TupleLLI> {
			long a; long b; int c;
			TupleLLI() {  }
			TupleLLI(long a, long b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLLI that = (TupleLLI) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLLI that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLLL implements Comparable<TupleLLL> {
			long a; long b; long c;
			TupleLLL() {  }
			TupleLLL(long a, long b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLLL that = (TupleLLL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLLL that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLLD implements Comparable<TupleLLD> {
			long a; long b; double c;
			TupleLLD() {  }
			TupleLLD(long a, long b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLLD that = (TupleLLD) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLLD that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLDI implements Comparable<TupleLDI> {
			long a; double b; int c;
			TupleLDI() {  }
			TupleLDI(long a, double b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLDI that = (TupleLDI) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLDI that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLDL implements Comparable<TupleLDL> {
			long a; double b; long c;
			TupleLDL() {  }
			TupleLDL(long a, double b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLDL that = (TupleLDL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLDL that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleLDD implements Comparable<TupleLDD> {
			long a; double b; double c;
			TupleLDD() {  }
			TupleLDD(long a, double b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleLDD that = (TupleLDD) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleLDD that) {
				int c = Long.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDII implements Comparable<TupleDII> {
			double a; int b; int c;
			TupleDII() {  }
			TupleDII(double a, int b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDII that = (TupleDII) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDII that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDIL implements Comparable<TupleDIL> {
			double a; int b; long c;
			TupleDIL() {  }
			TupleDIL(double a, int b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDIL that = (TupleDIL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDIL that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDID implements Comparable<TupleDID> {
			double a; int b; double c;
			TupleDID() {  }
			TupleDID(double a, int b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDID that = (TupleDID) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDID that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Integer.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDLI implements Comparable<TupleDLI> {
			double a; long b; int c;
			TupleDLI() {  }
			TupleDLI(double a, long b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDLI that = (TupleDLI) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDLI that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDLL implements Comparable<TupleDLL> {
			double a; long b; long c;
			TupleDLL() {  }
			TupleDLL(double a, long b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDLL that = (TupleDLL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDLL that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDLD implements Comparable<TupleDLD> {
			double a; long b; double c;
			TupleDLD() {  }
			TupleDLD(double a, long b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDLD that = (TupleDLD) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDLD that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Long.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDDI implements Comparable<TupleDDI> {
			double a; double b; int c;
			TupleDDI() {  }
			TupleDDI(double a, double b, int c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDDI that = (TupleDDI) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDDI that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Integer.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDDL implements Comparable<TupleDDL> {
			double a; double b; long c;
			TupleDDL() {  }
			TupleDDL(double a, double b, long c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDDL that = (TupleDDL) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDDL that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Long.compare(this.c, that.c);
				return c;
			}
		}
		class TupleDDD implements Comparable<TupleDDD> {
			double a; double b; double c;
			TupleDDD() {  }
			TupleDDD(double a, double b, double c) { this.a = a; this.b = b; this.c = c; }
			@Override public String toString() { return "("+a+", "+b+", "+c+")"; }
			@Override public int hashCode() { return Objects.hash(a, b, c); }
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(obj == null || this.getClass() != obj.getClass()) return false;
				TupleDDD that = (TupleDDD) obj;
				if(this.a != that.a || this.b != that.b || this.c != that.c) return false;
				return true;
			}
			@Override
			public int compareTo(TupleDDD that) {
				int c = Double.compare(this.a, that.a);
				if(c == 0) c = Double.compare(this.b, that.b);
				if(c == 0) c = Double.compare(this.c, that.c);
				return c;
			}
		}

public void solve() {
	int num = ni();
	int d[] = ni(num);
	int cnt[] = new int[13];
	cnt[0] ++;
	for(int i = 0; i < num; i ++) {
		cnt[d[i]] ++;
	}
	if(max(cnt) >= 3 || cnt[0] >= 2 || cnt[12] >= 2) exit(0);
	long max = 0;
	int maxBit = (int)bitFlag(11);
	for(int bit = 0; bit < maxBit; bit ++) {
		boolean b[] = new boolean[25];
		b[0] = true;
		b[24] = true;
		if(cnt[12] == 1) b[12] = true;
		for(int i = 0; i < 11; i ++) {
			if(cnt[i + 1] >= 2) {
				b[i + 1] = true;
				b[24 - i - 1] = true;
			}else if(cnt[i + 1] == 1) {
				b[i + 1] = isFlagged(bit, i);
				b[24 - i - 1] = !isFlagged(bit, i);
			}
		}
		long tmp = INF;
		int prv = 0;
		for(int i = 1; i < 25; i ++) {
			if(b[i]) {
				tmp = min(tmp, i - prv);
				prv = i;
			}
		}
		max = max(max, tmp);
	}
	prtln(max);
}


	}
}

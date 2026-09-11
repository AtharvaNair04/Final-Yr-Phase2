import java.io.*;
import java.math.*;
import java.util.*;

import static java.util.Arrays.*;

public class Main {
	private static final int mod = (int)1e9+7;

	final Random random = new Random(0);
	final IOFast io = new IOFast();

	/// MAIN CODE
	public void run() throws IOException {
		int TEST_CASE = Integer.parseInt(new String(io.nextLine()).trim());
//		int TEST_CASE = 1;
		while(TEST_CASE-- != 0) {
			cs = io.next();
			tokenize();
			root = new Tree(new ArrayDeque<String>(token));
//			root.dumpT();
			int ans = root.eval(cs.length).min;
			int low = 0, high = cs.length;
			while(high - low > 1) {
				int mid = (low + high) / 2;
				ParseResult r = root.eval(mid);
				if(r.min != ans || r.max != ans) {
					low = mid;
				} else {
					high = mid;
				}
			}
			io.out.println(ans + " " + high);
		}
	}
	
	char[] cs;
	List<String> token = new ArrayList<>();
	void tokenize() {
		token.clear();
		for(int i = 0; i < cs.length; ) {
			if("^_(),?".indexOf(cs[i]) >= 0) {
				token.add(new String(cs, i++, 1));
			} else {
				String s = "";
				for(int j = 0; j < 2; j++) {
					if(!Character.isDigit(cs[i])) {
						break;
					}
					s += cs[i++];
				}
				token.add(s);
			}
		}
	}
	
	
	static class Tree {
		Tree l, r;
		String ope;
		String val;
		boolean isNum;
		int len;
		
		void dumpT() {
			dump(isNum, ope, val, len);
			if(!isNum) {
				l.dumpT();
				r.dumpT();
			}
		}
		
		int val() { return Integer.parseInt(val); }
		
		public Tree(Queue<String> token) {
			String s = token.poll();
			if("^_(),?".indexOf(s) >= 0) {
				ope = s;
				token.poll();
				l = new Tree(token);
				token.poll();
				r = new Tree(token);
				token.poll();
				len = 1 + 1 + l.len + 1 + r.len + 1;
				isNum = false;
			} else {
				val = s;
				len = val.length();
				isNum = true;
			}
		}
		
		boolean max() {
			return ope == null || ope.equals("^");
		}
		
		ParseResult eval(int len) {
			if(isNum) {
				if(len <= 0) return new ParseResult(0, 99);
				if(val() == 0) return new ParseResult(0, 0);
				if(len == 1) return new ParseResult(val.charAt(0) - '0', (val.charAt(0) - '0') * 10 + 9);
				return new ParseResult(val(), val());
			}
			len -= 2;
			ParseResult resL = l.eval(len);
			len -= l.len + 1;
			ParseResult resR = r.eval(len);
			len -= r.len + 1;
			if(ope.equals("^")) {
				// max
				return new ParseResult(Math.max(resL.min, resR.min), Math.max(resL.max, resR.max));
			} else {
				return new ParseResult(Math.min(resL.min, resR.min), Math.min(resL.max, resR.max));
			}
		}
	}
	static
	class ParseResult {
		int min;
		int max;
		public ParseResult(int min, int max) {
			this.min = min;
			this.max = max;
		}
//		@Override
//		public String toString() {
//			// TODO Auto-generated method stub
//			return String.format("ParseResult: %d %d %d", value, len, len2);
//		}
	}
	
	Tree root;

	/// TEMPLATE
	static int gcd(int n, int r) { return r == 0 ? n : gcd(r, n%r); }
	static long gcd(long n, long r) { return r == 0 ? n : gcd(r, n%r); }
	
	static <T> void swap(T[] x, int i, int j) { T t = x[i]; x[i] = x[j]; x[j] = t; }
	static void swap(int[] x, int i, int j) { int t = x[i]; x[i] = x[j]; x[j] = t; }

	void printArrayLn(int[] xs) { for(int i = 0; i < xs.length; i++) io.out.print(xs[i] + (i==xs.length-1?"\n":" ")); }
	void printArrayLn(long[] xs) { for(int i = 0; i < xs.length; i++) io.out.print(xs[i] + (i==xs.length-1?"\n":" ")); }
	
	static void dump(Object... o) { System.err.println(Arrays.deepToString(o)); } 
	
	void main() throws IOException {
		//		IOFast.setFileIO("rle-size.in", "rle-size.out");
		try { run(); }
		catch (EndOfFileRuntimeException e) { }
		io.out.flush();
	}
	public static void main(String[] args) throws IOException { new Main().main(); }
	
	static class EndOfFileRuntimeException extends RuntimeException {
		private static final long serialVersionUID = -8565341110209207657L; }

	static
	public class IOFast {
		private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		private PrintWriter out = new PrintWriter(System.out);

		void setFileIn(String ins) throws IOException { in.close(); in = new BufferedReader(new FileReader(ins)); }
		void setFileOut(String outs) throws IOException { out.flush(); out.close(); out = new PrintWriter(new FileWriter(outs)); }
		void setFileIO(String ins, String outs) throws IOException { setFileIn(ins); setFileOut(outs); }

		private static int pos, readLen;
		private static final char[] buffer = new char[1024 * 8];
		private static char[] str = new char[500*8*2];
		private static boolean[] isDigit = new boolean[256];
		private static boolean[] isSpace = new boolean[256];
		private static boolean[] isLineSep = new boolean[256];

		static { for(int i = 0; i < 10; i++) { isDigit['0' + i] = true; } isDigit['-'] = true; isSpace[' '] = isSpace['\r'] = isSpace['\n'] = isSpace['\t'] = true; isLineSep['\r'] = isLineSep['\n'] = true; }
		public int read() throws IOException { if(pos >= readLen) { pos = 0; readLen = in.read(buffer); if(readLen <= 0) { throw new EndOfFileRuntimeException(); } } return buffer[pos++]; }
		public int nextInt() throws IOException { int len = 0; str[len++] = nextChar(); len = reads(len, isSpace); int i = 0; int ret = 0; if(str[0] == '-') { i = 1; } for(; i < len; i++) ret = ret * 10 + str[i] - '0'; if(str[0] == '-') { ret = -ret; } return ret; }
		public long nextLong() throws IOException { int len = 0; str[len++] = nextChar(); len = reads(len, isSpace); int i = 0; long ret = 0; if(str[0] == '-') { i = 1; } for(; i < len; i++) ret = ret * 10 + str[i] - '0'; if(str[0] == '-') { ret = -ret; } return ret; }
		public char nextChar() throws IOException { while(true) { final int c = read(); if(!isSpace[c]) { return (char)c; } } }
		int reads(int len, boolean[] accept) throws IOException { try { while(true) { final int c = read(); if(accept[c]) { break; } if(str.length == len) { char[] rep = new char[str.length * 3 / 2]; System.arraycopy(str, 0, rep, 0, str.length); str = rep; } str[len++] = (char)c; } } catch(EndOfFileRuntimeException e) { ; } return len; }
		int reads(char[] cs, int len, boolean[] accept) throws IOException { try { while(true) { final int c = read(); if(accept[c]) { break; } cs[len++] = (char)c; } } catch(EndOfFileRuntimeException e) { ; } return len; }
		public char[] nextLine() throws IOException { int len = 0; str[len++] = nextChar(); len = reads(len, isLineSep); try { if(str[len-1] == '\r') { len--; read(); } } catch(EndOfFileRuntimeException e) { ; } return Arrays.copyOf(str, len); }
		public String nextString() throws IOException { return new String(next()); }
		public char[] next() throws IOException { int len = 0; str[len++] = nextChar(); len = reads(len, isSpace); return Arrays.copyOf(str, len); }
//		public int next(char[] cs) throws IOException { int len = 0; cs[len++] = nextChar(); len = reads(cs, len, isSpace); return len; }
		public double nextDouble() throws IOException { return Double.parseDouble(nextString()); }
		public long[] nextLongArray(final int n) throws IOException { final long[] res = new long[n]; for(int i = 0; i < n; i++) { res[i] = nextLong(); } return res; }
		public int[] nextIntArray(final int n) throws IOException { final int[] res = new int[n]; for(int i = 0; i < n; i++) { res[i] = nextInt(); } return res; }
		public int[][] nextIntArray2D(final int n, final int k) throws IOException { final int[][] res = new int[n][]; for(int i = 0; i < n; i++) { res[i] = nextIntArray(k); } return res; }
		public int[][] nextIntArray2DWithIndex(final int n, final int k) throws IOException { final int[][] res = new int[n][k+1]; for(int i = 0; i < n; i++) { for(int j = 0; j < k; j++) { res[i][j] = nextInt(); } res[i][k] = i; } return res; }
		public double[] nextDoubleArray(final int n) throws IOException { final double[] res = new double[n]; for(int i = 0; i < n; i++) { res[i] = nextDouble(); } return res; }
	}
}

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;

public class Main {
	static InputStream is;
	static PrintWriter out;
	// 01
	// 010
	
	static String INPUT = "";
	
	static void solve()
	{
		int R = ni(), C = ni();
		int n = ni();
		int[][] co = new int[n][];
		for(int i = 0;i < n;i++){
			co[i] = new int[]{ni(), ni(), ni()};
		}
		long[] minc = new long[100001];
		long[] minr = new long[100001];
		DJSet dsc = new DJSet(100001);
		DJSet dsr = new DJSet(100001);
		try{
			{
				Arrays.sort(co, new Comparator<int[]>() {
					public int compare(int[] a, int[] b) {
						return a[0] - b[0];
					}
				});
				Arrays.fill(minc, Long.MAX_VALUE / 4);
				for(int i = 0;i < n;){
					int j = i;
					while(j < n && co[i][0] == co[j][0])j++;
					for(int k = i+1;k < j;k++){
						dsc.union(co[i][1], co[k][1], co[i][2]-co[k][2]);
					}
					i = j;
				}
				for(int i = 0;i <= 100000;i++){
					minc[dsc.root(i)] = Math.min(minc[dsc.root(i)], dsc.delta[i]);
				}
			}
			{
				Arrays.sort(co, new Comparator<int[]>() {
					public int compare(int[] a, int[] b) {
						return a[1] - b[1];
					}
				});
				Arrays.fill(minr, Long.MAX_VALUE / 4);
				for(int i = 0;i < n;){
					int j = i;
					while(j < n && co[i][1] == co[j][1])j++;
					for(int k = i+1;k < j;k++){
						dsr.union(co[i][0], co[k][0], co[i][2]-co[k][2]);
					}
					i = j;
				}
				for(int i = 0;i <= 100000;i++){
					minr[dsr.root(i)] = Math.min(minr[dsr.root(i)], dsr.delta[i]);
				}
			}
//			tr(dsr.delta);
//			tr(dsr.root(1));
//			tr(Arrays.copyOf(dsr.delta, 10));
//			tr(Arrays.copyOf(minr, 10));
//			tr(minr);
			for(int i = 0;i < n;i++){
				int rr = dsr.root(co[i][0]);
				int rc = dsc.root(co[i][1]);
				long val = (long)co[i][2] + minr[rr] - dsr.delta[co[i][0]] + minc[rc] - dsc.delta[co[i][1]];
				if(val < 0){
					out.println("No");
					return;
				}
			}
			out.println("Yes");
		}catch(IllegalStateException e){
			out.println("No");
			return;
		}
	}
	
	public static class DJSet {
		public int[] upper;
		public long[] delta;

		public DJSet(int n) {
			upper = new int[n];
			delta = new long[n];
			Arrays.fill(upper, -1);
		}

		public int root(int x) {
			if(upper[x] < 0)return x;
			int newr = root(upper[x]);
			delta[x] += delta[upper[x]];
			upper[x] = newr;
			return newr;
		}

		public boolean equiv(int x, int y) {
			return root(x) == root(y);
		}

		public boolean union(int x, int y, long xmy) {
			int rx = root(x);
			// v[x]-v[y]
			// v[rx]-v[ry]
			xmy += -delta[x];
			x = rx;
			int ry = root(y);
			xmy += delta[y];
			y = ry;
			if (x != y) {
				if (upper[y] < upper[x]) {
					int d = x;
					x = y;
					y = d;
					xmy = -xmy;
				}
				upper[x] += upper[y];
				delta[y] = -xmy;
				upper[y] = x;
			}else{
				if(xmy != 0)throw new IllegalStateException();
			}
			return x == y;
		}

		public int count() {
			int ct = 0;
			for (int u : upper)
				if (u < 0)
					ct++;
			return ct;
		}
	}

	
	public static void main(String[] args) throws Exception
	{
		long S = System.currentTimeMillis();
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		
		solve();
		out.flush();
		long G = System.currentTimeMillis();
		tr(G-S+"ms");
	}
	
	private static boolean eof()
	{
		if(lenbuf == -1)return true;
		int lptr = ptrbuf;
		while(lptr < lenbuf)if(!isSpaceChar(inbuf[lptr++]))return false;
		
		try {
			is.mark(1000);
			while(true){
				int b = is.read();
				if(b == -1){
					is.reset();
					return true;
				}else if(!isSpaceChar(b)){
					is.reset();
					return false;
				}
			}
		} catch (IOException e) {
			return true;
		}
	}
	
	private static byte[] inbuf = new byte[1024];
	static int lenbuf = 0, ptrbuf = 0;
	
	private static int readByte()
	{
		if(lenbuf == -1)throw new InputMismatchException();
		if(ptrbuf >= lenbuf){
			ptrbuf = 0;
			try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
			if(lenbuf <= 0)return -1;
		}
		return inbuf[ptrbuf++];
	}
	
	private static boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
//	private static boolean isSpaceChar(int c) { return !(c >= 32 && c <= 126); }
	private static int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }
	
	private static double nd() { return Double.parseDouble(ns()); }
	private static char nc() { return (char)skip(); }
	
	private static String ns()
	{
		int b = skip();
		StringBuilder sb = new StringBuilder();
		while(!(isSpaceChar(b))){
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	
	private static char[] ns(int n)
	{
		char[] buf = new char[n];
		int b = skip(), p = 0;
		while(p < n && !(isSpaceChar(b))){
			buf[p++] = (char)b;
			b = readByte();
		}
		return n == p ? buf : Arrays.copyOf(buf, p);
	}
	
	private static char[][] nm(int n, int m)
	{
		char[][] map = new char[n][];
		for(int i = 0;i < n;i++)map[i] = ns(m);
		return map;
	}
	
	private static int[] na(int n)
	{
		int[] a = new int[n];
		for(int i = 0;i < n;i++)a[i] = ni();
		return a;
	}
	
	private static int ni()
	{
		int num = 0, b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static long nl()
	{
		long num = 0;
		int b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}

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
	static String INPUT = "";
	
	static void solve()
	{
		int n = ni();
		int[][] co = new int[n][];
		for(int i = 0;i < n;i++){
			co[i] = new int[]{ni(), ni(), ni(), i};
		}
		
		int[][] xs = Arrays.copyOf(co, n);
		Arrays.sort(xs, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return a[0] - b[0];
			}
		});
		
		int[][] ys = Arrays.copyOf(co, n);
		Arrays.sort(ys, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return a[1] - b[1];
			}
		});
		
		int[][] tempx = new int[1<<n][];
		for(int i = 0;i < 1<<n;i++){
			int[] temp = new int[n];
			Arrays.fill(temp, Integer.MAX_VALUE);
			{
				int pre = -9999999;
				for(int k = 0;k < n;k++){
					if(i<<~xs[k][3]<0){
						pre = xs[k][0];
					}
					temp[xs[k][3]] = Math.min(temp[xs[k][3]], xs[k][0]-pre);
				}
			}
			
			{
				int pre = 9999999;
				for(int k = n-1;k >= 0;k--){
					if(i<<~xs[k][3]<0){
						pre = xs[k][0];
					}
					temp[xs[k][3]] = Math.min(temp[xs[k][3]], pre-xs[k][0]);
				}
			}
			tempx[i] = temp;
		}
		
		int[][] tempy = new int[1<<n][];
		for(int i = 0;i < 1<<n;i++){
			int[] temp = new int[n];
			Arrays.fill(temp, Integer.MAX_VALUE);
			{
				int pre = -9999999;
				for(int k = 0;k < n;k++){
					if(i<<~ys[k][3]<0){
						pre = ys[k][1];
					}
					temp[ys[k][3]] = Math.min(temp[ys[k][3]], ys[k][1]-pre);
				}
			}
			
			{
				int pre = 9999999;
				for(int k = n-1;k >= 0;k--){
					if(i<<~ys[k][3]<0){
						pre = ys[k][1];
					}
					temp[ys[k][3]] = Math.min(temp[ys[k][3]], pre-ys[k][1]);
				}
			}
			tempy[i] = temp;
		}
		
		long[] anss = new long[n+1];
		Arrays.fill(anss, Long.MAX_VALUE);
		for(int i = 0;i < 1<<n;i++){
			int mask = (1<<n)-1-i;
			for(int j = mask;j >= 0;j--){ j &= mask;
				long ans = 0;
				for(int k = 0;k < n;k++){
					long v = Math.min(tempx[i][k], tempy[j][k]);
					v = Math.min(v, Math.abs(co[k][0]));
					v = Math.min(v, Math.abs(co[k][1]));
					ans += v*co[k][2];
				}
				int bc = Integer.bitCount(i|j);
				anss[bc] = Math.min(anss[bc], ans);
			} // include j=0
		}
		for(int i = 0;i <= n;i++){
			out.println(anss[i]);
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
//		int n = 15, m = 99999;
//		Random gen = new Random();
//		StringBuilder sb = new StringBuilder();
//		sb.append(n + " ");
//		for (int i = 0; i < n; i++) {
//			sb.append(gen.nextInt(20000)-10000 + " ");
//			sb.append(gen.nextInt(20000)-10000 + " ");
//			sb.append(gen.nextInt(20000) + " ");
//		}
//		INPUT = sb.toString();

		
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

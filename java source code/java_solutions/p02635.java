import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";
	
	static void solve()
	{
		char[] s = ns().toCharArray();
		int K = ni();
		int n = s.length;
		int[] b = new int[n+1];
		int p = 0;
		int one = 0;
		int ao = 0;
		for(int i = 0;i < n;i++){
			if(s[i] == '1'){
				one++;
				ao++;
			}else{
				b[p++] = one;
				one = 0;
			}
		}
		b[p++] = one;
		b = Arrays.copyOf(b, p);
		K = Math.min(ao, K);
		
		long[][] dp = new long[K+1][ao+1]; // #trans, balance
		dp[0][0] = 1;
		int mod = 998244353;
		// p+ao<=300
		for(int i = 0;i < p;i++){
			long[][] ndp = new long[K+1][ao+1]; // #trans, balance
			for(int j = 0;j <= K;j++){
				for(int k = 0;k <= ao;k++){
					if(dp[j][k] != 0){
						// k+l-b[i] >= 0
						// l >= b[i]-k
						// k+l-b[i] <= ao
						// l <= ao + b[i]-K
						// j+l-b[i]<=K
						// l<=K+b[i]-j
						int sup = Math.min(K+b[i]-j, Math.min(ao, ao+b[i]-k));
						for(int l = Math.max(0, b[i]-k);l <= sup;l++){
							int nk = k + l - b[i];
							if(l-b[i] > 0){
								ndp[j+l-b[i]][nk] += dp[j][k];
								if(ndp[j+l-b[i]][nk] >= mod)ndp[j+l-b[i]][nk] -= mod;
							}else{
								ndp[j][nk] += dp[j][k];
								if(ndp[j][nk] >= mod)ndp[j][nk] -= mod;
							}
						}
					}
				}
			}
			dp = ndp;
		}
		
		long ans = 0;
		for(int i = 0;i <= K;i++){
			ans += dp[i][0];
		}
		out.println(ans % mod);
	}
	
	public static void main(String[] args) throws Exception
	{
//		int n = 300, m = 99999;
//		Random gen = new Random();
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < 150; i++) {
//			sb.append("01");
//		}
//		sb.append(" " + 1000000000);
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

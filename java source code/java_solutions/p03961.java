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
		int n = ni();
		int[] a = na(n);
		int[] zs = new int[n+1];
		
		int[] pre = new int[n];
		int[] lower = new int[n];
		Arrays.fill(lower, -1);
		int[] fl = new int[n+3];
		for(int i = n-1;i >= 0;i--){
			if(a[i] != 0){
				pre[a[i]-1] += 1;
				lower[i] = sumFenwick(fl, a[i]-1);
				addFenwick(fl, a[i]-1, 1);
			}
		}
		for(int i = 0;i < n;i++){
			if(pre[i] >= 2){
				out.println(0);
				return;
			}
			zs[i+1] = zs[i] + 1 - pre[i];
		}
//		tr(zs);
		long F = 1;
		int mod = 1000000007;
		// 2*2+1*2+0*2
		// (3*6+2*6+1*6+0*6)*6
		// 6*6*6
		// 3*2*4*2
		// 1*1*12*1
		// 216+48+12
		long OFS = 1;
		{
			int space = 0;
			for(int i = n-1;i >= 0;i--){
				if(a[i] == 0){
					space++;
					OFS = OFS * space % mod;
				}
			}
		}
		
		int space = 0;
		long ret = 0;
		long ret2 = 0;
		long ret3 = 0;
		long FS = 1;
		// 24 3
		// 23 2
		// 4 2
		long lowsum = 0;
		for(int i = n-1;i >= 0;i--){
			if(a[i] == 0){
//				tr(space, (long)space*(space+1)/2, F);
				ret = ret * (space+1) % mod;
				ret += (long)space*(space+1)/2%mod*F%mod*FS%mod;
				
				ret3 += lowsum%mod*F%mod*OFS%mod*invl(zs[n],mod)%mod;
				
				// 3*6 + 3*2
				space++;
				FS = FS * space % mod;
			}else{
				// l h
				// l/(l+h)*OFS*d*F
				ret2 += lower[i] * OFS % mod * F % mod;
				int los = zs[a[i]-1];
				ret2 += los*invl(zs[n], mod) % mod * OFS % mod * space % mod * F % mod;
				
				lowsum += zs[n]-los;
			}
			F = F * (n-i) % mod;
		}
		ret += ret2;
		ret += ret3;
		ret += FS;
		
		ret%=mod;
		out.println(ret);
	}
	
	public static long invl(long a, long mod) {
		long b = mod;
		long p = 1, q = 0;
		while (b > 0) {
			long c = a / b;
			long d;
			d = a;
			a = b;
			b = d % b;
			d = p;
			p = q;
			q = d - c * q;
		}
		return p < 0 ? p + mod : p;
	}

	
	public static int sumFenwick(int[] ft, int i)
	{
		int sum = 0;
		for(i++;i > 0;i -= i&-i)sum += ft[i];
		return sum;
	}
	
	public static void addFenwick(int[] ft, int i, int v)
	{
		if(v == 0 || i < 0)return;
		int n = ft.length;
		for(i++;i < n;i += i&-i)ft[i] += v;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main{

	void solver() {
		Scanner sc = new Scanner(System.in);
		long N = sc.nextLong();
		long P = sc.nextLong();
		long Q = sc.nextLong();
		long[] C = new long[(int)N];
		for (int i = 0; i < N; i++) {
			C[i] = sc.nextInt();
		}
		Long[] a = new Long[(int)N];
		for (int i = 0; i < N; i++) {
			a[i] = C[i] + i * P;
		}
		Arrays.sort(a, new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				return -Long.compare(o1, o2);
			}
		});
		for (int i = 1; i < N; i++) {
			a[i] += a[i - 1];
		}
		long ans = P * N * (2 * Q + N - 1) / 2;
		long diff = N * (N - 1) * P / 2;
		// i + 1:the number of days takig a dinner at the dining room
		for (int i = 0; i < N; i++) {
			ans = Math.max(ans, a[i] + P * (N - (i + 1)) * (2 * Q + 2 * (N - (i + 1) - 1)) / 2 - diff);
		}
		System.out.println(ans);
	}
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";


	public static void main(String[] args) throws Exception
	{
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);

		new Main().solver();
		out.flush();
	}

	static long nl()
	{
		try {
			long num = 0;
			boolean minus = false;
			while((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'));
			if(num == '-'){
				num = 0;
				minus = true;
			}else{
				num -= '0';
			}

			while(true){
				int b = is.read();
				if(b >= '0' && b <= '9'){
					num = num * 10 + (b - '0');
				}else{
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}

	static char nc()
	{
		try {
			int b = skip();
			if(b == -1)return 0;
			return (char)b;
		} catch (IOException e) {
		}
		return 0;
	}

	static double nd()
	{
		try {
			return Double.parseDouble(ns());
		}catch(Exception e) {
		}
		return 0;
	}

	static String ns()
	{
		try{
			int b = skip();
			StringBuilder sb = new StringBuilder();
			if(b == -1)return "";
			sb.append((char)b);
			while(true){
				b = is.read();
				if(b == -1)return sb.toString();
				if(b <= ' ')return sb.toString();
				sb.append((char)b);
			}
		} catch (IOException e) {
		}
		return "";
	}

	public static char[] ns(int n)
	{
		char[] buf = new char[n];
		try{
			int b = skip(), p = 0;
			if(b == -1)return null;
			buf[p++] = (char)b;
			while(p < n){
				b = is.read();
				if(b == -1 || b <= ' ')break;
				buf[p++] = (char)b;
			}
			return Arrays.copyOf(buf, p);
		} catch (IOException e) {
		}
		return null;
	}

	public static byte[] nse(int n)
	{
		byte[] buf = new byte[n];
		try{
			int b = skip();
			if(b == -1)return null;
			is.read(buf);
			return buf;
		} catch (IOException e) {
		}
		return null;
	}

	static int skip() throws IOException
	{
		int b;
		while((b = is.read()) != -1 && !(b >= 33 && b <= 126));
		return b;
	}

	static boolean eof()
	{
		try {
			is.mark(1000);
			int b = skip();
			is.reset();
			return b == -1;
		} catch (IOException e) {
			return true;
		}
	}

	static int ni()
	{
		try {
			int num = 0;
			boolean minus = false;
			while((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'));
			if(num == '-'){
				num = 0;
				minus = true;
			}else{
				num -= '0';
			}

			while(true){
				int b = is.read();
				if(b >= '0' && b <= '9'){
					num = num * 10 + (b - '0');
				}else{
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Main {
	public static HashSet<Integer> r= new HashSet<Integer>();;
	public static void main(String[] args) throws IOException {
		FastScanner in= new FastScanner(System.in);
		PrintWriter out= new PrintWriter(System.out);
		
		String s= in.next();
		if(s.length()<=3) {
			System.out.println("No");
			return;
		}
		if(s.substring(0, 4).equals("YAKI")) System.out.println("Yes");
		else System.out.println("No");
			
		
	}
	static class p implements Comparable<p>{
		int x;
		int y;
		public p(int a, int b) {
			x=a;
			y=b;
		}
		public int compareTo(p o) {
			return this.x-o.x;
		}
	}
	static class FenwickTree {
		long[] ft;
		public FenwickTree(int n) {
			ft = new long[n + 1];
		}

		int rsq(int b) {
			int sum = 0;
			for (; b > 0; b -= (b & (-b)))
				sum += ft[b];
			return sum;
		}

		int rsq(int a, int b) {
			return rsq(b) - (a == 1 ? 0 : rsq(a - 1));
		}

		void update(int k, int v) {
			for (; k < ft.length; k += (k & (-k)))
				ft[k] += v;
		}
	}
	public static long gcd(long a, long b) {
		return b==0 ? a: gcd(b, a%b);
	}
	public static long lcm(long a, long b) {
		return a*b/ gcd(a,b);
	}
	static class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner(InputStream in) {
			br = new BufferedReader(new InputStreamReader(in));
			st = new StringTokenizer("");
		}

		public String next() throws IOException {
			if (!st.hasMoreTokens()) {
				st = new StringTokenizer(br.readLine());
				return next();
			}
			return st.nextToken();
		}

		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
		public double nextDouble() throws NumberFormatException, IOException {
			return Double.parseDouble(next());
		}
		public long nextLong() throws IOException {
			return Long.parseLong(next());
		}
	}
}

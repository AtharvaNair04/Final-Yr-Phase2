import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		FastScanner sc = new FastScanner();
		int n = sc.nextInt();
		String ss = sc.next();
		if(ss.contains("2")) {
			char[] s = ss.toCharArray();
			boolean[] arr = new boolean[n];
			for(int i = 0; i < n; i++) {
				arr[i] = (s[i] == '2');
			}
			
			boolean[] res = arr;
			while(n > 1) {
				res = reduce(res, n);
				n = res.length;
			}
			System.out.println(res[0] ? "1" : "0");
		}
		else {
			char[] s = ss.toCharArray();
			boolean[] arr = new boolean[n];
			for(int i = 0; i < n; i++) {
				arr[i] = (s[i] == '3');
			}
			
			boolean[] res = arr;
			while(n > 1) {
				res = reduce(res, n);
				n = res.length;
			}
			System.out.println(res[0] ? "2" : "0");
		}
	}
	static boolean[] reduce(boolean[] arr, int n) {
		int d = 1;
		while((d<<1) < n) d <<= 1;
		int m = n - d;
		boolean[] res = new boolean[m];
		for(int i = 0; i < m; i++) {
			res[i] = arr[i] ^ arr[i+d];
		}
		return res;
	}
	static class FastScanner {
		public BufferedReader reader;
		public StringTokenizer tokenizer;
		public FastScanner() {
			reader = new BufferedReader(new InputStreamReader(System.in), 32768);
			tokenizer = null;
		}
		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}
		public int nextInt() {
			return Integer.parseInt(next());
		}
		public long nextLong() {
			return Long.parseLong(next());
		}
		public double nextDouble() {
			return Double.parseDouble(next());
		}
		public String nextLine() {
			try {
				return reader.readLine();
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}

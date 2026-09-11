import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.text.*;

public class Main {

	static char[] arr;
	static BigInteger[][][] dp;
	static BigInteger zero = BigInteger.ZERO, one = BigInteger.ONE;

	public static BigInteger dp(int idx, int less, int rem) {
		if (rem < 0)
			return zero;
		if (idx == arr.length)
			return rem == 0 ? one : zero;
		if (dp[idx][less][rem] != null)
			return dp[idx][less][rem];
		if (less == 1) {
			BigInteger ans = zero;
			ans = ans.add(dp(idx + 1, less, rem));
			for (int i = 1; i <= 9; i++) {
				ans = ans.add(dp(idx + 1, less, rem - 1));
			}
			return dp[idx][less][rem] = ans;
		} else {
			BigInteger ans = zero;
			ans = ans.add((dp(idx + 1, arr[idx] == '0' ? 0 : 1, rem)));
			for (char i = '1'; i < arr[idx]; i++) {
				ans = ans.add((dp(idx + 1, 1, rem - 1)));
			}
			if (arr[idx] != '0')
				ans = ans.add((dp(idx + 1, 0, rem - 1)));
			return dp[idx][less][rem] = ans;
		}
	}

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(System.out);
		arr = sc.next().toCharArray();
		int k = sc.nextInt();
		dp = new BigInteger[arr.length][2][k + 1];

		pw.println(dp(0, 0, k));
		pw.close();
	}

	static class Scanner {
		StringTokenizer st;
		BufferedReader br;

		public Scanner(InputStream s) {
			br = new BufferedReader(new InputStreamReader(s));
		}

		public Scanner(FileReader r) {
			br = new BufferedReader(r);
		}

		public String next() throws IOException {
			while (st == null || !st.hasMoreTokens())
				st = new StringTokenizer(br.readLine());
			return st.nextToken();
		}

		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}

		public long nextLong() throws IOException {
			return Long.parseLong(next());
		}

		public String nextLine() throws IOException {
			return br.readLine();
		}

		public double nextDouble() throws IOException {
			return Double.parseDouble(next());
		}

		public int[] nextIntArr(int n) throws IOException {
			int[] arr = new int[n];
			for (int i = 0; i < arr.length; i++)
				arr[i] = nextInt();
			return arr;
		}

		public boolean ready() throws IOException {
			return br.ready();
		}

	}
}

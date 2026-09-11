import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Main {

	
	public static void main(String[] args) {
		FastScanner scanner=new FastScanner();
		int n=scanner.nextInt();
		int a[]=scanner.readArray(n);
		
		long dp[][]=new long[n][n];
		for(int i=n-1;i>=0;i--) {
			for(int j=i;j<n;j++) {
				if(i==j) dp[i][j]=a[i];
				else dp[i][j]=Math.max(a[i]-dp[i+1][j], a[j]-dp[i][j-1]);
			}
		}
		System.out.println(dp[0][n-1]);

	}
	static class FastScanner{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st=new StringTokenizer("");
		
		String next() {
			while (!st.hasMoreTokens())
				try {
					st=new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return st.nextToken();
		}
		int nextInt() {
			return Integer.parseInt(next());
		}
		long nextLong() {
			return Long.parseLong(next());
		}
		int [] readArray(int n) {
			int a[]=new int[n];
			for(int i=0;i<n;i++) a[i]=nextInt();
			return a;
		}
		
		
	}

}

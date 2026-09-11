import java.util.*;



import java.io.*;
public class Main {

	public static void main(String[] args) {
	FastScanner sc=new FastScanner();
	int n=sc.nextInt(),w=sc.nextInt();
	int a[][]=new int[n][2];
	for(int i=0;i<n;i++) {
		a[i][0]=sc.nextInt();
		a[i][1]=sc.nextInt();
	
	}
	long dp[][]=new long[n+1][w+1];
	for(int i=0;i<=n;i++) {
		for(int j=0;j<=w;j++) {
			if(i==0||j==0) {
				dp[i][j]=0;
			}
			else {
				if(j<a[i-1][0])
					dp[i][j]=dp[i-1][j];
				else {
					dp[i][j]=Math.max(dp[i-1][j], dp[i-1][j-a[i-1][0]]+a[i-1][1]);
				}
				
				
			}
		}
	}
	
	System.out.println(dp[n][w]);


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

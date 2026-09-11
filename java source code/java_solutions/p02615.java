/* package codechef; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
public class Main
{
	public static void main (String[] args) throws java.lang.Exception
	{
		// your code goes here
		FastScanner sc=new FastScanner();
		int n=sc.nextInt();
		int a[]=sc.readArray(n);
		Arrays.sort(a);
		int min=Integer.MAX_VALUE;
	//	int j=n-1;
		long ans=0;
		ArrayList<Integer> al=new ArrayList<Integer>();
		al.add(a[n-1]);
		for(int i=n-2;i>=0;i--)
		for(int j=0;j<2;j++) al.add(a[i]);
		
		for(int i=0;i<n-1;i++)
		ans+=al.get(i);
		
		//ans-= al.get(n-1);
	
	     
	

	System.out.println(ans);
		
		
		
	}
		static class FastScanner {
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
		int[] readArray(int n) {
			int[] a=new int[n];
			for (int i=0; i<n; i++) a[i]=nextInt();
			return a;
		}
		long nextLong() {
			return Long.parseLong(next());
		}
}
}

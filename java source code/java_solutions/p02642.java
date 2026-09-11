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
		PrintWriter out=new PrintWriter(System.out);
		FastScanner fs=new FastScanner();
	
		int n=fs.nextInt();
		int a[]=fs.readArray(n);
	//	Arrays.sort(a);
	    
	    int count=0;
	   int max=0;
	   for(int i:a)
	   max=Math.max(i,max);
	   
	   int div[]=new int[max+1];
	   
	   for(int i:a){
	       for(int j=i;j<=max;j+=i)
	       div[j]++;
	   }
	 
	   for(int i:a){
	       if(div[i]==1)
	       count++;
	   }
	    
	    out.println(count);
		out.close();
	
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

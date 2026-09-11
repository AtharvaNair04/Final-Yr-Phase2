import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;

public class Main {
	static long m=(long)1e9+7;
	public static long mod(long a) {
		return (a%m+m)%m;
		
	}
	public static long add(long a,long b) {
		return mod(mod(a)+mod(b));
		
	}
	public static long mul(long a,long b) {
		return mod(mod(a)*mod(b));
		
	}
	
	
	static long findProductSum(int a[], int n) 
	{ 
	  
		long ans1=0;
		long sum=0;
		for(int i=0;i<n;i++) {
			ans1=add(mul(sum, a[i]), ans1);
			sum=add(sum, a[i]);
		}
		return ans1;
		
	} 
	public static void main(String []args) {
		
		FastScanner scanner=new FastScanner();
		int n=scanner.nextInt();
		int a[]=scanner.readArray(n);
		System.out.println(findProductSum(a, n));
	}
	  

	static class FastScanner {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer("");

		String next() {
			while (!st.hasMoreTokens())
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		int[] readArray(int n) {
			int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = nextInt();
			return a;
		}

		long nextLong() {
			return Long.parseLong(next());
		}
	}

}

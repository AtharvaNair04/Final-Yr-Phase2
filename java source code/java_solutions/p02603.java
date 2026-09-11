// Submitted By SUbhash Yadav

import java.util.*;
import java.lang.*;
import java.math.BigInteger;
import java.io.*;


public class Main
{ static class Interval { 
    int buy, sell; 
} 
public static int Swaps(int[] arr, int l, int m, int r) 
{ 

  //left
    int[] left = Arrays.copyOfRange(arr, l, m + 1); 

    // Right  
    int[] right = Arrays.copyOfRange(arr, m + 1, r + 1); 

    int i = 0, j = 0, k = l, swaps = 0; 

    while (i < left.length && j < right.length) { 
        if (left[i] <= right[j]) 
            arr[k++] = left[i++]; 
        else { 
            arr[k++] = right[j++]; 
            swaps += (m + 1) - (l + i); 
        } 
    } 

    
    while (i < left.length) 
        arr[k++] = left[i++]; 

   
    while (j < right.length) 
        arr[k++] = right[j++]; 

    return swaps; 
} 
public static int Count(int[] arr, int l, int r) 
{ 

 
    int count = 0; 

    if (l < r) { 
        int m = (l + r) / 2; 

        

        // Left subarray count 
        count += Count(arr, l, m); 

        // Right subarray count 
        count += Count(arr, m + 1, r); 

        //  count 
        count += Swaps(arr, l, m, r); 
    } 

    return count; 
} 

	public static void main (String[] args) throws java.lang.Exception
	{
		
		FastScanner sc=new FastScanner();
		int n=sc.nextInt();
		int a[]=sc.readArray(n);
		
		 long ans=1000;
        
		 for(int i=0;i<n-1;i++)
		 {   long stocks = 0;
         if (a[i] < a[i + 1]) stocks = ans / a[i];
         ans += (a[i + 1] - a[i]) * stocks;
		 }
        
         
         
         
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

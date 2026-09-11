import java.util.*;
import java.io.*;
class Main {
  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      int n = sc.nextInt();
      long k = sc.nextLong();
      long[] a = new long[n];
      for(int i = 0; i<n; i++)
          a[i] = sc.nextLong();
      Arrays.sort(a);
      long low = 1;
      long high = a[a.length-1];
      long ans = high;
      while(low<=high) {
          long mid = low + (high-low)/2;
          if(isPossible(a, mid, k)) {
              ans = Math.min(ans, mid);
              high = mid-1;
          } else low = mid+1;
      }
      System.out.println(ans);
  }
  static boolean isPossible(long[] a, long mid, long k) {
      long x = 0;
      for(long i: a) {
          if(i%mid==0) x+=i/mid-1;
          else x+=i/mid;
      }
      return x<=k;
  }
}

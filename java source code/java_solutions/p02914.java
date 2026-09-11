import java.util.*;

public class Main {

    public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      int num = Integer.parseInt(sc.next());
      
      long[] inputs = new long[num];
      long sum = 0;
      for (int i = 0; i < num; i++) {
        inputs[i] = Long.parseLong(sc.next());
        sum ^= inputs[i];
      }
      // only interested in 1's bit
      for (int i = 0; i < num; i++) {
        inputs[i] &= ~sum;
      }
      System.out.println(sum + (maxSubarrayXOR(inputs) << 1));
    }
  
  	public static long maxSubarrayXOR(long A[]) {
      int idx = 0;
      for (int bit = 60; bit >= 0; bit--) {
        int find = idx;

        while (find < A.length) {
          // find the element which MSB is in bit'th
          if ((A[find] & (1L << bit)) != 0) {
          	break;
          }
          find++;
        }
        
        if (find == A.length) continue;
        
        // swap element in idx and find
        if (idx != find) {
          A[idx] ^= A[find];
          A[find] ^= A[idx];
          A[idx] ^= A[find];
          find = idx;
        }
        
        for (int i = 0; i < A.length; i++) {
       	  if (i != find && (A[i] & (1L << bit)) != 0) {
            A[i] ^= A[find];
          }
        }

        idx++;
      }
      
      long ret = 0;
      for (long l: A) {
        ret ^= l;
      }

      return ret;
    } 
}

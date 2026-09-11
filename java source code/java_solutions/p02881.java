import java.util.*;
public class Main{
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      long n = sc.nextLong();

      long m = (long)Math.sqrt(n);

      long c = n+2;

      for (long i = 1; i < m+1; i++) {
         if (n % i == 0) {
            if(i + n/i < c){
               c = i + n/i;
            }
         }
      }
      System.out.println(c-2);
   }
}

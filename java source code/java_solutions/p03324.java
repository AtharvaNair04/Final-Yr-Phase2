import java.util.*;
public class Main {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      int d = sc.nextInt();
      int n = sc.nextInt();

      int i = 0;

      int j = 1;

      if (n != 100) {

      while ( i < d ) {
         j = j * 100;
         i = i + 1;
      }
      System.out.println(n * j);


      }
      else {
         if (d == 0) {
            System.out.println(101);
         }
         else if (d == 1) {
            System.out.println(10100);
         }
         else {
            System.out.println(1010000);
         }
      }
   }
}

import java.util.*;
public class Main {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      int a = sc.nextInt();
      int b = sc.nextInt();
      int c = sc.nextInt();
      int x = sc.nextInt();
      int y = sc.nextInt();

      int cost = 0;

      if (x <= y) {
         if (a + b <= 2 * c) {
            cost += (a + b) * x;
            if (b <= 2 * c) {
               cost += b * (y - x);
            } else {
               cost += 2 * c * (y - x);
            }
         } else {
            cost += 2 * c * x;
            if (b <= 2 * c) {
               cost += b * (y - x);
            } else {
               cost += 2 * c * (y - x);
            }
         }
      } else {
         if (a + b <= 2 * c) {
            cost += (a + b) * y;
            if (a <= 2 * c) {
               cost += a * (x - y);
            } else {
               cost += 2 * c * (x - y);
            }
         } else {
            cost += 2 * c * y;
            if (a <= 2 * c) {
               cost += a * (x - y);
            } else {
               cost += 2 * c * (x - y);
            }
         }
      }
      
      System.out.println(cost);

   }
}

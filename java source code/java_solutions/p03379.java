import java.util.*;
public class Main {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      int n = sc.nextInt();

      int[] a;
      a = new int[n];
      for (int i = 0; i < n; i++) {
         int s = sc.nextInt();
         a[i] = s;
      }

      int[] b;
      b = new int[n];
      for (int i = 0; i < n; i++) {
         b[i] = a[i];
      }

      Arrays.sort(b);

      int med1 = b[n / 2 - 1];
      int med2 = b[n / 2];

      for (int i = 0; i < n; i++) {
         if (a[i] <= med1) {
            System.out.println(med2);
         } else {
            System.out.println(med1);
         }
      }
   }
}

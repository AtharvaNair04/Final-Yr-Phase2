import java.util.*;
public class Main {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      int[] a = new int[3];

      for (int i = 0; i < a.length; i++) {
         a[i] = sc.nextInt();
      }

      Arrays.sort(a);

      int c;

      if ((a[1] + a[2]) % 2 == 0 && (a[1] + a[0]) % 2 == 0) {
         c = a[2] - ( a[0] + a[1] ) / 2;
         System.out.println(c);
      }

      else if ((a[0] + a[1]) % 2 == 0) {
         c = a[2] - ( a[0] + a[1] + 2) / 2 + 1;
         System.out.println(c);
      }

      else {
         c = a[2] - ( a[0] + a[1] - 1) / 2 + 1;
         System.out.println(c);
      }
   }
}

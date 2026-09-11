import java.util.*;
public class Main {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      int N = sc.nextInt();
      int count = 0;

      String S = sc.next();
      char[] s = new char[N];
      for (int i = 0; i < s.length; i++) {
         s[i] = S.charAt(i);
      }

      for (int i = 0; i < s.length-2; i++) {
         if (s[i]=='A') {
            if(s[i+1]=='B'){
               if(s[i+2]=='C'){
                  count = count + 1;
               }
            }
         }
      }

      System.out.println(count);
   }
}

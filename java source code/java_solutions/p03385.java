import java.io.*;

public class Main {
   public static void main(String[] args) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      try {
         String line = reader.readLine();
         int count = 0;
         for (int i = 0; i < 3; i++) {
            char a = line.charAt(i);
            if (a == 'a') {
               count ++;
            }
         }
         if (count == 1) {
            count = 0;
            for (int j = 0; j < 3; j++) {
            char b = line.charAt(j);
               if (b == 'b') {
                  count ++;
               }
            }
            if (count == 1) {
               count = 0;
               for (int s = 0; s < 3; s++) {
               char c = line.charAt(s);
                  if (c == 'c') {
                     count ++;
                  }
               }
               if (count == 1){
                     System.out.println("Yes");
               }
               else {
                  System.out.println("No");
               }
            }
            else {
               System.out.println("No");
            }
         }
         else {
            System.out.println("No");
         }  
      } catch (IOException e) {
         System.out.println(e);
      }
   }
}

import java.util.*;
import java.io.*;
public class Main {
   public static void main(String[] args) {
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      try {
         String line = reader.readLine();
         
         line = line.replace('C', 'A');
         line = line.replace('G', 'A');
         line = line.replace('T', 'A');

         int count = 0;
         String sample = "A";

         int n = line.indexOf(sample);
         if (n != -1) {
            count = 1;
         }

         for(int i = 1; i < 11; i++){
            sample = sample + "A";

            n = line.indexOf(sample);
            if(n != -1){
               count = i + 1;
            }
         }
         System.out.println(count);
      }

      catch (IOException e) {
         System.out.println(e);
      }
   }
}

import java.io.*;
import java.util.*;
public class Main{
   public static void main(String[] args) throws IOException{
   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   int[] kazu = new int[10];
   for(int a = 0; a <= 9; a++){
   kazu[a] = Integer.parseInt(br.readLine());
   }
   int n = 10;
   int kari;
   for(int b = 0; b <= n-2; b++){
   int x = b;
   for(int c = b+1; c <= n-1; c++){
   if(kazu[x] < kazu[c]){
   x = c;
   }
   }
   if(x != b){
   kari = kazu[x];
   kazu[x] = kazu[b];
   kazu[b] = kari;
   }
   }
   for(int d = 0; d <= 2; d++){
   System.out.println(kazu[d]);
   }
   }
}

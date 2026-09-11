import java.util.*;
import java.io.*;

public class Main{
 public static void main(String[] args){
   Scanner s = new Scanner(System.in);
   int n = s.nextInt();
   int[] v = new int[n];
   
   int profit =0;
   for(int i=0; i<n; i++){
    v[i] = s.nextInt(); 
   }
   
   for(int j=0; j<n; j++){
    profit = profit + Math.max(0,v[j] - s.nextInt());
   }
   
   System.out.print(profit);
 }
}

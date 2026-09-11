import java.util.*;
import java.io.*;
import static java.lang.System.*;
import static java.lang.Math.*;

public class Main {
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    while(true){
      int n = sc.nextInt();
      if(n == 0) break;
      int i;
      int[] array = new int[n];

      for(i=0; i<n; i++){
        array[i] = sc.nextInt();
      }
      Arrays.sort(array);
      array[0] = 0;
      array[array.length-1] = 0;

      int ave=0;
      for(i=0; i<n; i++){
        ave += array[i];
        //System.out.println(ave);
      }
      ave /= n-2;

    System.out.printf("%d\n", ave);
    }
  }
}

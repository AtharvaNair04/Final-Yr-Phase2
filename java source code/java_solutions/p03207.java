import java.util.Scanner;
import java.util.ArrayList;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int sum = 0;
    int max = -1;
    int max_index = -1;
    int[] array = new int[n];

    for(int i=0; i<n; i++){
      array[i] = sc.nextInt();
      if(array[i]>max){
        max = array[i];
        max_index = i;
      }
    }
    array[max_index] /= 2;
    for(int i=0; i<n; i++){
      sum += array[i];
    }

    System.out.println(sum);
  }

}

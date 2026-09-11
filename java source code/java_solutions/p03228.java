import java.util.*;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int A = sc.nextInt();
    int B = sc.nextInt();
    int N = sc.nextInt();
    int count = 0;
    while(count < N){
      if(count %2 == 0){
        if(A %2 != 0){
          A = A - 1;
        }
        int halfA = A / 2;
        A -= halfA;
        B += halfA;
        count++;
      }else{
        if(B %2 != 0){
          B = B - 1;
        }
        int halfB = B / 2;
        B -= halfB;
        A += halfB;
        count++;
      }
    }
    System.out.println(A + " " + B);
  }
}

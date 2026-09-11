import java.util.*;
import java.io.*;

public class Main{
  public static void main(String args[]){
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    int[] A =new int[N];
    for(int i =0; i<N; i++){
      A[i] = sc.nextInt();
    }
    int oCost = Math.abs(A[0]) + Math.abs(A[N-1]);
    for(int i =0; i<N-1; i++){
        oCost = oCost + Math.abs(A[i]-A[i+1]);
    }
    for(int i=0; i<N; i++){
      int tCost = 0;
      if(i == 0){
        tCost = oCost - Math.abs(A[0]) - Math.abs(A[0]-A[1]) + Math.abs(A[1]);
      }
      else if(i == N-1){
        tCost = oCost - Math.abs(A[N-1]) - Math.abs(A[N-1]-A[N-2]) + Math.abs(A[N-2]);
      }
      else{
        tCost = oCost - Math.abs(A[i]-A[i-1]) - Math.abs(A[i]-A[i+1]) + Math.abs(A[i-1]-A[i+1]);
      }
      System.out.println(tCost);
    }
  }
}

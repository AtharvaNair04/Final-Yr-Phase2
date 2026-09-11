import java.util.*;
  public class Main{
    public static void main(String[] args){
      Scanner scan = new Scanner(System.in);

    	int[][] A=new int[100][100];
    	int[] B=new int[100];
      int C;
     int n =scan.nextInt();
     int m = scan.nextInt();
      for (int s = 0;s<n;s++) {
        for(int t =0;t<m;t++){
        A[s][t]=scan.nextInt();
      }}
    	for (int s = 0;s<m;s++)   {
         B[s]=scan.nextInt();
      }
      
      for(int s=0;s<n;s++){C=0;
        for(int t=0;t<m;t++){
          C += A[s][t]*B[t];
        }
        System.out.println(C);
      }
}}

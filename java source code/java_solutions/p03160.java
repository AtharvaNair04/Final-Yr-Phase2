import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int[] array = new int[n];
    int[] DP = new int[n+1];
    Arrays.fill(DP,Integer.MAX_VALUE);
    DP[0] = 0;

    for(int i=0; i<n; i++){
      array[i] = sc.nextInt();
    }

    for(int i=0; i<n-2; i++){
      DP[i+1] = Math.min(DP[i+1],DP[i]+Math.abs(array[i+1]-array[i]));
      DP[i+2] = Math.min(DP[i+2],DP[i]+Math.abs(array[i+2]-array[i]));
    }

    DP[n-1] = Math.min(DP[n-1],DP[n-2]+Math.abs(array[n-1]-array[n-2]));

    System.out.println(DP[n-1]);


  }
}

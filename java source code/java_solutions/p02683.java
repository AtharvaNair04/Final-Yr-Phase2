import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int m = sc.nextInt();
    int x = sc.nextInt();

    int[] c = new int[n];
    int[][] a = new int[n][m];
    int ans = Integer.MAX_VALUE;

    for(int i=0; i<n; i++){
      c[i] = sc.nextInt();
      for(int j=0; j<m; j++){
        a[i][j] = sc.nextInt();
      }
    }

    for(int i=0; i<(1<<n); i++){
      int[] total = new int[m];
      int cost = 0;
      int flag = 0;

      for(int j=0; j<n; j++){
        if((i>>j&1)==1){
          for(int k=0; k<m; k++){
            total[k] += a[j][k];
          }
          cost += c[j];
        }
      }
      for(int j=0; j<m; j++){
        if(total[j]>=x)
          flag += 1;
      }

      if(flag==m){

        if(cost<ans){
          ans = cost;
        }
      }

    }

    if(ans==Integer.MAX_VALUE)
      ans = -1;

    System.out.println(ans);
  }
}

import java.util.*;

public class Main{
  public static int roo(int n){
    int ans =1;
    while(n>0){
      ans = ans * n;
      n = n-1;
    }
    return ans;
  }
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n= sc.nextInt();
    int[][] array = new int[n][2];
    double sum =0;

    for(int i=0; i<n; i++){
      array[i][0] = sc.nextInt();
      array[i][1] = sc.nextInt();
    }
    int k =1;
    for(int i=0; i<n-1;i++){

      for(int j=k; j<n; j++){
        sum += Math.sqrt((array[i][0]-array[j][0])*(array[i][0]-array[j][0])+(array[i][1]-array[j][1])*(array[i][1]-array[j][1]));
      }
      k += 1;
    }

    sum = sum * roo(n-2)* (n-1) *2 ;



    System.out.println(sum/roo(n));

  }
}

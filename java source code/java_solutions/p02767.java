import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int[] array = new int[n];
    int sum = 0;

    for(int i=0; i<n; i++){
      array[i] = sc.nextInt();
      sum += array[i];
    }

    int p=0;
    p = sum/n;
    if((double)sum/n-sum/n!=0){
      if(sum/n+1-(double)sum/n > (double)sum/n-sum/n){
        p = sum/n;
        if(p==0){p=1;}
      }else{
        p = sum/n + 1;
        if(p==101){p -= 1;}
      }
    }
    //System.out.println((double)sum/n);

    long ans = 0;

    for(int a: array){
      ans += (a-p) * (a-p);
    }

    System.out.println(ans);
  }
}

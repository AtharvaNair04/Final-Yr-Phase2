import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int a = sc.nextInt();
    long b = sc.nextLong();
    long n = sc.nextLong();
    long max = 0;
    long num = 0;

    long reg = n%b;
    if(n>b){
      n = n-reg-1;
    }else if(n%b==0){
      n = n -1;
    }

    long ans = ((a*n)/b)- a*(n/b);

    System.out.println(ans);


  }
}

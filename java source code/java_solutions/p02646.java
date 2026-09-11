import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    long a = sc.nextLong();
    long v = sc.nextLong();
    long b = sc.nextLong();
    long w = sc.nextLong();
    long t = sc.nextLong();
    long i = Math.abs(b-a);
    long j = (v-w) * t;
    if (i <= j){
      System.out.println("YES");
    }else{
      System.out.println("NO");
    }
  }
}

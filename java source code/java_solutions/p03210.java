import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int x = sc.nextInt();
    System.out.println((x==3 || x==5) || x==7 ? "YES" : "NO");
  }
}

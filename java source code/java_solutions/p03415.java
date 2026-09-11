import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String s1 = sc.next();
    String s2 = sc.next();
    String s3 = sc.next();
    String ans = "";
    ans += s1.charAt(0);
    ans += s2.charAt(1);
    ans += s3.charAt(2);
    System.out.println(ans);
  }

}

import java.util.Scanner;
 
public class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    String ans = in.nextInt() < 1000 ? "ABC" : "ABD";
    System.out.println(ans);
  }
}

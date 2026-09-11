import java.util.*;
 
public class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int a = in.nextInt();
    int b = in.nextInt();
    int c = in.nextInt();
    int d;
    if (c < a) {
      d = a;
      a = c;
      c = d;
    }
    if (c < b) b = c;
    d = a * b / 2;
    System.out.println(d);
  }
}

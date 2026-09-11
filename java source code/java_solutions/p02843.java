import java.util.*;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int x = scanner.nextInt();

    if (105 * (x / 100) < x || x < 100) {
      System.out.println(0);
    } else {
      System.out.println(1);
    }


  }

}

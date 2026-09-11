import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int ans = 0;
    int conf = 0;
    for (int i = 0; i < 3; i++) {
      if ((n / (int)Math.pow(10, i)) % 10 == 1) {
        ans += 9 * Math.pow(10, i);
      } else {
        ans += 1 * Math.pow(10, i);
      }
    }
    System.out.println(ans);

  }

}

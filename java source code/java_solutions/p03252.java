import java.util.Arrays;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String up = sc.next();
    String down = sc.next();
    int[] upMap = new int[26];
    int[] downMap = new int[26];
    Arrays.fill(upMap, -1);
    Arrays.fill(downMap, -1);
    for (int i = 0; i < up.length(); i++) {
      if (upMap[up.charAt(i) - 'a'] < 0) {
        upMap[up.charAt(i) - 'a'] = down.charAt(i);
      } else if (upMap[up.charAt(i) - 'a'] != down.charAt(i)) {
        System.out.println("No");
        return;
      }
      if (downMap[down.charAt(i) - 'a'] < 0) {
        downMap[down.charAt(i) - 'a'] = up.charAt(i);
      } else if (downMap[down.charAt(i) - 'a'] != up.charAt(i)) {
        System.out.println("No");
        return;
      }
    }
    System.out.println("Yes");
  }

}

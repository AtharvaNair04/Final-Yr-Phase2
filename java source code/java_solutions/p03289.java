import java.util.*;
import java.util.Collections;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String S = sc.next();
    String ans = "WA";
    int cnt = 0;
    int cnt2 = 0;
    if (S.charAt(0) == 'A') {
      for (int i = 2; i < S.length() - 1; i++) {
        if (S.charAt(i) == 'C') {
          cnt++;
        }
      }
      String str1 = S.replace("A", "a");
      String str2 = str1.replace("C", "c");
      if (cnt == 1) {
        for (int i = 0; i < str2.length(); i++) {
          if (!Character.isLowerCase(str2.charAt(i))) {
            break;
          } else {
            cnt2++;
          }
        }
        if (cnt2 == str2.length()) {
          ans = "AC";
        }
      }
    }
    System.out.println(ans);
  }
}

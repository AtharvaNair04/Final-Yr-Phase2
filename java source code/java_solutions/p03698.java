import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String S = sc.next();
    int cnt = 0;
    for (int i = 0; i < S.length(); i++) {
      for (int j = 0; j < S.length(); j++) {
        if (S.charAt(i) == S.charAt(j)) {
          cnt++;
        }
      }
    }
    if (cnt - S.length() == 0) {
      System.out.println("yes");
    } else {
      System.out.println("no");
    }
  }
}

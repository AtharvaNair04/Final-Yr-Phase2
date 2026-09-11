import java.util.*;
import java.util.Collections;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    int max = sc.nextInt();
    int cnt = 1;
    for (int i = 0; i < N - 1; i++) {
      int C = sc.nextInt();
      if (C >= max) {
        max = Math.max(max, C);
        cnt++;
      }
    }
    System.out.println(cnt);
  }
}

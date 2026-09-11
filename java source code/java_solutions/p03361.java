import java.util.*;
import java.util.TreeMap;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int H = sc.nextInt();
    int W = sc.nextInt();
    int cnt = 0;
    boolean A = false;
    String ans = "Yes";
    char[][] paints = new char[H][W];
    for (int i = 0; i < H; i++) {
      paints[i] = sc.next().toCharArray();
    }
    int[] y = { 1, -1, 0, 0 };
    int[] x = { 0, 0, 1, -1 };

    loop: for (int i = 0; i < H; i++) {
      for (int j = 0; j < W; j++) {
        if (paints[i][j] == '#') {
          cnt = 0;
          for (int k = 0; k < 4; k++) {
            A = false;
            int nextPointY = i + y[k];
            int nextPointX = j + x[k];
            if (nextPointY < 0 || nextPointY >= H || nextPointX < 0 || nextPointX >= W) {
              continue;
            }
            if (paints[nextPointY][nextPointX] == '#') {
              A = true;
              break;
            }
          }
          if (A == false) {
            ans = "No";
            break loop;
          }
        }
      }
    }
    System.out.println(ans);
  }
}

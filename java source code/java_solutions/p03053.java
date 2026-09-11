import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;

class Main {

  private static final int INF = Integer.MAX_VALUE / 4;
  private static final Integer[] di = { 1, -1, 0, 0 };
  private static final Integer[] dj = { 0, 0, 1, -1 };

  public static boolean isIn(int i, int j, int H, int W) {
    if (0 <= i && i < H && 0 <= j && j < W) {
      return true;
    } else {
      return false;
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int H = scanner.nextInt();
    int W = scanner.nextInt();
    char[][] map = new char[H][W];
    Queue<Point> queue = new ArrayDeque<>();
    int[][] min = new int[H][W];
    for (int[] is : min) {
      Arrays.fill(is, INF);
    }
    for (int i = 0; i < H; i++) {
      scanner.next().getChars(0, W, map[i], 0);
      for (int j = 0; j < W; j++) {
        if (map[i][j] == '#') {
          queue.add(new Point(i, j));
          min[i][j] = 0;
        }
      }
    }
    while (!queue.isEmpty()) {
      Point p = queue.poll();
      for (int k = 0; k < 4; k++) {
        int ci = p.x + di[k];
        int cj = p.y + dj[k];
        if (isIn(ci, cj, H, W) && min[ci][cj] == INF) {
          min[ci][cj] = min[p.x][p.y] + 1;
          queue.add(new Point(ci, cj));
        }
      }
    }
    int ans = 0;
    for (int[] is : min) {
      for (int i : is) {
        ans = Math.max(ans, i);
      }
    }
    System.out.println(ans);
  }
}

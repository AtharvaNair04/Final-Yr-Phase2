import java.util.Scanner;

public class Main {

  public static final long mod7 = 1000000007L;
  static int[][] map;
  static int sx, sy, ex, ey, h, w;
  static int[][] dir = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (run(sc)) {

    }
  }
  static boolean run(Scanner sc) {
    w = sc.nextInt();
    h = sc.nextInt();
    if (w + h == 0) {
      return false;
    }
    map = new int[h][w];
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int v = sc.nextInt();
        if (v == 2) {
          sx = j;
          sy = i;
          map[i][j] = 0;
        } else if (v == 3) {
          ex = j;
          ey = i;
          map[i][j] = 3;
        } else {
          map[i][j] = v;
        }
      }
    }
    int res = rep(sx, sy, 10);
    if (res == Integer.MAX_VALUE) {
      System.out.println(-1);
    } else {
      System.out.println(res);
    }
    return true;
  }

  static int rep(int px, int py, int t) {
    if (t == 0) {
       return Integer.MAX_VALUE;
    }
    int m = Integer.MAX_VALUE;
    for (int di = 0; di < 4; di++) {
      int[] d = dir[di];
      int nx = px;
      int ny = py;
      int by = -1;
      int bx = -1;
      boolean cant = false;
      for (int i = 0; i < 30; i++) {
        int ty = ny + d[1];
        int tx = nx + d[0];
        if (ty < 0 || ty >= h || tx < 0 || tx >= w) {
          cant = true;
          break;
        }
        if (map[ty][tx] == 1) {
          if (i == 0) {
            cant = true;
            break;
          }
          by = ty;
          bx = tx;
          map[by][bx] = 0;
          break;
        }
        if (map[ty][tx] == 3) {
          return 10 - t + 1;
        }
        ny = ty;
        nx = tx;
      }
      if (cant) {
        continue;
      }
      m = Math.min(m, rep(nx, ny, t - 1));
      if (bx != -1) {
        map[by][bx] = 1;
      }
    }
    return m;
  }
}

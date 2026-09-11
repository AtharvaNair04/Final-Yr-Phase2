import java.util.*;

public class Main {
  // constant

  static final int MAX_INT = 1 << 30;

  static final int[][] DRCS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

  // inner classes

  static class Masu implements Comparable<Masu> {
    int level, x, y;

    Masu(int level, int x, int y) {
      this.level = level;
      this.x = x;
      this.y = y;
    }

    public int compareTo(Masu m) {
      return this.level - m.level;
    }
  }

  // main
  public static final void main(String[] args) throws Exception {
    Scanner sc = new Scanner(System.in);

    for (;;) {
      int r = sc.nextInt();
      if (r == 0) break;

      int[][] mlss = new int[2][r + 1];
      int[] mlslen = new int[2];

      for (int k = 0; k < 2; k++) {
        int w = sc.nextInt();
        int h = sc.nextInt();
        int ex = sc.nextInt() - 1;
        int ey = sc.nextInt() - 1;

        int[][] als = new int[h][w];
        int[][] mls = new int[h][w];
        boolean[][] notq = new boolean[h][w];

        for (int y = 0; y < h; y++)
          for (int x = 0; x < w; x++) {
            als[y][x] = sc.nextInt();
            mls[y][x] = MAX_INT;
            notq[y][x] = true;
          }

        mls[ey][ex] = als[ey][ex];
        notq[ey][ex] = false;

        PriorityQueue<Masu> q = new PriorityQueue<Masu>();
        q.add(new Masu(mls[ey][ex], ex, ey));

        mlss[k][0] = 0;
        mlslen[k] = 1;

        while (! q.isEmpty()) {
          Masu min_u = q.poll();

          mlss[k][mlslen[k]++] = min_u.level;
          if (mlslen[k] > r) break;

          for (int[] drc: DRCS) {
            int vx = min_u.x + drc[0];
            int vy = min_u.y + drc[1];
            if (vx < 0 || vx >= w || vy < 0 || vy >= h) continue;

            int l = min_u.level;
            if (l < als[vy][vx]) l = als[vy][vx];

            if (mls[vy][vx] > l) {
              mls[vy][vx] = l;
              if (notq[vy][vx]) {
                notq[vy][vx] = false;
                q.add(new Masu(mls[vy][vx], vx, vy));
              }
            }
          }
        }
      }

      int min_sum = MAX_INT;

      int i0 = 0;
      int i1 = r - i0;

      if (i1 >= mlslen[1]) {
        i1 = mlslen[1] - 1;
        i0 = r - i1;
      }

      while (i0 < mlslen[0] && i1 >= 0) {
        int sum = mlss[0][i0] + mlss[1][i1];
        if (min_sum > sum) min_sum = sum;

        i0++; i1--;
      }

      System.out.println(min_sum);
    }
  }
}

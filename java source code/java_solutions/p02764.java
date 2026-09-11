import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int N = scanner.nextInt();
    int K = scanner.nextInt();
    Niku[] nikus = new Niku[N];
    for (int i = 0; i < N; i++) {
      nikus[i] = new Niku(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
    }

    double left = 0.d;
    double right = 400_000.d;
    while (right - left > 1e-9) {
      double mid = (right + left) / 2;
      List<Point> intersects = new ArrayList<>();
      for (int i = 1; i < N; i++) {
        for (int j = 0; j < i; j++) {
          intersects.addAll(intersects(nikus[i].loc, mid / nikus[i].c, nikus[j].loc, mid / nikus[j].c));
        }
      }
      for (int i = 0; i < N; i++) intersects.add(nikus[i].loc);
      if (suffice(N, K, nikus, mid, intersects)) {
        right = mid;
      } else {
        left = mid;
      }
    }
    System.out.println(right);
  }

  private static boolean suffice(int n, int k, Niku[] nikus, double mid, List<Point> intersects) {
    for (Point p : intersects) {
      int count = 0;
      for (int i = 0; i < n; i++) {
        if (nikus[i].loc.dist(p) * nikus[i].c < mid + 1e-9) count++;
      }
      if (count >= k) {
        return true;
      }
    }
    return false;
  }

  private static class Niku {
    private final Point loc;
    private final int c;

    private Niku(int x, int y, int c) {
      this.loc = new Point(x, y);
      this.c = c;
    }
  }

  private static List<Point> intersects(Point c1, double r1, Point c2, double r2) {
    List<Point> list = new ArrayList<>();
    double dist = c1.dist(c2);
    double cm = (r1 * r1 - r2 * r2 + dist * dist) / dist / 2;
    Point m = c2.subtract(c1).scale(cm / dist).add(c1);
    double pm = Math.sqrt(r1 * r1 - cm * cm);
    Point mc = c1.subtract(m).scale(pm / cm);
    list.add(mc.rotate(Math.PI / 2).add(m));
    list.add(mc.rotate(-Math.PI / 2).add(m));
    return list;
  }

  public static class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
      this.x = x;
      this.y = y;
    }

    public Point scale(double s) {
      return new Point(x * s, y * s);
    }

    public Point subtract(Point another) {
      return new Point(x - another.x, y - another.y);
    }

    public Point add(Point another) {
      return new Point(x + another.x, y + another.y);
    }

    public double dist() {
      return Math.sqrt(x * x + y * y);
    }

    public double distSq(Point another) {
      return (another.x - x) * (another.x - x) + (another.y - y) * (another.y - y);
    }

    public double dist(Point another) {
      return Math.sqrt(distSq(another));
    }

    public Point rotate(double theta) {
      double newX = x * Math.cos(theta) - y * Math.sin(theta);
      double newY = x * Math.sin(theta) + y * Math.cos(theta);
      return new Point(newX, newY);
    }

    @Override public String toString() {
      return "(" + x + ", " + y + ')';
    }
  }
}

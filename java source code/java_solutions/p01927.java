import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
  final static int INF = 1 << 28;
  final static long LNF = 1L << 60;
  final static long MOD = 1_000_000_007;
  final static double EPS = 1e-6;
  final static double GOLDEN_RATIO = (1.0 + Math.sqrt(5)) / 2.0;
  Scanner sc = new Scanner(System.in);

  public static void main(String[] args) {
    new Main().run();
  }

  class P {
    double x;
    double y;

    P(double a, double b) {
      x = a;
      y = b;
    }
  }

  class Node {
    double dist;
    int index;
  }

  P kaiten(double theta, P p) {
    return new P(p.x * Math.cos(theta) - p.y * Math.sin(theta),
        p.x * Math.sin(theta) + p.y * Math.cos(theta));
  }

  double getKakudo(P v, P w) {
    double atom = Math.atan2(v.y, v.x);
    if (atom < 0) {
      atom = 2 * Math.PI + atom;
    }
    P next = kaiten(-atom, w);
    double theta = Math.atan2(next.y, next.x);
    if (theta <= 0) {
      return -theta;
    } else {
      return 2 * Math.PI - theta;
    }
  }

  ArrayList<LineSegment> totuho(ArrayList<P> arr) {
    ArrayList<P> sub = new ArrayList<>();
    double value = Double.MAX_VALUE;
    P siten = null;
    for (P p : arr) {
      if (p.x * 100000 - p.y < value) {
        value = p.x * 100000 - p.y;
        siten = p;
      }
    }
    sub.add(siten);
    P vec = new P(0, 1);
    P ite = siten;
    for (; ; ) {
      double min = Double.MAX_VALUE;
      P next = null;
      P wec = null;
      for (P p : arr) {
        if (p == ite) {
          continue;
        }
        double dx = p.x - ite.x;
        double dy = p.y - ite.y;
        P w = new P(dx, dy);
        double theta = getKakudo(vec, w);
        if (theta < min) {
          min = theta;
          next = p;
          wec = w;
        }
//        double cos = GeomUtils.dot(vec.x, vec.y, dx, dy)
//            / (GeomUtils.abs(vec.x, vec.y) * GeomUtils.abs(dx, dy));
//        double theta = Math.acos(cos);
//        double ccw = GeomUtils.ccw(ite.x, ite.y, ite.x + vec.x, ite.y + vec.y, p.x, p.y);
//        if (ccw + EPS > 0) {
//          theta = 2 * Math.PI - theta;
//        }
//        theta += EPS;
//        theta %= 2 * Math.PI;
//        if (theta < min) {
//          min = theta;
//          next = p;
//          wec = new P(dx, dy);
//        }
      }
      if (next == siten) {
        break;
      }
      if (ite != siten) {
        double dx = siten.x - ite.x;
        double dy = siten.y - ite.y;
        P w = new P(dx, dy);
        double theta = getKakudo(vec, w);
        if (Math.abs(min - theta) < EPS) {
          break;
        }
      }
      sub.add(next);
      ite = next;
      vec = wec;
    }
    ArrayList<LineSegment> ans = new ArrayList<>();
    int n = sub.size();
    for (int i = 0; i < n; ++i) {
      P p = sub.get(i);
      P q = sub.get((i + 1) % n);
      LineSegment l = new LineSegment(p.x, p.y, q.x, q.y);
      ans.add(l);
    }
    return ans;
  }

  ArrayList<LineSegment> p2ls(double x, double y) {
    ArrayList<LineSegment> ans = new ArrayList<>();
    ans.add(new LineSegment(x, y, x, y));
    return ans;
  }

  boolean isIn(ArrayList<LineSegment> list, double x, double y) {
    if (list.size() == 1) {
      return false;
    }
    boolean flag = true;
    for (LineSegment l : list) {
      double ccw = GeomUtils.ccw(l.x1, l.y1, l.x2, l.y2, x, y);
      flag &= ccw - EPS <= 0;
    }
    return flag;
  }

  double dist(ArrayList<LineSegment> a, ArrayList<LineSegment> b) {
    double min = Double.MAX_VALUE;
    for (LineSegment ite : a) {
      if (isIn(b, ite.x1, ite.y1)
          || isIn(b, ite.x2, ite.y2)) {
        return 0;
      }
    }
    for (LineSegment ite : b) {
      if (isIn(a, ite.x1, ite.y1)
          || isIn(a, ite.x2, ite.y2)) {
        return 0;
      }
    }
    for (LineSegment ite : b) {
      for (LineSegment jte : a) {
        double d = ite.distance(jte);
        min = Math.min(min, d);
      }
    }
    return min;
  }

  void run() {
    for (; ; ) {
      int n = ni();
      if (n == 0) {
        break;
      }
      ArrayList<ArrayList<P>> list = new ArrayList<>();
      int[] hs = new int[n];
      for (int i = 0; i < n; ++i) {
        int m = ni();
        hs[i] = ni();
        ArrayList<P> arr = new ArrayList<>();
        for (int j = 0; j < m; ++j) {
          int x = ni();
          int y = ni();
          arr.add(new P(x, y));
        }
        list.add(arr);
      }
      double theta = ni() * Math.PI / 180;
      double gyokaku = ni() * Math.PI / 180;
//      debug("kage::");
      for (int i = 0; i < n; ++i) {
//        debug("\t", i);
        ArrayList<P> kage = new ArrayList<>();
        ArrayList<P> arr = list.get(i);
        double nagasa = hs[i] / Math.tan(gyokaku);
        for (P p : arr) {
          double x = p.x + nagasa * Math.cos(theta + Math.PI);
          double y = p.y + nagasa * Math.sin(theta + Math.PI);
//          debug("\t\t", x, y);
          kage.add(new P(x, y));
        }
        arr.addAll(kage);
      }
      ArrayList<ArrayList<LineSegment>> takakukeis = new ArrayList<>();
//      debug("takakukei::");
      for (ArrayList<P> arr : list) {
//        debug("\t", takakukeis.size());
        takakukeis.add(totuho(arr));
        for (LineSegment l : takakukeis.get(takakukeis.size() - 1)) {
//          debug("\t\t", l.x1, l.y1);
        }
      }
      double sx = ni();
      double sy = ni();
      double tx = ni();
      double ty = ni();
      takakukeis.add(p2ls(sx, sy));
      takakukeis.add(p2ls(tx, ty));
      PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(a -> a.dist));
      Node atom = new Node();
      atom.dist = 0;
      atom.index = n;
      queue.add(atom);
      boolean[] done = new boolean[n + 2];
      double[] ans = new double[n + 2];
//      debug("daikusutora");
      while (queue.size() > 0) {
        Node node = queue.poll();
        if (done[node.index]) {
          continue;
        }
//        debug("\t", node.index, node.dist);
        done[node.index] = true;
        ans[node.index] = node.dist;
        for (int i = 0; i < n + 2; ++i) {
          if (i == node.index) {
            continue;
          }
          ArrayList<LineSegment> arr = takakukeis.get(i);
          double dist = dist(takakukeis.get(node.index), arr);
          Node next = new Node();
          next.index = i;
          next.dist = dist + node.dist;
//          debug("\t\t", next.index, dist, next.dist);
          queue.add(next);
        }
      }
      System.out.printf("%.5f\n", ans[n + 1]);
    }
  }

  int ni() {
    return Integer.parseInt(sc.next());
  }

  long nl() {
    return sc.nextLong();
  }

  double nd() {
    return Double.parseDouble(sc.next());
  }

  void debug(Object... os) {
    System.err.println(Arrays.deepToString(os));
  }

  /**
   * from http://gihyo.jp/dev/serial/01/geometry part 6
   */
  static class Line {
    double a;
    double b;
    double c;

    /**
     * ?????¬??¢??????????????????????????´??????????????????
     *
     * @param a x????????°
     * @param b y????????°
     * @param c ?????°???
     */
    Line(double a, double b, double c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }

    /**
     * 2???(x1, y1), (x2, y2)???????????´??????????????????
     *
     * @param x1 1?????????x??§?¨?
     * @param y1 1?????????y??§?¨?
     * @param x2 2?????????x??§?¨?
     * @param y2 2?????????y??§?¨?
     * @return ??´???
     */
    static Line fromPoints(double x1, double y1, double x2, double y2) {
      double dx = x2 - x1;
      double dy = y2 - y1;
      return new Line(dy, -dx, dx * y1 - dy * x1);
    }

    /**
     * ?????????????????´?????¨??????????????????
     *
     * @param l ??´???
     * @return ?????????2??´?????????????????´??????null
     */
    Point2D getIntersectionPoint(Line l) {
      double d = a * l.b - l.a * b;
      if (d == 0.0) {
        return null;
      }
      double x = (b * l.c - l.b * c) / d;
      double y = (l.a * c - a * l.c) / d;
      return new Point2D.Double(x, y);
    }

    @Override
    public String toString() {
      return "a = " + a + ", b = " + b + ", c = " + c;
    }
  }

  static class Line1D {
    int x1;
    int x2;

    // [x1, x2) : ????????????
    Line1D(int x1, int x2) {
      this.x1 = x1;
      this.x2 = x2;
    }

    boolean isCross(Line1D l) {
      return isCross(l.x1, l.x2);
    }

    boolean isCross(int y1, int y2) {
      boolean ret = x1 < y2 && y1 < x2;
      assert ret == new LineSegment(x1, 0, x2, 0).intersects(new LineSegment(y1, 0, y2, 0));
      return ret;
    }
  }

  /**
   * from http://gihyo.jp/dev/serial/01/geometry part 6
   */
  static public class LineSegment {
    double x1;
    double y1;
    double x2;
    double y2;

    LineSegment(double x1, double y1, double x2, double y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
    }

    Line toLine() {
      return Line.fromPoints(x1, y1, x2, y2);
    }

    boolean intersects(Line l) {
      double t1 = l.a * x1 + l.b * y1 + l.c;
      double t2 = l.a * x2 + l.b * y2 + l.c;
      return t1 * t2 <= 0;
    }

    boolean intersects(LineSegment s) {
      return bothSides(s) && s.bothSides(this);
    }

    // s???????????????????????´????????????????????????????????????
    private boolean bothSides(LineSegment s) {
      double ccw1 = GeomUtils.ccw(x1, y1, s.x1, s.y1, x2, y2);
      double ccw2 = GeomUtils.ccw(x1, y1, s.x2, s.y2, x2, y2);
      if (ccw1 == 0 && ccw2 == 0) { // s??¨?????????????????´?????????????????´???
        // s???????????????1???????????????????????????????????????????????°???s??????????????¨??±?????¨?????????????????§
        // true?????????
        return internal(s.x1, s.y1) || internal(s.x2, s.y2);
      } else { // ????????\????????´???
        // CCW???????¬?????????°????????´??????true?????????
        return ccw1 * ccw2 <= 0;
      }
    }

    // (x, y)?????????????????????????????????????????????????????????
    private boolean internal(double x, double y) {
      // (x, y)????????????????????????????????????????????????????????\?????§????????°????????¨?????????
      return GeomUtils.dot(x1 - x, y1 - y, x2 - x, y2 - y) <= 0;
    }

    public Point2D getIntersectionPoint(Line l) {
      if (!intersects(l)) {
        return null; // ?????????????????´??????null?????????
      }
      return l.getIntersectionPoint(toLine());
    }

    public Point2D getIntersectionPoint(LineSegment s) {
      if (!intersects(s)) {
        return null; // ?????????????????´??????null?????????
      }
      return s.toLine().getIntersectionPoint(toLine());
    }

    /**
     * from : http://www.deqnotes.net/acmicpc/2d_geometry/lines#distance_between_line_segment_and_point
     */
    double distance(double x0, double y0) {
      // ???????????§??????
      if (GeomUtils.dot(x2 - x1, y2 - y1, x0 - x1, y0 - y1) < EPS) {
        return GeomUtils.abs(x0 - x1, y0 - y1);
      }
      if (GeomUtils.dot(x1 - x2, y1 - y2, x0 - x2, y0 - y2) < EPS) {
        return GeomUtils.abs(x0 - x2, y0 - y2);
      }
      // ??´?????¨???????????¢
      return Math.abs(GeomUtils.cross(x2 - x1, y2 - y1, x0 - x1, y0 - y1)) / GeomUtils.abs(x2 - x1, y2 - y1);
    }

    double distance(LineSegment l) {
      if (this.intersects(l)) {
        return 0.0;
      }
      double min = Double.MAX_VALUE;
      min = Math.min(min, distance(l.x1, l.y1));
      min = Math.min(min, distance(l.x2, l.y2));
      min = Math.min(min, l.distance(x1, y1));
      min = Math.min(min, l.distance(x2, y2));
      return min;
    }

    @Override
    public String toString() {
      return "(" + x1 + ", " + y1 + ") - (" + x2 + ", " + y2 + ")";
    }
  }

  /**
   * from http://gihyo.jp/dev/serial/01/geometry part 6
   */
  static class GeomUtils {
    // ??????
    static double cross(double x1, double y1, double x2, double y2) {
      return x1 * y2 - x2 * y1;
    }

    // ??????
    static double dot(double x1, double y1, double x2, double y2) {
      return x1 * x2 + y1 * y2;
    }

    // (x1, y1) -> (x2, y2) -> (x3, y3) ??¨?????????????????????????¨????????????´????????£?????????
    // ????¨????????????´???????????????????????´???????????´?????????????????????
    static double ccw(double x1, double y1, double x2, double y2,
                      double x3, double y3) {
      return cross(x2 - x1, y2 - y1, x3 - x2, y3 - y2);
    }

    static double ccw(Point2D p1, Point2D p2, Point2D p3) {
      return ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
    }

    // ?????????????????????
    static double abs(double x, double y) {
      return Math.sqrt(x * x + y * y);
    }

    // ????§???¢???????????????
    // http://www.nttpc.co.jp/technology/number_algorithm.html
    static boolean isIn(ArrayList<LineSegment> list, double x, double y) {
      int wn = 0;
      for (LineSegment l : list) {
        if (l.y1 <= y && l.y2 > y) {
          double vt = (y - l.y1) / (l.y2 - l.y1);
          if (x < l.x1 + (vt * (l.x2 - l.x1))) {
            ++wn;
          }
        }
        if (l.y1 > y && l.y2 <= y) {
          double vt = (y - l.y1) / (l.y2 - l.y1);
          if (x < (l.x1 + (vt * (l.x2 - l.x1)))) {
            --wn;
          }
        }
      }
      return wn > 0;
    }
  }
}

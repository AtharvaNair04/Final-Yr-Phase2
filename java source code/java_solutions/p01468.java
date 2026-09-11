import java.awt.Point;
import java.util.Scanner;

public class Main {
    final double EPS = 1.0e-10;
    final int INF = 1 << 28;
    final double pi = Math.pow(Math.E, 1.1447298858494);

    void run() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Line[] ls = new Line[n];
        for (int i = 0; i < n; i++) {
            Point p1 = new Point(sc.nextInt(), sc.nextInt());
            Point p2 = new Point(sc.nextInt(), sc.nextInt());
            ls[i] = new Line(p1, p2);
        }
        if (n > 5100)
            System.out.printf("%.9f\n", 0.);
        else if (n > 2550) {
            System.out.printf("%.9f\n", 1.);
        } else {
            double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    min = Math.min(min, ls[i].minDistance(ls[j]));
                }
            }
            System.out.printf("%.9f\n", min);
        }

    }

    public static void main(String[] args) {
        new Main().run();
    }
}

class Line {
    public Point p1;
    public Point p2;

    Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public double length() {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
                * (p1.y - p2.y));
    }

    public double ip(Line l) {
        Point q1 = l.p1;
        Point q2 = l.p2;
        return (p2.x - p1.x) * (q2.x - q1.x) + (p2.y - p1.y) * (q2.y - q1.y);
    }

    public double ep(Line l) {
        Point q1 = l.p1;
        Point q2 = l.p2;
        return (p2.x - p1.x) * (q2.y - q1.y) - (p2.y - p1.y) * (q2.x - q1.x);
    }

    public double disToPoint(Point q1) {
        Line p1q1 = new Line(p1, q1);
        double ip1 = p1q1.ip(this);
        double ip2 = this.ip(this);
        if (ip1 <= 0)
            return p1q1.length();
        else if (0 < ip1 && ip1 < ip2) {
            double d2 = ip1 / this.length();
            double size = p1q1.length();
            return Math.sqrt(size * size - d2 * d2);
        } else {
            Line p2q1 = new Line(p2, q1);
            return p2q1.length();
        }
    }

    public boolean isCross(Line q1q2) {
        Point q1 = q1q2.p1;
        Point q2 = q1q2.p2;
        Line p1q1 = new Line(p1, q1);
        Line p1q2 = new Line(p1, q2);
        Line q1p1 = new Line(q1, p1);
        Line q1p2 = new Line(q1, p2);
        if (this.ep(p1q1) * this.ep(p1q2) < 0
                && q1q2.ep(q1p1) * q1q2.ep(q1p2) < 0)
            return true;
        else
            return false;
    }

    public double minDistance(Line q1q2) {
        if (this.isCross(q1q2))
            return 0;
        double ans = Double.MAX_VALUE;
        Point q1 = q1q2.p1;
        Point q2 = q1q2.p2;
        ans = Math.min(ans, this.disToPoint(q1));
        ans = Math.min(ans, this.disToPoint(q2));
        ans = Math.min(ans, q1q2.disToPoint(p1));
        ans = Math.min(ans, q1q2.disToPoint(p2));
        return ans;
    }

    public String toString() {
        return p1 + "=>" + p2;
    }
}

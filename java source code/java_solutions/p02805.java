import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        FEncloseAll solver = new FEncloseAll();
        solver.solve(1, in, out);
        out.close();
    }

    static class FEncloseAll {
        double eps = 1e-9;

        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int n = in.readInt();
            List<Point> l = new ArrayList<>();
            double s = Math.sin(1);
            double c = Math.cos(1);
            for (int i = 0; i < n; i++) {
                double x = in.readDouble();
                double y = in.readDouble();
                l.add(new Point(s * x + c * y, c * x - s * y));
            }

            double ans = (int) 1e9;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    outer:
                    for (int k = 0; k < n; k++) {
                        double[] ret = f(l.get(i), l.get(j), l.get(k));
                        if (ret == null) continue;
                        Point p = new Point(ret[0], ret[1]);
                        for (int m = 0; m < n; m++) {
                            double dist = getDistance(p, l.get(m));
                            if (dist > ret[2] + eps) continue outer;
                        }
                        ans = Math.min(ans, ret[2]);
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                outer:
                for (int j = i + 1; j < n; j++) {
                    Point p = getMidpoint(l.get(i), l.get(j));
                    double r = getDistance(p, l.get(i));
                    for (int m = 0; m < n; m++) {
                        double dist = getDistance(p, l.get(m));
                        if (dist > r + eps) continue outer;
                    }
                    ans = Math.min(ans, r);
                }
            }

            out.printLine(String.format("%.10f", ans));
        }

        public double[] f(Point p1, Point p2, Point p3) {
            // p1,p2の垂直二等分線を求める
            Line l12 = getMidperpendicular(p1, p2);
            // p2,p3の垂直二等分線を求める
            Line l23 = getMidperpendicular(p2, p3);
            // 交点を求める
            Point intersection123 = getIntersection(l12, l23);
            if (intersection123 == null) {
                return null;
            }
            double[] ret = new double[3];
            ret[0] = intersection123.x;
            ret[1] = intersection123.y;
            ret[2] = getDistance(p1, intersection123);
            return ret;
        }

        public double getDistance(Point p1, Point p2) {
            Point dist = p1.difference(p2);

            return dist.getAbs();
        }

        public Point getIntersection(Line l1, Line l2) {
            double f1 = l1.p2.x - l1.p1.x;
            double g1 = l1.p2.y - l1.p1.y;
            double f2 = l2.p2.x - l2.p1.x;
            double g2 = l2.p2.y - l2.p1.y;

            double det = f2 * g1 - f1 * g2;
            if (det < eps) {
                return null;
            }

            double dx = l2.p1.x - l1.p1.x;
            double dy = l2.p1.y - l1.p1.y;
            double t1 = (f2 * dy - g2 * dx) / det;

            return new Point(l1.p1.x + f1 * t1, l1.p1.y + g1 * t1);
        }

        public Line getMidperpendicular(Point p1, Point p2) {
            Point midpoint = getMidpoint(p1, p2);
            Point dif = p1.difference(p2);
            Point gradient = new Point(dif.y, -1 * dif.x);

            Line midperpendicular = new Line(midpoint, midpoint.sum(gradient));
            return midperpendicular;
        }

        public Point getMidpoint(Point p1, Point p2) {

            Point midpoint = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            return midpoint;
        }

        class Point {
            double x;
            double y;

            Point(double x, double y) {
                this.x = x;
                this.y = y;
            }

            double getAbs() {
                return Math.sqrt(x * x + y * y);
            }

            Point difference(Point p2) {
                return new Point(x - p2.x, y - p2.y);
            }

            Point sum(Point p2) {
                return new Point(x + p2.x, y + p2.y);
            }

            public String toString() {
                return "Point [x=" + x + ", y=" + y + "]";
            }

        }

        class Line {
            Point p1;
            Point p2;
            double a;
            double b;
            double c;

            Line(Point p1, Point p2) {
                this.p1 = p1;
                this.p2 = p2;

                a = p1.y - p2.y;
                b = p2.x - p1.x;
                c = p1.x * p2.y - p2.x * p1.y;
            }

            public String toString() {
                return "Line [p1=" + p1 + ", p2=" + p2 + ", a=" + a + ", b=" + b
                        + ", c=" + c + "]";
            }

        }

    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader.SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int readInt() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public double readDouble() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            double res = 0;
            while (!isSpaceChar(c) && c != '.') {
                if (c == 'e' || c == 'E') {
                    return res * Math.pow(10, readInt());
                }
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            }
            if (c == '.') {
                c = read();
                double m = 1;
                while (!isSpaceChar(c)) {
                    if (c == 'e' || c == 'E') {
                        return res * Math.pow(10, readInt());
                    }
                    if (c < '0' || c > '9') {
                        throw new InputMismatchException();
                    }
                    m /= 10;
                    res += (c - '0') * m;
                    c = read();
                }
            }
            return res * sgn;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void print(Object... objects) {
            for (int i = 0; i < objects.length; i++) {
                if (i != 0) {
                    writer.print(' ');
                }
                writer.print(objects[i]);
            }
        }

        public void printLine(Object... objects) {
            print(objects);
            writer.println();
        }

        public void close() {
            writer.close();
        }

    }
}

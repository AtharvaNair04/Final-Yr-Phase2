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
        FMinimumBoundingBox solver = new FMinimumBoundingBox();
        solver.solve(1, in, out);
        out.close();
    }

    static class FMinimumBoundingBox {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int n = in.readInt();

            long[] x = new long[n];
            long[] y = new long[n];
            char[] d = new char[n];
            for (int i = 0; i < n; i++) {
                x[i] = in.readLong();
                y[i] = in.readLong();
                d[i] = in.readString().charAt(0);
            }

            double yMax = Long.MIN_VALUE;
            double yMin = Long.MAX_VALUE;
            double xMax = Long.MIN_VALUE;
            double xMin = Long.MAX_VALUE;
            double dMax = Long.MIN_VALUE;
            double uMin = Long.MAX_VALUE;
            double rMax = Long.MIN_VALUE;
            double lMin = Long.MAX_VALUE;
            double uMax = Long.MIN_VALUE;
            double dMin = Long.MAX_VALUE;
            double lMax = Long.MIN_VALUE;
            double rMin = Long.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (d[i] == 'R' || d[i] == 'L') {
                    yMax = Math.max(yMax, y[i]);
                    yMin = Math.min(yMin, y[i]);
                } else {
                    xMax = Math.max(xMax, x[i]);
                    xMin = Math.min(xMin, x[i]);
                }
                if (d[i] == 'D') {
                    dMax = Math.max(dMax, y[i]);
                    dMin = Math.min(dMin, y[i]);
                } else if (d[i] == 'U') {
                    uMin = Math.min(uMin, y[i]);
                    uMax = Math.max(uMax, y[i]);
                } else if (d[i] == 'R') {
                    rMax = Math.max(rMax, x[i]);
                    rMin = Math.min(rMin, x[i]);
                } else {
                    lMin = Math.min(lMin, x[i]);
                    lMax = Math.max(lMax, x[i]);
                }
            }

            List<Double> cand = new ArrayList<>();
            cand.add(.0);
            cand.add(Math.abs(dMin - yMin));
            cand.add(Math.abs(yMax - uMax));
            cand.add(Math.abs(xMax - rMax));
            cand.add(Math.abs(lMin - xMin));
            cand.add(Math.abs(dMin - yMax));
            cand.add(Math.abs(yMax - uMin));
            cand.add(Math.abs(xMax - rMin));
            cand.add(Math.abs(lMin - xMax));
            cand.add(Math.abs(dMax - yMin));
            cand.add(Math.abs(yMin - uMax));
            cand.add(Math.abs(xMin - rMax));
            cand.add(Math.abs(lMax - xMin));
            cand.add(Math.abs(dMax - yMax));
            cand.add(Math.abs(yMin - uMin));
            cand.add(Math.abs(xMin - rMin));
            cand.add(Math.abs(lMax - xMax));
            cand.add(Math.abs(dMax - uMin) / 2);
            cand.add(Math.abs(dMax - uMax) / 2);
            cand.add(Math.abs(dMin - uMin) / 2);
            cand.add(Math.abs(dMin - uMax) / 2);
            cand.add(Math.abs(rMax - lMin) / 2);
            cand.add(Math.abs(rMax - lMax) / 2);
            cand.add(Math.abs(rMin - lMin) / 2);
            cand.add(Math.abs(rMin - lMax) / 2);

            double ans = Long.MAX_VALUE;
            for (int i = 0; i < cand.size(); i++) {
                double s = cand.get(i);
                if (s > 1e10) continue;
                double xxMax = Long.MIN_VALUE;
                double xxMin = Long.MAX_VALUE;
                double yyMax = Long.MIN_VALUE;
                double yyMin = Long.MAX_VALUE;
                for (int j = 0; j < n; j++) {
                    double xx = x[j];
                    double yy = y[j];
                    if (d[j] == 'U') {
                        yy += s;
                    } else if (d[j] == 'D') {
                        yy -= s;
                    } else if (d[j] == 'L') {
                        xx -= s;
                    } else {
                        xx += s;
                    }
                    xxMax = Math.max(xxMax, xx);
                    xxMin = Math.min(xxMin, xx);
                    yyMax = Math.max(yyMax, yy);
                    yyMin = Math.min(yyMin, yy);
                }
                ans = Math.min(ans, (xxMax - xxMin) * (yyMax - yyMin));
            }

            out.printLine(String.format("%.10f", ans));
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

        public long readLong() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            long res = 0;
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

        public String readString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                if (Character.isValidCodePoint(c)) {
                    res.appendCodePoint(c);
                }
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
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

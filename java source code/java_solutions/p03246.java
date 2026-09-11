import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.io.IOException;
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
        C solver = new C();
        solver.solve(1, in, out);
        out.close();
    }

    static class C {
        public void solve(int testNumber, InputReader sc, OutputWriter out) {
            int n = sc.nextInt();
            int ar[] = new int[n];
            for (int i = 0; i < n; i++)
                ar[i] = sc.nextInt();
            if (n == 2) {
                if (ar[0] == ar[1])
                    out.println(1);
                else
                    out.println(0);
                return;
            }
            HashMap<Integer, Integer> map1 = new HashMap<>();
            HashMap<Integer, Integer> map2 = new HashMap<>();
            int max1 = 0, max2 = 0, max3 = 0, max4 = 0;
            int m1 = 0;
            int m2 = 0;
            for (int i = 0; i < n; i += 2) {
                if (map1.containsKey(ar[i]))
                    map1.put(ar[i], map1.get(ar[i]) + 1);
                else
                    map1.put(ar[i], 1);
                int m = map1.get(ar[i]);
                if (m > m1) {
                    m1 = m;
                    max1 = ar[i];
                }
            }
            for (int i = 1; i < n; i += 2) {
                if (map2.containsKey(ar[i]))
                    map2.put(ar[i], map2.get(ar[i]) + 1);
                else
                    map2.put(ar[i], 1);
                int m = map2.get(ar[i]);
                if (m > m2) {
                    m2 = m;
                    max3 = ar[i];
                }
            }
            max2 = -1;
            max4 = -1;
            int ma = 0;
            int mb = 0;
            for (int i : map1.keySet()) {
                if (i == max1)
                    continue;
                int m = map1.get(i);
                if (m > ma) {
                    ma = m;
                    max2 = i;
                }
            }
            if (max2 == -1)
                max2 = max1;
            for (int i : map2.keySet()) {
                if (i == max3)
                    continue;
                int m = map2.get(i);
                if (m > mb) {
                    mb = m;
                    max4 = i;
                }
            }
            if (max4 == -1)
                max4 = max3;
            if (max1 != max3) {
                if (max1 == max2 && max3 != max4)
                    out.println(n / 2 - map2.get(max3));
                else if (max1 == max2 && max3 == max4)
                    out.println(0);
                else if (max3 == max4 && max1 != max2)
                    out.println(n / 2 - map1.get(max1));
                else {
                    int res1 = n / 2 - map1.get(max1);
                    int res2 = n / 2 - map2.get(max3);
                    out.println(res1 + res2);
                }
            } else {
                if (max1 == max2 && max3 == max4)
                    out.println(n / 2);
                else if (max3 == max4 && max1 != max2) {
                    out.println(n / 2 - map1.get(max2));
                } else if (max1 == max2) {
                    out.println(n / 2 - map2.get(max4));
                } else {
                    int res1 = n / 2 - map1.get(max1);
                    int res2 = n / 2 - map2.get(max4);
                    int resa = res1 + res2;
                    int res3 = n / 2 - map1.get(max2);
                    int res4 = n / 2 - map2.get(max3);
                    int resb = res3 + res4;
                    out.println(Math.min(resa, resb));
                }
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

        public int nextInt() {
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

        public void close() {
            writer.close();
        }

        public void println(int i) {
            writer.println(i);
        }

    }
}

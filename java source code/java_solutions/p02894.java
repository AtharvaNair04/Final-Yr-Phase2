import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        FastScanner in = new FastScanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        D_Incenters solver = new D_Incenters();
        solver.solve(1, in, out);
        out.close();
    }

    static class D_Incenters {
        public void solve(int testNumber, FastScanner in, PrintWriter out) {
            int n = in.nextInt();
            int L = in.nextInt();
            double[] t = new double[n];
            for (int i = 0; i < n; i++) {
                t[i] = in.nextDouble();
            }

            double[] cosTable = new double[n];
            double[] sinTable = new double[n];
            for (int i = 0; i < n; i++) {
                cosTable[i] = Math.cos(Math.PI * t[i] / L);
                sinTable[i] = Math.sin(Math.PI * t[i] / L);
            }

            double x = 0;
            double y = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    outer:
                    for (int si = -1; si <= 1; si += 2) {
                        for (int sj = -1; sj <= 1; sj += 2) {
                            double xu = si * cosTable[i];
                            double yu = si * sinTable[i];
                            double xv = sj * cosTable[j];
                            double yv = sj * sinTable[j];

                            double xp = xu * xv - yu * yv;
                            double yp = xu * yv + xv * yu;

                            if (cross(xu, yu, xp, yp) >= 0 && cross(xp, yp, xv, yv) >= 0) {
                                int am = j - i - 1;
                                x += xp * am;
                                x -= xp * (n - 2 - am);
                                y += yp * am;
                                y -= yp * (n - 2 - am);
                                break outer;
                            }
                        }
                    }
                }
            }
            x /= (double) n * (n - 1) * (n - 2) / 6.0;
            y /= (double) n * (n - 1) * (n - 2) / 6.0;
            out.printf("%.15f %.15f", x, y);
        }

        private double cross(double x1, double y1, double x2, double y2) {
            return x1 * y2 - x2 * y1;
        }

    }

    static class FastScanner {
        private BufferedReader in;
        private StringTokenizer st;

        public FastScanner(InputStream stream) {
            in = new BufferedReader(new InputStreamReader(stream));
        }

        public String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(in.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

    }
}

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
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
        DComplexity solver = new DComplexity();
        solver.solve(1, in, out);
        out.close();
    }

    static class DComplexity {
        public void solve(int testNumber, FastScanner in, PrintWriter out) {
            int n = in.nextInt(), m = in.nextInt();
            char[][] c = new char[n][m];
            for (int i = 0; i < n; i++) {
                c[i] = in.next().toCharArray();
            }

            int MAX_ANS = 20;

            short[][][] dpRight = new short[n][m][n];
            short[][][] dpDown = new short[n][m][m];

            int[][] downDiff = new int[n][m], rightDiff = new int[n][m];
            for (int i = n - 1; i >= 0; i--) {
                for (int j = m - 1; j >= 0; j--) {
                    if (i == n - 1 || c[i][j] != c[i + 1][j]) {
                        downDiff[i][j] = i + 1;
                    } else {
                        downDiff[i][j] = downDiff[i + 1][j];
                    }
                    if (j == m - 1 || c[i][j] != c[i][j + 1]) {
                        rightDiff[i][j] = j + 1;
                    } else {
                        rightDiff[i][j] = rightDiff[i][j + 1];
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int minDiff = m;
                    for (int k = i; k < n; k++) {
                        minDiff = Math.min(minDiff, rightDiff[k][j]);
                        if (c[k][j] != c[i][j]) {
                            minDiff = j;
                        }
                        dpRight[i][j][k] = (short) minDiff;
                    }
                    minDiff = n;
                    for (int k = j; k < m; k++) {
                        minDiff = Math.min(minDiff, downDiff[i][k]);
                        if (c[i][k] != c[i][j]) {
                            minDiff = i;
                        }
                        dpDown[i][j][k] = (short) minDiff;
                    }
                }
            }
            if (dpRight[0][0][n - 1] == m) {
                out.println(0);
                return;
            }

            for (int ans = 1; ans <= MAX_ANS; ans++) {
                for (int i1 = 0; i1 < n; i1++) {
                    for (int i2 = i1; i2 < n; i2++) {
                        for (int k = 0; k < m; k++) {
                            int next = dpRight[i1][k][i2];
                            if (next < m) {
                                next = dpRight[i1][next][i2];
                            }
                            dpRight[i1][k][i2] = (short) next;
                        }
                    }
                }
                for (int j1 = 0; j1 < m; j1++) {
                    for (int j2 = j1; j2 < m; j2++) {
                        for (int k = 0; k < n; k++) {
                            int next = dpDown[k][j1][j2];
                            if (next < n) {
                                next = dpDown[next][j1][j2];
                            }
                            dpDown[k][j1][j2] = (short) next;
                        }
                    }
                }
                for (int i1 = 0; i1 < n; i1++) {
                    for (int i2 = i1; i2 < n; i2++) {
                        for (int k = 0; k < m; k++) {
                            int rightK = dpRight[i1][k][i2] - 1;
                            if (rightK > 0)
                                dpDown[i1][k][rightK] = (short) Math.max(dpDown[i1][k][rightK], i2 + 1);
                        }
                    }
                }

                for (int j1 = 0; j1 < m; j1++) {
                    for (int j2 = j1; j2 < m; j2++) {
                        for (int k = 0; k < n; k++) {
                            int downK = dpDown[k][j1][j2] - 1;
                            if (downK > 0)
                                dpRight[k][j1][downK] = (short) Math.max(dpRight[k][j1][downK], j2 + 1);
                        }
                    }
                }

                for (int len = n; len >= 1; len--) {
                    for (int i1 = 0, i2 = len - 1; i2 < n; i1++, i2++) {
                        for (int k = 0; k < m; k++) {
                            if (i2 + 1 < n) {
                                dpRight[i1][k][i2] = (short) Math.max(dpRight[i1][k][i2], dpRight[i1][k][i2 + 1]);
                            }
                            if (i1 - 1 >= 0) {
                                dpRight[i1][k][i2] = (short) Math.max(dpRight[i1][k][i2], dpRight[i1 - 1][k][i2]);
                            }
                        }
                    }
                }
                for (int len = m; len >= 1; len--) {
                    for (int j1 = 0, j2 = len - 1; j2 < m; j1++, j2++) {
                        for (int k = 0; k < n; k++) {
                            if (j2 + 1 < m) {
                                dpDown[k][j1][j2] = (short) Math.max(dpDown[k][j1][j2], dpDown[k][j1][j2 + 1]);
                            }
                            if (j1 - 1 >= 0) {
                                dpDown[k][j1][j2] = (short) Math.max(dpDown[k][j1][j2], dpDown[k][j1 - 1][j2]);
                            }
                        }
                    }
                }

                if (dpRight[0][0][n - 1] == m) {
                    out.println(ans);
                    return;
                }
            }
            out.println(MAX_ANS + 1);
        }

    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner(InputStream in) {
            br = new BufferedReader(new InputStreamReader(in));
        }

        public FastScanner(String fileName) {
            try {
                br = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public String next() {
            while (st == null || !st.hasMoreElements()) {
                String line = null;
                try {
                    line = br.readLine();
                } catch (IOException e) {
                }
                st = new StringTokenizer(line);
            }
            return st.nextToken();
        }

    }
}

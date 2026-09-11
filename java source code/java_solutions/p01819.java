import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        WhereIsTheBoundary solver = new WhereIsTheBoundary();
        solver.solve(1, in, out);
        out.close();
    }

    static class WhereIsTheBoundary {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            int N = in.nextInt();
            int M = in.nextInt();
            String[] d = new String[M];
            for (int i = 0; i < M; i++) {
                d[i] = in.next();
            }
            int[] wsum = new int[N + 1];
            int[] esum = new int[N + 1];
            boolean west = false, east = false;
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    if (d[i].charAt(j) == 'E') {
                        esum[j + 1]++;
                        east = true;
                    } else {
                        wsum[j + 1]++;
                        west = true;
                    }
                }
            }
            if (!east) {
                out.println(N + " " + (N + 1));
                return;
            }
            if (!west) {
                out.println(0 + " " + 1);
                return;
            }
            for (int i = 0; i < N; i++) {
                wsum[i + 1] += wsum[i];
                esum[i + 1] += esum[i];
            }
            int l = -1;
            int diff = (int) 1e9;
            for (int i = 0; i < N - 1; i++) {
                if (esum[i + 1] + wsum[N] - wsum[i + 1] < diff) {
                    l = i + 1;
                    diff = esum[i + 1] + wsum[N] - wsum[i + 1];
                }
            }
            if (wsum[N] <= diff) {
                l = 0;
                diff = wsum[N];
            }
            if (esum[N] < diff) {
                l = N;
            }
            out.println(l + " " + (l + 1));
        }

    }
}

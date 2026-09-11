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
        B solver = new B();
        solver.solve(1, in, out);
        out.close();
    }

    static class B {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            // 入力
            int n = Integer.parseInt(in.next());
            int a = Integer.parseInt(in.next());
            int b = Integer.parseInt(in.next());
            int[] p = new int[n];
            for (int i = 0; i < n; i++) {
                p[i] = Integer.parseInt(in.next());
            }
            int cntlessa = 0;
            int cnteven = 0;
            int cntmoreb = 0;
            for (int i = 0; i < n; i++) {
                if (p[i] <= a) {
                    cntlessa++;
                } else if ((a + 1) <= p[i] && p[i] <= b) {
                    cnteven++;
                } else {
                    cntmoreb++;
                }
            }

            // 出力
            out.println(Math.min(cntlessa, Math.min(cnteven, cntmoreb)));

        }

    }
}

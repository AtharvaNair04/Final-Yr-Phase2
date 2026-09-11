import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.InputMismatchException;
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
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskD solver = new TaskD();
        solver.solve(1, in, out);
        out.close();
    }
 
    static class TaskD {
        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt();
            long[] x = new long[n];
            long[] y = new long[n];
            for (int i = 0; i < n; i++) {
                x[i] = in.nextLong();
                y[i] = in.nextLong();
            }
 
            boolean isAllOdd = true;
            boolean isAllEven = true;
            for (int i = 0; i < n; i++) {
                if ((x[i] + y[i]) % 2 == 0) isAllOdd = false;
                else isAllEven = false;
            }
 
            if (!(isAllOdd ^ isAllEven)) {
                out.println(-1);
                return;
            }
 
            if (isAllEven) {
                for (int i = 0; i < n; i++) {
                    x[i] -= 1;
                }
            }
 
            for (int i = 0; i < n; i++) {
                long tmpX = x[i];
                long tmpY = y[i];
                x[i] = tmpX - tmpY;
                y[i] = tmpX + tmpY;
            }
 
            if (isAllEven) {
                out.println(38);
                out.print("1 ");
            } else {
                out.println(37);
            }
            for (int i = 0; i < 37; i++) {
                if (i > 0) out.print(" ");
                out.print(1L << 36 - i);
            }
            out.println();
 
            for (int i = 0; i < n; i++) {
                if (isAllEven) out.print("R");
 
                for (int j = 36; j >= 0; j--) {
                    if (x[i] > 0) {
                        if (y[i] > 0) {
                            out.print("R");
                            y[i] -= 1L << j;
                        } else {
                            out.print("D");
                            y[i] += 1L << j;
                        }
                        x[i] -= 1L << j;
                    } else {
                        if (y[i] > 0) {
                            out.print("U");
                            y[i] -= 1L << j;
                        } else {
                            out.print("L");
                            y[i] += 1L << j;
                        }
                        x[i] += 1L << j;
                    }
                }
 
                out.println();
            }
        }
 
    }
 
    static class InputReader {
        private BufferedReader br;
        private StringTokenizer st;
 
        public InputReader(InputStream inputStream) {
            br = new BufferedReader(new InputStreamReader(inputStream));
            st = new StringTokenizer("");
        }
 
        public String nextString() {
            while (!st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine(), " ");
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
            }
            return st.nextToken();
        }
 
        public int nextInt() {
            return Integer.parseInt(nextString());
        }
 
        public long nextLong() {
            return Long.parseLong(nextString());
        }
 
    }
}

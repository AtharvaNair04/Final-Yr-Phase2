import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author ankur
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        atcoderdec solver = new atcoderdec();
        solver.solve(1, in, out);
        out.close();
    }

    static class atcoderdec {
        public void solve(int testNumber, InputReader in, PrintWriter out) {
//        System.out.println(1);
            int n = in.nextInt();
            int k = in.nextInt();
            int ar[] = in.nextIntArray(n);
            int amx = in.max(ar);
            //   Arrays.sort(ar);
            int c[] = new int[amx + 1];
            HashMap<Integer, Integer> hm = new HashMap<>();
            for (int i = 0; i < ar.length; i++) {
                c[ar[i]]++;
                hm.put(ar[i], 1);
            }
            if (hm.size() <= k) {
                System.out.println(0);
                return;
            }
            int ans = 0;
            Arrays.sort(c);
            int remove = hm.size() - k;
            for (int i = 0; i < c.length; i++) {
                if (remove == 0)
                    break;
                if (c[i] != 0) {
                    ans += c[i];
                    remove--;
                }
            }
            System.out.println(ans);
            //System.out.println(Math.max(ctr-k,0));
        }

    }

    static class InputReader {
        private final InputStream stream;
        private final byte[] buf = new byte[8192];
        private int curChar;
        private int snumChars;

        public InputReader(InputStream st) {
            this.stream = st;
        }

        public int read() {
            if (snumChars == -1)
                throw new InputMismatchException();
            if (curChar >= snumChars) {
                curChar = 0;
                try {
                    snumChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (snumChars <= 0)
                    return -1;
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
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public int[] nextIntArray(int n) {
            int a[] = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = nextInt();
            }
            return a;
        }

        public int max(int ar[]) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < ar.length; i++) {
                max = Math.max(max, ar[i]);
            }
            return max;
        }

        public boolean isSpaceChar(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

    }
}

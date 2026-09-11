import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author Pavel Mavrin
 */
public class Main {

    private void solve() throws IOException {
        int n = nextInt();
        long l = nextInt();
        long t = nextInt();
        long[] x = new long[n];
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextInt();
            w[i] = nextInt() == 1 ? 1 : -1;
        }
        long[] y = new long[n];
        long q = 0;
        for (int i = 0; i < n; i++) {
            int ww = w[i] - w[0];
            y[i] = x[i] + t * ww;
            if (y[i] >= l) {
                q -= y[i] / l;
                y[i] %= l;
            }
            if (y[i] < 0) {
                q += ((-y[i] - 1) / l) + 1;
                y[i] %= l;
                if (y[i] < 0) y[i] += l;
            }
        }
        q %= n;
        if (q < 0) q += n;
        Arrays.sort(y);

        long[] res = new long[n];
        for (int i = 0; i < n; i++) {
            int k = (int) ((i + q) % n);
            res[k] = y[i] + w[0] * t;
            res[k] %= l;
            if (res[k] < 0) res[k] += l;
        }
        for (int i = 0; i < n; i++) {
            out.println(res[i]);
        }
    }

    // ------------------

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st;
    PrintWriter out = new PrintWriter(System.out);

    String next() throws IOException {
        while (st == null || !st.hasMoreTokens()) {
            st = new StringTokenizer(br.readLine());
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        solve();
        out.close();
    }

}

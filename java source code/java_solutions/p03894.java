import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    static BufferedReader in;
    static PrintWriter out;
    static StringTokenizer tok;

    void solve() throws IOException {
        int n = ni();
        int q = ni();
        int[] a = new int[q];
        int[] b = new int[q];
        for (int i = 0; i < q; i++) {
            a[i] = ni() - 1;
            b[i] = ni() - 1;
        }

        boolean[] can = new boolean[n];
        can[0] = can[1] = true;

        int idx = 0;

        for (int i = 0; i < q; i++) {
            if (a[i] == idx) idx = b[i];
            else if (b[i] == idx) idx = a[i];

            boolean tmp = can[a[i]];
            can[a[i]] = can[b[i]];
            can[b[i]] = tmp;

            if (idx + 1 <= n - 1) can[idx + 1] = true;
            if (idx - 1 >= 0) can[idx - 1] = true;
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (can[i]) {
                ans++;
            }
        }

        out.println(ans);
    }

    String ns() throws IOException {
        while (!tok.hasMoreTokens()) {
            tok = new StringTokenizer(in.readLine(), " ");
        }
        return tok.nextToken();
    }

    int ni() throws IOException {
        return Integer.parseInt(ns());
    }

    long nl() throws IOException {
        return Long.parseLong(ns());
    }

    double nd() throws IOException {
        return Double.parseDouble(ns());
    }

    String[] nsa(int n) throws IOException {
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = ns();
        }
        return res;
    }

    int[] nia(int n) throws IOException {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = ni();
        }
        return res;
    }

    long[] nla(int n) throws IOException {
        long[] res = new long[n];
        for (int i = 0; i < n; i++) {
            res[i] = nl();
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        tok = new StringTokenizer("");
        Main main = new Main();
        main.solve();
        out.close();
    }
}

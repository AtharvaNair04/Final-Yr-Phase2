import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static final void main(String args[]) throws Exception {
        new Main().solve();
    }

    private static int max = 6;

    void solve() {
        try (Scanner in = new Scanner(System.in)) {
            int n = in.nextInt();
            int x[] = new int[n];
            for (int i = 0; i < n; ++i) {
                x[i] = in.nextInt();
            }
            long mod = 1000000007L;
            long a = 1;
            for (int i = 0, j = 1; i < n - 1; ++i) {
                while (j + 1 < n && x[j] - (j - i) > (j - i)) ++j;
                a = (a * (j - i + 1)) % mod;
            }
            System.out.println(a);
        }
    }

    void tr(Object... o) {
        System.err.println(Arrays.deepToString(o));
    }
}

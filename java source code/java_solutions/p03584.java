import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Main {
    long k;
    int n;
    long[] as;
    long[] bs;

    public static void main(String[] args) {
        Main m = new Main();
        m.read();
        m.solve();
    }

    private void read() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        k = sc.nextLong();
        as = new long[n];
        bs = new long[n];
        for (int i = 0; i < n; i++) {
            as[i] = sc.nextLong();
            bs[i] = sc.nextLong();
        }
    }

    private void solve() {
        long ans = 0L;
        int length = Long.bitCount(k);
        for (int i = 0; i < n; i++) {
            if ((as[i] & k) == as[i])
                ans += bs[i];
        }
        for (int i = 0; i <= 30; i++) {
            long bit = 1 << i;
            if ((k &  bit) != 0) {
                final long key = (bit ^ k) | (bit - 1);
                long tmp = 0L;
                for (int j = 0; j < n; j++) {
                    if (as[j] == (as[j] & key) || as[j] == (as[j] & (bit ^ k)))
                        tmp += bs[j];
                }
                ans = Math.max(tmp, ans);
            }
        }
        System.out.println(ans);
    }
}

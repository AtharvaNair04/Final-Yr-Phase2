import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            new Main().solve(in);
        }
    }

    private void solve(Scanner in) {
        int N = in.nextInt(), M = in.nextInt();
        Fenwick F = new Fenwick(N);
        for (int i = 0; i < M; i++) {
            int a = in.nextInt(), b = in.nextInt();
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            F.add(a, +1);
            F.add(b, -1);
        }
//        System.out.println(F);

        boolean yes = true;
        for (int i = 1; i < N; i++) {
            int accum = F.accum(i);
//            System.out.println(accum);
            if (accum % 2 > 0) {
                yes = false;
                break;
            }
        }
        System.out.println(yes ? "YES" : "NO");
    }

    private static class Fenwick {
        private final int[] fenwick;

        public Fenwick(int n) {
            fenwick = new int[n + 1];
        }

        public int accum(int i) {
            int sum = 0;
            for (; i > 0; i -= i & -i) {
                sum += fenwick[i];
            }
            return sum;
        }

        public int sum(int begin, int end) {
            return accum(end) - accum(begin - 1);
        }

        public void add(int i, int a) {
            for (; i < fenwick.length; i += i & -i) {
                fenwick[i] += a;
            }
        }

        @Override
        public String toString() {
            int N = fenwick.length;
            long[] raw = new long[N];
            long[] acc = new long[N];
            for (int i = 1; i < N; i++) {
                acc[i] = accum(i);
                raw[i] = acc[i] - acc[i - 1];
            }
            StringBuilder s = new StringBuilder();
            s.append("Fenwick [\n");
            s.append(" fenwick=" + Arrays.toString(fenwick) + "\n");
            s.append(" accum  =" + Arrays.toString(acc) + "\n");
            s.append(" raw    =" + Arrays.toString(raw) + "\n]");
            return s.toString();
        }
    }
}

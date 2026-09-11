import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            solve(in);
        }
    }

    private static void solve(Scanner in) {
        int N = in.nextInt();
        int x = in.nextInt();
        int[] a = new int[N];
        for (int i = 0; i < N; i++) {
            a[i] = in.nextInt();
        }
        // b[k(magic)][i]
        long[][] b = new long[N][N];
        long ans = 0;

        // no magic
        for (int i = 0; i < N; i++) {
            b[0][i] = a[i];
            ans += b[0][i];
        }

        for (int k = 1; k < N; k++) {
            long sumb = 0;
            for (int i = 0; i < N; i++) {
                b[k][i] = Math.min(b[k - 1][i == 0 ? N - 1 : i - 1], a[i]);
                sumb += b[k][i];
            }
            ans = Math.min(ans, (long) k * x + sumb);
        }
        System.out.println(ans);
    }
}

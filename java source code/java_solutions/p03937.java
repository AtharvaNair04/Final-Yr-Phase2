import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            solve(in);
        }
    }

    private static void solve(Scanner in) {
        int H = in.nextInt();
        int W = in.nextInt();
        char[][] a = new char[H + 2][W + 2];
        for (int i = 0; i < H + 2; i++) {
            if (i == 0 || i == H + 1) {
                Arrays.fill(a[i], '.');
            } else {
                String row = "." + in.next() + ".";
                a[i] = row.toCharArray();
            }
        }
        // for (int i = 0; i < H + 2; i++) {
        // System.out.println(Arrays.toString(a[i]));
        // }
        boolean good = true;
        for (int i = 1; i < H + 1; i++) {
            if (!good) {
                break;
            }
            for (int j = 1; j < W + 1; j++) {
                if (!good) {
                    break;
                }
                if (i == H && j == W) {
                    if (SH(a, i - 1, j) && SH(a, i, j - 1)) {
                        good = false;
                    } else {
                        System.out.println("Possible");
                        return;
                    }
                }
                if (SH(a, i, j) == false) {
                    continue;
                }
                if (SH(a, i + 1, j) && SH(a, i, j + 1)) {
                    good = false;
                }
                if (SH(a, i - 1, j) && SH(a, i + 1, j)) {
                    if (SH(a, i, j - 1) || SH(a, i, j + 1)) {
                        good = false;
                    }
                }
                if (SH(a, i, j - 1) && SH(a, i, j + 1)) {
                    if (SH(a, i - 1, j) || SH(a, i + 1, j)) {
                        good = false;
                    }
                }
            }
        }
        System.out.println("Impossible");
    }

    static boolean SH(char[][] a, int i, int j) {
        return a[i][j] == SH;
    }

    static final char SH = '#';
}

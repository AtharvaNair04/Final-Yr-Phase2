import java.util.Arrays;
import java.util.Scanner;

class Main {

    public static int R;
    public static int G;
    public static int B;
    public static int N;

    public static int upper_bound(long x, long[] RG) {
        int ok = RG.length;
        int ng = -1;
        while (ok - ng > 1) {
            int mid = (ok + ng) / 2;
            if (RG[mid] <= x) {
                ng = mid;
            } else {
                ok = mid;
            }
        }
        return ok;
    }

    public static int lower_bound(long x, long[] RG) {
        int ok = -1;
        int ng = RG.length;
        while (ng - ok > 1) {
            int mid = (ok + ng) / 2;
            if (RG[mid] < x) {
                ok = mid;
            } else {
                ng = mid;
            }
        }
        return ok;

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        R = scanner.nextInt();
        G = scanner.nextInt();
        B = scanner.nextInt();
        N = scanner.nextInt();
        long[] RG = new long[((N + R - 1) / R + 1) * ((N + G - 1) / G + 1)];
        int idx = 0;
        for (int r = 0; r <= (N + R - 1) / R; r++) {
            for (int g = 0; g <= (N + G - 1) / G; g++) {
                RG[idx++] = R * r + G * g;
            }
        }
        Arrays.sort(RG);
        long cnt = 0;
        for (int b = 0; b <= (N + B - 1) / B; b++) {
            cnt += upper_bound(N - B * b, RG) - lower_bound(N - B * b, RG) - 1;
        }
        System.out.println(cnt);
    }

}

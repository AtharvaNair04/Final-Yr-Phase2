import java.util.*;

class Main {
    public static void main(String... args) {
        int N = IN.nextInt();
        int M = IN.nextInt();
        int[] a = new int[M];
        int[] b = new int[M];
        ArrayList<Integer> xr = new ArrayList<Integer>();
        for (int i = 0; i < M; i++) {
            a[i] = IN.nextInt();
            b[i] = IN.nextInt();
            xr.add(i);
        }
        xr.sort((i, j) -> {
            int tmp = a[i] - a[j];
            return tmp != 0 ? tmp : b[i] - b[j];
        });
        int sunk = N;
        int ans = 0;
        int left = -1;
        int rMin = -1;
        for (int x : xr) {
            if (left != a[x]) {
                left = a[x];
                rMin = b[x];
            } else {
                continue;
            }
            if (sunk <= left) {
                sunk = rMin;
                ans++;
            } else {
                if (rMin < sunk) {
                    sunk = rMin;
                }
            }
        }
        puts(ans + 1);
    }
    static final Scanner IN = new Scanner(System.in);
    static <T> void puts(T arg) { System.out.println(arg); }
}

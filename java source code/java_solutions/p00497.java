import java.util.*;

class Main {
    static int nail[][];
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        nail = new int[n+1][];
        for (int i = 1; i <= n; i++) {
            nail[i] = new int[i+1];
        }
        int ct = 0;
        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int x = sc.nextInt();
            ct += count(a, b, x);
        }
        System.out.println(ct);
    }

    static int count(int a, int b, int x) {
        if (x < 0) return 0;
        int y = nail[a][b];
        if (y > x) return 0;
        int c = 0;
        if (y == 0) c++;
        nail[a][b] = x + 1;
        c += count(a + 1, b, x - 1);
        c += count(a + 1, b + 1, x - 1);
        return c;
    }
}

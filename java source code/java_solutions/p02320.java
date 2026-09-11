import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    int n, w;
    int[] vs;
    int[] ws;
    int[] ms;
    int[][] dp;
    ArrayList<ArrayDeque<Pos>> ds;

    public static void main(String[] args) {
        Main m = new Main();
        m.read();
        m.solve();
    }

    private void read() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        w = sc.nextInt();
        vs = new int[n];
        ws = new int[n];
        ms = new int[n];
        for (int i = 0; i < n; i++) {
            vs[i] = sc.nextInt();
            ws[i] = sc.nextInt();
            ms[i] = sc.nextInt();
        }
        dp = new int[n+1][w+1];
        ds = new ArrayList<ArrayDeque<Pos>>(1001);
        for (int i = 0; i < 1001; i++)
            ds.add(new ArrayDeque<Pos>());
    }

    private void solve() {

        for (int i = 1; i < n + 1; i++) {
            int v = vs[i-1];
            int m = ms[i-1];
            int w = ws[i-1];
//            for (int j = 0; j < w; j++) {
//                for (int k = 0; k <= m; k++) {
//                    if (j + w * k <= this.w+1)
//                        ds.get(j).add(new Pos(i-1, j+w*k));
//                    if (j >= w)
//                        dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-w] + v);
//                }
//            }
            for (int j = 0; j < this.w + 1; j++) {
                add(i, j);
                Pos p = ds.get(j % w).getFirst();
                dp[i][j] = dp[p.i][p.j] + v * ((j - p.j) / w);
            }
            for (int j = 0; j < w; j++)
                ds.get(j).clear();
//            for (int j = 0; j < this.w + 1; j++)
//                System.out.printf("%3d ", dp[i][j]);
//            System.out.println();
        }
        int ans = Integer.MIN_VALUE;
        for (int j = 0; j <= w; j++) {
            ans = Math.max(ans, dp[n][j]);
        }
        System.out.println(ans);
    }

    private void add(int i, int j) {
        Pos newP = new Pos(i-1, j);
        ArrayDeque<Pos> deque = ds.get(j%ws[i-1]);
        while (!deque.isEmpty()) {
            Pos p = deque.getLast();
            if (j < ws[i-1] ||
                    dp[p.i][p.j] + vs[i-1] * ((j - p.j) / ws[i-1]) >= dp[i-1][j])
                break;
            deque.removeLast();
        }
        deque.addLast(newP);
        if (j - deque.getFirst().j > ms[i-1] * ws[i-1])
            deque.removeFirst();
    }

    private class Pos {
        int i, j;
        Pos (int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}

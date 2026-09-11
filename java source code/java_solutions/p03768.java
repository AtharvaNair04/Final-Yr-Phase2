import java.io.*;
import java.util.*;

public class Main {
    //-------------------------------------------------------------//
    public static final void main(String[] args) {
        new Main().solve();
    }
    //-------------------------------------------------------------//
    private final Scanner sc = new Scanner(System.in);

    int N, M, Q;
    boolean[][] memo;
    int[] color;
    ArrayList<ArrayList<Integer>> g = new ArrayList<ArrayList<Integer>>();
    void dfs(int v, int d, int c) {
        if (d < 0 || memo[d][v]) return;
        if (color[v] == 0) color[v] = c;
        memo[d][v] = true;
        dfs(v, d - 1, c);
        for (int n : g.get(v)) {
            dfs(n, d - 1, c);
        }
    }

    void solve() {
        N = sc.nextInt();
        M = sc.nextInt();
        for (int i = 0; i < N; i++) g.add(new ArrayList<Integer>());
        for (int i = 0; i < M; i++) {
            int a = sc.nextInt() - 1;
            int b = sc.nextInt() - 1;
            g.get(a).add(b);
            g.get(b).add(a);
        }
        Q = sc.nextInt();
        int[] v = new int[Q];
        int[] d = new int[Q];
        int[] c = new int[Q];
        for (int i = 0; i < Q; i++) {
            v[i] = sc.nextInt() - 1;
            d[i] = sc.nextInt();
            c[i] = sc.nextInt();
        }
        memo = new boolean[11][N];
        color = new int[N];
        Arrays.fill(color, 0);
        for (int i = Q - 1; i >= 0; i--) {
            dfs(v[i], d[i], c[i]);
        }
        PrintWriter w = new PrintWriter(System.out);
        for (int i = 0; i < N; i++) {
            w.println(color[i]);
        }
        w.flush();
    }
}

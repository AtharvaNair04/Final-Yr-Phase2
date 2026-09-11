import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    Scanner sc = new Scanner(System.in);
    int N;
    List<List<Integer>> G;
    List<Boolean> visited;
    int INF = Integer.MAX_VALUE / 2;

    int dfs(int v, int p) {
        if (v == 1) {
            return 0;
        }

        if (visited.get(v)) {
            return INF;
        }

        visited.set(v, true);

        int ret = INF;
        for (int nv : G.get(v)) {
            if (nv != p) {
                int tmp = dfs(nv, v) + 1;
                if (ret > tmp) {
                    ret = tmp;
                }
            }
        }

        return ret;
    }


    void solve() {
        N = sc.nextInt();
        G = new ArrayList<>(N);
        visited = new ArrayList<>(N);
        for (int i = 0; i < N; ++i) {
            G.add(new ArrayList<>());
            visited.add(false);
//            G.set(i , new ArrayList<>());
        }

        for (int i = 0; i < N; ++i) {
            int a = sc.nextInt();
            a--;
//            G.get(a).add(i);
            G.get(i).add(a);
        }

        int ans = dfs(0, -1);
        if (ans == INF) {
            System.out.println("-1");
            return;
        }
        System.out.println(ans);


    }

    public static void main(String[] args) {
        new Main().solve();
    }
}

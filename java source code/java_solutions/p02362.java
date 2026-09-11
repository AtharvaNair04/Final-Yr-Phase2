import java.util.Arrays;
import java.util.Scanner;

// bellman-ford ????????????????????????
public class Main {

    static int dist[][];
    static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int E = sc.nextInt();
        int s = sc.nextInt();

        Edge[] list = new Edge[E];

        for (int i = 0; i < E; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int f = sc.nextInt();
            list[i] = new Edge(a, b, f);
            // ????????°??????
            // list[i] = new Edge(b, a, f);
        }

        int[] d = new int[N];
        Arrays.fill(d, INF);
        d[s] = 0;

        for (int i = 0; i < E; i++) {
            for (int j = 0; j < E; j++) {
                Edge e = list[j];
                if (d[e.from] != INF && d[e.to] > d[e.from] + e.cost) {
                    d[e.to] = d[e.from] + e.cost;
                    if (i == E - 1) {
                        System.out.println("NEGATIVE CYCLE");
                        return;
                    }
                }
            }
        }

        for (int i = 0; i < N; i++) {
            System.out.println(d[i] == INF ? "INF" : d[i]);
        }
    }

    static class Edge implements Comparable<Edge> {
        public int from;
        public int to;
        public int cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }


        @Override
        public int compareTo(Edge o) {
            return this.cost - o.cost;
        }
    }

    public static boolean hasNegativeLoopWF(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            if (a[i][i] < 0) {
                return true;
            }
        }
        return false;
    }
}

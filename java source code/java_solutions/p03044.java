import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

class Main {
    public static class Dijkstra {
        Vertex[] g;
        PriorityQueue<Pair> que;
        long[] d;

        public Dijkstra(int n) {
            this.g = new Vertex[n];
            this.d = new long[n];
            this.que = new PriorityQueue<>();

            for (int i = 0; i < n; i++) {
                this.g[i] = new Vertex(i);
            }
            Arrays.fill(this.d, Long.MAX_VALUE / 4);
        }

        public void set(int i, int j, long cost) {
            Vertex u = this.g[i];
            Vertex v = this.g[j];
            u.adj.add(new Edge(cost, v));
            v.adj.add(new Edge(cost, u));
        }

        public void calc(int s) {
            this.d[s] = 0;
            this.que.add(new Pair(0, this.g[s]));

            while (this.que.size() > 0) {
                Pair t = this.que.poll();
                int from = t.now.name;
                List<Edge> toes = t.now.adj;
                if (this.d[from] < t.value) {
                    continue;
                }
                for (Edge e : toes) {
                    int to = e.to.name;
                    long cost = e.w;
                    if (this.d[to] > this.d[from] + cost) {
                        this.d[to] = this.d[from] + cost;
                        this.que.add(new Pair(this.d[to], e.to));
                    }
                }
            }
        }

        public long distance(int g) {
            return this.d[g];
        }
    }

    public static class Edge {
        long w;
        Vertex to;

        public Edge(long w, Vertex to) {
            this.w = w;
            this.to = to;
        }

    }

    public static class Vertex {
        int name;
        List<Edge> adj;

        public Vertex(int name) {
            this.name = name;
            this.adj = new ArrayList<>();
        }

    }

    public static class Pair implements Comparable<Pair> {
        long value;
        Vertex now;

        public Pair(long value, Vertex now) {
            this.value = value;
            this.now = now;
        }

        @Override
        public int compareTo(Pair o) {

            return Long.compare(this.value, o.value);
        }

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        Dijkstra dijkstra = new Dijkstra(N);
        for (int i = 0; i < N - 1; i++) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            long w = scanner.nextLong();
            dijkstra.set(u, v, w);
        }
        dijkstra.calc(0);

        for (int i = 0; i < N; i++) {
            System.out.println(dijkstra.distance(i) % 2);
        }
    }
}

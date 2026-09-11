import java.util.*;

public class Main {
    static final int INF = Integer.MAX_VALUE;
    static int ac[];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            int w = sc.nextInt();
            int h = sc.nextInt();
            if ((w | h) == 0) {
                break;
            }

            int en = 4 * 4 * w * h;

            int[][] lib = new int[][]{
                    {0, 1, 2, 3},
                    {1, 2, 3, 0},
                    {2, 3, 0, 1},
                    {3, 0, 1, 2}
            };

            int vn = h * w * 4;

            Edge[][] edges = new Edge[vn][4];

            int[][] map = new int[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    map[i][j] = sc.nextInt();
                }
            }
            ac = new int[4];
            for (int i = 0; i < 4; i++) {
                ac[i] = sc.nextInt();
            }

            int[] d = new int[]{-1, 0, 1, 0, -1};

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    for (int k = 0; k < 4; k++) {
                        int v1 = k + 4 * j + 4 * w * i;
                        for (int l = 0; l < 4; l++) {
                            int k2 = lib[k][l];
                            int i2 = i + d[k2];
                            int j2 = j + d[k2 + 1];
                            if (i2 < 0 || i2 >= h || j2 < 0 || j2 >= w) {
                                edges[v1][l] = new Edge(-1, -1, -1);
                                continue;
                            }
                            int v2 = k2 + 4 * j2 + 4 * w * i2;
                            int c = (map[i][j] == l) ? 0 : ac[l];
                            edges[v1][l] = new Edge(v1, v2, c);
                        }
                    }
                }
            }

            int[] distance = new int[vn];
            Arrays.fill(distance, INF);
            distance[1] = 0;
            Queue<Edge> queue = new PriorityQueue<>();
            queue.add(new Edge(1, 1, 0));

            while (!queue.isEmpty()) {
                Edge e1 = queue.poll();
                if (distance[e1.target] < e1.cost) {
                    continue;
                }

                for (Edge e2 : edges[e1.target]) {
                    if (e2.source == -1) {
                        continue;
                    }
                    if (distance[e2.target] > distance[e1.target] + e2.cost) {
                        distance[e2.target] = distance[e1.target] + e2.cost;
                        queue.add(new Edge(e1.target, e2.target, distance[e2.target]));
                    }
                }
            }

            // for (int i = 0; i < vn; i++) {
            //     if (i % 4 == 0) {
            //         System.out.println();
            //     }
            //     System.out.println(distance[i]);
            // }
            int res1 = distance[1 + 4 * (w - 1) + 4 * w * (h - 1)];
            int res2 = distance[2 + 4 * (w - 1) + 4 * w * (h - 1)];
            System.out.println(Math.min(res1, res2));
        }

    }

    static class Edge implements Comparable<Edge> {
        public int source;
        public int target;
        public int cost;

        public Edge(int source, int target, int cost) {
            this.source = source;
            this.target = target;
            this.cost = cost;
        }


        @Override
        public int compareTo(Edge o) {
            return this.cost - o.cost;
        }
    }
}

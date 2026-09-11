import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Comparator;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskD solver = new TaskD();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskD {
        static List<TaskD.Vertex> all = new ArrayList<>();
        static final long INF = (long) 1e18;

        void addEdge(TaskD.Vertex a, TaskD.Vertex b, long cap, long rcap, long cost) {
            TaskD.Edge ab = new TaskD.Edge(a, b, cap, cost);
            TaskD.Edge ba = new TaskD.Edge(b, a, rcap, -cost);
            ab.rev = ba;
            ba.rev = ab;
            a.outgo.add(ab);
            b.outgo.add(ba);
        }

        private void heapUp(TaskD.Vertex[] heap, int at) {
            while (at > 0) {
                int i = (at - 1) / 2;
                if (heap[i].dist <= heap[at].dist) break;
                TaskD.Vertex tmp = heap[at];
                heap[at] = heap[i];
                heap[i] = tmp;
                heap[at].heapIndex = at;
                heap[i].heapIndex = i;
                at = i;
            }
        }

        private void heapDown(TaskD.Vertex[] heap, int at, int nheap) {
            while (true) {
                int i = at;
                if (2 * at + 1 < nheap && heap[2 * at + 1].dist < heap[i].dist) i = 2 * at + 1;
                if (2 * at + 2 < nheap && heap[2 * at + 2].dist < heap[i].dist) i = 2 * at + 2;
                if (i == at) break;
                TaskD.Vertex tmp = heap[at];
                heap[at] = heap[i];
                heap[i] = tmp;
                heap[at].heapIndex = at;
                heap[i].heapIndex = i;
                at = i;
            }
        }

        private long minCostMaxFlow(TaskD.Vertex s, TaskD.Vertex t) {
            long res = 0;
            for (TaskD.Vertex v : all) v.dist = INF;
            s.dist = 0;
            while (true) {
                boolean updated = false;
                for (TaskD.Vertex v : all)
                    if (v.dist < INF) {
                        for (TaskD.Edge e : v.outgo) {
                            if (e.cap > 0) {
                                if (e.dest.dist > v.dist + e.cost) {
                                    e.dest.dist = v.dist + e.cost;
                                    updated = true;
                                }
                            }
                        }
                    }
                if (!updated) break;
            }
            long maxDist = 0;
            for (TaskD.Vertex v : all) if (v.dist < INF && v.dist > maxDist) maxDist = v.dist;
            for (TaskD.Vertex v : all) v.phi = v.dist < INF ? v.dist : maxDist;
            TaskD.Vertex[] heap = new TaskD.Vertex[all.size()];
            int nheap = 0;
            while (true) {
                for (TaskD.Vertex v : all) v.dist = INF;
                s.dist = 0;
                nheap = 1;
                heap[0] = s;
                s.heapIndex = 0;
                while (nheap > 0) {
                    TaskD.Vertex bi = heap[0];
                    bi.heapIndex = -1;
                    if (nheap > 1) {
                        heap[0] = heap[nheap - 1];
                        heap[0].heapIndex = 0;
                    }
                    --nheap;
                    if (nheap > 0) {
                        heapDown(heap, 0, nheap);
                    }
                    for (TaskD.Edge e : bi.outgo)
                        if (e.cap > 0) {
                            long edge = bi.phi - e.dest.phi + e.cost;
                            if (edge < 0) {
                                throw new RuntimeException();
                            }
                            if (e.dest.dist > bi.dist + edge) {
                                e.dest.dist = bi.dist + edge;
                                e.dest.prev = e;
                                if (e.dest.heapIndex < 0) {
                                    e.dest.heapIndex = nheap;
                                    heap[nheap++] = e.dest;
                                    heapUp(heap, nheap - 1);
                                } else {
                                    heapUp(heap, e.dest.heapIndex);
                                }
                            }
                        }
                }
                if (t.dist >= INF) break;
                long by = Long.MAX_VALUE;
                TaskD.Vertex at = t;
                while (at != s) {
                    by = Math.min(by, at.prev.cap);
                    at = at.prev.rev.dest;
                }
                res += by * (t.dist - s.phi + t.phi);
                at = t;
                while (at != s) {
                    at.prev.cap -= by;
                    at.prev.rev.cap += by;
                    at = at.prev.rev.dest;
                }
                maxDist = 0;
                for (TaskD.Vertex v : all) if (v.dist < INF && v.dist > maxDist) maxDist = v.dist;
                for (TaskD.Vertex v : all) v.phi += v.dist < INF ? v.dist : maxDist;
            }
            return res;
        }

        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt();
            TaskD.Ball[] balls = new TaskD.Ball[2 * n];
            long totalAmount = 0;
            for (int i = 0; i < 2 * n; ++i) {
                balls[i] = new TaskD.Ball();
                balls[i].x = in.nextInt();
                balls[i].y = in.nextInt();
                balls[i].amount = in.nextInt();
                balls[i].source = i < n;
                if (balls[i].source) totalAmount += balls[i].amount;
            }
            n *= 2;
            TaskD.Vertex s = new TaskD.Vertex();
            TaskD.Vertex t = new TaskD.Vertex();
            for (int i = 0; i < n; ++i) {
                balls[i].v = new TaskD.Vertex();
                if (balls[i].source) {
                    addEdge(s, balls[i].v, balls[i].amount, 0, 0);
                } else {
                    addEdge(balls[i].v, t, balls[i].amount, 0, 0);
                }
            }
            for (int sx = -1; sx <= 1; sx += 2) {
                for (int sy = -1; sy <= 1; sy += 2) {
                    for (TaskD.Ball b : balls) {
                        b.position = b.x * sx + b.y * sy;
                    }
                    Arrays.sort(balls, new Comparator<TaskD.Ball>() {

                        public int compare(TaskD.Ball o1, TaskD.Ball o2) {
                            return Long.compare(o1.position, o2.position);
                        }
                    });
                    TaskD.Vertex prev = null;
                    long prevPosition = -1;
                    for (int i = 0; i < balls.length; ) {
                        int j = i;
                        while (j < balls.length && balls[j].position == balls[i].position) ++j;

                        TaskD.Vertex cur = new TaskD.Vertex();
                        long curPosition = balls[i].position;
                        if (prev != null) {
                            addEdge(prev, cur, totalAmount, 0, prevPosition - curPosition);
                        }
                        for (int k = i; k < j; ++k) {
                            if (balls[k].source) {
                                addEdge(balls[k].v, cur, totalAmount, 0, 0);
                            } else {
                                addEdge(cur, balls[k].v, totalAmount, 0, 0);
                            }
                        }

                        prev = cur;
                        prevPosition = curPosition;
                        i = j;
                    }
                }
            }
            long res = -minCostMaxFlow(s, t);
            out.println(res);
        }

        static class Edge {
            TaskD.Vertex src;
            TaskD.Vertex dest;
            long cap;
            long cost;
            TaskD.Edge rev;

            public Edge(TaskD.Vertex src, TaskD.Vertex dest, long cap, long cost) {
                this.src = src;
                this.dest = dest;
                this.cap = cap;
                this.cost = cost;
            }

        }

        static class Vertex {
            long phi = 0;
            long dist;
            int heapIndex = -1;
            TaskD.Edge prev;
            List<TaskD.Edge> outgo = new ArrayList<>();

            public Vertex() {
                all.add(this);
            }

        }

        static class Ball {
            int x;
            int y;
            int amount;
            long position;
            boolean source;
            TaskD.Vertex v;

        }

    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}

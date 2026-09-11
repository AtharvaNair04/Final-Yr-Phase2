import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    static BufferedReader in;
    static PrintWriter out;
    static StringTokenizer tok;

    void solve() throws IOException {
        int h = ni();
        int w = ni();
        String[] s = nsa(h);

        int INF = Integer.MAX_VALUE;

        for (int i = 0; i < h; i++) {
            if (s[i].charAt(0) == 'X' || s[i].charAt(w - 1) == 'X') {
                out.println(-1);
                return;
            }
        }
        for (int i = 0; i < w; i++) {
            if (s[0].charAt(i) == 'X' || s[h - 1].charAt(i) == 'X') {
                out.println(-1);
                return;
            }
        }

        MaxFlowFordFulkerson m = new MaxFlowFordFulkerson(2 * h * w + 2);
        int source = 2 * h * w;
        int sink = 2 * h * w + 1;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (i > 0) m.addEdge(2 * (i * w + j) + 1, 2 * ((i - 1) * w + j), INF);
                if (i < h - 1) m.addEdge(2 * (i * w + j) + 1, 2 * ((i + 1) * w + j), INF);
                if (j > 0) m.addEdge(2 * (i * w + j) + 1, 2 * (i * w + j - 1), INF);
                if (j < w - 1) m.addEdge(2 * (i * w + j) + 1, 2 * (i * w + j + 1), INF);
                if (i == 0 || i == h - 1 || j == 0 || j == w - 1) {
                    m.addEdge(2 * (i * w + j) + 1, sink, INF);
                }
                if (s[i].charAt(j) == 'X') {
                    m.addEdge(source, 2 * (i * w + j) + 1, INF);
                    m.addEdge(2 * (i * w + j), 2 * (i * w + j) + 1, INF);
                } else {
                    m.addEdge(2 * (i * w + j), 2 * (i * w + j) + 1, 1);
                }
            }
        }

        out.println(m.maxFlow(source, sink));
    }

    class MaxFlowFordFulkerson {
        ArrayList<ArrayList<Edge>> G;
        boolean[] used;
        int INF = Integer.MAX_VALUE;

        public MaxFlowFordFulkerson(int n) {
            G = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                G.add(new ArrayList<Edge>());
            }
            used = new boolean[n];
        }

        void addEdge(int from, int to, int cap) {
            G.get(from).add(new Edge(to, cap, G.get(to).size()));
            G.get(to).add(new Edge(from, 0, G.get(from).size() - 1));
        }

        private int dfs(int v, int t, int f) {
            if (v == t) return f;
            used[v] = true;
            for (Edge e : G.get(v)) {
                if (!used[e.to] && e.cap > 0) {
                    int d = dfs(e.to, t, Math.min(f, e.cap));
                    if (d > 0) {
                        e.cap -= d;
                        G.get(e.to).get(e.rev).cap += d;
                        return d;
                    }
                }
            }

            return 0;
        }

        int maxFlow(int s, int t) {
            int flow = 0;
            while (true) {
                Arrays.fill(used, false);
                int f = dfs(s, t, INF);
                if (f == 0) return flow;
                flow += f;
            }
        }

        class Edge {
            int to, cap, rev;

            public Edge(int to, int cap, int rev) {
                this.to = to;
                this.cap = cap;
                this.rev = rev;
            }
        }
    }

    String ns() throws IOException {
        while (!tok.hasMoreTokens()) {
            tok = new StringTokenizer(in.readLine(), " ");
        }
        return tok.nextToken();
    }

    int ni() throws IOException {
        return Integer.parseInt(ns());
    }

    long nl() throws IOException {
        return Long.parseLong(ns());
    }

    double nd() throws IOException {
        return Double.parseDouble(ns());
    }

    String[] nsa(int n) throws IOException {
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = ns();
        }
        return res;
    }

    int[] nia(int n) throws IOException {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = ni();
        }
        return res;
    }

    long[] nla(int n) throws IOException {
        long[] res = new long[n];
        for (int i = 0; i < n; i++) {
            res[i] = nl();
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        tok = new StringTokenizer("");
        Main main = new Main();
        main.solve();
        out.close();
    }
}

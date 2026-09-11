import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
    public static int total[];
    public static int sum[];
    public static int num[];

    public static void main(String[] args){
        PrintWriter out = new PrintWriter(System.out);
        FastScanner sc = new FastScanner();

        int N = sc.nextInt();
        int Q = sc.nextInt();

        ArrayList<Integer> adjs[] = new ArrayList[N];
        ArrayList<Link> links[] = new ArrayList[N];
        for (int i=0;i<N;i++) {
            adjs[i] = new ArrayList<Integer>();
            links[i] = new ArrayList<Link>();
        }

        for (int i=0;i<N-1;i++) {
            int a = sc.nextInt() - 1;
            int b = sc.nextInt() - 1;
            int c = sc.nextInt() - 1;
            int d = sc.nextInt();
            links[a].add(new Link(b, c, d));
            links[b].add(new Link(a, c, d));

            adjs[a].add(b);
            adjs[b].add(a);
        }

        LCA lca = new LCA(adjs);
        ArrayList<Query> query[] = new ArrayList[N];
        int y[] = new int[Q];
        for (int i=0;i<N;i++) {
            query[i] = new ArrayList<Query>();
        }

        total = new int[Q * 3];
        sum = new int[Q * 3];
        num = new int[Q * 3];

        for (int i=0;i<Q;i++) {
            int x = sc.nextInt() - 1;
            y[i] = sc.nextInt();
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;

            int p = lca.lca(u, v);

            query[u].add(new Query(x, i * 3 + 0));
            query[v].add(new Query(x, i * 3 + 1));
            query[p].add(new Query(x, i * 3 + 2));
        }

        search(0, -1, 0, new int[N], new int[N], links, query);

        for (int i=0;i<Q;i++) {
            int ans = total[i*3+0] + total[i*3+1] - total[i*3+2] * 2;
            ans -= sum[i*3+0] + sum[i*3+1] - sum[i*3+2] * 2;
            ans += (num[i*3+0] + num[i*3+1] - num[i*3+2] * 2) * y[i];
            out.println(ans);
        }

        out.flush();
    }

    public static void search(int now, int parent, int _total, int _sum[], int _num[], ArrayList<Link> links[], ArrayList<Query> query[]) {
        for (Query q: query[now]) {
            total[q.i] = _total;
            sum[q.i] = _sum[q.x];
            num[q.i] = _num[q.x];
        }

        for (Link l: links[now]) {
            if (l.to == parent) continue;

            _sum[l.color] += l.distance;
            _num[l.color]++;
            search(l.to, now, _total + l.distance, _sum, _num, links, query);
            _sum[l.color] -= l.distance;
            _num[l.color]--;
        }
    }

    public static class Query {
        int x, i;

        Query(int x, int i) {
            this.i = i;
            this.x = x;
        }
    }

    public static class Link {
        int to, color, distance;
        Link(int to, int color, int distance) {
            this.to = to;
            this.color = color;
            this.distance = distance;
        }
    }

    public static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner(String s) {
            try {
                br = new BufferedReader(new FileReader(s));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String nextToken() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(nextToken());
        }

        long nextLong() {
            return Long.parseLong(nextToken());
        }

        double nextDouble() {
            return Double.parseDouble(nextToken());
        }

        int[] nextIntArray(int N) {
            int a[] = new int[N];
            for (int i=0;i<N;i++) {
                a[i] = this.nextInt();
            }

            return a;
        }

        long[] nextLongArray(int N) {
            long a[] = new long[N];
            for (int i=0;i<N;i++) {
                a[i] = this.nextLong();
            }

            return a;
        }

        double[] nextDoubleArray(int N) {
            double a[] = new double[N];
            for (int i=0;i<N;i++) {
                a[i] = this.nextDouble();
            }

            return a;
        }

        String[] nextTokenArray(int N) {
            String a[] = new String[N];
            for (int i=0;i<N;i++) {
                a[i] = this.nextToken();
            }

            return a;
        }
    }
}

class LCA {

    private int LOGN;

    /**
     * ancestors[i][j] = 2^j ancestor of vertex i
     */
    private int ancestors[][];

    private int heights[];

    private ArrayList<Integer> links[];

    public LCA(ArrayList<Integer> links[]) {
        int N = links.length;
        LOGN = log2(N);
        ancestors = new int[N][LOGN];
        heights = new int[N];
        this.links = links;
        dfs(0,-1);
    }

    private int goUp(int vertex, int count) {
        for (int i = 0; i < LOGN; i++) {
            if (((count >> i) & 1) == 1) {
                vertex = ancestors[vertex][i];
            }
        }
        return vertex;
    }

     int lca(int a, int b) {
        if(heights[a] < heights[b]) return lca(b, a);

        a = goUp(a, heights[a] - heights[b]);
        if(a == b) return a;

        for(int i=LOGN-1;i>=0; i--) {
            if(ancestors[a][i] != ancestors[b][i]) {
                a = ancestors[a][i];
                b = ancestors[b][i];
            }
        }

        return ancestors[a][0];
    }

    private void dfs(int vertex, int parent) {
        ancestors[vertex][0] = parent;
        if(parent != -1) {
            heights[vertex] = heights[parent] + 1;
        }
        for(int i=1;i< LOGN; i++) {
            if(ancestors[vertex][i-1] != -1) {
                ancestors[vertex][i] = ancestors[ancestors[vertex][i-1]][i-1];
            }
        }
        for(int neighbour : links[vertex]) {
            if(neighbour != parent) {
                dfs(neighbour, vertex);
            }
        }
    }

    private int log2(int n) {
        int result = 0;
        while((n >>= 1) != 0) {
            result++;
        }
        return (result+1);
    }

}

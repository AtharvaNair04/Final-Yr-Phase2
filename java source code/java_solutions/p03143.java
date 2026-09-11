// package other2019.nikkei2019.qual;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    static long __startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt();
        int m = in.nextInt();
        long[] x = new long[n];
        for (int i = 0; i < n ; i++) {
            x[i] = in.nextLong();
        }
        int[][] edges = new int[m][3];
        for (int i = 0; i < m ; i++) {
            for (int j = 0; j < 2 ; j++) {
                edges[i][j] = in.nextInt()-1;
            }
            edges[i][2] = in.nextInt();
        }
        Arrays.sort(edges, Comparator.comparingInt(u -> u[2]));


        UnionFind uf = new UnionFind(n, x);

        int[] canUse = new int[m];
        for (int i = 0; i < m; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            uf.unite(u, v);
            int nid = uf.find(u);
            if (uf.groupCount(nid) >= edges[i][2]) {
                canUse[i] = 2;
            }
        }

        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n ; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < m ; i++) {
            graph[edges[i][0]].add(i);
            graph[edges[i][1]].add(i);
        }

        Queue<Integer> q = new ArrayBlockingQueue<>(n + m + 10);
        boolean[] visited = new boolean[n];
        for (int i = m-1 ; i >= 0 ; i--) {
            if (canUse[i] == 2) {
                q.add(edges[i][0]);
                q.add(edges[i][1]);
                while (q.size() >= 1) {
                    int now = q.poll();
                    for (int eid : graph[now]) {
                        int to = edges[eid][0] + edges[eid][1] - now;
                        if (edges[eid][2] > edges[i][2]) {
                            continue;
                        }
                        canUse[eid] = Math.max(canUse[eid], 1);
                        if (!visited[to]) {
                            visited[to] = true;
                            q.add(to);
                        }
                    }
                }
            }
        }

        int ecount = 0;
        for (int i = 0; i < m ; i++) {
            ecount += canUse[i] >= 1 ? 1 : 0;
        }
        out.println(m-ecount);
        out.flush();
    }

    static class UnionFind {
        int[] rank;
        int[] parent;
        long[] cnt;

        public UnionFind(int n, long[] x) {
            rank = new int[n];
            parent = new int[n];
            cnt = new long[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                cnt[i] = x[i];
            }
        }

        public int find(int a) {
            if (parent[a] == a) {
                return a;
            }
            parent[a] = find(parent[a]);
            cnt[a] = cnt[parent[a]];
            return parent[a];
        }

        public void unite(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) {
                return;
            }
            if (rank[a] < rank[b]) {
                parent[a] = b;
                cnt[b] += cnt[a];
            } else {
                parent[b] = a;
                cnt[a] += cnt[b];
                if (rank[a] == rank[b]) {
                    rank[a]++;
                }
            }
        }

        public long groupCount(int a) {
            return cnt[find(a)];
        }

        private boolean issame(int a, int b) {
            return find(a) == find(b);
        }
    }


    private static void printTime(String label) {
        debug(label, System.currentTimeMillis() - __startTime);
    }

    private static void debug(Object... o) {
        System.err.println(Arrays.deepToString(o));
    }

    public static class InputReader {
        private static final int BUFFER_LENGTH = 1 << 12;
        private InputStream stream;
        private byte[] buf = new byte[BUFFER_LENGTH];
        private int curChar;
        private int numChars;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        private int next() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public char nextChar() {
            return (char) skipWhileSpace();
        }

        public String nextToken() {
            int c = skipWhileSpace();
            StringBuilder res = new StringBuilder();
            do {
                res.append((char) c);
                c = next();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public int nextInt() {
            return (int) nextLong();
        }

        public long nextLong() {
            int c = skipWhileSpace();
            long sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = next();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = next();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public double nextDouble() {
            return Double.valueOf(nextToken());
        }

        int skipWhileSpace() {
            int c = next();
            while (isSpaceChar(c)) {
                c = next();
            }
            return c;
        }

        boolean isSpaceChar(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }
    }
}

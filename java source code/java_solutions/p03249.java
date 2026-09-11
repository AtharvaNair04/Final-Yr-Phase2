// package other2018.kupc2018;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static long __startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt();

        long[][] a = new long[n][2];
        for (int i = 0; i < n ; i++) {
            a[i][0] = in.nextLong();
            a[i][1] = i;
        }
        Arrays.sort(a, (u, v) -> Long.compare(v[0], u[0]));

        Map<Long,Integer> vmap = new HashMap<>();
        for (int i = 0; i < n ; i++) {
            vmap.put(a[i][0], (int)a[i][1]);
        }

        child = new int[n];
        Arrays.fill(child, 1);
        List<int[]> edges = new ArrayList<>();

        graph = new List[n];
        for (int i = 0; i < n ; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < n ; i++) {
            long v = a[i][0];
            int idx = (int)a[i][1];
            long pl = child[idx];
            long pv = v + pl - (n - pl);
            if (!vmap.containsKey(pv) || v < pv) {
                continue;
            }
            int pidx = vmap.get(pv);
            edges.add(new int[]{idx, pidx});
            graph[pidx].add(idx);
            child[pidx] += child[idx];
        }

        boolean valid = true;
        visited = new boolean[n];
        valid &= dfs((int)a[n-1][1]);
        for (int i = 0; i < n ; i++) {
            valid &= visited[i];
        }
        if (valid) {
            valid &= dfs2((int) a[n - 1][1]) == a[n - 1][0];
        }

        if (valid) {
            for (int[] e : edges) {
                out.println(String.format("%d %d", e[0]+1, e[1]+1));
            }
        } else {
            out.println(-1);
        }
        out.flush();
    }

    static boolean[] visited;
    static List<Integer>[] graph;
    static int[] child;

    static long dfs2(int r) {
        long total = 0;
        for (int to : graph[r]) {
            total += dfs2(to) + child[to];
        }
        return total;
    }

    static boolean dfs(int r) {
        if (visited[r]) {
            return false;
        }
        visited[r] = true;
        boolean ok = true;
        for (int to : graph[r]) {
            ok &= dfs(to);
        }
        return ok;
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

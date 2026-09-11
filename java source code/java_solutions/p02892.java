import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

class Main {

    static PrintWriter out;
    static InputReader sc;

    static List<List<Integer>> graph;
    static int[] color;

    public static void main(String[] args) {
        sc = new InputReader(System.in);
        out = new PrintWriter(System.out);
        solve();
        out.flush();
    }

    static void solve() {
        int N = sc.nextInt();
        graph = new ArrayList<>();
        WarshallFloyd wf = new WarshallFloyd(N);
        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }
        color = new int[N];
        for (int i = 0; i < N; i++) {
            String s = sc.next();
            for (int j = 0; j < N; j++) {
                if (s.charAt(j) == '1') {
                    graph.get(i).add(j);
                    wf.set(i, j, 1);
                }
            }
        }
        if (!dfs(0, 1)) {
            out.println(-1);
            return;
        }
        wf.calc();
        long ans = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (wf.distance(i, j) != Long.MAX_VALUE / 3) {
                    ans = Math.max(ans, wf.distance(i, j) + 1);
                }
            }
        }
        out.println(ans);
    }

    static boolean dfs(int v, int c) {
        color[v] = c;// 頂点vをcで塗る
        for (int i : graph.get(v)) {
            if (color[i] == c) {
                return false;
            }
            if (color[i] == 0 && !dfs(i, -c)) {
                return false;
            }
        }
        return true;
    }

    static void debug(Object... args) {
        StringJoiner j = new StringJoiner(" ");
        for (Object arg : args) {
            if (arg == null) {
                j.add("null");
            } else if (arg instanceof int[]) {
                j.add(Arrays.toString((int[]) arg));
            } else if (arg instanceof long[]) {
                j.add(Arrays.toString((long[]) arg));
            } else if (arg instanceof double[]) {
                j.add(Arrays.toString((double[]) arg));
            } else if (arg instanceof char[]) {
                j.add(Arrays.toString((char[]) arg));
            } else if (arg instanceof boolean[]) {
                j.add(Arrays.toString((boolean[]) arg));
            } else if (arg instanceof Object[]) {
                j.add(Arrays.toString((Object[]) arg));
            } else {
                j.add(arg.toString());
            }
        }
        System.err.println(j.toString());
    }

    /**
     * WarshallFloyd法による全点対最短経路問題の実装.<br>
     * 負の辺があっても動作する
     */
    public static class WarshallFloyd {

        private final long INF = Long.MAX_VALUE / 3;
        private long[][] d;
        private int V;

        /**
         * 初期化
         *
         * @param n 頂点の数
         */
        public WarshallFloyd(int n) {
            this.d = new long[n][n];
            for (long[] ls : this.d) {
                Arrays.fill(ls, this.INF);
            }
            this.V = n;
        }

        public void set(int from, int to, long cost) {
            this.d[from][to] = cost;
        }

        /**
         * すべての2頂点間の最短距離を計算する O(|V|^3)
         */
        public void calc() {
            for (int k = 0; k < this.V; k++) {
                for (int i = 0; i < this.V; i++) {
                    for (int j = 0; j < this.V; j++) {
                        this.d[i][j] = Math.min(this.d[i][j], this.d[i][k] + this.d[k][j]);
                    }
                }
            }
        }

        /**
         * fromからtoまでの最短距離を返す O(1).<br>
         * 経路が存在しない場合はINF(Long.MAX_VALUE / 3)を返す
         *
         * @param from
         * @param to
         * @return fromからtoまでの最短距離
         */
        public long distance(int from, int to) {
            return this.d[from][to];
        }

    }

    static class InputReader {

        private InputStream in;
        private byte[] buffer = new byte[1024];
        private int curbuf;
        private int lenbuf;

        public InputReader(InputStream in) {
            this.in = in;
            this.curbuf = this.lenbuf = 0;
        }

        public boolean hasNextByte() {
            if (this.curbuf >= this.lenbuf) {
                this.curbuf = 0;
                try {
                    this.lenbuf = this.in.read(this.buffer);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (this.lenbuf <= 0) {
                    return false;
                }
            }
            return true;
        }

        private int readByte() {
            if (this.hasNextByte()) {
                return this.buffer[this.curbuf++];
            } else {
                return -1;
            }
        }

        private boolean isSpaceChar(int c) {
            return !(c >= 33 && c <= 126);
        }

        private void skip() {
            while (this.hasNextByte() && this.isSpaceChar(this.buffer[this.curbuf])) {
                this.curbuf++;
            }
        }

        public boolean hasNext() {
            this.skip();
            return this.hasNextByte();
        }

        public String next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            StringBuilder sb = new StringBuilder();
            int b = this.readByte();
            while (!this.isSpaceChar(b)) {
                sb.appendCodePoint(b);
                b = this.readByte();
            }
            return sb.toString();
        }

        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            int c = this.readByte();
            while (this.isSpaceChar(c)) {
                c = this.readByte();
            }
            boolean minus = false;
            if (c == '-') {
                minus = true;
                c = this.readByte();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res = res * 10 + c - '0';
                c = this.readByte();
            } while (!this.isSpaceChar(c));
            return minus ? -res : res;
        }

        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            int c = this.readByte();
            while (this.isSpaceChar(c)) {
                c = this.readByte();
            }
            boolean minus = false;
            if (c == '-') {
                minus = true;
                c = this.readByte();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res = res * 10 + c - '0';
                c = this.readByte();
            } while (!this.isSpaceChar(c));
            return minus ? -res : res;
        }

        public double nextDouble() {
            return Double.parseDouble(this.next());
        }

        public int[] nextIntArray(int n) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = this.nextInt();
            }
            return a;
        }

        public long[] nextLongArray(int n) {
            long[] a = new long[n];
            for (int i = 0; i < n; i++) {
                a[i] = this.nextLong();
            }
            return a;
        }

        public char[][] nextCharMap(int n, int m) {
            char[][] map = new char[n][m];
            for (int i = 0; i < n; i++) {
                map[i] = this.next().toCharArray();
            }
            return map;
        }
    }
}

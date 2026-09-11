import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

class Main {

    static PrintWriter out;
    static InputReader sc;

    public static void main(String[] args) {
        sc = new InputReader(System.in);
        out = new PrintWriter(System.out);
        solve();
        out.flush();
    }

    static void solve() {
        int N = sc.nextInt();
        long C = sc.nextLong();
        long[] x = new long[N];
        int[] v = new int[N];
        for (int i = 0; i < N; i++) {
            x[i] = sc.nextLong();
            v[i] = sc.nextInt();
        }
        long[] sum = new long[N + 1];
        long[] l = new long[N];
        long[] r = new long[N];
        long[] g = new long[N];
        for (int i = 0; i < N; i++) {
            sum[i + 1] = sum[i] + v[i];
        }
        long ans = 0;
        for (int i = 0; i < N; i++) {// 時計回りに出発し、折り返さない
            ans = Math.max(ans, l[i] = (sum[i + 1] - x[i]));
        }
        for (int i = N - 1; i >= 0; i--) {// 半時計回り出発し、折り返さない
            ans = Math.max(ans, r[i] = (sum[N] - sum[i] - (C - x[i])));
        }
        // 時計回りに出発し、折り返す
        g[N - 1] = r[N - 1];
        for (int i = N - 2; i >= 0; i--) {
            g[i] = Math.max(g[i + 1], r[i]);
        }
        for (int i = 0; i < N - 1; i++) {
            ans = Math.max(ans, l[i] + g[i + 1] - x[i]);
        }
        // 半時計回りに出発し、折り返す
        Arrays.fill(g, 0);
        g[0] = l[0];
        for (int i = 1; i < N; i++) {
            g[i] = Math.max(g[i - 1], l[i]);
        }
        for (int i = N - 1; i >= 1; i--) {
            ans = Math.max(ans, r[i] + g[i - 1] - (C - x[i]));
        }
        out.println(ans);
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

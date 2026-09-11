import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

class Main {

    static PrintWriter out;
    static InputReader sc;

    static final int MOD = (int) 1e9 + 7;

    public static void main(String[] args) {
        sc = new InputReader(System.in);
        out = new PrintWriter(System.out);
        solve();
        out.flush();
    }

    static void solve() {
        int ans = 0;
        int N = sc.nextInt();
        int W = sc.nextInt();
        int[] w = new int[N];
        int[] _v = new int[N];
        for (int i = 0; i < N; i++) {
            w[i] = sc.nextInt();
            _v[i] = sc.nextInt();
        }
        int ww = w[0];
        for (int i = 0; i < N; i++) {
            w[i] -= ww;
        }
        int[] cnt = new int[4];
        for (int i : w) {
            cnt[i]++;
        }
        int[][] v = new int[4][];
        for (int i = 0; i < 4; i++) {
            v[i] = new int[cnt[i]];
        }
        int[] idx = new int[4];
        for (int i = 0; i < N; i++) {
            v[w[i]][idx[w[i]]++] = _v[i];
        }
        for (int i = 0; i < 4; i++) {
            Arrays.sort(v[i]);
            reverse(v[i]);
        }
        int[][] sum = new int[4][];
        for (int i = 0; i < 4; i++) {
            sum[i] = new int[cnt[i] + 1];
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < cnt[i]; j++) {
                sum[i][j + 1] = sum[i][j] + v[i][j];
            }
        }
        for (int i = 0; i <= v[0].length; i++) {
            if (ww * i > W) {
                break;
            }
            for (int j = 0; j <= v[1].length; j++) {
                if (ww * (i + j) + 1 * j > W) {
                    break;
                }
                for (int k = 0; k <= v[2].length; k++) {
                    if (ww * (i + j + k) + 1 * j + 2 * k > W) {
                        break;
                    }
                    for (int l = 0; l <= v[3].length; l++) {
                        if (ww * (i + j + k + l) + 1 * j + 2 * k + 3 * l > W) {
                            break;
                        }
                        ans = Math.max(ans, sum[0][i] + sum[1][j] + sum[2][k] + sum[3][l]);
                    }
                }
            }
        }
        out.println(ans);
    }

    /**
     * arrの中身を逆に並び替える O(n)
     *
     * @param arr
     */
    public static void reverse(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int tmp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = tmp;
        }
    }

    static void debug(Object... args) {
        StringJoiner j = new StringJoiner(" ");
        for (Object arg : args) {
            Class<?> type = arg.getClass();
            if (!type.isArray()) {
                j.add(arg.toString());
            } else if (type.getComponentType().isArray()) {
                for (Object o : (Object[]) arg) {
                    debug(o);
                }
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

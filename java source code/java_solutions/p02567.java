import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.*;

public class Main {
    static In in = new In();
    static Out out = new Out();
    static final long inf = 0x1fffffffffffffffL;
    static final int iinf = 0x3fffffff;
    static final double eps = 1e-9;
    static long mod = 1000000007;

    void solve(boolean naive) {
        int n = in.nextInt();
        int q = in.nextInt();
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(in.nextLong());
        }
        SegmentTree<Long> tree1 = new SegmentTree<>(Monoid.MAX_LONG);
        tree1.build(list);
        for (int i = 0; i < q; i++) {
            int t = in.nextInt();
            if (t == 1) {
                int x = in.nextInt() - 1;
                long v = in.nextLong();
                tree1.set(x, v);
            } else if (t == 2) {
                int l = in.nextInt() - 1;
                int r = in.nextInt();
                out.println(tree1.get(l, r));
            } else {
                int x = in.nextInt() - 1;
                int v = in.nextInt();
                int ans = tree1.binarySearchLeft(x, n, e -> e < v);
                out.println(ans == -1 ? n + 1 : ans);
            }
        }
    }

    public static void main(String[] args) {
        new Main().solve(args.length == 1 && args[0].equals("naive"));
        out.flush();
    }
}

class Monoid<T> {
    static final Monoid<Integer> SUM_INTEGER = new Monoid<>(0, Integer::sum);
    static final Monoid<Integer> PRODUCT_INTEGER = new Monoid<>(1, (a, b) -> a * b);
    static final Monoid<Integer> XOR_INTEGER = new Monoid<>(0, (a, b) -> a ^ b);
    static final Monoid<Integer> GCD = new Monoid<>(0, (a, b) -> {
        if (b == 0) {
            return a;
        }
        while (a % b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return b;
    });
    static final Monoid<Integer> MIN_INTEGER = new Monoid<>(Main.iinf, Math::min);
    static final Monoid<Integer> MAX_INTEGER = new Monoid<>(-Main.iinf, Math::max);
    static final Monoid<Long> SUM_LONG = new Monoid<>(0L, Long::sum);
    static final Monoid<Long> PRODUCT_LONG = new Monoid<>(1L, (a, b) -> a * b);
    static final Monoid<Long> XOR_LONG = new Monoid<>(0L, (a, b) -> a ^ b);
    static final Monoid<Long> MIN_LONG = new Monoid<>(Main.inf, Math::min);
    static final Monoid<Long> MAX_LONG = new Monoid<>(-Main.inf, Math::max);
    static final Monoid<Long> SUM_MOD = new Monoid<>(0L, (a, b) -> (a + b) % Main.mod);
    private T identity;
    private Supplier<T> identitySupplier;
    private BinaryOperator<T> operation;

    Monoid(BinaryOperator<T> operation) {
        this.operation = operation;
    }

    Monoid(T identity, BinaryOperator<T> operation) {
        this(operation);
        this.identity = identity;
    }

    Monoid(Supplier<T> identitySupplier, BinaryOperator<T> operation) {
        this(operation);
        this.identitySupplier = identitySupplier;
    }

    T merge(T x, T y) {
        T identity = identity();
        if (Objects.equals(x, identity)) {
            if (Objects.equals(y, identity)) {
                return identity;
            }
            return y;
        } else if (Objects.equals(y, identity)) {
            return x;
        } else {
            return operation.apply(x, y);
        }
    }

    public T identity() {
        return identitySupplier == null ? identity : identitySupplier.get();
    }

    public boolean equalsIdentity(T obj) {
        return Objects.equals(obj, identity());
    }
}

class SegmentTree<T> {
    private boolean isBuilt;
    private List<T> node;
    private int n;
    private int m;
    private Monoid<T> monoid;
    private int[] left;
    private int[] right;

    SegmentTree(Monoid<T> monoid) {
        this.monoid = monoid;
    }

    void build(int m) {
        build(m, i -> monoid.identity());
    }

    void build(int m, T init) {
        build(m, i -> init);
    }

    void build(List<T> data) {
        build(data.size(), data::get);
    }

    void build(int m, IntFunction<T> initFunc) {
        this.m = m;
        this.n = Integer.highestOneBit(m * 2 - 1);
        this.node = new ArrayList<>(n * 2);
        this.left = new int[n * 2];
        this.right = new int[n * 2];
        for (int i = 0; i < n; i++) {
            node.add(null);
        }
        for (int i = 0; i < n; i++) {
            node.add(i < m ? initFunc.apply(i) : monoid.identity());
            left[i + n] = i;
            right[i + n] = i + 1;
        }
        for (int i = n - 1; i > 0; i--) {
            node.set(i, monoid.merge(node.get(i * 2), node.get(i * 2 + 1)));
            left[i] = left[i * 2];
            right[i] = right[i * 2 + 1];
        }
        this.isBuilt = true;
    }

    void set(int i, T value) {
        i += n;
        node.set(i, value);
        while (i > 1) {
            i >>= 1;
            node.set(i, monoid.merge(node.get(i * 2), node.get(i * 2 + 1)));
        }
    }

    T get(int i) {
        return node.get(i + n);
    }

    T get(int l, int r) {
        l += n;
        r += n;
        T xl = monoid.identity();
        T xr = monoid.identity();
        while (l < r) {
            if (l % 2 == 1) {
                xl = monoid.merge(xl, node.get(l++));
            }
            if (r % 2 == 1) {
                xr = monoid.merge(node.get(--r), xr);
            }
            l >>= 1;
            r >>= 1;
        }
        return monoid.merge(xl, xr);
    }

    // check(get(0,index-1)) && !check(get(0,index)
    int binarySearchLeft(Predicate<T> predicate) {
        return binarySearchLeft(0, n, predicate);
    }

    int binarySearchLeft(int l, int r, Predicate<T> predicate) {
        T current = monoid.identity();
        for (int i = l + n; i < n * 2 && l < r;) {
            T t = monoid.merge(current, node.get(i));
            if (right[i] <= r && predicate.test(t)) {
                current = t;
                l = right[i];
                i++;
                if (i % 2 == 0) {
                    i >>= 1;
                }
            } else {
                i <<= 1;
            }
        }
        return l < r ? l + 1 : -1;
    }

    // !check(get(index-1,n)) && check(get(index,n)
    int binarySearchRight(Predicate<T> predicate) {
        return binarySearchRight(0, n, predicate);
    }

    int binarySearchRight(int l, int r, Predicate<T> predicate) {
        T current = monoid.identity();
        for (int i = r + n - 1; i < n * 2 && l < r;) {
            T t = monoid.merge(node.get(i), current);
            if (left[i] >= l && predicate.test(t)) {
                current = t;
                r = left[i];
                i--;
                if (i % 2 == 1) {
                    i >>= 1;
                }
            } else {
                i = i * 2 + 1;
            }
        }
        return l < r ? r : -1;
    }

    List<T> getAll() {
        return new ArrayList<>(node.subList(n, n + m));
    }

    @Override
    public String toString() {
        return getAll().toString();
    }
}

class In {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 0x10000);
    private StringTokenizer tokenizer;

    String next() {
        try {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
        } catch (IOException ignored) {
        }
        return tokenizer.nextToken();
    }

    int nextInt() {
        return Integer.parseInt(next());
    }

    long nextLong() {
        return Long.parseLong(next());
    }

    double nextDouble() {
        return Double.parseDouble(next());
    }

    char[] nextCharArray() {
        return next().toCharArray();
    }

    char[][] nextCharGrid(int n, int m) {
        char[][] a = new char[n][m];
        for (int i = 0; i < n; i++) {
            a[i] = next().toCharArray();
        }
        return a;
    }

    int[] nextIntArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = nextInt();
        }
        return a;
    }

    int[] nextIntArray(int n, IntUnaryOperator op) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = op.applyAsInt(nextInt());
        }
        return a;
    }

    long[] nextLongArray(int n) {
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = nextLong();
        }
        return a;
    }

    long[] nextLongArray(int n, LongUnaryOperator op) {
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = op.applyAsLong(nextLong());
        }
        return a;
    }

    List<List<Integer>> nextEdges(int n, int m) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int u = nextInt() - 1;
            int v = nextInt() - 1;
            res.get(u).add(v);
            res.get(v).add(u);
        }
        return res;
    }
}

class Out {
    private PrintWriter out = new PrintWriter(System.out);
    boolean autoFlush = false;

    void println(Object... a) {
        StringJoiner joiner = new StringJoiner(" ");
        for (Object obj : a) {
            joiner.add(String.valueOf(obj));
        }
        out.println(joiner);
        if (autoFlush) {
            out.flush();
        }
    }

    void println(char[] s) {
        out.println(String.valueOf(s));
        if (autoFlush) {
            out.flush();
        }
    }

    void println(int[] a) {
        StringJoiner joiner = new StringJoiner(" ");
        for (int i : a) {
            joiner.add(Integer.toString(i));
        }
        out.println(joiner);
        if (autoFlush) {
            out.flush();
        }
    }

    void println(long[] a) {
        StringJoiner joiner = new StringJoiner(" ");
        for (long i : a) {
            joiner.add(Long.toString(i));
        }
        out.println(joiner);
        if (autoFlush) {
            out.flush();
        }
    }

    void flush() {
        out.flush();
    }
}

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 27);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            TaskF solver = new TaskF();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskF {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();

            if (n == 2) {
                out.println("1 2");
                return;
            }

            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
            }
            for (int i = 1; i < n; i++) {
                Node a = nodes[in.readInt()];
                Node b = nodes[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }

            for (int i = 1; i <= n; i++) {
                nodes[i].isLeaf = nodes[i].next.size() == 1;
            }


            boolean valid = true;
            for (int i = 1; i <= n; i++) {
                for (Node node : nodes[i].next) {
                    if (!node.isLeaf) {
                        nodes[i].end++;
                    }
                }
                nodes[i].tag = !nodes[i].isLeaf && nodes[i].end <= 2;
                valid = valid && nodes[i].end <= 2;
            }

            if (!valid) {
                out.println(-1);
                return;
            }

            List<Node> trace = new ArrayList<>(n);
            for (int i = 1; i <= n; i++) {
                if (nodes[i].tag && nodes[i].end <= 1) {
                    dfs(nodes[i], null, trace);
                    break;
                }
            }

            for (Node node : trace.get(0).next) {
                if (node.isLeaf) {
                    trace.get(0).childrenNum--;
                    trace.add(0, node);
                    node.selected = true;
                    break;
                }
            }

            for (Node node : trace.get(trace.size() - 1).next) {
                if (node.isLeaf && !node.selected) {
                    trace.get(trace.size() - 1).childrenNum--;
                    trace.add(node);
                    node.selected = true;
                    break;
                }
            }

            IntList p1 = new IntList(n);
            IntList p2 = new IntList(n);

            genPerm(trace, p1);
            SequenceUtils.reverse(trace, 0, trace.size() - 1);
            genPerm(trace, p2);

            if (CompareUtils.compareArray(p1.getData(), p2.getData(), 0, p1.size() - 1, 0, p2.size() - 1) > 0) {
                IntList tmp = p1;
                p1 = p2;
                p2 = tmp;
            }

            for (int i = 0; i < n; i++) {
                out.append(p1.get(i)).append(' ');
            }
        }

        public void genPerm(List<Node> nodes, IntList perm) {
            for (Node node : nodes) {
                int val = perm.size() + 1;
                for (int i = 1; i <= node.childrenNum; i++) {
                    perm.add(val + i);
                }
                perm.add(val);
            }
        }

        public void dfs(Node root, Node father, List<Node> trace) {
            trace.add(root);
            root.childrenNum = root.next.size() - root.end;
            root.selected = true;
            for (Node node : root.next) {
                if (node == father || !node.tag) {
                    continue;
                }
                dfs(node, root, trace);
                return;
            }
        }

    }
    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
                os.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }
    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 13];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastInput(InputStream is) {
            this.is = is;
        }

        private int read() {
            while (bufLen == bufOffset) {
                bufOffset = 0;
                try {
                    bufLen = is.read(buf);
                } catch (IOException e) {
                    bufLen = -1;
                }
                if (bufLen == -1) {
                    return -1;
                }
            }
            return buf[bufOffset++];
        }

        public void skipBlank() {
            while (next >= 0 && next <= 32) {
                next = read();
            }
        }

        public int readInt() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            int val = 0;
            if (sign == 1) {
                while (next >= '0' && next <= '9') {
                    val = val * 10 + next - '0';
                    next = read();
                }
            } else {
                while (next >= '0' && next <= '9') {
                    val = val * 10 - next + '0';
                    next = read();
                }
            }

            return val;
        }

    }
    static class CompareUtils {
        private CompareUtils() {}

        public static int compareArray(int[] a, int[] b, int al, int ar, int bl, int br) {
            for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
                if (a[i] != b[j]) {
                    return a[i] - b[i];
                }
            }
            return (ar - al) - (br - bl);
        }

    }
    static class Node {
        List<Node> next = new ArrayList<>();
        boolean tag;
        boolean isLeaf;
        int end;
        boolean selected;
        int childrenNum;

    }
    static class IntList {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public int[] getData() {
            return data;
        }

        public IntList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntList(IntList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntList() {
            this(0);
        }

        public void ensureSpace(int need) {
            int req = size + need;
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public int get(int i) {
            checkRange(i);
            return data[i];
        }

        public void add(int x) {
            ensureSpace(1);
            data[size++] = x;
        }

        public int size() {
            return size;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntList)) {
                return false;
            }
            IntList other = (IntList) obj;
            return SequenceUtils.equal(data, other.data, 0, size - 1, 0, other.size - 1);
        }

    }
    static class SequenceUtils {
        public static <T> void swap(List<T> data, int i, int j) {
            T tmp = data.get(i);
            data.set(i, data.get(j));
            data.set(j, tmp);
        }

        public static <T> void reverse(List<T> data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

        public static boolean equal(int[] a, int[] b, int al, int ar, int bl, int br) {
            if ((ar - al) != (br - bl)) {
                return false;
            }
            for (int i = al, j = bl; i <= ar; i++, j++) {
                if (a[i] != b[j]) {
                    return false;
                }
            }
            return true;
        }

    }
}

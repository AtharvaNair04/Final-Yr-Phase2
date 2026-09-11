import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.LongBinaryOperator;

public final class Main {
    void solve(MyScanner in, MyWriter out) {
        int n = in.nextInt();
        int k = in.nextInt();
        int[] a = in.nextIntArray(n);
        var maxs = new LongSegmentTree(300_001, 0, Math::max);
        maxs.update(a[0], 1);
        for (int i = 1; i < n; i++) {
            int max = Math.min(300_000, a[i] + k);
            int min = Math.max(0, a[i] - k);
            maxs.update(a[i], maxs.query(min, max + 1) + 1);
        }
        out.println(maxs.query(0, 300_001));
    }
    static final class LongSegmentTree {
        final int length;
        final long identityElement;
        final LongBinaryOperator op;
        private final int innerLength;
        private final long[] a;

        LongSegmentTree(int length,
                        long identityElement,
                        LongBinaryOperator op) {
            if (length < 0)
                throw new IllegalArgumentException();
            this.length = length;
            this.identityElement = Objects.requireNonNull(identityElement);
            this.op = Objects.requireNonNull(op);

            innerLength = Integer.bitCount(length) == 1
                    ? length
                    : Integer.highestOneBit(length) << 1;

            a = new long[(innerLength << 1) - 1];
            Arrays.fill(a, identityElement);
        }
        static LongSegmentTree min(int length) {
            return new LongSegmentTree(length, Long.MAX_VALUE, Math::min);
        }
        static LongSegmentTree max(int length) {
            return new LongSegmentTree(length, Long.MIN_VALUE, Math::max);
        }
        static LongSegmentTree sum(int length) {
            return new LongSegmentTree(length, 0, Math::addExact);
        }
        void update(int index, long value) {
            Objects.checkIndex(index, length);
            int j = innerLength + index - 1;
            a[j] = value;
            while (j > 0) {
                j = (j - 1) / 2;
                a[j] = op.applyAsLong(a[j * 2 + 1], a[j * 2 + 2]);
            }
        }
        private long _query(int from, int to, int index, int left, int right) {
            if (right <= from || to <= left)
                return identityElement;
            if (from <= left && right <= to)
                return a[index];
            int center = (left + right) >>> 1;
            return op.applyAsLong(_query(from, to, index * 2 + 1, left, center),
                                  _query(from,
                                         to,
                                         index * 2 + 2,
                                         center,
                                         right));
        }
        long query(int fromIndex, int toIndex) {
            Objects.checkFromToIndex(fromIndex, toIndex, length);
            return _query(fromIndex, toIndex, 0, 0, innerLength);
        }
    }
    public static void main(String[] args) {
        MyWriter w = new MyWriter(System.out);
        new Main().solve(new MyScanner(System.in), w);
        w.flush();
    }
    static final class MyScanner {
        private static final int DEFAULT_BUFFER_SIZE = 8192;
        private final InputStream in;
        private final byte[] buffer;
        private int point;
        private int readLength;

        MyScanner(InputStream in) {
            this(in, DEFAULT_BUFFER_SIZE);
        }
        MyScanner(InputStream in, int bufferSize) {
            this.in = Objects.requireNonNull(in);
            buffer = new byte[bufferSize];
        }
        private int readByte() {
            if (point < readLength) {
                int result = buffer[point];
                point += 1;
                return result;
            }
            try {
                readLength = in.read(buffer);
            } catch (IOException e) {
                throw new AssertionError(null, e);
            }
            if (readLength == -1)
                return -1;
            point = 1;
            return buffer[0];
        }
        private static boolean isVisibleChar(int c) {
            return 33 <= c && c <= 126;
        }
        char nextChar() {
            int c = readByte();
            while (!(c == -1 || isVisibleChar(c)))
                c = readByte();
            if (c == -1)
                throw new NoSuchElementException();
            return (char)c;
        }
        String next() {
            return next(16);
        }
        String next(int n) {
            int c = readByte();
            while (!(c == -1 || isVisibleChar(c)))
                c = readByte();
            if (c == -1)
                throw new NoSuchElementException();
            StringBuilder b = new StringBuilder(n);
            do {
                b.append((char)c);
                c = readByte();
            } while (c != -1 && isVisibleChar(c));
            return b.toString();
        }
        long nextLong() {
            int c = readByte();
            while (!(c == -1 || isVisibleChar(c)))
                c = readByte();
            if (c == -1)
                throw new NoSuchElementException();
            boolean minus = false;
            long limit = -Long.MAX_VALUE;
            if (c == '-') {
                minus = true;
                limit = Long.MIN_VALUE;
                c = readByte();
            }
            long n = 0L;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                if (n < limit / 10L)
                    throw new InputMismatchException();
                n *= 10L;
                int digit = c - '0';
                if (n < limit + digit)
                    throw new InputMismatchException();
                n -= digit;
                c = readByte();
            } while (c != -1 && isVisibleChar(c));
            return minus ? n : -n;
        }
        int nextInt() {
            long n = nextLong();
            if (n < Integer.MIN_VALUE || n > Integer.MAX_VALUE)
                throw new InputMismatchException();
            return (int)n;
        }
        double nextDouble() {
            return Double.parseDouble(next());
        }
        int[] nextIntArray(int n) {
            int[] result = new int[n];
            for (int i = 0; i < n; i++)
                result[i] = nextInt();
            return result;
        }
        long[] nextLongArray(int n) {
            long[] result = new long[n];
            for (int i = 0; i < n; i++)
                result[i] = nextLong();
            return result;
        }
        char[] nextCharArray(int n) {
            char[] result = new char[n];
            for (int i = 0; i < n; i++)
                result[i] = nextChar();
            return result;
        }
        int[][] next2dIntArray(int h, int w) {
            var result = new int[h][w];
            for (int i = 0; i < h; i++)
                for (int j = 0; j < w; j++)
                    result[i][j] = nextInt();
            return result;
        }
        long[][] next2dLongArray(int h, int w) {
            var result = new long[h][w];
            for (int i = 0; i < h; i++)
                for (int j = 0; j < w; j++)
                    result[i][j] = nextLong();
            return result;
        }
        char[][] next2dCharArray(int h, int w) {
            char[][] result = new char[h][w];
            for (int i = 0; i < h; i++)
                for (int j = 0; j < w; j++)
                    result[i][j] = nextChar();
            return result;
        }
        int[][] nextVerticalIntArrays(int arrayCount, int arrayLength) {
            int[][] result = new int[arrayCount][arrayLength];
            for (int i = 0; i < arrayLength; i++)
                for (int j = 0; j < arrayCount; j++)
                    result[j][i] = nextInt();
            return result;
        }
        long[][] nextVerticalLongArrays(int arrayCount, int arrayLength) {
            long[][] result = new long[arrayCount][arrayLength];
            for (int i = 0; i < arrayLength; i++)
                for (int j = 0; j < arrayCount; j++)
                    result[j][i] = nextLong();
            return result;
        }
        char[][] nextVerticalCharArrays(int arrayCount, int arrayLength) {
            char[][] result = new char[arrayCount][arrayLength];
            for (int i = 0; i < arrayLength; i++)
                for (int j = 0; j < arrayCount; j++)
                    result[j][i] = nextChar();
            return result;
        }
        List<String> nextStringList(int n) {
            var result = new ArrayList<String>(n);
            for (int i = 0; i < n; i++)
                result.add(next());
            return result;
        }
        List<Integer> nextIntList(int n) {
            var result = new ArrayList<Integer>(n);
            for (int i = 0; i < n; i++)
                result.add(nextInt());
            return result;
        }
        List<Long> nextLongList(int n) {
            var result = new ArrayList<Long>(n);
            for (int i = 0; i < n; i++)
                result.add(nextLong());
            return result;
        }
    }
    static final class MyWriter extends PrintWriter {
        MyWriter(OutputStream out) {
            super(out);
        }
        void println(int[] x) {
            println(x, " ");
        }
        void println(int[] x, String delimiter) {
            Objects.requireNonNull(delimiter);
            if (x.length > 0) {
                print(x[0]);
                for (int i = 1; i < x.length; i++) {
                    print(delimiter);
                    print(x[i]);
                }
            }
            println();
        }
        void println(long[] x) {
            println(x, " ");
        }
        void println(long[] x, String delimiter) {
            Objects.requireNonNull(delimiter);
            if (x.length > 0) {
                print(x[0]);
                for (int i = 1; i < x.length; i++) {
                    print(delimiter);
                    print(x[i]);
                }
            }
            println();
        }
        void println(Iterable<?> iterable) {
            println(iterable, " ");
        }
        void println(Iterable<?> iterable, String delimiter) {
            Objects.requireNonNull(delimiter);
            Iterator<?> i = iterable.iterator();
            if (i.hasNext()) {
                print(i.next());
                while (i.hasNext()) {
                    print(delimiter);
                    print(i.next());
                }
            }
            println();
        }
        void printLines(int[] x) {
            println(x, System.lineSeparator());
        }
        void printLines(long[] x) {
            println(x, System.lineSeparator());
        }
        void printLines(Iterable<?> iterable) {
            println(iterable, System.lineSeparator());
        }
    }
}

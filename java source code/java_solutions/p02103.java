import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {

  int MAX = 1000000;

  class Pos implements Comparable<Pos> {

    final int x, y;

    Pos(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public int compareTo(Pos o) {
      if (this.x != o.x) {
        return this.x - o.x;
      }
      return this.y - o.y;
    }
  }

  private void solve(FastScanner in, PrintWriter out) {
    int N = in.nextInt();
    int M = in.nextInt();
    int K = in.nextInt();

    TreeMap<Pos, Integer> idx = new TreeMap<>();
    ArrayList<Pos> pos = new ArrayList<>();

    Pos upRight = new Pos(0, MAX);
    idx.put(upRight, 0);
    pos.add(upRight);

    Pos downLeft = new Pos(MAX, 0);
    idx.put(downLeft, 1);
    pos.add(downLeft);

    UnionFind unionFind = new UnionFind(K + 2);
    for (int i = 0; i < K; i++) {
      int x = in.nextInt() + 1;
      int y = in.nextInt() + 1;

      Pos p = new Pos(x, y);
      if (!idx.containsKey(p)) {
        idx.put(p, pos.size());
        pos.add(p);
      }
    }

    for (Pos p : pos) {
      if (p.x > N || p.y > M) {
        continue;
      }

      for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
          if (dx == dy && dx == 0) {
            continue;
          }

          int nx = dx + p.x;
          int ny = dy + p.y;
          if (ny <= 0 || nx <= 0 || nx > N || ny > M) {
            continue;
          }

          Pos next = new Pos(nx, ny);
          if (idx.containsKey(next)) {
            int nextIdx = idx.get(next);
            unionFind.unite(nextIdx, idx.get(p));
          }
        }
      }

      if (p.x == 1 || p.y == M) {
        unionFind.unite(idx.get(p), idx.get(upRight));
      }
      if (p.y == 1 || p.x == N) {
        unionFind.unite(idx.get(p), idx.get(downLeft));
      }
    }

    if (unionFind.isSame(idx.get(upRight), idx.get(downLeft))) {
//      pos.add(new Pos(0, kuso(0)));
      out.println(0);
      return;
    }

    TreeSet<Pos> neighbors = new TreeSet<>();
    for (Pos p : pos) {
      if (p.x > N || p.y > M) {
        continue;
      }

      for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
          if (dx == dy && dx == 0) {
            continue;
          }

          int nx = dx + p.x;
          int ny = dy + p.y;
          if (ny <= 0 || nx <= 0 || nx > N || ny > M) {
            continue;
          }

          if (nx == 1 && ny == 1) {
            continue;
          }
          if (nx == N && ny == M) {
            continue;
          }

          Pos next = new Pos(nx, ny);
          if (!idx.containsKey(next)) {
            neighbors.add(next);
          }
        }
      }
    }

    for (Pos next : neighbors) {
      ArrayList<Pos> nine = new ArrayList<>();

      for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
          if (dx == dy && dx == 0) {
            continue;
          }

          int nx = dx + next.x;
          int ny = dy + next.y;
          if (ny <= 0 || nx <= 0 || nx > N || ny > M) {
            continue;
          }

          Pos e = new Pos(nx, ny);
          if (idx.containsKey(e)) {
            nine.add(e);
          }
        }
      }

      for (Pos p1 : nine) {
        for (Pos p2 : nine) {
          if (unionFind.isSame(idx.get(p1), idx.get(upRight)) &&
              unionFind.isSame(idx.get(p2), idx.get(downLeft))) {
//            pos.add(new Pos(0, kuso(1)));
            out.println(1);
            return;
          }
        }
      }

      if (next.x == 1 || next.y == M) {
        for (Pos p : nine) {
          if (unionFind.isSame(idx.get(p), idx.get(downLeft))) {
//            pos.add(new Pos(0, kuso(1)));
            out.println(1);
            return;
          }
        }
      }
      if (next.x == N || next.y == 1) {
        for (Pos p : nine) {
          if (unionFind.isSame(idx.get(p), idx.get(upRight))) {
//            pos.add(new Pos(0, kuso(1)));
            out.println(1);
            return;
          }
        }
      }


    }
//    pos.add(new Pos(0, kuso(2)));
    out.println(2);
  }

  private int kuso(int x) {
    int count = 0;
    for (long i = 0; i < 3000000000L + x * 500000000; i++) {
      count++;
    }
    return count;
  }

  class UnionFind {

    // par[i]????????????i????±?????????¨??????????????????i == par[i]?????¨???????????????i?????¨?????????????????§??????
    private int[] par;
    // sizes[i]???????????????i?????¨?????????????????????????????°???i?????????????????§????????´???????????????????????¨??????
    private int[] sizes;

    // ??¨?????°
    private int size;

    UnionFind(int n) {
      par = new int[n];
      sizes = new int[n];
      size = n;
      Arrays.fill(sizes, 1);
      // ???????????¨???????????????i?????°?????????i????????¨??????????????¨???????????????
      for (int i = 0; i < n; i++) {
        par[i] = i;
      }
    }

    /**
     * ?????????x????±?????????¨???????????????
     */
    int find(int x) {
      if (x == par[x]) {
        return x;
      }
      return par[x] = find(par[x]);  // ????????????????????????????????°???????????????????????¢???
    }

    /**
     * 2???????????????x, y????±?????????¨?????????????????????
     * ???????????????????????? true ?????????
     */
    boolean unite(int x, int y) {
      // ?????????????????????????????????
      x = find(x);
      y = find(y);

      // ??¢???????????¨????±?????????????????????????????????????
      if (x == y) {
        return false;
      }

      // x?????¨???y?????¨????????§???????????????????????????
      if (sizes[x] < sizes[y]) {
        int tx = x;
        x = y;
        y = tx;
      }

      // x???y??????????????????????????£?????????
      par[y] = x;
      sizes[x] += sizes[y];
      sizes[y] = 0;  // sizes[y]????????????????????¨???????????§0?????\????????????????????????

      size--;
      return true;
    }

    /**
     * 2???????????????x, y????±?????????¨???????????????true?????????
     */
    boolean isSame(int x, int y) {
      return find(x) == find(y);
    }

    /**
     * ?????????x?????????????????¨?????§???????????????
     */
    int partialSizeOf(int x) {
      return sizes[find(x)];
    }

    /**
     * ??¨?????°?????????
     */
    int size() {
      return size;
    }
  }

  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out);
    new Main().solve(new FastScanner(), out);
    out.close();
  }

  private static class FastScanner {

    private final InputStream in = System.in;
    private final byte[] buffer = new byte[1024];
    private int ptr = 0;
    private int bufferLength = 0;

    private boolean hasNextByte() {
      if (ptr < bufferLength) {
        return true;
      } else {
        ptr = 0;
        try {
          bufferLength = in.read(buffer);
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (bufferLength <= 0) {
          return false;
        }
      }
      return true;
    }

    private int readByte() {
      if (hasNextByte()) {
        return buffer[ptr++];
      } else {
        return -1;
      }
    }

    private static boolean isPrintableChar(int c) {
      return 33 <= c && c <= 126;
    }

    private void skipUnprintable() {
      while (hasNextByte() && !isPrintableChar(buffer[ptr])) {
        ptr++;
      }
    }

    boolean hasNext() {
      skipUnprintable();
      return hasNextByte();
    }

    public String next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      StringBuilder sb = new StringBuilder();
      int b = readByte();
      while (isPrintableChar(b)) {
        sb.appendCodePoint(b);
        b = readByte();
      }
      return sb.toString();
    }

    long nextLong() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      long n = 0;
      boolean minus = false;
      int b = readByte();
      if (b == '-') {
        minus = true;
        b = readByte();
      }
      if (b < '0' || '9' < b) {
        throw new NumberFormatException();
      }
      while (true) {
        if ('0' <= b && b <= '9') {
          n *= 10;
          n += b - '0';
        } else if (b == -1 || !isPrintableChar(b)) {
          return minus ? -n : n;
        } else {
          throw new NumberFormatException();
        }
        b = readByte();
      }
    }

    double nextDouble() {
      return Double.parseDouble(next());
    }

    double[] nextDoubleArray(int n) {
      double[] array = new double[n];
      for (int i = 0; i < n; i++) {
        array[i] = nextDouble();
      }
      return array;
    }

    double[][] nextDoubleMap(int n, int m) {
      double[][] map = new double[n][];
      for (int i = 0; i < n; i++) {
        map[i] = nextDoubleArray(m);
      }
      return map;
    }

    public int nextInt() {
      return (int) nextLong();
    }

    public int[] nextIntArray(int n) {
      int[] array = new int[n];
      for (int i = 0; i < n; i++) {
        array[i] = nextInt();
      }
      return array;
    }

    public long[] nextLongArray(int n) {
      long[] array = new long[n];
      for (int i = 0; i < n; i++) {
        array[i] = nextLong();
      }
      return array;
    }

    public String[] nextStringArray(int n) {
      String[] array = new String[n];
      for (int i = 0; i < n; i++) {
        array[i] = next();
      }
      return array;
    }

    public char[][] nextCharMap(int n) {
      char[][] array = new char[n][];
      for (int i = 0; i < n; i++) {
        array[i] = next().toCharArray();
      }
      return array;
    }

    public int[][] nextIntMap(int n, int m) {
      int[][] map = new int[n][];
      for (int i = 0; i < n; i++) {
        map[i] = nextIntArray(m);
      }
      return map;
    }
  }
}

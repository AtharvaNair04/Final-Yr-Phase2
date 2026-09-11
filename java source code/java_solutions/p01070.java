import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            pass System Test!
*/

public class Main {
  private static class Task {

    void solve(FastScanner in, PrintWriter out) throws Exception {
      String S = in.next();
      int N = S.length();
      SuffixArray sa = new SuffixArray(S);

      int Q = in.nextInt();
      int[][] query = new int[Q][2];
      ArrayList<int[]> queue = new ArrayList<>();
      for (int q = 0; q < Q; q++) {
        int l = in.nextInt();
        int r = in.nextInt();
        String M = in.next();
        r -= M.length() - 1;
        if (l > r) continue;

        int low = sa.lowerBound(M);
        int up = sa.upperBound(M);
        if (low == -1) continue;
        query[q][0] = low;
        query[q][1] = up;
        queue.add(new int[]{l, 0, q});
        queue.add(new int[]{r, 2, q});
      }
      for (int i = 0; i <= N; i++) {
        queue.add(new int[]{sa.sa[i], 1, i});
      }

      Collections.sort(queue, new Comparator<int[]>() {
        @Override
        public int compare(int[] o1, int[] o2) {
          if (o1[0] != o2[0]) return Integer.compare(o1[0], o2[0]);
          if (o1[1] != o2[1]) return Integer.compare(o1[1], o2[1]);
          return Integer.compare(o1[2], o2[2]);
        }
      });

      int[] ans = new int[Q];
      FenwickTree bit = new FenwickTree(N + 1);

      for (int[] event : queue) {
        int kind = event[1];
        if (kind == 1) {
          int saPos = event[2];
          bit.add(saPos, 1);
        } else if (kind == 0) {
          int q = event[2];
          ans[q] -= bit.sum(query[q][0], query[q][1]);
        } else {
          int q = event[2];
          ans[q] += bit.sum(query[q][0], query[q][1]);
        }
      }

      for (int a : ans) out.println(a);
    }

    class FenwickTree {
      int N;
      long[] data;

      FenwickTree(int N) {
        this.N = N + 1;
        data = new long[N + 1];
      }

      void add(int k, long val) {
        for (int x = k; x < N; x |= x + 1) {
          data[x] += val;
        }
      }

      // [0, k)
      long sum(int k) {
        if (k >= N) k = N - 1;
        long ret = 0;
        for (int x = k - 1; x >= 0; x = (x & (x + 1)) - 1) {
          ret += data[x];
        }
        return ret;
      }

      // [l, r)
      long sum(int l, int r) {
        return sum(r) - sum(l);
      }

      long get(int k) {
        assert (0 <= k && k < N);
        return sum(k + 1) - sum(k);
      }

      int getAsSetOf(int w) {
        w++;
        if (w <= 0) return -1;
        int x = 0;
        int k = 1;
        while (k * 2 <= N) k *= 2;
        for (; k > 0; k /= 2) {
          if (x + k <= N && data[x + k - 1] < w) {
            w -= data[x + k - 1];
            x += k;
          }
        }
        return x;
      }
    }

    class SuffixArray {
      String S;
      int N, K;
      Integer[] sa;
      int[] rank;
      public SuffixArray(String S) {
        this.S = S;
        build();
      }

      private void build() {
        N = S.length();
        rank = new int[N + 1];
        sa = new Integer[N + 1];
        for (int i = 0; i <= N; i++) {
          sa[i] = i;
          rank[i] = i < N ? S.charAt(i) : -1;
        }

        int[] tmp = new int[N + 1];
        for (int _k = 1; _k <= N; _k *= 2) {
          final int k = _k;
          Arrays.sort(sa, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
              return compareNode(i, j, k);
            }
          });
          tmp[sa[0]] = 0;
          for (int i = 1; i <= N; i++) {
            tmp[sa[i]] = tmp[sa[i - 1]] + ((compareNode(sa[i - 1], sa[i], k) < 0) ? 1 : 0);
          }
          for (int i = 0; i <= N; i++) {
            rank[i] = tmp[i];
          }
        }
      }

      private int compareNode(int i, int j, int k) {
        if (rank[i] != rank[j]) {
          return rank[i] - rank[j];
        } else {
          int ri = i + k <= N ? rank[i + k] : -1;
          int rj = j + k <= N ? rank[j + k] : -1;
          return ri - rj;
        }
      }

      public int lowerBound(String t) {
        int a = -1, b = S.length();
        while (b - a > 1) {
          int c = (a + b) / 2;
          String sub = S.substring(sa[c], Math.min(t.length() + sa[c], S.length()));
          if (sub.compareTo(t) < 0)
            a = c;
          else
            b = c;
        }
        String sub = S.substring(sa[b], Math.min(t.length() + sa[b], S.length()));
        return sub.compareTo(t) == 0 ? b : -1;
      }

      public int upperBound(String t) {
        int a = -1, b = S.length() + 1;
        while (b - a > 1) {
          int c = (a + b) / 2;
          String sub = S.substring(sa[c], Math.min(t.length() + sa[c], S.length()));
          if (sub.compareTo(t) <= 0)
            a = c;
          else
            b = c;
        }
        return b;
      }
    }
  }

  /**
   * ?????????????????????????????¬????????§??????
   */
  public static void main(String[] args) throws Exception {
    OutputStream outputStream = System.out;
    FastScanner in = new FastScanner();
    PrintWriter out = new PrintWriter(outputStream);
    Task solver = new Task();
    solver.solve(in, out);
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
      if (hasNextByte()) return buffer[ptr++];
      else return -1;
    }

    private static boolean isPrintableChar(int c) {
      return 33 <= c && c <= 126;
    }

    private void skipUnprintable() {
      while (hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++;
    }

    boolean hasNext() {
      skipUnprintable();
      return hasNextByte();
    }

    public String next() {
      if (!hasNext()) throw new NoSuchElementException();
      StringBuilder sb = new StringBuilder();
      int b = readByte();
      while (isPrintableChar(b)) {
        sb.appendCodePoint(b);
        b = readByte();
      }
      return sb.toString();
    }

    long nextLong() {
      if (!hasNext()) throw new NoSuchElementException();
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
      for (int i = 0; i < n; i++) array[i] = nextInt();
      return array;
    }

    public long[] nextLongArray(int n) {
      long[] array = new long[n];
      for (int i = 0; i < n; i++) array[i] = nextLong();
      return array;
    }

    public String[] nextStringArray(int n) {
      String[] array = new String[n];
      for (int i = 0; i < n; i++) array[i] = next();
      return array;
    }

    public char[][] nextCharMap(int n) {
      char[][] array = new char[n][];
      for (int i = 0; i < n; i++) array[i] = next().toCharArray();
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

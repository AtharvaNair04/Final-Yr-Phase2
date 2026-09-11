import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

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
    boolean oneDimensionSolver(char[] map) {
      int L = map.length;
      int x;
      for (x = 0; x < L; x++) {
        if (map[x] == '@') break;
      }
      {
        int count = 0;
        for (int i = 0; i < x; i++) {
          if (map[i] == 'o') count++;
        }
        int d = L - x;
        for (int i = x + 1; i < L; i++) {
          if (map[i] == 'o') {
            d = i - x;
            break;
          }
        }
        if (count >= d) return true;
      }
      {
        int count = 0;
        for (int i = x; i < L; i++) {
          if (map[i] == 'o') count++;
        }
        int d = x + 1;
        for (int i = x; i >= 0; i--) {
          if (map[i] == 'o') {
            d = x - i;
            break;
          }
        }
        if (count >= d) return true;
      }
      return false;
    }

    void solve(FastScanner in, PrintWriter out) throws Exception {
      int H = in.nextInt();
      int W = in.nextInt();
      char[][] map = new char[H][];
      for (int i = 0; i < H; i++) {
        map[i] = in.next().toCharArray();
      }
      int count = 0;
      int x = -1, y = -1;
      for (int i = 0; i < H; i++) {
        for (int j = 0; j < W; j++) {
          if (map[i][j] == 'o') count++;
          else if (map[i][j] == '@') {
            x = i;
            y = j;
          }
        }
      }

      char[] horizontal = new char[W];
      for (int i = 0; i < W; i++) {
        horizontal[i] = map[x][i];
      }
      char[] vertical = new char[H];
      for (int i = 0; i < H; i++) {
        vertical[i] = map[i][y];
      }
      if (oneDimensionSolver(horizontal) || oneDimensionSolver(vertical)) {
        out.println("yes");
        return;
      }

      if (H >= 2 && W >= 2) {
        if (count >= 3) {
          out.println("yes");
          return;
        }
        if (count == 1) {
          if ((x == 0 || x == H - 1) && (y == 0 || y == W - 1)) {
            out.println("yes");
          } else {
            out.println("no");
          }
        } else {
          if ((x == 0 || x == H - 1) || (y == 0 || y == W - 1)) {
            out.println("yes");
          } else {
            out.println("no");
          }
        }
        return;
      }
      out.println("no");
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

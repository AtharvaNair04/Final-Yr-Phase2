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
@SuppressWarnings("unchecked")
public class Main {
  private static class Task {

    class Edge {
      int to, cost;
      Edge(int to, int cost) {
        this.to = to;
        this.cost = cost;
      }
    }

    void solve(FastScanner in, PrintWriter out) throws Exception {
      int N = in.nextInt();
      int M = in.nextInt();
      int R = in.nextInt();
      int[] B = in.nextIntArray(N);

      int[][] dist = new int[N][N];
      for (int[] d : dist) Arrays.fill(d, Integer.MAX_VALUE / 2);
      for (int i = 0; i < M; i++) {
        int a = in.nextInt() - 1;
        int b = in.nextInt() - 1;
        int c = in.nextInt();

        dist[a][b] = c;
        dist[b][a] = c;

      }

      for (int i = 0; i < N; i++) dist[i][i] = 0;
      for (int k = 0; k < N; k++) {
        for (int i = 0; i < N; i++) {
          for (int j = 0; j < N; j++) {
            dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
          }
        }
      }

      int[][][][] dp = new int[R + 1][N][N][16];
      for (int[][][] x : dp) for (int[][] y : x) for (int[] z : y) Arrays.fill(z, -1);
      dp[0][0][0][0] = 0;
      for (int time = 0; time <= R; time++) {
        for (int cur = 0; cur < N; cur++) {
          for (int prev = 0; prev < N; prev++) {
            for (int x = 0; x <= 15; x++) {
              if (dp[time][cur][prev][x] < 0) continue;
              if (time < R) {
                int y = Math.max(0, x - 1);
                dp[time + 1][cur][prev][y] = Math.max(dp[time + 1][cur][prev][y], dp[time][cur][prev][x]);
              }

              for (int to = 1; to < N; to++) {
                if (to == prev) {
                  int next = time + Math.max(dist[cur][to], x);
                  int y = Math.max(0, 15 - Math.max(dist[cur][to], x));
                  int ball = dp[time][cur][prev][x] + B[to];
                  if (next <= R) dp[next][to][cur][y] = Math.max(dp[next][to][cur][y], ball);
                } else if (to == cur) {
                  int next = time + 15;
                  int ball = dp[time][cur][prev][x] + B[to];
                  if (next <= R) dp[next][to][0][15] = Math.max(dp[next][to][0][15], ball);
                } else {
                  int next = time + dist[cur][to];
                  int y = Math.max(0, 15 - dist[cur][to]);
                  int ball = dp[time][cur][prev][x] + B[to];
                  if (next <= R) dp[next][to][cur][y] = Math.max(dp[next][to][cur][y], ball);
                }
              }
            }
          }
        }
      }

      int max = 0;
      for (int time = 0; time <= R; time++) {
        for (int prev = 0; prev < N; prev++) {
          for (int x = 0; x <= 15; x++) {
            max = Math.max(max, dp[time][N - 1][prev][x]);
          }
        }
      }

      out.println(max);
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

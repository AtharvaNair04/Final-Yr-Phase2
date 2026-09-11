import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Main {

  char[] S;
  int N, K;
  int NAN = -(int) (1e9 + 7);

  private void solve(FastScanner in, PrintWriter out) {
    S = in.next().toCharArray();
    N = S.length;
    K = in.nextInt();

    if (K > N) {
      K = N;
    }

    //n, k, U=0,D=1*2+ on-off L=0,R=1, max
    int[][] dp1 = new int[N + 1][(K + 1) * 4 + 4];
    dp(dp1, 'U', 'D');
    int[][] dp2 = new int[N + 1][(K + 1) * 4 + 4];
    dp(dp2, 'L', 'R');

    int[] m1 = new int[K + 1];
    for (int k = 0; k <= K; k++) {
      int h = 0;
      for (int s = 0; s < 4; s++) {
        h = Math.max(h, dp1[N][k * 4 + s]);
      }
      m1[k] = h;
      if (k > 0) {
        m1[k] = Math.max(m1[k], m1[k - 1]);
      }
    }

    int[] m2 = new int[K + 1];
    for (int k = 0; k <= K; k++) {
      int h = 0;
      for (int s = 0; s < 4; s++) {
        h = Math.max(h, dp2[N][k * 4 + s]);
      }
      m2[k] = h;
      if (k > 0) {
        m2[k] = Math.max(m2[k], m2[k - 1]);
      }
    }
    int ans = 0;
    for (int max = 0; max <= K; max++) {

      ans = Math.max(ans, m1[max] + m2[K - max]);
    }
    out.println(ans);
  }

  private void dp(int[][] dp1, char U, char D) {
    for (int[] p : dp1) {
      Arrays.fill(p, NAN);
    }

    dp1[0][0 * 4 + 0] = 0;
    dp1[0][0 * 4 + 2] = 0;

    for (int i = 0; i < N; i++) {
      for (int k = 0; k <= K; k++) {
        for (int u = 0; u < 2; u++) {
          for (int m = 0; m < 2; m++) {
            int s = u * 2 + m;

            if (dp1[i][k * 4 + s] == NAN) {
              continue;
            }

            if (S[i] != U && S[i] != D) {
              dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s]);
              continue;
            }

            if (S[i] == U) {
              if (u == 0) {
                if (m == 0) {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] + 1);
                } else {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] - 1);
                }
              } else {
                if (m == 0) {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] - 1);
                } else {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] + 1);
                }
              }
            } else {
              if (u == 0) {
                if (m == 0) {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] - 1);
                } else {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] + 1);
                }
              } else {
                if (m == 0) {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] + 1);
                } else {
                  dp1[i + 1][k * 4 + s] = Math.max(dp1[i + 1][k * 4 + s], dp1[i][k * 4 + s] - 1);
                }
              }
            }

            if (k == K) {
              continue;
            }

            int n = m ^ 1;
            int ns = u * 2 + n;
            int k14ns = (k + 1) * 4 + ns;

            if (S[i] == U) {
              if (u == 0) {
                if (m == 0) {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] - 1);
                } else {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] + 1);
                }
              } else {
                if (m == 0) {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] + 1);
                } else {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] - 1);
                }
              }
            } else {
              if (u == 0) {
                if (m == 0) {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] + 1);
                } else {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] - 1);
                }
              } else {
                if (m == 0) {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] - 1);
                } else {
                  dp1[i + 1][k14ns] = Math.max(dp1[i + 1][k14ns], dp1[i][k * 4 + s] + 1);
                }
              }
            }
          }
        }
      }
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

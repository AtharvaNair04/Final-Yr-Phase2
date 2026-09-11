import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Main {

  class Edge {

    final int to, cost;

    Edge(int to, int cost) {
      this.to = to;
      this.cost = cost;
    }
  }

  class State implements Comparable<State> {

    final int v, vi;
    long dist;
    final boolean after;

    State(int v, long dist, int vi, boolean after) {
      this.v = v;
      this.dist = dist;
      this.vi = vi;
      this.after = after;
    }

    @Override
    public int compareTo(State o) {
      return Long.signum(this.dist - o.dist);
    }
  }

  long INF = Long.MAX_VALUE / 100;
  ArrayList<Edge>[] graph;

  @SuppressWarnings("unchecked")
  private void solve(FastScanner in, PrintWriter out) {
    int N = in.nextInt();
    int M = in.nextInt();
    graph = new ArrayList[N];
    for (int i = 0; i < N; i++) {
      graph[i] = new ArrayList<>();
    }

    for (int i = 0; i < M; i++) {
      int x = in.nextInt() - 1;
      int y = in.nextInt() - 1;
      int t = in.nextInt();
      graph[x].add(new Edge(y, t));
      graph[y].add(new Edge(x, t));
    }

    int v0 = in.nextInt();
    int a = in.nextInt();
    int b = in.nextInt();
    int c = in.nextInt();

    PriorityQueue<State> queue = new PriorityQueue<>();
    long[][][] dist = new long[N][51][2];
    for (long[][] d : dist) {
      for (long[] s : d) {
        Arrays.fill(s, INF);
      }
    }

    dist[0][v0][0] = 0;
    queue.add(new State(0, 0, v0, false));
    while (!queue.isEmpty()) {
      State state = queue.poll();

      if (state.v == 0 && state.after) {
        out.println(state.dist);
        return;
      }

      int v = state.v;
      long d = state.dist;
      int vi = state.vi;
      boolean after = state.after;
      if (v == N - 1) {
        after = true;
      }
      int f = after ? 1 : 0;

      for (Edge edge : graph[v]) {
        long addDist = (long) vi * edge.cost;
        int vj = (a * vi + b) % c;
        if (dist[edge.to][vj][f] > d + addDist) {
          dist[edge.to][vj][f] = d + addDist;
          queue.add(new State(edge.to, dist[edge.to][vj][f], vj, after));
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

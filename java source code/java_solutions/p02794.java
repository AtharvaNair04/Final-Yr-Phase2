import java.math.*;
import java.util.*;
import java.util.function.*;
 
public class Main {
  private static long path(int[][] edge, int parent_node, int src_node, int target_node) {
    int[] dist_edges = edge[src_node];
    for (int dist_node = 1; dist_node < dist_edges.length; dist_node++) {
      int edge_no = dist_edges[dist_node];
      if (edge_no == 0) continue;
      if (dist_node == parent_node) continue;
      if (dist_node == target_node) return 1L << edge_no - 1;
      long path = path(edge, src_node, dist_node, target_node);
      if (path == 0) continue;
      return  (1L << edge_no - 1) | path;
    }
    return 0L;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    int[] a = new int[N];
    int[] b = new int[N];
    for (int i = 1; i < N; i++) {
      a[i] = sc.nextInt();
      b[i] = sc.nextInt();
    }
    int M = sc.nextInt();
    int[] u = new int[M];
    int[] v = new int[M];
    for (int i = 0; i < M; i++) {
      u[i] = sc.nextInt();
      v[i] = sc.nextInt();
    }
    sc.close();

    int[][] edge = new int[N + 1][N + 1];
    for (int i = 1; i < N; i++) {
      edge[a[i]][b[i]] = i;
      edge[b[i]][a[i]] = i;
    }

    long[] path = new long[M];
    for (int i = 0; i < M; i++)
      path[i] = path(edge, 0, u[i], v[i]);

    long ans = 0;
    long mmax = 1L << M;
    for (long i = 0; i < mmax; i++) {
      long p = 0;
      long m = i;
      for (int j = 0; m != 0 && j < M; j++) {
        if ((m & 1) == 1) p |= path[j];
        m >>>= 1;
      }
      long ng_case = 1L << (N - 1 - Long.bitCount(p));
      if ((Long.bitCount(i) & 1) == 1) {
        ans -= ng_case;
      } else {
        ans += ng_case;
      }
    }
    System.out.println(ans);
  }
}

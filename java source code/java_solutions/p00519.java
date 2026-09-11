import java.util.*;

public class Main {
  // constant
  static final int MAX_INT = 1 << 30;

  // global variables
  static int[] dists;

  // inner classes

  static class QComp implements Comparator<Integer> {
    public int compare(Integer i0, Integer i1) {
      int cmpd = dists[i0] - dists[i1];
      return  (cmpd != 0) ? cmpd : i0 - i1;
    }
  }

  // main
  public static final void main(String[] args) throws Exception {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int k = sc.nextInt();

    int[][] taxis = new int[n][2];
    for (int i = 0; i < n; i++) {
      taxis[i][0] = sc.nextInt();
      taxis[i][1] = sc.nextInt();
    }

    ArrayList<ArrayList<Integer>> nbrs = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < n; i++)
      nbrs.add(new ArrayList<Integer>());

    for (int i = 0; i < k; i++) {
      int ai = sc.nextInt() - 1;
      int bi = sc.nextInt() - 1;
      nbrs.get(ai).add(bi);
      nbrs.get(bi).add(ai);
    }

    ArrayList<ArrayList<Integer>> tnbrs = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < n; i++)
      tnbrs.add(new ArrayList<Integer>());

    dists = new int[n];

    for (int st = 0; st < n; st++) {
      Arrays.fill(dists, MAX_INT);
      dists[st] = 0;

      ArrayList<Integer> q = new ArrayList<Integer>();
      q.add(st);

      int max_dist = taxis[st][1];
      ArrayList<Integer> tnbrst = tnbrs.get(st);

      while (! q.isEmpty()) {
        Integer ui = q.remove(0);

        int du = dists[ui];
        if (du >= max_dist) continue;

        for (Integer vi: nbrs.get(ui))
          if (dists[vi] >= MAX_INT) {
            dists[vi] = du + 1;
            q.add(vi);
            tnbrst.add(vi);
          }
      }
    }

    Arrays.fill(dists, MAX_INT);
    dists[0] = 0;

    PriorityQueue<Integer> q = new PriorityQueue<Integer>(n, new QComp());
    q.add(0);

    while (! q.isEmpty()) {
      Integer ui = q.poll();
      int u = ui.intValue();
      if (u == n - 1) break;

      int duv = dists[u] + taxis[u][0];

      for (Integer vi: tnbrs.get(u)) {
        int dv = dists[vi];
        if (dv > duv) {
          dists[vi] = duv;
          if (dv < MAX_INT) q.remove(vi);
          q.add(vi);
        }
      }
    }

    System.out.println(dists[n - 1]);
  }
}

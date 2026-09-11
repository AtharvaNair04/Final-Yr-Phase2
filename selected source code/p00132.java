import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
  private final static int MAX_SIZE = 20;
  private final static int MAX_N = 10;

  private final static int MAX_LOC = MAX_SIZE * MAX_SIZE;

  static class Grid {
    int h, w, size;
    int[] vals;

    Grid(int h, int w, String[] strs) {
      this.h = h;
      this.w = w;

      size = 0;
      vals = new int[h];
      Arrays.fill(vals, 0);

      if (strs != null)
        for (int i = 0; i < h; i++) {
          int b = 1;
          for (int j = 0; j < w; b <<= 1, j++)
            if (strs[i].charAt(j) == '#') {
              vals[i] |= b;
              size++;
            }
        }
    }

    Grid(Grid g) {
      h = g.h;
      w = g.w;

      size = g.size;

      vals = new int[h];
      for (int y = 0; y < h; y++) vals[y] = g.vals[y];
    }

    boolean fit(int x0, int y0, Grid pc) {
      for (int y = 0; y < pc.h; y++)
        if ((vals[y0 + y] & (pc.vals[y] << x0)) != 0) return false;

      return true;
    }

    void set(int x0, int y0, Grid pc) {
      for (int y = 0; y < pc.h; y++)
        vals[y0 + y] |= (pc.vals[y] << x0);
    }

    void unset(int x0, int y0, Grid pc) {
      for (int y = 0; y < pc.h; y++)
        vals[y0 + y] &= ~(pc.vals[y] << x0);
    }

    void rotate() {
      int[] vals0 = new int[w];

      for (int y = 0; y < h; y++)
        for (int x = 0; x < w; x++)
          if (((vals[y] >> x) & 1) != 0)
            vals0[w - 1 - x] |= (1 << y);

      int tmp = h; h = w; w = tmp;
      vals = vals0;
    }

    boolean equals(Grid g) {
      if (h != g.h || w != g.w) return false;
      for (int i = 0; i < h; i++)
        if (vals[i] != g.vals[i]) return false;
      return true;
    }

    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(String.format("[h=%d, w=%d, size=%d]\n", h, w, size));
      for (int i = 0; i < h; i++) {
        int v = vals[i];
        for (int j = 0; j < w; j++)
          sb.append(String.format("%d", (v >> j) & 1));
        sb.append("\n");
      }
      return sb.toString();
    }
  }

  static class Point {
    int x, y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public String toString() { return "(" + x + "," + y + ")"; }
  }

  static class PcLoc {
    Grid pc;
    ArrayList<Point> locs;

    PcLoc(Grid pc, ArrayList<Point> locs) {
      this.pc = pc;
      this.locs = locs;
    }
  }

  static class PcPat {
    ArrayList<PcLoc> pcls;

    PcPat() {
      pcls = new ArrayList<PcLoc>();
    }

    void add(PcLoc pcl) { pcls.add(pcl); }
    PcLoc get(int i) { return pcls.get(i); }
    int size() { return pcls.size(); }
  }

  static class PcPatComparator implements Comparator<PcPat> {
    public int compare(PcPat a, PcPat b) {
      return (b.get(0).pc.size - a.get(0).pc.size);
    }
  }

  static ArrayList<Point> find_locs(Grid pc, Grid fr) {
    int max_h = fr.h - pc.h;
    int max_w = fr.w - pc.w;

    ArrayList<Point> locs = new ArrayList<Point>();

    for (int y = 0; y <= max_h; y++)
      for (int x = 0; x <= max_w; x++)
        if (fr.fit(x, y, pc))
          locs.add(new Point(x, y));

    return locs;
  }

  static boolean check_blank(int k, PcPat[] pcpats, Grid fr) {
    Grid fr0 = new Grid(fr);

    for (int k0 = k; k0 < pcpats.length; k0++) {
      PcPat pcpat = pcpats[k0];

      for (int i = 0; i < pcpat.size(); i++) {
        PcLoc pcl = pcpat.get(i);

        for (int j = 0; j < pcl.locs.size(); j++) {
          Point pt = pcl.locs.get(j);
          if (fr.fit(pt.x, pt.y, pcl.pc))
            fr0.set(pt.x, pt.y, pcl.pc);
        }
      }
    }

    int allbits = (1 << fr0.w) - 1;
    for (int y = 0; y < fr0.h; y++)
      if (fr0.vals[y] != allbits) return false;
    return true;
  }

  static boolean check_pieces(int k, PcPat[] pcpats, Grid fr) {
    if (k >= pcpats.length) return true;

    PcPat pcpat = pcpats[k];

    for (int i = 0; i < pcpat.size(); i++) {
      PcLoc pcl = pcpat.get(i);

      for (int j = 0; j < pcl.locs.size(); j++) {
        Point pt = pcl.locs.get(j);
        if (fr.fit(pt.x, pt.y, pcl.pc)) {
          fr.set(pt.x, pt.y, pcl.pc);
          if (check_blank(k + 1, pcpats, fr) &&
              check_pieces(k + 1, pcpats, fr))
            return true;
          fr.unset(pt.x, pt.y, pcl.pc);
        }
      }
    }

    return false;
  }

/* main */

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line;
    String[] strs;

    while (true) {
      String[] hw = br.readLine().trim().split(" ");
      int h = Integer.parseInt(hw[0]);
      int w = Integer.parseInt(hw[1]);
      if (h == 0 && w == 0) break;

      strs = new String[h];
      for (int j = 0; j < h; j++)
        strs[j] = br.readLine().trim();

      Grid frame = new Grid(h, w, strs);
      int framesize = h * w - frame.size;
      /*System.out.println(frame);*/

      int n = Integer.parseInt(br.readLine().trim());

      PcPat[] pcpats = new PcPat[n];

      for (int i = 0; i < n; i++) {
        String[] phw = br.readLine().trim().split(" ");
        int ph = Integer.parseInt(phw[0]);
        int pw = Integer.parseInt(phw[1]);

        strs = new String[ph];
        for (int j = 0; j < ph; j++)
          strs[j] = br.readLine().trim();

        Grid pc0 = new Grid(ph, pw, strs);
        PcPat pcpat = pcpats[i] = new PcPat();

        for (int j = 0; j <= 3; j++) {
          boolean inc = false;
          for (int k = 0; k < pcpat.size(); k++)
            if (pcpat.get(k).pc.equals(pc0)) {
              inc = true;
              break;
            }

          if (! inc) {
            ArrayList<Point> locs0 = find_locs(pc0, frame);
            if (locs0.size() > 0)
              pcpat.add(new PcLoc(pc0, locs0));
          }

          Grid pc1 = new Grid(pc0);
          pc1.rotate();
          pc0 = pc1;
        }

        if (false) {
          System.out.println("PcPat(" + i + "):size = " + pcpat.size());
          for (int j = 0; j < pcpat.size(); j++) {
            PcLoc pcl = pcpat.get(j);
            System.out.print(pcl.pc);
            System.out.print("locs size = " + pcl.locs.size() + ": ");
            for (int k = 0; k < pcl.locs.size(); k++)
              System.out.print(pcl.locs.get(k));
            System.out.println();
          }
        }
      }

      int pi = Integer.parseInt(br.readLine().trim());

      for (int i = 0; i < pi; i++) {
        String[] pns = br.readLine().trim().split(" ");

        int np = Integer.parseInt(pns[0]);
        int[] pcs = new int[np];

        int ssize = 0;
        for (int j = 0; j < np; j++) {
          pcs[j] = Integer.parseInt(pns[j + 1]) - 1;
          PcLoc pcl = pcpats[pcs[j]].pcls.get(0);
          if (pcl == null) {
            ssize = -1;
            break;
          }
          ssize += pcl.pc.size;
        }

        if (ssize != framesize) {
          System.out.println("NO");
          continue;
        }

        Grid frame0 = new Grid(frame);
        PcPat[] pcset = new PcPat[np];

        for (int j = 0; j < np; j++)
          pcset[j] = pcpats[pcs[j]];

        Arrays.sort(pcset, new PcPatComparator());

        boolean tf = check_pieces(0, pcset, frame0);
        System.out.println(tf ? "YES" : "NO");
      }
    }
  }
}

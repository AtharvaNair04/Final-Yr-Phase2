import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
  private static class Solver1 {
    private static class VW implements Comparable<VW> {
      private long v;
      private long w;
      private VW(long v, long w) {
        this.v = v;
        this.w = w;
      }

      @Override
      public int compareTo(VW o) {
        return Long.compare(w, o.w);
      }

      @Override public String toString() {
        return String.format("%d:%d", v, w);
      }
    }
    private long solve(long W, List<Long> values, List<Long> weights) {
      int N = values.size();
      List<VW> svw1 = getCombs(values.subList(0, N / 2), weights.subList(0, N / 2));
      List<VW> svw2 = getCombs(values.subList(N / 2, N), weights.subList(N / 2, N));
      svw1 = sortAndRemove(svw1, W);
      svw2 = sortAndRemove(svw2, W);
      long res = 0;
      for (VW svw : svw1) {
        long v = svw.v;
        long w = svw.w;
        long target = W - w;
        if (target < 0) {
          continue;
        }
        int i = Collections.binarySearch(svw2, new VW(0, target));
        //if (i >= 0) {
        //  while (i < N && svw2.get(i).w == target) {
        //    ++i;
        //  }
        //  i = i - 1;
        //}
        if (i < 0) {
          i = -i - 1;
          i = i - 1;
        }
        if (i < 0) {
          continue;
        }
        res = Math.max(res, v + svw2.get(i).v);
      }
      return res;
    }

    private List<VW> getCombs(List<Long> values, List<Long> weights) {
      int n = values.size();
      List<VW> ret = new ArrayList<>();
      for (int i = 0; i < 1 << n; ++i) {
        long v = 0L;
        long w = 0L;
        for (int j = 0; j < n; ++j) {
          if (0x1 == ((i >> j) & 0x1)) {
            v += values.get(j);
            w += weights.get(j);
          }
        }
        ret.add(new VW(v, w));
      }
      return ret;
    }

    private List<VW> sortAndRemove(List<VW> vws, long W) {
      Collections.sort(vws);
      List<VW> ret = new ArrayList<>();
      VW prev = null;
      for (VW vw : vws) {
        if (null == prev || vw.v > prev.v) {
          prev = vw;
          ret.add(vw);
        }
      }
      return ret.stream().filter(i -> i.w <= W).collect(Collectors.toList());
    }
  }

  private static class Solver2 {
    private static class VW implements Comparable<VW> {
      private final long v;
      private final long w;
      private VW(long v, long w) {
        this.v = v;
        this.w = w;
      }
      @Override
      public int compareTo(VW o) {
        return Long.compare(w, o.w);
      }
    }

    private long solve(long W, List<Long> values, List<Long> weights) {
      List<VW> vws = new ArrayList<>();
      for (int i = 0; i < values.size(); ++i) {
        vws.add(new VW(values.get(i), weights.get(i)));
      }
      Collections.sort(vws, Collections.reverseOrder());
      List<VW> svws = new ArrayList<>();
      svws.add(vws.get(0));
      for (int i = 1; i < vws.size(); ++i) {
        VW vw = vws.get(i);
        VW svw = svws.get(i - 1);
        svws.add(new VW(vw.v + svw.v, vw.w + svw.w));
      }
      long[] res = {0};
      dfs(W, svws, 0, res);
      return res[0];
    }

    private void dfs(long W, List<VW> svws, long sv, long[] res) {
      if (W <= 0 || svws.isEmpty()) {
        res[0] = Math.max(res[0], sv);
        return;
      }
      VW svw = svws.get(svws.size() - 1);
      if (W >= svw.w) {
        res[0] = Math.max(res[0], sv + svw.v);
        return;
      }
      long v = svw.v;
      long w = svw.w;
      if (svws.size() > 1) {
        v = v - svws.get(svws.size() - 2).v;
        w = w - svws.get(svws.size() - 2).w;
      }
      if (W - w < 0) {
        res[0] = Math.max(res[0], sv);
        return;
      }
      dfs(W - w, svws.subList(0, svws.size() - 1), sv + v, res);
      dfs(W, svws.subList(0, svws.size() - 1), sv, res);
    }
  }

  private static final Pattern SPLIT = Pattern.compile("\\s");
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    List<Long> weights = new ArrayList<>();
    List<Long> values = new ArrayList<>();
    List<Long> NW = Stream.of(SPLIT.split(scanner.nextLine())).map(Long::valueOf)
        .collect(Collectors.toList());
    final int N = (int) NW.get(0).longValue();
    final long W = NW.get(1);
    for (int i = 0; i < N; ++i) {
      List<Long> collect = Stream.of(SPLIT.split(scanner.nextLine())).map(Long::valueOf)
          .collect(Collectors.toList());
      values.add(collect.get(0));
      weights.add(collect.get(1));
    }
    System.out.println(new Solver1().solve(W, values, weights));
  }
}

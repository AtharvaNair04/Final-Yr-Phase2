import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

  public static void main(String[] args) {
    resolve(System.in, System.out);
  }

  static void resolve(InputStream is, PrintStream pw) {
    Scanner sc = new Scanner(is);
    long n = Long.parseLong(sc.next());
    if (n == 1) {
      pw.println(0);
      return;
    }
    SortedMap<Long, AtomicInteger> soinus = primeFactorization(n);
    SortedMap<Integer, AtomicInteger> eCountMap = new TreeMap<>();
    for (AtomicInteger v : soinus.values()) {
      eCountMap
        .computeIfAbsent(v.intValue(), k -> new AtomicInteger(0))
        .incrementAndGet();
    }
    long ans = 0;
    int totalE = 0;
    for (int i = 1; totalE <= eCountMap.lastKey(); i++) {
      totalE += i;
      long nextTotalE = totalE + i + 1;
      for (int j = totalE; j < nextTotalE; j++) {
        if (eCountMap.containsKey(j)) {
          ans += eCountMap.get(j).intValue() * i;
        }
      }
    }
    pw.println(ans);
  }

  static SortedMap<Long, AtomicInteger> primeFactorization(long n) {
    if (n == 1) {
      return new TreeMap<>();
    }
    SortedMap<Long, AtomicInteger> ans = new TreeMap<>();
    while (n % 2 == 0) {
      ans.computeIfAbsent(
        2l,
        k -> new AtomicInteger(0)
      ).incrementAndGet();
      n /= 2;
    }
    for (long i = 3; i * i < n; i = i + 2) {
      while (n % i == 0) {
        ans.computeIfAbsent(
          i,
          k -> new AtomicInteger(0)
        ).incrementAndGet();
        n /= i;
      }
    }
    if (n != 1) {
      ans.put(n, new AtomicInteger(1));
    }
    return ans;
  }
}

import java.util.*;
import java.math.*;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    int[] a = new int[N];
    for (int i = 0; i < N; i++)
      a[i] = sc.nextInt();
    sc.close();

    long[][] memo = new long[N][N];
    long ans = fN(memo, a, 0, N - 1);
    System.out.println(ans);
  }

  private static long fN(long[][] memo, int[] a, int l, int r) {
    if (memo[l][r] != 0) return memo[l][r];
    if (l == r) return 0;
    long min = Long.MAX_VALUE;
    long sum = a[r];
    for (int i = l; i < r; i++) {
      min = Long.min(min, fN(memo, a, l, i) + fN(memo, a, i + 1, r));
      sum += a[i];
    }
    memo[l][r] = sum + min;
    return memo[l][r];
  }
}

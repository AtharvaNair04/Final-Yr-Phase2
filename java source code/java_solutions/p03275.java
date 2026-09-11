import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int N = scanner.nextInt();
    int[] a = new int[N];
    for (int i = 0; i < N; i++) {
      a[i] = scanner.nextInt();
    }
    int[] sorted = Arrays.copyOf(a, a.length);
    Arrays.sort(sorted);

    long target = N;
    target = (target * (N + 1) / 2 + 1) / 2;
    int left = -1, right = N;
    while (right - left > 1) {
      int mid = (left + right) / 2;
      long count = numSubarraysWhooseMedianBiggerThan(a, sorted[mid]);
      if (target <= count) {
        left = mid;
      } else {
        right = mid;
      }
    }
    System.out.println(sorted[left]);
  }

  private static long numSubarraysWhooseMedianBiggerThan(int[] a, int t) {
    int[] b = new int[a.length + 1];
    b[0] = 0;
    int sum = 0;
    for (int i = 0; i < a.length; i++) {
      sum += a[i] >= t ? 1 : -1;
      b[i + 1] = sum;
    }
    long n = a.length;
    return n * (n + 1) / 2 - countInversion(b, 0, b.length);
  }

  private static long countInversion(int[] a, int left, int right) {
    if (left + 1 >= right) return 0;
    int mid = (left + right) / 2;
    long count = countInversion(a, left, mid) + countInversion(a, mid, right);
    int[] b = new int[right - left];
    int i = 0;
    int li = left;
    int ri = mid;
    while (li < mid && ri < right) {
      if (a[li] <= a[ri]) {
        b[i++] = a[li++];
      } else {
        b[i++] = a[ri++];
        count += mid - li;
      }
    }
    while (li < mid) b[i++] = a[li++];
    while (ri < right) b[i++] = a[ri++];
    System.arraycopy(b, 0, a, left, b.length);
    return count;
  }
}

import java.util.*;
import java.util.TreeMap;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    int K = sc.nextInt();
    int Q = sc.nextInt();
    int[] A = new int[N];
    int[] P = new int[Q];
    Arrays.fill(A, K - Q);
    for (int i = 0; i < Q; i++) {
      A[sc.nextInt() - 1]++;
    }
    for (int i = 0; i < N; i++) {
      if (A[i] > 0) {
        System.out.println("Yes");
      } else {
        System.out.println("No");
      }
    }
  }
}

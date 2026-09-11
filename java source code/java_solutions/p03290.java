import java.util.Scanner;

class Main {

  private static int D, G;
  private static int[] p, c;

  private static int INF = Integer.MAX_VALUE / 4;

  public static int rec(int i, int sum, int s) {

    if (sum >= G) {
      return 0;
    }

    if (i == D + 1) {
      int cnt = 0;
      for (int j = D; j > 0; j--) {
        if ((s >> j - 1 & 1) == 1) {
          if (sum + j * (p[j - 1] - 1) < G) {
            continue;
          }
          while (sum < G) {
            sum += j;
            cnt++;
          }
          return cnt;
        }
      }
      return INF;
    }

    int res0 = rec(i + 1, sum + i * p[i - 1] + c[i - 1], s - (1 << i - 1)) + p[i - 1];
    int res1 = rec(i + 1, sum, s);

    return Math.min(res0, res1);

  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    D = scanner.nextInt();
    G = scanner.nextInt() / 100;
    p = new int[D];
    c = new int[D];
    for (int i = 0; i < D; i++) {
      p[i] = scanner.nextInt();
      c[i] = scanner.nextInt() / 100;
    }
    System.out.println(rec(1, 0, (1 << D) - 1));
  }

}

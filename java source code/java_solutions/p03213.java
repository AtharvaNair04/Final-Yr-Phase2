import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int[] primes =
        new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73,
            79, 83, 89, 97};
    int[] countPrimes = new int[25];
    int N = scanner.nextInt();

    for (int i = 2; i <= N; i++) {
      int n = i;
      for (int j = 24; j >= 0; j--) {
        int p = primes[j];
        while (n >= p && n % p == 0) {
          countPrimes[j]++;
          n /= p;
        }
      }
    }

    int over4 = 0;
    int over2 = 0;
    int over14 = 0;
    int over24 = 0;
    int over74 = 0;
    for (int i = 0; i < 25; i++) {
      int c = countPrimes[i];

      if (c >= 2) over2++;
      if (c >= 4) over4++;
      if (c >= 14) over14++;
      if (c >= 24) over24++;
      if (c >= 74) over74++;
    }

    System.out.println(
        over74
        + over24 * (over2 > 0 ? over2 - 1 : 0)
        + over14 * (over4 > 0 ? over4 - 1 : 0)
        + over4 * (over4 > 0 ? over4 - 1 : 0) * (over2 > 1 ? over2 - 2 : 0) / 2
    );
  }
}

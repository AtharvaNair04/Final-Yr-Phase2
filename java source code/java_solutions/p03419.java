import java.util.Scanner;

class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    long N = scanner.nextLong();
    long M = scanner.nextLong();
    if (N >= 3 && M >= 3) {
      System.out.println((N - 2) * (M - 2));
      return;
    } else if (N == 2 || M == 2) {
      System.out.println(0);
      return;
    } else {
      System.out.println(Math.max(Math.max(N, M) - 2, 1));
      return;
    }
  }

}

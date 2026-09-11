import java.util.Scanner;

class Main {

  private static final long INF = Long.MAX_VALUE / 4;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    long N = scanner.nextLong();
    long[] time = new long[5];
    for (int i = 0; i < 5; i++) {
      time[i] = scanner.nextLong();
    }
    long minTime = INF;
    for (int i = 0; i < 5; i++) {
      if (time[i] < minTime) {
        minTime = time[i];
      }
    }
    long ex = (N - 1) / minTime;
    System.out.println(5L + ex);
  }

}

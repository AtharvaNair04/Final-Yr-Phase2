import java.util.Scanner;

class Main {
  public static int min(int a, int b) {
    return a < b ? a : b;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    int[][] counts = new int[2][100001];
    for (int i = 0; i < n; i++) {
      counts[i % 2][scanner.nextInt()]++;
    }
    int[][] maxCnt = { { 0, 0 }, { 0, 0 } };
    int[] maxValue = { 0, 0 };
    for (int i = 0; i <= 100000; i++) {
      for (int j = 0; j < 2; j++) {
        if (counts[j][i] > maxCnt[j][0]) {
          maxCnt[j][1] = maxCnt[j][0];
          maxCnt[j][0] = counts[j][i];
          maxValue[j] = i;
        } else if (counts[j][i] > maxCnt[j][1]) {
          maxCnt[j][1] = counts[j][i];
        }
      }
    }
    int[] e1 = { maxCnt[0][0], maxValue[0] }, o1 = { maxCnt[1][0], maxValue[1] };
    int e2 = maxCnt[0][1], o2 = maxCnt[1][1];
    if (e1[1] != o1[1]) {
      System.out.println(n - e1[0] - o1[0]);
    } else {
      System.out.println(min(n - e1[0] - o2, n - e2 - o1[0]));
    }
  }
}

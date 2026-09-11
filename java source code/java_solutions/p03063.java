import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int N = scanner.nextInt();
    char[] s = scanner.next().toCharArray();

    int[] numb = new int[N];
    int[] numw = new int[N];
    int countb = 0;
    for (int i = 0; i < N; i++) {
      numb[i] = countb;
      if (s[i] == '#') {
        countb++;
      }
    }
    int countw = N - countb;
    int min = Math.min(countb, countw);
    for (int i = 0; i < N; i++) {
      numw[i] = countw;
      if (s[i] == '.') {
        countw--;
      }
    }

    for (int i = 0; i < N; i++) {
      min = Math.min(min, numb[i] + numw[i]);
    }
    System.out.println(min);
  }
}

import java.util.Arrays;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt(),
        m = sc.nextInt(),
        x = sc.nextInt(),
        y = sc.nextInt();
    int[] x_chil = new int[n];
    int[] y_chil = new int[m];
    for (int i = 0; i < n; i++) {
      x_chil[i] = sc.nextInt();
    }
    for (int i = 0; i < m; i++) {
      y_chil[i] = sc.nextInt();
    }
    Arrays.sort(x_chil);
    Arrays.sort(y_chil);
    System.out.println(x_chil[n - 1] < y_chil[0] && x < y_chil[0] && y > x_chil[n - 1]? "No War" : "War");
  }

}

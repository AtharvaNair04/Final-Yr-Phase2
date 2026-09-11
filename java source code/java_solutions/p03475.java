import java.util.*;

public class Main {

  public static void main (String[] args) {

    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] C = new int[N];
    int[] S = new int[N];
    int[] F = new int[N];

    for (int i = 0; i < N-1; i++) {
      C[i] = sc.nextInt();
      S[i] = sc.nextInt();
      F[i] = sc.nextInt();
    }

    sc.close();

    for (int i = 0; i < N; i++) {
      
      int t = 0;

      for (int j = i; j < N -1; j++) {
        if (t < S[j]) {
          t = S[j];
        } else if (t % F[j] != 0) {
          t = t + F[j] - t % F[j];
        }

        t += C[j];
      }

      System.out.println(t);
    }
  
  }

}

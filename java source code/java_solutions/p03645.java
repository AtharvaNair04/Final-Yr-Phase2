import java.util.Scanner;

public class Main{
  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);
    int N = scanner.nextInt();
    int M = scanner.nextInt();
    int[] a = new int[M];
    int[] b = new int[M];
    boolean[] adj1 = new boolean[N + 1];
    boolean[] adj2 = new boolean[N + 1];
    boolean flag = false;
    for (int i = 0; i < M; i++){
      a[i] = scanner.nextInt();
      b[i] = scanner.nextInt();
    }
    for (int i = 0; i < M; i++){
      if (a[i] == 1){
        adj1[b[i]] = true;
      } else if (b[i] == N){
        adj2[a[i]] = true;
      }
    }
    for (int k = 0; k < N; k++){
      if (adj1[k] == true & adj2[k] == true){
        flag = true;
        if (flag = true){
          System.out.println("POSSIBLE");
          break;
        }
      } else if (k == N - 1 & flag == false){
        System.out.println("IMPOSSIBLE");
      }
    }
  }
}

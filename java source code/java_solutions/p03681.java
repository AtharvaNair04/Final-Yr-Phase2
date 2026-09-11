import java.util.Scanner;

public class Main{
  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);
    long N = scanner.nextLong();
    long M = scanner.nextLong();
    long Nx = 1;
    long Mx = 1;
    long ans = 0;
    for (int i = 1; i <= N; i++){
      Nx = Nx * i % 1000000007;
    }
    for (int i = 1; i <= M; i++){
      Mx = Mx * i % 1000000007;
    }
    if(N == M){
      ans = 2 * Nx * Mx % 1000000007;
    } else if (N == M - 1 || N == M + 1){
      ans =  Nx * Mx % 1000000007;
    } else {
      ans = 0;
    }
    System.out.println(ans);
  }
}

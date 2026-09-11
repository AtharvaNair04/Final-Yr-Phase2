import java.util.Arrays;
import java.util.Scanner;

public class Main {

  static int[] pp;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    String X = sc.next();
    int count1 = (int) X.chars().filter(ch -> ch == '1').count();
    int popcntxp1 = count1 + 1;
    int popcntxm1 = Integer.max(count1 - 1, 0);

    pp = new int[N + 1];
    Arrays.fill(pp, -1);
    pp[0] = 0;
    pp[1] = 1;

    int[][] fa = new int[N][2];
    fa[0][0] = (popcntxm1 > 1) ? 1 : 0;
    fa[0][1] = (popcntxp1 > 1) ?  1 : 0;
    for (int i = 1; i < N; i++) {
      fa[i][0] = (popcntxm1 > 1) ? (fa[i - 1][0] * 2) % popcntxm1 : 0;
      fa[i][1] = (fa[i - 1][1] * 2) % popcntxp1;
    }

    int answerxm1 = 0;
    int answerxp1 = 0;

    for (int i = 0; i < N; i++) {
      if (X.charAt(i) == '1') {
        answerxm1 = (popcntxm1 > 1) ? (answerxm1 + fa[N - 1 - i][0]) % popcntxm1 : 0;
        answerxp1 = (answerxp1 + fa[N - 1 - i][1]) % popcntxp1;
      }
    }

    for (int i = 0; i < N; i++) {
      int tmp = 0;
      if(X.charAt(i) == '1'){
        if(popcntxm1 < 2){
          System.out.println(popcount(popcntxm1));
        } else {
          tmp = Math.abs(popcntxm1 + answerxm1 - fa[N - 1 - i][0]) % popcntxm1;
          System.out.println(popcount(tmp) + 1);
        }
      } else {
        if(answerxp1 == 1){
          System.out.println(popcount(answerxp1));
        } else {
          tmp = Math.abs(answerxp1 + fa[N - 1 - i][1]) % popcntxp1;
          System.out.println(popcount(tmp) + 1);
        }
      }
    }
  }
  static private int popcount(int n) {
    if (pp[n] == -1) {
      pp[n] = popcount(n % Integer.bitCount(n)) + 1;
    }
    return pp[n];
  }
}

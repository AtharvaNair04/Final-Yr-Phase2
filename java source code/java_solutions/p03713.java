import java.util.*;

public class Main {

  static long H;
  static long W;

  public static void main (String[] args) {

    Scanner sc = new Scanner(System.in);

    H = sc.nextLong();
    W = sc.nextLong();

    sc.close();

    long result = Math.min(cal(H, W), cal(W, H));

    System.out.println(result);

  }

  public static long cal(long a, long b) {
    
    long min = Long.MAX_VALUE;

    for (int i = 1; i < a; i++) {

      long S1 = i * b;
      long H1 = a - i;

      // case 1 
      long H2 = H1 / 2;
      long H3 = H1 - H2;
      long S2 = H2 * b;
      long S3 = H3 * b;
      min = Math.min(min, compare(S1, S2, S3));
      
      // case 2
      long W2 = b / 2;
      long W3 = b - W2;
      S2 = W2 * H1;
      S3 = W3 * H1;
      min = Math.min(min, compare(S1, S2, S3));

    }
  
    return min;

  }

  public static long compare(long S1, long S2, long S3){

    return Math.max(Math.abs(S1 - S2), Math.max(Math.abs(S2 - S3), Math.abs(S3 - S1)));

  }

}

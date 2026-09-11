import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int a = sc.nextInt();
    int b = sc.nextInt();
    int c = sc.nextInt();
    int k = sc.nextInt();
    int max = a;
    if (max < b) {
      max = b;
    }
    if (max < c) {
      max = c;
    }
    System.out.println(a + b + c + max * ((1 << k) - 1));
  }

}

import java.util.Scanner;

class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    int a = scanner.nextInt();
    int b = scanner.nextInt();
    scanner.close();
    int max = a < b ? a : b;
    int min = n < a + b ? (a + b) - n : 0;
    System.out.println(max + " " + min);
  }

}

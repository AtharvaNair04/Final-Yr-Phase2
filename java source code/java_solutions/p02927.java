import java.util.Scanner;

class Main {
  public static void main(String args[]) {

    Scanner sc = new Scanner(System.in);

    int month = sc.nextInt();
    int day = sc.nextInt();

    int tdCounter = 0;

    if (month < 1 || 100 < month || day < 1 || 100 <= day) {
      return;
    }

    for (int i = 1; month >= i; i++) {
      for (int n = 1; day >= n; n++) {
        int tens = n / 10;
        int ones = n % 10;
        if (tens > 1  && ones > 1) {
          if (i == tens * ones) {
            tdCounter++;
          }
        }

      }
    }
    System.out.println(tdCounter);
    return;
  }
}

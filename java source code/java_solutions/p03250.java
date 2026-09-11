import java.util.Arrays;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int[] numbers = new int[3];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = sc.nextInt();
    }
    Arrays.sort(numbers);
    System.out.println(numbers[2] * 10 + numbers[1] + numbers[0]);
  }

}

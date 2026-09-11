import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int day = sc.nextInt();
    String ans = new String();

    switch (day) {
    case 25:
      ans = "Christmas";
      break;
    case 24:
      ans = "Christmas Eve";
      break;
    case 23:
      ans = "Christmas Eve Eve";
      break;
    case 22:
      ans = "Christmas Eve Eve Eve";
      break;
    }
    System.out.println(ans);
  }

}

import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int num = in.nextInt();
    int timelimit = in.nextInt();
    int costmin = 0;
    for (int i = 0; i < num; i++) {
      int cost = in.nextInt();
      int time = in.nextInt();
      if (timelimit < time) {
        continue;
      }
      if (costmin == 0 || costmin > cost) {
        costmin = cost;
      }
    }
    if (costmin == 0) {
      System.out.println("TLE");
    } else {
      System.out.println(costmin);
    }
  }
}

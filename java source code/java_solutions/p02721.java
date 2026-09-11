import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    resolve(System.in, System.out);
  }

  static int MAX_INDEX = 200000;

  static void resolve(InputStream is, PrintStream pw) {
    Scanner sc = new Scanner(is);
    int n = Integer.parseInt(sc.next());
    int k = Integer.parseInt(sc.next());
    int c = Integer.parseInt(sc.next());
    String s = sc.next();
    boolean workable[] = new boolean[n];
    for (int i = 0; i < n; i++) {
      workable[i] = s.charAt(i) == 'o';
    }
    int[] leftGreedyWorkingDay = new int[MAX_INDEX];
    int[] rightGreedyWorkingDay = new int[MAX_INDEX];
    {
      // left greedy
      int restDays = MAX_INDEX;
      int workingCount = 0;
      for (int i = 0; i < n; i++) {
        if (workable[i] && restDays >= c) {
          workingCount++;
          leftGreedyWorkingDay[i] = workingCount;
          restDays = 0;
        } else {
          restDays++;
        }
      }
    }
    {
      // right greedy
      int restDays = MAX_INDEX;
      int workingCount = k;
      for (int i = n - 1; i >= 0; i--) {
        if (workable[i] && restDays >= c) {
          rightGreedyWorkingDay[i] = workingCount;
          workingCount--;
          restDays = 0;
        } else {
          restDays++;
        }
      }
    }
    for (int i = 0; i < n; i++) {
      if (leftGreedyWorkingDay[i] == rightGreedyWorkingDay[i] && leftGreedyWorkingDay[i] != 0) {
        pw.println(i + 1);
      }
    }
    pw.println();
  }
}

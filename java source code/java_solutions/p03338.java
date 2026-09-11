import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    String s = sc.next();
    int ans = 0;
    for (int i = 0; i < n; i++) {
      int count = 0;
      for (char c = 'a'; c <= 'z'; c++) {
        boolean left = false, right = false;
        for (int j = 0; j < i; j++) {
          if (s.charAt(j) == c)
            left = true;
        }
        for (int j = i; j < n; j++) {
          if (s.charAt(j) == c)
            right = true;
        }
        if (left && right)
          count++;
      }
      if(count > ans)
        ans = count;
    }
    System.out.println(ans);
  }

}

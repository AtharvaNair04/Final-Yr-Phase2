import java.util.*;
import java.util.Collections;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    List<String> list = new ArrayList<String>();
    String ans = "Yes";
    for (int i = 0; i < N; i++) {
      list.add(sc.next());
    }
    List<String> list2 = list.stream().distinct().collect(Collectors.toList());
    if (list2.size() != N) {
      ans = "No";
    } else {
      for (int i = 1; i < N; i++) {
        String formar = list.get(i - 1);
        String latter = list.get(i);
        if (formar.charAt(formar.length() - 1) != latter.charAt(0)) {
          ans = "No";
          break;
        }
      }
    }
    System.out.println(ans);
  }
}

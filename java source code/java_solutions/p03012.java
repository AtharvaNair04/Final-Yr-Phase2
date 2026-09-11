import java.util.*;
public class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int num = Integer.parseInt(in.nextLine());
    List<Integer> weis = new ArrayList<>();
    while(in.hasNext()) {
      weis.add(Integer.parseInt(in.next()));
    }
    int a = 0;
    int b = 0;
    if ((num & 1) == 1) {
      b += weis.get(num / 2);
    }
    for (int i = 0; i < num / 2; i++) {
      a += weis.get(i);
      b += weis.get(num - 1 - i);
    }
	boolean left = a > b;
    int index = num / 2 - 1;
    int ans;
    if (a > b) {
      while (a > b) {
        index--;
        a -= weis.get(index + 1);
        b += weis.get(index + 1);
      }
      ans = (b - a) < (a + weis.get(index + 1) * 2 - b) ? b - a : a + weis.get(index + 1) * 2 - b;
    } else if (a == b) {
      ans = 0;
    } else {
      while (a < b) {
        index++;
        a += weis.get(index);
        b -= weis.get(index);
      }
      ans = (a - b) < (b + weis.get(index) * 2 - a) ? a - b : b + weis.get(index) * 2 - a;
    }
    System.out.println(ans);
  }
}

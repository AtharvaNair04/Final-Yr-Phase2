import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    String s[] = sc.next().split("");
    String t[] = sc.next().split("");
    Arrays.sort(s);
    Arrays.sort(t, Collections.reverseOrder());
    String ss = String.join("", s);
    String tt = String.join("", t);
    String ans[] = {ss, tt};
    Arrays.sort(ans);
    if (ss.equals(tt)){
      System.out.println("No");
      return;
    }
    System.out.println(ans[0].equals(ss) ? "Yes" : "No");
  }
}

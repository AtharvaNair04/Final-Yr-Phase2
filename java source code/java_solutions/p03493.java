import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    String s = sc.next();
    String ss[] = s.split("");
    int ans = 0;
    for (int i = 0; i < s.length(); i++){
      if (ss[i].equals("1")) ans++;
    }
    System.out.println(ans);
  }
}

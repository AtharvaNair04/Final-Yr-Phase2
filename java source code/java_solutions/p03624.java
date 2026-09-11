import java.util.*;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    char[] S = sc.next().toCharArray();
    List<Character> d = new ArrayList<Character>();
    for(char c : S){
      d.add(c);
    }
    for(char i = 'a'; i <= 'z'; i++){
      if(!d.contains(i)){
        System.out.println(i);
        return;
      }
    }
    System.out.println("None");
  }
}

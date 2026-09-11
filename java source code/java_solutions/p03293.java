import java.util.Scanner;

public class Main {
  
  static String kaiten(String str) {
    String newStr = "";
    newStr += str.charAt(str.length() - 1);
    for(int i = 0; i < str.length() - 1; i++) {
      newStr += str.charAt(i);
    }
    return newStr;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String str = sc.next();
    String mohan = sc.next();
    for(int i = 0; i < str.length(); i++) {
      if(str.equals(mohan)) {
        System.out.println("Yes");
        return;
      }else {
        str = kaiten(str);
      }
    }
    System.out.println("No");
  }

}

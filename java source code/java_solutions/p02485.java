import java.util.Scanner;
class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    while(true) {
      String str = sc.next();
      if (str.equals("0")) {
        break;
      }
      int cnt = 0;
      for (int i = 0; i < str.length(); i++) {
        cnt += str.charAt(i) - '0';
      }
      System.out.println(cnt);
    }
  }
}

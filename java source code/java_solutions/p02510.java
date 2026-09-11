import java.util.Scanner;
class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    while(true) {
      String str = sc.next();
      if (str.equals("-")) {
        break;
      }
      int n = sc.nextInt();
      for (int i = 0; i < n; i++) {
        int h = sc.nextInt();
        str = str.substring((h+1)-1) + str.substring(0,h);
      }
      System.out.println(str);
    }
  }
}

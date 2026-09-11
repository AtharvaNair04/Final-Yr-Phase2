import java.util.Scanner;
class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    String block[] = new String[1000];
    int counter = 0;
    while (true) {
      String command = sc.next();
      if (command.equals("quit")) {
        break;
      }
      else if (command.equals("pop")) {
        counter--;
        System.out.println(block[counter]);
        block[counter] = null;
      }
      else {
        String color = sc.next();
        block[counter] = color;
        counter++;
      }
    }
  }
}

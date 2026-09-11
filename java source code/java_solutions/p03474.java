import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int a = sc.nextInt();
    int b = sc.nextInt();
    String blank = sc.nextLine();
    String s = sc.nextLine();


    for(int i = 0; i < a; i++){
      if(!('0'<=s.charAt(i)) || !(s.charAt(i)<='9')){
        System.out.println("No");
        System.exit(0);
      }
    }
  

    if(!(s.charAt(a) == '-')){
      System.out.println("No");
      System.exit(0);
    }
  

    if(!(s.length() == a+b+1)){
      System.out.println("No");
      System.exit(0);
    }
  

    for(int i = a+1; i < a + b + 1; i++){
      if(!('0'<=s.charAt(i)) || !(s.charAt(i)<='9')){
        System.out.println("No");
        System.exit(0);
      }
    }

    System.out.println("Yes");
  }
}

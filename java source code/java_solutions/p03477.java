import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int a = sc.nextInt();
    int b = sc.nextInt();
    int c = sc.nextInt();
    int d = sc.nextInt();
    int i = a+b;
    int j = c+d;
    if (i == j){
      System.out.println("Balanced");
    }else if (i < j){
      System.out.println("Right");
    }else{
      System.out.println("Left");
    }
  }
}

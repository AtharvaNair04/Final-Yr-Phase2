import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);

    int a = sc.nextInt();
    int b = sc.nextInt();

    if(a>=6&&a<13){
      b = b/2;
    }else if(a<6){
      b = 0;
    }
    System.out.println(b);
  }
}

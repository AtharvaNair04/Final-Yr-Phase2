import java.util.Scanner;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int mod7 = n / 7;
    int mod4 = n / 4;
    boolean f = false;
    
    for(int j = 0; j <= mod7; j++){
      for(int i = 0; i <= mod4; i++){
        if(7 * j + 4 * i == n)
          f = true;
      }
    }
    
    if(f == false)
      System.out.println("No");
    else
      System.out.println("Yes");
  }
}

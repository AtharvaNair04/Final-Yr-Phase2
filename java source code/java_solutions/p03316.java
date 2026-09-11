import java.util.Scanner;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int count = 0;
    int n_ = n;
    while(n_ > 0){
      count += n_ % 10;
      n_ /= 10;
    }
    if(n % count == 0)
      System.out.println("Yes");
    else
      System.out.println("No");
  }
}

import java.util.Scanner;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int k = sc.nextInt();
    n -= k;
    k -= 1;
    int count = 1;
    
    while(n > 0){
      n -= k;
      count++;
    } 
    
    System.out.println(count);
  }
}

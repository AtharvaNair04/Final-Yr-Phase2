import java.util.*;
 
class Main{
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    
    int n = sc.nextInt();
    int x = n;
    int sum = 0;
    while(x > 0){
      sum += x % 10;
      x /= 10;
    }
    if(n%sum == 0){
      System.out.println("Yes");
    } else {
      System.out.println("No");
    }

    sc.close();
  }
}

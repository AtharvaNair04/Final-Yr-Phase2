import java.util.*;
import java.math.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
	int n = sc.nextInt();
    
   	System.out.println(check(n));
    
    
    
  }
  
  private static String check(int n){
    for(int i = 1; i <= 50000; i++ ){
      int money = i*108/100;
      if(money == n)
        return "" + i;
    }
    return ":(";
  }
}

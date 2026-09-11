import java.util.*;

public class Main {
 	public static void main(String[] args) {
     	Scanner s = new Scanner(System.in);
      	int n = s.nextInt();
      	int a = s.nextInt();
      	int b = s.nextInt();
      	System.out.println(Math.min(n*a, b));
    }
}

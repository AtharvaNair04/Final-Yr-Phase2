import java.util.Scanner;
public class Main {
  
  public static void main (String[] args) {
    Scanner scan = new Scanner(System.in);
    int mai = Integer.parseInt(scan.next());
    int kei = Integer.parseInt(scan.next());
     if (mai * 500 >= kei) {
    	System.out.println("Yes");
     } else {
		System.out.println("No");
     }
  }
}

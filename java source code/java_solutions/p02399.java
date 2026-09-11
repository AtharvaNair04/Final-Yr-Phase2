import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt();
		int b = sc.nextInt();
		
		int c = a/b;
		int d = a%b;
		double e = (double)a / (double)b;
		
		String str = String.format("%.5f", e);
		
		System.out.println(c +" "+ d +" "+ str);
		
	    
	}
}

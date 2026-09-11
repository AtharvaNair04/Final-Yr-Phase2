import java.util.Scanner;

public class Main {

	public static void main(String... nui) {
		Scanner sc = new Scanner(System.in);
		
		int x = sc.nextInt(), a = sc.nextInt(), b = sc.nextInt();
		
		int c = Math.abs(x - a);
		int d = Math.abs(x - b);
		
		System.out.println((c < d) ? "A" : "B");
		
	}

}

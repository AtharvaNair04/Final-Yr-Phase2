import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sr = new Scanner(System.in);
		int a = sr.nextInt();
		int b = sr.nextInt();
		
		
		int c = b-a;
		int counter = 0;
		for(int i = 1; i<=c; i++) {
			counter = counter + i;
		}
		int s=counter-b;
		
		System.out.println(s);
	}
}

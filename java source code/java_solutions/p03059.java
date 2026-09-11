import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int a = sc.nextInt();
		int b = sc.nextInt();
		int t = sc.nextInt();
		
		int time = (int)(t+0.5)/a;
		System.out.println(time*b);
	}
}

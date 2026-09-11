import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int n=scan.nextInt();
		int b=scan.nextInt();
		if(n*b%2==1) System.out.println("Yes");
		else System.out.println("No");

	}

}

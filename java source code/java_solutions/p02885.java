import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int a=scan.nextInt();
		int b=scan.nextInt();
		int c=a-(2*b);
		if(c>0) System.out.println(c);
		else System.out.println("0");

	}

}

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan= new Scanner(System.in);
		int n=scan.nextInt();
		if(n%2==0) {
			n/=2;
			System.out.println(n*n);
		}
		else {
			int a=n/2;
			System.out.println((a*a)+(n/2));
		}

	}

}

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int n=scan.nextInt();
		if(n%1000==0) System.out.println("0");
		else
		{
			int c=n%1000;
			System.out.println(1000-c);
		}
	}

}

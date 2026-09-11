import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int a = sc.nextInt();
		int b = sc.nextInt();
		int c = sc.nextInt();

		String message = null;

		if (a * b * c != 175) {
			message = "NO";
		} else {
			message = "YES";
		}

		System.out.println(message);

		sc.close();
	}

}

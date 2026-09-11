import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		long x = sc.nextLong();
		if (x % 11 == 0) {
			System.out.println(x/11 * 2);
		} else if (x % 11 > 6) {
			System.out.println(((x / 11)) * 2 + 2);
		} else {
			System.out.println((x / 11) * 2 + 1);
		}
	}

}

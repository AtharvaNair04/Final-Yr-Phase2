import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner stdIn = new Scanner(System.in);

		long i = stdIn.nextLong();
		long money = 100;
		int count = 0;

		while (money < i) {
			money = (long) (money * 1.01);
			count++;
		}

		System.out.println(count);

	}

}

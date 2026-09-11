import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		long t025 = sc.nextLong();
		long t05 = sc.nextLong();
		long t1 = sc.nextLong();
		long t2 = sc.nextLong();

		long n = sc.nextLong();

		long one = Math.min(t1, t025 * 4);
		one = Math.min(one, t05 * 2);

		long two = Math.min(one * 2, t2);

		System.out.println(n % 2 * one + n / 2 * two);

	}

}

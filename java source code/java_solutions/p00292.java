import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		int n = scan.nextInt();
		for (int i = 0; i < n; i++) {
			int stones = scan.nextInt();
			int people = scan.nextInt();

			if (stones < people)
				System.out.println(stones);
			else if (stones % people == 0)
				System.out.println(people);
			else
				System.out.println(stones % people);

		}
	}
}

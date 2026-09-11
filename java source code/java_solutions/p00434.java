import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int student[] = new int[30];
		for (int i = 0; i < 30; i++) {
			student[i] = 0;
		}
		for (int i = 0; i < 28; i++) {
			student[scanner.nextInt() - 1] = 1;
		}
		for (int i = 0; i < 30; i++) {
			if (student[i] == 0) {
				System.out.println(i + 1);
			}
		}
	}
}

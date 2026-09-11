import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();

		if (s.length() == 2) {
			System.out.println(0);
		} else {
			for (int i = s.length() - 2; i >= 2; i -= 2) {
				if (s.substring(0, i / 2).equals(s.substring(i / 2, i))) {
					System.out.println(s.substring(0, i).length());
					break;
				}
			}
		}
	}

}

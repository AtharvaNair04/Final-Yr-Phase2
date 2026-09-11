import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String g = "CODEFESTIVAL2016";
		String rString = scanner.next();
		int j = 0;
		for (int i = 0; i < rString.length(); i++) {
			if (g.charAt(i) != rString.charAt(i)) {
				j++;
			}
		}
		System.out.println(j);
	}

}

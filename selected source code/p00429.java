import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner scan = new Scanner(System.in);
		// Scanner scan = new Scanner(new File("c:\\0509-input.txt"));

		while (true) {
			int n = scan.nextInt();
			if (n == 0)
				break;
			String s = scan.next();
			run(n, s);
		}

		scan.close();
		System.exit(0);
	}

	private static void run(int n, String s) {
		if (n == 0) {
			System.out.println(s);
			return;
		}

		StringBuilder ss = new StringBuilder("");
		
		for (int i = 0; i < s.length(); i++) {
			int repeat = 1;
			for (int j = i + 1; j < s.length(); j++)
				if (s.charAt(i) == s.charAt(j)) {
					repeat++;
					i++;
				} else
					break;
			ss.append(String.valueOf(repeat));
			ss.append(String.valueOf(s.charAt(i)));
		}

		run(n - 1, ss.toString());
	}

}

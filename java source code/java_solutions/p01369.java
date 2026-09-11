import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		while (true) {

			String word = scan.next();
			char chara;
			int side;
			int preside = 0;
			int count = 0;

			if (word.equals("#"))
				break;

			for (int i = 0; i < word.length(); i++) {
				chara = word.charAt(i);
				if (chara == 'q' || chara == 'w' || chara == 'e'
						|| chara == 'r' || chara == 't' || chara == 'a'
						|| chara == 's' || chara == 'd' || chara == 'f'
						|| chara == 'g' || chara == 'z' || chara == 'x'
						|| chara == 'c' || chara == 'v' || chara == 'b') {
					side = 0;
				} else {
					side = 1;
				}
				if (i == 0) {
					preside = side;
				} else if (preside != side) {
					count++;
				}
				preside = side;

			}
			System.out.println(count);
		}

	}
}

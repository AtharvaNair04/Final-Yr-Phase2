import java.util.Scanner;

public class Main{
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		String table[] = new String[10];
		String enter = "";

		table[0] = "* = ****";
		table[1] = "* =* ***";
		table[2] = "* =** **";
		table[3] = "* =*** *";
		table[4] = "* =**** ";
		table[5] = " *= ****";
		table[6] = " *=* ***";
		table[7] = " *=** **";
		table[8] = " *=*** *";
		table[9] = " *=**** ";

		while (sc.hasNext()) {
			String num = sc.next();
			while (num.length() < 5) {
				num = "0" + num;
			}
			
			String s[] = new String[5];
			char answer[][] = new char[8][5];
			for (int i = 0; i < 5; i++) {
				s[i] = table[num.charAt(i) - '0'];
			}
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 5; j++) {
					answer[i][j] = s[j].charAt(i);
				}
			}
			System.out.print(enter);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 5; j++) {
					System.out.print(answer[i][j]);
				}
				System.out.println();
			}
			enter = "\n";
		}
	}
}

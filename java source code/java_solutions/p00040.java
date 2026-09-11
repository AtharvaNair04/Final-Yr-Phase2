import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		String str[];
		int word[];
		int a, b;

		n = sc.nextInt();
		String s = sc.nextLine();
		word = new int[4];
		for (int i = 0; i < n; i++) {
			str = sc.nextLine().split(" ");
			a = 0;
			b = 0;
			for (int j = 0; j < str.length; j++) {
				if (str[j].length() == 4) {
					word[0] = str[j].charAt(0) - 'a';
					word[1] = str[j].charAt(1) - 'a';
					word[2] = str[j].charAt(2) - 'a';
					word[3] = str[j].charAt(3) - 'a';
					for (int k = 3; k <= 27; k += 2) {
						int l = word[0] - k * ('t' - 'a');
						if (((k * ('h' - 'a') + l) % 26 + 26) % 26 == word[1]) {
							if (((k * ('a' - 'a') + l) % 26 + 26) % 26 == word[2] && word[0] == word[3]) {
								a = k;
								b = l;
								break;
							} else if (((k * ('i' - 'a') + l) % 26 + 26) % 26 == word[2] && ((k * ('s' - 'a') + l) % 26 + 26) % 26 == word[3]) {
								a = k;
								b = l;
								break;
							} else {
								break;
							}
						}
					}
					if (a != 0) {
						break;
					}
				}
			}
			for (int j = 0; j < str.length; j++) {
				if (j != 0) {
					System.out.print(" ");
				}
				for (int k = 0; k < str[j].length(); k++) {
					for (int l = 'a'; l <= 'z'; l++) {
						if (((a * (l - 'a') + b) % 26 + 26) % 26 + 'a' == str[j].charAt(k)) {
							System.out.print((char)l);
						}
					}
				}
			}
			System.out.println();
		}
	}
}

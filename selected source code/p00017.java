import java.util.Scanner;


public class Main {

	static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		while (in.hasNext()) {
			String s = in.nextLine();
			while (true) {
				if (s.contains("this") ||
					s.contains("the") ||
					s.contains("that")) {
					System.out.println(s);
					break;
				} else {
					s = shift(s);
					//System.out.println(s);
				}
			}
		}
	}
	
	public static String shift(String s) {
		StringBuffer input = new StringBuffer(s);
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c >= 97 && c <= 122) {
				output.append((char)(((((c-97)+1)%26)+97)));
			} else {
				output.append(String.valueOf(c));
			}
		}
		return output.toString();
	}
}

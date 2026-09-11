import java.util.Scanner;

public class Main {
	public static void main(String [] args) {
		Scanner scanner = new Scanner(System.in);
		int N = scanner.nextInt();
		String S = scanner.next();
		// N == S.length();
		int black = 0;
		int white = 0;
		for(int i = 0; i < N; i++) {
			if(S.charAt(i) == '.') {
				white++;
			}
		}
		int min = white;
		for(int i = 0; i < N; i++) {
			if(S.charAt(i) == '#') {
				black++;
			}else {
				white--;
			}
			min=Math.min(min, black+white);
		}
		System.out.println(min);
		
		scanner.close();
	}
}

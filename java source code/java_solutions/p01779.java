import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int health = sc.nextInt();
		char[][] s = new char[n][];
		int[] t = new int[n];
		for(int i=0;i<n;i++) {
			s[i] = sc.next().toCharArray();
			t[i] = sc.nextInt();
		}
		int word = 0;
		int alpha = 0;
		int miss = 0;

		int typecount = 0;
		int misscount = 0;
		while(true) {
			System.out.println("? " + String.valueOf(s[word]));
			System.out.flush();
			char c = Character.toLowerCase(sc.next().charAt(0));
			typecount++;
			if (Character.toLowerCase(s[word][alpha]) == c) {
				s[word][alpha] = '_';
				alpha++;
				if (alpha >= s[word].length) {
					word++;
					alpha = 0;
					miss = 0;
				}
			}else{
				miss++;
				misscount++;
				if (miss > t[word]) {
					health--;
					word++;
					alpha = 0;
					miss = 0;
					if (health <= 0) {
						System.out.println("! Game Over");
						System.out.flush();
						return;
					}
				}
			}
			if (word >= n) {
				BigDecimal tc = BigDecimal.valueOf(typecount);
				BigDecimal mc = BigDecimal.valueOf(misscount);
				System.out.println("! Game Clear " + tc.subtract(mc).multiply(BigDecimal.valueOf(100)).divide(tc, 1, RoundingMode.FLOOR));
				System.out.flush();
				return;
			}
		}
	}

}

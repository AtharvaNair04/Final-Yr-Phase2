import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		long k = sc.nextLong();
		char[] c = s.toCharArray();
		for(int i = 0; i < k; i++) {
			if(c[i]!='1') {
				System.out.println(c[i]);
				System.exit(0);
			}
		}System.out.println('1');
	}
}

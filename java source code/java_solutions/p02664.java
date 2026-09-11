import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		String t = sc.next();
		String p = "";
		
		
		for(int i = 0; i < t.length(); i++) {
			if(t.charAt(i) == '?') {
				System.out.print('D');
			}else {
				System.out.print(t.charAt(i));
			}
		}
		
		System.out.println(p);
	}
}

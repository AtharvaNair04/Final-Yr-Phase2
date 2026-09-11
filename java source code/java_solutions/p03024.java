import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		char[] s = stdIn.next().toCharArray();
		int cnt = 0;
		
		for(int i = 0; i < s.length; i++) {
			if(s[i] == 'o') {
				cnt++;
			}
		}
		boolean flag = true;
		
		if(15 - s.length +cnt < 8) {
			flag = false;
		}
		
		if(flag) {
			System.out.println("YES");
		}else {
			System.out.println("NO");
		}
		
		
	}

}

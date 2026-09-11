import java.util.Scanner;
public class Main{

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		int n = stdIn.nextInt();
		int cnt = 0;
		for(int i = 1; i <=n; i++) {
			int nn = String.valueOf(i).length();
			if(nn % 2 == 1) {
				cnt++;
			}
		}
		System.out.println(cnt);
		
		
	}

}

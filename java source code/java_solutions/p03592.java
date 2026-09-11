import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		int n = stdIn.nextInt();
		int m = stdIn.nextInt();
		int k = stdIn.nextInt();
		
		int chou = n * m;
		
		
		for(int i = 0; i <= n; i++) {
			for(int j = 0; j <= m; j++) {
				
				if(k == i*(m-j)+j*(n-i)) {
					System.out.println("Yes");
					return;
				}
				
				
			}
		}
		System.out.println("No");
		
		
	}

}

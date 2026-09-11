import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int a = 0;
		int b = 0;
		int c = 0;
		int n = scanner.nextInt();

		for(int i = 0; i < n; i++){
			a = scanner.nextInt();
			b = scanner.nextInt();
			c = scanner.nextInt();

			if(c *  c == a * a + b * b || b *  b == a * a + c * c || a *  a == b * b + c * c){
				System.out.println("YES");
			}else{
				System.out.println("NO");
			}
		}
	}
}

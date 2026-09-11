import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int a =sc.nextInt();
		int b =sc.nextInt();
		int c =sc.nextInt();
		int d =sc.nextInt();
		int i=1;
		while(b<=a&&i<=d) {
			b*=2;
			++i;
		}

		while(Math.max(a, b)>=c&&i<=d) {
			c*=2;
			++i;
		}
		
			
			
			if(b>a&&c>b) {
				System.out.println("Yes");
				return;
			}
		
		
		System.out.println("No");
		

		

	}

}

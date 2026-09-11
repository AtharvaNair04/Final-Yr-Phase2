import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		int m1 = stdIn.nextInt();
		int d1 = stdIn.nextInt();
		
		int m2 = stdIn.nextInt();
		int d2 = stdIn.nextInt();
		
		if(m1 ==1 || m1 == 3 || m1 == 5 || m1 == 7 || m1 ==8 || m1 == 10 || m1 ==12) {
			if(d1 == 31) {
				System.out.println(1);
			}else {
				System.out.println(0);
			}
		}
		
		if( m1 == 4 || m1 == 6 || m1 == 9 || m1 ==11) {
			if(d1 == 30) {
				System.out.println(1);
			}else {
				System.out.println(0);
			}
		}
		
		if(m1 == 2) {
			if(d1 == 28) {
				System.out.println(1);
			}else {
				System.out.println(0);
			}
		}
		
	}

}

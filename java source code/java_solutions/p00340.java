import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner stdin = new Scanner(System.in);
		int[] a = new int[4];
		for(int i = 0; i < 4; i++) {
			a[i] = stdin.nextInt();
		}
		
		if(a[0] == a[1] && a[2] == a[3]) {
			System.out.println("yes");
		} else if(a[0] == a[2] && a[1] == a[3]) {
			System.out.println("yes");
		} else if(a[0] == a[3] && a[1] == a[2]) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}

	}
}

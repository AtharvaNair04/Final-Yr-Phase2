import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		int a = 0;
		int b = 0;
		java.util.Scanner stdIn = new Scanner(System.in);
		for(int i=0;i<4;i++){
			int p = stdIn.nextInt();
			a += p;
		}
		for(int i=0;i<4;i++){
			int p = stdIn.nextInt();
			b += p;
		}
		System.out.println(Math.max(a,b));
	}

}

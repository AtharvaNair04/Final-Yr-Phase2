import java.util.*;
public class Main {
	static final Scanner stdin = new Scanner(System.in);
	public static void main(String[] args) {
		int i = 9;
		while(i-- > 0){
			String s = stdin.next();
                        int a = stdin.nextInt(), b = stdin.nextInt();
			System.out.println(s + " " + (a+b) + " " + (200*a+300*b));
		}
	}
}

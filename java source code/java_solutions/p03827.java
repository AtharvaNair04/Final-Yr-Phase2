import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int n=scan.nextInt();
		String s=scan.next();
		int x=0,max = 0;
		for (int i = 0; i < n; i++) {
			if(s.charAt(i)=='I') x++;
			else if(s.charAt(i)=='D') x--;
			max=Math.max(x, max);
		}
		System.out.println(max);
	}
	

}

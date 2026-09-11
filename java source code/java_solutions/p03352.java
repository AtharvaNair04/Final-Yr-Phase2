import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);

		int x = stdIn.nextInt();
		int y = 0;
		int max = Integer.MIN_VALUE;
		
		if(x==1) {
			System.out.println(1);
			return;
		}
		
		for(int i = 1; i < x; i++) {
			for(int j = 2; j < 15; j++) {
				if(Math.pow(i,j) <= x && max < Math.pow(i, j)) {
					max = (int)Math.pow(i, j);
				}
			}
		}
		System.out.println(max);
	}

}

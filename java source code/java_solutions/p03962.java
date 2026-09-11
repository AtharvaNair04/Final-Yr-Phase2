import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		int[]penki = new int[101];
		
		int a = stdIn.nextInt();
		int b = stdIn.nextInt();
		int c = stdIn.nextInt();
		
		penki[a] = a;
		penki[b] = b;
		penki[c] = c;
		
		int cnt = 0;
		
		for(int i = 0; i <=100; i++) {
			if(penki[i]!= 0) {
				cnt++;
			}
		}
		
		System.out.println(cnt);
		
		
	}

}

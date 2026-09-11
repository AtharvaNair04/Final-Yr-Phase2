import java.util.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		int H1 = input.nextInt();
		int M1 = input.nextInt();
		int H2 = input.nextInt();
		int M2 = input.nextInt();
		int K = input.nextInt();
		
		int remainder  = 0;
		int minutes = 0;
		int hours =0;
		
		if (M2 < M1) {
			remainder = 1;
			minutes = (M2+60)-M1;
		} else if( M1 < M2) {
			minutes = M2-M1;
		}
		
		hours = H2-remainder-H1;
		
		int totalMinutes = (hours*60)+minutes;
		
		System.out.println(totalMinutes-K);
		
		

	}

}

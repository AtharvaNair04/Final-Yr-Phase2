import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		
		try(Scanner scan = new Scanner(System.in)){

			int n = scan.nextInt();
			int[] array = new int[n+2];
			for(int i=0; i<n; i++) {
				array[i] = scan.nextInt();
			}
			
			int dice = 0;
			int counter = 0; 
			
			for(int i=0; i<array.length; i++) {

				if(array[i] == 1) {
					counter++;
				}else {
					dice = Math.max(dice, counter);
					counter = 0;
				}
			}
			dice = Math.max(dice, counter);
			System.out.println(dice+1);
		}
	}
}

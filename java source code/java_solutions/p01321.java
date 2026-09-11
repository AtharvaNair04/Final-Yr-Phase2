import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		int count;

		while ((count = scan.nextInt()) != 0) {

			int[] seiseki = new int[count];
			for (int i = 0; i < seiseki.length; i++) {
				for (int j = 0; j < 5; j++) {
					seiseki[i] += scan.nextInt();
				}
			}

			java.util.Arrays.sort(seiseki);
			if(seiseki.length == 1){
				System.out.println(seiseki[0] + " " + seiseki[0]);
			}else{
				System.out.println(seiseki[seiseki.length - 1] + " " + seiseki[0]);
			}

		}
	}
}

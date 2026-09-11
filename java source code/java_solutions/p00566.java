import java.util.Scanner;

public class Main {
	public static void main(String args[]) {

		try(Scanner scan = new Scanner(System.in)){

			int h = scan.nextInt();
			int w = scan.nextInt();

			int[][] arrays = new int[h][w];

			for(int i=0; i<h; i++) {
				for(int j=0; j<w; j++) {
					arrays[i][j] = scan.nextInt();
				}
			}

			int min = Integer.MAX_VALUE;

			for(int i=0; i<h; i++) {
				for(int j=0; j<w; j++) {

					int sum = 0;

					for(int k=0; k<h; k++) {
						for(int l=0; l<w; l++) {

							sum += Math.min(Math.abs(i-k), Math.abs(j-l)) * arrays[k][l];

						}
					}

					min = Math.min(min, sum);
				}
			}
			System.out.println(min);
		}
	}
}

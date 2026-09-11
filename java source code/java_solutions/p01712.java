import java.util.Arrays;
import java.util.Scanner;

class Main{

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int w = sc.nextInt();
		int h = sc.nextInt();

		boolean[] width = new boolean[w];
		boolean[] height = new boolean[h];

		Arrays.fill(width, false);
		Arrays.fill(height, false);

		for (int i = 0; i < n; i++) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			int wireless = sc.nextInt();

			for(int j = Math.max(0, x - wireless); j < Math.min(w, x + wireless); j++) {
				width[j] = true;
			}

			for(int j = Math.max(0, y - wireless); j < Math.min(h, y + wireless); j++ ) {
				height[j] = true;
			}

		}

		sc.close();

		if( isFilledWithTrue(width) || isFilledWithTrue(height)) {
			System.out.println("Yes");
		}else {
			System.out.println("No");
		}


	}

	public static boolean isFilledWithTrue(boolean[] bool) {

		for(int i=0; i<bool.length; i++) {
			if(bool[i] == false) {
				return false;
			}
		}
		return true;

	}

}

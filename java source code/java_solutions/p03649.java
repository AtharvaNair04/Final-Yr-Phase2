import java.util.Arrays;
import java.util.Scanner;

public class Main {
	private Scanner sc;
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		sc = new Scanner(System.in);

		int n = Integer.parseInt(sc.nextLine());
		
		String[] nico = sc.nextLine().split(" ");
		
		long[] maki = new long[n];
		for (int i = 0; i < n; i++) {
			maki[i] = Long.parseLong(nico[i]);
		}
		
		long total = 0;
		
		while (true) {
			Arrays.sort(maki);
			long count = 0;
			
			if (maki[n - 1] > (n - 1)) {
				count = maki[n - 1] / n;
				maki[n - 1] = maki[n - 1] % n;
				
				total = total + count;
			} else {
				break;
			}
			
			for (int i = 1; i < n; i++) {
				int index = n - i - 1;
				
				maki[index] = maki[index] + count;
			}
		}
		System.out.println(total);
	}
}

import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int[] suuretu = new int[n];
		for (int i = 0; i < n; i++) {
			suuretu[i] = in.nextInt();
		}
		for (int i = n - 1; i >= 0; i--) {
			if (i != 0) {
				System.out.print(suuretu[i] + " ");
			} else {
				System.out.println(suuretu[i]);
			}
		}
	}
}

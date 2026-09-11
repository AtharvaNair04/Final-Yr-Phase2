import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			int n = sc.nextInt();
			double debt = 100000;
			for (int i = 0; i < n; i++) {
				debt *= 1.05;
				debt = Math.ceil(debt/1000) * 1000;
			}
			System.out.println((int)debt);
		}
	}
}

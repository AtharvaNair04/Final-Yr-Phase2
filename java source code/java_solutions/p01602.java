import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n;
		int sum;
		boolean f;

		n = sc.nextInt();
		sum = 0;
		f = true;
		
		for (int i = 0; i < n; i++) {
			String p = sc.next();
			int x = sc.nextInt();
			if (p.equals("(")) {
				sum += x;
			} else {
				sum -= x;
			}
			if (sum < 0) {
				f = false;
			}
		}
		if (sum != 0) {
			f = false;
		}

		System.out.println(f ? "YES" : "NO");
	}
}

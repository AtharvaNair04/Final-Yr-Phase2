import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, s;
		int pow[];

		while (true) {
			n = sc.nextInt();
			s = sc.nextInt();
			if ((n | s) == 0) {
				break;
			}

			pow = new int[n];
			for (int i = 0; i < n; i++) {
				pow[i] = sc.nextInt();
			}
			Arrays.sort(pow);

			int id = n - 1;
			int cnt = 0;
			for (int i = 0; i < n; i++) {
				while (s < pow[i] + pow[id] && i < id) {
					id--;
				}
				if (i <= id) {
					cnt += n - id - 1;
				} else {
					cnt += n - i - 1;
				}
			}

			System.out.println(cnt);
		}
	}
}

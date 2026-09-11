import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		while (true) {
			int[] count = new int[3];
			count[0] += Integer.parseInt(sc.next());
			count[1] += Integer.parseInt(sc.next());
			count[2] += Integer.parseInt(sc.next());
			count[0] += Integer.parseInt(sc.next());
			count[1] += Integer.parseInt(sc.next());
			count[2] += Integer.parseInt(sc.next());
			if (count[0] + count[1] + count[2] == 0) return;
			int min = Math.min(count[0], Math.min(count[1], count[2]));
			int ans = 0;
			for (int i = 0; i < 10 && min - i >= 0; ++i) {
				int c = (min - i);
				c += (count[0] - (min - i)) / 3;
				c += (count[1] - (min - i)) / 3;
				c += (count[2] - (min - i)) / 3;
				ans = Math.max(ans, c);
			}
			System.out.println(ans);
		}
	}

}

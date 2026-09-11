import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int a;
		int map[];
		int max;

		map = new int[101];
		max = 0;
		while (sc.hasNext()) {
			a = sc.nextInt();
			map[a]++;
			if (max < map[a]) {
				max = map[a];
			}
		}
		for (int i = 1; i <= 100; i++) {
			if (map[i] == max) {
				System.out.println(i);
			}
		}

	}
}

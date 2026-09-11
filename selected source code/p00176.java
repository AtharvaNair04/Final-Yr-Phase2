import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int[][] map = { { 0, 0, 0 }, { 0, 0, 255 }, { 0, 255, 0 },
				{ 0, 255, 255 }, { 255, 0, 0 }, { 255, 0, 255 },
				{ 255, 255, 0 }, { 255, 255, 255 } };
		String name[] = { "black", "blue", "lime", "aqua", "red", "fuchsia",
				"yellow", "white" };

		while (true) {
			String line = scanner.next();
			if (line.equals("0"))
				break;

			int[] num = new int[3];
			for (int i = 0; i < 3; i++) {
				num[i] = Integer.parseInt(line.substring(i * 2 + 1, i * 2 + 3),
						16);
			}
			double min = 1<<12;
			int ans = 0;
			for (int i = 0; i < 8; i++) {
				double sum = 0;
				for (int j = 0; j < 3; j++) {
					sum += Math.pow(num[j] - map[i][j], 2);
				}
				sum = Math.sqrt(sum);
				if (min > sum) {
					min = sum;
					ans = i;
				}
			}
			System.out.println(name[ans]);
		}
	}
}

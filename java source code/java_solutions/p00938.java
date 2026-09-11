import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	void run() {
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
		int w = scan.nextInt();
		int d = scan.nextInt();
		
		int[][] ary = new int[n][2];
		for (int i = 0; i < n; i++) {
			int x = scan.nextInt();
			int y = scan.nextInt();
			switch (scan.next()) {
			case "N":
				ary[i][0] = w + (w - x + y);
				ary[i][1] = ary[i][0] + (d - y) * 2;
				break;
			case "E":
				ary[i][0] = x + y;
				ary[i][1] = ary[i][0] + (w - x) * 2;
				break;
			case "S":
				if (x < y)
					ary[i][0] = 2 * w + 2 * d - (y - x);
				else
					ary[i][0] = x - y;
				ary[i][1] = ary[i][0] + y * 2;
				break;
			default:
				ary[i][0] = w + d + (w - x) + (d - y);
				ary[i][1] = ary[i][0] + x * 2;
				break;
			}
		}
//		for (int i = 0; i < n; i++) {
//			System.out.println(ary[i][0] + " " + ary[i][1]);
//		}
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			Arrays.sort(ary, new Comparator<int[]>() {
				public int compare(int[] o1, int[] o2) {
					return o1[1] - o2[1];
				}
			});
			int ans = 1;
			int p = ary[0][1];
			for (int j = 1; j < n; j++) {
				if (p < ary[j][0]) {
					ans++;
					p = ary[j][1];
				}
			}
			ary[0][0] += 2 * w + 2 * d;
			ary[0][1] += 2 * w + 2 * d;
			min = Math.min(min, ans);
		}
		System.out.println(min);
	}
	public static void main(String[] args) {
		new Main().run();
	}
}

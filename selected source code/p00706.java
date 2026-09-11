import java.util.Scanner;

public class Main {
	public void mapDebug(int[][] a) {
		System.out.println("--------map display---------");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.printf("%3d ", a[i][j]);
			}
			System.out.println();
		}
		System.out.println("----------------------------" + '\n');
	}

	void run() {
		Scanner sc = new Scanner(System.in);

		while (true) {
			int n = sc.nextInt();
			if (n == 0) {
				break;
			}
			int w = sc.nextInt();
			int h = sc.nextInt();
			int[][] f = new int[h][w];
			for (int i = 0; i < n; i++) {
				int x = sc.nextInt() - 1;
				int y = sc.nextInt() - 1;
				f[y][x] = 1;
			}
			int s = sc.nextInt();
			int t = sc.nextInt();

			int[][] sub = new int[h][w];
			sub[0][0] = f[0][0];
			for (int i = 1; i < h; i++) {
				sub[i][0] = f[i][0] + sub[i - 1][0];
			}
			for (int j = 1; j < w; j++) {
				sub[0][j] = f[0][j] + sub[0][j - 1];
			}
			for (int i = 1; i < h; i++) {
				for (int j = 1; j < w; j++) {
					sub[i][j] = f[i][j] + sub[i - 1][j] + sub[i][j - 1] - sub[i - 1][j - 1];
				}
			}

			int max = 0;
			for (int i = t - 1; i < h; i++) {
				for (int j = s - 1; j < w; j++) {
					int a = i - t;
					int b = j - s;
					int t1 = a < 0 ? 0 : sub[a][j];
					int t2 = b < 0 ? 0 : sub[i][b];
					int t3 = a < 0 || b < 0 ? 0 : sub[a][b];
					int t4 = sub[i][j] - t1 - t2 + t3;
					max = Math.max(max, t4);
				}
			}

			//			mapDebug(f);
			//			mapDebug(sub);

			System.out.println(max);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

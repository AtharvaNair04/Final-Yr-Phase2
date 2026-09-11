import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();

		long[][] H = new long[101][101];

		List<int[]> zeros = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			long h = sc.nextLong();
			if (h == 0) {
				zeros.add(new int[]{x, y});
				continue;
			}

			// h = max(H - |x - cx| - |y - cy|, 0)
			// H = h + |x - cx| + |y - cy|
			for (int j = 0; j < 101; j++) {
				for (int k = 0; k < 101; k++) {
					if (H[j][k] == -1) continue;
					long height = h + Math.abs(x - j) + Math.abs(y - k);
					if (height < 1 && H[j][k] > 0) {
						H[j][k] = -1;
						continue;
					}

					if (H[j][k] == 0 || H[j][k] == height) {
						H[j][k] = height;
						continue;
					}
					H[j][k] = -1;
				}
			}
		}

		for (int[] zero : zeros) {
			int x = zero[0];
			int y = zero[1];
			for (int j = 0; j < 101; j++) {
				for (int k = 0; k < 101; k++) {
					if (H[j][k] < 1) continue;
					if (H[j][k] - Math.abs(j - x) - Math.abs(k - y) > 0) {
						H[j][k] = -1;
					}
				}
			}
		}

		for (int i = 0; i < H.length; i++) {
			for (int j = 0; j < H.length; j++) {
				if (H[i][j] > 0) {
					System.out.print(i);
					System.out.print(" ");
					System.out.print(j);
					System.out.print(" ");

					System.out.println(H[i][j]);
					return;
				}
			}
		}
	}
}

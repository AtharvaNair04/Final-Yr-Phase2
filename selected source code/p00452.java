import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n, w;
		int arrow[];
		int kk[];
		boolean flag;
		int max;

		while (true) {
			n = sc.nextInt();
			w = sc.nextInt();
			if ((n | w) == 0) {
				break;
			}
			arrow = new int[n + 1];
			kk = new int[(n + 1) * (n + 1)];
			max = 0;

			arrow[0] = 0;
			for (int i = 1; i <= n; i++) {
				arrow[i] = sc.nextInt();
			}

			for (int i = 0; i <= n; i++) {
				for (int j = 0; j <= n; j++) {
					kk[i * (n + 1) + j] = arrow[i] + arrow[j];
				}
			}

			Arrays.sort(kk);
			int index = 0;
			for (int i = 0; i < kk.length - 1; i++) {
				if (kk[i] != kk[i + 1]) {
					kk[index++] = kk[i];
				}
			}
			kk[index++] = kk[kk.length - 1];

			for (int i = 0; i <= n; i++) {
				for (int j = 0; j <= n; j++) {
					int k = search(kk, 0, index - 1, arrow[i] + arrow[j], w);
					if (max < k) {
						max = k;
					}
				}
			}
			System.out.println(max);
		}
	}

	public static int search(int kk[], int low, int high, int k1k2, int w) {
		if (high < low) {
			if (k1k2 <= w) {
				return k1k2;
			} else {
				return 0;
			}
		}

		int mid = (low + high) / 2;
		int max = 0;

		if (w < k1k2 + kk[mid]) {
			int k = search(kk, low, mid - 1, k1k2, w); 
			return k;
		} else if (w == k1k2 + kk[mid]) {
			return w;
		} else if (k1k2 + kk[mid] < w) {
			max = k1k2 + kk[mid];
			int k = search(kk, mid + 1, high, k1k2, w);
			if (max < k) {
				max = k;
			}
		}
		return max;
	}
}

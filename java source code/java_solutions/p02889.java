// practice with kaiboy
import java.io.*;
import java.util.*;

public class Main extends PrintWriter {
	Main() { super(System.out); }
	Scanner sc = new Scanner(System.in);
	public static void main(String[] $) {
		Main o = new Main(); o.main(); o.flush();
	}

	void main() {
		int n = sc.nextInt();
		int m = sc.nextInt();
		int l = sc.nextInt();
		int[][] aa = new int[n][n];
		while (m-- > 0) {
			int i = sc.nextInt() - 1;
			int j = sc.nextInt() - 1;
			int c = sc.nextInt();
			if (c <= l)
				aa[i][j] = aa[j][i] = c;
		}
		int[][] kk_ = new int[n][n];
		int[][] rr_ = new int[n][n];
		boolean[] used = new boolean[n];
		for (int s = 0; s < n; s++) {
			int[] kk = kk_[s];
			int[] rr = rr_[s];
			for (int j = 0; j < n; j++)
				kk[j] = n;
			kk[s] = 0;
			rr[s] = l;
			Arrays.fill(used, false);
			while (true) {
				int i = -1;
				for (int j = 0; j < n; j++)
					if (!used[j] && (i == -1 || kk[i] > kk[j] || kk[i] == kk[j] && rr[i] < rr[j]))
						i = j;
				if (i == -1)
					break;
				used[i] = true;
				for (int j = 0, c; j < n; j++)
					if (!used[j] && (c = aa[i][j]) != 0) {
						int k = kk[i];
						int r = rr[i];
						if (r < c) {
							k++;
							r = l;
						}
						r -= c;
						if (kk[j] > k || kk[j] == k && rr[j] < r) {
							kk[j] = k;
							rr[j] = r;
						}
					}
			}
		}
		int q = sc.nextInt();
		while (q-- > 0) {
			int i = sc.nextInt() - 1;
			int j = sc.nextInt() - 1;
			int k = kk_[i][j];
			if (k == n)
				k = -1;
			println(k);
		}
	}
}

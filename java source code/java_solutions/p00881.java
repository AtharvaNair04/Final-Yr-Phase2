import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		new Main().solver();
	}

	int[][] memo;
	int[] ans;
	int n, m;

	int rep(int masked, int answer) {
		if (memo[masked][answer] != -1) {
			return memo[masked][answer];
		}
		int count = 0;
		for (int i = 0; i < n; i++) {
			if ((ans[i] & masked) == answer) {
				count++;
			}
		}
		if (count <= 1) {
			memo[masked][answer] = 0;
			return 0;
		}
		int min = 1000;
		for (int i = 0; i < m; i++) {
			int shi = 1 << i;
			if ((masked & shi) != 0) {
				continue;
			}
			int z = shi;
			min = Math.min(min, Math.max(rep(masked | z, answer), rep(masked | z, answer | z)));
		}
		memo[masked][answer] = min + 1;
		return min + 1;
	}

	void solver() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			m = sc.nextInt();// the number of features 0<m<=11
			n = sc.nextInt();// the number of objects 0<n<=128
			memo = new int[1 << 11][1 << 11];
			for (int i = 0; i < (1 << 11); i++) {
				Arrays.fill(memo[i], -1);
			}
			if (m == 0 && n == 0)
				break;
			ans = new int[n];
			for (int i = 0; i < n; i++) {
				ans[i] = sc.nextInt(2);
			}
			int m = rep(0, 0);
			System.out.println(m);
		}
	}
}

import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	static int L, D;
	static char[][] str = new char[3][];
	static int[][] count;

	public static void main(String[] args) {
		while (true) {
			L = sc.nextInt();
			D = sc.nextInt();
			if (L == 0) break;
			for (int i = 0; i < 3; ++i) {
				str[i] = sc.next().toCharArray();
			}
			String ans = solve();
			System.out.println(ans == null ? "-1" : ans);
		}
	}

	static String solve() {
		count = new int[L + 1][5];
		char[] ret = new char[L];
		for (int i = 0; i < L; ++i) {
			if (str[0][i] == str[1][i] && str[0][i] == str[2][i]) {
				count[i][4]++;
			} else if (str[1][i] == str[2][i]) {
				count[i][0]++;
			} else if (str[0][i] == str[2][i]) {
				count[i][1]++;
			} else if (str[0][i] == str[1][i]) {
				count[i][2]++;
			} else {
				count[i][3]++;
			}
		}
		for (int i = L - 2; i >= 0; --i) {
			for (int j = 0; j < 5; ++j) {
				count[i][j] += count[i + 1][j];
			}
		}
		int[] rem = { D, D, D };
		for (int i = 0; i < L; ++i) {
			char[] cand = { 'A', str[0][i], str[1][i], str[2][i] };
			Arrays.sort(cand);
			boolean ok = false;
			for (int j = 0; j < cand.length; ++j) {
				char ch = cand[j];
				if (j > 0 && ch == cand[j - 1]) continue;
				for (int k = 0; k < 3; ++k) {
					if (str[k][i] != ch) rem[k]--;
				}
				if (ok(rem, i + 1)) {
					ok = true;
					ret[i] = ch;
					break;
				}
				for (int k = 0; k < 3; ++k) {
					if (str[k][i] != ch) rem[k]++;
				}
			}
			if (!ok) return null;
		}
		return String.valueOf(ret);
	}

	static boolean ok(int[] rem, int pos) {
		int[] num = new int[3];
		for (int i = 0; i < 3; ++i) {
			if (rem[i] < 0) return false;
			if (rem[i] < count[pos][i]) {
				num[i] += rem[i];
				for (int j = 0; j < 3; ++j) {
					if (j != i) num[j] += count[pos][i] - rem[i];
				}
			} else {
				num[i] += count[pos][i];
			}
		}
		for (int i = 0; i < 3; ++i) {
			if (num[i] > rem[i]) return false;
			num[i] = rem[i] - num[i];
		}
		Arrays.sort(num);
		int use = num[1] - num[0];
		num[1] -= use;
		num[2] -= use;
		if (num[0] + num[1] < num[2]) {
			use += num[0] + num[1];
		} else {
			use += (num[0] + num[1] + num[2]) / 2;
		}
		return count[pos][3] <= use;
	}

}

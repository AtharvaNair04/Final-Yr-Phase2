import java.util.*;
import java.lang.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);

//	int[][] map = { { 0, 1, 2, 4, 3, 5 }, { 0, 2, 4, 3, 1, 5 },
//			{ 0, 4, 3, 1, 2, 5 }, { 0, 3, 1, 2, 4, 5 },
//
//			{ 1, 2, 0, 3, 5, 4 }, { 1, 5, 2, 0, 3, 4 }, { 1, 3, 5, 2, 0, 4 },
//			{ 1, 5, 2, 0, 3, 4 },
//
//			{ 2, 0, 1, 5, 4, 3 }, { 2, 1, 5, 4, 0, 3 }, { 2, 5, 4, 0, 1, 3 },
//			{ 2, 4, 0, 1, 5, 3 },
//
//			{}, {}, {}, {},
//
//			{}, {}, {}, {},
//
//			{}, {}, {}, {} };

	int[][] map = { { 0, 1, 2, 3, 4, 5 }, { 0, 2, 4, 1, 3, 5 },
			{ 0, 4, 3, 2, 1, 5 }, { 0, 3, 1, 4, 2, 5 }, { 3, 1, 0, 5, 4, 2 },
			{ 3, 0, 4, 1, 5, 2 }, { 3, 4, 5, 0, 1, 2 }, { 3, 5, 1, 4, 0, 2 },
			{ 5, 1, 3, 2, 4, 0 }, { 5, 3, 4, 1, 2, 0 }, { 5, 4, 2, 3, 1, 0 },
			{ 5, 2, 1, 4, 3, 0 }, { 2, 1, 5, 0, 4, 3 }, { 2, 5, 4, 1, 0, 3 },
			{ 2, 4, 0, 5, 1, 3 }, { 2, 0, 1, 4, 5, 3 }, { 4, 0, 2, 3, 5, 1 },
			{ 4, 2, 5, 0, 3, 1 }, { 4, 5, 3, 2, 0, 1 }, { 4, 3, 0, 5, 2, 1 },
			{ 1, 0, 3, 2, 5, 4 }, { 1, 3, 5, 0, 2, 4 }, { 1, 5, 2, 3, 0, 4 },
			{ 1, 2, 0, 5, 3, 4 } };

	int[] rev(int[] a) {
		int[] ret = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			ret[i] = a[a.length - 1 - i];
		}
		return ret;
	}

	void run() {
		{
			int n = 12;
			for (int i = 0; i < n; i++) {
				// map[i + n] = rev(map[i]);
			}
		}
		for (int i = 0; i < 24; i++) {
			// System.out.println(Arrays.toString(map[i]));
		}

		for (;;) {
			int n = sc.nextInt();

			if (n == 0) {
				break;
			}
			if (n == 1) {
				for (int i = 0; i < 6; i++) {
					sc.next();
				}
				System.out.println(0);
				continue;
			}

			HashMap<String, Integer> hm = new HashMap<String, Integer>();

			int dice[][] = new int[n][6];

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < 6; j++) {
					String buffer = sc.next();
					int v = 0;
					if (!hm.containsKey(buffer)) {
						v = hm.size();
						hm.put(buffer, hm.size());
					} else {
						v = hm.get(buffer);
					}
					dice[i][j] = v;
				}
				// System.out.println(Arrays.toString(dice[i]));
			}

			int dice2[][] = new int[n][6];

			for (int i = 0; i < 6; i++) {
				dice2[0][i] = dice[0][map[0][i]];
			}

			long patmax = 1;
			for (int i = 1; i < n; i++) {
				patmax *= 24;
			}

			int min = Integer.MAX_VALUE;
			for (long pat = 0; pat < patmax; pat++) {
				for (int j = 1; j < n; j++) {
					int p = exe(j, pat, n);
					// System.out.print(p+" ");
					int[] m = map[p];
					for (int k = 0; k < 6; k++) {
						dice2[j][k] = dice[j][m[k]];
					}
					// System.out.println(Arrays.toString(dice2[j]));
				}
				// System.out.println();

				min = check(dice2, min);
			}
			System.out.println(min);
		}

	}

	int check(int[][] dice, int min) {
		int value = 0;
		for (int j = 0; j < 6; j++) {
			int hind[] = new int[24];
			for (int i = 0; i < dice.length; i++) {
				hind[dice[i][j]]++;
			}
			int maxHind = 0;
			for (int i = 0; i < 24; i++) {
				maxHind = Math.max(maxHind, hind[i]);
			}
			value += dice.length - maxHind;
			if (min <= value)
				return min;
		}
		// System.out.println(value);
		for (int i = 0; i < dice.length; i++) {
			// System.out.println(Arrays.toString(dice[i]));
		}
		return Math.min(value, min);
	}

	int exe(int j, long pat, int n) {
		for (int i = 1; i < j; i++) {
			pat /= 24;
		}
		return (int) (pat % 24);
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}
}

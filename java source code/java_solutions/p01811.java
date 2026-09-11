import java.util.*;

public class Main {
	static String[] ABC = { "A", "B", "C" };

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String S = sc.next();
		if (dfs(S)) {
			System.out.println("Yes");
		} else {
			System.out.println("No");
		}
	}

	static boolean dfs(String S) {
		if (S.equals("ABC")) {
			return true;
		}
		int cnt = 0;
		boolean f = false;
		boolean[] pos = new boolean[S.length()];
		for (int j = 0; j + 3 <= S.length(); ++j) {
			if (S.substring(j, j + 3).equals("ABC")) {
				pos[j] = true;
				++cnt;
			}
		}
		if (cnt < 1)
			return false;
		out: for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < S.length(); ++j) {
				if (S.charAt(j) == 'A' + i) {
					if (j - i < 0 || !pos[j - i])
						continue out;
				}
			}
			String next = S.replaceAll("ABC", ABC[i]);
			f |= dfs(next.toString());
		}
		return f;
	}

	static void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}

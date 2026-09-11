import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import static java.lang.Integer.parseInt;

/**
 * Best Matched Pair
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		int N = parseInt(br.readLine());
		int[] a = new int[N];

		StringTokenizer st = new StringTokenizer(br.readLine());

		for (int i = 0; i < N; i++) {
			a[i] = parseInt(st.nextToken());
		}

		SortedSet<Integer> pairs = new TreeSet<>(Comparator.reverseOrder());

		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				pairs.add(a[i] * a[j]);
			}
		}

		int ans = -1;
		for (int p : pairs) {
			if (check(p)) {
				ans = p;
				break;
			}
		}

		System.out.println(ans);

	} //end main

	static boolean check(int p) {

		String s = String.valueOf(p);
		boolean ret = true;

		char t = s.charAt(0);
		for (int i = 1; i < s.length(); i++) {
			if (t + 1 != s.charAt(i)) {
				ret = false;
				break;
			}
			t = s.charAt(i);
		}

		return ret;
	}
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * Many Kinds of Apples
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		int N = parseInt(br.readLine());
		int[] c = new int[N + 1];
		int[] a = new int[N + 1];

		StringTokenizer st = new StringTokenizer(br.readLine());

		for (int i = 1; i <= N; i++) {
			c[i] = parseInt(st.nextToken());
		}

		int Q = parseInt(br.readLine());
		int ans = 0;
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());

			int t = parseInt(st.nextToken());
			int x = parseInt(st.nextToken());
			int d = parseInt(st.nextToken());

			if (t == 1) {
				a[x] += d;
			} else {
				a[x] -= d;
			}

			if (a[x] < 0 || a[x] > c[x]) {
				ans = x;
				break;
			}
		}

		System.out.println(ans);
	}
}

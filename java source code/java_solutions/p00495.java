import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * Card Game is Fun
 * --------------------
 * JOI 11th, Fin.2
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		line = br.readLine();

		int A, B;

		//??\????????????????????????
		if (line.indexOf(' ') >= 0) {
			A = parseInt(line.substring(0, line.indexOf(' ')));
			B = parseInt(line.substring(line.indexOf(' ') + 1));
		} else {
			A = parseInt(line);
			B = parseInt(br.readLine());
		}

		int[] CA = new int[A + 1];
		int[] CB = new int[B + 1];

		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		for (int i = 1; i <= A; i++) {
			CA[i] = parseInt(st.nextToken());
		}
		st = new StringTokenizer(br.readLine());
		for (int i = 1; i <= B; i++) {
			CB[i] = parseInt(st.nextToken());
		}

		int[][] dp = new int[A + 1][B + 1];
		int ans = 0;

		for (int i = 1; i <= A; i++) {
			for (int j = 1; j <= B; j++) {
				if (CA[i] == CB[j]) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = dp[i - 1][j];
				}
				ans = Math.max(ans, dp[i][j]);
			}
		}

		System.out.println(ans);
	}
}

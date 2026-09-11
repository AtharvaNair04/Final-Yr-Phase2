import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * Maximization of Relational Expression
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		int N = parseInt(br.readLine());
		int[] a = new int[N];

		StringTokenizer st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) a[i] = parseInt(st.nextToken());
		Arrays.sort(a);

		double max = 0.0;
		BigDecimal AB, CD;

		for (int i = 0; i < N - 3; i++) {
			AB = BigDecimal.valueOf(a[N - 1] + a[N - 2]);
			CD = BigDecimal.valueOf(a[i + 1] - a[i]);
			max = Math.max(max, AB.divide(CD, 7, BigDecimal.ROUND_HALF_UP).doubleValue());
		}

		AB = BigDecimal.valueOf(a[N - 1] + a[N - 4]);
		CD = BigDecimal.valueOf(a[N - 2] - a[N - 3]);
		max = Math.max(max, AB.divide(CD, 7, BigDecimal.ROUND_HALF_UP).doubleValue());


		AB = BigDecimal.valueOf(a[N - 3] + a[N - 4]);
		CD = BigDecimal.valueOf(a[N - 1] - a[N - 2]);
		max = Math.max(max, AB.divide(CD, 7, BigDecimal.ROUND_HALF_UP).doubleValue());

		System.out.format("%.6f%n", max);
	}
}

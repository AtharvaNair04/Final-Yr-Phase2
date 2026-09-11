import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * Pencils
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine());

		int N, A, B, C, D;
		N = parseInt(st.nextToken());
		A = parseInt(st.nextToken());
		B = parseInt(st.nextToken());
		C = parseInt(st.nextToken());
		D = parseInt(st.nextToken());

		int AB, CD;
		AB = (N / A) * B;
		if (N % A > 0) AB += B;
		CD = (N / C) * D;
		if (N % C > 0) CD += D;

		System.out.println(AB < CD ? AB : CD);
	}
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * The Number of Participants
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		int p, m, c;

		StringTokenizer st = new StringTokenizer(br.readLine());
		p = parseInt(st.nextToken());
		m = parseInt(st.nextToken());
		c = parseInt(st.nextToken());

		System.out.println(p + m + c);
	}
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;

/**
 * Fast Division
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		int n = parseInt(br.readLine());
		if (n == 0 || n == 2) {
			System.out.println(1);
		} else if (n == 1) {
			System.out.println(2);
		} else {
			System.out.println(0);
		}

	} //end main
}

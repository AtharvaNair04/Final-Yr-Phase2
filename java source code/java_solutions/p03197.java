import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int n = Integer.parseInt(br.readLine());

		long odd = 0;
		long count = 0;

		for (int i = 0; i < n; i++) {
			long a = Long.parseLong(br.readLine());
			if (a % 2 != 0)
				odd++;

			if (a == 1)
				count++;
		}

		if (odd != 0) {
			System.out.println("first");
		} else {
			System.out.println("second");
		}
	}
}

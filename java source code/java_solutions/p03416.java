import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str[] = br.readLine().split(" ");

		int a = Integer.parseInt(str[0]);
		int b = Integer.parseInt(str[1]);

		int ans = 0;
		for (int i = a; i <= b; i++) {
			String s[] = String.valueOf(i).split("");

			if (s[0].equals(s[4])) {
				if (s[1].equals(s[3])) {
					ans++;
				}
			}
		}

		System.out.println(ans);

	}
}

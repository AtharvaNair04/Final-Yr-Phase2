import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str[] = br.readLine().split("");

		int res = 0;
		for (int i = 0, j = str.length - 1; i < j;) {
			if (str[i].equals(str[j])) {
				++i;
				--j;
			} else if (str[i].equals("x")) {
				++res;
				++i;
			} else if (str[j].equals("x")) {
				++res;
				--j;
			} else {
				System.out.println("-1");
				return;
			}
		}
		System.out.println(res);
	}
}

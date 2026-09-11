import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int n = Integer.parseInt(br.readLine());
		ArrayList<Integer> list = new ArrayList<>();

		for (int i = 1; i <= n; i++) {
			if (n - i < i) {
				list.add(i);
				n -= i;
				break;
			} else {
				list.add(i);
				n -= i;
			}
		}

		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - n)
				System.out.println(list.get(i) + n);
			else
				System.out.println(list.get(i));
		}
	}
}

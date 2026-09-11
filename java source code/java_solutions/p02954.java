import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str[] = br.readLine().split("");

		int n = str.length;

		int a[] = new int[n];

		for (int i = 0; i < n;) {
			int r = 0;
			int l = 0;
			int now = 0;

			while (i < n && str[i].equals("R")) {
				i++;
				r++;
			}

			now = i;
			a[now - 1] = a[now - 1] + (r + 1) / 2;
			a[now] = a[now] + r / 2;

			while (i < n && str[i].equals("L")) {
				i++;
				l++;
			}
			a[now - 1] = a[now - 1] + l / 2;
			a[now] = a[now] + (l + 1) / 2;
		}

		System.out.print(a[0]);
		for (int j = 1; j < n; j++) {
			System.out.print(" " + a[j]);
		}
		System.out.println();
	}
}

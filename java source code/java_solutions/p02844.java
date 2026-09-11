import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int n = Integer.parseInt(br.readLine());

		String str[] = br.readLine().split("");

		int ans = 0;

		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 9; j++) {
				for (int k = 0; k <= 9; k++) {
					boolean flag1 = false;
					boolean flag2 = false;
					boolean flag3 = false;

					for (int l = 0; l < str.length; l++) {
						int num = Integer.parseInt(str[l]);
						if (num == k && flag2) {
							flag3 = true;
						} else if (num == j && flag1) {
							flag2 = true;
						} else if (num == i) {
							flag1 = true;
						}
					}

					if (flag1 && flag2 && flag3) {
						ans++;
					}
				}
			}
		}
		System.out.println(ans);
	}
}

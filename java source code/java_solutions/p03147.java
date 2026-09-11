import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int h[] = new int[n];
		for (int i = 0; i < n; i++) {
			h[i] = sc.nextInt();
		}
		int ans = 0;

		for (int i = 0; i < n; i++) {
			while (h[i] > 1) {
				ans++;
				h[i]--;
				for (int i2 = i + 1; i2 < h.length; i2++) {
					if (h[i2] > 1) {
						h[i2]--;
					} else {
						break;
					}
				}
			}
		}

		boolean start = false;
		boolean zero = false;
		boolean none = true;
		for (int i = 0; i < n; i++) {
			if (h[i] != 0) {
				none = false;
				start = true;
				if (zero == true) {
					ans++;
					zero = false;
				}
			}
			if (h[i] == 0 && none == false) {
				zero = true;
			}
		}

		if (none == false)
			ans++;

		System.out.println(ans);

	}
}

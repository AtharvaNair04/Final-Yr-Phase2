import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static int mobile[][];
	static int n;
	static long weight[];

	public static long f(int id) {
		if (0 < weight[id]) {
			return weight[id];
		}

		long le = 1;
		long ri = 1;
		if (mobile[id][2] != 0) {
			le = f(mobile[id][2] - 1);
		}
		if (mobile[id][3] != 0) {
			ri = f(mobile[id][3] - 1);
		}

		long kle = 1;
		long kri = 1;
		while (true) {
			if (le * kle * mobile[id][0] == ri * kri * mobile[id][1]) {
				break;
			} else if (le * kle * mobile[id][0] < ri * kri * mobile[id][1]) {
				kle++;
			} else {
				kri++;
			}
		}

		return weight[id] = le * kle + ri * kri;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			mobile = new int[n][4];
			weight = new long[n];
			for (int i = 0; i < n; i++) {
				mobile[i][0] = sc.nextInt();
				mobile[i][1] = sc.nextInt();
				mobile[i][2] = sc.nextInt();
				mobile[i][3] = sc.nextInt();
			}
			Arrays.fill(weight, -1);
			for (int i = 0; i < n; i++) {
				f(i);
			}

			Arrays.sort(weight);
			System.out.println(weight[n - 1]);
		}
	}
}

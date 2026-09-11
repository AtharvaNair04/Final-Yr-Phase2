import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();
		int[] d = new int[n];
		for (int i = 0; i < n; i++) {
			d[i] = sc.nextInt();
		}

		int m = sc.nextInt();
		int[] t = new int[m];
		for (int i = 0; i < m; i++) {
			t[i] = sc.nextInt();
		}

		/*
		 * int i, j = 0; if (n >= m) { for (i = 0; i < m; i++) {
		 * 
		 * for (j = 0; j < n; j++) { if (d[j] != -1 && d[j] == t[i]) { d[j] = -1; break;
		 * } } if (j >= n) { break; } }
		 * 
		 * if (j >= n) { System.out.println("NO"); } else { System.out.println("YES"); }
		 * } else { System.out.println("NO"); }
		 */

		Map<Integer, Integer> df = new HashMap<Integer, Integer>();
		Map<Integer, Integer> tf = new HashMap<Integer, Integer>();

		for (int x : d) {
			if (df.containsKey(x)) {
				df.put(x, df.get(x) + 1);
			} else {
				df.put(x, 0);
			}
		}

		for (int x : t) {
			if (tf.containsKey(x)) {
				tf.put(x, tf.get(x) + 1);
			} else {
				tf.put(x, 0);
			}
		}

		if (df.size() >= tf.size()) {
			for (int x : t) {
				if (!df.containsKey(x) ||  tf.get(x) > df.get(x)) {
					System.out.println("NO");
					System.exit(0);
				}
			}
		} else {
			System.out.println("NO");
			System.exit(0);
		}

		System.out.println("YES");
	}
}

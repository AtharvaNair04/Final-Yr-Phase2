import java.util.Scanner;
import java.util.TreeSet;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, k, sum, diff;
		TreeSet<Integer> ts = new TreeSet<Integer>();
		TreeSet<Integer> nts = new TreeSet<Integer>();

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}

			sum = 0;
			ts.clear();
			nts.clear();
			for (int i = 0; i < n; i++) {
				k = sc.nextInt();
				sum += k;
				nts.add(k);
				for (Integer x: ts) {
					nts.add(x + k);
				}
				for (Integer x: nts) {
					ts.add(x);
				}
			}

			diff = 0;
			while (true) {
				if (ts.contains(sum / 2 - diff)) {
					System.out.println(sum - 2 * (sum / 2 - diff));
					break;
				}
				diff++;
			}
		}
	}
}

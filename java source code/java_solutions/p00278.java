import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		n = scanner.nextInt();
		int q = scanner.nextInt();
		int[] st = new int[n];
		for (int i = 0; i < n; i++)
			st[i] = scanner.nextInt();
		int[] s = Arrays.copyOf(st, n);
		Arrays.sort(s);
		List<Integer> list = new LinkedList<Integer>();
		while (q-- > 0) {
			char query = scanner.next().charAt(0);
			int x = scanner.nextInt();
			if (query == 'A') {
				list.add(st[x - 1]);
				Collections.sort(list);
			} else if (query == 'R') {
				list.remove(Integer.valueOf(st[x - 1]));
			} else {
				int r = INF;
				int l = 0;
				while (r != l) {
					int mid = (r + l) / 2;
					int pre = 0;
					int BAN = 0;
					for (int i : list) {
						int p = lower(s, i - mid);
						BAN += Math.max(p - pre, 0);
						pre = upper(s, i);
					}
					BAN += n - pre;
					if (BAN <= x)
						r = mid;
					else
						l = mid + 1;
				}
				System.out.println(l != INF ? l : "NA");
			}
		}
	}

	private int lower(int[] s, int i) {
		int p = Arrays.binarySearch(s, i);
		if (p < 0) {
			p = p * -1 - 1;
		} else {
			for (int j = p - 1; j >= 0; j--) {
				if (s[j] != i)
					break;
				p--;
			}
		}
		return p;
	}

	private int upper(int[] s, int i) {
		int pre = Arrays.binarySearch(s, i);
		for (int j = pre + 1; j < n; j++) {
			if (s[j] != i)
				break;
			pre++;
		}

		return pre + 1;
	}

	int INF = 1000000001;
	int n;
}

import java.util.Arrays;
import java.util.Scanner;

public class Main {
	class Line implements Comparable<Line>{
		int h, l, r;

		Line(int h, int l, int r) {
			this.h = h;
			this.l = l;
			this.r = r;
		}

		@Override
		public int compareTo(Line o) {
			if (this.h != o.h) return o.h - this.h;
			if (this.l != o.l) return this.l - o.l;
			return this.r - o.r;
		}
	}

	void run() {
		Scanner sc = new Scanner(System.in);

		while (true) {
			int n = sc.nextInt();
			int m = sc.nextInt();
			int k = sc.nextInt();
			if ((n | m | k) == 0) break;

			int[] a = new int[n];
			Line[] l = new Line[m];
			for (int i = 0; i < n; i++) {
				a[i] = i + 1;
			}
			for (int i = 0; i < m; i++) {
				l[i] = new Line(sc.nextInt(), sc.nextInt() - 1, sc.nextInt() - 1);
			}
			Arrays.sort(l);

			for (int i = 0; i < m; i++) {
				int id1 = l[i].l;
				int id2 = l[i].r;
				int tmp = a[id1];
				a[id1] = a[id2];
				a[id2] = tmp;
			}

			for (int i = 0; i < n; i++) {
				if (a[i] == k) {
					System.out.println(++i);
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

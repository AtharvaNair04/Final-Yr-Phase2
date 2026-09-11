import java.lang.*;
import java.math.*;
import java.util.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	class a {
		LinkedList<Integer> list = new LinkedList<Integer>();

		a() {
			list = new LinkedList<Integer>();
		}
	}

	public void run() {
		for (;;) {
			int w = sc.nextInt();
			int h = sc.nextInt();
			if ((w | h) == 0) {
				break;
			}

			int[][] map = new int[w * h][4];
			a[] l = new a[h * w];
			int j;
			for (int z = 0; z < h * w; z++) {
				l[z] = new a();
			}

			for (j = 0; j < h - 1; j++) {
				for (int i = 0; i < w - 1; i++) {
					int d = sc.nextInt();
					if (d == 0) {
						l[i + 1 + j * w].list.add(i + j * w);
						l[i + j * w].list.add(i + 1 + j * w);
					}
				}
				for (int i = 0; i < w; i++) {
					int d = sc.nextInt();
					if (d == 0) {
						l[i + w + j * w].list.add(i + j * w);
						l[i + j * w].list.add(i + w + j * w);
					}
				}
			}
			for (int i = 0; i < w - 1; i++) {
				int d = sc.nextInt();
				if (d == 0) {
					l[i + 1 + j * w].list.add(i + j * w);
					l[i + j * w].list.add(i + 1 + j * w);
				}
			}

			boolean[] visited = new boolean[w * h];
			LinkedList<Integer> Q = new LinkedList<Integer>();
			Q.add(0);
			int now = 1;
			int depth[] = new int[h * w];
			boolean flag = true;
			int res = 0;
			for (; flag;) {
				LinkedList<Integer> nextQ = new LinkedList<Integer>();
				if (Q.isEmpty()) {
					res = 0;
					flag = false;
					break;
				}
				for (int k : Q) {
					depth[k] = now;
					visited[k] = true;
					for (int z : l[k].list) {
						if (!visited[z]) {
							if (!nextQ.contains(z)) {
								nextQ.add(z);
							}
						}
					}
					if (k == w * h - 1) {
						flag = false;
						res = now;
						break;
					}
				}
				Q = nextQ;
				now++;
			}
			System.out.println(res);
		}
	}

	public static void main(String args[]) {
		Main m = new Main();
		m.run();
	}
}

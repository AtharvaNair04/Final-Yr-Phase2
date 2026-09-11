import java.util.Scanner;
import java.util.TreeMap;

public class Main {
	static class Page {
		String name;
		int l[], r[], t[], b[];
		String button[];
		int num, k;

		Page(String name, int k, int num) {
			this.name = name;
			l = new int[k];
			r = new int[k];
			t = new int[k];
			b = new int[k];
			button = new String[k];
			this.num = num;
			this.k = k;
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		Page p[];
		int buf[], index, end;
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			sc.nextInt();
			sc.nextInt();

			p = new Page[n];
			for (int i = 0; i < n; i++) {
				String name = sc.next();
				int k = sc.nextInt();
				p[i] = new Page(name, k, i);
				tm.put(p[i].name, p[i].num);
				for (int j = 0; j < p[i].k; j++) {
					p[i].l[j] = sc.nextInt();
					p[i].t[j] = sc.nextInt();
					p[i].r[j] = sc.nextInt();
					p[i].b[j] = sc.nextInt();
					p[i].button[j] = sc.next();
				}
			}

			m = sc.nextInt();
			buf = new int[100000];
			index = 0;
			end = 1;
			for (int i = 0; i < m; i++) {
				String order = sc.next();
				Page now = p[buf[index]];
				if (order.equals("click")) {
					int x = sc.nextInt();
					int y = sc.nextInt();
					for (int j = 0; j < now.k; j++) {
						if (now.l[j] <= x && x <= now.r[j] && now.t[j] <= y && y <= now.b[j]) {
							index++;
							buf[index] = tm.get(now.button[j]);
							end = index + 1;
							break;
						}
					}
				} else if (order.equals("show")) {
					System.out.println(now.name);
				} else if (order.equals("back")) {
					index = Math.max(index - 1, 0);
				} else if (order.equals("forward")) {
					index = Math.min(index + 1, end - 1);
				}
			}
		}
	}
}

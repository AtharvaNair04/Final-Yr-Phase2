import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	Scanner sc = new Scanner(System.in);

	void run() {
		L: for (;;) {
			int n = sc.nextInt();
			if (n == 0)
				return;
			String[][] sed = new String[n][2];
			for (int i = 0; i < n; i++) {
				sed[i][0] = sc.next();
				sed[i][1] = sc.next();
			}
			String from = sc.next();
			String to = sc.next();
			LinkedList<String> que = new LinkedList<String>();
			HashSet<String> set = new HashSet<String>();
			que.add(from);
			set.add(from);
			int cnt = 0;
			while (!que.isEmpty()) {
				int size = que.size();
				for (int i = 0; i < size; i++) {
					String s = que.poll();
					if (s.equals(to)) {
						System.out.println(cnt);
						continue L;
					}
					for (int j = 0; j < n; j++) {
						String m = s.replaceAll(sed[j][0], sed[j][1]);
						if (m.length() <= to.length() && !set.contains(m)) {
							que.add(m);
							set.add(m);
						}
					}
				}
				cnt++;
			}
			System.out.println(-1);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

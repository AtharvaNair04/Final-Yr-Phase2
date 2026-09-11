import java.util.HashMap;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			map.clear();
			while (n-- != 0) {
				String name = sc.next();
				int cost = sc.nextInt();
				map.put(name,  cost);
			}
			m = sc.nextInt();
			String alc[] = new String[m];
			int num[] = new int[m];
			String list[][] = new String[m][110];
			for (int i = 0; i < m; i++) {
				alc[i] = sc.next();
				num[i] = sc.nextInt();
				for (int j = 0; j < num[i]; j++) {
					list[i][j] = sc.next();
				}
			}

			boolean flag = true;
			while (flag) {
				flag = false;
				for (int i = 0; i < m; i++) {
					int sum = 0;
					for (int j = 0; j < num[i]; j++) {
						if (map.containsKey(list[i][j])) {
							sum += map.get(list[i][j]);
						}
					}
					if (map.get(alc[i]) == null || sum < map.get(alc[i])) {
						map.put(alc[i], sum);
						flag = true;
					}
				}
			}

			System.out.println(map.get(sc.next()));
		}
	}
}

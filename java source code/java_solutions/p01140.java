import java.util.Scanner;
import java.util.TreeMap;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		int w[], h[];

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}

			h = new int[n + 1];
			w = new int[m + 1];
			
			for (int i = 1; i < n + 1; i++) {
				h[i] = h[i - 1] + sc.nextInt();
			}
			for (int i = 1; i < m + 1; i++) {
				w[i] = w[i - 1] + sc.nextInt();
			}
			
			TreeMap<Integer, Integer> t = new TreeMap<Integer, Integer>();
			for (int i = 0; i < n + 1; i++) {
				 for (int j = i + 1; j < n + 1; j++) {
					 Integer v = t.get(h[j] - h[i]);
					 t.put(h[j] - h[i], v == null ? 1 : v + 1);
				 }
			}
			
			int cnt = 0;
			for (int i = 0; i < m + 1; i++) {
				for (int j = i + 1; j < m + 1; j++) {
					Integer v = t.get(w[j] - w[i]);
					cnt += v == null ? 0 : v;
				}
			}
			
			System.out.println(cnt);
		}
	}
}

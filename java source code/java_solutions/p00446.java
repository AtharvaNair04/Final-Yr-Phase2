import java.util.*;

public class Main {
	private int[] p1, p2;

	public static void main(String[] args) {
		new Main().run();
	}
	
	private void run() {
		Scanner scan = new Scanner(System.in);
		while (true) {
			int n = scan.nextInt();
			if (n == 0) break;
			
			p1 = new int[n];
			p2 = new int[n];
			for (int i = 0; i < n; i++) {
				p1[i] = scan.nextInt();
			}
			
			Arrays.sort(p1);
			
			int j = 0;
			int p = 0;
			for (int i = 0; i < 2 * n && p < n; i++) {
				
				if (j >= n || p1[j] != i+1) {
					p2[p] = i+1;
					p++;
				}else {
					j++;
				}
			}
			
			//debug();
			
			int card = 0, cnt1 = 0, cnt2 = 0;
			while (true) {
				boolean flag = false;
				for (int i = 0; i < n; i++) {
					if (p1[i] > card) {
						card = p1[i];
						p1[i] = -1;
						flag = true;
						//debug();
						break;
					}
				}
				if (!flag) {
					card = 0;
				}
				for (int i = 0; i < n; i++) {
					if (p1[i] != -1) cnt1++;
					if (p2[i] != -1) cnt2++;
				}
				
				if (cnt1 == 0) break;
				else cnt1 = cnt2 = 0;
				
				flag = false;
				for (int i = 0; i < n; i++) {
					if (p2[i] > card) {
						card = p2[i];
						p2[i] = -1;
						flag = true;
						//debug();
						break;
					}
				}
				if (!flag) {
					card = 0;
				}
				
				for (int i = 0; i < n; i++) {
					if (p1[i] != -1) cnt1++;
					if (p2[i] != -1) cnt2++;
				}
				
				if (cnt2 == 0) break;
				else cnt1 = cnt2 = 0;
			}
			
			System.out.println(cnt2);
			System.out.println(cnt1);
		}
	}
	
	private void debug() {
		System.out.println(Arrays.toString(p1));
		System.out.println(Arrays.toString(p2));
	}
}

import java.util.*;
import java.lang.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	class arrow {
		LinkedList<Integer> from = new LinkedList<Integer>();
	}

	void run() {
		for (;;) {
			int n = sc.nextInt();
			int a = sc.nextInt();
			int s = sc.nextInt();
			int g = sc.nextInt();
			if ((n | a | s | g) == 0) {
				break;
			}
			int[] x = new int[a];
			int[] y = new int[a];
			String[] lab = new String[a];
			for (int i = 0; i < a; i++) {
				x[i] = sc.nextInt();
				y[i] = sc.nextInt();
				lab[i] = sc.next();
			}
			String[][] dp = new String[n][n * 6+241];
			boolean[][] visited = new boolean[n][n * 6+241];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n * 6; j++) {
					dp[i][j] = "";
				}
			}
			visited[s][0] = true;
			for (int i = 0; i < n * 6+241; i++) {
				for (int st = 0; st < n; st++) {
					for (int k = 0; k < a; k++) {
						if (st == x[k]) {
							if (i - lab[k].length() < 0)
								continue;
							if (!visited[x[k]][i - lab[k].length()]) {
								continue;
							}
							if(!visited[y[k]][i]){
								dp[y[k]][i] = dp[x[k]][i - lab[k].length()]
														+ lab[k];	
								visited[y[k]][i] = true;
							}
							if (dp[y[k]][i].compareTo(dp[x[k]][i
									- lab[k].length()]
									+ lab[k]) > 0) {
								dp[y[k]][i] = dp[x[k]][i - lab[k].length()]
										+ lab[k];
							}
						}
					}
				}
			}
			String min = "";
			boolean f = false;
			for (int j = 0; j < n * 6+241; j++) {
				if (visited[g][j]) {
					if (!f) {
						min = dp[g][j];
						f = true;
					}
					if (min.compareTo(dp[g][j]) > 0) {
						min = dp[g][j] ;

						if(j >= n*6){
							f = false;
							break;
						}
					}
				}
			}
			if (!f) {
				System.out.println("NO");
			} else {
				System.out.println(min);
			}
		}

	}

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}
}

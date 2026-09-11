import java.util.Scanner;

public class Main {

	public static int[] uf;
	public static int[] dp;

	public static int Root(int n) {
		int tmp = n;
		while(uf[tmp] != tmp) {
			tmp = uf[tmp];
		}
		return tmp;
	}

	public static void Union(int m, int n) {
		if(Root(m) != Root(n)) {
			int x = Root(m);
			int y = Root(n);
			if(dp[x] < dp[y]) {
				uf[x] = y;
			} else if(dp[x] > dp[y]) {
				uf[y] = x;
			} else {
				uf[x] = y;
				dp[y]++;
			}
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		int[] p = new int[n];
		for(int i = 0; i < n; i++) {
			p[i] = sc.nextInt() - 1;
		}
		uf = new int[n];
		dp = new int[n];
		for(int i = 0; i < n; i++) {
			uf[i] = i;
			dp[i] = 0;
		}
		for(int i = 0; i < m; i++) {
			int x = sc.nextInt() - 1;
			int y = sc.nextInt() - 1;
			Union(x, y);
		}
		int ans = 0;
		for(int i = 0; i < n; i++) {
			if(Root(i) == Root(p[i]))
				ans++;
		}
		System.out.println(ans);
	}

}
